package com.jmeter.websocket.plugin.modifiers;

import com.jmeter.websocket.plugin.samplers.WebsocketSessionSampler;
import com.jmeter.websocket.plugin.samplers.WebsocketSessionSamplerGui;

public class WebsocketSessionSamplerModifier implements Modifier<WebsocketSessionSamplerGui, WebsocketSessionSampler> {

    @Override
    public void modify(WebsocketSessionSamplerGui samplerGui, WebsocketSessionSampler sampler) {
        sampler.setServerNameOrIp(samplerGui.getServerNameOrIp().getText());
        sampler.setPortNumber(samplerGui.getPortNumber().getText());
        sampler.setProtocol(samplerGui.getProtocol().getText());
        sampler.setConnectTimeOut(samplerGui.getConnectTimeOut().getText());
        sampler.setResponseTimeOut(samplerGui.getResponseTimeOut().getText());
        sampler.setPath(samplerGui.getPath().getText());
        sampler.setMessage(samplerGui.getMessage().getText());
    }
}
