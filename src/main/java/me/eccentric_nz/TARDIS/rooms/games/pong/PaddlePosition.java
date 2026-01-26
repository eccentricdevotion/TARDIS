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
            case PLAYER_TOP, PLAYER_BOTTOM -> {
                return angle < Math.PI / 2 ? -0.1f : 0.1f;
            }
            case TARDIS_BOTTOM, TARDIS_TOP -> {
                return angle < Math.PI / 2 ? 0.1f : -0.1f;
            }
            default -> { // PLAYER_MIDDLE, TARDIS MIDDLE
                return 0;
            }
        }
    }
}
