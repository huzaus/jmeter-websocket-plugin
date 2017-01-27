package com.jmeter.websocket.plugin.endpoint;


public interface SessionsManager<K, S> {

    boolean hasOpenSession(K key);

    S getOpenSession(K key);

    void registerSession(K key, S session);

    void closeSessions();
}
