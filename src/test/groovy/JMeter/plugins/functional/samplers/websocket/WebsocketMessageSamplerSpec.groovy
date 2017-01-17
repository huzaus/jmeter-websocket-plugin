package JMeter.plugins.functional.samplers.websocket

import org.apache.jmeter.protocol.http.control.CookieManager
import org.apache.jmeter.protocol.http.control.HeaderManager
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static JMeter.plugins.functional.samplers.websocket.WebsocketMessageSampler.CONNECT_TIME_OUT
import static JMeter.plugins.functional.samplers.websocket.WebsocketMessageSampler.COOKIE_MANAGER
import static JMeter.plugins.functional.samplers.websocket.WebsocketMessageSampler.HEADER_MANAGER
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

    def "#field field value should be stored in #property property of #type type"() {
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

    def "Should not set #property property via addTestElement method when class type is not matched"() {
        when:
        sampler.addTestElement(manager)
        then:
        sampler.getProperty(property).objectValue == null
        where:
        property       | manager
        COOKIE_MANAGER | new HeaderManager()
        HEADER_MANAGER | new CookieManager()
    }

    def "Should set #property property via addTestElement method when class type is matched"() {
        when:
        sampler.addTestElement(manager)
        then:
        sampler.getProperty(property).objectValue == manager
        where:
        property       | manager
        COOKIE_MANAGER | new CookieManager()
        HEADER_MANAGER | new HeaderManager()
    }

    def "Should return null when #property is not set"() {
        expect:
        sampler."$property" == null
        where:
        property << ["cookieManager", "headerManager"]

    }

    def "Should return value when #property is set"() {
        when:
        sampler."$property" = value
        then:
        sampler."$property" == value
        where:
        property        | value
        "cookieManager" | new CookieManager()
        "cookieManager" | null
        "headerManager" | new HeaderManager()
        "headerManager" | null
    }
}
