package com.jmeter.websocket.plugin.endpoint.jetty.session;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.jmeter.websocket.plugin.endpoint.SessionsManager;
import com.jmeter.websocket.plugin.endpoint.WebsocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Optional.fromNullable;

public class JettySessionsManager implements SessionsManager<String> {

    private final ConcurrentHashMap<String, WebsocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public boolean hasOpenSession(String id) {
        return fromNullable(sessions.get(id))
                .transform(new Function<WebsocketSession, Boolean>() {
                    @Override
                    public Boolean apply(WebsocketSession session) {
                        return session.isOpen();
                    }
                })
                .or(false);
    }

    @Override
    public WebsocketSession getOpenSession(String id) {
        if (hasOpenSession(id)) {
            return sessions.get(id);
        }
        return null;
    }

    @Override
    public void registerSession(String id, WebsocketSession session) {
        sessions.put(id, session);
    }

    @Override
    public void closeSessions() throws IOException {
        for (WebsocketSession session : sessions.values()) {
            if (session.isOpen()) {
                session.close();
            }
        }
        sessions.clear();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sessions", sessions)
                .toString();
    }
}
