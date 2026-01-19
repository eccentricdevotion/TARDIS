package me.eccentric_nz.TARDIS.rooms.games.pong;

import me.eccentric_nz.TARDIS.TARDISConstants;

public class Lines {

    public static char[][] CANVAS = new char[15][29];

    static {
        for (int r = 0; r < 15; r++) {
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
        // ball
        CANVAS[TARDISConstants.RANDOM.nextBoolean() ? 6 : 8][14] = GameChar.ball;
    }
}
