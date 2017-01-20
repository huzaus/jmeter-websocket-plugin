package com.jmeter.websocket.plugin.listeners;

import org.apache.jmeter.reporters.AbstractListenerElement;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.testelement.TestStateListener;

public class WebsocketDataWriter extends AbstractListenerElement implements SampleListener, TestStateListener {

    private final String FILE = "websocket.data.wirter.file";

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

    }

    @Override
    public void testStarted(String host) {

    }

    @Override
    public void testEnded() {

    }

    @Override
    public void testEnded(String host) {

    }

    public void setFile(String filename) {
        setProperty(FILE, filename);
    }

    public String getFile() {
        return getPropertyAsString(FILE, "");
    }
}
