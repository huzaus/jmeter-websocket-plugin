package JMeter.plugins.functional.samplers.websocket.modifiers;

public interface Modifier<S, T> {
    void modify(S guiElement, T testElement);
}
