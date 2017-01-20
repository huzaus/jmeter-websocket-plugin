package com.jmeter.websocket.plugin.configurations;

import com.jmeter.websocket.plugin.endpoint.WebsocketEndpoint;
import com.jmeter.websocket.plugin.endpoint.WebsocketUpgradeListener;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.IOException;
import java.net.CookieStore;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class JettyWebsocketSession implements WebsocketSession {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private Session session;
    private WebsocketEndpoint websocketEndpoint;

    @Override
    public void sendMessage(String message) throws IOException {
        checkNotNull(session, "Session is null");
        checkArgument(session.isOpen(), "Session is not open");
        session.getRemote().sendString(message);
    }

    @Override
    public void connect(URI uri, CookieStore cookies, ClientUpgradeRequest upgradeRequest, SampleResult result, long timeOut) throws Exception {
        checkArgument(session == null || !session.isOpen(), "Session is already open");
        log.info("Opening " + uri + " connection." +
                " Cookies: " + cookies +
                " Request: " + upgradeRequest);
        WebSocketClient webSocketClient = webSocketClient(cookies);
        webSocketClient.start();
        websocketEndpoint = new WebsocketEndpoint();

        Future<Session> promise = webSocketClient.connect(websocketEndpoint, uri, upgradeRequest, new WebsocketUpgradeListener(result));

        session = promise.get(timeOut, MILLISECONDS);
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
