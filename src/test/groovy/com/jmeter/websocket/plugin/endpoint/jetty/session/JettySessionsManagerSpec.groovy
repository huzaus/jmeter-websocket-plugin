package com.jmeter.websocket.plugin.endpoint.jetty.session

import com.jmeter.websocket.plugin.endpoint.WebsocketSession
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class JettySessionsManagerSpec extends Specification {

    @Subject
    JettySessionsManager manager = new JettySessionsManager()

    def "Should return false when zero session is registered on hasOpenSession"() {
        expect:
            !manager.hasOpenSession('user1Session')
    }

    def "Should return '#value' if '#registeredSessionId' session is registered and it isOpen()='#open' on hasOpenSession('#sessionId')"() {
        given:
            manager.registerSession(registeredSessionId,
                    Stub(WebsocketSession) {
                        isOpen() >> open
                    }
            )
        expect:
            manager.hasOpenSession(sessionId) == value
        where:
            sessionId      | registeredSessionId | open  || value
            'user1Session' | 'user1Session'      | false || false
            'user1Session' | 'user1Session'      | true  || true
            'user1Session' | 'user2Session'      | true  || false
    }

    def "Should return null if '#registeredSessionId' session is registered and it isOpen()='#open' on getOpenSession('#sessionId')"() {
        given:
            manager.registerSession(registeredSessionId,
                    Stub(WebsocketSession) {
                        isOpen() >> open
                    }
            )
        expect:
            manager.getOpenSession(sessionId) == null
        where:
            sessionId      | registeredSessionId | open
            'user1Session' | 'user1Session'      | false
            'user1Session' | 'user2Session'      | true
    }

    def "Should return not null if '#registeredSessionId' session is registered and it isOpen()='#open' on getOpenSession('#sessionId')"() {
        given:
            manager.registerSession(registeredSessionId,
                    Stub(WebsocketSession) {
                        isOpen() >> open
                    }
            )
        expect:
            manager.hasOpenSession(sessionId) != null
        where:
            sessionId      | registeredSessionId | open
            'user1Session' | 'user1Session'      | true
    }

    def "Should close all open sessions and clear sessions map on closeSessions()"() {
        given:
            WebsocketSession session = Mock()
            manager.registerSession('user1Session', session)
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
            WebsocketSession session = Mock(WebsocketSession)
            manager.registerSession('user1Session', session)
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
