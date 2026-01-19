package me.eccentric_nz.TARDIS.rooms.games.tictactoe;

import javax.swing.*;
import java.awt.*;

public class NoughtsAndCrosses {

    private final int[][] winPos = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {2, 4, 6}, {0, 4, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}};
    private final boolean[] used = {false, false, false, false, false, false, false, false, false};
    private final String[] board = {"1", "1", "1", "1", "1", "1", "1", "1", "1"};
    int[] compUsed = {10, 10, 10, 10, 10, 10, 10, 10, 10};
    int c = 0;
    int turn = 0;
    int delay = 500;
    //Button Start
    JButton btn1 = new JButton("");
    JButton btn2 = new JButton("");
    JButton btn3 = new JButton("");
    JButton btn4 = new JButton("");
    JButton btn5 = new JButton("");
    JButton btn6 = new JButton("");
    JButton btn7 = new JButton("");
    JButton btn8 = new JButton("");
    JButton btn9 = new JButton("");
    private JFrame frame;
    private String startGame = "X";
    private int count = 0;
    //Button End

    public void playerTurn() {
        if (startGame.equalsIgnoreCase("X")) {
            startGame = "0";
        } else {
            startGame = "X";
        }
        checkWinner();
    }

    public void turn(JButton tempB, int n) {
        System.out.println("Turn : " + turn);
        tempB.setText(startGame);
        if (startGame.equalsIgnoreCase("X")) {
            tempB.setForeground(Color.RED);
            board[n] = "X";
        } else {
            tempB.setForeground(Color.BLUE);
            board[n] = "0";
        }
        //System.out.println(board[n]);
        playerTurn();
        count++;
        turn++;
        if (turn % 2 == 1) {
            Timer timer = new Timer(delay, e -> computersTurn());
            timer.setRepeats(false);
            timer.start();
        }
    }


    public void checkWinner() {
        int chk = 0;
        for (int i = 0; i < 8; i++) {
            if ((board[winPos[i][0]].equals("X")) && (board[winPos[i][0]].equals(board[winPos[i][1]])) && (board[winPos[i][1]].equals(board[winPos[i][2]]))) {
                //x has won
                System.out.println("X has won");
                JOptionPane.showMessageDialog(frame, "X has won!");
                chk = 1;
                for (int j = 0; j < 9; j++) {
                    used[j] = true;
                }
            } else if ((board[winPos[i][0]].equals("0")) && (board[winPos[i][0]].equals(board[winPos[i][1]])) && (board[winPos[i][1]].equals(board[winPos[i][2]]))) {
                //o has won
                System.out.println("0 has won");
                JOptionPane.showMessageDialog(frame, "0 has won!");
                chk = 1;
                for (int j = 0; j < 9; j++) {
                    used[j] = true;
                }
            }
        }
        if (count == 8 && chk == 0) {
            //draw
            System.out.println("DRAW");
            chk = 1;
            JOptionPane.showMessageDialog(frame, "It's a  Draw!");
            for (int j = 0; j < 9; j++) {
                used[j] = true;
            }
        }
    }


    public int computersTurn() {
        int rand = (int) (Math.random() * 9);
        boolean test = true;
        if (c < 9) {
            if (!used[4]) {
                rand = 4;
            } else if ((used[0] && used[1]) && !used[2] && ((board[0].equals("X") && board[1].equals("X")) || (board[0].equals("0") && board[1].equals("0")))) {
                rand = 2;
            } else if ((used[1] && used[2]) && !used[0] && ((board[1] == "X" && board[2] == "X") || (board[1] == "0" && board[2] == "0"))) {
                rand = 0;
            } else if ((used[0] && used[2]) && !used[1] && ((board[0] == "X" && board[2] == "X") || (board[0] == "0" && board[2] == "0"))) {
                rand = 1;
            } else if ((used[3] && used[4]) && !used[5] && ((board[3] == "X" && board[4] == "X") || (board[3] == "0" && board[4] == "0"))) {
                rand = 5;
            } else if ((used[4] && used[5]) && !used[3] && ((board[4] == "X" && board[5] == "X") || (board[4] == "0" && board[5] == "0"))) {
                rand = 3;
            } else if ((used[3] && used[5]) && !used[4] && ((board[3] == "X" && board[5] == "X") || (board[3] == "0" && board[5] == "0"))) {
                rand = 4;
            } else if ((used[6] && used[7]) && !used[8] && ((board[6] == "X" && board[7] == "X") || (board[6] == "0" && board[7] == "0"))) {
                rand = 8;
            } else if ((used[7] && used[8]) && !used[6] && ((board[7] == "X" && board[8] == "X") || (board[7] == "0" && board[8] == "0"))) {
                rand = 6;
            } else if ((used[6] && used[8]) && !used[7] && ((board[6] == "X" && board[8] == "X") || (board[6] == "0" && board[8] == "0"))) {
                rand = 7;
            } else if ((used[0] && used[3]) && !used[6] && ((board[0] == "X" && board[3] == "X") || (board[0] == "0" && board[3] == "0"))) {
                rand = 6;
            } else if ((used[3] && used[6]) && !used[0] && ((board[3] == "X" && board[6] == "X") || (board[3] == "0" && board[6] == "0"))) {
                rand = 0;
            } else if ((used[0] && used[6]) && !used[3] && ((board[0] == "X" && board[6] == "X") || (board[0] == "0" && board[6] == "0"))) {
                rand = 3;
            } else if ((used[1] && used[4]) && !used[7] && ((board[1] == "X" && board[4] == "X") || (board[1] == "0" && board[4] == "0"))) {
                rand = 7;
            } else if ((used[4] && used[7]) && !used[1] && ((board[4] == "X" && board[7] == "X") || (board[4] == "0" && board[7] == "0"))) {
                rand = 1;
            } else if ((used[1] && used[7]) && !used[4] && ((board[1] == "X" && board[7] == "X") || (board[1] == "0" && board[7] == "0"))) {
                rand = 4;
            } else if ((used[2] && used[5]) && !used[8] && ((board[2] == "X" && board[5] == "X") || (board[2] == "0" && board[5] == "0"))) {
                rand = 8;
            } else if ((used[5] && used[8]) && !used[2] && ((board[5] == "X" && board[8] == "X") || (board[5] == "0" && board[8] == "0"))) {
                rand = 2;
            } else if ((used[2] && used[8]) && !used[5] && ((board[2] == "X" && board[8] == "X") || (board[2] == "0" && board[8] == "0"))) {
                rand = 5;
            } else if ((used[0] && used[4]) && !used[8] && ((board[0] == "X" && board[4] == "X") || (board[0] == "0" && board[4] == "0"))) {
                rand = 8;
            } else if ((used[4] && used[8]) && !used[0] && ((board[4] == "X" && board[8] == "X") || (board[4] == "0" && board[8] == "0"))) {
                rand = 0;
            } else if ((used[0] && used[8]) && !used[4] && ((board[0] == "X" && board[8] == "X") || (board[0] == "0" && board[8] == "0"))) {
                rand = 4;
            } else if ((used[2] && used[4]) && !used[6] && ((board[2] == "X" && board[4] == "X") || (board[2] == "0" && board[4] == "0"))) {
                rand = 6;
            } else if ((used[4] && used[6]) && !used[2] && ((board[4] == "X" && board[6] == "X") || (board[4] == "0" && board[6] == "0"))) {
                rand = 2;
            } else if ((used[2] && used[6]) && !used[4] && ((board[2] == "X" && board[6] == "X") || (board[2] == "0" && board[6] == "0"))) {
                rand = 4;
            } else {
                while ((used[rand] || test)) {
                    test = false;
                    rand = (int) (Math.random() * 9);
                    for (int i : compUsed) {
                        if (rand == i) {
                            test = true;
                        }
                    }
                    if (!test) {
                        compUsed[c] = rand;
                    }
                }
            }
            compUsed[c] = rand;
            c++;
            used[rand] = true;
            checkWinner();
            switch (rand) {
                case 0 -> turn(btn1, 0);
                case 1 -> turn(btn2, 1);
                case 2 -> turn(btn3, 2);
                case 3 -> turn(btn4, 3);
                case 4 -> turn(btn5, 4);
                case 5 -> turn(btn6, 5);
                case 6 -> turn(btn7, 6);
                case 7 -> turn(btn8, 7);
                case 8 -> turn(btn9, 8);
            }
            return rand;
        }
        return 0;
    }
}
