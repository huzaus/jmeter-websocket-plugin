package com.jmeter.websocket.plugin.endpoint.jetty.converters

import com.jmeter.websocket.plugin.endpoint.jetty.JettyWebsocketUpgradeListener
import org.apache.jmeter.samplers.SampleResult
import org.eclipse.jetty.websocket.client.io.UpgradeListener
import spock.lang.Specification
import spock.lang.Subject

class SampleResultToUpgradeListenerConverterSpec extends Specification {
    @Subject
    SampleResultToUpgradeListenerConverter converter = new SampleResultToUpgradeListenerConverter()

    def "Should return null when sample result is null"() {
        expect:
        converter.apply(null) == null
    }

    def "Should return JettyWebsocketUpgradeListener when sample result is not null"() {
        given:
        SampleResult sampleResult = new SampleResult()
        when:
        UpgradeListener listener = converter.apply(sampleResult)
        then:
        listener != null
        and:
        listener instanceof JettyWebsocketUpgradeListener
        and:
        listener.sampleResult.is(sampleResult)
    }
}
