package com.jmeter.websocket.plugin.endpoint.jetty.session

import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketIncomingMessageConsumer
import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketMessageConsumer
import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketOutgoingMessageConsumer
import org.eclipse.jetty.websocket.api.RemoteEndpoint
import org.eclipse.jetty.websocket.api.Session
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll


class JettySessionWrapperSpec extends Specification {

    @Subject
    JettySessionWrapper jettySession

    @Unroll
    def "Should throw NullPointerException on new JettySessionWrapper('#id', '#session', '#socket') "() {
        when:
            jettySession = new JettySessionWrapper(id, session, socket)
        then:
            thrown(NullPointerException)
        where:
            id             | session       | socket
            null           | Stub(Session) | Stub(JettySocket)
            'user1Session' | null          | Stub(JettySocket)
            'user1Session' | Stub(Session) | null
    }

    def "Should add consumer to websocketOutgoingMessageConsumers on registerWebsocketOutgoingMessageConsumer()"() {
        given:
            jettySession = new JettySessionWrapper(id, session, socket)
        when:
            jettySession.registerWebsocketOutgoingMessageConsumer(consumer)
        then:
            jettySession.websocketOutgoingMessageConsumers.contains consumer
        where:
            id             | session       | socket            | consumer
            'user1Session' | Stub(Session) | Stub(JettySocket) | Stub(WebsocketOutgoingMessageConsumer)
    }

    def "Should remove consumer from websocketOutgoingMessageConsumers on unregisterWebsocketOutgoingMessageConsumer()"() {
        given:
            jettySession = new JettySessionWrapper(id, session, socket)
            jettySession.registerWebsocketOutgoingMessageConsumer(consumer)
        expect:
            jettySession.websocketOutgoingMessageConsumers.contains consumer
        when:
            jettySession.unregisterWebsocketOutgoingMessageConsumer(consumer)
        then:
            !jettySession.websocketOutgoingMessageConsumers.contains(consumer)
        where:
            id             | session       | socket            | consumer
            'user1Session' | Stub(Session) | Stub(JettySocket) | Stub(WebsocketOutgoingMessageConsumer)
    }

    def "Should add consumer to socketDelegate on registerWebsocketIncomingMessageConsumer()"() {
        given:
            jettySession = new JettySessionWrapper(id, session, socket)
        when:
            jettySession.registerWebsocketIncomingMessageConsumer(consumer)
        then:
            jettySession.socketDelegate.websocketIncomingMessageConsumers.contains consumer
        where:
            id             | session       | socket                          | consumer
            'user1Session' | Stub(Session) | new JettySocket('user1Session') | Stub(WebsocketIncomingMessageConsumer)
    }

    def "Should remove consumer from socketDelegate on unregisterWebsocketIncomingMessageConsumer()"() {
        given:
            jettySession = new JettySessionWrapper(id, session, socket)
            jettySession.registerWebsocketIncomingMessageConsumer(consumer)
        expect:
            jettySession.socketDelegate.websocketIncomingMessageConsumers.contains consumer
        when:
            jettySession.unregisterWebsocketIncomingMessageConsumer(consumer)
        then:
            !jettySession.socketDelegate.websocketIncomingMessageConsumers.contains(consumer)
        where:
            id             | session       | socket                          | consumer
            'user1Session' | Stub(Session) | new JettySocket('user1Session') | Stub(WebsocketIncomingMessageConsumer)
    }

    def "Should add consumer to socketDelegate and websocketOutgoingMessageConsumers on registerWebsocketMessageConsumer()"() {
        given:
            jettySession = new JettySessionWrapper(id, session, socket)
        when:
            jettySession.registerWebsocketMessageConsumer(consumer)
        then:
            jettySession.websocketOutgoingMessageConsumers.contains consumer
        and:
            jettySession.socketDelegate.websocketIncomingMessageConsumers.contains consumer
        where:
            id             | session       | socket                          | consumer
            'user1Session' | Stub(Session) | new JettySocket('user1Session') | Stub(WebsocketMessageConsumer)
    }

    def "Should remove consumer from socketDelegate and websocketOutgoingMessageConsumers on unregisterWebsocketMessageConsumer()"() {
        given:
            jettySession = new JettySessionWrapper(id, session, socket)
            jettySession.registerWebsocketMessageConsumer(consumer)
        expect:
            jettySession.websocketOutgoingMessageConsumers.contains consumer
        and:
            jettySession.socketDelegate.websocketIncomingMessageConsumers.contains consumer
        when:
            jettySession.unregisterWebsocketMessageConsumer(consumer)
        then:
            !jettySession.websocketOutgoingMessageConsumers.contains(consumer)
        and:
            !jettySession.socketDelegate.websocketIncomingMessageConsumers.contains(consumer)
        where:
            id             | session       | socket                          | consumer
            'user1Session' | Stub(Session) | new JettySocket('user1Session') | Stub(WebsocketMessageConsumer)
    }

    def "Should delegate message sending to remote endpoint and notify consumers"() {
        given:
            RemoteEndpoint remote = Mock()
            Session sessionDelegate = Stub(Session) {
                getRemote() >> remote
            }
            jettySession = new JettySessionWrapper(id, sessionDelegate, Stub(JettySocket))
        and:
            WebsocketOutgoingMessageConsumer consumer = Mock()
            jettySession.registerWebsocketOutgoingMessageConsumer(consumer)
        when:
            jettySession.sendMessage(message)
        then:
            1 * remote.sendString(message)
        and:
            1 * consumer.onMessageSend(id, message)
        where:
            id        | message
            'message' | 'user1Session'
    }

    def "Should delegate isOpen() to sessionDelegate"() {
        given:
            Session session = Mock()
            jettySession = new JettySessionWrapper(id, session, socket)
        when:
            boolean open = jettySession.isOpen()
        then:
            1 * session.isOpen() >> value
        and:
            open == value
        where:
            id             | socket            || value
            'user1Session' | Stub(JettySocket) || true
    }

    def "Should delegate close() to sessionDelegate"() {
        given:
            Session session = Mock()
            jettySession = new JettySessionWrapper(id, session, socket)
        when:
            jettySession.close()
        then:
            1 * session.close()
        where:
            id             | socket            || value
            'user1Session' | Stub(JettySocket) || true
    }
}
