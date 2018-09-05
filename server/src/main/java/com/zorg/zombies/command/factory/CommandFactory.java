package com.zorg.zombies.command.factory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zorg.zombies.command.Command;
import com.zorg.zombies.command.UserMoveCommand;
import com.zorg.zombies.command.exception.CommandToJsonParseException;
import com.zorg.zombies.command.exception.UserIdIsRequired;
import com.zorg.zombies.model.MoveDirection;
import com.zorg.zombies.model.factory.MoveDirectionFactory;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.zorg.zombies.command.Command.MOVE_COMMAND_FIELD;
import static com.zorg.zombies.command.MoveDirectionCommand.*;

@Component
public class CommandFactory {

    private final MoveDirectionFactory moveDirectionFactory;

    private final ObjectMapper mapper = new ObjectMapper();

    public CommandFactory(MoveDirectionFactory moveDirectionFactory) {
        this.moveDirectionFactory = moveDirectionFactory;
    }

    @SneakyThrows
    public Command fromJson(String jsonCommand) {
        final Map<String, String> jsonAsMap = mapper.readValue(jsonCommand, new TypeReference<Map<String, String>>() {
        });

        if ((jsonAsMap == null) || jsonAsMap.isEmpty()) throw new CommandToJsonParseException(jsonCommand);

        final String userId = jsonAsMap.get("userId");

        if ((userId == null) || userId.isEmpty()) throw new UserIdIsRequired(userId);

        final String isMoveCommand = jsonAsMap.get(MOVE_COMMAND_FIELD);

        if ("true".equals(isMoveCommand)) {

            final String moveDirection = jsonAsMap.get(DIRECTION_FIELD);
            final MoveDirection direction = moveDirectionFactory.parseMoveDirection(moveDirection);

            return new UserMoveCommand(userId, direction);
        }

        throw new RuntimeException("Continue implementation");
    }
}
