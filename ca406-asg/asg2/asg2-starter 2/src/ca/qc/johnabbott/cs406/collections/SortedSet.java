package ca.qc.johnabbott.cs406.collections;

import javax.management.RuntimeErrorException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import static java.util.Collections.EMPTY_SET;
import static java.util.Collections.checkedCollection;

/**
 * James Lee
 */
public class SortedSet<T extends Comparable<T>> implements Set<T> {

    private static final int DEFAULT_CAPACITY = 100;

    private T[] elements;
    private int size;
    private int current = 0;
    private boolean DirtyDan= false;
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
        //loop through the entire rhs set and make sure that every element of rhs is in the this. set
        rhs.reset();
        while(rhs.hasNext()) {
            if (!this.contains(rhs.next())){return false;}
        }
        return true;
    }

    @Override
    public boolean add(T elem) {
        //function created to add an element into the set
        if(isFull())throw new FullSetException();

        int binaryReturn = Arrays.binarySearch(this.elements,0,size,elem);//Returns index of where elem should be or where it was found
        int binary = Math.abs(binaryReturn);//absolute to make things easier as binary search returns negative number

        if(binaryReturn<0){ //if the element is not found in the array
            for(int i = size; i> (binary-1);i--){   //loop from the end of array till where the element should be inserted into
                this.elements[i] = this.elements[i-1];  //shift each element up by 1;
            }
            this.elements[binary -1] = elem;    //Insert the element into the array where it is supposed to be.
            size++; //increases the counter for the size the array since we added another element.
            DirtyDan = true;//boolean to ensure that you cannot add during a traversal
            return true;//element was inserted
        }
            return false;//element was not insert as was already inside array
    }

    @Override
    public boolean remove(T elem) {
        int binaryReturn = Arrays.binarySearch(this.elements,0,size,elem);//Returns index of where elem should be or where it was found

        if(binaryReturn >= 0){    //if the item is found
            for (int i = binaryReturn; i<this.elements.length-1;i++){
                this.elements[i] = this.elements[i+1];
            }
            size--;
            return true;//Element was removed successfully
        }
        return false;//element was not found
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (size==0);
    }

    /**
     * TODO
     * @return
     */
    public T min() {
        if(isEmpty())throw new EmptySetException();//make sure that the elements are not empty
        return this.elements[0];                   //since set is sorted the first element should be the minimum
    }

    /**
     * TODO
     * @return
     */
    public T max() {
        if(isFull())throw new FullSetException();//make sure the elements.length is not too full
        if(isEmpty())throw new EmptySetException();//make sure it's also not empty
        return this.elements[size-1];               //return the last element as the set is sorted
    }

    /**
     * TODO
     * @param first
     * @param last
     * @return
     */
    private T first;    //initialize the first index to create the substring
    private T last;     //initialize the last index to finish the substring

    public SortedSet<T> subset(T first, T last) {

        int posOfFirst = Arrays.binarySearch(this.elements,0,size,first);//Returns index of where elem should be or where it was found
        int posOfLast = Arrays.binarySearch(this.elements,0,size,last);//Returns index of where elem should be or where it was found
        int subPosition = posOfFirst;//ensure that the subset position starts at where the set's first is and continues on from there

        if(posOfFirst<0) posOfFirst++;//make sure that if first is not in the possible spectrum then add one as binary search returns the index -1
        if(posOfLast<0) posOfLast++;  //same reasoning as the First

        int realPosOfFirst =Math.abs(posOfFirst);//just to make it easier to reason with as it is a positive number in case binary search does not find the element
        int realPosOfLast =  Math.abs(posOfLast);//same as first

        int sizeOfSet = realPosOfLast-realPosOfFirst;//keep track of the number of elements that need to be added to the subset
        SortedSet<T> sub = new SortedSet<>(sizeOfSet);//initialize the subset

        if((Integer)first>(Integer)last){throw new IllegalArgumentException();}
            for (int i = realPosOfFirst; i < realPosOfLast; i++) {  //loop through the initial set and place the first into first index of subset
                sub.add(elements[i]);//make use of our add method to add T's to our subset
            }

        return sub;//make sure to return the set

    }

    @Override
    public boolean isFull() {
        return size == elements.length; //is the set full?
    }

    @Override
    public String toString() {      //stringifies the item and returns a string value of whatever generic you need
        StringBuilder sb = new StringBuilder();     //Constructs a string builder with no characters in it
        sb.append('{');             //beginning of the array is {
        boolean first = true;       // first is for the first element which is a comma, making sure it does not string {,value,value}, but {value,value}
        for(T x : this.elements)    //loop through everything in the elements array
            if (x !=  null) {       //Do not concatenate null values
                if (first)
                    first = false;  //after the first comma is avoided, don't skip anymore
                else
                    sb.append(", ");//add a comma and a space after every element is concatenated
                    sb.append(x);   //append x to the string builder
            }
        sb.append('}');             //concatenate the } at the end to finish the array
        return String.valueOf(sb);  //finally return the stringified value of the sb which is the string builder
    }

    @Override
    public void reset() {
        current = 0;    //reset the current iteration of pass throughs
        DirtyDan=false;
    }

    @Override
    public T next() {
        if(!hasNext()||DirtyDan)throw new TraversalException(); //the dirty bool makes sure that the user cannot add during traversal. Make sure that there is a next element before you go to the next one

        return this.elements[current++];//make sure to increment to the next element
    }

    @Override
    public boolean hasNext() {
        return(this.current<size);//return if there is a next element that is still within bounds
    }
}
