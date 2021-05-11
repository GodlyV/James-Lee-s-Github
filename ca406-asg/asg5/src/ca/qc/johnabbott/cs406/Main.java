package ca.qc.johnabbott.cs406;

import ca.qc.johnabbott.cs406.collections.list.ArrayList;
import ca.qc.johnabbott.cs406.collections.list.LinkedList;
import ca.qc.johnabbott.cs406.collections.list.List;
import ca.qc.johnabbott.cs406.collections.map.HashMap;
import ca.qc.johnabbott.cs406.collections.map.Map;
import ca.qc.johnabbott.cs406.collections.map.NaiveMap;
import ca.qc.johnabbott.cs406.generator.Generator;
import ca.qc.johnabbott.cs406.generator.SentenceGenerator;
import ca.qc.johnabbott.cs406.generator.WordGenerator;
import ca.qc.johnabbott.cs406.profiler.Profiler;

import java.util.Random;


public class Main {

    public static final Generator<String> STRING_GENERATOR;
    public static final Random RANDOM;
    public static final int SAMPLE_SIZE = 10000;

    static {
        RANDOM = new Random();
        STRING_GENERATOR = new SentenceGenerator(new WordGenerator("foo bar baz qux quux quuz corge grault garply waldo fred plugh xyzzy thud".split(" ")), 10);
    }

    public static void main(String[] args) throws InterruptedException {

        //Profiler.getInstance().startSection("LinkedList -  Initialize with add(x)");
        //listInitializeWithAppend(new LinkedList<>());
        //Profiler.getInstance().endSection();

        //Profiler.getInstance().startSection("ArrayList -  Initialize with add(x)");
        //listInitializeWithAppend(new ArrayList<>());
        //Profiler.getInstance().endSection();

        //Report.printAllSections(Profiler.getInstance().getSections());

        Profiler.getInstance().startSection("LinkedList -  Initialize with add(x)");
        addToList(new LinkedList<>());
        Profiler.getInstance().endSection();

        Profiler.getInstance().startSection("ArrayList -  Initialize with add(x)");
        addToList(new ArrayList<>());
        Profiler.getInstance().endSection();
        Report.printAllSections(Profiler.getInstance().getSections());

        Profiler.getInstance().startSection("LinkedList -  Initialize with add(pos,x)");
        addToPos(0,new LinkedList<>());
        Profiler.getInstance().endSection();

        Profiler.getInstance().startSection("ArrayList -  Initialize with add(pos,x)");
        addToPos(0,new ArrayList<>());
        Profiler.getInstance().endSection();
        Report.printAllSections(Profiler.getInstance().getSections());

        Profiler.getInstance().startSection("LinkedList -  Initialize with add(middle,x)");
        addToMid(new LinkedList<>());
        Profiler.getInstance().endSection();

        Profiler.getInstance().startSection("ArrayList -  Initialize with add(middle,x)");
        addToMid(new ArrayList<>());
        Profiler.getInstance().endSection();
        Report.printAllSections(Profiler.getInstance().getSections());


        List<String> aL = new ArrayList<>();
        List<String> lL = new LinkedList<>();


        Profiler.getInstance().startSection("LinkedList -  Initialize with addToSize-2(Size-2,x)");
        lL = addToSize2(new LinkedList<>());
        Profiler.getInstance().endSection();


        Profiler.getInstance().startSection("ArrayList -  Initialize with addToSize-2(size-2,x)");
        aL = addToSize2(new ArrayList<>());
        Profiler.getInstance().endSection();
        Report.printAllSections(Profiler.getInstance().getSections());

        Profiler.getInstance().startSection("LinkedList -  Initialize with front to end");
        reverse(addToSize2(lL));
        Profiler.getInstance().endSection();

        Profiler.getInstance().startSection("ArrayList -  Initialize with front to end ");
        reverse(addToSize2(aL));
        Profiler.getInstance().endSection();
        Report.printAllSections(Profiler.getInstance().getSections());

        Profiler.getInstance().startSection("LinkedList -  Initialize with end to front");
        coReverse(addToSize2(lL));
        Profiler.getInstance().endSection();

        Profiler.getInstance().startSection("ArrayList -  Initialize with end to front ");
        coReverse(addToSize2(aL));
        Profiler.getInstance().endSection();
        Report.printAllSections(Profiler.getInstance().getSections());

        Profiler.getInstance().startSection("LinkedList -  Max with Traditional For loop");
        maxValFor(lL);
        Profiler.getInstance().endSection();

        Profiler.getInstance().startSection("ArrayList -  Max with Traditional For loop");
        maxValFor(aL);
        Profiler.getInstance().endSection();
        Report.printAllSections(Profiler.getInstance().getSections());

        //Profiler.getInstance().startSection("LinkedList -  Max with Traversal");
        //maxValTrav(lL);
        //Profiler.getInstance().endSection();

        //Profiler.getInstance().startSection("ArrayList -  Max with Traversal");
        //maxValTrav(aL);
        //Profiler.getInstance().endSection();
        //Report.printAllSections(Profiler.getInstance().getSections());
        Profiler.getInstance().startSection("NaiveMap - Map Construction");
        Map Naive =constructNaive();
        Profiler.getInstance().endSection();

        Profiler.getInstance().startSection("HashMap - Map Construction");
        Map Hash = constructHash();
        Profiler.getInstance().endSection();
        Report.printAllSections(Profiler.getInstance().getSections());

        Profiler.getInstance().startSection("NaiveMap - Retrieve 50%");
        boolean on = true;
        for(int i=0;i<Naive.size()-1;i++){//Go to the naive map the amount of times as the size
            getNaive(Naive,on);           //pass in the boolean on to see if we want to actually get from naive map as it has to be 50%
            if(on){
                on = false;
            }
            else{
                on = true;
            }
        }
        Profiler.getInstance().endSection();

        Profiler.getInstance().startSection("HashMap - Retrieve 50%");
        boolean dirtyDan = true;
        for(int i=0;i<Hash.size()-1;i++){//Go to the Hash map the amount of times as the size
            getHash(Naive,dirtyDan);//pass in the boolean dirtyDan to see if we want to actually get from hash map as it has to be 50%
            if(dirtyDan){
                dirtyDan = false;
            }
            else{
                dirtyDan = true;
            }
        }
        Profiler.getInstance().endSection();
        Report.printAllSections(Profiler.getInstance().getSections());

        System.out.println("Half of Naive Map Size: "+ Naive.size());//what the numbers should be for the amount of times it does the get operation
        System.out.println("Half of Hash Map Size: " + Hash.size());

    }
    public static <T extends Comparable<T>> void insertionSort(T[] arr) {

        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            T key = arr[i];
            int j = i - 1;

            Profiler.getInstance().startRegion("shift");
            while (j >= 0) {
                Profiler.getInstance().startRegion("compare(x,y)");
                int x = arr[j].compareTo(key);
                Profiler.getInstance().endRegion();

                if(x <= 0)
                    break;

                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
            Profiler.getInstance().endRegion();
        }
    }

