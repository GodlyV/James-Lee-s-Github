/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.collections.list;

import ca.qc.johnabbott.cs406.profiler.Profiler;

/**
 * An implementation of the List interface using unidirectional links, forming a "chain".
 *
 * @author Ian Clement
 */
public class LinkedList<T> implements List<T> {

    /* private inner class for link "chains" */
    private static class Link<T> {
        T element;
        Link<T> next;
        public Link(T element) {
            this.element = element;
        }
        public Link() {}
    }

    /**
     * Move a reference `i` links along the chain. Returns null if `i` exceeds chain length.
     * @param i number of links to move.
     * @return the reference to the link after `i` moves.
     */
    private Link<T> move(int i) {
        // move traversal forward i times.
        Profiler.getInstance().startRegion("move(n)");
        Link<T> current = head;
        for(int j=0; j<i && current != null; j++)
            current = current.next;
        Profiler.getInstance().endRegion();
        return current;
    }

    /* a list is represented with a head "dummy" node to simplify the
     * add/remove operation implementation. */
    private Link<T> head;

    /* a last reference is used to make list append operations
     *     add(x),
     *     add(size(), x)
     * more efficient */
    private Link<T> last;

    // stores the current link in the traversal.
    private Link<T> traversal;

    private int size;

    public LinkedList() {
        // create a "dummy" link, representing an empty list
        head = new Link<T>();
        last = head;
        size = 0;
    }

    @Override
    public void add(T element) {
        // add a new link at the end of the list, put last accordingly
        Profiler.getInstance().startRegion("add(x)");
        last.next = new Link<>(element);
        last = last.next;
        size++;
        Profiler.getInstance().endRegion();
    }

    @Override
    public void add(int position, T element) {
        if(position < 0 || position > size)
            throw new ListBoundsException();
        Profiler.getInstance().startRegion("add(pos,x)");
        
        // when "appending" call the add(x) method
        if(position == size) {
            add(element);
            Profiler.getInstance().endRegion();
            return;
        }

        // move a link reference to the desired position (point to link "position")
        Link<T> current = move(position);

        // place new link between "position" and "position + 1"
        Link<T> tmp = new Link<T>(element);
        tmp.next = current.next;
        current.next = tmp;

        size++;
        Profiler.getInstance().endRegion();
    }

    @Override
    public T remove(int position) {
        if(position < 0 || position >= size)
            throw new ListBoundsException();
        Profiler.getInstance().startRegion("remove(pos)");

        // move a link pointer to the desired position (point to link "position")
        Link<T> current = move(position);

        T element = current.next.element;
        current.next = current.next.next;

        // reset the last if we're removing the last link
        if(current.next == null)
            last = current;
        
        size--;
        Profiler.getInstance().endRegion();

        return element;
    }

    @Override
    public void clear() {
        head.next = null; // remove all the links
        last = head; // empty list
        size = 0;
    }

    @Override
    public T get(int position){

        if(position < 0 || position >= size)
            throw new ListBoundsException();
        Profiler.getInstance().startRegion("get(pos)");

        // move a link pointer to the desired position (point to link "position")
        Link<T> link = move(position + 1);
        Profiler.getInstance().endRegion();

        return link.element;
    }

    @Override
    public T set(int position, T element){
        if(position < 0 || position >= size)
            throw new ListBoundsException();

        Profiler.getInstance().startRegion("set(pos,x)");
        // move a link pointer to the desired position (point to link "position")
        Link<T> current = move(position + 1);
        T ret = current.element;
        current.element = element;
        Profiler.getInstance().endRegion();
        return ret;
    }

    @Override
    public boolean isEmpty(){
        return size() == 0;
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public boolean contains(T element){
        // simple linear search
        Link<T> current = head.next;
        while(current != null) {
            if(current.element.equals(element))
                return true;
            current = current.next;
        }
        Profiler.getInstance().endRegion();
        return false;
    }

    @Override
    public boolean remove(T element) {
        for(Link<T> current = head; current != null; current = current.next)
            if(current.next.element.equals(element)) {
                current.next = current.next.next;
                size--;
                return true;
            }
        Profiler.getInstance().endRegion();
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for(Link<T> current = head.next; current != null; current = current.next) {
            sb.append(current.element);
            if(current.next != null)
                sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    public void reset() {
        traversal = head.next;
    }

    @Override
    public boolean hasNext() {
        return traversal != null;
    }

    @Override
    public T next() {
        T tmp = traversal.element;
        traversal = traversal.next;
        return tmp;
    }

}


     
      
