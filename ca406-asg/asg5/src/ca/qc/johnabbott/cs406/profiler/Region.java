package ca.qc.johnabbott.cs406.profiler;

public class Region {
    
    private int runCount;
    private double percentOfSection;
    private long elapsedTime;

    public Region(int runCount,long elapsedTime){
            this.runCount = runCount;
            this.elapsedTime = elapsedTime;
    }
    public int getRunCount() {
        return runCount;
    }

    public void setRunCount(int runCount) {
        this.runCount = runCount;
    }


    public double getPercentOfSection() {
        return percentOfSection;
    }

    public void setPercentOfSection(double percentOfSection) {
        this.percentOfSection = percentOfSection;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}
