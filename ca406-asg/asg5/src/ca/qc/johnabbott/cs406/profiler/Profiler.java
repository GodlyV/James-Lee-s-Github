package ca.qc.johnabbott.cs406.profiler;

import java.util.*;

/**
 * A simple profiling class.
 *
 * @author TODO
 */
public class Profiler {

    /*

            Delimits a section or region of the profiling.
             */
    private static class Mark {

        // stores the type of mark
        private enum Type {
            START_REGION, END_REGION, START_SECTION, END_SECTION
        }

        public Type type;
        public long time;
        public String label;

        // Create mark without a label
        public Mark(Type type, long time) {
            this(type, time, null);
        }

        // Create a mark with a label
        public Mark(Type type, long time, String label) {
            this.type = type;
            this.time = time;
            this.label = label;
        }

        @Override
        public String toString() {
            return "Mark{" +
                    "type=" + type +
                    ", time=" + time +
                    ", label='" + label + '\'' +
                    '}';
        }
    }

    // store marks in list
    private List<Mark> marks;

    // use to prevent regions when not wanted/needed.
    private boolean inSection;
    private static Profiler INSTANCE;

    // private constructor for singleton
    private Profiler() {
        // linked list, because append is a constant time operation.
        marks = new LinkedList<>();
        inSection = false;
    }
    public static Profiler getInstance(){
        if (INSTANCE == null){
            INSTANCE = new Profiler();
        }
        return INSTANCE;
    }

    /**
     * Starts a new profiling section.
     * @param label The section label.
     */
    public void startSection(String label) {
        marks.add(new Mark(Mark.Type.START_SECTION, System.nanoTime(), label));
        inSection = true;
    }

    /**
     * Ends a section. Must be paired with a corresponding call to `startSection(..)`.
     */
    public void endSection() {
        marks.add(new Mark(Mark.Type.END_SECTION, System.nanoTime()));
        inSection = false;
    }

    /**
     * Starts a new profiling region.
     * @param label The region label.
     */
    public void startRegion(String label) {
        if(inSection)
            marks.add(new Mark(Mark.Type.START_REGION, System.nanoTime(), label));
    }

    /**
     * Ends a region. Must be pair with a corresponding call to `startRegion(..)`.
     */
    public void endRegion() {
        if(inSection)
            marks.add(new Mark(Mark.Type.END_REGION, System.nanoTime()));
    }

    public List<Section> getSections() {
        Map<String, Region> regions = new HashMap<>();  //map of all regions inside a section
        List<Mark> startRegions = new ArrayList<>();    //list of all start regions in case there are more than one start region before an end region
        List<Mark> startSections = new ArrayList<>();   //list of all the start sections since they contain the label and start time of the section
        List<Section> sections = new ArrayList<>();     //list of sections to be returned

        for(Mark mark: marks){
            if(mark.type == Mark.Type.START_SECTION) {
                startSections.add(mark);
            }
            else if(mark.type == Mark.Type.END_SECTION){
                Mark startSection = startSections.remove(startSections.size()-1); //stores the first start sections of the start sections mark.
                Region section = new Region(1,mark.time-startSection.time); //creates the region out of the section to be put into the hash map
                regions.put(Section.TOTAL,section);    //put the region into the hash map
                Section adder = new Section(startSection.label,regions,mark.time-startSection.time);      //stores the section and the regions in the section as well as the time into this section
                sections.add(adder);          //stores the section created into the sections list to return later;
                for(String label : regions.keySet()){   //loops through each region and section and keeps its key
                    Region reg =regions.get(label);     //gets the regions label
                    reg.setPercentOfSection((double)reg.getElapsedTime()/(mark.time-startSection.time));    //calculates the percent of your region or section depending on what mark you are on.
                }
                regions = new HashMap<>();      //make sure to reset your hash map as you are ending the section
            }
            else if(mark.type == Mark.Type.START_REGION){   //starting the region
                startRegions.add(mark);                     //add this mark to the start region list so that it can be used later to compute the times and keep the label.
            }
            else if(mark.type ==Mark.Type.END_REGION){      //End of region
                Mark startRegion = startRegions.remove(startRegions.size()-1);  //get the start region that is aligned with the end region
                if(!regions.containsKey(startRegion.label)){        //If this region does not have a start then create a new one
                    Region reg = new Region(1,mark.time-startRegion.time);  //Create the new region
                    regions.put(startRegion.label,reg);     //adds region to the hash map
                }
                else{
                    Region tmp = new Region(regions.get(startRegion.label).getRunCount()+1,regions.get(startRegion.label).getElapsedTime()+(mark.time-startRegion.time));   //adds a run count to the existing region and adds the time again
                    regions.put(startRegion.label,tmp); //re ads the region back into the hash map and overrides the existing one to new data
                }
            }

        }
        return sections;    //returns the sections list that was created with all the sections inside.
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(Mark mark : marks)
            builder.append(String.format("%d %13s %-30s\n", mark.time, mark.type.toString(), mark.label != null ? mark.label : ""));
        return builder.toString();
    }

  
}
