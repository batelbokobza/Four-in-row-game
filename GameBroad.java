package question1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*The GameBroad class creates the main board of the game.
 * Creates panels for the board, and buttons for the players to choose from.
 * The GameBroad class uses the Game class which is responsible for the game logic.
 * The Game class returns an object with the game results - which column to insert a disc of the user's choice,
 *  whether the game is over, and whether there is a winner or a tie.
 * If players choose to start a new game by pressing the Clear button, required to reset the game logic class as well..*/
public class GameBroad extends JPanel {

    private interface ColorP{
        Color COLOR_FIRST_PLAYER = new Color(220, 52, 18); //The disc color of the first player.
        Color COLOR_SECOND_PLAYER = new Color(220, 144, 7); //The disc color of the other player.
        Color BUTTON = new Color(137, 186, 236); //Color buttons.
        Color BACKGROUND = new Color(0xACACA5);//The default background color of the panel.
    }

    private interface Constant{
        int FULL_COLUMN = 0; //If the column is full.
        int ROWS = 6; //Number of rows in the panel.
        int COLS = 7; //Number of columns in the panel.
        String TURN_FIRST_PLAYER = "The red player's turn!";
        String TURN_SECOND_PLAYER = "The orange player's turn!";
        int FIRST_PLAYER_TURN = 1;
    }

    private int curPlayerTurn = Constant.FIRST_PLAYER_TURN; // first player makes first move
    private final JButton clearBut = new JButton("clear"); //Button for cleaning the board.
    private final CellPanel[][] matrixPanel = new CellPanel[Constant.ROWS][Constant.COLS]; //Matrix of panels on the main panel.
    private final JButton[] buttons = new JButton[Constant.COLS]; //Panel of buttons.
    private final JLabel infoLabel = new JLabel();//Label information about the turn of the players.
    private final JPanel infoPanel = new JPanel();

    private Game game = new Game(Constant.ROWS, Constant.COLS);

    /*Class constructor.
     * Produces the matrix cells located on the panel.
     * Each cell is white.
     * In addition, adds to the main panel the game buttons and the clean button.*/
    GameBroad() {
        this.setLayout(new BorderLayout());

        JPanel tempPanel = new JPanel(new BorderLayout());
        tempPanel.add(creteInfoPanel(), BorderLayout.NORTH);

        JPanel matrixCells = new JPanel();
        matrixCells.setLayout(new GridLayout(Constant.ROWS, Constant.COLS));
        infoLabel.setText(Constant.TURN_FIRST_PLAYER); //Information Boot - The first player starts.
        infoPanel.setBackground(ColorP.COLOR_FIRST_PLAYER);////color panel Boot - The first player starts.

        for (int row = 0; row < matrixPanel.length; row++)
            for (int col = 0; col < matrixPanel[0].length; col++) {
                matrixPanel[row][col] = new CellPanel();
                matrixCells.add(matrixPanel[row][col]);
            }

        tempPanel.add(matrixCells, BorderLayout.CENTER);
        this.add(tempPanel, BorderLayout.NORTH);
        this.add(createPanelOfButtons(), BorderLayout.CENTER);
        this.add(clearButton(), BorderLayout.PAGE_END);
    }

    /*Creates the information panel, which displays the information of the player in turn to play.*/
    private JPanel creteInfoPanel(){
        infoLabel.setForeground(Color.BLACK);
        infoLabel.setFont(new Font("Ariel", Font.BOLD, 20));
        infoPanel.add(infoLabel, BorderLayout.CENTER);
        return infoPanel;
    }

    /*The function updates the panel of information in each iteration of the game.
     * Updates the text on the panel, and the color according to the current player.*/
    private void updateNextTurnMessage(){
        String infoTurnPlayer = curPlayerTurn == Constant.FIRST_PLAYER_TURN ? Constant.TURN_FIRST_PLAYER : Constant.TURN_SECOND_PLAYER;
        Color colorPlayerTurn = curPlayerTurn == Constant.FIRST_PLAYER_TURN ? ColorP.COLOR_FIRST_PLAYER : ColorP.COLOR_SECOND_PLAYER;
        infoLabel.setText(infoTurnPlayer);
        infoPanel.setBackground(colorPlayerTurn);
    }

