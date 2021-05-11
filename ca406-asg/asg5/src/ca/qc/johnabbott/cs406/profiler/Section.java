package ca.qc.johnabbott.cs406.profiler;

import java.util.Map;

public class Section {
    public Section(String label,Map<String, Region> regions,long time){
        this.label = label;
        this.regions = regions;
        this.time = time;

    }
    public static final String TOTAL = "TOTAL";
    public String label;

    public String getLabel() {
        return this.label;
    }
    public long time;
    public long getTime(){
        return this.time;
    }

    public Map<String, Region> regions;
    public Map<String, Region> getRegions() {
        return this.regions;
    }
}
