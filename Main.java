package question1;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        /* Creates the window in the main panel of the game.*/
        JFrame frame = new JFrame("Four In Row");
        frame.add(new GameBroad());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 650);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

