package JMeter.plugins.functional.samplers.websocket;

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
import static java.awt.BorderLayout.WEST;
import static javax.swing.BorderFactory.createEtchedBorder;
import static javax.swing.BorderFactory.createTitledBorder;

public class WebsocketMessageSamplerGui extends AbstractSamplerGui {

    private JLabeledTextField serverNameOrIp;
    private JLabeledTextField portNumber;
    private JLabeledTextField protocol;
    private JLabeledTextField connectTimeOut;
    private JLabeledTextField responseTimeOut;
    private JLabeledTextField path;
    private JLabeledTextArea message;

    public WebsocketMessageSamplerGui() {
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
        super.configureTestElement(sampler);
    }

    @Override
    public String getStaticLabel() {
        return "Websocket Message Sampler";
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
        websocketServerPanel.add(makeServerNameOrIpPanel(), WEST);
        websocketServerPanel.add(makePortPanel(), CENTER);
        websocketServerPanel.add(makeProtocolPanel(), EAST);

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
        serverNameOrIp = new JLabeledTextField("Server Name or IP:", 10);
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(serverNameOrIp, CENTER);
        return panel;
    }

    private JPanel makePortPanel() {
        portNumber = new JLabeledTextField("Port Number:", 5);
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(portNumber, CENTER);
        return panel;
    }

    private JPanel makeProtocolPanel() {
        protocol = new JLabeledTextField("Protocol:", 5);
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(protocol, CENTER);
        return panel;
    }

    private JPanel makeConnectTimeOutPanel() {
        connectTimeOut = new JLabeledTextField("Connect:", 5);
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(connectTimeOut, CENTER);
        return panel;
    }

    private JPanel makeResponseTimeOutPanel() {
        responseTimeOut = new JLabeledTextField("Response:", 5);
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(responseTimeOut, CENTER);
        return panel;
    }


    private Component makePathPanel() {
        path = new JLabeledTextField("Path:", 15);
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(path, CENTER);
        return panel;
    }

    private Component makeMessageBodyPanel() {
        message = new JLabeledTextArea("Message:");
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(message, CENTER);
        return panel;
    }

}
