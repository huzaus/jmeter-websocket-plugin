package JMeter.plugins.functional.samplers.websocket;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;

public class WebsocketSampler extends AbstractSampler {

    public WebsocketSampler() {
        super();
        setName("Websocket Sampler");
    }

    @Override
    public SampleResult sample(Entry entry) {
        return new SampleResult();
    }
}
