package com.jmeter.websocket.plugin.endpoint.jetty;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.jmeter.websocket.plugin.endpoint.WebsocketSession;
import com.jmeter.websocket.plugin.endpoint.jetty.converters.CookieManagerToWebSocketClientConverter;
import com.jmeter.websocket.plugin.endpoint.jetty.converters.HeadersToClientUpgradeRequestConverter;
import com.jmeter.websocket.plugin.endpoint.jetty.converters.SampleResultToUpgradeListenerConverter;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketFrame;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.websocket.client.io.UpgradeListener;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.ByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Suppliers.memoize;
import static java.lang.System.lineSeparator;
import static java.nio.ByteBuffer.wrap;
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

    private Function<CookieManager, WebSocketClient> cookieManagerToWebSocketClientConverter = new CookieManagerToWebSocketClientConverter();
    private Function<Map<String, List<String>>, ClientUpgradeRequest> headersToClientUpgradeRequestConverter = new HeadersToClientUpgradeRequestConverter();
    private Function<SampleResult, UpgradeListener> sampleResultToUpgradeListenerConverter = new SampleResultToUpgradeListenerConverter();

    public JettyWebsocketEndpoint(Path file) {
        checkNotNull(file, "File should be set");
        this.file = file;
    }

    @Override
    public void connect(URI uri, CookieManager cookieManager, Map<String, List<String>> headers, SampleResult result, long timeOut) throws Exception {
        checkArgument(session == null || !session.isOpen(), "Session is already open");
        log.info("Opening " + uri + " connection." +
                " CookieManager: " + cookieManager +
                " Headers: " + headers);
        WebSocketClient webSocketClient = cookieManagerToWebSocketClientConverter.apply(cookieManager);
        webSocketClient.start();
        session = webSocketClient
                .connect(this,
                        uri,
                        headersToClientUpgradeRequestConverter.apply(headers),
                        sampleResultToUpgradeListenerConverter.apply(result))
                .get(timeOut, MILLISECONDS);
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
}
