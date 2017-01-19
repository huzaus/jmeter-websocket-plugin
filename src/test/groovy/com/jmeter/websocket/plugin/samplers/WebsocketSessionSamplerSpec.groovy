package com.jmeter.websocket.plugin.samplers

import com.jmeter.websocket.plugin.samplers.WebsocketSessionSampler
import org.apache.jmeter.protocol.http.control.Cookie
import org.apache.jmeter.protocol.http.control.CookieManager
import org.apache.jmeter.protocol.http.control.Header
import org.apache.jmeter.protocol.http.control.HeaderManager
import org.eclipse.jetty.util.ssl.SslContextFactory
import org.eclipse.jetty.websocket.client.WebSocketClient
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.jmeter.websocket.plugin.samplers.WebsocketSessionSampler.CONNECT_TIME_OUT
import static com.jmeter.websocket.plugin.samplers.WebsocketSessionSampler.COOKIE_MANAGER
import static com.jmeter.websocket.plugin.samplers.WebsocketSessionSampler.HEADER_MANAGER
import static com.jmeter.websocket.plugin.samplers.WebsocketSessionSampler.MESSAGE
import static com.jmeter.websocket.plugin.samplers.WebsocketSessionSampler.PATH
import static com.jmeter.websocket.plugin.samplers.WebsocketSessionSampler.PORT_NUMBER
import static com.jmeter.websocket.plugin.samplers.WebsocketSessionSampler.PROTOCOL
import static com.jmeter.websocket.plugin.samplers.WebsocketSessionSampler.RESPONSE_TIME_OUT
import static com.jmeter.websocket.plugin.samplers.WebsocketSessionSampler.SERVER_NAME_OR_IP

@Unroll
class WebsocketSessionSamplerSpec extends Specification {

    @Subject
    WebsocketSessionSampler sampler = new WebsocketSessionSampler()

    def "#field field value should be stored in #property property of #type type"() {
        when:
        sampler."$field" = value
        then:
        sampler."getPropertyAs$type"(property) == value
        where:
        field             | property          | type     | value
        'serverNameOrIp'  | SERVER_NAME_OR_IP | 'String' | '127.0.0.1'
        'portNumber'      | PORT_NUMBER       | 'String' | '8080'
        'protocol'        | PROTOCOL          | 'String' | 'ws'
        'connectTimeOut'  | CONNECT_TIME_OUT  | 'String' | '2000'
        'responseTimeOut' | RESPONSE_TIME_OUT | 'String' | '5000'
        'path'            | PATH              | 'String' | '/websocket'
        'message'         | MESSAGE           | 'String' | 'CONNECT\\naccept-version:1.1,1.0\\nheart-beat:10000,10000\\n\\n\\u0000'
    }

    def "Should not set #property property via addTestElement method when class type is not matched"() {
        when:
        sampler.addTestElement(manager)
        then:
        sampler.getProperty(property).objectValue == null
        where:
        property       | manager
        COOKIE_MANAGER | new HeaderManager()
        HEADER_MANAGER | new CookieManager()
    }

    def "Should set #property property via addTestElement method when class type is matched"() {
        when:
        sampler.addTestElement(manager)
        then:
        sampler.getProperty(property).objectValue == manager
        where:
        property       | manager
        COOKIE_MANAGER | new CookieManager()
        HEADER_MANAGER | new HeaderManager()
    }

    def "Should return null when #property is not set"() {
        expect:
        sampler."$property" == null
        where:
        property << ["cookieManager", "headerManager"]

    }

    def "Should return value when #property is set to #value"() {
        when:
        sampler."$property" = value
        then:
        sampler."$property" == value
        where:
        property        | value
        "cookieManager" | new CookieManager()
        "cookieManager" | null
        "headerManager" | new HeaderManager()
        "headerManager" | null
    }

    def "Should create new uri equal to #uri"() {
        when:
        sampler.serverNameOrIp = serverNameOrIp
        and:
        sampler.portNumber = portNumber
        and:
        sampler.protocol = 'ws'
        and:
        sampler.path = '/websocket'
        then:
        sampler.uri() as String == uri
        and:
        !uri.is(sampler.uri())
        where:
        serverNameOrIp | portNumber | protocol | path         | uri
        '127.0.0.1'    | '8080'     | 'ws'     | '/websocket' | 'ws://127.0.0.1:8080/websocket'
    }

    def "Should create new SSL context factory with trustAll flag"() {
        when:
        SslContextFactory sslContextFactory = sampler.sslContextFactory()
        then:
        sslContextFactory != sampler.sslContextFactory()
        and:
        sslContextFactory.trustAll
    }

    def "should create not empty headers map with [#name:#value] headers when headerManager is set"() {
        given:
        HeaderManager headerManager = new HeaderManager();
        headerManager.add(new Header(name, value))
        when:
        sampler.headerManager = headerManager
        and:
        def headers = sampler.headers()
        then:
        [(name): [value]] == headers
        and:
        !headers.is(sampler.headers())
        and:
        headers == sampler.headers()
        where:
        name      | value
        'Session' | '1'
        'Session' | null
    }

    def "should create empty map headers when headerManager is not set"() {
        expect:
        [:] == sampler.headers()
    }

    def "Should create new upgrade request with headers when headerManager is set"() {
        given:
        HeaderManager headerManager = new HeaderManager();
        headerManager.add(new Header(name, value))
        when:
        sampler.headerManager = headerManager
        and:
        def upgradeRequest = sampler.upgradeRequest()
        then:
        [(name): [value]] == upgradeRequest.headers
        and:
        upgradeRequest != sampler.upgradeRequest()
        where:
        name      | value
        'Session' | '1'
        'Session' | null
    }

    def "Should create new upgrade request with headers when headerManager is not set"() {
        when:
        def upgradeRequest = sampler.upgradeRequest()
        then:
        [:] == upgradeRequest.headers
        and:
        upgradeRequest != sampler.upgradeRequest()
    }

    def "Should create not empty cookies with [#name, #value, #domain, #path, #secure, #expires] value when cookieManager is set"() {

        given:
        CookieManager cookieManager = new CookieManager()
        and:
        cookieManager.add(new Cookie(name, value, domain, path, secure, expires))
        when:
        sampler.cookieManager = cookieManager
        and:
        CookieStore cookies = sampler.cookies()
        then:
        cookies.cookies.contains new HttpCookie(name, domain)
        and:
        cookies != sampler.cookies()
        where:
        name      | value | domain      | path         | secure | expires
        'Session' | '1'   | '127.0.0.1' | '/websocket' | true   | 0
    }

    def "Should create empty cookies when cookieManager is not set"() {
        expect:
        sampler.cookies().cookies.isEmpty()
    }

    def "Should create websocket with sslContextFactory, executor and set cookies"() {
        when:
        WebSocketClient webSocketClient = sampler.webSocketClient()
        then:
        webSocketClient.sslContextFactory != null
        and:
        webSocketClient.executor != null
        and:
        webSocketClient.cookieStore != null
    }
}

