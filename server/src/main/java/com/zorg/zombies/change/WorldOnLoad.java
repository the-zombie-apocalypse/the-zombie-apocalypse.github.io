package com.zorg.zombies.change;

import com.zorg.zombies.model.User;
import com.zorg.zombies.model.UserData;
import com.zorg.zombies.model.UserSubscriber;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WorldOnLoad extends WorldChange<OnLoadUserChange> {

    private final Collection<UserData> users;

    @SuppressWarnings("unused") // it is used by json (de)serializer
    public WorldOnLoad() {
        this((OnLoadUserChange) null, new HashSet<>());
    }

    public WorldOnLoad(User user, Collection<UserSubscriber> users) {
        this(new OnLoadUserChange(user), users);
    }

    public WorldOnLoad(OnLoadUserChange onLoadUserChange, Collection<UserSubscriber> users) {
        super(onLoadUserChange, true);
        this.users = users.stream().map(UserData::new).collect(Collectors.toList());
    }
}
