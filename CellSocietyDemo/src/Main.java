import util.*;
import cells.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

// TODO Are each of the classes organized well?
// TODO Are comments and javadoc written the right way?
/**
 * Main class for running a Cell Automaton
 *
 * @author Jonathan Yu
 */
public class Main {

    // TODO Is an instance variable scanner bad? It probably is.
    // user input
    private final Scanner SC = new Scanner(System.in);
    private final String BAD_ANSWER_TO_Y_OR_N = "Error: Invalid answer (Must be 'y' or 'n')";

    // TODO Should this be a local variable that is returned by readConfig?
    /**
     * data read from config file
     * index 0 is states, index 1 is population percentages
     * Note: Right now, the only subclass of Cell is SpreadingFireCell, which has hardcoded states, so the states array
     * in the value list is not yet used.
      */
    private Map<String, List<int[]>> configInfo;

    private Simulator sim;
    private Visualizer vis;

    // TODO Should user input be in Main or Visualizer?
    /**
     * Get user input and set up the simulator and visualizer
     */
    private void start() {
        // prompt for user input for grid size
        int size;
        System.out.println();
        while (true) {
            // prompt for user input
            System.out.println("What size should the grid side be?");
            try {
                size = Integer.parseInt(SC.nextLine());
            }
            catch (NumberFormatException e) {
                System.out.println("Error: Not an integer");
                continue;
            }
            if (size <= 0) {
                System.out.println("Error: Size must be greater than 0");
            }
            else {
                break;
            }
        }
        // prompt for user input for CA type
        while (true) {
            System.out.println("Which Cell Automaton would you like to run? (Options: " +
                    String.join(", ", configInfo.keySet()) + ")");

            // setup the simulator and visualizer
            if (SC.nextLine().equals("SpreadingFire")) {
                sim = new Simulator(new SpreadingFireCell(0), size, configInfo.get("SpreadingFire").get(1));
                vis = new Visualizer(sim);
                break;
            }
            else {
                System.out.println("Error: Not a valid Cell Automaton");
            }
        }
        System.out.println();
        vis.showDisplayKey();
        System.out.println();
        run();
    }

    /**
     * Reads the config file and loads the data into the configInfo map
     */
    private void readConfig() {
        configInfo = new HashMap<>();
        File f = new File("resources/config.txt");
        try {
            Scanner fsc = new Scanner(f);
            // see config.txt for format
            fsc.nextLine();
            fsc.nextLine();
            fsc.nextLine();
            while(fsc.hasNextLine()) {
                // fill configInfo map (name, states, population percentages)
                String caName = fsc.next();
                int[] states = configStringToIntArr(fsc.next());
                int[] popPercs = configStringToIntArr(fsc.next());
                List<int[]> data = new ArrayList<>();
                data.add(states);
                data.add(popPercs);
                configInfo.put(caName, data);

                //TODO Probably better to do this in each Cell subclass in order to maintain strict encapsulation

                // send miscellaneous data to the Cell subclasses
                String[] misc = fsc.next().split(",");
                sendMisc(caName, misc);
                fsc.nextLine();
            }
        }
        catch (FileNotFoundException e) {
            // only for debugging, will never get here in release because config.txt will always exist
            System.out.println("ERROR: config.txt not set up properly. Check code.");
            System.exit(-1);
        }
    }

    /**
     * Helper function that takes a string read from config file and converts to corresponding int array
     * @param configString string representing some numerical data read from config file
     * @returns int array version of the configString
     */
    private int[] configStringToIntArr (String configString) {
        String[] stringArr = configString.split(",");
        int[] intArr = new int[stringArr.length];
        for (int i = 0; i < stringArr.length; i++) {
            intArr[i] = Integer.parseInt(stringArr[i]);
        }
        return intArr;
    }

    /**
     * Sends the misc. data from the config file to their respective class for them to handle
     * Right now, there is only one subclass of Cell, but more can be added easily.
     * @param caName string representing the name of the corresponding class that will be sent the data
     * @param misc string array representing the misc. data read from the config file
     */
    private void sendMisc(String caName, String[] misc) {
        if (caName.equals("SpreadingFire")) {
            SpreadingFireCell.loadMisc(misc);
        }
    }

    // TODO For continuous running, use Threads to "slow down" display?
    /**
     * Run and display the simulator in one step at a time or 10 steps at a time (continuing based off of user input)
     * Also ends the simulator if applicable
     */
    private void run() {
        // determine run interval (1 or 10 steps at a time)
        System.out.println("Would you like to run the simulator one step at a time (if not, will run 10 steps at a " +
                "time)? (y/n)");
        String answer = SC.nextLine();
        int stepsAtATime = 1;
        if (answer.equals("n")) {
            stepsAtATime = 10;
        }
        else if (!answer.equals("y")) {
            System.out.println(BAD_ANSWER_TO_Y_OR_N);
            run(); // recursive call here to not have to make nested while loops
        }
        System.out.println();
        while (true) {
            for (int i = 0; i < stepsAtATime; i++) {
                boolean ending = sim.step();
                vis.display();
                if (ending) {
                    System.out.println("Simulation has ended (no further changes will occur).");
                    restart();
                    return; // if user does not want to restart need to end program still
                }
                System.out.println();
            }
            System.out.println("Would you like to continue? (y/n)");
            String answer2 = SC.nextLine();
            if (answer2.equals("n")) {
                System.out.println("EXITING");
                return;
            }
            else if (!answer2.equals("y")) {
                System.out.println(BAD_ANSWER_TO_Y_OR_N);
            }
        }
    }

    private void restart() {
        System.out.println("EXITING");
        System.out.println();
        while (true) {
            System.out.println("Would you like to restart (y/n)?");
            String answer = SC.nextLine();
            if (answer.equals("y")) {
                start();
            }
            else if (answer.equals("n")) {
                System.out.println();
                System.out.println("Goodbye");
                return;
            }
            else {
                System.out.println(BAD_ANSWER_TO_Y_OR_N);
            }
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.readConfig();
        System.out.println();
        System.out.println("Welcome to Cell Society");
        main.start();
    }
}
