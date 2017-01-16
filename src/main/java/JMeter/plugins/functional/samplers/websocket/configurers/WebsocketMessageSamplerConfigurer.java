package JMeter.plugins.functional.samplers.websocket.configurers;

import JMeter.plugins.functional.samplers.websocket.WebsocketMessageSampler;
import JMeter.plugins.functional.samplers.websocket.WebsocketMessageSamplerGui;

public class WebsocketMessageSamplerConfigurer implements Configurer<WebsocketMessageSampler, WebsocketMessageSamplerGui> {
    @Override
    public void configure(WebsocketMessageSampler sampler, WebsocketMessageSamplerGui samplerGui) {
        samplerGui.getServerNameOrIp().setText(sampler.getServerNameOrIp());
        samplerGui.getPortNumber().setText(sampler.getPortNumber());
        samplerGui.getProtocol().setText(sampler.getProtocol());
        samplerGui.getConnectTimeOut().setText(sampler.getConnectTimeOut());
        samplerGui.getResponseTimeOut().setText(sampler.getResponseTimeOut());
        samplerGui.getPath().setText(sampler.getPath());
        samplerGui.getMessage().setText(sampler.getMessage());
    }
}
