package com.jmeter.websocket.plugin.samplers;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.jmeter.websocket.plugin.WebsocketEndpoint;
import com.jmeter.websocket.plugin.WebsocketUpgradeListener;
import com.jmeter.websocket.plugin.configurations.WebsocketSessionsManager;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.eclipse.jetty.util.HttpCookieStore;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Throwables.propagate;
import static java.util.Collections.singletonList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class WebsocketSessionSampler extends AbstractWebsocketSampler {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final String SERVER_NAME_OR_IP = "serverNameOrIp";
    private static final String PORT_NUMBER = "portNumber";
    private static final String PROTOCOL = "protocol";
    private static final String CONNECT_TIME_OUT = "connectTimeOut";
    private static final String RESPONSE_TIME_OUT = "responseTimeOut";
    private static final String PATH = "path";
    private static final String COOKIE_MANAGER = "cookie_manager";
    private static final String HEADER_MANAGER = "header_manager";

    public WebsocketSessionSampler() {
        setName("Websocket Message Sampler");
    }

    @Override
    public SampleResult sample(Entry entry) {
        SampleResult sampleResult = new SampleResult();
        sampleResult.sampleStart();
        sampleResult.setSampleLabel(getName());
        try {
            checkNotNull(getWebsocketSessionsManager(), "WebsocketSessionManager should be added to test plan");

            WebSocketClient webSocketClient = webSocketClient();
            webSocketClient.start();

            WebsocketEndpoint websocketEndpoint = new WebsocketEndpoint();
            getWebsocketSessionsManager().setWebsocketEndpoint(websocketEndpoint);

            Future<Session> promise = webSocketClient.connect(websocketEndpoint, uri(), upgradeRequest(), new WebsocketUpgradeListener(sampleResult));

            getWebsocketSessionsManager().setSession(promise.get(Long.valueOf(getConnectTimeOut()), MILLISECONDS));
        } catch (Exception e) {
            log.error("Error: ", e);
            sampleResult.setResponseMessage(e.getMessage());
            sampleResult.setSuccessful(false);
        } finally {
            sampleResult.sampleEnd();
        }
        return sampleResult;
    }

    @Override
    public void addTestElement(TestElement el) {
        if (el instanceof CookieManager) {
            setCookieManager((CookieManager) el);
        } else if (el instanceof HeaderManager) {
            setHeaderManager((HeaderManager) el);
        } else if (el instanceof WebsocketSessionsManager) {
            setWebsocketSessionsManager((WebsocketSessionsManager) el);
        } else {
            super.addTestElement(el);
        }
    }

    public CookieManager getCookieManager() {
        return Optional.fromNullable(getProperty(COOKIE_MANAGER).getObjectValue())
                .transform(
                        new Function<Object, CookieManager>() {
                            @Override
                            public CookieManager apply(Object property) {
                                return (CookieManager) property;
                            }
                        })
                .orNull();
    }

    public void setCookieManager(CookieManager cookieManager) {
        setProperty(new TestElementProperty(COOKIE_MANAGER, cookieManager));
    }

    public HeaderManager getHeaderManager() {
        return Optional.fromNullable(getProperty(HEADER_MANAGER).getObjectValue())
                .transform(
                        new Function<Object, HeaderManager>() {
                            @Override
                            public HeaderManager apply(Object property) {
                                return (HeaderManager) property;
                            }
                        })
                .orNull();
    }

    public void setHeaderManager(HeaderManager headerManager) {
        setProperty(new TestElementProperty(HEADER_MANAGER, headerManager));
    }

    public String getServerNameOrIp() {
        return getPropertyAsString(SERVER_NAME_OR_IP);
    }

    public void setServerNameOrIp(String serverNameOrIp) {
        setProperty(SERVER_NAME_OR_IP, serverNameOrIp, "");
    }

    public String getPortNumber() {
        return getPropertyAsString(PORT_NUMBER);
    }

    public void setPortNumber(String portNumber) {
        setProperty(PORT_NUMBER, portNumber, "");
    }

    public String getProtocol() {
        return getPropertyAsString(PROTOCOL);
    }

    public void setProtocol(String protocol) {
        setProperty(PROTOCOL, protocol, "");
    }

    public String getConnectTimeOut() {
        return getPropertyAsString(CONNECT_TIME_OUT);
    }

    public void setConnectTimeOut(String connectTimeOut) {
        setProperty(CONNECT_TIME_OUT, connectTimeOut, "");
    }

    public String getResponseTimeOut() {
        return getPropertyAsString(RESPONSE_TIME_OUT);
    }

    public void setResponseTimeOut(String responseTimeOut) {
        setProperty(RESPONSE_TIME_OUT, responseTimeOut, "");
    }

    public String getPath() {
        return getPropertyAsString(PATH);
    }

    public void setPath(String path) {
        setProperty(PATH, path, "");
    }

    public URI uri() throws URISyntaxException {
        return new URI(getProtocol(), null, getServerNameOrIp(), Integer.valueOf(getPortNumber()), getPath(), null, null);
    }

    public SslContextFactory sslContextFactory() {
        return new SslContextFactory(true);
    }

    public Map<String, List<String>> headers() {
        return Optional.fromNullable(getHeaderManager())
                .transform(new Function<HeaderManager, Map<String, List<String>>>() {
                    @Override
                    public Map<String, List<String>> apply(HeaderManager headerManager) {
                        Map<String, List<String>> headers = new HashMap<>();
                        for (int i = 0; i < headerManager.size(); i++) {
                            Header header = headerManager.get(i);
                            headers.put(header.getName(), singletonList(header.getValue()));
                        }
                        return headers;
                    }
                }).or(Collections.<String, List<String>>emptyMap());
    }

    public ClientUpgradeRequest upgradeRequest() {
        ClientUpgradeRequest request = new ClientUpgradeRequest();
        request.setHeaders(headers());
        return request;
    }

    public CookieStore cookies() throws URISyntaxException {
        return Optional.fromNullable(getCookieManager())
                .transform(new Function<CookieManager, CookieStore>() {
                    @Override
                    public CookieStore apply(CookieManager cookieManager) {
                        HttpCookieStore cookieStore = new HttpCookieStore();
                        for (int i = 0; i < cookieManager.getCookieCount(); i++) {
                            try {
                                cookieStore.add(
                                        new URI(null, cookieManager.get(i).getDomain(), cookieManager.get(i).getPath(), null),
                                        new HttpCookie(cookieManager.get(i).getName(), cookieManager.get(i).getValue())
                                );
                            } catch (URISyntaxException e) {
                                propagate(e);
                            }
                        }
                        return cookieStore;
                    }
                })
                .or(new HttpCookieStore());
    }

    public WebSocketClient webSocketClient() throws URISyntaxException {
        WebSocketClient webSocketClient = new WebSocketClient(sslContextFactory(), Executors.newCachedThreadPool());
        webSocketClient.setCookieStore(cookies());
        return webSocketClient;
    }
}
