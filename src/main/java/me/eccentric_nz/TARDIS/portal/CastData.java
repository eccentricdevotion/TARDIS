package me.eccentric_nz.TARDIS.portal;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Location;

public class CastData {

    final Location interior;
    final Location exterior;
    final COMPASS direction;
    // TODO Time Rotor?

    public CastData(Location interior, Location exterior, COMPASS direction) {
        this.interior = interior;
        this.exterior = exterior;
        this.direction = direction;
    }

    public Location getInterior() {
        return interior;
    }

    public Location getExterior() {
        return exterior;
    }

    public COMPASS getDirection() {
        return direction;
    }
}
