package me.eccentric_nz.tardisweepingangels.monsters.ood;

public enum OodColour {

    BLACK(0),
    BLUE(6),
    BROWN(12);

    private final int step;

    OodColour(int step) {
        this.step = step;
    }

    public int getStep() {
        return step;
    }
}
