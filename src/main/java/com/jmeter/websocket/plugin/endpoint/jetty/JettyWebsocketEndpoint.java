package com.jmeter.websocket.plugin.endpoint.jetty;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.jmeter.websocket.plugin.endpoint.WebsocketSession;
import com.jmeter.websocket.plugin.endpoint.jetty.converters.CookieManagerToWebSocketClientConverter;
import com.jmeter.websocket.plugin.endpoint.jetty.converters.HeadersToClientUpgradeRequestConverter;
import com.jmeter.websocket.plugin.endpoint.jetty.converters.SampleResultToUpgradeListenerConverter;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.websocket.client.io.UpgradeListener;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.ByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Suppliers.memoize;
import static java.lang.System.lineSeparator;
import static java.nio.ByteBuffer.wrap;
import static java.nio.file.Files.newByteChannel;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class JettyWebsocketEndpoint implements WebsocketSession {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final Set<StandardOpenOption> OPEN_OPTIONS = ImmutableSet.of(APPEND, CREATE);
    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss.SSS";
    private static final String SEND = "send";
    private static final String RECEIVE = "receive";
    private static final String SEPARATOR = ",";

    private final Path file;
    private Session session;

    private Function<CookieManager, WebSocketClient> cookieManagerToWebSocketClientConverter = new CookieManagerToWebSocketClientConverter();
    private Function<Map<String, List<String>>, ClientUpgradeRequest> headersToClientUpgradeRequestConverter = new HeadersToClientUpgradeRequestConverter();
    private Function<SampleResult, UpgradeListener> sampleResultToUpgradeListenerConverter = new SampleResultToUpgradeListenerConverter();
    private Supplier<ByteChannel> byteChannelSupplier = byteChannelSupplier();

    public JettyWebsocketEndpoint(Path file) {
        checkNotNull(file, "File should be set");
        this.file = file;
    }

    @Override
    public void connect(URI uri, CookieManager cookieManager, Map<String, List<String>> headers, SampleResult result, long timeOut) throws Exception {
        checkArgument(session == null || !session.isOpen(), "Session is already open");
        log.info("Opening " + uri + " connection." +
                " CookieManager: " + cookieManager +
                " Headers: " + headers);
        WebSocketClient webSocketClient = cookieManagerToWebSocketClientConverter.apply(cookieManager);
        webSocketClient.start();
        session = webSocketClient
                .connect(this,
                        uri,
                        headersToClientUpgradeRequestConverter.apply(headers),
                        sampleResultToUpgradeListenerConverter.apply(result))
                .get(timeOut, MILLISECONDS);
    }

    @Override
    public void sendMessage(String message) throws IOException {
        checkNotNull(session, "Session is null");
        checkArgument(session.isOpen(), "Session is not open");
        log.debug("sendMessage() message: " + message);
        session.getRemote().sendString(message);
        write(SEND, message);
    }

    @Override
    @OnWebSocketMessage
    public void onReceiveMessage(String message) throws IOException {
        log.debug("onReceiveMessage() message: " + message);
        write(RECEIVE, message);
    }

    @OnWebSocketClose
    public void onWebSocketClose(Session session, int closeCode, String closeReason) {
        log.debug("onWebSocketClose()" +
                " session: " + session +
                " closeCode: " + closeCode +
                " closeReason: " + closeReason);
        ByteChannel byteChannel = getByteChannel();
        if (byteChannel != null && byteChannel.isOpen()) {
            try {
                byteChannel.close();
            } catch (IOException e) {
                log.error("Exception thrown on close byteChannel: " + e);
            }
        }
    }

    private void write(String action, String message) {
        ByteChannel byteChannel = getByteChannel();
        if (byteChannel != null) {
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            byte data[] = (
                    Joiner.on(SEPARATOR)
                            .join(
                                    dateFormat.format(new Date()),
                                    Integer.toHexString(session.hashCode()),
                                    action,
                                    message

                            )
                            .concat(lineSeparator())
                            .getBytes()
            );
            try {
                byteChannel.write(wrap(data));
            } catch (IOException e) {
                log.error("Exception thrown on write to byte channel: " + e);
            }
        } else {
            log.info(message);
        }
    }

    private ByteChannel getByteChannel() {
        return byteChannelSupplier.get();
    }

    private Supplier<ByteChannel> byteChannelSupplier() {
        return memoize(new Supplier<ByteChannel>() {
            @Override
            public ByteChannel get() {
                try {
                    return newByteChannel(file, OPEN_OPTIONS);
                } catch (IOException e) {
                    log.error("Exception thrown on open byte channel: " + e);
                    return null;
                }
            }
        });
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("file", file)
                .add("session", session)
                .add("hash", hashCode())
                .toString();
    }
}
