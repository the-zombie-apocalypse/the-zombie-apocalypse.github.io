package com.zorg.zombies.command.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zorg.zombies.command.Command;
import com.zorg.zombies.command.NoActionCommand;
import com.zorg.zombies.command.UserMoveCommand;
import com.zorg.zombies.model.geometry.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CommandFactoryTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final CommandFactory commandFactory = new CommandFactory(mapper);

    @Test
    void commandParse_When_InvalidMoveDirectionInMoveCommandSent_Expect_NoActionCommandReturned() throws Exception {
        UserMoveCommand command = new UserMoveCommand(null);
        String commandAsJson = mapper.writeValueAsString(command);
        Command parsed = commandFactory.fromJson(commandAsJson);

        assertTrue(parsed instanceof NoActionCommand);
    }

    @Test
    void commandParse_When_ValidMoveCommandSent_Expect_Parsed() throws Exception {

        UserMoveCommand command = new UserMoveCommand(Direction.WEST);
        String commandAsJson = mapper.writeValueAsString(command);

        Command parsed = commandFactory.fromJson(commandAsJson);

        assertEquals(command, parsed);
    }

}
