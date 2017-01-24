package com.jmeter.websocket.plugin.endpoint.comsumers

import spock.lang.Specification
import spock.lang.Subject

import java.nio.channels.ByteChannel

import static java.nio.file.Files.createTempFile

class CsvFileWriterSpec extends Specification {
    @Subject
    CsvFileWriter csvWriter = new CsvFileWriter(createTempFile("temp-file-delete-on-close", ".tmp"))

    def "Should throw null pointer exception when file is null"() {
        when:
        new CsvFileWriter(null)
        then:
        thrown(NullPointerException)
    }

    def "Should return the same byteChannel "() {
        when:
        ByteChannel channel = csvWriter.byteChannel
        then:
        channel.is(csvWriter.byteChannel)
    }
}
