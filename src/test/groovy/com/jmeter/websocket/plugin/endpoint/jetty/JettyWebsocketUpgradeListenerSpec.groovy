package com.jmeter.websocket.plugin.endpoint.jetty

import org.apache.jmeter.samplers.SampleResult
import org.eclipse.jetty.websocket.api.UpgradeRequest
import org.eclipse.jetty.websocket.api.UpgradeResponse
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class JettyWebsocketUpgradeListenerSpec extends Specification {
    
    @Subject
    JettyWebsocketUpgradeListener listener
    
    def "Should throw NullPointerException when sampleResult is null on new"() {
        when:
            listener = new JettyWebsocketUpgradeListener(null)
        then:
            thrown(NullPointerException)
    }
    
    def "Should throw NullPointerException when request is null onHandshakeRequest"() {
        given:
            listener = new JettyWebsocketUpgradeListener(new SampleResult())
        when:
            listener.onHandshakeRequest(null)
        then:
            thrown(NullPointerException)
    }
    
    def "Should set SampleResult '#resultField' field to value '#resultFieldValue' from UpgradeRequest '#requestField' field with '#requestFieldValue' onHandshakeRequest"() {
        given:
            SampleResult result = new SampleResult()
            listener = new JettyWebsocketUpgradeListener(result)
            UpgradeRequest request = new UpgradeRequest(URI.create('ws://localhost:8080/websocket'))
        when:
            request."$requestField" = requestFieldValue
        and:
            listener.onHandshakeRequest(request)
        then:
            result."$resultField" == resultFieldValue
        where:
            requestField | requestFieldValue                            | resultField      || resultFieldValue
            'headers'    | [:]                                          | 'requestHeaders' || ''
            'headers'    | ['name': ['value1', 'value2']]               | 'requestHeaders' || 'name=[value1, value2]'
            'requestURI' | URI.create('ws://localhost:8081/websocket')  | 'URL'            || new URL('http://localhost:8081/websocket')
            'requestURI' | URI.create('wss://localhost:8081/websocket') | 'URL'            || new URL('https://localhost:8081/websocket')
    }
    
    def "Should throw NullPointerException when response is null onHandshakeResponse"() {
        given:
            listener = new JettyWebsocketUpgradeListener(new SampleResult())
        when:
            listener.onHandshakeResponse(null)
        then:
            thrown(NullPointerException)
    }
    
    def "Should set SampleResult '#resultField' field to value '#resultFieldValue' from UpgradeResponse '#responseField' field with '#responseFieldValue' onHandshakeResponse"() {
        given:
            SampleResult result = new SampleResult()
            listener = new JettyWebsocketUpgradeListener(result)
            UpgradeResponse response = new UpgradeResponse()
        when:
            response."$responseField" = responseFieldValue
        and:
            listener.onHandshakeResponse(response)
        then:
            result."$resultField" == resultFieldValue
        where:
            responseField  | responseFieldValue             | resultField       || resultFieldValue
            'success'      | true                           | 'successful'      || true
            'statusReason' | 'OK'                           | 'responseMessage' || 'OK'
            'statusCode'   | 101                            | 'responseCode'    || '101'
            'headers'      | [:]                            | 'responseHeaders' || ''
            'headers'      | ['name': ['value1', 'value2']] | 'responseHeaders' || 'name=[value1, value2]'
    }
}