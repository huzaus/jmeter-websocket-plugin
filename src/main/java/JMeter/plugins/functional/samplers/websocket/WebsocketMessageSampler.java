package JMeter.plugins.functional.samplers.websocket;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;

public class WebsocketMessageSampler extends AbstractSampler {

    public WebsocketMessageSampler() {
        super();
        setName("Websocket Message Sampler");
    }

    @Override
    public SampleResult sample(Entry entry) {
        return new SampleResult();
    }
}
