package com.jmeter.websocket.plugin.configurations

import com.jmeter.websocket.plugin.endpoint.WebsocketClient
import spock.lang.Specification
import spock.lang.Subject

import java.nio.file.Files

class WebsocketSessionsManagerSpec extends Specification {

    @Subject
    WebsocketSessionsManager manager = new WebsocketSessionsManager()

    def "Should return the same WebsocketSession"() {
        when:
        WebsocketClient client = manager.getWebsocketClient()
        then:
        client.is(manager.getWebsocketClient())
    }

    def "Should set file to WebsocketSession from manager"() {
        given:
        manager.setFile(Files.createTempFile("temp-file", ".tmp").toString())
        when:
        WebsocketClient client = manager.getWebsocketClient()
        then:
        client.file.toString() == manager.file
    }
}
