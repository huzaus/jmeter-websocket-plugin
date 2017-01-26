package com.jmeter.websocket.plugin.endpoint;

import java.net.URI;

public interface SessionsManager<S> {

    boolean hasOpenSession(URI uri);

    S getOpenSession(URI uri);

    void registerSession(URI uri, S session);

    void closeSessions();
}
