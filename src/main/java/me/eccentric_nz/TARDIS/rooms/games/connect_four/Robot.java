package me.eccentric_nz.TARDIS.rooms.games.connect_four;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Material;

import java.util.ArrayList;

/**
 * This class represents the TARDIS.
 * The TARDIS needs access to the board to be able to make strategic moves.
 */
public class Robot {

    /**
     * The board object that the robot can access.
     */
    private final Board board;

    /**
     * Constructor for the PlayerRobot class.
     *
     * @param board The board object that the robot can access.
     */
    public Robot(Board board) {
        this.board = board;
    }

    /**
     * This method is used to choose a column for the robot player.
     * The robot player will try to find a winning move, a blocking move, or choose a random move.
     * This method implements the abstract method from the Player class.
     *
     * @return The column chosen by the robot player.
     */
    public int chooseColumn() {
        // find if there is a winning move with 3 tokens in line
        int winningMove = findWinningMove();
        if (winningMove != -1) {
            TARDIS.plugin.debug("I am about to win!");
            return winningMove;
        }
        // find if the opponent has 3 tokens in line and block it
        int blockMove = blockOpponentWinningMove();
        if (blockMove != -1) {
            TARDIS.plugin.debug("I am blocking you haha");
            return blockMove;
        }
        // if no strategic move is found, random move
        return randomMove();
    }

    /**
     * This method is used to block the opponent's winning move.
     * It checks if the opponent can win by entering a token in a specific column.
     * Hence, it represents a brute force approach to block the opponent, by checking
     * all the available options. If no winning move for the opponent is found, the
     * method returns -1.
     *
     * @return The column to block the opponent's winning move or -1.
     */
    private int blockOpponentWinningMove() {
        // checking the opponents moves for a win
        for (int col = 0; col < 7; col++) {
            if (board.isValidMove(col)) {
                int row = board.getFirstAvailableRow(col);
                board.setToken(row, col, Material.RED_CONCRETE_POWDER);
                if (areFourInLine()) {
                    board.removeToken(row, col);
                    return col;
                }
                board.removeToken(row, col);
            }
        }
        return -1;
    }

    /**
     * This method is used to find a winning move for the robot player.
     * It checks if the robot can win by entering a token in a specific column.
     * Hence, it represents a brute force approach to find a winning move, by checking
     * all the available options. If a winning move is found, the method returns the column,
     * otherwise it returns -1.
     *
     * @return The column to make a winning move or -1.
     */
    private int findWinningMove() {
        // checking all the available moves for a win
        for (int col = 0; col < 7; col++) {
            if (board.isValidMove(col)) {
                int row = board.getFirstAvailableRow(col);
                board.setToken(row, col, Material.YELLOW_CONCRETE_POWDER);
                if (areFourInLine()) {
                    board.removeToken(row, col);
                    return col;
                }
                board.removeToken(row, col);
            }
        }
        return -1;
    }

    /**
     * This method is used to generate a random move for the robot player.
     * The robot player will choose a random column to place its token.
     * The method selects a random column among the available columns.
     * If a column is full, it is not available for selection.
     *
     * @return The random column chosen by the robot player.
     */
    private int randomMove() {
        TARDIS.plugin.debug("I am moving at random...");
        // create a list of available columns
        ArrayList<Integer> availableColumns = new ArrayList<>();
        for (int col = 0; col < 7; col++) {
            if (!board.isColumnFull(col)) {
                availableColumns.add(col);
            }
        }
        // handle the empty available columns list
        if (availableColumns.isEmpty()) {
            TARDIS.plugin.debug("Oh no... I have no available columns to use.");
            return -1;
        }
        // randomly choose one of the available columns
        int randomIndex = TARDISConstants.RANDOM.nextInt(availableColumns.size());
        return availableColumns.get(randomIndex);
    }

    /**
     * This method is used to check if there are four tokens in line.
     * It checks if there are four tokens in line vertically, horizontally, or diagonally.
     *
     * @return True if there are four tokens in line, false otherwise.
     */
    private boolean areFourInLine() {
        return (board.checkVertically() || board.checkHorizontally() || board.checkDiagonally());
    }
}
