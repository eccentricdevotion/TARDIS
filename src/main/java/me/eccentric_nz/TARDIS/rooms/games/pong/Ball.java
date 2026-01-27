package me.eccentric_nz.TARDIS.rooms.games.pong;


import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.rooms.games.GameUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Ball {

    Pong pong;
    int by;
    int bx = 14;
    int originY;
    int originX = 14;
    int oldY;
    int oldX = 14;
    double limit = Math.PI / 6;
    double angle = TARDISConstants.RANDOM.nextDouble(-limit, limit);

    public Ball(Pong pong, int starty) {
        this.pong = pong;
        this.by = starty;
        this.originY = starty;
        this.oldY = starty;
        if (angle < 0) {
            angle = (Math.PI * 2) + angle;
        }
    }

    public static List<Point> findLine(int ox, int oy, int ex, int ey) {
        List<Point> line = new ArrayList<>();
        int dx = Math.abs(ex - ox);
        int dy = Math.abs(ey - oy);
        int sx = ox < ex ? 1 : -1; // step direction for x
        int sy = oy < ey ? 1 : -1; // step direction for y
        int err = dx - dy; // initial error/decision parameter
        while (true) {
            line.add(new Point(ox, oy));
            if (ox == ex && oy == ey) {
                break;
            }
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                ox += sx;
            }
            if (e2 < dx) {
                err += dx;
                oy += sy;
            }
        }
        return line;
    }

    public void move() {
        Point point = positionForAngle(angle, originY, originX);
        int ny = point.y;
        Border border = detectBorder(ny);
        if (border == Border.BOTTOM) {
            // flip angle
            angle = GameUtils.getReflectedAngle(angle, Border.BOTTOM);
            originY = 15;
            originX = bx;
        }
        if (border == Border.TOP) {
            // flip angle
            angle = GameUtils.getReflectedAngle(angle, Border.TOP);
            originY = 0;
            originX = bx;
        }
        if (bx == 0) {
            TARDIS.plugin.debug("tardis wins point");
            // tardis wins point
            pong.reset();
            return;
        }
        if (bx == 13 || bx == 15) {
            // reset net
            for (int n = 1; n < 14; n += 2) {
                pong.getCANVAS()[n][14] = GameChar.net;
            }
        }
        if (bx == 28) {
            TARDIS.plugin.debug("player wins point");
            // player wins point
            pong.reset();
            return;
        }
        point = positionForAngle(angle, originY, originX);
        ny = point.y;
        int nx = point.x;
        PaddlePosition paddle = detectPaddle(ny, nx);
        if (paddle != PaddlePosition.NONE) {
            // flip angle
            switch (paddle) {
                case PLAYER_TOP, PLAYER_MIDDLE, PLAYER_BOTTOM -> angle = GameUtils.getReflectedAngle(angle, Border.LEFT) + paddle.getDeflection(angle);
                case TARDIS_TOP, TARDIS_MIDDLE, TARDIS_BOTTOM -> angle = GameUtils.getReflectedAngle(angle, Border.RIGHT) + paddle.getDeflection(angle);
            }
            originX = bx;
            originY = by;
            // recalculate path
            point = positionForAngle(angle, originY, originX);
        }
        oldY = by;
        oldX = bx;
        by = Math.max(point.y, 0);
        bx = Math.min(point.x, 28);
        pong.getCANVAS()[by][bx] = GameChar.ball;
        pong.getCANVAS()[oldY][oldX] = GameChar.space;
    }

    private Point positionForAngle(double angle, int oy, int ox) {
        TARDIS.plugin.debug(String.format("%.2f", angle));
        int ey = angle > Math.PI ? 0 : 15;
        int ex = (int) (ox + (ey - oy / Math.tan(angle)));
        List<Point> line = findLine(ox, oy, ex, ey);
        int i = 1;
        for (Point point : line) {
            i++;
            if (point.x == bx && point.y == by) {
                break;
            }
        }
        return i < line.size() ? line.get(i) : line.getFirst();
    }

    private PaddlePosition detectPaddle(int y, int x) {
        TARDIS.plugin.debug(by + "," + bx + " / " + y + "," + x);
        if (y < pong.getCANVAS().length && x < pong.getCANVAS()[y].length) {
            TARDIS.plugin.debug(" = " + pong.getCANVAS()[y][x]);
        } else {
            return PaddlePosition.NONE;
        }
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

    private Border detectBorder(int y) {
        return switch (y) {
            case 0 -> Border.TOP;
            case 15 -> Border.BOTTOM;
            default -> Border.NONE;
        };
    }
}
