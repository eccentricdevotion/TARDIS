package me.eccentric_nz.TARDIS.camera;

import org.bukkit.Location;

public class CameraLocation {

    private final Location location;
    private final int id;
    private final boolean forceLoaded;

    public CameraLocation(Location location, int id, boolean forceLoaded) {
        this.location = location;
        this.id = id;
        this.forceLoaded = forceLoaded;
    }

    public Location getLocation() {
        return location;
    }

    public int getId() {
        return id;
    }

    public boolean isForceLoaded() {
        return forceLoaded;
    }
}
