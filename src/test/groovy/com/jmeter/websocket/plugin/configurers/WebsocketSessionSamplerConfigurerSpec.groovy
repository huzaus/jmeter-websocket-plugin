package com.jmeter.websocket.plugin.configurers

import com.jmeter.websocket.plugin.JmeterAbstractSpec
import com.jmeter.websocket.plugin.samplers.WebsocketSessionSampler
import com.jmeter.websocket.plugin.samplers.WebsocketSessionSamplerGui
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class WebsocketSessionSamplerConfigurerSpec extends JmeterAbstractSpec {

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
    }

}