package com.jmeter.websocket.plugin.configurations;

import com.jmeter.websocket.plugin.WebsocketEndpoint;
import com.google.common.base.Function;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.eclipse.jetty.websocket.api.Session;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Optional.fromNullable;

public class WebsocketSessionsManager extends ConfigTestElement {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private transient Session session;

    private transient WebsocketEndpoint websocketEndpoint;

    public static final String WEBSOCKET_MANAGER = "websocket_manager";

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        log.debug("setSession() session: " + session);
        this.session = session;
    }

    public WebsocketEndpoint getWebsocketEndpoint() {
        return websocketEndpoint;
    }

    public void setWebsocketEndpoint(WebsocketEndpoint websocketEndpoint) {
        log.debug("setWebsocketEndpoint() websocketEndpoint: " + websocketEndpoint);
        this.websocketEndpoint = websocketEndpoint;
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("session", fromNullable(session)
                        .transform(new Function<Session, String>() {
                            @Override
                            public String apply(Session input) {
                                return "is set";
                            }
                        })
                        .or("is not set"))
                .add("websocketEndpoint", websocketEndpoint)
                .toString();
    }
}
