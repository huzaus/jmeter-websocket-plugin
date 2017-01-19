package JMeter.plugins.functional.samplers.websocket

import org.apache.jmeter.protocol.http.sampler.HTTPSampler
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class WebsocketSessionSamplerGuiSpec extends JmeterAbstractSpec {

    @Subject
    WebsocketSessionSamplerGui websocketMessageSamplerGui = new WebsocketSessionSamplerGui()

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
        websocketMessageSamplerGui = new WebsocketSessionSamplerGui()
        then:
        websocketMessageSamplerGui."$field"
        where:
        field           || value
        'labelResource' || 'websocket.message.sampler.title'
        'staticLabel'   || 'Websocket Message Sampler'
    }

    def "Should modify #property property with '#value' value from gui #component component"() {
        given:
        WebsocketSessionSampler sampler = new WebsocketSessionSampler()
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

    def "Should configure gui #component component with '#value' value from #property sampler property"() {
        given:
        WebsocketSessionSampler sampler = new WebsocketSessionSampler()
        sampler."$property" = value
        when:
        websocketMessageSamplerGui.configure(sampler)
        then:
        websocketMessageSamplerGui."$component".text == value
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

    def "Should throw ClassCastException when testElement is not WebsocketMessageSampler on configure"() {
        when:
        websocketMessageSamplerGui.configure(new HTTPSampler())
        then:
        thrown(ClassCastException)
    }

    def "Should throw ClassCastException when testElement is not WebsocketMessageSampler on modifyTestElement"() {
        when:
        websocketMessageSamplerGui.modifyTestElement(new HTTPSampler())
        then:
        thrown(ClassCastException)
    }
}