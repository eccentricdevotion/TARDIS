package me.eccentric_nz.TARDIS.database.data;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Location;

public record Current(Location location, COMPASS direction, boolean submarine) {
}
