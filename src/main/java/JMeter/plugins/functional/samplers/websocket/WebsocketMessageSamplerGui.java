package JMeter.plugins.functional.samplers.websocket;

import JMeter.plugins.functional.samplers.websocket.configurers.WebsocketMessageSamplerConfigurer;
import JMeter.plugins.functional.samplers.websocket.modifiers.WebsocketMessageSamplerModifier;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextArea;
import org.apache.jorphan.gui.JLabeledTextField;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import static javax.swing.BorderFactory.createEtchedBorder;
import static javax.swing.BorderFactory.createTitledBorder;

public class WebsocketMessageSamplerGui extends AbstractSamplerGui {

    private final JLabeledTextField serverNameOrIp;
    private final JLabeledTextField portNumber;
    private final JLabeledTextField protocol;
    private final JLabeledTextField connectTimeOut;
    private final JLabeledTextField responseTimeOut;
    private final JLabeledTextField path;
    private final JLabeledTextArea message;
    private final WebsocketMessageSamplerModifier modifier;
    private final WebsocketMessageSamplerConfigurer configurer;

    public WebsocketMessageSamplerGui() {
        serverNameOrIp = new JLabeledTextField("Server Name or IP:", 10);
        portNumber = new JLabeledTextField("Port Number:", 5);
        protocol = new JLabeledTextField("Protocol:", 5);
        connectTimeOut = new JLabeledTextField("Connect:", 5);
        responseTimeOut = new JLabeledTextField("Response:", 5);
        path = new JLabeledTextField("Path:", 15);
        message = new JLabeledTextArea("Message:");
        modifier = new WebsocketMessageSamplerModifier();
        configurer = new WebsocketMessageSamplerConfigurer();

        init();
    }

    @Override
    public String getLabelResource() {
        return "websocket.message.sampler.title";
    }

    @Override
    public TestElement createTestElement() {
        WebsocketMessageSampler sampler = new WebsocketMessageSampler();
        modifyTestElement(sampler);
        return sampler;
    }

    @Override
    public void modifyTestElement(TestElement sampler) {
        sampler.clear();
        modifier.modify(this, (WebsocketMessageSampler) sampler);
        super.configureTestElement(sampler);
    }

    @Override
    public String getStaticLabel() {
        return "Websocket Message Sampler";
    }

    @Override
    public void configure(TestElement sampler) {
        super.configure(sampler);
        configurer.configure((WebsocketMessageSampler) sampler, this);
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), NORTH);
        add(makeWebsocketPanel(), CENTER);
    }

    private JPanel makeWebsocketPanel() {
        JPanel websocketPanel = new JPanel(new BorderLayout());
        websocketPanel.add(makeWebsocketConnectionPanel(), NORTH);
        websocketPanel.add(makeMessageBodyPanel(), CENTER);
        return websocketPanel;
    }

    private JPanel makeWebsocketConnectionPanel() {
        HorizontalPanel websocketServerPanel = new HorizontalPanel();
        websocketServerPanel.setBorder(createTitledBorder(createEtchedBorder(), "Websocket Server"));
        websocketServerPanel.add(makeServerNameOrIpPanel());
        websocketServerPanel.add(makePortPanel());
        websocketServerPanel.add(makeProtocolPanel());

        HorizontalPanel timeOut = new HorizontalPanel();
        timeOut.setBorder(createTitledBorder(createEtchedBorder(), "Timeouts (milliseconds)"));
        timeOut.add(makeConnectTimeOutPanel(), CENTER);
        timeOut.add(makeResponseTimeOutPanel(), EAST);

        JPanel webServerTimeoutPanel = new HorizontalPanel();
        webServerTimeoutPanel.add(websocketServerPanel, CENTER);
        webServerTimeoutPanel.add(timeOut, EAST);

        VerticalPanel bigPanel = new VerticalPanel();
        bigPanel.add(webServerTimeoutPanel, CENTER);
        bigPanel.add(makePathPanel(), SOUTH);
        return bigPanel;
    }

    private JPanel makeServerNameOrIpPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(serverNameOrIp, CENTER);
        return panel;
    }

    private JPanel makePortPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(portNumber, CENTER);
        return panel;
    }

    private JPanel makeProtocolPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(protocol, CENTER);
        return panel;
    }

    private JPanel makeConnectTimeOutPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(connectTimeOut, CENTER);
        return panel;
    }

    private JPanel makeResponseTimeOutPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(responseTimeOut, CENTER);
        return panel;
    }

    private Component makePathPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(path, CENTER);
        return panel;
    }

    private Component makeMessageBodyPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(message, CENTER);
        return panel;
    }

    public JLabeledTextField getServerNameOrIp() {
        return serverNameOrIp;
    }

    public JLabeledTextField getPortNumber() {
        return portNumber;
    }

    public JLabeledTextField getProtocol() {
        return protocol;
    }

    public JLabeledTextField getConnectTimeOut() {
        return connectTimeOut;
    }

    public JLabeledTextField getResponseTimeOut() {
        return responseTimeOut;
    }

    public JLabeledTextField getPath() {
        return path;
    }

    public JLabeledTextArea getMessage() {
        return message;
    }
}
