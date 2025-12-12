import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;

/**
 * GUI for displaying CircuitTracer results.
 * Shows the circuit board as a grid and allows selection of different solution paths.
 * 
 * @author Chelsea Ma
 */
public class CircuitTracerGUI extends JFrame {
    private CircuitBoard originalBoard;
    private ArrayList<TraceState> solutions;
    private JLabel[][] gridLabels;
    private JList<String> solutionList;
    private DefaultListModel<String> listModel;
    private Color[][] originalColors;
    
    //Colors for display
    private static final Color OPEN_COLOR = Color.WHITE;
    private static final Color CLOSED_COLOR = Color.DARK_GRAY;
    private static final Color START_COLOR = new Color(100, 200, 100);  // Light green
    private static final Color END_COLOR = new Color(255, 100, 100);     // Light red
    private static final Color TRACE_COLOR = new Color(100, 150, 255);   // Light blue
    private static final Color HIGHLIGHT_COLOR = new Color(255, 255, 100); // Yellow
    
    /**
     * Constructor for the GUI
     * @param board the original circuit board
     * @param solutions list of all shortest path solutions
     */
    public CircuitTracerGUI(CircuitBoard board, ArrayList<TraceState> solutions) {
        this.originalBoard = board;
        this.solutions = solutions;
        
        setTitle("Circuit Tracer - Solutions");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        //Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        //Create title
        JLabel titleLabel = new JLabel("Circuit Board Solutions", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        //Create center panel with board and solutions list
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        
        //Create the board grid
        JPanel boardPanel = createBoardPanel();
        centerPanel.add(boardPanel, BorderLayout.CENTER);
        
        //Create the solutions list
        JPanel solutionsPanel = createSolutionsPanel();
        centerPanel.add(solutionsPanel, BorderLayout.EAST);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        //Create info panel
        JPanel infoPanel = createInfoPanel();
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        pack();
        setLocationRelativeTo(null); //Center on screen
        setVisible(true);
    }
    
    /**
     * Creates the board display panel with a grid of labels
     */
    private JPanel createBoardPanel() {
        JPanel boardPanel = new JPanel();
        boardPanel.setBorder(BorderFactory.createTitledBorder("Circuit Board"));
        
        int rows = originalBoard.numRows();
        int cols = originalBoard.numCols();
        
        boardPanel.setLayout(new GridLayout(rows, cols, 2, 2));
        gridLabels = new JLabel[rows][cols];
        originalColors = new Color[rows][cols];
        
        //Create grid labels
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char c = originalBoard.charAt(row, col);
                JLabel label = new JLabel(String.valueOf(c), SwingConstants.CENTER);
                label.setOpaque(true);
                label.setPreferredSize(new Dimension(40, 40));
                label.setFont(new Font("Monospaced", Font.BOLD, 16));
                label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                
                //Set colors based on character
                Color bgColor;
                switch (c) {
                    case 'O':
                        bgColor = OPEN_COLOR;
                        label.setForeground(Color.BLACK);
                        break;
                    case 'X':
                        bgColor = CLOSED_COLOR;
                        label.setForeground(Color.WHITE);
                        break;
                    case '1':
                        bgColor = START_COLOR;
                        label.setForeground(Color.BLACK);
                        break;
                    case '2':
                        bgColor = END_COLOR;
                        label.setForeground(Color.BLACK);
                        break;
                    default:
                        bgColor = OPEN_COLOR;
                        label.setForeground(Color.BLACK);
                }
                
                label.setBackground(bgColor);
                originalColors[row][col] = bgColor;
                gridLabels[row][col] = label;
                boardPanel.add(label);
            }
        }
        
        return boardPanel;
    }
    
    /**
     * Creates the solutions list panel
     */
    private JPanel createSolutionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Solutions (select to highlight)"));
        
        listModel = new DefaultListModel<>();
        
        if (solutions.isEmpty()) {
            listModel.addElement("No solutions found");
        } else {
            for (int i = 0; i < solutions.size(); i++) {
                int pathLength = solutions.get(i).pathLength();
                listModel.addElement("Solution " + (i + 1) + " (length: " + pathLength + ")");
            }
        }
        
        solutionList = new JList<>(listModel);
        solutionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        solutionList.setFont(new Font("Arial", Font.PLAIN, 12));
        
        //Add selection listener
        solutionList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !solutions.isEmpty()) {
                int selectedIndex = solutionList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    highlightSolution(selectedIndex);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(solutionList);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        //Add clear button
        JButton clearButton = new JButton("Clear Highlighting");
        clearButton.addActionListener(e -> clearHighlighting());
        panel.add(clearButton, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Creates the info panel with statistics
     */
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Information"));
        
        int rows = originalBoard.numRows();
        int cols = originalBoard.numCols();
        int numSolutions = solutions.size();
        String pathLength = numSolutions > 0 ? String.valueOf(solutions.get(0).pathLength()) : "N/A";
        
        JLabel infoLabel = new JLabel(
            String.format("Board Size: %d×%d  |  Solutions Found: %d  |  Path Length: %s",
                rows, cols, numSolutions, pathLength)
        );
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(infoLabel);
        
        return panel;
    }
    
    /**
     * Highlights a specific solution on the board
     */
    private void highlightSolution(int solutionIndex) {
        // First, reset all cells to original colors
        clearHighlighting();
        
        if (solutionIndex < 0 || solutionIndex >= solutions.size()) {
            return;
        }
        
        TraceState solution = solutions.get(solutionIndex);
        ArrayList<Point> path = solution.getPath();
        
        //Highlight the path
        for (Point p : path) {
            int row = p.x;
            int col = p.y;
            gridLabels[row][col].setBackground(TRACE_COLOR);
            gridLabels[row][col].setText("T");
        }
        
        //Keep start and end with their original colors but show T
        Point start = originalBoard.getStartingPoint();
        Point end = originalBoard.getEndingPoint();
        gridLabels[start.x][start.y].setBackground(START_COLOR);
        gridLabels[start.x][start.y].setText("1");
        gridLabels[end.x][end.y].setBackground(END_COLOR);
        gridLabels[end.x][end.y].setText("2");
    }
    
    /**
     * Clears all highlighting and returns board to original state
     */
    private void clearHighlighting() {
        int rows = originalBoard.numRows();
        int cols = originalBoard.numCols();
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char c = originalBoard.charAt(row, col);
                gridLabels[row][col].setText(String.valueOf(c));
                gridLabels[row][col].setBackground(originalColors[row][col]);
            }
        }
        
        solutionList.clearSelection();
    }
}