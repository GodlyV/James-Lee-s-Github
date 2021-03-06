/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.search;

import ca.qc.johnabbott.cs406.collections.Traversable;
import ca.qc.johnabbott.cs406.terrain.Direction;
import ca.qc.johnabbott.cs406.terrain.Terrain;

/**
 * <p>Defines the structure of a search algorithm. Includes `Traversable<Direction>` as way of navigating
 * the solution path.</p>
 *
 * <p>The class method `getAlgorithm()` is used to choose the search algorithm at runtime.</p>
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @since 2017-02-07
 */
public interface Search extends Traversable<Direction> {

    /**
     * Create a Search object for the algorithm name provided.
     * @param algorithm
     * @return
     * @throws UnknownAlgorithm
     */
    static Search getAlgorithm(String algorithm) throws UnknownAlgorithm {
        switch (algorithm) {
            case "Random":
                return new RandomSearch();

            case "DFS":
                return new DFS();

            case "BFS":
                return new BFS();
            default:
                throw new UnknownAlgorithm("Unknown search algorithm: " + algorithm);
        }
    }

    /**
     * Search the provided terrain to find a solution path.
     * @param terrain the terrain to search for a solution path.
     */
    void solve(Terrain terrain);

    /**
     * Determine if a solution path was found on the previous call to `solve`.
     * @return True if there is a solution, false otherwise.f
     */
    boolean hasSolution();
}
