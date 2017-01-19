package com.jmeter.websocket.plugin

import org.apache.jmeter.samplers.SampleResult
import org.eclipse.jetty.websocket.api.UpgradeRequest
import org.eclipse.jetty.websocket.api.UpgradeResponse
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class WebsocketUpgradeListenerSpock extends Specification {

    @Subject
    WebsocketUpgradeListener listener = new WebsocketUpgradeListener(new SampleResult())

    def "Should throw NullPointerException when result is null"() {
        when:
        listener = new WebsocketUpgradeListener(null)
        then:
        thrown(NullPointerException)
    }

    def "Should extract #headers upgrade request headers to sample result request headers as '#result'"() {
        given:
        UpgradeRequest upgradeRequest = new UpgradeRequest('wss://127.0.0.1/websocket')
        upgradeRequest.headers = headers
        when:
        listener.onHandshakeRequest(upgradeRequest)
        then:
        listener.sampleResult.requestHeaders == result
        where:
        headers                                               || result
        [:]                                                   || ''
        ['header': ['value']]                                 || 'header=[value]'
        ['header': ['value']]                                 || 'header=[value]'
        ['header1': ['value one'], 'header2': ['value two']]  || 'header1=[value one]\nheader2=[value two]'
        ['multiple_value_header': ['value one', 'value two']] || 'multiple_value_header=[value one, value two]'
    }

    def "Should extract '#uri' uri upgrade request to sample result URL as '#url'"() {
        given:
        UpgradeRequest upgradeRequest = new UpgradeRequest(uri)
        when:
        listener.onHandshakeRequest(upgradeRequest)
        then:
        url == listener.sampleResult.urlAsString
        where:
        uri                         || url
        'ws://127.0.0.1/websocket'  || 'http://127.0.0.1/websocket'
        'wss://127.0.0.1/websocket' || 'https://127.0.0.1/websocket'
    }

    def "Should extract '#statusReason' statusReason, '#responseCode' response code, #success response status and set it sample result"() {
        given:
        UpgradeResponse upgradeResponse = new UpgradeResponse()
        upgradeResponse.statusCode = responseCode
        upgradeResponse.statusReason = statusReason
        upgradeResponse.success = success
        when:
        listener.onHandshakeResponse(upgradeResponse)
        then:
        statusReason == listener.sampleResult.responseMessage
        and:
        responseCode == listener.sampleResult.responseCode as int
        and:
        success == listener.sampleResult.successful
        where:
        statusReason     | responseCode | success
        ''               | 101          | true
        'response body'  | 200          | true
        'Page Not Found' | 404          | false
    }

    def "Should extract #headers upgrade response headers to sample result response headers as '#result'"() {
        given:
        UpgradeResponse upgradeResponse = new UpgradeResponse()
        upgradeResponse.headers = headers
        when:
        listener.onHandshakeResponse(upgradeResponse)
        then:
        listener.sampleResult.responseHeaders == result
        where:
        headers                                               || result
        [:]                                                   || ''
        ['header': ['value']]                                 || 'header=[value]'
        ['header': ['value']]                                 || 'header=[value]'
        ['header1': ['value one'], 'header2': ['value two']]  || 'header1=[value one]\nheader2=[value two]'
        ['multiple_value_header': ['value one', 'value two']] || 'multiple_value_header=[value one, value two]'
    }
}
