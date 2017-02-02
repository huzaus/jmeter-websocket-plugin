package com.jmeter.websocket.plugin.endpoint;

import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketIncomingMessageConsumer;
import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketMessageConsumer;
import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketOutgoingMessageConsumer;

import java.io.Closeable;
import java.io.IOException;

public interface WebsocketSession extends Closeable {

    void registerWebsocketMessageConsumer(WebsocketMessageConsumer consumer);

    void unregisterWebsocketMessageConsumer(WebsocketMessageConsumer consumer);

    void registerWebsocketIncomingMessageConsumer(WebsocketIncomingMessageConsumer consumer);

    void unregisterWebsocketIncomingMessageConsumer(WebsocketIncomingMessageConsumer consumer);

    void registerWebsocketOutgoingMessageConsumer(WebsocketOutgoingMessageConsumer consumer);

    void unregisterWebsocketOutgoingMessageConsumer(WebsocketOutgoingMessageConsumer consumer);

    void sendMessage(String message) throws IOException;

    boolean isOpen();

}
