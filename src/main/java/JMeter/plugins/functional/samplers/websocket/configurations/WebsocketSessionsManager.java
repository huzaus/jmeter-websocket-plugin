package JMeter.plugins.functional.samplers.websocket.configurations;

import com.google.common.base.Function;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.eclipse.jetty.websocket.api.Session;

import javax.annotation.Nullable;

import static com.google.common.base.MoreObjects.toStringHelper;
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
        return toStringHelper(this)
                .add("session", fromNullable(session)
                        .transform(new Function<Session, String>() {
                            @Nullable
                            @Override
                            public String apply(@Nullable Session input) {
                                return "is set";
                            }
                        })
                        .or("is not set")
                )
                .toString();
    }
}
