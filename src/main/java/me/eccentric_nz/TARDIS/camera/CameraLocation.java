package me.eccentric_nz.TARDIS.camera;

import org.bukkit.GameMode;
import org.bukkit.Location;

public class CameraLocation {

    private final Location location;
    private final GameMode gameMode;
    private final int id;
    private final boolean forceLoaded;

    public CameraLocation(Location location, GameMode gameMode, int id, boolean forceLoaded) {
        this.location = location;
        this.gameMode = gameMode;
        this.id = id;
        this.forceLoaded = forceLoaded;
    }

    public Location getLocation() {
        return location;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public int getId() {
        return id;
    }

    public boolean isForceLoaded() {
        return forceLoaded;
    }
}
