package com.jmeter.websocket.plugin.endpoint;

import com.google.common.base.Joiner;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.client.io.UpgradeListener;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static com.google.common.base.Preconditions.checkNotNull;

public class WebsocketUpgradeListener implements UpgradeListener {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private final SampleResult sampleResult;

    public WebsocketUpgradeListener(SampleResult sampleResult) {
        checkNotNull(sampleResult);
        this.sampleResult = sampleResult;
    }

    @Override
    public void onHandshakeRequest(UpgradeRequest request) {
        log.info("onHandshakeRequest() request: " + request);
        checkNotNull(request);
        checkNotNull(request.getHeaders());
        URI requestURI = request.getRequestURI();
        checkNotNull(requestURI);
        sampleResult.setRequestHeaders(
                Joiner.on("\n")
                        .withKeyValueSeparator("=")
                        .join(request.getHeaders())
        );
        try {
            sampleResult.setURL(
                    new URL(
                            "wss".equals(requestURI.getScheme()) ? "https" : "http",
                            requestURI.getHost(),
                            requestURI.getPort(),
                            requestURI.getPath()
                    )
            );
        } catch (MalformedURLException e) {
            log.error("Failed to set url: ", e);
        }
    }

    @Override
    public void onHandshakeResponse(UpgradeResponse response) {
        log.info("onHandshakeResponse() response: " + response);
        checkNotNull(response);
        checkNotNull(response.getHeaders());
        sampleResult.setSuccessful(response.isSuccess());
        sampleResult.setResponseMessage(response.getStatusReason());
        sampleResult.setResponseCode(String.valueOf(response.getStatusCode()));
        sampleResult.setResponseHeaders(
                Joiner.on("\n")
                        .withKeyValueSeparator("=")
                        .join(response.getHeaders())
        );
    }
}
