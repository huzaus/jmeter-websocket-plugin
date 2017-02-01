package com.jmeter.websocket.plugin.endpoint.jetty.session

import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketMessageConsumer
import org.eclipse.jetty.websocket.api.Session
import spock.lang.Specification
import spock.lang.Subject

class JettyWebsocketSpec extends Specification {
    @Subject
    JettySocket websocket
    
    def "Should throw NullPointerException onWebSocketMessage"() {
        given:
            websocket = new JettySocket('user1Session')
        when:
            websocket.onWebSocketMessage(null, 'message')
        then:
            thrown(NullPointerException)
    }
    
    def "Should notify WebsocketMessageConsumer about message with session hashCode as hexString on onWebSocketMessage"() {
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
