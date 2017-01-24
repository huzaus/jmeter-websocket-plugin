package com.jmeter.websocket.plugin.configurers;

import com.jmeter.websocket.plugin.samplers.AbstractWebsocketSampler;
import com.jmeter.websocket.plugin.samplers.AbstractWebsocketSamplerGui;

public class AbstractWebsocketSamplerConfigurer implements Configurer<AbstractWebsocketSampler, AbstractWebsocketSamplerGui> {

    @Override
    public void configure(AbstractWebsocketSampler sampler, AbstractWebsocketSamplerGui samplerGui) {
        samplerGui.getServerNameOrIp().setText(sampler.getServerNameOrIp());
        samplerGui.getPortNumber().setText(sampler.getPortNumber());
        samplerGui.getProtocol().setText(sampler.getProtocol());
        samplerGui.getPath().setText(sampler.getPath());
    }
}
