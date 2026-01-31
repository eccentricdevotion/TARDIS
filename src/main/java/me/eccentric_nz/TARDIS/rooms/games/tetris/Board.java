package me.eccentric_nz.TARDIS.rooms.games.tetris;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    private final int width;
    private final int height;
    private final byte[][] board;
    private final Game game;

    Board(int width, int height, Game game) {
        this.width = width;
        this.height = height;
        board = new byte[height][width];
        this.game = game;
    }

    public boolean isInside(int x, int y) {
        return 0 <= x && x < width && 0 <= y && y < height;
    }

    private boolean isEmpty(int x, int y) {
        return y < 0 || isInside(x, y) && board[y][x] == 0;
    }

    public boolean isBlocked(Pieces t) {
        for (int y = 0; y < t.getShape().length; y++) {
            for (int x = 0; x < t.getShape()[y].length; x++) {
                if (t.getShape()[y][x] != 0) {
                    if (!isEmpty(t.getXOffset() + x, t.getYOffset() + y)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isPlacable(Pieces t) {
        t.move(0, 1);
        if (isBlocked(t)) {
            t.move(0, -1);
            return true;
        }
        return false;
    }

    public void place(Pieces t) {
        for (int y = 0; y < t.getShape().length; y++) {
            for (int x = 0; x < t.getShape()[y].length; x++) {
                if (t.getShape()[y][x] != 0) {
                    board[t.getYOffset() + y][t.getXOffset() + x] = t.getShape()[y][x];
                    game.playSound(GameSound.DROP);
                }
            }
        }
    }

    private boolean isFull(int line) {
        for (int x = 0; x < width; x++)
            if (board[line][x] == 0) return false;
        return true;
    }

    private void moveDown(int line, int nbLines) {
        for (int x = 0; x < width; x++) {
            board[line + nbLines][x] = board[line][x];
            board[line][x] = 0;
        }
    }

    public int clearFullLines() {
        int cleared = 0;
        List<Integer> linesToClear = new ArrayList<>();
        List<Integer> linesToMoveDown = new ArrayList<>();
        Map<Integer, Integer> downMap = new HashMap<>();
        for (int y = height - 1; y >= 0; y--) {
            if (isFull(y)) {
                linesToClear.add(y);
                cleared++;
            } else if (cleared > 0) {
                linesToMoveDown.add(y);
                downMap.put(y, cleared);
            }
        }
        if (cleared > 0) {
            new BukkitRunnable() {
                int x = width / 2;
                @Override
                public void run() {
                    for (int y : linesToClear) {
                        board[y][x] = 0;
                        board[y][width - 1 - x] = 0;
                        game.drawBoard();
                    }
                    x--;
                    if (x < 0) {
                        linesToMoveDown.forEach(y -> moveDown(y, downMap.get(y)));
                        cancel();
                    }
                }
            }.runTaskTimer(game.getPlugin(), 0, 2);
        }
        return cleared;
    }

    public void clear() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                board[y][x] = 0;
            }
        }
    }

    public void close() {
        new BukkitRunnable() {
            int y = 0;
            @Override
            public void run() {
                for (int x = 0; x < width; x++)
                    board[y][x] = (byte) (y % 3 + 1);

                game.drawBoard();
                y++;
                if (y >= height) cancel();
            }
        }.runTaskTimer(game.getPlugin(), 0, 4);
    }

    public int get(int x, int y) {
        return board[y][x];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                s.append(board[y][x]).append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }
}
