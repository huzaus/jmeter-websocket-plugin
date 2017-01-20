package com.jmeter.websocket.plugin.configurations;

import com.google.common.base.Supplier;
import com.jmeter.websocket.plugin.endpoint.JettyWebsocketEndpoint;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Suppliers.memoize;
import static com.google.common.base.Throwables.propagate;

public class WebsocketSessionsManager extends ConfigTestElement implements TestStateListener {

    private static final String FILE = "websocket.data.output.file";
    private static final Logger log = LoggingManager.getLoggerForClass();

    public String getFile() {
        return getPropertyAsString(FILE, "");
    }

    public void setFile(String filename) {
        setProperty(FILE, filename);
    }

    public WebsocketSession getWebsocketSession() {
        return memoize(websocketSessionSupplier()).get();
    }

    private Supplier<WebsocketSession> websocketSessionSupplier() {
        return new Supplier<WebsocketSession>() {
            @Override
            public WebsocketSession get() {
                return new JettyWebsocketEndpoint(Paths.get(getFile()));
            }
        };
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("websocketSession", getWebsocketSession())
                .add("file", getFile())
                .toString();
    }

    @Override
    public void testStarted() {
        log.info("Test started.");
        try {
            Files.createFile(Paths.get(getFile()));
        } catch (IOException e) {
            log.error("Failed to create " + getFile() + " file.", e);
            propagate(e);
        }
    }

    @Override
    public void testStarted(String host) {
        log.info("Test started. Host: " + host + ".");
    }

    @Override
    public void testEnded() {
        log.info("Test ended");
    }

    @Override
    public void testEnded(String host) {
        log.info("Test ended. Host: " + host + ".");

    }
}
