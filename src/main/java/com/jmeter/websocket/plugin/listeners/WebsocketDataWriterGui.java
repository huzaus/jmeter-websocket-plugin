package com.jmeter.websocket.plugin.listeners;

import org.apache.jmeter.gui.util.FilePanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.visualizers.gui.AbstractListenerGui;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

import static java.awt.BorderLayout.NORTH;

public class WebsocketDataWriterGui extends AbstractListenerGui {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private final FilePanel filePanel;
    private static final String[] EXTENSIONS = {".csv"};

    public WebsocketDataWriterGui() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), NORTH);

        filePanel = new FilePanel("Websocket data file", EXTENSIONS);
        add(makeFilePanel());
    }

    @Override
    public String getStaticLabel() {
        return "Websocket Data File Writer";
    }

    @Override
    public String getLabelResource() {
        return "websocket_data_file_writer";
    }

    @Override
    public TestElement createTestElement() {
        WebsocketDataWriter writer = new WebsocketDataWriter();
        modifyTestElement(writer);
        return writer;
    }

    @Override
    public void modifyTestElement(TestElement testElement) {
        testElement.clear();
        WebsocketDataWriter writer = (WebsocketDataWriter) testElement;
        writer.setFile(filePanel.getFilename());
        super.configureTestElement(writer);
    }

    @Override
    public void configure(TestElement writer) {
        super.configure(writer);
        filePanel.setFilename(((WebsocketDataWriter) writer).getFile());
    }

    private Component makeFilePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(filePanel, NORTH);
        return panel;
    }
}


