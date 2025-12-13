****************
* CircuitTracer
* CS221 - 2
* December 7, 2025
* Chelsea Ma
**************** 

OVERVIEW:

 CircuitTracer is a path-finding application that searches for the shortest routes connecting two components on a circuit board. The program will read board layouts form input files, use either stack-based or queue-based search algorithms to find all optimal paths, and display the results in either the console or through a graphical user interface.


INCLUDED FILES:
 
 * CircuitBoard.java - Represents a 2D circuit board layout read from an input file and will validate the board format and track component positions.
 * CircuitTracer.java - The main driver class that parses command-line arguments, run the algorithms, and outputs the results.
 * CircuitTracerGUI.java - Graphical user interface that displays circuit boards and solution paths with highlight interactions
 * Storage.java - Wrapper class providing unified interface for stack and queue data structures.
 * TraceState.java - Represents search state containing the current path configuration during the search process
 * InvalidFileFormatException.java - Custom exception for file parsing errors.
 * OccupiedPositionException.java - Custom exception for invalid trace placement attempts.
 * README - this file

ANALYSIS:

 i. How does the choice of Storage configuration (stack vs queue) affect the sequence in which paths are explored in the search algorithm? (This requires more than a "stacks are LIFOs and queues are FIFOs" answer.)
 Stack Storage (Depth-First Search)
 Stack explores paths by searching deep in one direction before backtracking. Starting at '1' at (0,0), the algorithm can move right to (0,1) or down to (1,0). When a state generates multiple valid next positions, the stack will add them all, but immediately explores the most recently added one. In the test board, the stack may go down from start and complete a path along the bottom before exploring the path along the top.
 Queue Storage (Breadth-First Search)
 Queue explores paths by looking at all positions at length n before any path of length n+1. States will be processed in the order they're added (first-in, first-out), which creates layers of exploration. Multiple solutions will be found simultaneously. When a state generates its neighbors, they're added to the back of the queue, which ensures that all "same-distance" states are processed before moving to the next layer. In the test board, the queue explores all length-1 paths, then all length-2 paths, then all length-3 paths, and so on, creating layer after layer that expands outwards from the start.

 ii. Is the total number of search states (possible paths) affected by the choice of stack or queue?
 Both data structures explore the same set of states. The order of processing is what's different. The total number of states is determined by the board layout, how many valid paths exist from start to goal, and not revisiting positions within a path. Both stack and queue will generate the neighbors for every retrieved state, create new states for every open neighbor, and continue until storage is empty or all solutions are found.
 
 iii. Is using one of the storage structures likely to find a solution in fewer steps than the other? Always?
 Queue would find a solution in fewer steps, but this does not always happen. Queue explores by distance layers and finds the shortest path as soon as it reaches the goal. If only the shortest path is needed, then there is no need to continue. There are times Stack would be faster, for example, if the goal happens to be in the direction queue explores first, there is a chance to find a solution quickly. A possible way for stack to be faster would be if it were to find the fastest path out of luck. When it comes to boards with restricted paths, queue and stack can find solutions at similar speeds.

 iv. Does using either of the storage structures guarantee that the first solution found will be a shortest path?
 When using queue, the first solution found is guarenteed to be the shortest path because queue explores paths in the order of increasing length.
 For stack, the first solution may be longer than other solutions because it explores depth first. This creates a chance of a long path being found before a shorter one.

 v. How is memory use (the maximum number of states in Storage at one time) affected by the choice of underlying structure?
 Stack generally has lower memory. It only stores states along the current path that's being explored and branches. Queue has higher memory as it stores states in the current distance level before moving on to the next, making it grow exponentially.

 vi. What is the Big-Oh runtime order for the search algorithm?
 What does the order reflect? (Maximum size of Storage? Number of board positions? Number of paths explored? Maximum path length? Something else?)
 What is 'n', the single primary input factor that increases the difficulty of the task?
 The Big-Oh runtime order for the search algorithm is O(3^n), where n is the number of board positions. This order reflects the number of possible paths that could be explored in the worst case scenario. The 'n' is the number of board positions, which are rows x columns. It's the primary input factor because it defines the size of the space needed to search, determines the maximum possible path length at most n positions, bounds how many states can exist, and, if there are larger boards, it means more positions to explore and potentially longer paths.


COMPILING AND RUNNING:

 From the directory that contains all the source files, compile the driver class and all dependencies with the command:
 $ javac CircuitTracer.java

 Run the compiled class with the command:
 $ java CircuitTracer [storage] [display] [filename]

 [storage] will be the algorithms used, either -s for storage or -q for queue.
 [display] will be either -c for console or -g for the GUI.
 [filename] will be the input file that contains a circuit board.

 The console output will display all the shortest path solutions. The GUI will open an interactive window where the user can select different solutions that will be highlighted on the board.


