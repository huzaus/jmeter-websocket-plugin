package com.jmeter.websocket.plugin.elements.helpers;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;

public interface Configurer<S extends AbstractSampler, G extends AbstractSamplerGui> {
    void configure(S sampler, G samplerGui);
}
