package com.jmeter.websocket.plugin.endpoint.comsumers;

import com.google.common.base.MoreObjects;
import com.google.common.base.Supplier;
import com.jmeter.websocket.plugin.elements.helpers.Expectation;
import com.jmeter.websocket.plugin.elements.helpers.ExpectationResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Suppliers.memoize;
import static com.jmeter.websocket.plugin.elements.helpers.ExpectationResult.failure;
import static com.jmeter.websocket.plugin.elements.helpers.ExpectationResult.success;
import static com.jmeter.websocket.plugin.elements.helpers.ExpectationResult.timeoutFailure;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class WebsocketMessageRegexExpectation implements WebsocketIncomingMessageConsumer, Expectation {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private static Supplier<Lock> lockSupplier = lockSupplier();
    private final String regex;
    private final Lock resultLock;
    private final Condition resultCondition;
    private ExpectationResult result;
    private Supplier<Pattern> patternSupplier = patternSupplier();

    public WebsocketMessageRegexExpectation(String regex) {
        checkNotNull(regex);
        this.regex = regex;
        resultLock = lockSupplier.get();
        resultCondition = resultLock.newCondition();
    }

    private static Supplier<Lock> lockSupplier() {
        return new Supplier<Lock>() {
            @Override
            public Lock get() {
                return new ReentrantLock();
            }
        };
    }

    @Override
    public void onMessageReceive(String sessionId, String message) {
        log.debug("in onMessageReceive()" +
                " sessionId=" + sessionId +
                " message=" + message);
        resultLock.lock();
        try {
            log.info("Matching message: " + message);
            if (result == null) {
                if (getPattern().matcher(message).matches()) {
                    log.error("Matched message: " + message);
                    result = success();
                }
            }
        } catch (Exception e) {
            log.error("Failure: ", e);
            result = failure(e.getMessage());
        } finally {
            resultCondition.signal();
            resultLock.unlock();
        }
    }

    @Override
    public ExpectationResult getResult(final long timeout) {
        log.info("Getting result");
        return fromNullable(result)
                .or(new Supplier<ExpectationResult>() {
                    @Override
                    public ExpectationResult get() {
                        resultLock.lock();
                        try {
                            log.info("Waiting for result");
                            resultCondition.await(timeout, MILLISECONDS);
                        } catch (InterruptedException e) {
                            result = failure(e.getMessage());
                        } finally {
                            resultLock.unlock();
                        }
                        return fromNullable(result)
                                .or(timeoutFailure(timeout));
                    }
                });
    }

    public Pattern getPattern() {
        return patternSupplier.get();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("regex", regex)
                .add("result", result)
                .toString();
    }

    private Supplier<Pattern> patternSupplier() {
        return memoize(new Supplier<Pattern>() {
            @Override
            public Pattern get() {
                return Pattern.compile(regex);
            }
        });
    }
}
