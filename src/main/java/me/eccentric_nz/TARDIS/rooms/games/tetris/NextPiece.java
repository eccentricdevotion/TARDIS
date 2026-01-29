package me.eccentric_nz.TARDIS.rooms.games.tetris;

import me.eccentric_nz.TARDIS.TARDISConstants;

public class NextPiece {

    private Pieces next;

    NextPiece() {
        this.next = random();
    }

    private Pieces random() {
        return Pieces.values()[TARDISConstants.RANDOM.nextInt(Pieces.values().length)];
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
