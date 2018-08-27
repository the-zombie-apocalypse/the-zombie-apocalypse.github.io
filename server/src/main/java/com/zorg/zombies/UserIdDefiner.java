package com.zorg.zombies;

import org.springframework.stereotype.Component;

@Component
public class UserIdDefiner {

    public String getUserId(String sessionId) {
        return sessionId; // todo: implement association between session id and user id
    }

}
