package com.jmeter.websocket.plugin.endpoint.jetty.converters

import org.apache.jmeter.protocol.http.control.Cookie
import org.apache.jmeter.protocol.http.control.CookieManager
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class CookieManagerToCookieStoreConverterSpec extends Specification {
    
    @Subject
    CookieManagerToCookieStoreConverter converter = new CookieManagerToCookieStoreConverter()
    
    def "Should return empty cookie store when CookieManager is null"() {
        expect:
            converter.apply(null).cookies.empty
    }
    
    def "Should create not empty cookies with [#name, #value, #domain, #path, #secure, #expires] value when cookieManager is set"() {
        given:
            CookieManager cookieManager = new CookieManager()
        and:
            cookieManager.add(new Cookie(name, value, domain, path, secure, expires))
        when:
            CookieStore cookies = converter.apply(cookieManager)
        then:
            cookies.cookies.contains new HttpCookie(name, domain)
        where:
            name      | value | domain      | path         | secure | expires
            'Session' | '1'   | '127.0.0.1' | '/websocket' | true   | 0
    }
}
