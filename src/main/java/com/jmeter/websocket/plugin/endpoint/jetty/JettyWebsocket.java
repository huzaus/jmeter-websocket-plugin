package com.jmeter.websocket.plugin.endpoint.jetty;

import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketMessageConsumer;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketFrame;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.extensions.Frame;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

@WebSocket(maxTextMessageSize = 128 * 1024)
public class JettyWebsocket {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private final String sessionId;

    private final Collection<WebsocketMessageConsumer> websocketMessageConsumers = new ArrayList<>();

    public JettyWebsocket(String sessionId) {
        this.sessionId = sessionId;
    }

    @OnWebSocketConnect
    public void onWebSocketConnect(Session session) {
        log.info("onWebSocketConnect()" +
                " session: " + session);
    }

    @OnWebSocketClose
    public void onWebSocketClose(Session session, int closeCode, String closeReason) {
        log.info("onWebSocketClose()" +
                " session: " + session +
                " closeCode: " + closeCode +
                " closeReason: " + closeReason);
    }

    @OnWebSocketMessage
    public void onWebSocketMessage(Session session, String message) {
        log.debug("OnWebSocketMessage()" +
                " session: " + session +
                " message: " + message);
        checkNotNull(session);
        for (WebsocketMessageConsumer consumer : websocketMessageConsumers) {
            consumer.onMessageReceive(sessionId, message);
        }
    }

    @OnWebSocketError
    public void onWebSocketError(Session session, Throwable cause) {
        log.error("OnWebSocketError() session: " + session, cause);
    }

    @OnWebSocketFrame
    public void onWebSocketFrame(Session session, Frame frame) {
        log.debug("OnWebSocketFrame()" +
                " session: " + session +
                " frame:" + frame);
    }

    public void registerWebsocketMessageConsumer(WebsocketMessageConsumer consumer) {
        websocketMessageConsumers.add(consumer);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("websocketMessageConsumers", websocketMessageConsumers)
                .toString();
    }
}