    /*The function updates the information panel when the game is over.
     * The game ends when there is a tie, or when there is a winner.
     * Updates the text on the panel according to the reason the game is over.*/
    private void updateGameEndedMessage(boolean isTie){
        if(isTie){
            infoLabel.setText("The game ended equally. For a new game, press on Clear.");
        } else{
            String infoWinner = (curPlayerTurn == Constant.FIRST_PLAYER_TURN ? "Red player won!" : "Orange player won!") +
                    " For a new game, press Clear.";
            infoLabel.setText(infoWinner);
        }
    }

    /*A function reads the press of the Clear button and calls the reset function to reset the game to start a new game.*/
    private JPanel clearButton() {
        clearBut.setBackground(ColorP.BUTTON);
        clearBut.setPreferredSize(new Dimension(80, 40));
        clearBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JButton btn : buttons)
                    btn.setEnabled(true);
                gameReset();
            }
        });
        JPanel panel = new JPanel();
        panel.setBackground(new Color(25, 63, 206));
        panel.add(clearBut);
        return panel;
    }

    /*A function creates a panel of push buttons in the game matrix columns.*/
    private JPanel createPanelOfButtons() {
        JPanel controlsBtn = new JPanel();
        controlsBtn.setLayout(new GridLayout(1, Constant.COLS));
        insertButtonsOfPanel(controlsBtn);
        return controlsBtn;
    }

    /*The function puts the game buttons on the board, and identifies the player pressing the button.
     * The function sends the selected button number to the function that checks the user selection.*/
    private void insertButtonsOfPanel(JPanel controlsBtn) {
        JPanel panel = new JPanel(new GridLayout());

        for (int numberButton = 1; numberButton <= Constant.COLS; numberButton++) {
            JButton button = new JButton(String.valueOf(numberButton));
            panel.add(button);
            button.setBackground(ColorP.BUTTON);
            final int colNumber = numberButton - 1;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    resultCheckInsert(colNumber);
                }
            });
            buttons[numberButton - 1] = button;
        }
        controlsBtn.add(panel);
    }

    /*The function uses the result object from the game class, and performs the appropriate actions,
     * depending on the result obtained after the player's selection.
     * If the column is full - performs a button shutdown.
     * If following the selection the player wins - call to the gameEnd function.
     * If all the board columns are full and there is no winner, an appropriate message is returned.*/
    private void resultCheckInsert(int colNumber) {
        Game.Result resultGame = game.insert(colNumber);
        int rowInsert = resultGame.getRowInsert();
        insertDiscIntoBroad(rowInsert, colNumber);
        curPlayerTurn = resultGame.getNextPlayerTurn();
        if (rowInsert == Constant.FULL_COLUMN) {
            buttons[colNumber].setEnabled(false);
        }
        if (resultGame.isGameOver()) {
            GameEnd(resultGame.isTie());
        } else {
            updateNextTurnMessage();
        }
    }

    /*The function inserts discs into a game board, according to the color appropriate to the player playing.*/
    private void insertDiscIntoBroad(int rowNumber, int colNumber) {
        Color disc = curPlayerTurn == Constant.FIRST_PLAYER_TURN ? ColorP.COLOR_FIRST_PLAYER : ColorP.COLOR_SECOND_PLAYER;
        matrixPanel[rowNumber][colNumber].setColor(disc);
    }

    /*When the game is over, turn off the game buttons, and update the panel information.
     * The function calls 2 the performance of these operations.*/
    private void GameEnd(boolean isTie) {
        updateGameEndedMessage(isTie);
        setEnableAllButtons(false);
    }

    /*A function resets the game matrix in which the results are stored,
     * buttons on the broad, starting player and resets the game class of game logic.*/
    private void gameReset() {
        for (int row = 0; row < matrixPanel.length; row++) {//Clearing the discs on the board.
            for (int col = 0; col < matrixPanel[0].length; col++) {
                matrixPanel[row][col].setColor(ColorP.BACKGROUND);
            }
        }
        game.reset();
        game = new Game(Constant.ROWS, Constant.COLS);
        curPlayerTurn = Constant.FIRST_PLAYER_TURN;
        updateNextTurnMessage();
        setEnableAllButtons(true);
    }

    /*The function turns the game buttons on or off, depending on the parameter it receives.*/
    private void setEnableAllButtons(boolean enableBth){
        for(JButton button : buttons)
            button.setEnabled(enableBth);
    }
}
