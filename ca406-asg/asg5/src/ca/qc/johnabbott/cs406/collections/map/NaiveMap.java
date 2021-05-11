/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.collections.map;

import ca.qc.johnabbott.cs406.collections.list.ArrayList;
import ca.qc.johnabbott.cs406.collections.list.List;
import ca.qc.johnabbott.cs406.profiler.Profiler;

/**
 * A naive implementation of the map interface using a simple link chain.
 * Improved in HashMap.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public class NaiveMap<K,V> implements Map<K,V> {



    /* private inner class for link "chains" */
    private static class Link<T> {
        T element;
        Link<T> next;
        public Link(T element) {
            this.element = element;
        }
        public Link() {}
    }

    private Link<Entry<K,V>> head;
    private int size;
    private Link<Entry<K,V>> current;

    /**
     * Construct a "naive" map.
     */
    public NaiveMap() {
        head = null;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        Profiler.getInstance().startRegion("put(k,v)");
        // try replacing an existing value
        for(Link<Entry<K,V>> current = head; current != null; current = current.next)
            if(current.element.getKey().equals(key)) {
                current.element.setValue(value);
                Profiler.getInstance().endRegion();
                return;
            }

        // add to front of chain
        Link<Entry<K,V>> tmp = new Link<>(new Entry<>(key, value));
        tmp.next = head;
        head = tmp;
        size++;
        Profiler.getInstance().endRegion();
    }

    @Override
    public V get(K key) {
        Profiler.getInstance().startRegion("get(k,v)");
        // try replacing an existing value
        for(Link<Entry<K,V>> current = head; current != null; current = current.next)
            if(current.element.getKey().equals(key))
            {Profiler.getInstance().endRegion();
                return current.element.getValue();}

        Profiler.getInstance().endRegion();
        return null;
    }

    @Override
    public V remove(K key) {
        Profiler.getInstance().startRegion("remove(k)");
        Link<Entry<K,V>> previous = null;
        for(Link<Entry<K,V>> current = head; current != null; current = current.next) {
            if (current.element.getKey().equals(key)) {
                V tmp = current.element.getValue();
                if (previous == null)
                    head = head.next;
                else
                    previous.next = previous.next.next;
                return tmp;
            }
            previous = current;
        }
        Profiler.getInstance().endRegion();
        return null;
    }

    @Override
    public void clear() {
        head = null;
    }

    @Override
    public boolean containsKey(K key) {
        // try replacing an existing value
        Profiler.getInstance().startRegion("containsKey(k)");
        for(Link<Entry<K,V>> current = head; current != null; current = current.next)
            if(current.element.getKey().equals(key)){
                Profiler.getInstance().endRegion();
                return true;
            }
        Profiler.getInstance().endRegion();
        return false;
    }

    @Override
    public List<K> keys() {
        Profiler.getInstance().startRegion("keys()");
        List<K> keys = new ArrayList<>();
        for(Link<Entry<K,V>> current = head; current != null; current = current.next)
            keys.add(current.element.getKey());
        Profiler.getInstance().endRegion();
        return keys;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void reset() {
        current = head;
    }

    @Override
    public Entry<K, V> next() {
        Entry<K,V> tmp = current.element;
        current = current.next;
        return tmp;
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

}
