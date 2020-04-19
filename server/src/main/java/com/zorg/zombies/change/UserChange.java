package com.zorg.zombies.change;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserChange extends Change {

    private final String id;
    protected volatile String nickname;

    protected UserChange(String id, boolean isUpdate) {
        super(isUpdate);
        this.id = id;
    }

    public UserChange(String id, String nickname) {
        super(true);
        this.id = id;
        this.nickname = nickname;
    }

    protected UserChange(String id) {
        super();
        this.id = id;
    }

    /**
     * @deprecated this exist for jackson mapper only, don't use it!!!!1
     */
    @Deprecated
    public UserChange() {
        this(null);
    }

}
