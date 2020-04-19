package com.zorg.zombies.command;

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
        this.nickname = nickname;
        startGameCommand = true;
    }
}
