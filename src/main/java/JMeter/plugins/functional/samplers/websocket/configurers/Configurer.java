package JMeter.plugins.functional.samplers.websocket.configurers;

public interface Configurer <S,T> {
    void configure(S testElement, T guiElement);
}
