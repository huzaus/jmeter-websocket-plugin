package com.jmeter.websocket.plugin.configurations;

import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.testelement.TestElement;

import java.awt.BorderLayout;

import static java.awt.BorderLayout.NORTH;

public class WebsocketSessionsManagerGui extends AbstractConfigGui {

    public WebsocketSessionsManagerGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), NORTH);
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
    public void modifyTestElement(TestElement manager) {
        manager.clear();
        super.configureTestElement(manager);
    }
}
