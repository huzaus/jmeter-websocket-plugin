package com.jmeter.websocket.plugin.endpoint.jetty.session;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.jmeter.websocket.plugin.endpoint.SessionsManager;
import org.eclipse.jetty.websocket.api.Session;

import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Optional.fromNullable;

public class JettySessionsManager implements SessionsManager<String, Session> {

    private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    @Override
    public boolean hasOpenSession(String id) {
        return fromNullable(sessions.get(id))
                .transform(new Function<Session, Boolean>() {
                    @Override
                    public Boolean apply(Session session) {
                        return session.isOpen();
                    }
                })
                .or(false);
    }

    @Override
    public Session getOpenSession(String id) {
        Session session = sessions.get(id);
        if (session != null && session.isOpen()) {
            return session;
        } else {
            return null;
        }
    }

    @Override
    public void registerSession(String id, Session session) {
        sessions.put(id, session);
    }

    @Override
    public void closeSessions() {
        for (Session session : sessions.values()) {
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
