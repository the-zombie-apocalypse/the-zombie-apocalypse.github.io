package com.zorg.zombies.change;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Change {

    @JsonIgnore
    protected boolean updated = true;

    protected String name;

}
