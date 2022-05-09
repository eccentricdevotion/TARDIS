package me.eccentric_nz.TARDIS.portal;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Location;

import java.util.UUID;

public class CastData {

    final Location interior;
    final Location exterior;
    final COMPASS direction;
    final UUID rotor;

    public CastData(Location interior, Location exterior, COMPASS direction, UUID rotor) {
        this.interior = interior;
        this.exterior = exterior;
        this.direction = direction;
        this.rotor = rotor;
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

    public UUID getRotor() {
        return rotor;
    }
}
