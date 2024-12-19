package org.github.minidoc.util;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KeyValue<K, V> {

    private K key;
    private V value;

}
