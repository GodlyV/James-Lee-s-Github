package ca.qc.johnabbott.cs406;

import ca.qc.johnabbott.cs406.generator.Generator;
import ca.qc.johnabbott.cs406.generator.SentenceGenerator;
import ca.qc.johnabbott.cs406.generator.WordGenerator;
import ca.qc.johnabbott.cs406.profiler.Profiler;

import java.util.Random;

import static ca.qc.johnabbott.cs406.Main.insertionSort;


public class Samples {

    public static final Generator<String> STRING_GENERATOR;
    public static final Random RANDOM;
    public static final int SAMPLE_SIZE = 10000;

    static {
        RANDOM = new Random();
        STRING_GENERATOR = new SentenceGenerator(new WordGenerator("foo bar baz qux quux quuz corge grault garply waldo fred plugh xyzzy thud".split(" ")), 10);
    }

    public static void main(String[] args) throws InterruptedException {


        // ================================================================================

        Profiler.getInstance().startSection("Sample Section 1");

        Thread.sleep(1);
        Profiler.getInstance().startRegion("region 1");
        Thread.sleep(2);
        Profiler.getInstance().endRegion();
        Profiler.getInstance().startRegion("region 2");
        Thread.sleep(5);
        Profiler.getInstance().endRegion();

        Profiler.getInstance().endSection();

        // --------------------------------------------------------------------------------

        Profiler.getInstance().startSection("Sample Section 2");

        for(int i=0; i<10; i++) {
            Profiler.getInstance().startRegion("region 1");
            Thread.sleep(1);
            Profiler.getInstance().endRegion();
        }

        Profiler.getInstance().endSection();

        // --------------------------------------------------------------------------------

        Profiler.getInstance().startSection("Sample Section 3");

        String string = "";
        for(int i = 0; i < 1000; i++) {

            Profiler.getInstance().startRegion("random string");
            String s = STRING_GENERATOR.generate(RANDOM);
            Profiler.getInstance().endRegion();

            Profiler.getInstance().startRegion("concat");
            string += s;
            Profiler.getInstance().endRegion();
        }

        Profiler.getInstance().startRegion("console");
        System.out.println(string);
        Profiler.getInstance().endRegion();

        Profiler.getInstance().endSection();

        // --------------------------------------------------------------------------------

        Profiler.getInstance().startSection("Sample Section 4");

        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < 1000; i++) {

            Profiler.getInstance().startRegion("random string");
            String s = STRING_GENERATOR.generate(RANDOM);
            Profiler.getInstance().endRegion();

            Profiler.getInstance().startRegion("append");
            builder.append(s);
            Profiler.getInstance().endRegion();
        }

        Profiler.getInstance().startRegion("console");
        System.out.println(builder.toString());
        Profiler.getInstance().endRegion();

        Profiler.getInstance().endSection();

        // --------------------------------------------------------------------------------

        Report.printAllSections(Profiler.getInstance().getSections());

        Profiler.getInstance().startSection("Sample Section 5");

        Thread.sleep(1);

        Profiler.getInstance().startRegion("region 1");
        Thread.sleep(1);

        Profiler.getInstance().startRegion("region 2");
        Thread.sleep(1);
        Profiler.getInstance().endRegion();

        Profiler.getInstance().startRegion("region 3");
        Thread.sleep(1);
        Profiler.getInstance().endRegion();

        Profiler.getInstance().endRegion(); // end region 1

        Profiler.getInstance().endSection();

        Report.printAllSections(Profiler.getInstance().getSections());




        Profiler.getInstance().startSection("Sample Section 6 - Insertion insertionSort");

        Profiler.getInstance().startRegion("random string");
        String[] arr = new String[1000];
        for(int i = 0; i < 1000; i++)
            arr[i] = STRING_GENERATOR.generate(RANDOM);
        Profiler.getInstance().endRegion();

        Profiler.getInstance().startRegion("sort");
        insertionSort(arr);
        Profiler.getInstance().endRegion();

        Profiler.getInstance().endSection();

        Report.printAllSections(Profiler.getInstance().getSections());




    }


}
