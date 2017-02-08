package com.jmeter.websocket.plugin.endpoint.comsumers;

public interface WebsocketIncomingMessageConsumer {
    void onMessageReceive(String sessionId, String message);
}
