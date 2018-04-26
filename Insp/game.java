import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class game extends JFrame
{
    private int size = 7;                             // Board size
    private int playerOrder=0;                    // First player 1 will PVPPlay the game
    private int numberOfPlayer;                   // withChip.Player number
    private static int livingCellNumber=0;        // Number of living cells

    // GUI Requirements
    private final JFrame frame;                   // Frame
    private final JPanel panel;                   // Panel
    private final JButton[][] buttons;            // Buttons
    private Cell gameBoard[][];                   // Game Board
    private final GridLayout grid;                // GridLayout

    // Button icons
    ImageIcon empty   = new ImageIcon(getClass().getResource("emptycell.png"));
    ImageIcon player1 = new ImageIcon(getClass().getResource("player1.png"));
    ImageIcon player2 = new ImageIcon(getClass().getResource("player2.png"));


    public static void main(String[] args)
    {
        game game = new game();
    }

    public game ()
    {
        //Start menu
        //---------------------------------------------------------------
        frame = new JFrame("Connect Four Game");
        panel = new JPanel();

        Object[] options = {"withChip.Player vs withChip.Player", "withChip.Player vs AI"};
        int n = JOptionPane.showOptionDialog(frame, "Gamemode", "Connect Four",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);
        if (n == 0) {
            numberOfPlayer = 2;
        } else {
            numberOfPlayer = 1;
        }

        dynamicAllocation();         // Create 2D dynamic Cell array

        buttons = new JButton[getBoardSize()][getBoardSize()];    // Create button array
        grid = new GridLayout(getBoardSize(),getBoardSize());     // Create GridLayout
        panel.setLayout(grid);

        // Initialization board
        initialBoard();

        // Frame functions
        frame.setContentPane(panel);
        frame.pack();                 // Automatic sizing of the window based on the added swing components
        frame.setLocationRelativeTo(null); // Game board will be center of the screen
        frame.setVisible(true);            // Show frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the frame
    }




    /**
     * Getter to board size
     * @return Board size
     */
    public int getBoardSize()
    {
        return size;
    }

    /**
     * static function that returns the number of living cells in all the games.
     * @return Number of living cell in game
     */
    public static int numberOfLivingCells()
    {
        return livingCellNumber;
    }


    /**
     * Create 2D dynamic Cell array
     */
    public void dynamicAllocation()
    {
        // Create dynamic Cell array to game board
        gameBoard = new Cell[getBoardSize()][getBoardSize()];
        for (int i = 0; i <getBoardSize(); i++)
        {
            for (int j = 0; j <getBoardSize(); j++)
            {
                gameBoard [i][j]=new Cell();
            }
        }
    }
    /**
     * Add buttons to game board
     */
    public void addButtonsToBoard()
    {
        for(int i=0; i<getBoardSize(); ++i)
        {
            for(int j=0; j<getBoardSize(); ++j)
            {
                buttons[i][j] = new JButton(empty); // Empty button

                if(numberOfPlayer==1)   // Computer vs withChip.Player button listener
                {
                    buttons[i][j].addActionListener(new AI());
                }

                if(numberOfPlayer==2)   // Two players button listener
                {
                    buttons[i][j].addActionListener(new PvP());
                }

                panel.add(buttons[i][j]);   // Add buttons to panel
            }
        }
    }
    /**
     * Initial all cells to empty
     */
    public void initialBoard()
    {
        for (int i=getBoardSize()-2; i>= 0; --i)
        {
            for (int j = getBoardSize()-1; j>=0; --j)
            {
                gameBoard[i][j].setCellState(-99);
            }
        }
        addButtonsToBoard(); // Add buttons and listener
    }



    /**
     * Game winning state
     * If the four cell is same, user 1 will win the game
     * @param winner integer If the player 1 is equal to 1, otherwise 2
     */
    public void winnerPlayer(int winner)
    {
        for(int i=0; i<getBoardSize(); ++i)
        {
            for(int j=0; j<getBoardSize(); ++j)
            {
                if(gameBoard[i][j].getCellState() == winner)
                {
                    // CHECK UP TO DOWN POSITIONS
                    if(i+3<getBoardSize())
                    {
                        if(gameBoard[i+1][j].getCellState() == winner && gameBoard[i+2][j].getCellState() == winner && gameBoard[i+3][j].getCellState() == winner)
                        {
                            if(winner==1)
                                showResult(1);
                            else
                                showResult(2);
                        }
                    }
                    // CHECK LEFT TO RIGHT POSITION
                    if(j + 3 <getBoardSize())
                    {
                        if(gameBoard[i][j+1].getCellState() == winner && gameBoard[i][j+2].getCellState() == winner && gameBoard[i][j+3].getCellState() == winner)
                        {
                            if(winner==1)
                                showResult(1);
                            else
                                showResult(2);
                        }
                    }

                    // CHECK DIAGONAL LEFT TO RIGHT POSITION
                    if(i  < getBoardSize()- 3 && j<getBoardSize()-3)
                    {
                        if(gameBoard[i+1][j+1].getCellState() == winner && gameBoard[i+2][j+2].getCellState() == winner && gameBoard[i+3][j+3].getCellState() == winner)
                        {
                            if(winner==1)
                                showResult(1);
                            else
                                showResult(2);
                        }
                    }

                    // CHECK DIAGONAL RIGHT TO LEFT POSITION
                    if(i  < getBoardSize()- 3 && j - 3 >= 0 )
                    {
                        if(gameBoard[i+1][j-1].getCellState() == winner && gameBoard[i+2][j-2].getCellState() == winner && gameBoard[i+3][j-3].getCellState() == winner)
                        {
                            if(winner==1)
                                showResult(1);
                            else
                                showResult(2);
                        }
                    }
                }
            }
        }
    } // End winnerPlayer function


    /**
     * Show winner player on the new frame
     * @param winnerPlayer integer if the parameter is equal to 1,player 1 is winner.Otherwise, player 2
     */
    public void showResult(int winnerPlayer)
    {
        JFrame frameShowResult = new JFrame();
        if(winnerPlayer==1)
        {
            JOptionPane.showMessageDialog(frameShowResult,
                    "\nWinner : withChip.Player 1\n\nThe new game will start.\n\n",
                    "End Game",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            JOptionPane.showMessageDialog(frameShowResult,
                    "\nWinner : withChip.Player 2\n\nThe new game will start.\n\n",
                    "End Game",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     *
     * Action listener to game button
     * Computer vs withChip.Player 1
     */
    private class AI implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            try {
                for(int i=getBoardSize()-1; i>=0; --i)  // Check the buttons up to down position
                {
                    for(int j=0; j<=getBoardSize()-1; ++j)
                    {
                        // Get the button component that was clicked
                        if(buttons[i][j]== e.getSource())
                        {

                            if(0 == playerOrder%2)   // withChip.Player 1 operations
                            {
                                for(int k=0; k<=getBoardSize(); ++i)
                                {
                                    // withChip.Player 1 Operations
                                    // Fill the board from down to up
                                    if(gameBoard[i-k][j].getCellState() == 0)
                                    {
                                        buttons[i-k][j].setIcon(player1);           // Change button icon
                                        gameBoard[i-k][j].setAllPosition('X', i);   // Set cell parameters
                                        gameBoard[i-k][j].setCellState(1);          // Set cell state
                                        ++livingCellNumber;                         // Increase living cell number
                                        winnerPlayer(1);                            // Check game winning state
                                        break;
                                    }
                                }

                                setUpperCellToEmpty(i,j); // Set the upper cells to empty cell to listen button
                                System.out.println("... withChip.Player 1 played ... ");
                                ++playerOrder;  // Change player order from player 1 to computer
                                break;
                            }

                            // Computer Operations
                            // Basic idea is filling the cells left to right

                            if(1 == playerOrder%2)
                            {
                                moveComputer(i);
                                System.out.println("... Computer played ... ");
                                ++playerOrder; // Change player order from computer to player 1
                                break;
                            }
                            else
                            {
                                System.out.println("Wrong move");
                            }
                        }
                    }
                }


            }
            catch(Exception ex)
            {
                System.out.println(ex);
            }
        }

    }
    


    /**
     * Set the upper cells to empty cell to listen button
     * @param rowPos integer row position to board
     * @param columnPos integer column position to board
     */
    public void setUpperCellToEmpty(int rowPos, int columnPos)
    {
        try
        {
            gameBoard[rowPos-1][columnPos].setCellState(0);
        }
        catch (Exception ex)
        { }
    }

    /**
     * Computer's logic fills cells from left to right
     * @param rowPosition Cell row position
     */
    public void moveComputer(int rowPosition)
    {
        int l,m;
        boolean flag=false;

        for(l=getBoardSize()-1; (l>=0)&& !flag; --l)
        {
            for(m=0; (m<getBoardSize()) && !flag; ++m)
            {
                if(gameBoard[l][m].getCellState() == 0)
                {
                    buttons[l][m].setIcon(player2);         // Set new button icon
                    gameBoard[l][m].setAllPosition('O', rowPosition); // Set cell parameters
                    gameBoard[l][m].setCellState(2);        // Set cell state
                    ++livingCellNumber;
                    winnerPlayer(2); // Check the computer winning state
                    flag = true;
                    setUpperCellToEmpty(l,m);
                }
            }
        }
    }

    /**
     * Action listener to game button
     * withChip.Player 1 vs withChip.Player 2
     */
    private class PvP implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            try {
                int eventFlag = 0;
                int flagPlayerOrder=0;

                for(int i=getBoardSize()-1; i>=0; --i)
                {
                    for(int j=0; j<=getBoardSize()-1; ++j)
                    {
                        if(eventFlag==0 && buttons[i][j]== e.getSource()) // Get the button component that was clicked
                        {
                            if(flagPlayerOrder==0 && playerOrder%2==0)
                            {
                                // withChip.Player 1 Operations
                                // Fill the board from down to up
                                for(int k=0; k<=getBoardSize(); ++i)
                                {
                                    if(gameBoard[i-k][j].getCellState()==0 && playerOrder%2==0)
                                    {
                                        buttons[i-k][j].setIcon(player1);          // Set new icon to player 1
                                        gameBoard[i-k][j].setAllPosition('X', i);  // Set cell parameters
                                        gameBoard[i-k][j].setCellState(1);
                                        ++livingCellNumber;  // Increase living cell number
                                        winnerPlayer(1);     // Check player 1 winning state
                                        flagPlayerOrder=1;
                                        eventFlag=1;
                                        break;
                                    }
                                }

                                setUpperCellToEmpty(i,j);   // Set upper cell to empty
                                System.out.println("... withChip.Player 1 played ... ");
                                ++playerOrder; // Change order from player 1 to player 2
                                break;
                            }

                            // withChip.Player 2 operations
                            if(flagPlayerOrder==0 && playerOrder%2==1)
                            {
                                for(int k=0; k<=getBoardSize(); ++i)
                                {
                                    if(gameBoard[i-k][j].getCellState()==0 && playerOrder%2==1)
                                    {
                                        buttons[i-k][j].setIcon(player2);            // Set new icon to player 2
                                        gameBoard[i-k][j].setAllPosition('O', i);    // Set cell parameters
                                        gameBoard[i-k][j].setCellState(2);           // Set cell state
                                        ++livingCellNumber;
                                        winnerPlayer(2);
                                        flagPlayerOrder=1;
                                        eventFlag=1;
                                        break;
                                    }
                                }
                                setUpperCellToEmpty(i,j);
                                System.out.println("... withChip.Player 2 played ... ");
                                ++playerOrder;
                                break;
                            }
                        }
                    }
                }
            }catch(Exception ex)
            {
                System.out.println(ex);
            }

        }
    }
}
