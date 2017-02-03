package com.jmeter.websocket.plugin.elements.samplers

import com.jmeter.websocket.plugin.JmeterAbstractGuiSpec
import org.apache.jmeter.protocol.http.sampler.HTTPSampler
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class WebsocketSessionSamplerGuiSpec extends JmeterAbstractGuiSpec {

    @Subject
    WebsocketSessionSamplerGui samplerGui = new WebsocketSessionSamplerGui()

    def "#component should be initialized in constructor with #label"() {
        expect:
            samplerGui."$component".mLabel.text == label
        where:
            component        || label
            'serverNameOrIp' || 'Server Name or IP:'
            'portNumber'     || 'Port Number:'
            'protocol'       || 'Protocol:'
            'connectTimeOut' || 'Timeout:'
            'path'           || 'Path:'
            'sessionId'      || 'Session id:'
    }

    def "get#field should return #value"() {
        expect:
            samplerGui."$field"
        where:
            field           || value
            'labelResource' || 'websocket.message.sampler.title'
            'staticLabel'   || 'Websocket Message Sampler'
    }

    def "Should modify #property property with '#value' value from gui #component component"() {
        given:
            WebsocketSessionSampler sampler = new WebsocketSessionSampler()
            samplerGui."$component".text = value
        when:
            samplerGui.modifyTestElement(sampler)
        then:
            sampler."$property" == value
        where:
            component        | property         | value
            'serverNameOrIp' | 'serverNameOrIp' | '127.0.0.1'
            'portNumber'     | 'portNumber'     | '8080'
            'protocol'       | 'protocol'       | 'ws'
            'connectTimeOut' | 'connectTimeOut' | '2000'
            'path'           | 'path'           | 'websocket'
            'sessionId'      | 'sessionId'      | 'user1Session'
    }

    def "Should configure gui #component component with '#value' value from #property sampler property"() {
        given:
            WebsocketSessionSampler sampler = new WebsocketSessionSampler()
            sampler."$property" = value
        when:
            samplerGui.configure(sampler)
        then:
            samplerGui."$component".text == value
        where:
            component        | property         | value
            'serverNameOrIp' | 'serverNameOrIp' | '127.0.0.1'
            'portNumber'     | 'portNumber'     | '8080'
            'protocol'       | 'protocol'       | 'ws'
            'connectTimeOut' | 'connectTimeOut' | '2000'
            'path'           | 'path'           | 'websocket'
            'sessionId'      | 'sessionId'      | 'user1Session'
    }

    def "Should throw ClassCastException when testElement is not WebsocketMessageSampler on configure"() {
        when:
            samplerGui.configure(new HTTPSampler())
        then:
            thrown(ClassCastException)
    }

    def "Should throw ClassCastException when testElement is not WebsocketMessageSampler on modifyTestElement"() {
        when:
            samplerGui.modifyTestElement(new HTTPSampler())
        then:
            thrown(ClassCastException)
    }
}