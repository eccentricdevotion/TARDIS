package me.eccentric_nz.TARDIS.rooms.games.pong;

import me.eccentric_nz.TARDIS.TARDISConstants;

public class Lines {

    /**
     * Playing canvas is 15 x 29 pixels. The extra line displays the score.
     */
    public static char[][] CANVAS = new char[16][29];

    static {
        for (int r = 0; r < 16; r++) {
            for (int c = 0; c < 29; c++) {
                CANVAS[r][c] = GameChar.space;
            }
        }
        // net
        for (int n = 1; n < 14; n += 2) {
            CANVAS[n][14] = GameChar.net;
        }
        // paddles
        for (int p = 6; p < 9; p++) {
            CANVAS[p][1] = GameChar.paddle;
            CANVAS[p][27] = GameChar.paddle;
        }
        // ball - random position between 2 and 13
        CANVAS[TARDISConstants.RANDOM.nextInt(2,14)][14] = GameChar.ball;
    }
}
