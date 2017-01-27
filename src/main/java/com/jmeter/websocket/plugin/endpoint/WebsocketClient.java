package com.jmeter.websocket.plugin.endpoint;

import com.jmeter.websocket.plugin.endpoint.comsumers.WebsocketMessageConsumer;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.samplers.SampleResult;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

public interface WebsocketClient {

    void connect(URI uri, String sessionId, CookieManager cookieManager, Map<String, List<String>> headers, SampleResult result, long timeOut) throws Exception;

    void sendMessage(String sessionId, String message) throws IOException;

    void registerMessageConsumer(WebsocketMessageConsumer consumer);

    void unregisterMessageConsumer(WebsocketMessageConsumer consumer);

    void start();

    void stop();
}
