package com.jmeter.websocket.plugin.configurations;

import com.google.common.base.Function;
import com.jmeter.websocket.plugin.endpoint.WebsocketEndpoint;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.eclipse.jetty.websocket.api.Session;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Optional.fromNullable;

public class WebsocketSessionsManager extends ConfigTestElement implements SampleListener, TestStateListener {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final String FILE = "websocket.data.output.file";

    public static final String WEBSOCKET_MANAGER = "websocket_manager";

    private transient Session session;
    private transient WebsocketEndpoint websocketEndpoint;


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
    public void testStarted(String s) {

    }

    @Override
    public void testEnded() {

    }

    @Override
    public void testEnded(String s) {

    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        log.debug("setSession() session: " + session);
        this.session = session;
    }

    public WebsocketEndpoint getWebsocketEndpoint() {
        return websocketEndpoint;
    }

    public void setWebsocketEndpoint(WebsocketEndpoint websocketEndpoint) {
        log.debug("setWebsocketEndpoint() websocketEndpoint: " + websocketEndpoint);
        this.websocketEndpoint = websocketEndpoint;
    }

    public String getFile() {
        return getPropertyAsString(FILE, "");
    }

    public void setFile(String filename) {
        setProperty(FILE, filename);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("session", fromNullable(session)
                        .transform(new Function<Session, String>() {
                            @Override
                            public String apply(Session input) {
                                return "is set";
                            }
                        })
                        .or("is not set"))
                .add("websocketEndpoint", websocketEndpoint)
                .add("file", getFile())
                .toString();
    }
}
