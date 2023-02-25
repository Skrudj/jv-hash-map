package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int thereshold;
    private int capacity;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        thereshold = (int) (capacity * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hash = getHash(key);
        Node<K,V> newNode = new Node<>(hash, key, value, null);
        if (table[hash] == null) {
            table[hash] = newNode;
            size++;
            return;
        }
        Node<K,V> currentNode = table[hash];
        while (true) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                break;
            }
            currentNode = currentNode.next;
        }
        currentNode.next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = getHash(key);
        Node<K,V> currentNode = table[hash];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size >= thereshold) {
            size = 0;
            capacity = capacity << 1;
            Node<K,V>[] oldTable = table;
            table = new Node[capacity];
            thereshold = (int) (capacity * LOAD_FACTOR);
            for (Node<K,V> element: oldTable) {
                while (element != null) {
                    put(element.key, element.value);
                    element = element.next;
                }
            }
        }
    }

    private int getHash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
