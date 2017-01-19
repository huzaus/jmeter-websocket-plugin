package com.jmeter.websocket.plugin.configurers

import com.jmeter.websocket.plugin.samplers.WebsocketSessionSampler
import com.jmeter.websocket.plugin.samplers.WebsocketSessionSamplerGui
import com.jmeter.websocket.plugin.JmeterAbstractSpec
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class WebsocketSessionSamplerConfigurerSpec extends JmeterAbstractSpec {

    @Subject
    WebsocketSessionSamplerConfigurer modifier = new WebsocketSessionSamplerConfigurer()

    def "Should set #component gui component value to #value from #property sampler property"() {
        given:
        WebsocketSessionSamplerGui gui = new WebsocketSessionSamplerGui()
        WebsocketSessionSampler sampler = new WebsocketSessionSampler()
        sampler."$property" = value
        when:
        modifier.configure(sampler, gui)
        then:
        gui."$component".text == value
        where:
        component         | property          | value
        'serverNameOrIp'  | 'serverNameOrIp'  | '127.0.0.1'
        'portNumber'      | 'portNumber'      | '80'
        'protocol'        | 'protocol'        | 'ws'
        'connectTimeOut'  | 'connectTimeOut'  | '2000'
        'responseTimeOut' | 'responseTimeOut' | '5000'
        'path'            | 'path'            | '/websocket'
    }

}