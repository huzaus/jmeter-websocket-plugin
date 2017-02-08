package com.jmeter.websocket.plugin.elements.helpers;

public interface Expectation {
    ExpectationResult getResult(long timeout);
}
