import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Game extends JFrame {

    //Non changeable numbers
    public static final int EMPTY = 0; //to be used for empty spots in board
    public static final int PLAYER1 = 1;
    public static final int PLAYER2 = 2;
    public static final int PLAYING = 0;

    public static int numberOfPlayers;
    public int currentState = 1;
    public int currentPlayer;
    public int rows = 6, columns = 7;
    public int[][] board = new int[rows][columns];
    MouseAdapter mouseadapter;
    gamePanel[][] gamepanel;


    /**
     * Method that gives you the option between playing pvp or player vs AI
     *
     * @return numberOfPlayers 1 for player vs AI, 2 for PvP
     */
    public int checkPlayerAmount() {
        Object[] options = {"Player vs Player", "Player vs AI"};
        int n = JOptionPane.showOptionDialog(this, "Gamemode", "Connect Four", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if (n == 0) {
            numberOfPlayers = 2;
        } else {
            numberOfPlayers = 1;
        }

        return numberOfPlayers;
    }


    /**
     * Core of the game, initialize board and game, starts playing logic depending on how many players.
     * Communication between the graphical part of the game and logical. Places mouselistener on every
     * gamepanel element. Initialize other swing settings
     */
    public Game() {
        initGame(board);
        gamepanel = new gamePanel[rows][columns];
        if (numberOfPlayers == 2) {
            PVPPlay();
        } else {
            AIPlay();
        }
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(rows, columns));
        this.setTitle("Connect Four");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                gamepanel[i][j] = new gamePanel(i, j);
                panel.add(gamepanel[i][j]);
                gamepanel[i][j].addMouseListener(mouseadapter);
            }
        }
        add(panel, BorderLayout.CENTER);

        //swing settings
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    /**
     * PVPPlay is where the player vs player logic is implemented.
     * I use the MouseAdapter method mousePressed that listens for
     * a mouse pressed in the area.
     */
    public void PVPPlay() {
        mouseadapter = new MouseAdapter() {
            /**
             * @param me where i clicked in the game
             * Method starts with *translating* my click
             * to a place on the board. Then places the
             * current player number on the board on where i decided to click.
             *  Switches between player by using a simple if/else
             */
            @Override
            public void mousePressed(MouseEvent me) {
                gamePanel panel = (gamePanel) me.getSource();
                int y = panel.column;
                int x = availableRow(y);
                if (x == -1) //if column is full, it returns
                    return;

                board[x][y] = currentPlayer;

                if (currentPlayer == PLAYER1)
                    currentPlayer = PLAYER2;
                else
                    currentPlayer = PLAYER1;
                gamepanel[x][y].repaint();

                //checks if someone has won after each piece placed.
                if (checkForResult(currentPlayer, board) == 1) {
                    showResult(currentPlayer);
                } else if (checkForResult(currentPlayer, board) == 2) {
                    showResult(currentPlayer);
                }
            }
        };
    }

    /**
     * AIPlay is where the player vs AI logic is implemented.
     * I use the MouseAdapter method mousePressed that listens for
     * a mouse pressed in the area.
     */
    public void AIPlay() {
        AI ai = new AI();
        mouseadapter = new MouseAdapter() {
            /**
             * @param me where i clicked in the game
             * Method starts with *translating* my click
             * to a place on the board. Then places the
             * current player number on the board on where i decided to click.
             * Uses if/else to see if player is AI or human
             * Switches between player by using a simple if/else
             */
            @Override
            public void mousePressed(MouseEvent me) {
                int x = -1;
                int y = 0;
                if (currentPlayer == PLAYER1) {
                    gamePanel panel = (gamePanel) me.getSource();
                    y = panel.column;
                    x = availableRow(y);
                } else {
                    while (x == -1) { //checks if column is full
                        y = ai.move();
                        x = availableRow(y);
                    }
                }
                if (x == -1)
                    return;

                board[x][y] = currentPlayer;

                if (currentPlayer == PLAYER1)
                    currentPlayer = PLAYER2;
                else
                    currentPlayer = PLAYER1;
                gamepanel[x][y].repaint();

                //checks if someone has won after each piece placed.
                if (checkForResult(currentPlayer, board) == 1) {
                    showResult(currentPlayer);
                } else if (checkForResult(currentPlayer, board) == 2) {
                    showResult(currentPlayer);
                }
            }
        };
    }

    /**
     * @param board with information about where the players are located - 0 for empty, 1 for player 1 and 2 for player 2
     *              initialize game with clearing the game with setting all cells to empty (0)
     *              Also changing gamestate from nothing to playing and currentplayer to player1
     */
    public void initGame(int[][] board) {
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < columns; ++col) {
                board[row][col] = EMPTY;
            }
        }
        numberOfPlayers = checkPlayerAmount();
        currentState = PLAYING;
        currentPlayer = PLAYER1;
    }

    /**
     * @param player that just put down a piece
     * @param board  updated boardversion
     * @return either playernumber that won or empty_slot which means no one won that round
     */
    public int checkForResult(int player, int[][] board) {
        final int EMPTY_SLOT = 0;
        for (int r = 0; r < rows; r++) { //rows
            for (int c = 0; c < columns; c++) { //columns
                player = board[r][c];
                if (player == EMPTY_SLOT)
                    continue;

                if (c + 3 < columns && player == board[r][c + 1] && player == board[r][c + 2] && player == board[r][c + 3])
                    return player;
                if (r + 3 < rows) {
                    if (player == board[r + 1][c] && player == board[r + 2][c] && player == board[r + 3][c])
                        return player;
                    if (c + 3 < columns && player == board[r + 1][c + 1] && player == board[r + 2][c + 2] && player == board[r + 3][c + 3])
                        return player;
                    if (c - 3 >= 0 && player == board[r + 1][c - 1] && player == board[r + 2][c - 2] && player == board[r + 3][c - 3])
                        return player;
                }
            }
        }
        return EMPTY_SLOT; //No winner found
    }

    /**
     * shows winner player in a pop-up window
     *
     * @param winnerPlayer player that won the game
     */
    public void showResult(int winnerPlayer) {

        JFrame frameShowResult = new JFrame();
        if (winnerPlayer == 1) {
            JOptionPane.showMessageDialog(frameShowResult, "\nWinner: YELLOW", "End Game",
                    JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } else if (winnerPlayer == 2) {
            JOptionPane.showMessageDialog(frameShowResult, "\nWinner: RED", "End Game",
                    JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }


    /**
     * @param col is column that needs to be checked
     * @return either row number that is available or -1 if not
     */
    public int availableRow(int col) {
        for (int row = rows - 1; row >= 0; row--) {
            if (board[row][col] == EMPTY) {
                return row;
            }
        }
        return -1;
    }


    /**
     * gamePanel is the visual logic in the game
     */
    class gamePanel extends JPanel {
        int row, column;

        /**
         * @param r row
         * @param c column
         *          Sets the background blue and sets dimensions
         */
        public gamePanel(int r, int c) {
            row = r;
            column = c;
            this.setBackground(Color.BLUE);
            this.setPreferredSize(new Dimension(100, 100));
        }

        /**
         * @param graphics graphical element
         *                 Here it paints according to what state the cell is in.
         *                 If it is empty, the circle should be white, but
         *                 if it is a player it should be red or yellow
         */
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Color piece = board[row][column] == EMPTY ? Color.WHITE
                    : board[row][column] == PLAYER1 ? Color.RED : Color.YELLOW;
            graphics.setColor(piece);
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.fillOval(10, 10, 90, 90);
            graphics2D.dispose();
        }
    }

}