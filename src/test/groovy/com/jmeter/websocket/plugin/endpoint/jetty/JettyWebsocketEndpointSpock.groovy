package com.jmeter.websocket.plugin.endpoint.jetty

import org.eclipse.jetty.websocket.api.RemoteEndpoint
import org.eclipse.jetty.websocket.api.Session
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class JettyWebsocketEndpointSpock extends Specification {
    @Subject
    JettyWebsocketEndpoint endpoint = new JettyWebsocketEndpoint()

    def "Should delegate message sending to remote endpoint"() {
        given:
        Session session = Stub(Session)
        session.isOpen() >> true
        RemoteEndpoint remote = Mock(RemoteEndpoint)
        session.getRemote() >> remote
        String message = 'message'
        URI uri = URI.create('ws://localhost:8080/websocket')
        endpoint.sessions.put(uri, session)
        when:
        endpoint.sendMessage(uri, message)
        then:
        1 * remote.sendString(message)
    }

//    def "Should delegate establishing connection to web socket client"() {
//        given:
//        URI uri = URI.create('ws://localhost:8080/websocket')
//        CookieManager cookieManager = new CookieManager()
//        def headers = [:]
//        SampleResult sampleResult = new SampleResult()
//        long timeout = 2000
//        and:
//        CookieStore cookieStore = Mock()
//        endpoint.cookieManagerToCookieStoreConverter = Stub(Function) {
//            apply(cookieManager) >> cookieStore
//        }
//        and:
//        ClientUpgradeRequest upgradeRequest = Stub(ClientUpgradeRequest)
//        endpoint.headersToClientUpgradeRequestConverter = Stub(Function) {
//            apply(headers) >> upgradeRequest
//        }
//        and:
//        JettyWebsocketUpgradeListener listener = Stub(JettyWebsocketUpgradeListener)
//        endpoint.sampleResultToUpgradeListenerConverter = Stub(Function) {
//            apply(sampleResult) >> listener
//        }
//        and:
//        Future promise = Mock()
//        when:
//        endpoint.connect(
//                uri,
//                cookieManager,
//                headers,
//                sampleResult,
//                timeout)
//        then:
//        1 * webSocketClient.connect(_, uri, upgradeRequest, listener) >> promise
//        and:
//        1 * promise.get(timeout, MILLISECONDS)
//    }
}
