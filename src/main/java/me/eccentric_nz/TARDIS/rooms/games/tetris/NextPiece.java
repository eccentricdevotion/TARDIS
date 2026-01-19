package me.eccentric_nz.TARDIS.rooms.games.tetris;

import java.util.Random;

public class NextPiece {

    private final Random random = new Random();
    private Pieces next;

    NextPiece() {
        this.next = random();
    }

    private Pieces random() {
        return Pieces.values()[random.nextInt(Pieces.values().length)];
    }

    public Pieces getAndUpdate() {
        Pieces t = next;
        next = random();
        if (next == t) {
            next = random();
        }
        return t;
    }

    public Pieces getNext() {
        return next;
    }

    public void reset() {
        next = random();
    }
}
