package com.jmeter.websocket.plugin.samplers;

import com.jmeter.websocket.plugin.configurers.AbstractWebsocketSamplerConfigurer;
import com.jmeter.websocket.plugin.configurers.Configurer;
import com.jmeter.websocket.plugin.modifiers.AbstractWebsocketSamplerModifier;
import com.jmeter.websocket.plugin.modifiers.Modifier;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

import static java.awt.BorderLayout.CENTER;

public abstract class AbstractWebsocketSamplerGui extends AbstractSamplerGui {

    protected final JLabeledTextField serverNameOrIp;
    protected final JLabeledTextField portNumber;
    protected final JLabeledTextField protocol;
    protected final JLabeledTextField path;
    private final Modifier<AbstractWebsocketSamplerGui, AbstractWebsocketSampler> modifier = new AbstractWebsocketSamplerModifier();
    private final Configurer<AbstractWebsocketSampler, AbstractWebsocketSamplerGui> configurer = new AbstractWebsocketSamplerConfigurer();

    protected AbstractWebsocketSamplerGui() {
        serverNameOrIp = new JLabeledTextField("Server Name or IP:", 10);
        portNumber = new JLabeledTextField("Port Number:", 5);
        protocol = new JLabeledTextField("Protocol:", 5);
        path = new JLabeledTextField("Path:", 30);
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

    public JLabeledTextField getPath() {
        return path;
    }

    @Override
    public void modifyTestElement(TestElement sampler) {
        sampler.clear();
        modifier.modify(this, (AbstractWebsocketSampler) sampler);
        super.configureTestElement(sampler);
    }

    @Override
    public void configure(TestElement sampler) {
        super.configure(sampler);
        configurer.configure((AbstractWebsocketSampler) sampler, this);
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
}
