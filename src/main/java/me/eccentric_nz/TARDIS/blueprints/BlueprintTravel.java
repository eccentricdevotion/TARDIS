package me.eccentric_nz.TARDIS.blueprints;

public enum BlueprintTravel {

    BIOME("tardis.timetravel.biome"),
    CAVE("tardis.timetravel.cave"),
    LOCATION("tardis.timetravel.location"),
    NETHER("tardis.nether"),
    PLAYER("tardis.timetravel.player"),
    END("tardis.end"),
    VILLAGE("tardis.timetravel.village");

    private final String permission;

    BlueprintTravel(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
