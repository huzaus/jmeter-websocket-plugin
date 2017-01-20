package com.jmeter.websocket.plugin.configurations

import org.eclipse.jetty.util.ssl.SslContextFactory
import org.eclipse.jetty.websocket.client.WebSocketClient
import spock.lang.Specification
import spock.lang.Subject

class JettyWebsocketSessionSpock extends Specification {
    @Subject
    JettyWebsocketSession session = new JettyWebsocketSession()

    def "Should create new SSL context factory with trustAll flag"() {
        when:
        SslContextFactory sslContextFactory = session.sslContextFactory()
        then:
        sslContextFactory != session.sslContextFactory()
        and:
        sslContextFactory.trustAll
    }

    def "Should create websocket with sslContextFactory, executor and set cookies"() {
        when:
        CookieStore cookieStore = Stub(CookieStore);
        WebSocketClient webSocketClient = session.webSocketClient()
        then:
        webSocketClient.sslContextFactory != null
        and:
        webSocketClient.executor != null
        and:
        webSocketClient.cookieStore != cookieStore
    }
}
