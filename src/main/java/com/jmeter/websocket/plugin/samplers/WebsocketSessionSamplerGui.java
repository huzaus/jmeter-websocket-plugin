package com.jmeter.websocket.plugin.samplers;

import com.jmeter.websocket.plugin.configurers.WebsocketSessionSamplerConfigurer;
import com.jmeter.websocket.plugin.modifiers.WebsocketSessionSamplerModifier;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
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

public class WebsocketSessionSamplerGui extends AbstractSamplerGui {

    private final JLabeledTextField serverNameOrIp;
    private final JLabeledTextField portNumber;
    private final JLabeledTextField protocol;
    private final JLabeledTextField connectTimeOut;
    private final JLabeledTextField responseTimeOut;
    private final JLabeledTextField path;
    private final WebsocketSessionSamplerModifier modifier;
    private final WebsocketSessionSamplerConfigurer configurer;

    public WebsocketSessionSamplerGui() {
        serverNameOrIp = new JLabeledTextField("Server Name or IP:", 10);
        portNumber = new JLabeledTextField("Port Number:", 5);
        protocol = new JLabeledTextField("Protocol:", 5);
        connectTimeOut = new JLabeledTextField("Connect:", 5);
        responseTimeOut = new JLabeledTextField("Response:", 5);
        path = new JLabeledTextField("Path:", 15);
        modifier = new WebsocketSessionSamplerModifier();
        configurer = new WebsocketSessionSamplerConfigurer();

        init();
    }

    @Override
    public String getLabelResource() {
        return "websocket.session.sampler.title";
    }

    @Override
    public TestElement createTestElement() {
        WebsocketSessionSampler sampler = new WebsocketSessionSampler();
        modifyTestElement(sampler);
        return sampler;
    }

    @Override
    public void modifyTestElement(TestElement sampler) {
        sampler.clear();
        modifier.modify(this, (WebsocketSessionSampler) sampler);
        super.configureTestElement(sampler);
    }

    @Override
    public String getStaticLabel() {
        return "Websocket Session Sampler";
    }

    @Override
    public void configure(TestElement sampler) {
        super.configure(sampler);
        configurer.configure((WebsocketSessionSampler) sampler, this);
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

}
