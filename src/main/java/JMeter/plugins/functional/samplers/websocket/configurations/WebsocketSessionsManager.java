package JMeter.plugins.functional.samplers.websocket.configurations;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.UpgradeRequest;

import java.net.URI;

import static com.google.common.base.Optional.fromNullable;

public class WebsocketSessionsManager extends ConfigTestElement {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private transient Session session;

    public static final String WEBSOCKET_MANAGER = "websocket_manager";

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        log.error("setSession{}");
        this.session = session;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("session",
                        fromNullable(session)
                                .transform(new Function<Session, UpgradeRequest>() {
                                    @Override
                                    public UpgradeRequest apply(Session session) {
                                        return session.getUpgradeRequest();
                                    }
                                })
                                .transform(new Function<UpgradeRequest, URI>() {
                                    @Override
                                    public URI apply(UpgradeRequest request) {
                                        return request.getRequestURI();
                                    }
                                })
                                .transform(new Function<URI, String>() {
                                    @Override
                                    public String apply(URI uri) {
                                        return uri.toString();
                                    }
                                })
                                .orNull()
                )
                .toString();
    }
}
