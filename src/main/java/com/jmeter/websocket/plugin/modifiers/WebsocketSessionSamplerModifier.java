package com.jmeter.websocket.plugin.modifiers;

import com.jmeter.websocket.plugin.samplers.WebsocketSessionSampler;
import com.jmeter.websocket.plugin.samplers.WebsocketSessionSamplerGui;

public class WebsocketSessionSamplerModifier implements Modifier<WebsocketSessionSamplerGui, WebsocketSessionSampler> {

    @Override
    public void modify(WebsocketSessionSamplerGui samplerGui, WebsocketSessionSampler sampler) {
        sampler.setConnectTimeOut(samplerGui.getConnectTimeOut().getText());
    }
}
