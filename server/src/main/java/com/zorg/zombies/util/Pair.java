package com.zorg.zombies.util;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>A convenience class to represent name-value pairs.</p>
 */
@Data
public class Pair<K, V> implements Serializable {

    /**
     * Key of this <code>Pair</code>.
     */
    private final K key;

    /**
     * Value of this this <code>Pair</code>.
     */
    private final V value;

}
