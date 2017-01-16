package JMeter.plugins.functional.samplers.websocket

import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class WebsocketMessageSamplerGuiSpec extends JmeterAbstractSpec {

    @Subject
    WebsocketMessageSamplerGui websocketMessageSamplerGui = new WebsocketMessageSamplerGui()

    def "#component should be initialized in constructor with #label"() {
        expect:
        websocketMessageSamplerGui."$component".mLabel.text == label
        where:
        component         || label
        'serverNameOrIp'  || 'Server Name or IP:'
        'portNumber'      || 'Port Number:'
        'protocol'        || 'Protocol:'
        'connectTimeOut'  || 'Connect:'
        'responseTimeOut' || 'Response:'
        'path'            || 'Path:'
        'message'         || 'Message:'
    }

    def "get#field should return #value"() {
        when:
        websocketMessageSamplerGui = new WebsocketMessageSamplerGui()
        then:
        websocketMessageSamplerGui."$field"
        where:
        field           || value
        'labelResource' || 'websocket.message.sampler.title'
        'staticLabel'   || 'Websocket Message Sampler'
    }

    def "Should modify #property property with '#value' value from gui #component component"() {
        given:
        WebsocketMessageSampler sampler = new WebsocketMessageSampler()
        websocketMessageSamplerGui."$component".text = value
        when:
        websocketMessageSamplerGui.modifyTestElement(sampler)
        then:
        sampler."$property" == value
        where:
        component         | property          | value
        'serverNameOrIp'  | 'serverNameOrIp'  | '127.0.0.1'
        'portNumber'      | 'portNumber'      | '8080'
        'protocol'        | 'protocol'        | 'ws'
        'connectTimeOut'  | 'connectTimeOut'  | '2000'
        'responseTimeOut' | 'responseTimeOut' | '5000'
        'path'            | 'path'            | 'websocket'
        'message'         | 'message'         | 'CONNECT\\naccept-version:1.1,1.0\\nheart-beat:10000,10000\\n\\n\\u0000'
    }
}