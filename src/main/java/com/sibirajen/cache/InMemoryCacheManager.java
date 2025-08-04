package com.sibirajen.cache;

import lombok.ToString;

@ToString
public class InMemoryCacheManager<K, V> implements CacheManager<K, V>{
    private final Cache<K, V> cache = new LRUCache<>();

    @Override
    public V get(K key) {
        return this.cache.get(key);
    }

    @Override
    public void put(K key, V value) {
        this.cache.put(key, value);
    }

    @Override
    public void clearCache() {
        this.cache.clearCache();
    }

    @Override
    public boolean contains(K key) {
        return cache.contains(key);
    }
}
