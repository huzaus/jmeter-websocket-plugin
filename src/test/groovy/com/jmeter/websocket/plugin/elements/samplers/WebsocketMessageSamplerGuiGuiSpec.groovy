package com.jmeter.websocket.plugin.elements.samplers

import com.jmeter.websocket.plugin.JmeterAbstractGuiSpec
import org.apache.jmeter.protocol.http.sampler.HTTPSampler
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class WebsocketMessageSamplerGuiGuiSpec extends JmeterAbstractGuiSpec {
    @Subject
    WebsocketMessageSamplerGui samplerGui = new WebsocketMessageSamplerGui()
    
    def "#component should be initialized in constructor with #label"() {
        expect:
            samplerGui."$component".mLabel.text == label
        where:
            component   || label
            'sessionId' || 'Session id:'
            'message'   || 'Message:'
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
            WebsocketMessageSampler sampler = new WebsocketMessageSampler()
            samplerGui."$component".text = value
        when:
            samplerGui.modifyTestElement(sampler)
        then:
            sampler."$property" == value
        where:
            component   | property    | value
            'message'   | 'message'   | 'Message'
            'sessionId' | 'sessionId' | 'user1Session'
    }
    
    def "Should configure gui #component component with '#value' value from #property sampler property"() {
        given:
            WebsocketMessageSampler sampler = new WebsocketMessageSampler()
            sampler."$property" = value
        when:
            samplerGui.configure(sampler)
        then:
            samplerGui."$component".text == value
        where:
            component   | property    | value
            'message'   | 'message'   | 'Message'
            'sessionId' | 'sessionId' | 'user1Session'
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
