package com.jmeter.websocket.plugin.endpoint.jetty.converters;

import com.google.common.base.Function;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.CookieStore;

import static java.util.concurrent.Executors.newCachedThreadPool;

public class CookieManagerToWebSocketClientConverter implements Function<CookieManager, WebSocketClient> {

    private final Function<CookieManager, CookieStore> cookieManagerCookieStoreConverter = new CookieManagerToCookieStoreConverter();

    @Override
    public WebSocketClient apply(CookieManager cookieManager) {
        WebSocketClient webSocketClient = new WebSocketClient(sslContextFactory(), newCachedThreadPool());
        webSocketClient.setCookieStore(cookieManagerCookieStoreConverter.apply(cookieManager));
        return webSocketClient;
    }

    private SslContextFactory sslContextFactory() {
        return new SslContextFactory(true);
    }
}
