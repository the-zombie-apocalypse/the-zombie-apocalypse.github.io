package com.zorg.zombies.command.factory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zorg.zombies.command.Command;
import com.zorg.zombies.command.ErrorCommand;
import com.zorg.zombies.command.UserMoveCommand;
import com.zorg.zombies.command.UserStopMoveCommand;
import com.zorg.zombies.command.exception.CommandToJsonParseException;
import com.zorg.zombies.model.MoveDirection;
import com.zorg.zombies.model.factory.MoveDirectionFactory;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.zorg.zombies.command.Command.MOVE_COMMAND_FIELD;
import static com.zorg.zombies.command.MoveDirectionCommand.*;
import static com.zorg.zombies.command.MoveDirectionCommand.MOVE_STOP_COMMAND_FIELD;

@Component
public class CommandFactory {

    private final MoveDirectionFactory moveDirectionFactory;

    private final ObjectMapper mapper = new ObjectMapper();

    public CommandFactory(MoveDirectionFactory moveDirectionFactory) {
        this.moveDirectionFactory = moveDirectionFactory;
    }

    @SneakyThrows
    public Command fromJson(String jsonCommand) {

        try {
            final Map<String, String> jsonAsMap = mapper.readValue(jsonCommand, new TypeReference<Map<String, String>>() {
            });

            if ((jsonAsMap == null) || jsonAsMap.isEmpty()) throw new CommandToJsonParseException(jsonCommand);

            final String moveDirection = jsonAsMap.get(DIRECTION_FIELD);

            if (moveDirection != null) {
                final MoveDirection direction = moveDirectionFactory.parseMoveDirection(moveDirection);

                if ("true".equals(jsonAsMap.get(MOVE_COMMAND_FIELD))) return new UserMoveCommand(direction);
                if ("true".equals(jsonAsMap.get(MOVE_STOP_COMMAND_FIELD))) return new UserStopMoveCommand(direction);
            }
        } catch (Exception e) {
            return new ErrorCommand(e);
        }

        throw new RuntimeException("Continue implementation");
    }
}
