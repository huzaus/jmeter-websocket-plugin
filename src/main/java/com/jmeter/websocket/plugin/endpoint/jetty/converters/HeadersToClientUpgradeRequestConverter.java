package com.jmeter.websocket.plugin.endpoint.jetty.converters;

import com.google.common.base.Function;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;

import java.util.List;
import java.util.Map;

public class HeadersToClientUpgradeRequestConverter implements Function<Map<String, List<String>>, ClientUpgradeRequest> {
    @Override
    public ClientUpgradeRequest apply(Map<String, List<String>> headers) {
        ClientUpgradeRequest request = new ClientUpgradeRequest();
        if (headers != null) {
            request.setHeaders(headers);
        }
        return request;
    }
}
