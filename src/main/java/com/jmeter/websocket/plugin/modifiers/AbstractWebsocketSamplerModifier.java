package com.jmeter.websocket.plugin.modifiers;

import com.jmeter.websocket.plugin.samplers.AbstractWebsocketSampler;
import com.jmeter.websocket.plugin.samplers.AbstractWebsocketSamplerGui;

public class AbstractWebsocketSamplerModifier implements Modifier<AbstractWebsocketSamplerGui, AbstractWebsocketSampler> {
    @Override
    public void modify(AbstractWebsocketSamplerGui samplerGui, AbstractWebsocketSampler sampler) {
        sampler.setServerNameOrIp(samplerGui.getServerNameOrIp().getText());
        sampler.setPortNumber(samplerGui.getPortNumber().getText());
        sampler.setProtocol(samplerGui.getProtocol().getText());
        sampler.setPath(samplerGui.getPath().getText());
    }
}
