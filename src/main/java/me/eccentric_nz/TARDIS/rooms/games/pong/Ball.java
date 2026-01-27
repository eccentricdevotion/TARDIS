package me.eccentric_nz.TARDIS.rooms.games.pong;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.rooms.games.GameUtils;

public class Ball {

    static final int WIDTH = 29;
    static final int HEIGHT = 16;
    int x, y;
    Pong pong;
    int vx, vy;

    public Ball(int y, Pong pong) {
        this.x = 14;
        this.y = y;
        this.pong = pong;
        int[] d = GameUtils.DIRECTIONS[TARDISConstants.RANDOM.nextInt(GameUtils.DIRECTIONS.length)];
        vx = d[0];
        vy = d[1];
    }

    public void update() {
        if (x == 0) {
            TARDIS.plugin.debug("tardis wins point");
            // tardis wins point
            pong.reset();
            return;
        }
        if (x == 28) {
            TARDIS.plugin.debug("player wins point");
            // player wins point
            pong.reset();
            return;
        }
        pong.setChar(y, x, GameChar.space);
        int nextX = x + vx;
        int nextY = y + vy;
        // walls
        if (nextX < 0 || nextX >= WIDTH) {
            vx = -vx;
        }
        if (nextY < 0 || nextY >= HEIGHT) {
            vy = -vy;
        }
        nextX = x + vx;
        nextY = y + vy;
        // paddles
        PaddlePosition position = detectPaddle(y, nextX);
        if (position == PaddlePosition.NONE) {
            x = nextX;
        } else {
//            TARDIS.plugin.debug(position.toString());
            applyPaddleBounce(position);
            pong.setChar(y, x, GameChar.paddle);
        }
        // update grid
        pong.setChar(nextY, nextX, GameChar.ball);
        if (x == 13 || x == 15) {
            // reset net
            for (int n = 1; n < 14; n += 2) {
                pong.setChar(n, 14, GameChar.net);
            }
        }
        y = nextY;
    }

    private PaddlePosition detectPaddle(int y, int x) {
        if (pong.getCANVAS()[y][x] == GameChar.paddle) {
            if (y == 0 || pong.getCANVAS()[y - 1][x] == GameChar.space) {
                return x == 1 ? PaddlePosition.PLAYER_TOP : PaddlePosition.TARDIS_TOP;
            } else if (y == 15 || y + 1 < 16 && pong.getCANVAS()[y + 1][x] == GameChar.space) {
                return x == 1 ? PaddlePosition.PLAYER_BOTTOM : PaddlePosition.TARDIS_BOTTOM;
            } else {
                return x == 1 ? PaddlePosition.PLAYER_MIDDLE : PaddlePosition.TARDIS_MIDDLE;
            }
        } else {
            return PaddlePosition.NONE;
        }
    }

    private void applyPaddleBounce(PaddlePosition position) {
        vy = switch (position) {
            case PLAYER_TOP, TARDIS_TOP -> -1;
            case PLAYER_BOTTOM, TARDIS_BOTTOM -> 1;
            default -> 0;
        };
        // always reverse horizontal direction
        vx = -vx;
        // loop-breaking randomness
        if (TARDISConstants.RANDOM.nextBoolean()) {
            if (vy == 0) {
                vy = TARDISConstants.RANDOM.nextBoolean() ? 1 : -1;
            } else {
                vy = -vy;
            }
        }
    }
}
