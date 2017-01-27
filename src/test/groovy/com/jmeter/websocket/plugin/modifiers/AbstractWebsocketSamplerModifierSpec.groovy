package com.jmeter.websocket.plugin.modifiers

import com.jmeter.websocket.plugin.JmeterAbstractSpec
import com.jmeter.websocket.plugin.samplers.WebsocketMessageSampler
import com.jmeter.websocket.plugin.samplers.WebsocketMessageSamplerGui
import com.jmeter.websocket.plugin.samplers.WebsocketSessionSampler
import com.jmeter.websocket.plugin.samplers.WebsocketSessionSamplerGui
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class AbstractWebsocketSamplerModifierSpec extends JmeterAbstractSpec {
    
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
            'serverNameOrIp' | 'serverNameOrIp' | '127.0.0.1'
            'portNumber'     | 'portNumber'     | '80'
            'protocol'       | 'protocol'       | 'ws'
            'path'           | 'path'           | '/websocket'
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
            'serverNameOrIp' | 'serverNameOrIp' | '127.0.0.1'
            'portNumber'     | 'portNumber'     | '80'
            'protocol'       | 'protocol'       | 'ws'
            'path'           | 'path'           | '/websocket'
    }
}
