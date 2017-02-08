package com.jmeter.websocket.plugin.elements.assertions

import com.jmeter.websocket.plugin.elements.helpers.Expectation
import com.jmeter.websocket.plugin.elements.helpers.ExpectationResult
import com.jmeter.websocket.plugin.elements.helpers.ExpectationUtils
import com.jmeter.websocket.plugin.endpoint.SessionsManager
import com.jmeter.websocket.plugin.endpoint.WebsocketSession
import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketMessageRegexExpectation
import org.apache.jmeter.assertions.AssertionResult
import org.apache.jmeter.samplers.SampleResult
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.jmeter.websocket.plugin.elements.assertions.WebsocketRegexAssertion.REGEX_ID
import static com.jmeter.websocket.plugin.elements.assertions.WebsocketRegexAssertion.SESSION_ID
import static com.jmeter.websocket.plugin.elements.assertions.WebsocketRegexAssertion.TIMEOUT
import static com.jmeter.websocket.plugin.elements.helpers.ExpectationResult.success

class WebsocketRegexAssertionSpec extends Specification {

    @Subject
    WebsocketRegexAssertion assertion = new WebsocketRegexAssertion()

    def "Should return null expectation on newly expectation on getExpectation()"() {
        expect:
            assertion.getExpectation() == null
    }

    @Unroll
    def "#field field value should be stored in #property property of #type type"() {
        when:
            assertion."$field" = value
        then:
            assertion."getPropertyAs$type"(property) == value
        where:
            field       | property   | type     | value
            'sessionId' | SESSION_ID | 'String' | 'user1Session'
            'timeout'   | TIMEOUT    | 'String' | '2000'
            'regex'     | REGEX_ID   | 'String' | '.*'
    }

    def "Should set expectation with predefined failure result on process()"() {
        given:
            assertion.setSessionId(sessionId)
        when:
            assertion.process()
        then:
            with(assertion.expectation.getResult(timeout)) {
                !success
                reason == sessionId + expectedReason
            }
        where:
            sessionId      | expectedReason  | timeout
            'user1Session' | ' is not open.' | 2000
    }

    def "Should set WebsocketMessageRegexExpectation expectation and register it when session is open on process()"() {
        given:
            WebsocketMessageRegexExpectation websocketMessageRegexExpectation
            assertion.setSessionId(sessionId)
            WebsocketSession session = Mock()
            assertion.sessionsManager = Stub(SessionsManager) {
                getOpenSession(sessionId) >> session
            }
        when:
            assertion.process()
        then:
            1 * session.registerWebsocketIncomingMessageConsumer(_) >> { arguments -> websocketMessageRegexExpectation = arguments[0] }
        and:
            websocketMessageRegexExpectation.is(assertion.expectation)
        where:
            sessionId      | expectedReason
            'user1Session' | ' is not open.'
    }

    def "Should return failure when exception occurs on getResult()"() {
        when:
            AssertionResult assertionResult = assertion.getResult(sampleResult)
        then:
            with(assertionResult) {
                failure
                failureMessage.contains 'Exception'
            }
        where:
            sampleResult = new SampleResult()
    }

    def "Should unregister expectation when exception is WebsocketMessageRegexExpectation and session is open on getResult()"() {
        given:
            assertion.setSessionId(sessionId)
            assertion.setTimeout(timeout)
            assertion.expectation = Stub(WebsocketMessageRegexExpectation) {
                getResult(timeout as Long) >> expectationResult
            }
            WebsocketSession session = Mock()
            assertion.sessionsManager = Mock(SessionsManager)
        when:
            assertion.getResult(sampleResult)
        then:
            1 * assertion.sessionsManager.hasOpenSession(sessionId) >> true
        and:
            1 * assertion.sessionsManager.getOpenSession(sessionId) >> session
        and:
            1 * session.unregisterWebsocketIncomingMessageConsumer(assertion.expectation)
        where:
            sessionId      | sampleResult       | timeout | expectationResult
            'user1Session' | new SampleResult() | '2000'  | success()
    }

    def "Should not unregister expectation when exception is not WebsocketMessageRegexExpectation on getResult()"() {
        given:
            assertion.setSessionId(sessionId)
            assertion.setTimeout(timeout)
            assertion.expectation = Stub(Expectation) {
                getResult(timeout as Long) >> expectationResult
            }
            assertion.sessionsManager = Mock(SessionsManager)
        when:
            assertion.getResult(sampleResult)
        then:
            0 * assertion.sessionsManager.hasOpenSession(sessionId)
        and:
            0 * assertion.sessionsManager.getOpenSession(sessionId)
        where:
            sessionId      | sampleResult       | timeout | expectationResult
            'user1Session' | new SampleResult() | '2000'  | success()
    }

    def "Should not unregister expectation when exception is WebsocketMessageRegexExpectation and session is open on getResult()"() {
        given:
            assertion.setSessionId(sessionId)
            assertion.setTimeout(timeout)
            assertion.expectation = Stub(WebsocketMessageRegexExpectation) {
                getResult(timeout as Long) >> expectationResult
            }
            assertion.sessionsManager = Mock(SessionsManager)
        when:
            assertion.getResult(sampleResult)
        then:
            1 * assertion.sessionsManager.hasOpenSession(sessionId) >> false
        and:
            0 * assertion.sessionsManager.getOpenSession(sessionId)
        where:
            sessionId      | sampleResult       | timeout | expectationResult
            'user1Session' | new SampleResult() | '2000'  | success()
    }

    def "Should return assertion result constructed from expectation result and set end time for sample result on getResult()"() {
        given:
            sampleResult.sampleStart()
            assertion.setName(name)
            assertion.setTimeout(timeout)
            ExpectationResult expectationResult = success()
            assertion.expectation = ExpectationUtils.expectationFrom(expectationResult)
        when:
            AssertionResult assertionResult = assertion.getResult(sampleResult)
        then:
            with(assertionResult) {
                failure == !expectationResult.success
                failureMessage == expectationResult.reason
            }
        and:
            sampleResult.endTime == expectationResult.endTime
        where:
            sampleResult       | name                      | timeout
            new SampleResult() | 'WebsocketRegexAssertion' | '2000'
    }
}