package com.jmeter.websocket.plugin.endpoint.jetty;

import com.google.common.base.Function;
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
import java.util.List;
import java.util.Map;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.System.lineSeparator;

public class JettyWebsocketUpgradeListener implements UpgradeListener {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private final SampleResult sampleResult;

    @Override
    public void onHandshakeRequest(UpgradeRequest request) {
        checkNotNull(request);
        log.debug("onHandshakeRequest() request: " + toStringHelper(request)
                .add("uri", request.getRequestURI())
                .add("headers", request.getHeaders())
                .add("cookies", request.getCookies())
                .add("extensions", request.getExtensions())
                .toString()
        );
        sampleResult.setRequestHeaders(
                fromNullable(request.getHeaders())
                        .transform(new Function<Map<String, List<String>>, String>() {
                            @Override
                            public String apply(Map<String, List<String>> headers) {
                                return Joiner.on(lineSeparator())
                                        .withKeyValueSeparator("=")
                                        .join(headers);
                            }
                        })
                        .or("")
        );
        sampleResult.setURL(
                fromNullable(request.getRequestURI())
                        .transform(new Function<URI, URL>() {
                            @Override
                            public URL apply(URI uri) {
                                try {
                                    return new URL(
                                            "wss".equals(uri.getScheme()) ? "https" : "http",
                                            uri.getHost(),
                                            uri.getPort(),
                                            uri.getPath()
                                    );
                                } catch (MalformedURLException e) {
                                    log.info("Failed to set url: ", e);
                                }
                                return null;
                            }
                        })
                        .orNull()
        );
    }

    public JettyWebsocketUpgradeListener(SampleResult sampleResult) {
        checkNotNull(sampleResult);
        this.sampleResult = sampleResult;
    }

    @Override
    public void onHandshakeResponse(UpgradeResponse response) {
        checkNotNull(response);
        log.debug("onHandshakeResponse() response: " + toStringHelper(response)
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
                fromNullable(response.getHeaders())
                        .transform(new Function<Map<String, List<String>>, String>() {
                            @Override
                            public String apply(Map<String, List<String>> headers) {
                                return Joiner.on(lineSeparator())
                                        .withKeyValueSeparator("=")
                                        .join(headers);
                            }
                        })
                        .or("")
        );
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("sampleResult", sampleResult)
                .toString();
    }
}
