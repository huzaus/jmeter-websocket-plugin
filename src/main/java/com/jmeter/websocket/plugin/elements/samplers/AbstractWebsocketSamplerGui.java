package com.jmeter.websocket.plugin.elements.samplers;

import com.jmeter.websocket.plugin.elements.helpers.Configurer;
import com.jmeter.websocket.plugin.elements.helpers.Modifier;
import com.jmeter.websocket.plugin.elements.samplers.configurers.AbstractWebsocketSamplerConfigurer;
import com.jmeter.websocket.plugin.elements.samplers.modifiers.AbstractWebsocketSamplerModifier;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

import static java.awt.BorderLayout.CENTER;

public abstract class AbstractWebsocketSamplerGui extends AbstractSamplerGui {

    protected final JLabeledTextField sessionId;

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
        sessionId = new JLabeledTextField("Session id:", 10);

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

    public JLabeledTextField getSessionId() {
        return sessionId;
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

    protected JPanel makeSessionIdPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(sessionId, CENTER);
        return panel;
    }
}
