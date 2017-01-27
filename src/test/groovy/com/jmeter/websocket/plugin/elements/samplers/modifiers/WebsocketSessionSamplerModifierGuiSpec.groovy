package com.jmeter.websocket.plugin.elements.samplers.modifiers

import com.jmeter.websocket.plugin.JmeterAbstractGuiSpec
import com.jmeter.websocket.plugin.elements.samplers.WebsocketSessionSampler
import com.jmeter.websocket.plugin.elements.samplers.WebsocketSessionSamplerGui
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class WebsocketSessionSamplerModifierGuiSpec extends JmeterAbstractGuiSpec {
    
    @Subject
    WebsocketSessionSamplerModifier modifier = new WebsocketSessionSamplerModifier()
    
    def "Should set #field sampler field to '#value'(value of #component gui component)"() {
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
            'connectTimeOut' | 'connectTimeOut' | '2000'
            'serverNameOrIp' | 'serverNameOrIp' | '127.0.0.1'
            'portNumber'     | 'portNumber'     | '80'
            'protocol'       | 'protocol'       | 'ws'
            'path'           | 'path'           | '/websocket'
    }
    
}
