package com.jmeter.websocket.plugin.endpoint.jetty

import com.google.common.base.Function
import org.apache.jmeter.protocol.http.control.CookieManager
import org.apache.jmeter.samplers.SampleResult
import org.eclipse.jetty.websocket.api.RemoteEndpoint
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest
import org.eclipse.jetty.websocket.client.WebSocketClient
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.nio.channels.ByteChannel
import java.util.concurrent.Future

import static java.nio.file.Files.createTempFile
import static java.util.concurrent.TimeUnit.MILLISECONDS

@Unroll
class JettyWebsocketSessionSpock extends Specification {
    @Subject
    JettyWebsocketEndpoint endpoint = new JettyWebsocketEndpoint(createTempFile("temp-file-delete-on-close", ".tmp"))

    def "Should throw null pointer exception when file is null"() {
        when:
        new JettyWebsocketEndpoint(null)
        then:
        thrown(NullPointerException)
    }

    def "Should return the same byteChannel " () {
        when:
        ByteChannel channel = endpoint.byteChannel
        then:
        channel.is(endpoint.byteChannel)
    }

    def "Should delegate message sending to remote endpoint"() {
        given:
        Session session = Stub(Session)
        session.isOpen() >> true
        RemoteEndpoint remote = Mock(RemoteEndpoint)
        session.getRemote() >> remote
        String message = 'message'
        endpoint.session = session
        when:
        endpoint.sendMessage(message)
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
//        WebSocketClient webSocketClient = Mock()
//        endpoint.cookieManagerToWebSocketClientConverter = Stub(Function) {
//            apply(cookieManager) >> webSocketClient
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
//        1 * webSocketClient.start()
//        and:
//        1 * webSocketClient.connect(endpoint, uri, upgradeRequest, listener) >> promise
//        and:
//        1 * promise.get(timeout, MILLISECONDS)
//    }
}
