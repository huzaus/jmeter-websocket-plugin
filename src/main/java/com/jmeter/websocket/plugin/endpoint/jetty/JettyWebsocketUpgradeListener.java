package com.jmeter.websocket.plugin.endpoint.jetty;

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

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

public class JettyWebsocketUpgradeListener implements UpgradeListener {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private final SampleResult sampleResult;

    public JettyWebsocketUpgradeListener(SampleResult sampleResult) {
        checkNotNull(sampleResult);
        this.sampleResult = sampleResult;
    }

    @Override
    public void onHandshakeRequest(UpgradeRequest request) {
        checkNotNull(request);
        checkNotNull(request.getHeaders());
        URI requestURI = request.getRequestURI();
        checkNotNull(requestURI);
        log.info("onHandshakeRequest() request: "
                + toStringHelper(request)
                .add("uri", request.getRequestURI())
                .add("headers", request.getHeaders())
                .add("cookies", request.getCookies())
                .add("extensions", request.getExtensions())
                .toString()
        );
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
            log.info("Failed to set url: ", e);
        }
    }

    @Override
    public void onHandshakeResponse(UpgradeResponse response) {
        checkNotNull(response);
        checkNotNull(response.getHeaders());
        log.info("onHandshakeResponse() response: "
                + toStringHelper(response)
                .add("success", response.isSuccess())
                .add("statusCode", response.getStatusCode())
                .add("statusReason", response.getStatusReason())
                .add("headers", response.getHeaders())
                .toString()
        );
        sampleResult.setSuccessful(response.isSuccess());
        sampleResult.setResponseMessage(response.getStatusReason());
        sampleResult.setResponseCode(String.valueOf(response.getStatusCode()));
        sampleResult.setResponseHeaders(
                Joiner.on("\n")
                        .withKeyValueSeparator("=")
                        .join(response.getHeaders())
        );
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("sampleResult", sampleResult)
                .toString();
    }
}
