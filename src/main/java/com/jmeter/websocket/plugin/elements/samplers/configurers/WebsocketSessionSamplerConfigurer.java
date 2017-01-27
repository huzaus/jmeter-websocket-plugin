package com.jmeter.websocket.plugin.elements.samplers.configurers;

import com.jmeter.websocket.plugin.elements.helpers.Configurer;
import com.jmeter.websocket.plugin.elements.samplers.WebsocketSessionSampler;
import com.jmeter.websocket.plugin.elements.samplers.WebsocketSessionSamplerGui;

public class WebsocketSessionSamplerConfigurer implements Configurer<WebsocketSessionSampler, WebsocketSessionSamplerGui> {
    @Override
    public void configure(WebsocketSessionSampler sampler, WebsocketSessionSamplerGui samplerGui) {
        samplerGui.getConnectTimeOut().setText(sampler.getConnectTimeOut());
        samplerGui.getServerNameOrIp().setText(sampler.getServerNameOrIp());
        samplerGui.getPortNumber().setText(sampler.getPortNumber());
        samplerGui.getProtocol().setText(sampler.getProtocol());
        samplerGui.getPath().setText(sampler.getPath());
    }
}
