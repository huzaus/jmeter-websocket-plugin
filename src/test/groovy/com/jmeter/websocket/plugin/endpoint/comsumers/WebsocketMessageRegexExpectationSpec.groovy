package com.jmeter.websocket.plugin.endpoint.comsumers

import com.google.common.base.Supplier
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.util.regex.Pattern

import static java.lang.System.currentTimeMillis

@Unroll
class WebsocketMessageRegexExpectationSpec extends Specification {
    
    @Subject
    WebsocketMessageRegexExpectation expectation
    
    def "Should throw NullPointerException when sessionId == '#sessionId' or pattern == '#pattern' on new"() {
        when:
            expectation = new WebsocketMessageRegexExpectation(sessionId, pattern)
        then:
            thrown(NullPointerException)
        where:
            sessionId      | pattern
            null           | '.*'
            'user1Session' | null
        
    }
    
    def "Should return not null the same pattern on getPattern()"() {
        given:
            expectation = new WebsocketMessageRegexExpectation('user1Session', '.*')
        when:
            Pattern pattern = expectation.pattern
        then:
            pattern != null
        and:
            pattern.is(expectation.pattern)
    }
    
    def "Should not check pattern match when result is finished on onMessageReceive"() {
        given:
            expectation = new WebsocketMessageRegexExpectation('user1Session', '.*')
            expectation.result = new ExpectationResult(endTime: currentTimeMillis(), success: false, finished: true)
            Pattern pattern = GroovyMock(Pattern)
            expectation.patternSupplier = Stub(Supplier) {
                get() >> pattern
            }
        when:
            expectation.onMessageReceive('session', 'message')
        then:
            0 * pattern._
    }
    
    def "Result should be success == '#expectedSuccess' and finished == '#expectedFinished' when '#pattern' pattern onMessageReceive('session', '#message')"() {
        given:
            expectation = new WebsocketMessageRegexExpectation('user1Session', pattern)
        when:
            expectation.onMessageReceive('session', message)
        then:
            with(expectation.result) {
                success == expectedSuccess
                finished == expectedFinished
                finished ? endTime > 0 : endTime == 0
            }
        where:
            pattern      | message           || expectedSuccess | expectedFinished
            '^message.*' | 'message1'        || true            | true
            '^message.*' | 'another message' || false           | false
    }
    
    def "Result should be success == 'false' and finished == 'true' when '#pattern' pattern is incorrect onMessageReceive('session', '#message')"() {
        given:
            expectation = new WebsocketMessageRegexExpectation('user1Session', pattern)
        when:
            expectation.onMessageReceive('session', message)
        then:
            with(expectation.result) {
                !success
                finished
                !reason.isEmpty()
                endTime > 0
            }
        where:
            pattern | message
            '{'     | 'message1'
    }
}
