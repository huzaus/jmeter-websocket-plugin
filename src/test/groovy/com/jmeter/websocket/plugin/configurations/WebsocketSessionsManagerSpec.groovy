package com.jmeter.websocket.plugin.configurations

import com.jmeter.websocket.plugin.endpoint.WebsocketSession
import spock.lang.Specification
import spock.lang.Subject

import java.nio.file.Files

class WebsocketSessionsManagerSpec extends Specification {

    @Subject
    WebsocketSessionsManager manager = new WebsocketSessionsManager()

    def "Should return the same WebsocketSession"() {
        when:
        WebsocketSession session = manager.getWebsocketSession()
        then:
        session.is(manager.getWebsocketSession())
    }

    def "Should set file to WebsocketSession from manager"() {
        given:
        manager.setFile(Files.createTempFile("temp-file", ".tmp").toString())
        when:
        WebsocketSession session = manager.getWebsocketSession()
        then:
        session.file.toString() == manager.file
    }
}
