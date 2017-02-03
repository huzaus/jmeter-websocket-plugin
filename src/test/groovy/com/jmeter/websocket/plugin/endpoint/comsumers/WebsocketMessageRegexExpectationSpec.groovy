package com.jmeter.websocket.plugin.endpoint.comsumers

import com.google.common.base.Supplier
import com.jmeter.websocket.plugin.elements.helpers.ExpectationResult
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock
import java.util.regex.Pattern

import static com.jmeter.websocket.plugin.elements.helpers.ExpectationResult.SUCCESS
import static com.jmeter.websocket.plugin.elements.helpers.ExpectationResult.TIMEOUT
import static com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketMessageRegexExpectation.lockSupplier
import static java.util.concurrent.TimeUnit.MILLISECONDS

class WebsocketMessageRegexExpectationSpec extends Specification {

    @Subject
    WebsocketMessageRegexExpectation expectation

    def "Should throw NullPointerException when regex is null on new"() {
        when:
            expectation = new WebsocketMessageRegexExpectation(regex)
        then:
            thrown(NullPointerException)
        where:
            regex = null
    }

    def "Should return not null and the same pattern on getPattern()"() {
        given:
            expectation = new WebsocketMessageRegexExpectation(regex)
        when:
            Pattern pattern = expectation.pattern
        then:
            pattern.is(expectation.pattern)
        where:
            regex = '.*'
    }

    def "Should return not null and new lock lockSupplier().get()"() {
        given:
            expectation = new WebsocketMessageRegexExpectation(regex)
        when:
            Lock lock = expectation.lockSupplier.get()
        then:
            !lock.is(expectation.lockSupplier.get())
        where:
            regex = '.*'
    }

    def "Should acquire lock, release lock and signal result resultCondition on onMessageReceive()"() {
        given:
            Condition condition = Mock()
            Lock lock = Mock()
            lockSupplier = Stub(Supplier) {
                get() >> lock
            }
        when:
            expectation = new WebsocketMessageRegexExpectation(regex)
        and:
            expectation.onMessageReceive(sessionId, message)
        then:
            1 * lock.newCondition() >> condition
        and:
            1 * lock.lock()
        and:
            1 * condition.signal()
        and:
            1 * lock.unlock()
        cleanup: 'restore default supplier'
            lockSupplier = lockSupplier()
        where:
            sessionId      | message   | regex
            'user1Session' | 'message' | '.*'
    }

    def "Should acquire lock, release lock and signal resultCondition on getResult()"() {
        given:
            Condition condition = Mock()
            Lock lock = Mock()
            lockSupplier = Stub(Supplier) {
                get() >> lock
            }
        when:
            expectation = new WebsocketMessageRegexExpectation(regex)
        and:
            expectation.getResult(timeout)
        then:
            1 * lock.newCondition() >> condition
        and:
            1 * lock.lock()
        and:
            1 * condition.await(timeout, MILLISECONDS)
        and:
            1 * lock.unlock()
        cleanup: 'restore default supplier'
            lockSupplier = lockSupplier()
        where:
            timeout | regex
            2000    | '.*'
    }

    @Unroll
    def "Should return result with success = '#expectedSuccess' and reason = '#expectedReason' result when regex = '#regex' and incoming message = '#message' is incorrect on getResult()"() {
        given:
            expectation = new WebsocketMessageRegexExpectation(regex)
        when: 'expectation notified about message'
            expectation.onMessageReceive(sessionId, message)
        and: 'expectation asked about result'
            ExpectationResult result = expectation.getResult(timeout)
        then:
            with(result) {
                success == expectedSuccess
                reason.contains expectedReason
            }
        where:
            regex     | sessionId      | message   | timeout || expectedSuccess | expectedReason
            '*.'      | 'user1Session' | 'message' | 2000    || false           | 'Dangling meta character \'*\' near index 0'
            'Message' | 'user1Session' | 'message' | 10      || false           | TIMEOUT
            'message' | 'user1Session' | 'message' | 2000    || true            | SUCCESS
    }


}
