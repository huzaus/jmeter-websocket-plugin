package com.jmeter.websocket.plugin.samplers;

import com.jmeter.websocket.plugin.configurers.WebsocketSessionSamplerConfigurer;
import com.jmeter.websocket.plugin.modifiers.WebsocketSessionSamplerModifier;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;

import java.awt.BorderLayout;
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
        websocketServerPanel.add(makeProtocolPanel());
        websocketServerPanel.add(makeServerNameOrIpPanel());
        websocketServerPanel.add(makePortPanel());
        websocketServerPanel.add(makePathPanel());
        websocketServerPanel.add(makeConnectTimeOutPanel());
        return websocketServerPanel;
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
