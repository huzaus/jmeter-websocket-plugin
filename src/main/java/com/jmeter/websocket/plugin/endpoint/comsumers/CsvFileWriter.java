package com.jmeter.websocket.plugin.endpoint.comsumers;

import com.google.common.base.Joiner;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Suppliers.memoize;
import static java.lang.System.lineSeparator;
import static java.nio.ByteBuffer.wrap;
import static java.nio.file.Files.newByteChannel;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class CsvFileWriter implements WebsocketMessageConsumer {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final Set<StandardOpenOption> OPEN_OPTIONS = ImmutableSet.of(APPEND, CREATE);
    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss.SSS";
    private static final String SEPARATOR = ",";
    private static final String SEND = "send";
    private static final String RECEIVE = "receive";
    private final Path file;
    private Supplier<ByteChannel> byteChannelSupplier = byteChannelSupplier();

    public CsvFileWriter(Path file) {
        checkNotNull(file, "File should be set");
        this.file = file;
    }

    public static Supplier<CsvFileWriter> csvFileWriterSupplier(final Path file) {
        return memoize(new Supplier<CsvFileWriter>() {
            @Override
            public CsvFileWriter get() {
                return new CsvFileWriter(file);
            }
        });
    }

    @Override
    public void onMessageReceive(String sessionId, String message) {
        write(RECEIVE, sessionId, message);
    }

    @Override
    public void onMessageSend(String sessionId, String message) {
        write(SEND, sessionId, message);
    }

    protected ByteChannel getByteChannel() {
        return byteChannelSupplier.get();
    }

    protected void write(String action, String sessionId, String message) {
        ByteChannel byteChannel = getByteChannel();
        if (byteChannel != null) {
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            byte data[] = (
                    Joiner.on(SEPARATOR)
                            .join(
                                    dateFormat.format(new Date()),
                                    sessionId,
                                    action,
                                    message.getBytes().length,
                                    message
                            )
                            .concat(lineSeparator())
                            .getBytes()
            );
            try {
                byteChannel.write(wrap(data));
            } catch (IOException e) {
                log.error("Exception thrown on write to byte channel: ", e);
            }
        }
        log.info(message);
    }

    public void stop() {
        ByteChannel byteChannel = getByteChannel();
        if (byteChannel != null && byteChannel.isOpen()) {
            try {
                byteChannel.close();
            } catch (IOException e) {
                log.error("Exception thrown on close byteChannel: " + e);
            }
        }
    }

    private Supplier<ByteChannel> byteChannelSupplier() {
        return memoize(new Supplier<ByteChannel>() {
            @Override
            public ByteChannel get() {
                try {
                    return newByteChannel(file, OPEN_OPTIONS);
                } catch (IOException e) {
                    log.error("Exception thrown on open byte channel: " + e);
                }
                return null;
            }
        });
    }

}
