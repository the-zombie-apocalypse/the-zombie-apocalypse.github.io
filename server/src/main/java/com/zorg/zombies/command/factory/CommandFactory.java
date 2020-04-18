package com.zorg.zombies.command.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zorg.zombies.command.Command;
import com.zorg.zombies.command.ErrorCommand;
import com.zorg.zombies.command.UserMoveCommand;
import com.zorg.zombies.command.UserStopMoveCommand;
import com.zorg.zombies.command.exception.CommandToJsonParseException;
import com.zorg.zombies.model.geometry.Direction;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import static com.zorg.zombies.command.Command.MOVE_COMMAND_FIELD;
import static com.zorg.zombies.command.MoveDirectionCommand.DIRECTION_FIELD;
import static com.zorg.zombies.command.MoveDirectionCommand.MOVE_STOP_COMMAND_FIELD;

@Component
public class CommandFactory {

    private final ObjectMapper mapper = new ObjectMapper();


    @SneakyThrows
    public Command fromJson(String jsonCommand) {

        try {
             JsonNode jsonNode = mapper.readValue(jsonCommand, JsonNode.class);

            if (jsonNode == null) {
                throw new CommandToJsonParseException(jsonCommand);
            }

            String moveDirection = jsonNode.get(DIRECTION_FIELD).asText();

            if (moveDirection != null) {
                Direction direction = Direction.valueOf(moveDirection.toUpperCase());

                JsonNode moveCommand = jsonNode.get(MOVE_COMMAND_FIELD);
                if ((moveCommand != null) && moveCommand.asBoolean()) {
                    return new UserMoveCommand(direction);
                }
                JsonNode moveStopCommand = jsonNode.get(MOVE_STOP_COMMAND_FIELD);
                if ((moveStopCommand != null) && moveStopCommand.asBoolean()) {
                    return new UserStopMoveCommand(direction);
                }
            }
        } catch (Exception e) {
            return new ErrorCommand(e);
        }

        throw new RuntimeException("Continue implementation");
    }
}
