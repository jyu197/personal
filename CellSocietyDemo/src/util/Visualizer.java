package util;

import cells.Cell;

/**
 * Class for visualizing a Cell Automaton
 *
 * @author Jonathan Yu
 */
public class Visualizer {

    private Simulator simulator;

    public Visualizer(Simulator sim) {
        simulator = sim;
    }
    // TODO Is saving an iteration worth it? This implementation puts Visualizer in charge of actually updating the grid
    // TODO which messes up the encapsulation. Also, you must run display at least once to simulate, otherwise the grid
    // TODO Does the reason for updating prior to displaying the initial grid (lines 3-4 of the function description) make sense?
    /**
     * Updates the grid and displays the result
     * Performs the update to avoid having to iterate through the grid another time just for updating
     * Updates before displaying the initial grid because Cells are initialized with only a next state. This saves
     * having to make a separate function just for displaying the initial grid.
     */
    public void display() {
        Cell[][] grid = simulator.getGrid();
        // column because grid uses (x, y) coordinate system
        for (Cell[] column : grid) {
            for (Cell c : column) {
                System.out.print(c.updateAndGetState() + " ");
            }
            System.out.println();
        }
    }
}
