package me.eccentric_nz.TARDIS.rooms.games;

import org.bukkit.Location;
import org.bukkit.event.Listener;

public record ArcadeData(Location backup, boolean allowFlight, Listener listener, int id) {
}
