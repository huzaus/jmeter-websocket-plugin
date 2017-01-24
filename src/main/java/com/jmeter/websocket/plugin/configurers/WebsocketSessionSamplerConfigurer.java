package com.jmeter.websocket.plugin.configurers;

import com.jmeter.websocket.plugin.samplers.WebsocketSessionSampler;
import com.jmeter.websocket.plugin.samplers.WebsocketSessionSamplerGui;

public class WebsocketSessionSamplerConfigurer implements Configurer<WebsocketSessionSampler, WebsocketSessionSamplerGui> {
    @Override
    public void configure(WebsocketSessionSampler sampler, WebsocketSessionSamplerGui samplerGui) {
        samplerGui.getConnectTimeOut().setText(sampler.getConnectTimeOut());
    }
}
