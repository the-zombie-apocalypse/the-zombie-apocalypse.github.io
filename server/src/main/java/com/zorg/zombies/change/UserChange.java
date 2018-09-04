package com.zorg.zombies.change;

import com.zorg.zombies.model.Coordinates;
import lombok.Data;

@Data
public class UserChange {

    protected boolean updated = true;

    private String id;

    private Coordinates coordinates;

    public UserChange(String id) {
        this.id = id;
    }

    public UserChange() {
    }
}
