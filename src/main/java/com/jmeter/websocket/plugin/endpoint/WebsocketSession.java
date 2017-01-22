package com.jmeter.websocket.plugin.endpoint;

import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.samplers.SampleResult;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

public interface WebsocketSession {

    void connect(URI uri, CookieManager cookieManager, Map<String, List<String>> headers, SampleResult result, long timeOut) throws Exception;

    void sendMessage(String message) throws IOException;

    void onReceiveMessage(String message) throws IOException;

}
