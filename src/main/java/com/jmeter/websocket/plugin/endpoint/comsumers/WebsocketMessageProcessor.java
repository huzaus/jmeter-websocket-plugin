package com.jmeter.websocket.plugin.endpoint.comsumers;

public interface WebsocketMessageProcessor {

    void onMessageReceive(String sessionId, String message);

    void onMessageSend(String sessionId, String message);

}
