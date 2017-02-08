package com.jmeter.websocket.plugin.endpoint.comsumers;

public interface WebsocketOutgoingMessageConsumer {
    void onMessageSend(String sessionId, String message);
}
