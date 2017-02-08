package com.jmeter.websocket.plugin.endpoint.jetty.session

import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketIncomingMessageConsumer
import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketMessageConsumer
import org.eclipse.jetty.websocket.api.Session
import spock.lang.Specification
import spock.lang.Subject

class JettySocketSpec extends Specification {
    @Subject
    JettySocket websocket

    def "Should throw NullPointerException new JettySocket(null)"() {
        when:
            websocket = new JettySocket(null)
        then:
            thrown(NullPointerException)
    }

    def "Should add consumer to websocketIncomingMessageConsumers on registerWebsocketIncomingMessageConsumer()"() {
        given:
            websocket = new JettySocket(sessionId)
        when:
            websocket.registerWebsocketIncomingMessageConsumer(consumer)
        then:
            websocket.websocketIncomingMessageConsumers.contains consumer
        where:
            sessionId      | consumer
            'user1Session' | Stub(WebsocketIncomingMessageConsumer)
    }
    def "Should remove consumer from websocketIncomingMessageConsumers on unregisterWebsocketIncomingMessageConsumer()"() {
        given:
            websocket = new JettySocket(sessionId)
            websocket.registerWebsocketIncomingMessageConsumer(consumer)
        expect:
            websocket.websocketIncomingMessageConsumers.contains consumer
        when:
            websocket.unregisterWebsocketIncomingMessageConsumer(consumer)
        then:
            !websocket.websocketIncomingMessageConsumers.contains(consumer)
        where:
            sessionId      | consumer
            'user1Session' | Stub(WebsocketIncomingMessageConsumer)
    }

    def "Should notify consumer about message with session sessionId on onWebSocketMessage"() {
        given:
            websocket = new JettySocket(sessionId)
            WebsocketMessageConsumer consumer = Mock()
            Session session = Stub(Session)
            websocket.registerWebsocketIncomingMessageConsumer(consumer)
        when:
            websocket.onWebSocketMessage(session, message)
        then:
            1 * consumer.onMessageReceive(sessionId, message)
        where:
            sessionId      | message
            'user1Session' | 'message'
    }
}
