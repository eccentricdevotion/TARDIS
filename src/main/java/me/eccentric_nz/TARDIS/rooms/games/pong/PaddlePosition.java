package me.eccentric_nz.TARDIS.rooms.games.pong;

public enum PaddlePosition {

    PLAYER_TOP,
    PLAYER_MIDDLE,
    PLAYER_BOTTOM,
    TARDIS_TOP,
    TARDIS_MIDDLE,
    TARDIS_BOTTOM,
    NONE;

    public double getDeflection(double angle) {
        switch (this) {
            case PLAYER_TOP, TARDIS_BOTTOM -> {
                return 0.15f;
            }
            case PLAYER_BOTTOM, TARDIS_TOP -> {
                return -0.15f;
            }
            default -> { // PLAYER_MIDDLE, TARDIS MIDDLE
                return 0;
            }
        }
    }
}
