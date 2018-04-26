
import org.junit.Assert;
import org.junit.Test;

public class GameTest {

    //tests horizontal
    @Test
    public void winnerTestHorizontal() {
        Game game = new Game();
        int[][] board = new int[6][7];
        game.initGame(board);
        board[1][1] = 1;
        board[1][2] = 1;
        board[1][3] = 1;
        board[1][4] = 1;
        Assert.assertTrue(1 == game.checkForResult(1, board));
    }

    //Tests to see if player 1 is not the winner.
    @Test
    public void notWinnerTest() {
        Game game = new Game();
        int[][] board = new int[6][7];
        game.initGame(board);
        Assert.assertTrue(0 == game.checkForResult(1, board));
    }

    //tests vertical
    @Test
    public void winnerTestVertical() {
        Game game = new Game();
        int[][] board = new int[6][7];
        game.initGame(board);
        board[1][1] = 1;
        board[2][1] = 1;
        board[3][1] = 1;
        board[4][1] = 1;
        Assert.assertTrue(1 == game.checkForResult(1, board));
    }
    //tests diagonal one way
    @Test
    public void winnerTestDiagonal1() {
        int[][] board = new int[6][7];
        Game game = new Game();
        game.initGame(board);
        board[1][1] = 1;
        board[2][2] = 1;
        board[3][3] = 1;
        board[4][4] = 1;
        Assert.assertTrue(1 == game.checkForResult(1, board));
    }

    //tests diagonal the other way
    @Test
    public void winnerTestDiagonal2() {
        int[][] board = new int[6][7];
        Game game = new Game();
        game.initGame(board);
        board[1][4] = 1;
        board[2][3] = 1;
        board[3][2] = 1;
        board[4][1] = 1;
        Assert.assertTrue(1 == game.checkForResult(1, board));
    }

}
