package com.jmeter.websocket.plugin.endpoint.jetty;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.jmeter.websocket.plugin.endpoint.SessionsManager;
import org.eclipse.jetty.websocket.api.Session;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Optional.fromNullable;

public class JettySessionManager implements SessionsManager<Session> {

    private final ConcurrentHashMap<URI, Session> sessions = new ConcurrentHashMap<>();

    @Override
    public boolean hasOpenSession(URI uri) {
        return fromNullable(sessions.get(uri))
                .transform(new Function<Session, Boolean>() {
                    @Override
                    public Boolean apply(Session session) {
                        return session.isOpen();
                    }
                })
                .or(false);
    }

    @Override
    public Session getOpenSession(URI uri) {
        Session session = sessions.get(uri);
        if (session != null && session.isOpen()) {
            return session;
        } else {
            return null;
        }
    }

    @Override
    public void registerSession(URI uri, Session session) {
        sessions.put(uri, session);
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
