package com.jmeter.websocket.plugin.endpoint.comsumers;

import com.google.common.base.MoreObjects;
import com.google.common.base.Supplier;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Suppliers.memoize;

public class WebsocketMessageRegexExpectation implements WebsocketMessageConsumer {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private final String sessionId;
    private final String regex;

    private ExpectationResult result = new ExpectationResult();
    private Supplier<Pattern> patternSupplier = patternSupplier();

    public WebsocketMessageRegexExpectation(String sessionId, String regex) {
        checkNotNull(sessionId);
        checkNotNull(regex);
        this.sessionId = sessionId;
        this.regex = regex;
    }

    @Override
    public void onMessageReceive(String sessionId, String message) {
        synchronized (this) {
            if (!result.isFinished()) {
                try {
                    if (getPattern().matcher(message).matches()) {
                        result.success();
                    }
                } catch (PatternSyntaxException e) {
                    log.error("Failed to match: ", e);
                    result.failure(e.getMessage());
                }
            }
        }
    }

    @Override
    public void onMessageSend(String sessionId, String message) {
        //do nothing TODO: split WebsocketMessageConsumer interface
    }

    public Pattern getPattern() {
        return patternSupplier.get();
    }

    private Supplier<Pattern> patternSupplier() {
        return memoize(new Supplier<Pattern>() {
            @Override
            public Pattern get() {
                return Pattern.compile(regex);
            }
        });
    }

    public String getSessionId() {
        return sessionId;
    }

    public ExpectationResult getResult() {
        synchronized (this) {
            return new ExpectationResult(result.getEndTime(), result.isSuccess(), result.isFinished(), result.getReason());
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sessionId", sessionId)
                .add("regex", regex)
                .add("result", result)
                .toString();
    }
}
