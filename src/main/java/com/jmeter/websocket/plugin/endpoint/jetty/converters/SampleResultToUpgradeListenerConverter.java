package com.jmeter.websocket.plugin.endpoint.jetty.converters;

import com.google.common.base.Function;
import com.jmeter.websocket.plugin.endpoint.jetty.JettyWebsocketUpgradeListener;
import org.apache.jmeter.samplers.SampleResult;
import org.eclipse.jetty.websocket.client.io.UpgradeListener;

import static com.google.common.base.Optional.fromNullable;

public class SampleResultToUpgradeListenerConverter implements Function<SampleResult, UpgradeListener> {
    @Override
    public UpgradeListener apply(SampleResult sampleResult) {
        return fromNullable(sampleResult)
                .transform(new Function<SampleResult, UpgradeListener>() {
                    @Override
                    public UpgradeListener apply(SampleResult sampleResult) {
                        return new JettyWebsocketUpgradeListener(sampleResult);
                    }
                })
                .orNull();
    }
}
