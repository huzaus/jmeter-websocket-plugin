package JMeter.plugins.functional.samplers.websocket;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.client.io.UpgradeListener;

public class WebsocketUpgradeListener implements UpgradeListener {

    private static final Logger log = LoggingManager.getLoggerForClass();

    @Override
    public void onHandshakeRequest(UpgradeRequest request) {
        log.info("onHandshakeRequest() request: " + request);
    }

    @Override
    public void onHandshakeResponse(UpgradeResponse response) {
        log.info("onHandshakeResponse() response: " + response);
    }
}
