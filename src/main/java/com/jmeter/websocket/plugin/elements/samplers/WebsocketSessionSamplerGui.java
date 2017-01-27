package com.jmeter.websocket.plugin.elements.samplers;

import com.jmeter.websocket.plugin.elements.samplers.configurers.WebsocketSessionSamplerConfigurer;
import com.jmeter.websocket.plugin.elements.samplers.modifiers.WebsocketSessionSamplerModifier;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static javax.swing.BorderFactory.createEtchedBorder;
import static javax.swing.BorderFactory.createTitledBorder;

public class WebsocketSessionSamplerGui extends AbstractWebsocketSamplerGui {

    private final JLabeledTextField connectTimeOut;
    private final WebsocketSessionSamplerModifier modifier;
    private final WebsocketSessionSamplerConfigurer configurer;

    public WebsocketSessionSamplerGui() {
        connectTimeOut = new JLabeledTextField("Timeout:", 5);
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
        super.modifyTestElement(sampler);
        modifier.modify(this, (WebsocketSessionSampler) sampler);
        super.configureTestElement(sampler);
    }

    @Override
    public void configure(TestElement sampler) {
        super.configure(sampler);
        configurer.configure((WebsocketSessionSampler) sampler, this);
    }

    @Override
    public String getStaticLabel() {
        return "Websocket Session Sampler";
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), NORTH);
        add(makeSamplerPanel(), CENTER);
    }

    private JPanel makeSamplerPanel() {
        JPanel websocketPanel = new VerticalPanel();
        websocketPanel.add(makeWebsocketPanel());
        websocketPanel.add(makeSessionIdPanel());
        return websocketPanel;
    }

    private JPanel makeWebsocketPanel() {
        JPanel websocketPanel = new JPanel(new BorderLayout());
        websocketPanel.add(makeWebsocketConnectionPanel(), NORTH);
        return websocketPanel;
    }

    private JPanel makeWebsocketConnectionPanel() {
        HorizontalPanel websocketServerPanel = new HorizontalPanel();
        websocketServerPanel.setBorder(createTitledBorder(createEtchedBorder(), "Websocket Server"));
        websocketServerPanel.add(makeProtocolPanel());
        websocketServerPanel.add(makeServerNameOrIpPanel());
        websocketServerPanel.add(makePortPanel());
        websocketServerPanel.add(makePathPanel());
        websocketServerPanel.add(makeConnectTimeOutPanel());
        return websocketServerPanel;
    }

    protected JPanel makeProtocolPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(protocol, CENTER);
        return panel;
    }

    protected JPanel makeServerNameOrIpPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(serverNameOrIp, CENTER);
        return panel;
    }

    protected JPanel makePortPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(portNumber, CENTER);
        return panel;
    }

    protected Component makePathPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(path, CENTER);
        return panel;
    }

    private JPanel makeConnectTimeOutPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(connectTimeOut, CENTER);
        return panel;
    }

    public JLabeledTextField getConnectTimeOut() {
        return connectTimeOut;
    }

}
