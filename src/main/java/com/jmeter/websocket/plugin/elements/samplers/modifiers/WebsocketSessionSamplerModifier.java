package com.jmeter.websocket.plugin.elements.samplers.modifiers;

import com.jmeter.websocket.plugin.elements.helpers.Modifier;
import com.jmeter.websocket.plugin.elements.samplers.WebsocketSessionSampler;
import com.jmeter.websocket.plugin.elements.samplers.WebsocketSessionSamplerGui;

public class WebsocketSessionSamplerModifier implements Modifier<WebsocketSessionSamplerGui, WebsocketSessionSampler> {

    @Override
    public void modify(WebsocketSessionSamplerGui samplerGui, WebsocketSessionSampler sampler) {
        sampler.setConnectTimeOut(samplerGui.getConnectTimeOut().getText());
    }
}
