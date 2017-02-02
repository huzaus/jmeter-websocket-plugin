package com.jmeter.websocket.plugin.endpoint;


import java.io.IOException;

public interface SessionsManager<K> {

    boolean hasOpenSession(K key);

    WebsocketSession getOpenSession(K key);

    void registerSession(K key, WebsocketSession session);

    void closeSessions() throws IOException;
}
