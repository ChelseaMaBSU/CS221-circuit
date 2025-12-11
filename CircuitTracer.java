
import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to
 * a GUI according to options specified via command-line arguments.
 * 
 * @author mvail
 */
public class CircuitTracer {

	/** Launch the program. 
	 * 
	 * @param args three required arguments:
	 *  first arg: -s for stack or -q for queue
	 *  second arg: -c for console output or -g for GUI output
	 *  third arg: input file name 
	 */
	public static void main(String[] args) {
		new CircuitTracer(args); //create this with args
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private void printUsage() {
		System.out.println("Usage: java CircuitTracer storageChoice displayChoice inputFile ");
		System.out.println("\twhere storageChoice is either -s for a stack or -q for a queue,");
		System.out.println("\tdisplayChoice is either -c for console-only output or -g for GUI output,");
		System.out.println("\tand inputFile is the name of a file containing a layout to complete.");
	}
	
	/** 
	 * Set up the CircuitBoard and all other components based on command
	 * line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 */
	public CircuitTracer(String[] args) {
		//validate number of command-line arguments
		if (args.length != 3) {
			printUsage();
			return; //exit the constructor immediately
		}
		//parse and validate storage choice
		Storage<TraceState> stateStore;
		if (args[0].equals("-s")) {
			stateStore = Storage.getStackInstance();
		} else if (args[0].equals("-q")) {
			stateStore = Storage.getQueueInstance();
		} else {
			printUsage();
			return;
		}

		//parse and validate display choice
		boolean consoleMode = false;
		if (args[1].equals("-c")) {
			consoleMode = true;
		} else if (args[1].equals("-g")) {
			System.out.println("GUI output not yet implemented.");
		} else {
			printUsage();
			return;
		}

		//get filename
		String filename = args[2];

		//read circuit board
		CircuitBoard board;
		try {
			board = new CircuitBoard(filename);
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + filename);
			System.out.println(e.toString());
			return;
		} catch (InvalidFileFormatException e) {
			System.out.println("Invalid file format: " + filename);
			System.out.println(e.toString());
			return;
		}

		//run search for best paths
		ArrayList<TraceState> bestPaths = new ArrayList<>();

		//initialize with starting positions adjacent to component '1'
		Point start = board.getStartingPoint();
		int startRow = start.x;
		int startCol = start.y;

		//check all four adjacent positions
		int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
		for (int[] dir : directions) {
			int newRow = startRow + dir[0];
			int newCol = startCol + dir[1];

			if (board.isOpen(newRow, newCol)) {
				stateStore.store(new TraceState(board, newRow, newCol));
			}
		}

		//search for best paths
		while (!stateStore.isEmpty()) {
			TraceState currentState = stateStore.retrieve();

			if (currentState.isSolution()) {
				//solution found
				if (bestPaths.isEmpty() || currentState.pathLength() == bestPaths.get(0).pathLength()) {
					//first solution or equal length
					bestPaths.add(currentState);
				} else if (currentState.pathLength() < bestPaths.get(0).pathLength()) {
					//shorter path found
					bestPaths.clear();
					bestPaths.add(currentState);
				}
			} else {
				//generate next states
				int currentRow = currentState.getRow();
				int currentCol = currentState.getCol();

				//try all four possible directions
				for (int[] dir : directions) {
					int newRow = currentRow + dir[0];
					int newCol = currentCol + dir[1];

					if (currentState.isOpen(newRow,newCol)) {
						stateStore.store(new TraceState(currentState,newRow,newCol));
					}
				}
			}
		}

		//output results
		if (consoleMode) {
			for (TraceState solution : bestPaths) {
				System.out.println(solution.toString());
			}
		}
	}

} // class CircuitTracer
