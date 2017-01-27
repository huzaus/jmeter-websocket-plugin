package com.jmeter.websocket.plugin.endpoint.comsumers;

public interface WebsocketMessageConsumer {

    void onMessageReceive(String sessionId, String message);

    void onMessageSend(String sessionId, String message);

}
