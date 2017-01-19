package com.jmeter.websocket.plugin.samplers;

import com.jmeter.websocket.plugin.configurations.WebsocketSessionsManager;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;

public class WebsocketMessageSampler extends AbstractWebsocketSampler {

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

    @Override
    public void addTestElement(TestElement el) {
        if (el instanceof WebsocketSessionsManager) {
            setWebsocketSessionsManager((WebsocketSessionsManager) el);
        } else {
            super.addTestElement(el);
        }
    }
}