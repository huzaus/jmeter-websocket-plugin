package JMeter.plugins.functional.samplers.websocket.configurers;

import JMeter.plugins.functional.samplers.websocket.WebsocketSessionSampler;
import JMeter.plugins.functional.samplers.websocket.WebsocketSessionSamplerGui;

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
