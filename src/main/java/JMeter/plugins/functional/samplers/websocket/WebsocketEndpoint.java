package JMeter.plugins.functional.samplers.websocket;

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

@WebSocket(maxTextMessageSize = 64 * 1024)
public class WebsocketEndpoint {

    private static final Logger log = LoggingManager.getLoggerForClass();

    public final StringBuilder stringBuilder = new StringBuilder();

    @OnWebSocketConnect
    public void onWebSocketConnect(Session session) {
        log.debug("onWebSocketConnect{} session=" + session);
        stringBuilder.append("onWebSocketConnect{} session=" + session + "\n");
    }

    @OnWebSocketClose
    public void onWebSocketClose(Session session, int closeCode, String closeReason) {
        log.debug("onWebSocketClose{} session = " + session + ", closeCode=" + closeCode + ", closeReason=" + closeReason);
        stringBuilder.append("onWebSocketClose{} session = " + session + ", closeCode=" + closeCode + ", closeReason=" + closeReason + "\n");
    }

    @OnWebSocketMessage
    public void OnWebSocketMessage(Session session, String text) {
        log.debug("OnWebSocketMessage{}" + session + " text=" + text);
        stringBuilder.append("OnWebSocketMessage{} session=" + session + " text=" + text + "\n");
    }

    @OnWebSocketError
    public void OnWebSocketError(Session session, Throwable cause) {
        log.error("OnWebSocketError{} session=" + session, cause);
        stringBuilder.append("OnWebSocketError{} session=" + session + ", cause=" + cause + "\n");
    }

    @OnWebSocketFrame
    public void OnWebSocketFrame(Session session, Frame frame) {
        log.error("OnWebSocketFrame{} session=" + session + ", frame=" + frame);
        stringBuilder.append("OnWebSocketFrame{} session=" + session + ", frame=" + frame + "\n");
    }
}
