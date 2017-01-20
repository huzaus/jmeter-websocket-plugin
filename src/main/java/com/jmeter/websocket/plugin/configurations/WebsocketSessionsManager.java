package com.jmeter.websocket.plugin.configurations;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import static com.google.common.base.MoreObjects.toStringHelper;

public class WebsocketSessionsManager extends ConfigTestElement implements SampleListener, TestStateListener {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final String FILE = "websocket.data.output.file";

    private transient WebsocketSession websocketSession = new JettyWebsocketSession();

    @Override
    public void sampleOccurred(SampleEvent sampleEvent) {

    }

    @Override
    public void sampleStarted(SampleEvent sampleEvent) {
    }

    @Override
    public void sampleStopped(SampleEvent sampleEvent) {

    }

    @Override
    public void testStarted() {
        log.info("Test started");
    }

    @Override
    public void testStarted(String host) {
        log.info("Test started on " + host + " host.");
    }

    @Override
    public void testEnded() {
        log.info("Test ended");
    }

    @Override
    public void testEnded(String host) {
        log.info("Test ended on " + host + " host.");

    }

    public String getFile() {
        return getPropertyAsString(FILE, "");
    }

    public void setFile(String filename) {
        setProperty(FILE, filename);
    }

    public WebsocketSession getWebsocketSession() {
        return websocketSession;
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("websocketSession", websocketSession)
                .add("file", getFile())
                .toString();
    }
}
