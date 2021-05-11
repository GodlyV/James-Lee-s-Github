//PROGRAMER: JAMES LEE


package ca.qc.johnabbott.cs406.search;

import ca.qc.johnabbott.cs406.collections.Queue;
import ca.qc.johnabbott.cs406.terrain.Direction;
import ca.qc.johnabbott.cs406.terrain.Location;
import ca.qc.johnabbott.cs406.terrain.Terrain;
import ca.qc.johnabbott.cs406.terrain.Token;

public class BFS implements Search{
    private Memory memory;

    // for tracking the "traversable" solution.
    private Location solution;
    private boolean foundSolution;

    // the terrain we're searching in.
    private Terrain terrain;

    @Override
    public void solve(Terrain terrain) {
        this.terrain = terrain;

        // track locations we've been to using our terrain "memory"
        memory = new Memory();

        Location current = terrain.getStart();//know the starting location of your terrain
        Location lookingAt = new Location();//this will allow us to scan ahead to see if the terrain is blocked without making changes to the current

        memory.setColor(current,Color.BLACK);//We are currently in the start point and have checked the starting cell so it turns black

        Queue<Location> queue= new Queue<>();//queue to keep track of what grey locations to check first
        queue.enqueue(current);              //queue the starting item as we have to check that first

        Direction direction = Direction.NONE;//initialize the direction
        while(!queue.isEmpty() && !foundSolution){//while there's something in the queue and we've not found a solution

            current =queue.dequeue();             //dequeue from queue to get the item in which to check with bfs
            if (terrain.getToken(current) == Token.GOAL) {//if our current is our goal then we have to trace back our from directions to make a path to draw it out.
                foundSolution = true;                     //found a solution
                while(!current.equals(terrain.getStart())){//loop though all of our from directions till the location is our start.
                    direction = memory.getFromDirection(current).opposite();//store the opposite of our from direction to direction

                    current = current.get(direction.opposite());// put the current into the token towards the from direction. we're going backwards.

                    memory.setToDirection(current,direction);//set out to direction of our current to the opposite of our from direction which is stored in direction.
                    System.out.println(memory.getToDirection(current));
                }
            }
            memory.setColor(current,Color.BLACK);           //we've gone through the terrain so it's now black

            lookingAt = current;                            //lookingAt is the variable to check further locations in the directions we go to.
            for(Direction next : direction.getClockwise()){ //loop through each of the directions in clockwise fashion
                lookingAt = current.get(next);              //move our lookingAt to the next direction of current. We get the current and not the lookingAt since we want to search all the possible directions of our current token.
                if(!terrain.isBlocked(lookingAt) && memory.getColor(lookingAt) == Color.WHITE){//make sure that the token is an empty token and not blocked
                    memory.setFromDirection(lookingAt,next.opposite());//the direction in which is comes from is the to direction but opposite
                    memory.setColor(lookingAt,Color.GREY);              //set the color to grey now that we've seen it once and will come back to it.
                    queue.enqueue(lookingAt);                           //queue it up so we know to check it later for possible routes after all possible directions have been checked in our current token
                }
                lookingAt = current;                        //reset our looking at location to the current location as we must check every possible direction of the current token.
            }
        }
        System.out.println(memory);
    }

    @Override
    public boolean hasSolution() {
        // normally we would return `foundSolution` but that prevents the app from showing the
        // partially constructed path.
        return foundSolution;
    }

    @Override
    public void reset() {
        // start the traversal of our path at the terrain's start.
        solution = terrain.getStart();
    }

    @Override
    public boolean hasNext() {
        // we're only done when we get to the terrain goal.
        return !solution.equals(terrain.getGoal());
    }

    @Override
    public Direction next() {
        // recall the direction at this location, move to the corresponding location and return it.
        Direction direction = memory.getToDirection(solution);
        solution = solution.get(direction);
        return direction;
    }
}
