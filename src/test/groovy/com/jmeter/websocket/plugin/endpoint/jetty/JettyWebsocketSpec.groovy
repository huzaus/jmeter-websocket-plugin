package com.jmeter.websocket.plugin.endpoint.jetty

import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketMessageConsumer
import org.eclipse.jetty.websocket.api.Session
import spock.lang.Specification
import spock.lang.Subject

import static java.lang.Integer.toHexString

class JettyWebsocketSpec extends Specification {
    @Subject
    JettyWebsocket websocket = new JettyWebsocket()

    def "Should throw NullPointerException onWebSocketMessage"() {
        when:
        websocket.onWebSocketMessage(null, 'message')
        then:
        thrown(NullPointerException)
    }

    def "Should notify WebsocketMessageConsumer about message with session hashCode as hexString on onWebSocketMessage"() {
        given:
        WebsocketMessageConsumer consumer = Mock()
        Session session = Stub(Session)
        websocket.registerWebsocketMessageConsumer(consumer)
        when:
        websocket.onWebSocketMessage(session, message)
        then:
        1 * consumer.onMessageReceive(toHexString(session.hashCode()), message)
        where:
        message = 'message'
    }
}
