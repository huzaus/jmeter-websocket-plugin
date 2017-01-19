package com.jmeter.websocket.plugin.configurers;

public interface Configurer <S,T> {
    void configure(S testElement, T guiElement);
}
