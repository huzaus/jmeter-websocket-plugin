package com.jmeter.websocket.plugin.configurers

import com.jmeter.websocket.plugin.JmeterAbstractSpec
import com.jmeter.websocket.plugin.samplers.WebsocketMessageSampler
import com.jmeter.websocket.plugin.samplers.WebsocketMessageSamplerGui
import com.jmeter.websocket.plugin.samplers.WebsocketSessionSampler
import com.jmeter.websocket.plugin.samplers.WebsocketSessionSamplerGui
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class AbstractWebsocketSamplerConfigurerSpec extends JmeterAbstractSpec {

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
        component        | property         | value
        'serverNameOrIp' | 'serverNameOrIp' | '127.0.0.1'
        'portNumber'     | 'portNumber'     | '80'
        'protocol'       | 'protocol'       | 'ws'
        'path'           | 'path'           | '/websocket'
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
        component        | property         | value
        'serverNameOrIp' | 'serverNameOrIp' | '127.0.0.1'
        'portNumber'     | 'portNumber'     | '80'
        'protocol'       | 'protocol'       | 'ws'
        'path'           | 'path'           | '/websocket'
    }
}
