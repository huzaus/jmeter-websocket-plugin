package com.jmeter.websocket.plugin.endpoint.jetty

import com.google.common.base.Function
import com.google.common.base.Supplier
import com.jmeter.websocket.plugin.endpoint.SessionsManager
import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketMessageConsumer
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
    
    def "Should delegate message sending to remote endpoint and notify consumers"() {
        given:
            RemoteEndpoint remote = Mock()
            Session session = Stub(Session) {
                getRemote() >> remote
            }
        and:
            WebsocketMessageConsumer consumer = Mock()
            endpoint.registerMessageConsumer(consumer)
        and:
            String message = 'message'
            URI uri = URI.create('ws://localhost:8080/websocket')
            endpoint.sessionsManager = Stub(SessionsManager) {
                getOpenSession(uri) >> session
            }
        when:
            endpoint.sendMessage(uri, message)
        then:
            1 * remote.sendString(message)
        and:
            1 * consumer.onMessageSend(_, message)
    }
    
    def "Should throw IllegalArgumentException when session for uri is open on connect"() {
        given:
            URI uri = URI.create('ws://localhost:8080/websocket')
            endpoint.sessionsManager = Stub(SessionsManager) {
                hasOpenSession(uri) >> true
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
            endpoint.sessionsManager = Mock(SessionsManager)
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
            1 * endpoint.sessionsManager.registerSession(uri, session)
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
    
    def "Should close sessions on stop"() {
        given:
            endpoint.webSocketClientSupplier = Stub(Supplier) {
                get() >> Stub(WebSocketClient)
            }
            endpoint.sessionsManager = Mock(SessionsManager)
        when:
            endpoint.stop()
        then:
            1 * endpoint.sessionsManager.closeSessions()
    }
}
