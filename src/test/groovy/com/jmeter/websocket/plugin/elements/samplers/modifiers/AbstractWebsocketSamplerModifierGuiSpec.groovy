package com.jmeter.websocket.plugin.elements.samplers.modifiers

import com.jmeter.websocket.plugin.JmeterAbstractGuiSpec
import com.jmeter.websocket.plugin.elements.samplers.WebsocketMessageSampler
import com.jmeter.websocket.plugin.elements.samplers.WebsocketMessageSamplerGui
import com.jmeter.websocket.plugin.elements.samplers.WebsocketSessionSampler
import com.jmeter.websocket.plugin.elements.samplers.WebsocketSessionSamplerGui
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class AbstractWebsocketSamplerModifierGuiSpec extends JmeterAbstractGuiSpec {
    
    @Subject
    AbstractWebsocketSamplerModifier modifier = new AbstractWebsocketSamplerModifier()
    
    def "Should set #field WebsocketSessionSampler field to '#value' value of #component WebsocketSessionSamplerGui component"() {
        given:
            WebsocketSessionSamplerGui gui = new WebsocketSessionSamplerGui()
            gui."$component".text = value
            WebsocketSessionSampler sampler = new WebsocketSessionSampler()
        when:
            modifier.modify(gui, sampler)
        then:
            sampler."$field" == value
        where:
            component        | field            | value
            'sessionId' | 'sessionId' | 'user1Session'
    }
    
    def "Should set #field WebsocketMessageSampler field to '#value' value of #component WebsocketMessageSamplerGui component"() {
        given:
            WebsocketMessageSamplerGui gui = new WebsocketMessageSamplerGui()
            gui."$component".text = value
            WebsocketMessageSampler sampler = new WebsocketMessageSampler()
        when:
            modifier.modify(gui, sampler)
        then:
            sampler."$field" == value
        where:
            component        | field            | value
            'sessionId' | 'sessionId' | 'user1Session'
    }
}
