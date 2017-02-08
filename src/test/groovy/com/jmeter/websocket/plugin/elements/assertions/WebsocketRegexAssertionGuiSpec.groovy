package com.jmeter.websocket.plugin.elements.assertions

import com.jmeter.websocket.plugin.JmeterAbstractGuiSpec
import org.apache.jmeter.assertions.SizeAssertion
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class WebsocketRegexAssertionGuiSpec extends JmeterAbstractGuiSpec {
    @Subject
    WebsocketRegexAssertionGui assertionGui = new WebsocketRegexAssertionGui()

    def "#component should be initialized in constructor with #label"() {
        expect:
            assertionGui."$component".mLabel.text == label
        where:
            component   || label
            'sessionId' || 'Session id:'
            'timeout'   || 'Timeout (milliseconds):'
            'regex'     || 'Regex:'
    }

    def "Should modify #property property with '#value' value from gui #component component"() {
        given:
            WebsocketRegexAssertion assertion = new WebsocketRegexAssertion()
            assertionGui."$component".text = value
        when:
            assertionGui.modifyTestElement(assertion)
        then:
            assertion."$property" == value
        where:
            component   | property    | value
            'sessionId' | 'sessionId' | 'user1Session'
            'timeout'   | 'timeout'   | '2000'
            'regex'     | 'regex'     | '.*'
    }

    def "Should configure gui #component component with '#value' value from #property assertion property"() {
        given:
            WebsocketRegexAssertion assertion = new WebsocketRegexAssertion()
            assertion."$property" = value
        when:
            assertionGui.configure(assertion)
        then:
            assertionGui."$component".text == value
        where:
            component   | property    | value
            'sessionId' | 'sessionId' | 'user1Session'
            'timeout'   | 'timeout'   | '2000'
            'regex'     | 'regex'     | '.*'
    }

    def "Should throw ClassCastException when testElement is not WebsocketMessageSampler on configure"() {
        when:
            assertionGui.configure(new SizeAssertion())
        then:
            thrown(ClassCastException)
    }

    def "Should throw ClassCastException when testElement is not WebsocketMessageSampler on modifyTestElement"() {
        when:
            assertionGui.modifyTestElement(new SizeAssertion())
        then:
            thrown(ClassCastException)
    }
}