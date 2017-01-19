package com.jmeter.websocket.plugin.configurations

import org.eclipse.jetty.websocket.api.Session
import spock.lang.Specification
import spock.lang.Subject

class WebsocketSessionsManagerSpec extends Specification {

    @Subject
    WebsocketSessionsManager manager = new WebsocketSessionsManager()

    def "Should print 'WebsocketSessionsManager{session=is not set}' on toString() when session is not set "() {
        expect:
        manager.toString() startsWith 'WebsocketSessionsManager{session=is not set'
    }

    def "Should print 'WebsocketSessionsManager{session=is set}' on toString() when upgrade request is null "() {
        given:
        manager.session = Mock(Session)
        expect:
        manager.toString() startsWith 'WebsocketSessionsManager{session=is set'
    }

}
