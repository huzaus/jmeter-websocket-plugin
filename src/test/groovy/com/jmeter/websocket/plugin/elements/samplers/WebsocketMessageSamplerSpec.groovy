package com.jmeter.websocket.plugin.elements.samplers

import com.jmeter.websocket.plugin.elements.configurations.WebsocketSessionsManager
import org.apache.jmeter.protocol.http.control.HeaderManager
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.jmeter.websocket.plugin.elements.samplers.AbstractWebsocketSampler.SESSION_ID
import static com.jmeter.websocket.plugin.elements.samplers.AbstractWebsocketSampler.WEBSOCKET_MANAGER
import static com.jmeter.websocket.plugin.elements.samplers.WebsocketMessageSampler.MESSAGE

@Unroll
class WebsocketMessageSamplerSpec extends Specification {
    
    @Subject
    WebsocketMessageSampler sampler = new WebsocketMessageSampler()
    
    def "#field field value should be stored in #property property of #type type"() {
        when:
            sampler."$field" = value
        then:
            sampler."getPropertyAs$type"(property) == value
        where:
            field       | property   | type     | value
            'message'   | MESSAGE    | 'String' | 'message'
            'sessionId' | SESSION_ID | 'String' | 'user1Session'
    }
    
    def "Should not set #property property via addTestElement method when class type is not matched"() {
        when:
            sampler.addTestElement(element)
        then:
            sampler.getProperty(property).objectValue == null
        where:
            property          | element
            WEBSOCKET_MANAGER | new HeaderManager()
    }
    
    def "Should set #property property via addTestElement method when class type is matched"() {
        when:
            sampler.addTestElement(manager)
        then:
            sampler.getProperty(property).objectValue == manager
        where:
            property          | manager
            WEBSOCKET_MANAGER | new WebsocketSessionsManager()
    }
    
    def "Should return null when #property is not set"() {
        expect:
            sampler."$property" == null
        where:
            property = "websocketSessionsManager"
    }
    
    def "Should return value when #property is set to #value"() {
        when:
            sampler."$property" = value
        then:
            sampler."$property" == value
        where:
            property                   | value
            "websocketSessionsManager" | new WebsocketSessionsManager()
            "websocketSessionsManager" | null
    }
}
