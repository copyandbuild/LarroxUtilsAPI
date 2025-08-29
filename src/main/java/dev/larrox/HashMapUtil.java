package dev.larrox;

import java.util.HashMap;
import java.util.Map;

public class HashMapUtil {

    /**
     * Erstellt eine neue leere HashMap.
     */
    public static <K, V> Map<K, V> create() {
        return new HashMap<>();
    }

    /**
     * Erstellt eine HashMap mit einem Key-Value-Paar.
     */
    public static <K, V> Map<K, V> of(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    /**
     * Erstellt eine HashMap mit mehreren Key-Value-Paaren (varargs).
     * Erwartet: key1, value1, key2, value2, ...
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> of(Object... keyValues) {
        if (keyValues.length % 2 != 0) {
            throw new IllegalArgumentException("Ungerade Anzahl an Parametern! Muss Key-Value Paare sein.");
        }
        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            K key = (K) keyValues[i];
            V value = (V) keyValues[i + 1];
            map.put(key, value);
        }
        return map;
    }

    /**
     * Builder für HashMaps.
     */
    public static class Builder<K, V> {
        private final Map<K, V> map = new HashMap<>();

        public Builder<K, V> put(K key, V value) {
            map.put(key, value);
            return this;
        }

        public Map<K, V> build() {
            return map;
        }
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }
}
