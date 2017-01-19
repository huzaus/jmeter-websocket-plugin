package com.jmeter.websocket.plugin.samplers;

import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextArea;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;

public class WebsocketMessageSamplerGui extends AbstractSamplerGui {

    private final JLabeledTextArea message;

    public WebsocketMessageSamplerGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), NORTH);
        message = new JLabeledTextArea("Message:");
        add(makeMessageBodyPanel(), CENTER);

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
        testElement.clear();
        WebsocketMessageSampler sampler = (WebsocketMessageSampler) testElement;
        sampler.setMessage(message.getText());
        super.configureTestElement(testElement);
    }

    @Override
    public void configure(TestElement sampler) {
        super.configure(sampler);
        message.setText(((WebsocketMessageSampler) sampler).getMessage());
    }

    private Component makeMessageBodyPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(message, CENTER);
        return panel;
    }
}