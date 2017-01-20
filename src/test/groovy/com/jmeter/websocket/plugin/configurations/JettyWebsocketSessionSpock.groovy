package com.jmeter.websocket.plugin.configurations

import com.jmeter.websocket.plugin.endpoint.JettyWebsocketEndpoint
import org.eclipse.jetty.util.ssl.SslContextFactory
import org.eclipse.jetty.websocket.client.WebSocketClient
import spock.lang.Specification
import spock.lang.Subject

import static java.nio.file.Files.createTempFile

class JettyWebsocketSessionSpock extends Specification {
    @Subject
    JettyWebsocketEndpoint session = new JettyWebsocketEndpoint(createTempFile("tempfiles-delete-on-close", ".tmp"))

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
