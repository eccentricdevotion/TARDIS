package me.eccentric_nz.TARDIS.rooms.games.pong;


import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.rooms.games.GameUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Ball {

    private final char[][] CANVAS;
    int by;
    int bx = 14;
    int originY;
    int originX = 14;
    int oldY;
    int oldX = 14;
    double angle = TARDISConstants.RANDOM.nextDouble(-Math.PI/6, Math.PI/6);

    public Ball(char[][] CANVAS, int starty) {
        this.CANVAS = CANVAS;
        this.by = starty;
        this.originY = starty;
        this.oldY = starty;
    }

    public static List<Point> findLine(int x0, int y0, int x1, int y1) {
        List<Point> line = new ArrayList<>();
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1; // step direction for x
        int sy = y0 < y1 ? 1 : -1; // step direction for y
        int err = dx - dy; // initial error/decision parameter
        while (true) {
            line.add(new Point(x0, y0));
            if (x0 == x1 && y0 == y1) {
                break;
            }
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
        return line;
    }

    public void move() {
        if (by == 15) {
            // flip angle
            angle = GameUtils.getReflectedAngle(angle, Border.BOTTOM);
            originY = 15;
            originX = bx;
        }
        if (by < 0) {
            // flip angle
            angle = GameUtils.getReflectedAngle(angle, Border.TOP);
            originY = 0;
            originX = bx;
        }
        if (bx == 0) {
            TARDIS.plugin.debug("tardis wins point");
            // tardis wins point
            return;
        }
        if (bx == 29) {
            TARDIS.plugin.debug("player wins point");
            // player wins point
            return;
        }
        Point point = positionForAngle(angle, originY, originX);
        int ny = point.y;
        int nx = point.x;
        PaddlePosition paddle = detectPaddle(ny, nx);
        if (paddle != PaddlePosition.NONE) {
            // flip angle
            switch (paddle) {
                case PLAYER_TOP, PLAYER_MIDDLE, PLAYER_BOTTOM -> angle = GameUtils.getReflectedAngle(angle, Border.LEFT) + paddle.getDeflection(angle);
                case TARDIS_TOP, TARDIS_MIDDLE, TARDIS_BOTTOM -> angle = GameUtils.getReflectedAngle(angle, Border.RIGHT) + paddle.getDeflection(angle);
            }
            // recalculate path
            point = positionForAngle(angle, originY, originX);
        }
        oldY = by;
        oldX = bx;
        by = point.y;
        bx = point.x;
        CANVAS[by][bx] = GameChar.ball;
        CANVAS[oldY][oldX] = GameChar.space;
    }

    private Point positionForAngle(double angle, int oy, int ox) {
        int ey = angle < Math.PI ? 0 : 15;
        int ex = (int) (ox + (ey - oy / Math.tan(angle)));
        List<Point> line = findLine(ox, oy, ex, ey);
        int i = 0;
        for (Point point : line) {
            i++;
            if (point.x == bx && point.y == by) {
                break;
            }
        }
        return line.get(i);
    }

    private PaddlePosition detectPaddle(int y, int x) {
        TARDIS.plugin.debug(by + "," + bx + " / " + y + "," + x + " = " + CANVAS[y][x]);
        if (CANVAS[y][x] == GameChar.paddle) {
            TARDIS.plugin.debug("will hit paddle");
            if (y == 0 || CANVAS[y - 1][x] == GameChar.space) {
                TARDIS.plugin.debug("top paddle");
                return x == 1 ? PaddlePosition.PLAYER_TOP : PaddlePosition.TARDIS_TOP;
            } else if (y == 15 || y + 1 < 16 && CANVAS[y + 1][x] == GameChar.space) {
                TARDIS.plugin.debug("bottom paddle");
                return x == 1 ? PaddlePosition.PLAYER_BOTTOM : PaddlePosition.TARDIS_BOTTOM;
            } else {
                TARDIS.plugin.debug("middle paddle");
                return x == 1 ? PaddlePosition.PLAYER_MIDDLE : PaddlePosition.TARDIS_MIDDLE;
            }
        } else {
            return PaddlePosition.NONE;
        }
    }
}
