package com.sibirajen.cache;

public interface Cache<K, V> {
    V get(K key);
    void put(K key, V value);
    void clearCache();
    boolean contains(K key);
}