package me.eccentric_nz.TARDIS.maze;

import me.eccentric_nz.TARDIS.TARDISConstants;

import java.util.ArrayDeque;
import java.util.Random;

public class TARDISMazeGenerator {

    private final boolean[][] field; // The field we are cutting into
    private final ArrayDeque<Integer[]> tracker; // stack to trace location
    private final int rows; // number of rows in the representative 2r+1 array
    private final int cols; // number of cols in the representative 2c+1 array
    private final int act_rows; // number of rows in the real maze
    private final int act_cols; // number of columns in the real maze

    public TARDISMazeGenerator() {
        act_rows = 5;
        act_cols = 5;
        rows = 11;
        cols = 11;
        // initializes the field to proper size
        field = new boolean[rows][cols];
        // initialize tracker to ample size
        tracker = new ArrayDeque<>(rows * cols);

        // setting the inside to filled
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                field[i][j] = true;
            }
        }
    }

    private static int[] mix(int[] arr) {
        Random rand = new Random(); // a random number generator
        int temp; // a temp variable for swapping
        int place1; // one location to be swapped
        int place2;
        // shuffle seven times
        for (int i = 0; i < 7; i++) {
            // pick two random indices of the array
            place1 = rand.nextInt(arr.length);
            place2 = rand.nextInt(arr.length);
            // swap the two random indices
            temp = arr[place1];
            arr[place1] = arr[place2];
            arr[place2] = temp;
        }
        return arr;
    }

    public void makeMaze() {

        // an array that gets all the possible motions to which can be cut into
        int[] cut_order;
        // a variable that is positive when there are items in the stack,
        // and negative when the stack of locations is empty
        int not_done;
        // pick a random location to start building the maze
        start();
        // initializes the looping condition now that there is a location (the start) in the stack
        not_done = stackPeek();
        // loop while there are locations in the stack
        while (not_done > 0) {
            // get the available locations
            cut_order = canCut();
            // if there are directions to cut into
            if (cut_order[0] != 0) {
                // shuffle the available directions take the first direction to be cut into after shuffling and
                // cut into it - like shuffling a deck of cards and drawing the top card
                cut_order = mix(cut_order);
                switch (cut_order[0]) {
                    case 1:
                        cutNextUp();
                        break;
                    case 2:
                        cutNextRight();
                        break;
                    case 3:
                        cutNextDown();
                        break;
                    case 4:
                        cutNextLeft();
                        break;
                    default:
                        break;
                }
            } else {
                // if there are no directions to cut, back up one location
                back();
            }
            // are the more locations in stack?
            not_done = stackPeek();
        }
    }

    public boolean[][] getMaze() {
        return field;
    }

    // starts making the maze at a random location
    private void start() {
        // a temp array to access the stack
        Integer[] loc = new Integer[2];
        // pick a random start location - the location must be odd to be valid
        int start_row_index = TARDISConstants.RANDOM.nextInt(act_rows - 1) * 2 + 1;
        int start_col_index = TARDISConstants.RANDOM.nextInt(act_cols - 1) * 2 + 1;
        loc[0] = start_row_index;
        loc[1] = start_col_index;
        tracker.addFirst(loc);
        // clears the start point
        field[start_row_index][start_col_index] = false;
    }

    // check to see if there are more locations in the tracker
    private int stackPeek() {
        if (tracker.peekFirst() == null) {
            return -1;
        }
        return 1;
    }

    private int[] canCut() {
        // an array of the directions able to be cut
        int[] cut = new int[4];
        // number of directions that can be cut into
        int place = 0;
        // check to see if up is a valid direction
        if (canUp() != 0) {
            cut[place] = canUp();
            place++;
        }
        // check to see if right is a valid direction
        if (canRight() != 0) {
            cut[place] = canRight();
            place++;
        }
        // check to see if down is a valid direction
        if (canDown() != 0) {
            cut[place] = canDown();
            place++;
        }
        // check to see if left is a valid direction
        if (canLeft() != 0) {
            cut[place] = canLeft();
            place++;
        }
        // return array full of 0 is there are no valid directions
        if (place == 0) {
            for (int i = 0; i < 4; i++) {
                cut[i] = 0;
            }
            return cut;
        } else {
            // otherwise trim the array to the right length and return it
            int[] cancut = new int[place];
            for (int i = 0; i < place; i++) {
                cancut[i] = cut[i];
            }
            return cancut;
        }
    }

    private int canUp() {
        // current location
        Integer[] current = tracker.peekFirst();
        // next location
        int nxt_row = current[0] - 2;
        int nxt_col = current[1];
        // if next location is in the array and not already cut, can cut it
        if (nxt_row < 0 || field[nxt_row][nxt_col] == false) {
            return 0;
        } else {
            // 1 corresponds to up
            return 1;
        }
    }

    private int canDown() {
        // current location
        Integer[] current = tracker.peekFirst();
        // next location
        int nxt_row = current[0] + 2;
        int nxt_col = current[1];
        // if the next location is in the array and not already cut, can cut it
        if (nxt_row > rows - 1 || field[nxt_row][nxt_col] == false) {
            return 0;
        } else {
            // 3 corresponds to down
            return 3;
        }
    }

    private int canRight() {
        // current location
        Integer[] current = tracker.peekFirst();
        // next location
        int nxt_row = current[0];
        int nxt_col = current[1] + 2;
        // if the next location is in the array and not already cut, can cut it
        if (nxt_col > cols - 1 || field[nxt_row][nxt_col] == false) {
            return 0;
        } else {
            // 2 corresponds to right
            return 2;
        }
    }

    private int canLeft() {
        // current location
        Integer[] current = tracker.peekFirst();
        // next location
        int nxt_row = current[0];
        int nxt_col = current[1] - 2;
        // if next location is in the array and not already cut, can cut it
        if (nxt_col < 0 || field[nxt_row][nxt_col] == false) {
            return 0;
        } else {
            // 4 corresponds to left
            return 4;
        }
    }

    private int cutNextUp() {
        // gets the current location
        Integer[] current = tracker.peekFirst();
        // temp var to access the stack
        Integer[] loc = new Integer[2];
        // the location of the next row index
        int nxt_row = current[0] - 2;
        // Location of next col index
        int nxt_col = current[1];
        // sets the next index and the wall between it to blank
        field[current[0] - 1][current[1]] = false;
        field[current[0] - 2][current[1]] = false;
        loc[0] = nxt_row;
        loc[1] = nxt_col;
        // adds the new index to the stack
        tracker.addFirst(loc);
        return 1;
    }

    private int cutNextDown() {
        // gets the current location
        Integer[] current = tracker.peekFirst();
        // temp variable to access stack
        Integer[] loc = new Integer[2];
        // Location of next row and col
        int nxt_row = current[0] + 2;
        int nxt_col = current[1];
        // clears the next index and the wall between it
        field[current[0] + 1][current[1]] = false;
        field[current[0] + 2][current[1]] = false;
        loc[0] = nxt_row;
        loc[1] = nxt_col;
        // adds new index to stack
        tracker.addFirst(loc);
        return 1;
    }

    private int cutNextRight() {
        // gets the current location
        Integer[] current = tracker.peekFirst();
        // dummy variable to access stack
        Integer[] loc = new Integer[2];
        // location of next row and col
        int nxt_row = current[0];
        int nxt_col = current[1] + 2;
        // clears the necessary locations
        field[current[0]][current[1] + 1] = false;
        field[current[0]][current[1] + 2] = false;
        loc[0] = nxt_row;
        loc[1] = nxt_col;
        // adds index to stack
        tracker.addFirst(loc);
        return 1;
    }

    private int cutNextLeft() {
        // gets current location
        Integer[] current = tracker.peekFirst();
        // temp variable to access stack
        Integer[] loc = new Integer[2];
        // location of next row and col
        int nxt_row = current[0];
        int nxt_col = current[1] - 2;
        // clears the necessary locations
        field[current[0]][current[1] - 1] = false;
        field[current[0]][current[1] - 2] = false;
        loc[0] = nxt_row;
        loc[1] = nxt_col;
        // adds new index to stack
        tracker.addFirst(loc);
        return 1;
    }

    private void back() {
        tracker.removeFirst();
    }
}
