package com.zorg.zombies.change;

import com.zorg.zombies.model.Coordinates;
import lombok.Data;

@Data
public class UserChange {

    protected boolean updated = true;

    protected boolean movingChange;
    protected boolean stopMovingChange;
    protected Coordinates coordinates;
    private String id;

    public UserChange(String id) {
        this.id = id;
    }

    /**
     * @deprecated this exist for jackson mapper only, don't use it!!!!1
     */
    @Deprecated
    public UserChange() {
    }

    public UserChange(String id, Coordinates coordinates) {
        this(id);
        this.coordinates = coordinates;
    }
}
