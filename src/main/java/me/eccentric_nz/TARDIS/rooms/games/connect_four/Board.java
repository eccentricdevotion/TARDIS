package me.eccentric_nz.TARDIS.rooms.games.connect_four;

import org.bukkit.Material;

/**
 * This class represents the board for the game.
 * It is represented as a 2D array of Tokens with 6 rows and 7 columns.
 * The structure of the board is represented by the following table:
 * <pre>
 *         0   1   2   3   4   5   6
 *       _____________________________
 *     0 |   |   |   |   |   |   |   |
 *     1 |   |   |   |   |   |   |   |
 *     2 |   |   |   |   |   |   |   |
 *     3 |   |   |   |   |   |   |   |
 *     4 |   |   |   |   |   |   |   |
 *     5 |   |   |   |   |   |   |   |
 *
 */

public class Board {

    /** A 2D array of Tokens representing the board. */
    private final Material[][] board = new Material[6][7];

    /**
     * Constructor for the Board class.
     * It initialises the board with null values.
     */
    public Board() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                board[i][j] = null;
            }
        }
    }

    // Methods
    /**
     * This method returns the entire board.
     * @return the board
     */
    public Material[][] getBoard() {
        return board;
    }

    /**
     * This method sets a token at a specific position on the board.
     * @param row the row of the position.
     * @param col the column of the position.
     * @param token the token to set.
     */
    public void setToken(int row, int col, Material token) {
        board[row][col] = token;
    }

    /**
     * This method removes a token at a specific position on the board by
     * setting the item in that location to null.
     * @param row the row of the position.
     * @param col the column of the position.
     */
    public void removeToken(int row, int col) {
        board[row][col] = null;
    }

    /**
     * This method returns the first available row in a column,
     * that is the row containing null.
     * @param col the column to check.
     * @return the first available row in the column.
     */
    public int getFirstAvailableRow(int col) {
        int maxRow = -1;
        // checking from the bottom of the column
        for (int row = 6 - 1; row >= 0; row--) {
            if (board[row][col] == null) {
                if (row >= maxRow) {
                    maxRow = row;
                }
            }
        }
        return maxRow;
    }

    /**
     * This method checks if a column is full.
     * A column is full if the top row contains a token.
     * @param column the column to check.
     * @return true if the column is full, false otherwise.
     */
    public boolean isColumnFull(int column) {
        return board[0][column] != null;
    }

    /**
     * This method checks if all columns are full.
     * @return true if all columns are full, false otherwise.
     */
    public boolean areAllColumnsFull() {
        for (int i = 0; i < 7; i++) {
            if (!isColumnFull(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method checks if a move is valid.
     * A move is valid if the column is within the bounds of the board,
     * this check is mostly necessary due to user input in command line,
     * and if the chosen column is not full.
     *
     * @param column the column to check.
     * @return true if the move is valid, false otherwise.
     */
    public boolean isValidMove(int column) {
        return (column >= 0 && column < 7 && !isColumnFull(column));
    }

    /**
     * This method checks if there are four tokens in a row horizontally.
     * It uses the equals method of the Token class to compare the tokens.
     *
     * @return true if there are four tokens in a row, false otherwise.
     */
    public boolean checkHorizontally() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7 - 3; j++) {
                if (horizontalCondition(i,j)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method checks if there are four tokens in a row horizontally.
     * @param i the row index.
     * @param j the column index.
     * @return  true if there are four tokens in a row, false otherwise.
     */
    private boolean horizontalCondition(int i, int j) {
        return (board[i][j] != null && board[i][j].equals(board[i][j + 1]) &&
                board[i][j].equals(board[i][j + 2]) && board[i][j].equals(board[i][j + 3]));
    }

    /**
     * This method checks if there are four tokens in a row vertically.
     * It uses the equals method of the Token class to compare the tokens.
     *
     * @return true if there are four tokens in a row, false otherwise.
     */
    public boolean checkVertically() {
        for (int i = 0; i < 6 - 3; i++) {
            for (int j = 0; j < 7; j++) {
                if (verticalCondition(i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method checks if there are four tokens in a row vertically.
     * @param i the row index.
     * @param j the column index.
     * @return  true if there are four tokens in a row, false otherwise.
     */
    private boolean verticalCondition(int i, int j) {
        return (board[i][j] != null && board[i][j].equals(board[i + 1][j]) &&
                board[i][j].equals(board[i + 2][j]) && board[i][j].equals(board[i + 3][j]));
    }

    /**
     * This method checks if there are four tokens in a row diagonally.
     * It uses the equals method of the Token class to compare the tokens.
     *
     * @return true if there are four tokens in a row, false otherwise.
     */
    public boolean checkDiagonally() {
        for (int i = 0; i < 6 - 3; i++) {
            for (int j = 0; j < 7 - 3; j++) {
                if (diagonalConditionLeftRight(i, j)) {
                    return true;
                }
            }
        }
        for (int i = 0; i < 6 - 3; i++) {
            for (int j = 3; j < 7; j++) {
                if (diagonalConditionRightLeft(i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method checks if there are four tokens in a row diagonally from left to right.
     * @param i the row index.
     * @param j the column index.
     * @return  true if there are four tokens in a row, false otherwise.
     */
    private boolean diagonalConditionLeftRight(int i, int j) {
        return (board[i][j] != null && board[i][j].equals(board[i + 1][j + 1]) &&
                board[i][j].equals(board[i + 2][j + 2]) && board[i][j].equals(board[i + 3][j + 3]));
    }

    /**
     * This method checks if there are four tokens in a row diagonally from right to left.
     * @param i the row index.
     * @param j the column index.
     * @return  true if there are four tokens in a row, false otherwise.
     */
    private boolean diagonalConditionRightLeft(int i, int j) {
        return (board[i][j] != null && board[i][j].equals(board[i + 1][j - 1]) &&
                board[i][j].equals(board[i + 2][j - 2]) && board[i][j].equals(board[i + 3][j - 3]));
    }
}
