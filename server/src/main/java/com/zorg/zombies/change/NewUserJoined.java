package com.zorg.zombies.change;

import com.zorg.zombies.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NewUserJoined extends WorldChange<OnLoadUserChange> {

    public NewUserJoined(User user) {
        super(new OnLoadUserChange(user), true);
    }

}
