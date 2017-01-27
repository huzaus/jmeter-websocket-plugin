package com.jmeter.websocket.plugin.configurers;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;

public interface Configurer<S extends AbstractSampler, G extends AbstractSamplerGui> {
    void configure(S sampler, G samplerGui);
}
