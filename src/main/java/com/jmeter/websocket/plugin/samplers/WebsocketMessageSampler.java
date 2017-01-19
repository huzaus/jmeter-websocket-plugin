package com.jmeter.websocket.plugin.samplers;

import com.jmeter.websocket.plugin.configurations.WebsocketSessionsManager;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.eclipse.jetty.websocket.api.Session;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class WebsocketMessageSampler extends AbstractWebsocketSampler {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final String MESSAGE = "websocket.message";

    @Override
    public SampleResult sample(Entry entry) {
        SampleResult sampleResult = new SampleResult();
        sampleResult.sampleStart();
        sampleResult.setSampleLabel(getName());
        try {
            WebsocketSessionsManager websocketSessionsManager = getWebsocketSessionsManager();
            checkNotNull(websocketSessionsManager, "WebsocketSessionManager should be added to test plan");
            Session session = websocketSessionsManager.getSession();
            checkNotNull(session, "Session should be not null");
            checkArgument(session.isOpen(), "Session should be open");
            Future promise = session.getRemote().sendStringByFuture(getMessage());
            promise.get(2000, TimeUnit.MILLISECONDS);
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

    @Override
    public void addTestElement(TestElement el) {
        if (el instanceof WebsocketSessionsManager) {
            setWebsocketSessionsManager((WebsocketSessionsManager) el);
        } else {
            super.addTestElement(el);
        }
    }
}