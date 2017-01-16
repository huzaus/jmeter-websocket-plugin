package JMeter.plugins.functional.samplers.websocket

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static org.apache.jmeter.util.JMeterUtils.initLocale
import static org.apache.jmeter.util.JMeterUtils.loadJMeterProperties
import static org.apache.jmeter.util.JMeterUtils.setJMeterHome

@Unroll
class WebsocketMessageSamplerGuiSpec extends JmeterAbstractSpec {

    @Subject
    WebsocketMessageSamplerGui websocketMessageSamplerGui = new WebsocketMessageSamplerGui()

    def "#field should be initialized in constructor"() {
        expect:
        websocketMessageSamplerGui."$field" != null
        where:
        field << ['serverNameOrIp', 'portNumber', 'protocol', 'connectTimeOut', 'responseTimeOut', 'path', 'message']
    }

    def "get#field should return #value"() {
        when:
        websocketMessageSamplerGui = new WebsocketMessageSamplerGui()
        then:
        websocketMessageSamplerGui."$field" == value
        where:
        field           || value
        'labelResource' || 'websocket.message.sampler.title'
        'staticLabel'   || 'Websocket Message Sampler'
    }

}
