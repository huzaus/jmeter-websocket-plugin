package com.jmeter.websocket.plugin.elements.helpers;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;

public interface Modifier<G extends AbstractSamplerGui, S extends AbstractSampler> {
    void modify(G guiElement, S testElement);
}
