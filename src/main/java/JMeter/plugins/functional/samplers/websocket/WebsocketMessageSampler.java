package JMeter.plugins.functional.samplers.websocket;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketMessageSampler extends AbstractSampler {

    private static final String SERVER_NAME_OR_IP = "serverNameOrIp";
    private static final String PORT_NUMBER = "portNumber";
    private static final String PROTOCOL = "protocol";
    private static final String CONNECT_TIME_OUT = "connectTimeOut";
    private static final String RESPONSE_TIME_OUT = "responseTimeOut";
    private static final String PATH = "path";
    private static final String MESSAGE = "message";
    private static final String COOKIE_MANAGER = "cookie_manager";
    private static final String HEADER_MANAGER = "header_manager";

    public WebsocketMessageSampler() {
        setName("Websocket Message Sampler");
    }

    @Override
    public SampleResult sample(Entry entry) {
        return new SampleResult();
    }

    @Override
    public void addTestElement(TestElement el) {
        if (el instanceof CookieManager) {
            setCookieManager((CookieManager) el);
        } else if (el instanceof HeaderManager) {
            setHeaderManager((HeaderManager) el);
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

    public String getMessage() {
        return getPropertyAsString(MESSAGE);
    }

    public void setMessage(String message) {
        setProperty(MESSAGE, message, "");
    }

    public URI uri() throws URISyntaxException {
        return new URI(getProtocol(), null, getServerNameOrIp(), Integer.valueOf(getPortNumber()), getPath(), null, null);
    }

    public SslContextFactory sslContextFactory() {
        return new SslContextFactory(true);
    }
}
