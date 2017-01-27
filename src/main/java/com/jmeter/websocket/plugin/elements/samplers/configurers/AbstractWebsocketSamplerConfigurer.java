package com.jmeter.websocket.plugin.elements.samplers.configurers;

import com.jmeter.websocket.plugin.elements.helpers.Configurer;
import com.jmeter.websocket.plugin.elements.samplers.AbstractWebsocketSampler;
import com.jmeter.websocket.plugin.elements.samplers.AbstractWebsocketSamplerGui;

public class AbstractWebsocketSamplerConfigurer implements Configurer<AbstractWebsocketSampler, AbstractWebsocketSamplerGui> {

    @Override
    public void configure(AbstractWebsocketSampler sampler, AbstractWebsocketSamplerGui samplerGui) {
        samplerGui.getServerNameOrIp().setText(sampler.getServerNameOrIp());
        samplerGui.getPortNumber().setText(sampler.getPortNumber());
        samplerGui.getProtocol().setText(sampler.getProtocol());
        samplerGui.getPath().setText(sampler.getPath());
    }
}
