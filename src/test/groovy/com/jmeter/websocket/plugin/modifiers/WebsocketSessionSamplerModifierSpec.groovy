package com.jmeter.websocket.plugin.modifiers

import com.jmeter.websocket.plugin.JmeterAbstractSpec
import com.jmeter.websocket.plugin.samplers.WebsocketSessionSampler
import com.jmeter.websocket.plugin.samplers.WebsocketSessionSamplerGui
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class WebsocketSessionSamplerModifierSpec extends JmeterAbstractSpec {

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
    }

}
