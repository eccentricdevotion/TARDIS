package me.eccentric_nz.TARDIS.enumeration;

public enum BIND {

    SAVE(0, 3),
    CAVE(1, 2),
    HIDE(1, 2),
    HOME(1, 2),
    MAKE_HER_BLUE(1, 2),
    OCCUPY(1, 2),
    REBUILD(1, 2),
    PLAYER(2, 3),
    AREA(3, 3),
    BIOME(4, 3),
    CHAMELEON(5, 3),
    TRANSMAT(6, 2);

    private final int type;
    private final int args;

    BIND(int type, int args) {
        this.type = type;
        this.args = args;
    }

    public int getType() {
        return type;
    }

    public int getArgs() {
        return args;
    }
}
