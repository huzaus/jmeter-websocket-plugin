package com.jmeter.websocket.plugin.elements.samplers.configurers

import com.jmeter.websocket.plugin.JmeterAbstractGuiSpec
import com.jmeter.websocket.plugin.elements.samplers.WebsocketMessageSampler
import com.jmeter.websocket.plugin.elements.samplers.WebsocketMessageSamplerGui
import com.jmeter.websocket.plugin.elements.samplers.WebsocketSessionSampler
import com.jmeter.websocket.plugin.elements.samplers.WebsocketSessionSamplerGui
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class AbstractWebsocketSamplerConfigurerGuiSpec extends JmeterAbstractGuiSpec {
    
    @Subject
    AbstractWebsocketSamplerConfigurer configurer = new AbstractWebsocketSamplerConfigurer()
    
    def "Should set #component WebsocketMessageSamplerGui component value to #value from #property WebsocketMessageSampler property"() {
        given:
            WebsocketMessageSamplerGui gui = new WebsocketMessageSamplerGui()
            WebsocketMessageSampler sampler = new WebsocketMessageSampler()
            sampler."$property" = value
        when:
            configurer.configure(sampler, gui)
        then:
            gui."$component".text == value
        where:
            component   | property    | value
            'sessionId' | 'sessionId' | 'user1Session'
    }
    
    def "Should set #component WebsocketSessionSamplerGui component value to #value from #property WebsocketSessionSampler property"() {
        given:
            WebsocketSessionSamplerGui gui = new WebsocketSessionSamplerGui()
            WebsocketSessionSampler sampler = new WebsocketSessionSampler()
            sampler."$property" = value
        when:
            configurer.configure(sampler, gui)
        then:
            gui."$component".text == value
        where:
            component   | property    | value
            'sessionId' | 'sessionId' | 'user1Session'
    }
}
