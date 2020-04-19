package com.zorg.zombies.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@EqualsAndHashCode(of = "id")
public class UserData {

    protected final String id;
    protected final Coordinates coordinates;

    @Setter
    @Getter
    private volatile String nickname;

    public UserData(String id) {
        this(id, new Coordinates(0, 0));
    }

    public UserData(String id, Coordinates coordinates) {
        this.id = id;
        this.coordinates = coordinates;
    }

    public UserData(String id, Coordinates coordinates, String nickname) {
        this.id = id;
        this.coordinates = coordinates;
        this.nickname = nickname;
    }

    public UserData(UserData from) {
        this(from.getId(), from.getCoordinates(), from.getNickname());
    }

    public void setPosition(Coordinates coordinates) {
        coordinates.set(coordinates);
    }
}
