package com.zorg.zombies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.change.WorldOnLoad;
import com.zorg.zombies.model.User;
import com.zorg.zombies.service.UserIdDefiner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.StandardWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;

import static com.zorg.zombies.ZombiesApplication.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameWebSocketHandlerTest {

    private static final Duration TIMEOUT = Duration.ofMillis(5000);
    private final WebSocketClient client = new StandardWebSocketClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @LocalServerPort
    private String port;

    @MockBean
    private UserIdDefiner userIdDefiner;

    private URI getUrl() throws URISyntaxException {
        return new URI("ws://localhost:" + this.port + ENTRY);
    }

    @Test
    void testGreeting() throws Exception {
        final String id = "session-id";
        final User user = new User(id);
        final WorldOnLoad greetingCommand = new WorldOnLoad(new UserChange(user.getId()));
        final Flux<String> producer = Flux.empty();
        final ReplayProcessor<String> output = ReplayProcessor.create(1);

        given(userIdDefiner.getUserId(anyString())).willReturn(id);

        client.execute(getUrl(), session -> session
                .send(producer.map(session::textMessage))
                .thenMany(session.receive().take(1).map(WebSocketMessage::getPayloadAsText))
                .subscribeWith(output)
                .then())
                .block(TIMEOUT);

        final List<String> received = output.collectList().log().block(TIMEOUT);

        assertNotNull(received);

        new WorldChange();
        final String greetingJson = received.get(0);
        final WorldChange worldOnLoad = mapper.readValue(greetingJson, WorldChange.class);

        assertEquals(worldOnLoad, greetingCommand);
    }
}
