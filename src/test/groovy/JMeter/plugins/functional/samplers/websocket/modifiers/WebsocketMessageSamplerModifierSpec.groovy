package JMeter.plugins.functional.samplers.websocket.modifiers

import JMeter.plugins.functional.samplers.websocket.JmeterAbstractSpec
import JMeter.plugins.functional.samplers.websocket.WebsocketMessageSampler
import JMeter.plugins.functional.samplers.websocket.WebsocketMessageSamplerGui
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class WebsocketMessageSamplerModifierSpec extends JmeterAbstractSpec {

    @Subject
    WebsocketMessageSamplerModifier modifier = new WebsocketMessageSamplerModifier()

    def "Should set #field sampler field to '#value'(value of #component gui component)"() {
        given:
        WebsocketMessageSamplerGui gui = new WebsocketMessageSamplerGui()
        gui."$component".text = value
        WebsocketMessageSampler sampler = new WebsocketMessageSampler()
        when:
        modifier.modify(gui, sampler)
        then:
        sampler."$field" == value
        where:
        component         | field             | value
        'serverNameOrIp'  | 'serverNameOrIp'  | '127.0.0.1'
        'portNumber'      | 'portNumber'      | '80'
        'protocol'        | 'protocol'        | 'ws'
        'connectTimeOut'  | 'connectTimeOut'  | '2000'
        'responseTimeOut' | 'responseTimeOut' | '5000'
        'path'            | 'path'            | '/websocket'
        'message'         | 'message'         | 'CONNECT\\naccept-version:1.1,1.0\\nheart-beat:10000,10000\\n\\n\\u0000'
    }

}
