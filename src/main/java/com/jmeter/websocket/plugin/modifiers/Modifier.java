package com.jmeter.websocket.plugin.modifiers;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;

public interface Modifier<S extends AbstractSamplerGui, T extends AbstractSampler> {
    void modify(S guiElement, T testElement);
}
