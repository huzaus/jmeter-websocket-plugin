package com.jmeter.websocket.plugin.configurations;

import org.apache.jmeter.samplers.SampleResult;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;

import java.io.IOException;
import java.net.CookieStore;
import java.net.URI;

public interface WebsocketSession {

    void sendMessage(String message) throws IOException;

    void connect(URI uri, CookieStore cookies, ClientUpgradeRequest upgradeRequest, SampleResult result, long timeOut) throws Exception;
}
