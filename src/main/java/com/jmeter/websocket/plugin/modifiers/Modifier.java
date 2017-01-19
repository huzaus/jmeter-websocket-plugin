package com.jmeter.websocket.plugin.modifiers;

public interface Modifier<S, T> {
    void modify(S guiElement, T testElement);
}
