package com.jmeter.websocket.plugin.elements.samplers;

import com.jmeter.websocket.plugin.endpoint.WebsocketClient;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

public class WebsocketMessageSampler extends AbstractWebsocketSampler {

    public static final String MESSAGE = "websocket.message";
    private static final Logger log = LoggingManager.getLoggerForClass();

    @Override
    public SampleResult sample(Entry entry) {
        SampleResult sampleResult = new SampleResult();
        sampleResult.sampleStart();
        sampleResult.setSampleLabel(getName());
        try {
            WebsocketClient websocketClient = getWebsocketClient();
            checkNotNull(websocketClient, "WebsocketSessionManager has to be added to test plan");
            String message = getMessage();
            sampleResult.setResponseMessage(message);
            websocketClient.sendMessage(getSessionId(), message);
            sampleResult.setSuccessful(true);
        } catch (Exception e) {
            log.error("Error: ", e);
            sampleResult.setResponseMessage(e.getMessage());
            sampleResult.setSuccessful(false);
        } finally {
            sampleResult.sampleEnd();
        }
        return sampleResult;
    }

    public String getMessage() {
        return getPropertyAsString(MESSAGE);
    }

    public void setMessage(String message) {
        setProperty(MESSAGE, message, "");
    }

}