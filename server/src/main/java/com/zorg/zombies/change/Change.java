package com.zorg.zombies.change;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Change {

    @JsonIgnore
    private final boolean update;

    public Change(boolean update) {
        this.update = update;
    }

    public Change() {
        this(true);
    }

//    private final String name;

}
