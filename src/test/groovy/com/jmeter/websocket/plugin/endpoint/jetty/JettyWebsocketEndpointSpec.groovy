package com.jmeter.websocket.plugin.endpoint.jetty

import com.google.common.base.Function
import com.google.common.base.Supplier
import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketMessageProcessor
import org.apache.jmeter.protocol.http.control.CookieManager
import org.apache.jmeter.samplers.SampleResult
import org.eclipse.jetty.websocket.api.RemoteEndpoint
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest
import org.eclipse.jetty.websocket.client.WebSocketClient
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.util.concurrent.Future

import static java.util.concurrent.TimeUnit.MILLISECONDS

@Unroll
class JettyWebsocketEndpointSpec extends Specification {
    @Subject
    JettyWebsocketEndpoint endpoint = new JettyWebsocketEndpoint()

    def "Should throw NullPointerException when session for uri is null"() {
        when:
        endpoint.sendMessage(URI.create('ws://localhost:8080/websocket'), 'message')
        then:
        thrown(NullPointerException)
    }

    def "Should throw IllegalArgumentException when session for #uri is not open"() {
        given:
        URI uri = URI.create('ws://localhost:8080/websocket')
        endpoint.sessions[uri] = Stub(Session) {
            isOpen() >> false
        }
        when:
        endpoint.sendMessage(uri, 'message')
        then:
        thrown(IllegalArgumentException)
    }

    def "Should delegate message sending to remote endpoint and notify processors"() {
        given:
        Session session = Stub(Session)
        session.isOpen() >> true
        RemoteEndpoint remote = Mock()
        session.getRemote() >> remote
        and:
        WebsocketMessageProcessor processor = Mock()
        endpoint.registerWebsocketMessageConsumer(processor)
        and:
        String message = 'message'
        URI uri = URI.create('ws://localhost:8080/websocket')
        endpoint.sessions[uri] = session
        when:
        endpoint.sendMessage(uri, message)
        then:
        1 * remote.sendString(message)
        and:
        1 * processor.onMessageSend(_, message)
    }

    def "Should throw IllegalArgumentException when session for uri is open on connect"() {
        given:
        URI uri = URI.create('ws://localhost:8080/websocket')
        endpoint.sessions[uri] = Stub(Session) {
            isOpen() >> true
        }
        when:
        endpoint.connect(uri, new CookieManager(), [:], new SampleResult(), 2000)
        then:
        thrown(IllegalArgumentException)

    }

    def "Should delegate establishing connection to web socket client and save acquired session in sessions on connect"() {
        given:
        URI uri = URI.create('ws://localhost:8080/websocket')
        CookieManager cookieManager = new CookieManager()
        def headers = [:]
        SampleResult sampleResult = new SampleResult()
        long timeout = 2000
        and:
        WebSocketClient webSocketClient = Mock()
        endpoint.webSocketClientSupplier = Stub(Supplier) {
            get() >> webSocketClient
        }
        and:
        CookieStore cookieStore = Stub()
        endpoint.cookieManagerToCookieStoreConverter = Stub(Function) {
            apply(cookieManager) >> cookieStore
        }
        and:
        ClientUpgradeRequest upgradeRequest = Stub()
        endpoint.headersToClientUpgradeRequestConverter = Stub(Function) {
            apply(headers) >> upgradeRequest
        }
        and:
        JettyWebsocketUpgradeListener listener = Stub(JettyWebsocketUpgradeListener)
        endpoint.sampleResultToUpgradeListenerConverter = Stub(Function) {
            apply(sampleResult) >> listener
        }
        and:
        Future promise = Mock()
        Session session = Stub()
        when:
        endpoint.connect(
                uri,
                cookieManager,
                headers,
                sampleResult,
                timeout)
        then:
        1 * webSocketClient.connect(_, uri, upgradeRequest, listener) >> promise
        and:
        1 * promise.get(timeout, MILLISECONDS) >> session
        and:
        endpoint.sessions[uri] == session
    }

    def "Should not throw exception when webSocketClientSupplier throws exception on start"() {
        given:
        endpoint.webSocketClientSupplier = Mock(Supplier)
        when:
        endpoint.start()
        then:
        1 * endpoint.webSocketClientSupplier.get() >> { throw new NullPointerException() }
        and:
        noExceptionThrown()
    }

    def "Should not throw exception when webSocketClientSupplier throws exception on stop"() {
        given:
        endpoint.webSocketClientSupplier = Mock(Supplier)
        when:
        endpoint.stop()
        then:
        1 * endpoint.webSocketClientSupplier.get() >> { throw new NullPointerException() }
        and:
        noExceptionThrown()
    }

    def "Should clear all sessions on stop"() {
        given:
        endpoint.webSocketClientSupplier = Stub(Supplier) {
            get() >> Stub(WebSocketClient)
        }
        Session session = Mock()
        endpoint.sessions[URI.create('ws://localhost:8080/websocket')] = session
        when:
        endpoint.stop()
        then:
        1 * session.isOpen() >> false
        and:
        endpoint.sessions.isEmpty()
    }

    def "Should stop all open sessions and clear sessions on stop"() {
        given:
        endpoint.webSocketClientSupplier = Stub(Supplier) {
            get() >> Stub(WebSocketClient)
        }
        Session session = Mock()
        endpoint.sessions[URI.create('ws://localhost:8080/websocket')] = session
        when:
        endpoint.stop()
        then:
        1 * session.isOpen() >> true
        and:
        1 * session.close()
        and:
        endpoint.sessions.isEmpty()
    }
}
