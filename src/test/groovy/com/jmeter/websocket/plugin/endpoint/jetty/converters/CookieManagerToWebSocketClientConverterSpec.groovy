package com.jmeter.websocket.plugin.endpoint.jetty.converters

import org.apache.jmeter.protocol.http.control.Cookie
import org.apache.jmeter.protocol.http.control.CookieManager
import org.eclipse.jetty.websocket.client.WebSocketClient
import spock.lang.Specification
import spock.lang.Subject

class CookieManagerToWebSocketClientConverterSpec extends Specification {
    
    @Subject
    CookieManagerToWebSocketClientConverter converter = new CookieManagerToWebSocketClientConverter()
    
    def "Should return configured WebSocketClient when CookieManager is null"() {
        when:
            WebSocketClient client = converter.apply(null)
        then:
            with(client) {
                executor != null
                sslContextFactory.trustAll
                cookieStore.cookies.empty
                executor != null
            }
    }
    
    def "Should create not empty cookies with [#name, #value, #domain, #path, #secure, #expires] value when cookieManager is set"() {
        given:
            CookieManager cookieManager = new CookieManager()
        and:
            cookieManager.add(new Cookie(name, value, domain, path, secure, expires))
        when:
            WebSocketClient client = converter.apply(cookieManager)
        then:
            client.cookieStore.cookies.contains new HttpCookie(name, domain)
        where:
            name      | value | domain      | path         | secure | expires
            'Session' | '1'   | '127.0.0.1' | '/websocket' | true   | 0
    }
}
