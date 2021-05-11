package com.company;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Combiner<T> implements Sequences<T, Set<T>> {

    private int k;
    Combiner(){

    }
    Combiner(int i){
        this.k=i;
    }

    public Set<Set<T>> generate(Set<Set<T>>builder, T[] data, int index, int limit){
        Set combined = new HashSet<>();
        if (k==0 && data.length > 0){
            k=data.length;
        }
        for(int i = 0; i<k;i++){
            combined.add(data[i]);
        }

        if(index == data.length-1){
            System.out.println(combined);
            builder.add(combined);
        }
        for(int i = index; i < data.length; i++){
            swap(data,index,i);
            generate(builder,data,index+1,limit);
            if (builder.size()==limit){
                return null;
            }
            swap(data,index,i);
        }

        return builder;
    }
    @Override
    public Set<Set<T>> generateAll() {
        int count =0;
        int limit =-1;
        Set<Set<T>> yikes = new HashSet<>();
        T[] data = (T[]) new Integer[] {1, 2, 3, 4};
        generate(yikes,data,count,limit);

        return null;
    }
    @Override
    public Set<Set<T>> generateSome(int limit) {
        int count=0;
        Set<Set<T>> yikes = new HashSet<>();
        T[] data = (T[]) new Integer[] {1, 2, 3, 4};
        generate(yikes,data,count,limit);
        return null;
    }

    private void swap(T[] data,int i, int j){
        T temp = data[i];
        data[i]= data[j];
        data[j]=temp;
    }
}
