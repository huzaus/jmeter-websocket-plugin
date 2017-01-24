package com.jmeter.websocket.plugin.endpoint.jetty;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.jmeter.websocket.plugin.endpoint.WebsocketClient;
import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketMessageProcessor;
import com.jmeter.websocket.plugin.endpoint.jetty.converters.CookieManagerToCookieStoreConverter;
import com.jmeter.websocket.plugin.endpoint.jetty.converters.HeadersToClientUpgradeRequestConverter;
import com.jmeter.websocket.plugin.endpoint.jetty.converters.SampleResultToUpgradeListenerConverter;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.websocket.client.io.UpgradeListener;

import java.io.IOException;
import java.net.CookieStore;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Suppliers.memoize;
import static java.lang.Integer.toHexString;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class JettyWebsocketEndpoint implements WebsocketClient {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final Supplier<WebSocketClient> webSocketClientSupplier = webSocketClientSupplier();

    private Function<CookieManager, CookieStore> cookieManagerToCookieStoreConverter = new CookieManagerToCookieStoreConverter();
    private Function<Map<String, List<String>>, ClientUpgradeRequest> headersToClientUpgradeRequestConverter = new HeadersToClientUpgradeRequestConverter();
    private Function<SampleResult, UpgradeListener> sampleResultToUpgradeListenerConverter = new SampleResultToUpgradeListenerConverter();

    private Collection<WebsocketMessageProcessor> websocketMessageProcessors = new ArrayList<>();
    private ConcurrentHashMap<URI, Session> sessions = new ConcurrentHashMap<>();

    @Override
    public void connect(URI uri, CookieManager cookieManager, Map<String, List<String>> headers, SampleResult result, long timeOut) throws Exception {
        Session session = sessions.get(uri);
        checkArgument(session == null || !session.isOpen(), "Session is already open");
        log.info("Opening " + uri + " connection." +
                " CookieManager: " + cookieManager +
                " Headers: " + headers);
        WebSocketClient webSocketClient = webSocketClientSupplier.get();
        Future<Session> promise;
        synchronized (webSocketClient) {
            webSocketClient.setCookieStore(cookieManagerToCookieStoreConverter.apply(cookieManager));
            promise = webSocketClient
                    .connect(jettyWebsocket(),
                            uri,
                            headersToClientUpgradeRequestConverter.apply(headers),
                            sampleResultToUpgradeListenerConverter.apply(result));
        }
        sessions.put(uri, promise.get(timeOut, MILLISECONDS));
        log.info("Connected to: " + uri + ". Sessions: " + sessions);
    }

    @Override
    public void sendMessage(URI uri, String message) throws IOException {
        Session session = sessions.get(uri);
        checkNotNull(session, "Session is null for " + uri);
        checkArgument(session.isOpen(), "Session is not open for " + uri + ". Session: " + session);
        log.info("sendMessage() message: " + message + " to " + uri + " using session:" + session);
        session.getRemote().sendString(message);
        for (WebsocketMessageProcessor processor : websocketMessageProcessors) {
            processor.onMessageSend(toHexString(session.hashCode()), message);
        }
    }

    @Override
    public void registerWebsocketMessageConsumer(WebsocketMessageProcessor processor) {
        websocketMessageProcessors.add(processor);
    }

    @Override
    public void unregisterWebsocketMessageConsumer(WebsocketMessageProcessor processor) {
        websocketMessageProcessors.remove(processor);
    }

    @Override
    public void start() {
        try {
            log.info("Starting websocket client.");
            WebSocketClient webSocketClient = webSocketClientSupplier.get();
            synchronized (webSocketClient) {
                if (!webSocketClient.isStarted() || !webSocketClient.isStarting()) {
                    webSocketClient.start();
                }
            }
        } catch (Exception e) {
            log.error("Failed to start WebSocketClient: " + e);
        }
    }

    @Override
    public void stop() {
        try {
            log.info("Stopping websocket client.");
            WebSocketClient webSocketClient = webSocketClientSupplier.get();
            synchronized (webSocketClient) {
                for (Session session : sessions.values()) {
                    if (session.isOpen()) {
                        log.info("Closing " + toHexString(session.hashCode()) + " session.");
                        session.close();
                    }
                }
                sessions.clear();
                if (!webSocketClient.isStopped() || !webSocketClient.isStopping()) {
                    webSocketClient.stop();
                }
            }
        } catch (Exception e) {
            log.error("Failed to start WebSocketClient: " + e);
        }
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("hash", toHexString(hashCode()))
                .add("session", sessions)
                .toString();
    }

    private JettyWebsocket jettyWebsocket() {
        JettyWebsocket jettyWebsocket = new JettyWebsocket();
        for (WebsocketMessageProcessor processor : websocketMessageProcessors) {
            jettyWebsocket.registerWebsocketMessageConsumer(processor);
        }
        return jettyWebsocket;
    }

    private static Supplier<WebSocketClient> webSocketClientSupplier() {
        return memoize(new Supplier<WebSocketClient>() {
            @Override
            public WebSocketClient get() {
                WebSocketClient webSocketClient = new WebSocketClient(sslContextFactory(), newCachedThreadPool());
                return webSocketClient;
            }
        });
    }

    private static SslContextFactory sslContextFactory() {
        return new SslContextFactory(true);
    }
}
