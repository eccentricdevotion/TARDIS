package me.eccentric_nz.TARDIS.blueprints;

public enum BlueprintSonic {

    ARROW("tardis.sonic.arrow"),
    BIO("tardis.sonic.bio"),
    DIAMOND("tardis.sonic.diamond"),
    EMERALD("tardis.sonic.emerald"),
    FREEZE("tardis.sonic.freeze"),
    IGNITE("tardis.sonic.ignite"),
    KNOCKBACK("tardis.sonic.knockback"),
    PAINT("tardis.sonic.paint"),
    PLANT("tardis.sonic.plant"),
    REDSTONE("tardis.sonic.redstone"),
    SILKTOUCH("tardis.sonic.silktouch"),
    SORT("tardis.sonic.sort"),
    STANDARD("tardis.sonic.standard");

    private final String permission;

    BlueprintSonic(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
