package JMeter.plugins.functional.samplers.websocket

import org.apache.jmeter.samplers.SampleResult
import org.eclipse.jetty.websocket.api.UpgradeRequest
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

    def "Should extract #headers upgrade request headers to sample result headers as '#result'"() {
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
}
