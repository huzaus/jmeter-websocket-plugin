package JMeter.plugins.functional.samplers.websocket;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;

public class WebsocketMessageSampler extends AbstractSampler {

    private static final String SERVER_NAME_OR_IP = "serverNameOrIp";
    private static final String PORT_NUMBER = "portNumber";
    private static final String PROTOCOL = "protocol";
    private static final String CONNECT_TIME_OUT = "connectTimeOut";
    private static final String RESPONSE_TIME_OUT = "responseTimeOut";
    private static final String PATH = "path";
    private static final String MESSAGE = "message";

    public WebsocketMessageSampler() {
        super();
        setName("Websocket Message Sampler");
    }

    @Override
    public SampleResult sample(Entry entry) {
        return new SampleResult();
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
}
