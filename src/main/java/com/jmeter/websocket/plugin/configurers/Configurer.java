package com.jmeter.websocket.plugin.configurers;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;

public interface Configurer<S extends AbstractSampler, T extends AbstractSamplerGui> {
    void configure(S sampler, T samplerGui);
}
