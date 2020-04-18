package com.zorg.zombies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zorg.zombies.change.UserPositionChange;
import com.zorg.zombies.change.WorldOnLoad;
import com.zorg.zombies.model.User;
import com.zorg.zombies.service.UserIdDefiner;
import com.zorg.zombies.service.UsersCommunicator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.List;

import static com.zorg.zombies.ZombiesApplication.ENTRY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameWebSocketHandlerTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(3);
    private final WebSocketClient client = new StandardWebSocketClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @LocalServerPort
    private String port;

    @MockBean
    private UserIdDefiner userIdDefiner;

    @Autowired
    private UsersCommunicator usersCommunicator;

    private URI getUrl() throws URISyntaxException {
        return new URI("ws://localhost:" + this.port + ENTRY);
    }

    @Test
    void testGreeting() throws Exception {
        String id = "session-id";
        User user = new User(id, usersCommunicator);
        Flux<String> producer = Flux.empty();
        ReplayProcessor<String> output = ReplayProcessor.create(1);

        given(userIdDefiner.getUserId(anyString())).willReturn(id);

        client.execute(getUrl(), session -> session
                .send(producer.map(session::textMessage))
                .thenMany(session.receive().take(1).map(WebSocketMessage::getPayloadAsText))
                .subscribeWith(output)
                .then())
                .block(TIMEOUT);

        List<String> received = output.collectList().log().block(TIMEOUT);

        assertNotNull(received);
        assertEquals(received.size(), 1);

        String greetingJson = received.get(0);
        WorldOnLoad worldOnLoad = mapper.readValue(greetingJson, WorldOnLoad.class);

        WorldOnLoad greetingCommand = new WorldOnLoad(
                new UserPositionChange(user.getId(), user.getCoordinates()), new ArrayList<>()
        );
        assertEquals(worldOnLoad, greetingCommand);
    }
}