PROGRAM DESIGN AND IMPORTANT CONCEPTS:

 CircuitBoard parses the input files and validates board formats. It stores the 2D character array that represents the board. The starting and ending positions, represented as '1' and '2', are tracked, then it provides methods to check if the positions are open. The paths will be marked and traced. This code will also validate that there is exactly one start and one ending position exist in the board.

 CircuitTracer validates command-line arguments. It will instantiate appropriate storage structure, either stack or queue. This file implements the main search algorithm, in which it will initialize storage with states adjacent to the starting component '1', repeatedly retrieve states and generate valid neighbors, track all the shortest path solutions found, and finally output the results in the console or GUI. It will handle FileNotFoundExceptions and InvalidFileFormatExceptions.

 In TraceState, it represents a single search state: a board with a partial path traced. Path history is maintained as a list of points and it generates new states by extending the path to valid neighboring positions. Provided are methods to check if the current state is a solution, and uses copy-on-write pattern to avoid modifying the board.

 Storage allows the algorithms stack and queue to work identically regardless of underlying structure. It abstracts stack and queue implementation behind a common interface, and provides store(), retrieve(), isEmpty(), and size() methods. Storage also utilizes Java's stack and LinkedList implementations for queue.

 The GUI creates an interface with a menu bar and grid that displays the [filename] circuit board. It will list all solution paths in selectable JList and highlights the selected paths with a red 'T' character. The GUI also provides a File>Quit and Help>About options that can quit the program and display a pop-up that states the author of the GUI.

 The search algorithm explores all possible paths from start '1' to goal '2', in which it will begin with positions adjacent ot the start component. While the storage is not empty, it will retrieve the next state and continue to generate new states for every valid neighboring positions that aren't blocked or already in the path. If the path ends adjacent to the goal component or it's found the goal, it will be added to solutions and return all solutions with the shortest path length. The storage abstraction will allow clean separation between the search algorithms and data structure choices. This will make the code more maintainable, and the same algorithm will work for both stack and queue. TraceState prevents bugs from accidental state corruption, but will increase memory usage.
 An improvement can be adding a way to stop exploring longer paths than the ones that are currently the best to shorten exploration time.


TESTING:

 Testing included three different states: unit testing of CircuitBoard and CircuitTracer, integrating testing of the search algorithm, and system testing. 

 This project was provided with multiple valid and invalid circuit boards that were used to see if the printed output would be correct. During testing of CircuitBoard, using the given input files helped check if the board dimensions and character parsing was correct. It would confirm if the starting and ending points were identified correctly, and tested edge cases. The invalid input testing was a way to check if the approprate exceptions would be thrown if there were multiple starting or ending points, misssing starting or ending points, mismatched rows and columns, extra values, decimal values, and non-existent files.

 Each command-line testing involved multiple valid combinations, -s -c, -s -g, -q -c, -q -g, to print the solutions of the input files. Invalid storage and display options were tested to confirm that it displayed the correct message. GUI testing was done by the -g flag. Once the GUI opened, the solution selection and highlighting was tested to see if a valid solution was highlighted for the specific storage and input file. The tabs File>Quit exited out of the JPanel cleanly and Help>About displayed the correct information. 


DISCUSSION:
 
 I feel the most challenging part of this project would be the file validation in the CircuitBoard constructor. There were many edge cases that required specific exception handling, and I had to review the Scanner's hasNextInt() and hasNext() methods. The analysis questions that were required to be answered before coding helped me understand the key differences between stack and queue search. Knowing the differences explained why one would use more memory than the other, but it will find the shortest path first. 
 
 I think I still struggle a lot with visualizing the code and boards, and knowing what to do next for each method and search behavior. Data structures in this project demonstrated how stack and queue can affect algorithm behavior. To be honest, I think I'm still extremely rusty with data structures. It's a concept that's hard for me to grasp, but writing and drawing the boards helped me visualize how and where paths need to go.

 A real challenge would be the GUI implementation. Truthfully, I haven't done any GUI implementations in months, so I had to review past CS121 notes and projects to be reminded of how it's supposed to be coded. Figuring out the board layout, selection listeners for interactive highlighting, and JOptionPanes prove to be quite a challenge, especially when I wanted to match it as close to the provided example screenshots of what it's supposed to look like and do. 

 The most trickiest part of the GUI was managing the highlighting state and ensuring that the board resets to the original state before showing a new solution. I had to store the original characters and reset from that baseline rather than attempting to go back on the highlighting for the next solution.
 
 
EXTRA CREDIT:

 GUI Implementation: There is an implemented fully functional GUI for CircuitTracer. It displays a grid of circuit board with appropriate visual styling, and the user will be able to select multiple solutions and view highlighted paths that show a red 'T' character from start to end. The GUI can be used using the -g command-line flag:
 $ java CircuitTracer [storage] -g [filename]
