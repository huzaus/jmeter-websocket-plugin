package com.jmeter.websocket.plugin.endpoint.comsumers

import com.google.common.base.Supplier
import spock.lang.Specification
import spock.lang.Subject

import java.nio.channels.ByteChannel
import java.nio.file.Path

import static com.jmeter.websocket.plugin.endpoint.comsumers.CsvFileWriter.csvFileWriterSupplier
import static java.nio.file.Files.createTempFile
import static java.nio.file.Files.delete

class CsvFileWriterSpec extends Specification {
    
    Path file
    @Subject
    CsvFileWriter csvWriter
    
    def setup() {
        file = createTempFile("temp-file-delete-on-close", ".tmp")
        csvWriter = new CsvFileWriter(file)
    }
    
    def cleanup() {
        delete file
    }
    
    def "Should throw null pointer exception when file is null"() {
        when:
            new CsvFileWriter(null)
        then:
            thrown(NullPointerException)
    }
    
    def "Should return the same CsvFileWriter"() {
        when:
            Supplier supplier = csvFileWriterSupplier(csvWriter.file)
        then:
            supplier.get().is(supplier.get())
    }
    
    def "Should return the same byteChannel "() {
        when:
            ByteChannel channel = csvWriter.byteChannel
        then:
            channel.is(csvWriter.byteChannel)
    }
    
    def "Should write data to byteChannel on onMessageReceive"() {
        given:
            ByteChannel byteChannel = Mock()
            csvWriter.byteChannelSupplier = Stub(Supplier) {
                get() >> byteChannel
            }
        when:
            csvWriter.onMessageReceive('101', 'Message')
        then:
            1 * byteChannel.write(_)
    }
    
    def "Should catch exception thrown by byteChannel on onMessageReceive"() {
        given:
            ByteChannel byteChannel = Mock()
            csvWriter.byteChannelSupplier = Stub(Supplier) {
                get() >> byteChannel
            }
        when:
            csvWriter.onMessageReceive('101', 'Message')
        then:
            1 * byteChannel.write(_) >> { throw new IOException() }
        and:
            noExceptionThrown()
    }
    
    def "Should write data to byteChannel on onMessageSend"() {
        given:
            ByteChannel byteChannel = Mock()
            csvWriter.byteChannelSupplier = Stub(Supplier) {
                get() >> byteChannel
            }
        when:
            csvWriter.onMessageSend('101', 'Message')
        then:
            1 * byteChannel.write(_)
    }
    
    def "Should catch exception thrown by byteChannel on onMessageSend"() {
        given:
            ByteChannel byteChannel = Mock()
            csvWriter.byteChannelSupplier = Stub(Supplier) {
                get() >> byteChannel
            }
        when:
            csvWriter.onMessageSend('101', 'Message')
        then:
            1 * byteChannel.write(_) >> { throw new IOException() }
        and:
            noExceptionThrown()
    }
    
    
    def "Should close byteChannel if it is open on stop "() {
        given:
            ByteChannel byteChannel = Mock()
            csvWriter.byteChannelSupplier = Stub(Supplier) {
                get() >> byteChannel
            }
        when:
            csvWriter.stop()
        then:
            1 * byteChannel.isOpen() >> true
        and:
            1 * byteChannel.close()
    }
    
    def "Should catch thrown by byteChannel on stop "() {
        given:
            ByteChannel byteChannel = Mock()
            csvWriter.byteChannelSupplier = Stub(Supplier) {
                get() >> byteChannel
            }
        when:
            csvWriter.stop()
        then:
            1 * byteChannel.isOpen() >> true
        and:
            1 * byteChannel.close() >> { throw new IOException() }
        and:
            noExceptionThrown()
    }
    
}
