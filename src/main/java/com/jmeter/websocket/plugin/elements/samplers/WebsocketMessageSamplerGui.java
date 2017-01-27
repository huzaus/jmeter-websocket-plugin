package com.jmeter.websocket.plugin.elements.samplers;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextArea;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;

public class WebsocketMessageSamplerGui extends AbstractWebsocketSamplerGui {

    private final JLabeledTextArea message;

    public WebsocketMessageSamplerGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), NORTH);
        message = new JLabeledTextArea("Message:");
        add(makeWebsocketMessagePanel(), CENTER);
    }

    @Override
    public String getStaticLabel() {
        return "Websocket Message Sampler";
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
    public void modifyTestElement(TestElement testElement) {
        super.modifyTestElement(testElement);
        WebsocketMessageSampler sampler = (WebsocketMessageSampler) testElement;
        sampler.setMessage(message.getText());
        super.configureTestElement(testElement);
    }

    @Override
    public void configure(TestElement sampler) {
        super.configure(sampler);
        message.setText(((WebsocketMessageSampler) sampler).getMessage());
    }

    private JPanel makeWebsocketMessagePanel() {
        JPanel websocketPanel = new JPanel(new BorderLayout());
        websocketPanel.add(makeSessionIdPanel(), NORTH);
        websocketPanel.add(makeMessageBodyPanel(), CENTER);
        return websocketPanel;
    }

    private Component makeMessageBodyPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(message, CENTER);
        return panel;
    }
}