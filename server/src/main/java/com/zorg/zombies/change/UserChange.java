package com.zorg.zombies.change;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserChange {

    @JsonIgnore
    protected boolean updated = true;

    protected boolean movingChange;
    protected boolean stopMovingChange;
    protected boolean positionChange;

    private String id;

    protected UserChange(String id) {
        this.id = id;
    }

    /**
     * @deprecated this exist for jackson mapper only, don't use it!!!!1
     */
    @Deprecated
    public UserChange() {
    }

}
