/*
 * Copyright (c) 2019 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.search;

import ca.qc.johnabbott.cs406.generator.Generator;
import ca.qc.johnabbott.cs406.terrain.Direction;
import ca.qc.johnabbott.cs406.terrain.Location;
import ca.qc.johnabbott.cs406.terrain.Terrain;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * A "search" for the goal, choosing a random direction each step (but favouring the previous direction 3/4 of the time.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public class RandomSearch implements Search {

    // records where we've been and what steps we've taken.
    private Memory memory;

    // for tracking the "traversable" solution.
    private Location solution;
    private boolean foundSolution;

    // the terrain we're searching in.
    private Terrain terrain;

    /**
     * Create a new Random search.
     */
    public RandomSearch() {
    }

    @Override
    public void solve(Terrain terrain) {

        this.terrain = terrain;

        // track locations we've been to using our terrain "memory"
        memory = new Memory();

        // setup random direction generator.
        Random random = new Random();
        Generator<Direction> generator = Direction.generator();

        // track the current search location, starting at the terrain start location.
        Location current = terrain.getStart();

        // start in a random direction. We will adjust this accordingly.
        Direction previous = generator.generate(random);

        while(!current.equals(terrain.getGoal())) {

            // find the next direction
            Direction direction = Direction.NONE;
            Location next = current.get(previous);

            // change direction if we can't go in the previous direction, or with a 25% chance of changing direction
            if(random.nextInt(100) < 25 || terrain.isBlocked(next) || memory.getColor(next) != Color.WHITE) {

                // keep track of what we've seen in a set of directions
                Set<Direction> checked = new HashSet<>();

                // check in all directions randomly
                while (checked.size() < 4) {

                    // get a random direction
                    Direction tmp = generator.generate(random);
                    checked.add(tmp);

                    // see if stepping in that direction is possible and do it!
                    next = current.get(tmp);
                    if (!terrain.isBlocked(next) && memory.getColor(next) == Color.WHITE) {
                        previous = direction = tmp;
                        break;
                    }
                }

                // if no direction was found, we are stuck and leave without solution
                // see below on how foundSolution would normally be used.
                if(direction == Direction.NONE) {
                    foundSolution = false;
                    return;
                }

            }
            else
                direction = previous;

            // record the step we've taken to memory to recreate the solution in the later traversal.
            memory.setToDirection(current, direction);

            // step
            current = current.get(direction);

            // record that we've been here
            memory.setColor(current, Color.BLACK);
        }

        // we reached the goal and have a solution.
        // see below on how foundSolution would normally be used.
        foundSolution = true;
    }

    @Override
    public boolean hasSolution() {
        // normally we would return `foundSolution` but that prevents the app from showing the
        // partially constructed path.
        return true;
    }

    @Override
    public void reset() {
        // start the traversal of our path at the terrain's start.
        solution = terrain.getStart();
    }

    @Override
    public Direction next() {
        // recall the direction at this location, move to the corresponding location and return it.
        Direction direction = memory.getToDirection(solution);
        solution = solution.get(direction);
        return direction;
    }

    @Override
    public boolean hasNext() {
        // we're only done when we get to the terrain goal.
        return !solution.equals(terrain.getGoal());
    }
}
