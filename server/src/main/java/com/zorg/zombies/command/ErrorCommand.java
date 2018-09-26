package com.zorg.zombies.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ErrorCommand extends Command {

    @JsonIgnore
    private transient Throwable error;

    {
        isErrorCommand = true;
    }

    public ErrorCommand(Exception e) {
        this.error = e;
    }
}
