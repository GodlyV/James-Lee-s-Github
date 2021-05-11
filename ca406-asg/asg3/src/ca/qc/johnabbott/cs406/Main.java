/*
 * Copyright (c) 2019 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406;

import ca.qc.johnabbott.cs406.generator.Generator;
import ca.qc.johnabbott.cs406.graphics.GraphicalTerrain;
import ca.qc.johnabbott.cs406.search.Search;
import ca.qc.johnabbott.cs406.search.UnknownAlgorithm;
import ca.qc.johnabbott.cs406.terrain.Terrain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * Main
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public class Main {

    public static final String DEFAULT_PROPERTIES = "config.properties";

    public static void main(String[] args) throws IOException, UnknownAlgorithm {

        // if specified use the properties file, otherwise default is used.
        String propertiesFile = DEFAULT_PROPERTIES;
        if(args.length != 0)
            propertiesFile = args[0];

        // load configuration, reporting errors as the happen
        Config config = null;
        try {
            config = new Config(propertiesFile);
        } catch (PropertiesExeception e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }

        if(config.getTerrain().equals("random"))
            runRandom(config);
        else
            runFromFile(config);
    }

    /**
     * Run a random
     * @throws UnknownAlgorithm
     */
    private static void runRandom(Config config) throws UnknownAlgorithm {
        Scanner consoleInput = new Scanner(System.in);

        Generator<Terrain> generator = Terrain.generator(config.getWidth(), config.getHeight(), config.getDensity(), config.getClusters());
        Random random = new Random();

        Search search = config.getSearch();

        boolean again;
        do {

            // load terrain
            Terrain terrain = generator.generate(random);
            System.out.println(terrain);

            // solve terrain
            search.solve(terrain);

            // output solution
            if (search.hasSolution()) {
                terrain.applySolution(search);
                System.out.println(terrain.toString(false));
            } else
                System.out.println("No solution.");

            // animate is setup
            if(config.animate())
                GraphicalTerrain.run(terrain, search, 1200, 800, false);

            // ask the user if they want to continue.
            boolean input;
            do {
                System.out.print("Run again [y/N]? ");
                String line = consoleInput.nextLine().trim().toLowerCase();

                // default is "N"
                if(line.isEmpty())
                    line = "n";

                input = line.matches("^[yn]$"); // valid input is "y" or "n"
                again = line.equals("y");

            } while(!input);

        } while(again);

    }

    /**
     * Run the terrain search from a file.
     * @param config
     * @throws FileNotFoundException
     * @throws UnknownAlgorithm
     */
    private static void runFromFile(Config config) throws FileNotFoundException, UnknownAlgorithm {

        // load terrain
        Terrain terrain = new Terrain(config.getTerrain());
        System.out.println(terrain);

        // solve terrain
        Search search = config.getSearch();
        search.solve(terrain);

        // output solution
        if (search.hasSolution()) {
            terrain.applySolution(search);
            System.out.println(terrain.toString(false));
        }
        else
            System.out.println("No solution.");

        // animate if setup
        if(config.animate())
            GraphicalTerrain.run(terrain, search, 1200, 800, false);

    }


}
