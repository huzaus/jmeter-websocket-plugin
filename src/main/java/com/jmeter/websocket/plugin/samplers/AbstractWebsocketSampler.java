package com.jmeter.websocket.plugin.samplers;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.jmeter.websocket.plugin.configurations.WebsocketSessionsManager;
import com.jmeter.websocket.plugin.endpoint.WebsocketClient;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.TestElementProperty;

public abstract class AbstractWebsocketSampler extends AbstractSampler {

    public static final String WEBSOCKET_MANAGER = "websocket_manager";

    protected WebsocketSessionsManager getWebsocketSessionsManager() {
        return Optional.fromNullable(getProperty(WEBSOCKET_MANAGER).getObjectValue())
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
        setProperty(new TestElementProperty(WEBSOCKET_MANAGER, websocketSessionsManager));
    }

    public WebsocketClient getWebsocketClient() {
        return Optional.fromNullable(getWebsocketSessionsManager())
                .transform(
                        new Function<WebsocketSessionsManager, WebsocketClient>() {
                            @Override
                            public WebsocketClient apply(WebsocketSessionsManager manager) {
                                return manager.getWebsocketClient();
                            }
                        }
                )
                .orNull();
    }

    @Override
    public void addTestElement(TestElement el) {
        if (el instanceof WebsocketSessionsManager) {
            setWebsocketSessionsManager((WebsocketSessionsManager) el);
        } else {
            super.addTestElement(el);
        }
    }
}
