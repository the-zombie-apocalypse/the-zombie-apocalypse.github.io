package com.zorg.zombies.map;

import com.zorg.zombies.model.Coordinates;
import org.springframework.stereotype.Component;

@Component
public class MapChunkSupervisor {

    private final MapChunk temporaryChunk = new DefaultMapChunk();

    public MapChunk getChunkFor(Coordinates coordinates) {
        return temporaryChunk;
    }

    public MapChunk getChunkFor(String userId) {
        return temporaryChunk;
    }
}
