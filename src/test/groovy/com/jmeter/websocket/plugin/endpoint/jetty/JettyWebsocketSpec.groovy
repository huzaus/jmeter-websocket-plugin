package com.jmeter.websocket.plugin.endpoint.jetty

import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketMessageConsumer
import org.eclipse.jetty.websocket.api.Session
import spock.lang.Specification
import spock.lang.Subject

class JettyWebsocketSpec extends Specification {
    @Subject
    JettyWebsocket websocket
    
    def "Should throw NullPointerException onWebSocketMessage"() {
        given:
            websocket = new JettyWebsocket('user1Session')
        when:
            websocket.onWebSocketMessage(null, 'message')
        then:
            thrown(NullPointerException)
    }
    
    def "Should notify WebsocketMessageConsumer about message with session hashCode as hexString on onWebSocketMessage"() {
        given:
            websocket = new JettyWebsocket(sessionId)
            WebsocketMessageConsumer consumer = Mock()
            Session session = Stub(Session)
            websocket.registerWebsocketMessageConsumer(consumer)
        when:
            websocket.onWebSocketMessage(session, message)
        then:
            1 * consumer.onMessageReceive(sessionId, message)
        where:
            sessionId      | message
            'user1Session' | 'message'
    }
}
