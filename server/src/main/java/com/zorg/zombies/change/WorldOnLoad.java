package com.zorg.zombies.change;

import com.zorg.zombies.model.Coordinates;
import com.zorg.zombies.model.UserData;
import com.zorg.zombies.model.UserSubscriber;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class WorldOnLoad extends WorldChange<UserPositionChange> {

    private boolean isGreeting = true;
    private final Collection<UserData> users;

    public WorldOnLoad(UserPositionChange user, Collection<UserSubscriber> otherUsers) {
        super(user);
        this.users = otherUsers.stream().map(UserData::new).collect(Collectors.toList());
    }

    public static WorldOnLoad forTest(String id, Coordinates coordinates) {
        return new WorldOnLoad(new UserPositionChange(id, coordinates), new ArrayList<>());
    }
}
