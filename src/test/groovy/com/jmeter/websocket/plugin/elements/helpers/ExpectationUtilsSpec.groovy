package com.jmeter.websocket.plugin.elements.helpers

import org.apache.jmeter.assertions.AssertionResult
import spock.lang.Specification

import static com.jmeter.websocket.plugin.elements.helpers.ExpectationResult.failure
import static com.jmeter.websocket.plugin.elements.helpers.ExpectationUtils.UNKNOWN

class ExpectationUtilsSpec extends Specification {

    def "Should return expectation with predefined result on expectationFrom()"() {
        when:
            Expectation expectation = ExpectationUtils.expectationFrom(predefinedResult)
        then:
            expectation.getResult(timeout).is predefinedResult
        where:
            predefinedResult      | timeout
            failure('predefined') | 2000
    }

    def "Should return expectation with failure result with predefined reason on failedExpectationWithReason()"() {
        when:
            Expectation expectation = ExpectationUtils.failedExpectationWithReason(predefinedReason)
        then:
            with(expectation.getResult(timeout)) {
                !success
                reason == predefinedReason
            }

        where:
            predefinedReason | timeout
            'timeout'        | 2000
    }

    def "Should convert expectation result to assertion result with given name on failedExpectationWithReason()"() {
        when:
            AssertionResult assertionResult = ExpectationUtils.assertionResultFrom(assertionName, expectationResult)
        then:
            with(assertionResult) {
                name == assertionName
                failure == !expectationResult.success
                failureMessage == expectationResult.reason
            }
        where:
            assertionName          | expectationResult  | timeout
            'connection assertion' | failure('timeout') | 2000
    }

    def "Should return default assertion result with given name when expectationResult is null on failedExpectationWithReason()"() {
        when:
            AssertionResult assertionResult = ExpectationUtils.assertionResultFrom(assertionName, expectationResult)
        then:
            with(assertionResult) {
                name == assertionName
                error
                failureMessage == UNKNOWN
            }
        where:
            assertionName          | expectationResult | timeout
            'connection assertion' | null              | 2000
    }
}
