package ca.qc.johnabbott.cs406.search;

import ca.qc.johnabbott.cs406.collections.Queue;
import ca.qc.johnabbott.cs406.collections.Stack;
import ca.qc.johnabbott.cs406.generator.Generator;
import ca.qc.johnabbott.cs406.terrain.Direction;
import ca.qc.johnabbott.cs406.terrain.Location;
import ca.qc.johnabbott.cs406.terrain.Terrain;
import ca.qc.johnabbott.cs406.terrain.Token;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class DFS implements Search{

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
        Location current = terrain.getStart();  //know the starting location of your terrain
        Location tmp = new Location();          //this will allow us to scan ahead to see if the terrain is blocked without making changes to the current
        Stack<Location> stack = new Stack<>(10000);//stack to keep track of every move to the goal
        boolean foundNextNode;                  //If the next node has been found. Keep track in case we've reached a dead end to be able to backtrack after
        memory.setColor(current,Color.BLACK);   //we've visited the current token so make sure it's blacked out to show that we've visited it//
        stack.push(current);                    //put the token visited into the stack. We can pop it later if found next node is empty and we need to backtrack for a bit

        while(!current.equals(terrain.getGoal())) { //while the token we're checking is not on the goal

            foundNextNode = false;                  //haven't found the next node
            // find the next direction
            tmp = current;                       //tmp is used to scan ahead but we must first initialize it to the current token.

            Direction direction = Direction.NONE;//initialize direction
            for (Direction next : direction.getClockwise()) {//loop through each of the directions in clockwise fashion
                tmp = tmp.get(next);                         //move tmp to the next direction
                memory.setToDirection(current,next);         //use memory to set the direction in which you went. This will allow us to use traversal later to be able to display the route used.

                if (!terrain.isBlocked(tmp) && memory.getColor(tmp) == Color.WHITE) {//make sure that the token is an empty token and not blocked
                    current = tmp;                           //we can set up our current now since we know that the token is empty
                    memory.setColor(current, Color.BLACK);   //set the color to black now that we have gone into it
                    memory.setFromDirection(current,next.opposite()); //the direction in which is comes from is the to direction but opposite
                    stack.push(current);                              //push the current location into the stack since we are in it.
                    direction = next;                                 //set the direction to the current direction.
                    foundNextNode = true;                             //we found the next node.
                }

                if (foundNextNode) break;                             //skip to the next location in stack as it is depth first search. we have found the node so we go to the next one right away

                tmp = current;                               //reset tmp if we didn't cannot find the next direction for the tmp location
            }
            if(stack.isEmpty()){                             //if stack is empty then you cannot find a solution.
                break;
            }
            else if (!foundNextNode) {                      //pop the stack to get the next node to check from since you backtrack.
                current = stack.pop();
            }
            if (terrain.getToken(current) == Token.GOAL) { //set found solution to true if the current token is the goal.
                foundSolution = true;
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
        solution = terrain.getStart();    }

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
