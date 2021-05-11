package ca.qc.johnabbott.cs406.collections;

import java.util.Arrays;

/**
 * TODO
 */
public class SortedSet<T extends Comparable<T>> implements Set<T> {

    private static final int DEFAULT_CAPACITY = 100;

    private T[] elements;
    private int size;

    public SortedSet() {
        this(DEFAULT_CAPACITY);
    }

    public SortedSet(int capacity) {
        this.size = 0;
        this.elements = (T[]) new Comparable[capacity];

    }

    @Override
    public boolean contains(T elem) {
        // elements is sorted, so we can binary search for the element.
        return Arrays.binarySearch(elements, 0, size, elem) >= 0;
    }

    @Override
    public boolean containsAll(Set<T> rhs) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public boolean add(T elem) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public boolean remove(T elem) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public int size() {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public boolean isEmpty() {
        throw new RuntimeException("Not implemented.");
    }

    /**
     * TODO
     * @return
     */
    public T min() {
        throw new RuntimeException("Not implemented.");
    }

    /**
     * TODO
     * @return
     */
    public T max() {
        throw new RuntimeException("Not implemented.");
    }

    /**
     * TODO
     * @param first
     * @param last
     * @return
     */
    public SortedSet<T> subset(T first, T last) {
        throw new RuntimeException("Not implemented.");
    }


    @Override
    public boolean isFull() {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public String toString() {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public void reset() {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public T next() {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public boolean hasNext() {
        throw new RuntimeException("Not implemented.");
    }
}