    public static void addToList(List<String> list){
        for(int i=0; i<SAMPLE_SIZE;i++){
            list.add(0,STRING_GENERATOR.generate(RANDOM));  //add to the lists ad the end of the list
        }
    }

    public static void addToPos(int pos,List<String>list){
        for(int i=0; i<SAMPLE_SIZE;i++)
        list.add(pos,STRING_GENERATOR.generate(RANDOM));//adds to beginning of the list
    }
    public static void addToMid(List<String>List){
        for(int i =0;i<SAMPLE_SIZE;i++) {
            List.add(List.size() / 2, STRING_GENERATOR.generate(RANDOM));//adds to the middle of the lists
        }
    }
    public static List<String> addToSize2(List<String>List){
        int inCase = 0;
        for(int i =0;i<SAMPLE_SIZE;i++){
            if(List.size()-2 <0){
                List.add(inCase,STRING_GENERATOR.generate(RANDOM));//adds to beginning as we do not want to add to a minus value of a list
            }
            else{
                List.add(List.size()-2,STRING_GENERATOR.generate(RANDOM));//adds to the size -2 of the list
            }
        }
        return List;
    }
    public static List<String> reverse(List<String>List){
        for(int i = 0;i<List.size()-1;i++){
            List.add(List.remove(0));//adds the first value to the end
        }
        return List;
    }
    public static List<String> coReverse(List<String>List){
        for(int i = 0;i<List.size()-1;i++){
            List.add(0,List.remove(List.size()-1));//adds the last value to the front
        }
        return List;
    }
    public static String maxValFor(List<String>List){
        String max = List.remove(0);
        for(int i = 0;i<SAMPLE_SIZE;i++){
            String current = List.get(i);
            if(max.compareTo(current)>0){//gets the max value with a for loop
                max = current;
            }
        }
        return max;
    }
    public static String maxValTrav(List<String>List){//gets the max value with traversals
        String max = List.get(0);
        String current = max;
       // List<String> cur = List.
        for(int i=0;i<List.size()-1;i++){
            if(List.next() == null) break;
            current = List.next();
            if(max.compareTo(current)>0){
                max = current;
            }
        }
        return max;
    }

    public static Map constructNaive(){
        Map<String, Integer> regions = new NaiveMap<>();
        for(int i = 0;i<SAMPLE_SIZE;i++){
            regions.put(STRING_GENERATOR.generate(RANDOM),i);//constructs the naive map
        }
        return regions;
    }
    public static Map  constructHash(){
        Map<String,Integer> regions = new HashMap<>();
        for(int i =0;i<SAMPLE_SIZE;i++){
            regions.put(STRING_GENERATOR.generate(RANDOM),i);//constructs the hash map
        }
        return regions;
    }
    public static Object getNaive(Map Naive, boolean on){
        if(on){
            return  Naive.get(STRING_GENERATOR.generate(RANDOM));//gets a value from naive map 50% of the time
        }
        return null;
    }
    public static Object getHash(Map Hash,boolean on){
        if(on){
            return Hash.get(STRING_GENERATOR.generate(RANDOM));//gets a value from the hashmap 50% of the time
        }
        return null;
    }
}
