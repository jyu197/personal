package cells;

import java.util.Map;

public abstract class Cell {

    private final String DISPLAY_KEY;

    /**
     * store states in a way that a cell's update does not affect other cells' updates
      */
    protected int[] states;
    protected int currState;
    protected int nextState;

    public Cell(int curr, String displayKey) {
        currState = curr;
        // TODO Is having this weird constructor intialization worth saving us from writing an extra method in .Visualizer?
        // initialize this way because .Visualizer updates cells to the nextState and this allows to write 1 display
        // method instead of 2 (one to display initial grid) (see .Visualizer)
        nextState = curr;
        DISPLAY_KEY = displayKey;
    }

    /**
     * Calculates and sets the next state based of off current state and neighbors' states (does not perform update)
     * Also helps Simulator determine if it should end
     * Will be called by Simulator when performing a step
     * @param neighbors map where the key is the direction of the neighbor (N, NE, E, SE, S, SW, W, NW) and the value is
     * the neighbor's state (-1, 0, 1, 2) (NOTE: a state of -1 is for a neighbor that does not exist within the grid)
     * @returns true if this cell is blocking the end of the simulator, false otherwise
     */
    public abstract boolean setNextState(Map<String, Integer> neighbors);

    /**
     * Returns the possible states of this cell
     * Will be called by Simulator when getting possible states to populate the grid with
     *
     * @returns array of ints representing the possible states
     */
    public int[] getStates() {
        return states;
    }

    /**
     * Returns the cell's current state
     * Will be called by Simulator when getting neighbor states
     *
     * @returns int representing the current state
     */
    public int getCurrState() {
        return currState;
    }

    /**
     * Returns the display key for the type of cell
     * @returns String representing the display key
     */
    public String getDisplayKey() {
        return DISPLAY_KEY;
    }

    /**
     * Updates the cell's state (current state becomes next state) and returns the updated state in display form
     * Will be called by .Visualizer (.Visualizer updates in order to save an iteration through the grid, see .Visualizer)
     * @returns String representing the updated state in display form
     */
    public abstract String updateAndGetState();

    /**
     * Returns a new instance of a Cell subclass for use by the .Simulator to populate the grid
     * @param curr int representing the current state
     * @returns new instance of Cell subclass (when overridden)
     */
    public abstract Cell newInstance(int curr);
}
