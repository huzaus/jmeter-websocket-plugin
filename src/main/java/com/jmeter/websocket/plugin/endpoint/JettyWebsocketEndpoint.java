package com.jmeter.websocket.plugin.endpoint;

import com.jmeter.websocket.plugin.configurations.WebsocketSession;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketFrame;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.IOException;
import java.net.CookieStore;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.nio.file.Files.isWritable;
import static java.nio.file.Files.write;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class JettyWebsocketEndpoint implements WebsocketSession {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private final Path file;
    private Session session;
    private WebsocketEndpoint websocketEndpoint;

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
        write(file, message.getBytes());
    }

    @Override
    public void onReceiveMessage(String message) throws IOException {
        log.debug("onReceiveMessage() message: " + message);
        write(file, message.getBytes());
    }

    @OnWebSocketFrame
    public void onWebSocketFrame(Frame frame) throws IOException {
        log.debug("onWebSocketFrame()" +
                " session: " + session +
                " frame:" + frame);
        onReceiveMessage(frame.toString());

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
