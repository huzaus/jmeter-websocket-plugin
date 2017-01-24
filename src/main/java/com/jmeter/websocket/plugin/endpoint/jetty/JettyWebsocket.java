package com.jmeter.websocket.plugin.endpoint.jetty;

import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketMessageProcessor;
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
import static java.lang.Integer.toHexString;

@WebSocket(maxTextMessageSize = 128 * 1024)
public class JettyWebsocket {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private Collection<WebsocketMessageProcessor> websocketMessageProcessors = new ArrayList<>();

    @OnWebSocketConnect
    public void onWebSocketConnect(Session session) {
        log.debug("onWebSocketConnect()" +
                " session: " + session);
    }

    @OnWebSocketClose
    public void onWebSocketClose(Session session, int closeCode, String closeReason) {
        log.debug("onWebSocketClose()" +
                " session: " + session +
                " closeCode: " + closeCode +
                " closeReason: " + closeReason);
    }

    @OnWebSocketMessage
    public void OnWebSocketMessage(Session session, String message) {
        log.debug("OnWebSocketMessage()" +
                " session: " + session +
                " message: " + message);
        for (WebsocketMessageProcessor processor : websocketMessageProcessors) {
            processor.onMessageReceive(toHexString(session.hashCode()), message);
        }
    }

    @OnWebSocketError
    public void OnWebSocketError(Session session, Throwable cause) {
        log.error("OnWebSocketError() session: " + session, cause);
    }

    @OnWebSocketFrame
    public void OnWebSocketFrame(Session session, Frame frame) {
        log.debug("OnWebSocketFrame()" +
                " session: " + session +
                " frame:" + frame);
    }

    public void registerWebsocketMessageConsumer(WebsocketMessageProcessor processor) {
        websocketMessageProcessors.add(processor);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .toString();
    }
}
