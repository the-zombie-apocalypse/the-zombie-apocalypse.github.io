package com.zorg.zombies.change;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserChange extends Change {

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
