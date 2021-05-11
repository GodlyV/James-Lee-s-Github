package com.company;

import java.util.*;

public class Permuter <T> implements Sequences<T, List<T>> {
//References : https://www.geeksforgeeks.org/java-program-to-print-all-permutations-of-a-given-string/

    private int limit;
    private int k;

    Permuter(){
        k=this.k;
    }

    public Permuter(int i) {
        k=i;
    }

    public Set<List<T>> generate(Set<List<T>>builder, T[] data, int index,int limit){
        List permuted = new ArrayList<>();  //Makes a new list that will add will contain the numbers in this permutation to be added to a set
        if (k==0 && data.length > 0){       //to check if were at generateAll or generate with a limiter and a k
            k=data.length;
        }
        for(int i = 0; i<k;i++){            //adds the amount of elements to the list as the k will allow
            permuted.add(data[i]);
        }

            if(index == data.length-1){     //if were at the end of the change and we have a new permuted list
                System.out.println(permuted);
                builder.add(permuted);      //add this finished list to the set
            }
            for(int i = index; i < data.length; i++){
                swap(data,index,i);         //a swap method that swaps the numbers starting from the end of the list to get a different permutation
                generate(builder,data,index+1,limit);   //recursively call the function to re add another list with a swapped list to the set
                if (builder.size()==limit){ // ensure that it hasn't reached the limit
                    return null;
                }
                swap(data,index,i);
            }

        return builder; //return the set of lists
    }

    @Override
    public Set<List<T>> generateAll() {
        int count =0;   //at which number is it focusing on.
        int limit =-1;  //since there's no limit to generate all we set it to -1
        Set<List<T>> yikes = new HashSet<>();   //the set to have all the lists to be added
        T[] data = (T[]) new Integer[] {1, 2, 3, 4};    //the data set to be permuted
        generate(yikes,data,count,limit);       //call to the recursive function to generate the set

        return null;
    }

    @Override
    public Set<List<T>> generateSome(int limit){
        int count=0;    //at which number is it focusing on.
        Set<List<T>> yikes = new HashSet<>();   //the set to have all the lists to be added
        T[] data = (T[]) new Integer[] {1, 2, 3, 4};    //the data set to be permuted
        generate(yikes,data,count,limit);       //call to the recursive function to generate the set
        return null;
    }

    private void swap(T[] data,int i, int j){
        T temp = data[i];   //keeps the ith element in a temp
        data[i]= data[j];   //puts i into j
        data[j]=temp;       //puts j into temp
    }
}