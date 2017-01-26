package com.jmeter.websocket.plugin.endpoint.jetty

import org.eclipse.jetty.websocket.api.Session
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class JettySessionManagerSpec extends Specification {

    @Subject
    JettySessionManager manager = new JettySessionManager()

    def "Should return false when zero session is registered on hasOpenSession"() {
        expect:
        !manager.hasOpenSession(URI.create('ws://localhost:8080/ws'))
    }

    def "Should return '#value' if '#registeredUri' session is registered and it isOpen()='#isOpen' on hasOpenSession('#uri')"() {
        given:
        manager.registerSession(registeredUri,
                Stub(Session) {
                    isOpen() >> open
                }
        )
        expect:
        manager.hasOpenSession(uri) == value
        where:
        uri                                         | registeredUri                               | open  || value
        URI.create('ws://localhost:8080/websocket') | URI.create('ws://localhost:8080/websocket') | false || false
        URI.create('ws://localhost:8080/websocket') | URI.create('ws://localhost:8080/websocket') | true  || true
        URI.create('ws://localhost:8081/websocket') | URI.create('ws://localhost:8080/websocket') | true  || false
    }

    def "Should return null if '#registeredUri' session is registered and it isOpen()='#open' on getOpenSession('#uri')"() {
        given:
        manager.registerSession(registeredUri,
                Stub(Session) {
                    isOpen() >> open
                }
        )
        expect:
        manager.getOpenSession(uri) == null
        where:
        uri                                         | registeredUri                               | open
        URI.create('ws://localhost:8080/websocket') | URI.create('ws://localhost:8080/websocket') | false
        URI.create('ws://localhost:8081/websocket') | URI.create('ws://localhost:8080/websocket') | true
    }

    def "Should return not null if '#registeredUri' session is registered and it isOpen()='#open' on getOpenSession('#uri')"() {
        given:
        manager.registerSession(URI.create('ws://localhost:8080/websocket'),
                Stub(Session) {
                    isOpen() >> open
                }
        )
        expect:
        manager.hasOpenSession(uri) != null
        where:
        uri                                         | registeredUri                               | open
        URI.create('ws://localhost:8080/websocket') | URI.create('ws://localhost:8080/websocket') | true
    }

    def "Should close all open sessions and clear sessions map on closeSessions()"() {
        given:
        Session session = Mock(Session)
        manager.registerSession(URI.create('ws://localhost:8080/websocket'), session)
        expect:
        !manager.sessions.isEmpty()
        when:
        manager.closeSessions()
        then:
        1 * session.isOpen() >> true
        and:
        1 * session.close()
        and:
        manager.sessions.isEmpty()
    }

    def "Should clear sessions map on closeSessions()"() {
        given:
        Session session = Mock(Session)
        manager.registerSession(URI.create('ws://localhost:8080/websocket'), session)
        expect:
        !manager.sessions.isEmpty()
        when:
        manager.closeSessions()
        then:
        1 * session.isOpen() >> false
        and:
        0 * session.close()
        and:
        manager.sessions.isEmpty()
    }
}
