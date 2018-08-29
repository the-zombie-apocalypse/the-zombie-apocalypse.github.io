package com.zorg.zombies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zorg.zombies.command.Command;
import com.zorg.zombies.command.UserMoveCommand;
import com.zorg.zombies.command.exception.WrongUserIdException;
import com.zorg.zombies.command.factory.CommandFactory;
import com.zorg.zombies.model.MoveDirectionX;
import com.zorg.zombies.model.factory.MoveDirectionFactory;
import com.zorg.zombies.model.factory.WrongMoveDirectionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.zorg.zombies.command.Command.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommandFactoryTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private final MoveDirectionFactory moveDirectionFactory = new MoveDirectionFactory();
    private final CommandFactory commandFactory = new CommandFactory(moveDirectionFactory);

    @Test
    void commandParse_When_ValidMoveCommandSent_Expect_Parsed() throws Exception {

        final UserMoveCommand command = new UserMoveCommand("test-id", MoveDirectionX.WEST);
        final String commandAsJson = mapper.writeValueAsString(command);

        final Command parsed = commandFactory.fromJson(commandAsJson);

        assertEquals(command, parsed);
    }

    @Test
    void commandParse_When_NullUserIdInMoveCommandSent_Expect_Parsed() throws Exception {

        final UserMoveCommand command = new UserMoveCommand(null, MoveDirectionX.WEST);
        final String commandAsJson = mapper.writeValueAsString(command);

        assertThrows(WrongUserIdException.class, () -> commandFactory.fromJson(commandAsJson));
    }

    @Test
    void commandParse_When_EmptyUserIdInMoveCommandSent_Expect_Parsed() throws Exception {

        final UserMoveCommand command = new UserMoveCommand("", MoveDirectionX.WEST);
        final String commandAsJson = mapper.writeValueAsString(command);

        assertThrows(WrongUserIdException.class, () -> commandFactory.fromJson(commandAsJson));
    }

    @Test
    void commandParse_When_InvalidMoveDirectionInMoveCommandSent_Expect_Parsed() {

        final String commandAsJson = "{" +
                "\"userId\":\"test\"," +
                "\"" + MOVE_COMMAND_FIELD + "\":\"true\"," +
                "\"" + MOVE_DIRECTION_FIELD + "\":\"wrong\"" +
                "}";

        assertThrows(WrongMoveDirectionException.class, () -> commandFactory.fromJson(commandAsJson));
    }
}
