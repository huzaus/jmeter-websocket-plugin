package com.jmeter.websocket.plugin.endpoint.jetty.session;

import com.jmeter.websocket.plugin.endpoint.WebsocketSession;
import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketIncomingMessageConsumer;
import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketMessageConsumer;
import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketOutgoingMessageConsumer;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

public class JettySessionWrapper implements WebsocketSession {

    private final Session sessionDelegate;
    private final JettySocket socketDelegate;
    private final String id;

    private final Collection<WebsocketOutgoingMessageConsumer> websocketOutgoingMessageConsumers = new ArrayList<>();

    public JettySessionWrapper(String sessionId, Session session, JettySocket socket) {
        checkNotNull(sessionId);
        checkNotNull(session);
        checkNotNull(socket);
        id = sessionId;
        sessionDelegate = session;
        socketDelegate = socket;
    }

    public void registerWebsocketMessageConsumer(WebsocketMessageConsumer consumer) {
        registerWebsocketIncomingMessageConsumer(consumer);
        registerWebsocketOutgoingMessageConsumer(consumer);
    }

    @Override
    public void unregisterWebsocketMessageConsumer(WebsocketMessageConsumer consumer) {
        unregisterWebsocketIncomingMessageConsumer(consumer);
        unregisterWebsocketOutgoingMessageConsumer(consumer);
    }

    public void registerWebsocketIncomingMessageConsumer(WebsocketIncomingMessageConsumer consumer) {
        socketDelegate.registerWebsocketIncomingMessageConsumer(consumer);
    }

    @Override
    public void unregisterWebsocketIncomingMessageConsumer(WebsocketIncomingMessageConsumer consumer) {
        socketDelegate.unregisterWebsocketIncomingMessageConsumer(consumer);
    }

    @Override
    public void unregisterWebsocketOutgoingMessageConsumer(WebsocketOutgoingMessageConsumer consumer) {
        websocketOutgoingMessageConsumers.remove(consumer);
    }

    public void registerWebsocketOutgoingMessageConsumer(WebsocketOutgoingMessageConsumer consumer) {
        websocketOutgoingMessageConsumers.add(consumer);
    }

    @Override
    public void sendMessage(String message) throws IOException {
        sessionDelegate.getRemote().sendString(message);
        for (WebsocketOutgoingMessageConsumer consumer : websocketOutgoingMessageConsumers) {
            consumer.onMessageSend(id, message);
        }
    }

    @Override
    public boolean isOpen() {
        return sessionDelegate.isOpen();
    }

    @Override
    public void close() throws IOException {
        sessionDelegate.close();
    }
}
