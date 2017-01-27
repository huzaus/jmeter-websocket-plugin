package com.jmeter.websocket.plugin.endpoint.jetty.converters

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class HeadersToClientUpgradeRequestConverterSpec extends Specification {

    @Subject
    HeadersToClientUpgradeRequestConverter converter = new HeadersToClientUpgradeRequestConverter()

    def "Should create ClientUpgradeRequest with empty headers when headers is null"() {
        when:
        ClientUpgradeRequest request = converter.apply(null)
        then:
        request.headers.isEmpty()
    }

    def "Should create new upgrade request with provided [#name:#values] headers on apply"() {
        given:
        Map headers = [(name): values]
        when:
        ClientUpgradeRequest request = converter.apply(headers)
        then:
        headers == request.headers
        where:
        name      | values
        'Session' | null
        'Session' | ['1']
        'Session' | ['1', '2']
    }
}
