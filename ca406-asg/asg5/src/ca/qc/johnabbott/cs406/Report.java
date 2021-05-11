/*
 * Copyright (c) 2019 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406;

import ca.qc.johnabbott.cs406.profiler.Region;
import ca.qc.johnabbott.cs406.profiler.Section;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
  A reporiting utility class for a simple profiler.
  @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public class Report {

    public static final String ROW_FORMAT = "| %-30s | %9d | %10.2f\u00B5s | %6.2f%% %-30s |\n";

    private Report() {
    }

    /**
     * Print sections.
     * @param sections A list of sections to print.
     */
    public static void printAllSections(List<Section> sections) {
        for(Section section : sections)
            printSection(section);
    }

    private static void printHeader(Section section) {
        System.out.println(section.getLabel());
        System.out.println(
                "+--------------------------------+-----------+--------------+----------------------------------------+\n" +
                "| Region                         | Run Count | Total Time   | Percent of Section                     |\n" +
                "|--------------------------------+-----------+--------------+----------------------------------------|"
        );
    }
    private static void printFooter() {
        System.out.println(
                "+--------------------------------+-----------+--------------+----------------------------------------+"
        );
        System.out.println();
    }

    private static void printSection(Section section) {

        printHeader(section);

        Map<String, Region> regions = section.getRegions();

        List<String> labels = new ArrayList<>();
        for (String label : regions.keySet()) {
            if (!label.equals(Section.TOTAL))
                labels.add(label);
        }
        labels.add(Section.TOTAL);

        for (String label : labels) {
            Region region = regions.get(label);

            double elapsedTimeInMicroSeconds = (double) region.getElapsedTime() / 1000.0;

            StringBuilder bar = new StringBuilder();
            int pct = (int) (30.0 * region.getPercentOfSection());
            for (int i = 0; i < pct; i++)
                bar.append('\u25A0');

            System.out.format(ROW_FORMAT, label, region.getRunCount(), elapsedTimeInMicroSeconds, region.getPercentOfSection() * 100, bar);
        }

        printFooter();

    }


}
