package com.jmeter.websocket.plugin.configurations;

import com.google.common.base.Supplier;
import com.jmeter.websocket.plugin.endpoint.WebsocketClient;
import com.jmeter.websocket.plugin.endpoint.comsumers.CsvFileWriter;
import com.jmeter.websocket.plugin.endpoint.jetty.JettyWebsocketEndpoint;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.nio.file.Paths;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Suppliers.memoize;
import static com.jmeter.websocket.plugin.endpoint.comsumers.CsvFileWriter.csvFileWriterSupplier;

public class WebsocketSessionsManager extends ConfigTestElement implements TestStateListener {

    private static final String FILE = "websocket.data.output.file";
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static Supplier<CsvFileWriter> csvFileWriterSupplier;
    private static Supplier<WebsocketClient> websocketClientSupplier = websocketClientSupplier();

    public String getFile() {
        return getPropertyAsString(FILE, "");
    }

    public void setFile(String filename) {
        setProperty(FILE, filename);
    }

    public WebsocketClient getWebsocketClient() {
        return websocketClientSupplier.get();
    }

    private static Supplier<WebsocketClient> websocketClientSupplier() {
        return memoize(new Supplier<WebsocketClient>() {
            @Override
            public WebsocketClient get() {
                return new JettyWebsocketEndpoint();
            }
        });
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("websocketClient", getWebsocketClient())
                .add("file", getFile())
                .toString();
    }

    @Override
    public void testStarted() {
        csvFileWriterSupplier = csvFileWriterSupplier(Paths.get(getFile()));
        getWebsocketClient().registerMessageConsumer(csvFileWriterSupplier.get());
        getWebsocketClient().start();
        log.info("Test started: " + this);
    }

    @Override
    public void testStarted(String host) {
        log.info("Test started: " + this + ". Host: " + host + ".");
    }

    @Override
    public void testEnded() {
        getWebsocketClient().stop();
        csvFileWriterSupplier.get().stop();
        getWebsocketClient().unregisterMessageConsumer(csvFileWriterSupplier.get());
        log.info("Test ended: " + this);
    }

    @Override
    public void testEnded(String host) {
        log.info("Test ended: " + this + ". Host: " + host + ".");
    }
}
