package com.zorg.zombies.command;

import com.zorg.zombies.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserStartGameCommand extends Command {

    public static final String START_GAME_COMMAND_FIELD = "startGameCommand";
    public static final String NICKNAME_FIELD = "nickname";

    protected final String nickname;

    public UserStartGameCommand(String nickname) {
        this.nickname = trimToValidNickname(nickname);
        startGameCommand = true;
    }

    private String trimToValidNickname(String nickname) {
        if ((nickname == null) || nickname.isBlank()) {
            return Constants.DEFAULT_NICKNAME;
        }

        if (nickname.length() >= Constants.MAX_NICKNAME_LENGTH) {
            return nickname.substring(0, Constants.MAX_NICKNAME_LENGTH);
        }

        return nickname;
    }
}
