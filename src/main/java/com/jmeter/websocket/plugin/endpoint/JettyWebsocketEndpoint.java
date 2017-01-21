package com.jmeter.websocket.plugin.endpoint;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.jmeter.websocket.plugin.configurations.WebsocketSession;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketFrame;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.IOException;
import java.net.CookieStore;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.ByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Suppliers.memoize;
import static java.lang.System.lineSeparator;
import static java.nio.ByteBuffer.wrap;
import static java.nio.file.Files.isWritable;
import static java.nio.file.Files.newByteChannel;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class JettyWebsocketEndpoint implements WebsocketSession {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final Set<StandardOpenOption> OPEN_OPTIONS = ImmutableSet.of(APPEND, CREATE);

    private final Path file;
    private Session session;

    public JettyWebsocketEndpoint(Path file) {
        checkNotNull(file, "File should be set");
        checkArgument(isWritable(file), file + " + file should be writable");
        this.file = file;
    }

    @Override
    public void connect(URI uri, CookieStore cookies, ClientUpgradeRequest upgradeRequest, SampleResult result, long timeOut) throws Exception {
        checkArgument(session == null || !session.isOpen(), "Session is already open");
        log.info("Opening " + uri + " connection." +
                " Cookies: " + cookies +
                " Request: " + upgradeRequest);
        WebSocketClient webSocketClient = webSocketClient(cookies);
        webSocketClient.start();

        Future<Session> promise = webSocketClient.connect(this, uri, upgradeRequest, new WebsocketUpgradeListener(result));

        session = promise.get(timeOut, MILLISECONDS);
    }

    @Override
    public void sendMessage(String message) throws IOException {
        checkNotNull(session, "Session is null");
        checkArgument(session.isOpen(), "Session is not open");
        log.debug("sendMessage() message: " + message);
        session.getRemote().sendString(message);
        write(message);
    }

    @Override
    public void onReceiveMessage(String message) throws IOException {
        log.debug("onReceiveMessage() message: " + message);
    }

    @OnWebSocketFrame
    public void onWebSocketFrame(Frame frame) throws IOException {
        log.debug("onWebSocketFrame()" +
                " session: " + session +
                " frame:" + frame);
        write(frame.toString());

    }

    @OnWebSocketClose
    public void onWebSocketClose(Session session, int closeCode, String closeReason) {
        log.debug("onWebSocketClose()" +
                " session: " + session +
                " closeCode: " + closeCode +
                " closeReason: " + closeReason);
        ByteChannel byteChannel = byteChannel().get();
        if (byteChannel != null && byteChannel.isOpen()) {
            try {
                byteChannel.close();
            } catch (IOException e) {
                log.error("Exception thrown: " + e);
            }
        }
    }

    private void write(String line) {
        ByteChannel byteChannel = byteChannel().get();
        if (byteChannel != null) {
            byte data[] = (line + lineSeparator()).getBytes();
            try {
                byteChannel.write(wrap(data));
            } catch (IOException e) {
                log.error("Exception thrown: " + e);
            }
        } else {
            log.info(line);
        }
    }

    private Supplier<ByteChannel> byteChannel() {
        return memoize(new Supplier<ByteChannel>() {
            @Override
            public ByteChannel get() {
                try {
                    return newByteChannel(file, OPEN_OPTIONS);
                } catch (IOException e) {
                    log.error("Exception thrown: " + e);
                    return null;
                }
            }
        });
    }

    private WebSocketClient webSocketClient(CookieStore cookies) throws URISyntaxException {
        WebSocketClient webSocketClient = new WebSocketClient(sslContextFactory(), Executors.newCachedThreadPool());
        webSocketClient.setCookieStore(cookies);
        return webSocketClient;
    }

    private SslContextFactory sslContextFactory() {
        return new SslContextFactory(true);
    }
}
