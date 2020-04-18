package com.zorg.zombies.change;

import com.zorg.zombies.model.UserData;
import com.zorg.zombies.model.UserSubscriber;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class WorldOnLoad extends WorldChange<UserPositionChange> {

    private boolean isGreeting = true;
    private final Collection<UserData> users;

    @SuppressWarnings("unused") // it is used by json (de)serializer
    public WorldOnLoad() {
        this.users = Collections.emptySet();
    }

    public WorldOnLoad(UserPositionChange user, Collection<UserSubscriber> users) {
        super(user);
        this.users = users.stream().map(UserData::new).collect(Collectors.toList());
    }
}
