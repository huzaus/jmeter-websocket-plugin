package JMeter.plugins.functional.samplers.websocket.modifiers;

import JMeter.plugins.functional.samplers.websocket.WebsocketSessionSampler;
import JMeter.plugins.functional.samplers.websocket.WebsocketSessionSamplerGui;

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
