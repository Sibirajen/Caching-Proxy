package com.sibirajen.cache;

import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> implements Cache<K, V>{
    private final class Node{
        K key;
        V value;
        Node prev, next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static final int DEFAULT_CAPACITY = 100;
    private final int capacity;
    private final Node head = new Node(null, null);
    private final Node tail = new Node(null, null);
    private final Map<K, Node> map;

    public LRUCache() {
        this(DEFAULT_CAPACITY);
    }

    public LRUCache(int capacity) {
        if (capacity <= 1)
            throw new IllegalArgumentException("Capacity must be greater than 1");

        this.capacity = capacity;
        this.map = new HashMap<>(capacity);
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    @Override
    public V get(K key) {
        if (!this.map.containsKey(key))
            return null;

        Node node = this.map.get(key);
        this.remove(node);
        this.insertToFront(node);
        return this.map.get(key).value;
    }

    @Override
    public void put(K key, V value) {
        if (key == null || value == null)
            throw new IllegalArgumentException("Key or value must not be null");

        if (map.containsKey(key)){
            Node node = this.map.get(key);
            node.value = value;
            this.remove(node);
            this.insertToFront(node);
        }
        else {
            if (this.map.size() == capacity){
                Node lru = this.tail.prev;
                this.remove(lru);
                this.map.remove(lru.key);
            }
            Node newNode = new Node(key, value);
            this.insertToFront(newNode);
            this.map.put(key, newNode);
        }
    }

    @Override
    public void clearCache(){
        this.map.clear();
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    @Override
    public boolean contains(K key) {
        if (key == null)
            throw new IllegalArgumentException("Key or value must not be null");
        return this.map.containsKey(key);
    }

    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void insertToFront(Node node) {
        node.next = this.head.next;
        node.prev = this.head;

        this.head.next.prev = node;
        head.next = node;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node curr = head.next;
        if (curr == tail) return "Cache is empty";

        while (curr != tail) {
            sb.append(curr.key).append("=").append(curr.value);
            if (curr.next != tail) sb.append("->");
            curr = curr.next;
        }
        return sb.toString();
    }
}