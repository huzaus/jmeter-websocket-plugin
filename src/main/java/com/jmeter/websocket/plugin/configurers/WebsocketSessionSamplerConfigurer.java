package com.jmeter.websocket.plugin.configurers;

import com.jmeter.websocket.plugin.samplers.WebsocketSessionSampler;
import com.jmeter.websocket.plugin.samplers.WebsocketSessionSamplerGui;

public class WebsocketSessionSamplerConfigurer implements Configurer<WebsocketSessionSampler, WebsocketSessionSamplerGui> {
    @Override
    public void configure(WebsocketSessionSampler sampler, WebsocketSessionSamplerGui samplerGui) {
        samplerGui.getServerNameOrIp().setText(sampler.getServerNameOrIp());
        samplerGui.getPortNumber().setText(sampler.getPortNumber());
        samplerGui.getProtocol().setText(sampler.getProtocol());
        samplerGui.getConnectTimeOut().setText(sampler.getConnectTimeOut());
        samplerGui.getResponseTimeOut().setText(sampler.getResponseTimeOut());
        samplerGui.getPath().setText(sampler.getPath());
        samplerGui.getMessage().setText(sampler.getMessage());
    }
}
