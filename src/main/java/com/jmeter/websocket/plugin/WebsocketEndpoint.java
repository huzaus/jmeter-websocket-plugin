package com.jmeter.websocket.plugin;

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

import static com.google.common.base.MoreObjects.toStringHelper;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class WebsocketEndpoint {

    private static final Logger log = LoggingManager.getLoggerForClass();

    public final StringBuilder stringBuilder = new StringBuilder();

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
    public void OnWebSocketMessage(Session session, String text) {
        log.debug("OnWebSocketMessage()" +
                " session: " + session +
                " text:" + text);
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

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("stringBuilder", stringBuilder)
                .toString();
    }
}
