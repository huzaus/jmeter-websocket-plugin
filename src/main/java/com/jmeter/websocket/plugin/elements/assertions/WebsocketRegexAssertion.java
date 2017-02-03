package com.jmeter.websocket.plugin.elements.assertions;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.jmeter.websocket.plugin.elements.helpers.Expectation;
import com.jmeter.websocket.plugin.elements.helpers.ExpectationResult;
import com.jmeter.websocket.plugin.endpoint.SessionsManager;
import com.jmeter.websocket.plugin.endpoint.WebsocketSession;
import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketMessageRegexExpectation;
import org.apache.jmeter.assertions.Assertion;
import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import static com.google.common.base.Optional.fromNullable;
import static com.jmeter.websocket.plugin.elements.configurations.WebsocketSessionsManager.getWebsocketClient;
import static com.jmeter.websocket.plugin.elements.helpers.ExpectationResult.failure;
import static com.jmeter.websocket.plugin.elements.helpers.ExpectationUtils.assertionResultFrom;
import static com.jmeter.websocket.plugin.elements.helpers.ExpectationUtils.failedExpectationWithReason;

public class WebsocketRegexAssertion extends AbstractTestElement implements PreProcessor, Assertion {

    public static final String SESSION_ID = "sessionId";
    public static final String TIMEOUT = "timeout";
    public static final String REGEX_ID = "regex";

    private static final Logger log = LoggingManager.getLoggerForClass();

    private Expectation expectation;

    private SessionsManager<String> sessionsManager = getWebsocketClient().getSessionsManager();

    @Override
    public void process() {
        log.info("in process");
        WebsocketSession session = sessionsManager.getOpenSession(getSessionId());
        expectation = fromNullable(session)
                .transform(new Function<WebsocketSession, Expectation>() {
                    @Override
                    public Expectation apply(WebsocketSession session) {
                        WebsocketMessageRegexExpectation websocketMessageRegexExpectation = expectationSupplier().get();
                        session.registerWebsocketIncomingMessageConsumer(websocketMessageRegexExpectation);
                        log.debug("Registered  " + websocketMessageRegexExpectation + " consumer.");
                        return websocketMessageRegexExpectation;
                    }
                })
                .or(failedExpectationWithReason(getSessionId() + " is not open."));
    }

    @Override
    public AssertionResult getResult(SampleResult response) {
        log.info("in getResult");
        try {
            ExpectationResult expectationResult = expectation.getResult(Long.valueOf(getTimeout()));

            log.debug("Update sample result end time from " + response.getEndTime() + " to " + expectationResult.getEndTime() + ".");
            response.setEndTime(expectationResult.getEndTime());

            if (expectation instanceof WebsocketMessageRegexExpectation && sessionsManager.hasOpenSession(getSessionId())) {
                sessionsManager
                        .getOpenSession(getSessionId())
                        .unregisterWebsocketIncomingMessageConsumer((WebsocketMessageRegexExpectation) expectation);
                log.debug("Unregistered  " + expectation + " consumer.");
            }
            return assertionResultFrom(getName(), expectationResult);
        } catch (Exception e) {
            return assertionResultFrom(getName(), failure(e.getClass().getSimpleName() + ": " +e.getMessage()));
        }
    }

    public String getSessionId() {
        return getPropertyAsString(SESSION_ID);
    }

    public void setSessionId(String sessionId) {
        setProperty(SESSION_ID, sessionId);
    }

    public String getTimeout() {
        return getPropertyAsString(TIMEOUT);
    }

    public void setTimeout(String timeout) {
        setProperty(TIMEOUT, timeout);
    }

    public String getRegex() {
        return getPropertyAsString(REGEX_ID);
    }

    public void setRegex(String regex) {
        setProperty(REGEX_ID, regex);
    }

    public Expectation getExpectation() {
        return expectation;
    }

    private Supplier<WebsocketMessageRegexExpectation> expectationSupplier() {
        return new Supplier<WebsocketMessageRegexExpectation>() {
            @Override
            public WebsocketMessageRegexExpectation get() {
                return new WebsocketMessageRegexExpectation(getRegex());
            }
        };
    }
}