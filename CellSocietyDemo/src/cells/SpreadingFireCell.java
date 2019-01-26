package cells;

import java.util.Map;
import java.util.Random;

/**
 * Class to represent a cell in the Spreading Fire Cell Automaton
 *
 * @author Jonathan Yu
 */
public class SpreadingFireCell extends Cell {

    // TODO Are private static variables ok?
    /**
     * the probability that a tree next to a burning tree will also catch on fire
      */
    private static double probCatch;

    public SpreadingFireCell(int curr) {
        super(curr, "Display Key: _(empty)  T(tree)  W(burning)");
        states = new int[]{0, 1, 2}; // states are fixed in SpreadingFire
    }

    // TODO Is this implementation ok?
    /**
     * Take misc. info read from the config file by the Main class and set probCatch accordingly
     * @param misc string array representing misc. info for the SpreadingFireCell class from the config file
     */
    public static void loadMisc(String[] misc) {
        probCatch = Double.parseDouble(misc[0]);
    }

    // TODO Need comment for overriden function?
    /**
     * Calculates and sets the next state based of off current state and neighbors' states (does not perform update)
     * Also helps Simulator determine if it should end
     * Will be called by Simulator when performing a step
     * @param neighbors map where the key is the direction of the neighbor (N, NE, E, SE, S, SW, W, NW) and the value is
     * the neighbor's state (-1, 0, 1, 2) (NOTE: a state of -1 is for a neighbor that does not exist within the grid)
     * @returns true if this cell is blocking the end of the simulator, false otherwise. A SpreadingFireCell that blocks
     * ending is only one that is burning (if no trees are burning, no more changes will occur to the grid)
     */
    @Override
    public boolean setNextState(Map<String, Integer> neighbors) {
        // cell states (NOTE: empty can be no tree or burned down tree)
        final int EMPTY =  0;
        final int TREE = 1;
        final int BURNING = 2;

        // only look at the cardinal directions
        neighbors.remove("NE");
        neighbors.remove("SE");
        neighbors.remove("SW");
        neighbors.remove("NW");

        boolean blockingEnding = false;

        // if a tree is next to a burning tree, it will catch fire with a probability of probCatch
        if (currState == TREE && neighbors.containsValue(BURNING)) {
            // TODO Better to do this conversion or make probCatch an integer?
            int rand = new Random().nextInt(100);
            if (rand < probCatch * 100) {
                nextState = BURNING;
                blockingEnding = true;
            }

        }
        // if tree is burning, it will burn down and leave an empty cell after 1 step
        else if (currState == BURNING) {
            nextState = EMPTY;
        }
        // if a cell is empty or is a tree surrounded by no burning trees
        else {
            nextState = currState;
        }
        return blockingEnding;
    }

    /**
     * Updates the cell's state (current state becomes next state) and returns the updated state in display form (_ T X)
     * Will be called by Visualizer (Visualizer updates in order to save an iteration through the grid, see Visualizer)
     * @returns String representing the updated state in display form (_ T X)
     */
    @Override
    public String updateAndGetState() {
        currState = nextState;
        if (currState == 0) {
            return "_";
        }
        else if (currState == 1) {
            return "T";
        }
        else {
            return "W";
        }
    }

    /**
     * Returns a new instance of a SpreadingFireCell for use by the Simulator to populate the grid
     * @param curr int representing the current state
     * @returns new instance of SpreadingFireCell
     */
    @Override
    public SpreadingFireCell newInstance(int curr) {
        return new SpreadingFireCell(curr);
    }


}
