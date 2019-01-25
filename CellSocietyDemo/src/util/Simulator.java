package util;

import cells.Cell;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// TODO add functionality to know when to end simulation and refactor Main accordingly
/**
 * Class to represent a general Cell Automaton and simulate it
 *
 * @author Jonathan Yu
 */
public class Simulator {

    //grid is always a square
    private Cell[][] grid;
    private int gridSideSize;

    // TODO There has got to be a better way of doing this. (Would also have to refactor Cell)
    /**
     * sample Cell instance variable that represents the type of CA
     */
    private Cell caType;

    // TODO Should these be instance variables or just be passed to methods?
    /**
     * info for populating grid
     * both arrays correspond to each other
      */
    private int[] possibleStates, populationPercs;

    // TODO Better way to pass in cell subclass to access static (instead of instance) method?
    public Simulator(Cell type, int size, int[] popPercs) {
        caType = type;
        possibleStates = type.getStates();
        populationPercs = popPercs;
        gridSideSize = size;
        makeAndPopulateGrid();
    }

    /**
     * Creates the grid and populates it with cells with the given states
     *
     * Will be accessed by the Main class
     */
    private void makeAndPopulateGrid() {
        grid = new Cell[gridSideSize][gridSideSize];

        Random rand = new Random();
        // populate the grid with states given the corresponding population percentages
        // inverted j and i for loops because grid uses (x, y) coordinate system
        for (int j = 0; j < gridSideSize; j++) {
            for (int i = 0; i < gridSideSize; i++) {
                int randNum = rand.nextInt(100);
                // use random number between 0 and 99 to determine state based on population percentages
                int cumulativePerc = 0; // used to update the range of numbers that represent the percentages
                for (int k = 0; k < possibleStates.length; k++) {
                    int currPerc = populationPercs[k];
                    if (randNum >= cumulativePerc && randNum < cumulativePerc + currPerc) {
                        grid[j][i] = caType.newInstance(possibleStates[k]);
                    }
                    cumulativePerc += currPerc;
                }
            }
        }
    }

    /**
     * Iterates through grid and calculates each cell's next state by calling its setNextState function
     * Will be called by the Main class to continue the simulation
     */
    public void step() {
        for (int i = 0; i < gridSideSize; i++) {
            for (int j = 0; j < gridSideSize; j++) {
                grid[i][j].setNextState(getNeighborStates(i, j));
            }
        }
    }

    /**
     * Returns a map of the cell's neighbors' states
     * @param x int, outer array index ('x' coordinate) of the cell in question
     * @param y int, inner array index ('y' coordinate) of the cell in question
     * @return a map where the key is the direction of the neighbor (N, NE, E, SE, S, SW, W, NW) and the value
     * is the neighbor's state (-1, 0, 1, 2) (NOTE: a state of -1 is for a neighbor that does not exist within the grid)
     */
    private Map<String, Integer> getNeighborStates (int x, int y) {
        Map<String, Integer> neighbors = new HashMap<>();

        // TODO The following code is shorter and uses less duplicated code but is a little more complex. Good or bad?
        // coordinates of the neighbors (may be invalid coordinates, will check later)
        int[] north = {x, y - 1};
        int[] northEast = {x + 1, y - 1};
        int[] east = {x + 1, y};
        int[] southEast = {x + 1, y + 1};
        int[] south = {x, y + 1};
        int[] southWest = {x - 1, y + 1};
        int[] west = {x - 1, y};
        int[] northWest = {x - 1, y - 1};
        int[][] coords = {north, northEast, east, southEast, south, southWest, west, northWest};
        // for easy naming in the following for loop
        String[] coordNames = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};

        for (int i = 0; i < coords.length; i++) {
            // try-catch block to account for some neighbors not being in the grid (like with corner and side cells)
            try {
                int xCoord = coords[i][0];
                int yCoord = coords[i][1];
                neighbors.put(coordNames[i], grid[xCoord][yCoord].getCurrState());
            }
            catch (ArrayIndexOutOfBoundsException e) {
                // mark non-existent neighbors' states as -1 (invalid)
                neighbors.put(coordNames[i], -1);
            }
        }
        return neighbors;
    }

    /**
     * Will be called by the Visualizer, hides the grid implementation
     * @return 2d array made up of Cell objects representing the grid
     */
    public Cell[][] getGrid() {
        return grid;
    }

}
