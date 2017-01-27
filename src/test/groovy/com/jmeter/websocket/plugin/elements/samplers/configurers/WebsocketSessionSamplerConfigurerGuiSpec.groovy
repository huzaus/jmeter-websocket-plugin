package com.jmeter.websocket.plugin.elements.samplers.configurers

import com.jmeter.websocket.plugin.JmeterAbstractGuiSpec
import com.jmeter.websocket.plugin.elements.samplers.WebsocketSessionSampler
import com.jmeter.websocket.plugin.elements.samplers.WebsocketSessionSamplerGui
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class WebsocketSessionSamplerConfigurerGuiSpec extends JmeterAbstractGuiSpec {
    
    @Subject
    WebsocketSessionSamplerConfigurer configurer = new WebsocketSessionSamplerConfigurer()
    
    def "Should set #component gui component value to #value from #property sampler property"() {
        given:
            WebsocketSessionSamplerGui gui = new WebsocketSessionSamplerGui()
            WebsocketSessionSampler sampler = new WebsocketSessionSampler()
            sampler."$property" = value
        when:
            configurer.configure(sampler, gui)
        then:
            gui."$component".text == value
        where:
            component        | property         | value
            'connectTimeOut' | 'connectTimeOut' | '5000'
            'serverNameOrIp' | 'serverNameOrIp' | '127.0.0.1'
            'portNumber'     | 'portNumber'     | '80'
            'protocol'       | 'protocol'       | 'ws'
            'path'           | 'path'           | '/websocket'
    }
    
}