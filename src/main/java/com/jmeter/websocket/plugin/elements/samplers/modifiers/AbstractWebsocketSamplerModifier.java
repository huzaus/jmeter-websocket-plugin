package com.jmeter.websocket.plugin.elements.samplers.modifiers;

import com.jmeter.websocket.plugin.elements.helpers.Modifier;
import com.jmeter.websocket.plugin.elements.samplers.AbstractWebsocketSampler;
import com.jmeter.websocket.plugin.elements.samplers.AbstractWebsocketSamplerGui;

public class AbstractWebsocketSamplerModifier implements Modifier<AbstractWebsocketSamplerGui, AbstractWebsocketSampler> {
    @Override
    public void modify(AbstractWebsocketSamplerGui samplerGui, AbstractWebsocketSampler sampler) {
        sampler.setSessionId(samplerGui.getSessionId().getText());
    }
}
