package com.jmeter.websocket.plugin.configurations;

import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.gui.util.FilePanel;
import org.apache.jmeter.testelement.TestElement;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

import static java.awt.BorderLayout.NORTH;

public class WebsocketSessionsManagerGui extends AbstractConfigGui {

    private final FilePanel filePanel;
    private static final String[] EXTENSIONS = {".csv"};

    public WebsocketSessionsManagerGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), NORTH);

        filePanel = new FilePanel("Websocket data file", EXTENSIONS);
        add(makeFilePanel());
    }

    @Override
    public String getStaticLabel() {
        return "Websocket Session Manager";
    }

    @Override
    public String getLabelResource() {
        return "websocket.session.manager.title";
    }

    @Override
    public TestElement createTestElement() {
        WebsocketSessionsManager manager = new WebsocketSessionsManager();
        modifyTestElement(manager);
        return manager;
    }

    @Override
    public void modifyTestElement(TestElement testElement) {
        testElement.clear();
        WebsocketSessionsManager manager = (WebsocketSessionsManager) testElement;
        manager.setFile(filePanel.getFilename());
        super.configureTestElement(manager);
    }

    @Override
    public void configure(TestElement manager) {
        filePanel.setFilename(((WebsocketSessionsManager) manager).getFile());
    }

    private Component makeFilePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(filePanel, NORTH);
        return panel;
    }
}
