package JMeter.plugins.functional.samplers.websocket.modifiers;

import JMeter.plugins.functional.samplers.websocket.WebsocketMessageSampler;
import JMeter.plugins.functional.samplers.websocket.WebsocketMessageSamplerGui;

public class WebsocketMessageSamplerModifier implements Modifier<WebsocketMessageSamplerGui, WebsocketMessageSampler> {

    @Override
    public void modify(WebsocketMessageSamplerGui samplerGui, WebsocketMessageSampler sampler) {
        sampler.setServerNameOrIp(samplerGui.getServerNameOrIp().getText());
        sampler.setPortNumber(samplerGui.getPortNumber().getText());
        sampler.setProtocol(samplerGui.getProtocol().getText());
        sampler.setConnectTimeOut(samplerGui.getConnectTimeOut().getText());
        sampler.setResponseTimeOut(samplerGui.getResponseTimeOut().getText());
        sampler.setPath(samplerGui.getPath().getText());
        sampler.setMessage(samplerGui.getMessage().getText());
    }
}
