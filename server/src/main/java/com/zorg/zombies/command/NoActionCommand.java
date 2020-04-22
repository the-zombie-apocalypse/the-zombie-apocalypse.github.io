package com.zorg.zombies.command;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NoActionCommand extends Command {

    public NoActionCommand() {
        super();
        this.noActionCommand = true;
    }
}
