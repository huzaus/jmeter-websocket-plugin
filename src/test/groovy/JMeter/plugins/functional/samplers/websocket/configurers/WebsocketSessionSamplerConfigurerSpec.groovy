package JMeter.plugins.functional.samplers.websocket.configurers

import JMeter.plugins.functional.samplers.websocket.JmeterAbstractSpec
import JMeter.plugins.functional.samplers.websocket.WebsocketSessionSampler
import JMeter.plugins.functional.samplers.websocket.WebsocketSessionSamplerGui
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class WebsocketSessionSamplerConfigurerSpec extends JmeterAbstractSpec {

    @Subject
    WebsocketSessionSamplerConfigurer modifier = new WebsocketSessionSamplerConfigurer()

    def "Should set #component gui component value to #value from #property sampler property"() {
        given:
        WebsocketSessionSamplerGui gui = new WebsocketSessionSamplerGui()
        WebsocketSessionSampler sampler = new WebsocketSessionSampler()
        sampler."$property" = value
        when:
        modifier.configure(sampler, gui)
        then:
        gui."$component".text == value
        where:
        component         | property          | value
        'serverNameOrIp'  | 'serverNameOrIp'  | '127.0.0.1'
        'portNumber'      | 'portNumber'      | '80'
        'protocol'        | 'protocol'        | 'ws'
        'connectTimeOut'  | 'connectTimeOut'  | '2000'
        'responseTimeOut' | 'responseTimeOut' | '5000'
        'path'            | 'path'            | '/websocket'
        'message'         | 'message'         | 'CONNECT\\naccept-version:1.1,1.0\\nheart-beat:10000,10000\\n\\n\\u0000'
    }

}