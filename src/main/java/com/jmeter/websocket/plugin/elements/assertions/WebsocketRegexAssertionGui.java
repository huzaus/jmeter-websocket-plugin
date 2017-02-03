package com.jmeter.websocket.plugin.elements.assertions;

import org.apache.jmeter.assertions.gui.AbstractAssertionGui;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static javax.swing.BorderFactory.createEtchedBorder;
import static javax.swing.BorderFactory.createTitledBorder;

public class WebsocketRegexAssertionGui extends AbstractAssertionGui {

    protected final JLabeledTextField sessionId;
    protected final JLabeledTextField regex;
    protected final JLabeledTextField timeout;

    public WebsocketRegexAssertionGui() {
        sessionId = new JLabeledTextField("Session id:", 5);
        timeout = new JLabeledTextField("Timeout (milliseconds):", 5);
        regex = new JLabeledTextField("Regex:", 40);
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), NORTH);
        add(wrap(makeMessageExpectationPanel()), CENTER);
    }

    @Override
    public String getLabelResource() {
        return "websocket.regex.expectation.wrapper.sampler.gui.title";
    }

    @Override
    public TestElement createTestElement() {
        WebsocketRegexAssertion preProcessor = new WebsocketRegexAssertion();
        modifyTestElement(preProcessor);
        return preProcessor;
    }

    @Override
    public void modifyTestElement(TestElement testElement) {
        WebsocketRegexAssertion preProcessor = (WebsocketRegexAssertion) testElement;
        preProcessor.setSessionId(sessionId.getText());
        preProcessor.setTimeout(timeout.getText());
        preProcessor.setRegex(regex.getText());
        super.configureTestElement(testElement);
    }

    @Override
    public void configure(TestElement testElement) {
        super.configure(testElement);
        WebsocketRegexAssertion preProcessor = (WebsocketRegexAssertion) testElement;
        sessionId.setText(preProcessor.getSessionId());
        timeout.setText(preProcessor.getTimeout());
        regex.setText(preProcessor.getRegex());
    }

    @Override
    public String getStaticLabel() {
        return "Websocket Response Regex Assertion";
    }

    private JPanel wrap(JPanel panel) {
        JPanel wrapPanel = new JPanel(new BorderLayout());
        wrapPanel.add(panel, NORTH);
        return wrapPanel;
    }

    private JPanel makeMessageExpectationPanel() {
        JPanel websocketPanel = new HorizontalPanel();
        websocketPanel.setBorder(createTitledBorder(createEtchedBorder(), "Expectation"));
        websocketPanel.add(makeSessionIdPanel());
        websocketPanel.add(makeTimeoutPanel());
        websocketPanel.add(makeRegexPanel());
        return websocketPanel;
    }

    private Component makeSessionIdPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(sessionId, CENTER);
        return panel;
    }

    private Component makeTimeoutPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(timeout, CENTER);
        return panel;
    }

    private Component makeRegexPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(regex, CENTER);
        return panel;
    }
}