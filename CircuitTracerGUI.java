import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

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
    
    /**
     * Constructor for the GUI
     * @param board the original circuit board
     * @param solutions list of all shortest path solutions
     */
    public CircuitTracerGUI(CircuitBoard board, ArrayList<TraceState> solutions) {
        this.originalBoard = board;
        this.solutions = solutions;
        
        setTitle("Circuit Trace Search (on onyx.boisestate.edu)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Create menu bar
        createMenuBar();
        
        //Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        //Create the board grid
        JPanel boardPanel = createBoardPanel();
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        
        //Create the solutions list
        JPanel solutionsPanel = createSolutionsPanel();
        mainPanel.add(solutionsPanel, BorderLayout.EAST);
        
        add(mainPanel);
        
        pack();
        setLocationRelativeTo(null);// Center on screen
        setVisible(true);
    }
    
    /**
     * Creates the menu bar with File and Help menus
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        //File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(quitItem);
        
        //Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About...");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * Shows the About dialog
     */
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "Circuit Tracing Search\n\n" +
            "Written by Chelsea Ma (chelseama@u.boisestate.edu)",
            "About MenuDemo... (on onyx.boisestate.edu)",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Creates the board display panel with a grid of labels
     */
    private JPanel createBoardPanel() {
        JPanel boardPanel = new JPanel();
        
        int rows = originalBoard.numRows();
        int cols = originalBoard.numCols();
        
        boardPanel.setLayout(new GridLayout(rows, cols, 1, 1));
        boardPanel.setBackground(Color.GRAY);
        gridLabels = new JLabel[rows][cols];
        
        //Create grid labels
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char c = originalBoard.charAt(row, col);
                JLabel label = new JLabel(String.valueOf(c), SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBackground(Color.WHITE);
                label.setPreferredSize(new Dimension(60, 60));
                label.setFont(new Font("SansSerif", Font.BOLD, 32));
                label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                label.setForeground(Color.BLACK);
                
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
        JPanel panel = new JPanel(new BorderLayout());
        
        listModel = new DefaultListModel<>();
        
        if (solutions.isEmpty()) {
            listModel.addElement("No solutions found");
        } else {
            for (int i = 0; i < solutions.size(); i++) {
                listModel.addElement("Solution " + (i + 1));
            }
        }
        
        solutionList = new JList<>(listModel);
        solutionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        solutionList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
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
        scrollPane.setPreferredSize(new Dimension(120, 300));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Highlights a specific solution on the board
     */
    private void highlightSolution(int solutionIndex) {
        // First, reset all cells to original state
        clearHighlighting();
        
        if (solutionIndex < 0 || solutionIndex >= solutions.size()) {
            return;
        }
        
        TraceState solution = solutions.get(solutionIndex);
        ArrayList<Point> path = solution.getPath();
        
        //Highlight the path with red T's
        for (Point p : path) {
            int row = p.x;
            int col = p.y;
            gridLabels[row][col].setText("T");
            gridLabels[row][col].setForeground(Color.RED);
        }
        
        //Keep start and end with their original characters in black
        Point start = originalBoard.getStartingPoint();
        Point end = originalBoard.getEndingPoint();
        gridLabels[start.x][start.y].setText("1");
        gridLabels[start.x][start.y].setForeground(Color.BLACK);
        gridLabels[end.x][end.y].setText("2");
        gridLabels[end.x][end.y].setForeground(Color.BLACK);
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
                gridLabels[row][col].setForeground(Color.BLACK);
            }
        }
    }
}