****************
* CircuitTracer
* CS221 - 2
* December 7, 2025
* Chelsea Ma
**************** 

OVERVIEW:

 Concisely explain what the program does. If this exceeds a couple
 of sentences, you're going too far. The details go in other
 sections.


INCLUDED FILES:

 List the files required for the project with a brief
 explanation of why each is included.

 e.g.
 * CircuitBoard.java
 * CircuitTracer.java
 * Storage.java
 * TraceState.java
 * InvalidFileFormatException.java
 * OccupiedPositionException.java
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

 Give the command for compiling the program, the command
 for running the program, and any usage instructions the
 user needs.
 
 These are command-line instructions for a system like onyx.
 They have nothing to do with Eclipse or any other IDE. They
 must be specific - assume the user has Java installed, but
 has no idea how to compile or run a Java program from the
 command-line.
 
 e.g.
 From the directory containing all source files, compile the
 driver class (and all dependencies) with the command:
 $ javac Class1.java

 Run the compiled class file with the command:
 $ java Class1

 Console output will give the results after the program finishes.


PROGRAM DESIGN AND IMPORTANT CONCEPTS:

 This is the sort of information someone who really wants to
 understand your program - possibly to make future enhancements -
 would want to know.

 Explain the main concepts and organization of your program so that
 the reader can understand how your program works. This is not a repeat
 of javadoc comments or an exhaustive listing of all methods, but an
 explanation of the critical algorithms and object interactions that make
 up the program.

 Explain the main responsibilities of the classes and interfaces that make
 up the program. Explain how the classes work together to achieve the program
 goals. If there are critical algorithms that a user should understand, 
 explain them as well.
 
 If you were responsible for designing the program's classes and choosing
 how they work together, why did you design the program this way? What, if 
 anything, could be improved? 

TESTING:

 How did you test your program to be sure it works and meets all of the
 requirements? What was the testing strategy? What kinds of tests were run?
 Can your program handle bad input? Is your program  idiot-proof? How do you 
 know? What are the known issues / bugs remaining in your program?


DISCUSSION:
 
 Discuss the issues you encountered during programming (development)
 and testing. What problems did you have? What did you have to research
 and learn on your own? What kinds of errors did you get? How did you 
 fix them?
 
 What parts of the project did you find challenging? Is there anything
 that finally "clicked" for you in the process of working on this project?
 
 
EXTRA CREDIT:

 If the project had opportunities for extra credit that you attempted,
 be sure to call it out so the grader does not overlook it.


----------------------------------------------------------------------------

All content in a README file is expected to be written in clear English with
proper grammar, spelling, and punctuation. If you are not a strong writer,
be sure to get someone else to help you with proofreading. Consider all project
documentation to be professional writing for your boss and/or potential
customers.
