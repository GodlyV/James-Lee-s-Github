/*
 * Copyright (c) 2019 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406;

import ca.qc.johnabbott.cs406.search.Search;
import ca.qc.johnabbott.cs406.search.UnknownAlgorithm;

import java.io.IOException;
import java.util.Properties;

public class Config {

    private Search search;
    private String terrain;
    private boolean animate;
    private int width;
    private int height;
    private double density;
    private int clusters;

    public Config(String propertiesFile) throws PropertiesExeception {

        // extract app config from the .properties file
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(propertiesFile));
        } catch (IOException e) {
            throw new PropertiesExeception(e);
        }

        String algorithmName = properties.getProperty("search");

        if(algorithmName == null)
            throw new PropertiesExeception("Missing search property");

        try {
            search = Search.getAlgorithm(algorithmName);
        } catch (UnknownAlgorithm e) {
            throw new PropertiesExeception(e);
        }

        terrain = properties.getProperty("terrain");

        try {
            animate = Boolean.valueOf(properties.getProperty("animate"));
            width = Integer.parseInt(properties.getProperty("width"));
            height = Integer.parseInt(properties.getProperty("height"));
            density = Double.parseDouble(properties.getProperty("density"));
            clusters = Integer.parseInt(properties.getProperty("clusters"));
        }
        catch (NumberFormatException e) {
            throw new PropertiesExeception(e);
        }

    }

    public Search getSearch() {
        return search;
    }

    public String getTerrain() {
        return terrain;
    }

    public boolean animate() {
        return animate;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getDensity() {
        return density;
    }

    public int getClusters() {
        return clusters;
    }
}
