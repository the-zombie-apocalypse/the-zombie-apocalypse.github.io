package com.zorg.zombies.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class UserData {

    protected String id;
    protected Coordinates coordinates;

    public UserData(String id) {
        this(id, new Coordinates(0, 0));
    }

    public UserData(String id, Coordinates coordinates) {
        this.id = id;
        this.coordinates = coordinates;
    }
}
