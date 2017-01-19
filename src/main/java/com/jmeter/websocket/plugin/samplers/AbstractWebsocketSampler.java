package com.jmeter.websocket.plugin.samplers;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.jmeter.websocket.plugin.configurations.WebsocketSessionsManager;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.testelement.property.TestElementProperty;

public abstract class AbstractWebsocketSampler extends AbstractSampler {

    protected WebsocketSessionsManager getWebsocketSessionsManager() {
        return Optional.fromNullable(getProperty(WebsocketSessionsManager.WEBSOCKET_MANAGER).getObjectValue())
                .transform(
                        new Function<Object, WebsocketSessionsManager>() {
                            @Override
                            public WebsocketSessionsManager apply(Object property) {
                                return (WebsocketSessionsManager) property;
                            }
                        })
                .orNull();
    }

    protected void setWebsocketSessionsManager(WebsocketSessionsManager websocketSessionsManager) {
        setProperty(new TestElementProperty(WebsocketSessionsManager.WEBSOCKET_MANAGER, websocketSessionsManager));
    }
}
