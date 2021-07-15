package question1;

/*The logic class of the game.
 * The Game class has a static internal class - Result that represents the result object in each iteration.
 * In the created object, there will be information about the line number that a disc will enter,
 * whether the game is over, and who wins if there is one.
 * The Game class uses a matrix and inserts values into cells according to the game user's choice if possible.
 * In each column selection that the player selects.
 * The class will check if there is a sequence of 4 discs in his suit - diagonals / row / column.
 * Once the game class has checked all the outcome options for the game,
 * it will return a result type object with the tested results.*/
public class Game {

    private final int[][] gameMatrix;
    private final int ROWS;
    private final int COLS;
    private int movePlayer = 1;
    private final int FIRST_PLAYER = 1;
    private static final int SECOND_PLAYER = 2;
    private static final int WIN_LENGTH = 4;
    /*The variable SECOND_PLAYER is set statically, because the RESULT class uses it,
     * to indicate the next player in the first iteration of the game.*/

    public Game(int numRow, int numCol) {
        ROWS = numRow;
        COLS = numCol;
        gameMatrix = new int[ROWS][COLS];
    }

    /*The function checks all the results and data of the game, returns an object of type result,
     * Containing the results and data examined.*/
    public Result insert(int colNumber) {
        int row = rowToInsert(colNumber);
        boolean gameOver = finished(row, colNumber);
        boolean isTie = isTie();
       if(gameOver || isTie){
           return Result.createGameOver(isTie, movePlayer, row);
       }
        switchPlayers();
       return Result.createNextTurn(movePlayer, row);
    }

    private void switchPlayers(){
        movePlayer = movePlayer == FIRST_PLAYER ? SECOND_PLAYER : FIRST_PLAYER;
    }

    /*The function returns the free row number that a disk will enter, according to the column selected by the user.*/
    private int rowToInsert(int colNumber){
        int rowNumber = gameMatrix.length - 1;
        for (; rowNumber >= 0; rowNumber--) {
            if (gameMatrix[rowNumber][colNumber] == 0) {
                gameMatrix[rowNumber][colNumber] = movePlayer;
                return rowNumber;
            }
        }
        return rowNumber;
    }

    /*The function returns true or false, is there a winner in every iteration of the game.
     * The function has a three-dimensional array, which contains all the possible directions for victory.
     * In each iteration in the game in which the player inserts a disc,
     * the function receives the cell into which the player inserted a disc,
     * and checks the possible directions to know if there is a win.
     * Possible directions: Column - Victory can be checked from below. Row - You can check right or left
     * Diagonals - You can check diagonals right / left / down / up. */
    private boolean finished(int row, int col) {
        final int[][][] directions = {
                {{1, 0}}, //Vertical
                {{0, 1}, {0, -1}}, //Horizontal
                {{1, 1}, {-1, -1}}, //Left diagonal
                {{-1, 1}, {1, -1}}, //Right diagonal
        };

        for (int[][] direction : directions) {
            int count = 1;
            for (int[] dir : direction) {
                count += countSequence(row, col, dir);
            }
            if (count >= WIN_LENGTH) {
                return true;
            }
        }
    return false;
    }

    /*The function receives an array that returns the possible direction of the test,
     * and the row and column number into which a disk entered, and in each test of a cell in the matrix,
     * checks by calling the isValid function whether the current cell being tested is identical to the value of the inserted disk.
     * If so continue to check if there is a sequence.
     * If the test finds a cell with a value different from the value of the current player, stop.*/
    private int countSequence(int row, int col, int[] direction){
        int color = gameMatrix[row][col];
        int count = 0;
        row += direction[0];
        col += direction[1];

        while(isValid(row, col, color)){
            count++;
            row += direction[0];
            col += direction[1];
        }
        return count;
    }

    /*The function checks whether the row number and column number received do not exceed the matrix limits,
     * and checks whether the value of the current player is the same as the value that exists in the cell being checked.
     * If all conditions are met, returns true. Otherwise a lie.*/
    private boolean isValid(int row, int col, int color){
        return (row >= 0 && row < gameMatrix.length && col >= 0 && col < gameMatrix[0].length
                && gameMatrix[row][col] == color);
    }

    private boolean isTie() {
        for (int ind = 0; ind < gameMatrix[0].length; ind++) {
            if (gameMatrix[0][ind] == 0)
                return false;
        }
        return true;
    }

    /*A function resets the auxiliary matrix that uses the game logic*/
    public void reset() {
        movePlayer = FIRST_PLAYER;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                gameMatrix[row][col] = 0;
            }
        }
    }

    /*Internal class of the Game class,
    the Result class returns an object which contains the results of the current iteration of the game.*/
    public static class Result {

        private boolean gameOver;
        private boolean isTie;
        private int nextPlayerTurn;
        private int rowInsert;

        private Result() {
        }

        public static Result createGameOver(boolean isTie, int winnerPlayer, int rowInsert) {
            Game.Result result = new Game.Result();
            result.rowInsert = rowInsert;
            result.isTie = isTie;
            result.gameOver = true;
            result.nextPlayerTurn = winnerPlayer;
            return result;
        }

        public static Result createNextTurn(int nextPlayerTurn, int rowInsert) {
            Game.Result result = new Game.Result();
            result.nextPlayerTurn = nextPlayerTurn;
            result.rowInsert = rowInsert;
            return result;
        }

        public boolean isGameOver() {
            return gameOver;
        }

        public boolean isTie() { return isTie; }

        public int getNextPlayerTurn() {
            return nextPlayerTurn;
        }

        public int getRowInsert() {
            return rowInsert;
        }
    }
}