package com.jmeter.websocket.plugin.samplers;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;

public class WebsocketMessageSampler extends AbstractSampler {

    private static final String MESSAGE = "websocket.message";

    @Override
    public SampleResult sample(Entry entry) {
        return null;
    }

    public String getMessage() {
        return getPropertyAsString(MESSAGE);
    }

    public void setMessage(String message) {
        setProperty(MESSAGE, message, "");
    }

}