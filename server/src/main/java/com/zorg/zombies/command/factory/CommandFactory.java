package com.zorg.zombies.command.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zorg.zombies.command.Command;
import com.zorg.zombies.command.ErrorCommand;
import com.zorg.zombies.command.NoActionCommand;
import com.zorg.zombies.command.UserMoveCommand;
import com.zorg.zombies.command.UserStartGameCommand;
import com.zorg.zombies.command.UserStopMoveCommand;
import com.zorg.zombies.command.exception.CommandToJsonParseException;
import com.zorg.zombies.model.exception.WrongDirectionException;
import com.zorg.zombies.model.geometry.Direction;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import static com.zorg.zombies.command.Command.MOVE_COMMAND_FIELD;
import static com.zorg.zombies.command.MoveDirectionCommand.DIRECTION_FIELD;
import static com.zorg.zombies.command.MoveDirectionCommand.MOVE_STOP_COMMAND_FIELD;
import static com.zorg.zombies.command.UserStartGameCommand.NICKNAME_FIELD;
import static com.zorg.zombies.command.UserStartGameCommand.START_GAME_COMMAND_FIELD;

@Component
@RequiredArgsConstructor
public class CommandFactory {

    private final ObjectMapper mapper;


    @SneakyThrows
    public Command fromJson(String jsonCommand) {
        try {
            JsonNode jsonNode = mapper.readValue(jsonCommand, JsonNode.class);

            if (jsonNode == null) {
                throw new CommandToJsonParseException(jsonCommand);
            }

            JsonNode directionField = jsonNode.get(DIRECTION_FIELD);
            String moveDirection;

            if ((directionField != null)
                    && ((moveDirection = directionField.asText()) != null)
                    && !"null".equalsIgnoreCase(moveDirection)
            ) {
                Direction direction;
                try {
                    direction = Direction.valueOf(moveDirection.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new WrongDirectionException(moveDirection);
                }

                JsonNode moveCommand = jsonNode.get(MOVE_COMMAND_FIELD);
                if ((moveCommand != null) && moveCommand.asBoolean()) {
                    return new UserMoveCommand(direction);
                }
                JsonNode moveStopCommand = jsonNode.get(MOVE_STOP_COMMAND_FIELD);
                if ((moveStopCommand != null) && moveStopCommand.asBoolean()) {
                    return new UserStopMoveCommand(direction);
                }
            }

            JsonNode startGameField = jsonNode.get(START_GAME_COMMAND_FIELD);

            if (startGameField != null && startGameField.asBoolean(false)) {
                return new UserStartGameCommand(jsonNode.get(NICKNAME_FIELD).asText());
            }
        } catch (Exception e) {
            return new ErrorCommand(e);
        }

        return new NoActionCommand();
    }
}
