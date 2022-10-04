package louie.hanse.shareplate.integration;

import static java.time.format.DateTimeFormatter.ofPattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import louie.hanse.shareplate.config.WebConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;


public class InitSocketIntegrationTest extends InitIntegrationTest {

    private static final String FORMATTER_FIELD_NAME = "_formatter";

    protected StompSession stompSession;
    protected CompletableFuture<Object> completableFuture = new CompletableFuture<>();

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll() throws IllegalAccessException {
        initLocalDateTimeSerializerAndDeserializerFormatter();
    }

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException, TimeoutException {
        List<Transport> transports = List.of(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(transports));

        stompSession = stompClient.connect("ws://localhost:" + port + "/websocket",
            getStompSessionHandlerAdapter(String.class))
            .get(3, TimeUnit.SECONDS);
    }

    private static void initLocalDateTimeSerializerAndDeserializerFormatter()
        throws IllegalAccessException {
        initDateTimeFormatter(LocalDateTimeSerializer.INSTANCE);
        initDateTimeFormatter(LocalDateTimeDeserializer.INSTANCE);
    }

    private static <T> void initDateTimeFormatter(T object) throws IllegalAccessException {
        if (isNotInstanceOfLocalDateTimeSerializerOrDeserializer(object)) {
            throw new RuntimeException("지원하지 않은 타입입니다.");
        }
        Field field = ReflectionUtils.findField(object.getClass(), FORMATTER_FIELD_NAME);
        field.setAccessible(true);
        field.set(object, ofPattern(WebConfig.LOCAL_DATE_TIME_FORMAT));
    }

    private static <T> boolean isNotInstanceOfLocalDateTimeSerializerOrDeserializer(T object) {
        return !(object instanceof LocalDateTimeSerializer ||
            object instanceof LocalDateTimeDeserializer);
    }

    protected <T> StompSessionHandlerAdapter getStompSessionHandlerAdapter(Class<T> clazz) {
        return new StompSessionHandlerAdapter() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    completableFuture.complete(objectMapper.readValue((byte[]) payload, clazz));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void handleException(StompSession session, @Nullable StompCommand command,
                StompHeaders headers, byte[] payload, Throwable exception) {
                throw new RuntimeException(exception);
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                throw new RuntimeException(exception);
            }
        };
    }

}
