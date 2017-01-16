package JMeter.plugins.functional.samplers.websocket

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static JMeter.plugins.functional.samplers.websocket.WebsocketMessageSampler.CONNECT_TIME_OUT
import static JMeter.plugins.functional.samplers.websocket.WebsocketMessageSampler.MESSAGE
import static JMeter.plugins.functional.samplers.websocket.WebsocketMessageSampler.PATH
import static JMeter.plugins.functional.samplers.websocket.WebsocketMessageSampler.PORT_NUMBER
import static JMeter.plugins.functional.samplers.websocket.WebsocketMessageSampler.PROTOCOL
import static JMeter.plugins.functional.samplers.websocket.WebsocketMessageSampler.RESPONSE_TIME_OUT
import static JMeter.plugins.functional.samplers.websocket.WebsocketMessageSampler.SERVER_NAME_OR_IP

@Unroll
class WebsocketMessageSamplerSpec extends Specification {

    @Subject
    WebsocketMessageSampler sampler = new WebsocketMessageSampler()

    def "Should set #field as getPropertyAs#type(#property) "() {
        when:
        sampler."$field" = value
        then:
        sampler."getPropertyAs$type"(property) == value
        where:
        field             | property          | type     | value
        'serverNameOrIp'  | SERVER_NAME_OR_IP | 'String' | '127.0.0.1'
        'portNumber'      | PORT_NUMBER       | 'String' | '8080'
        'protocol'        | PROTOCOL          | 'String' | 'ws'
        'connectTimeOut'  | CONNECT_TIME_OUT  | 'String' | '2000'
        'responseTimeOut' | RESPONSE_TIME_OUT | 'String' | '5000'
        'path'            | PATH              | 'String' | '/websocket'
        'message'         | MESSAGE           | 'String' | 'CONNECT\\naccept-version:1.1,1.0\\nheart-beat:10000,10000\\n\\n\\u0000'
    }
}
