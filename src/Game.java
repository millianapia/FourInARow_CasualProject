
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Scanner;
import javax.swing.*;

public class Game extends JFrame {

    // ------------------------------------------------------------
    // Name-constants to represent the seeds and cell contents
    public static final int EMPTY = 0;
    public static final int PLAYER1 = 1;
    public static final int PLAYER2 = 2;

    // Name-constants to represent the various states of the game
    public static final int PLAYING = 0;

    // The game board and the game status
    public int currentState = 1;
    public int currentPlayer;
    public int rows = 6, columns = 7, AmountToWin = 4;
    public int[][] board = new int[rows][columns];
    public Scanner input = new Scanner(System.in); // the input Scanner
    MouseAdapter mouseadapter;
    gamePanel[][] gamepanel;

    // ------------------------------------------------------------
    public static void main(String[] args) {
        new Game();
    }

    // ------------------------------------------------------------
    /* contructor */
    public Game() {
        initGame();
        gamepanel = new gamePanel[rows][columns];
        mouseadapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                gamePanel panel = (gamePanel) me.getSource();
                int y = panel.column;
                int x = availableRow(y);
                if (x == -1)
                    return;

                board[x][y] = currentPlayer;
                if (currentPlayer == PLAYER1)
                    currentPlayer = PLAYER2;
                else
                    currentPlayer = PLAYER1;
                gamepanel[x][y].repaint();

                if (checkForResult(currentPlayer) == 1) {
                    showResult(currentPlayer);
                } else if (checkForResult(currentPlayer) == 2) {
                    showResult(currentPlayer);
                }
            }
        };
        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(rows, columns));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                gamepanel[i][j] = new gamePanel(i, j);
                p1.add(gamepanel[i][j]);
                gamepanel[i][j].addMouseListener(mouseadapter);
            }
        }
        add(p1, BorderLayout.CENTER);

        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    // ------------------------------------------------------------
    public void initGame() {
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < columns; ++col) {
                board[row][col] = EMPTY; // all cells empty
            }
        }
        currentState = PLAYING; // ready to play
        currentPlayer = PLAYER1; // player 1 plays first
    }

    // ------------------------------------------------------------
    private int checkForResult(int player) {
        final int EMPTY_SLOT = 0;
        for (int r = 0; r < rows; r++) { // iterate rows, bottom to top
            for (int c = 0; c < columns; c++) { // iterate columns, left to right
                player = board[r][c];
                if (player == EMPTY_SLOT)
                    continue; // don't check empty slots

                if (c + 3 < columns && player == board[r][c + 1] && // look right
                        player == board[r][c + 2] && player == board[r][c + 3])
                    return player;
                if (r + 3 < rows) {
                    if (player == board[r + 1][c] && // look up
                            player == board[r + 2][c] && player == board[r + 3][c])
                        return player;
                    if (c + 3 < columns && player == board[r + 1][c + 1] && // look up & right
                            player == board[r + 2][c + 2] && player == board[r + 3][c + 3])
                        return player;
                    if (c - 3 >= 0 && player == board[r + 1][c - 1] && // look up & left
                            player == board[r + 2][c - 2] && player == board[r + 3][c - 3])
                        return player;
                }
            }
        }
        return EMPTY_SLOT; // no winner found
    }

    // -------------------------------------------
    // Shows the result of the winning game on a pop-out window
    public void showResult(int winnerPlayer) {

        JFrame frameShowResult = new JFrame();
        if (winnerPlayer == 1) {
            JOptionPane.showMessageDialog(frameShowResult, "\nWinner : YELLOW", "End Game",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frameShowResult, "\nWinner : RED", "End Game",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ------------------------------------------------------------
    /** finds the first empty space in a column starting at the bottom. */
    public int availableRow(int col) {
        for (int row = rows - 1; row >= 0; row--) {
            if (board[row][col] == EMPTY) {
                return row;
            }
        }
        return -1;
    }

    // ------------------------------------------------------------
    class gamePanel extends JPanel {
        int row, column;

        public gamePanel(int r, int c) {
            row = r;
            column = c;
            this.setBackground(Color.BLUE);
            this.setPreferredSize(new Dimension(100, 100)); // try commenting out this line!
        }

        // ------------------------------------------------------------
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Color c = board[row][column] == EMPTY ? Color.WHITE
                    : board[row][column] == PLAYER1 ? Color.RED : Color.YELLOW;
            g.setColor(c);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.fillOval(10, 10, 90, 90);
            g2d.dispose();
        }
    }

}