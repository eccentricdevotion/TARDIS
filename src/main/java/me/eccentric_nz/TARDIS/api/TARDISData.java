/*
 *  Copyright 2015 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.api;

import org.bukkit.Location;

/**
 *
 * @author eccentric_nz
 */
public class TARDISData {

    private final Location location;
    private final String console;
    private final String chameleon;
    private final String powered;
    private final String siege;

    public TARDISData(Location location, String console, String chameleon, String powered, String siege) {
        this.location = location;
        this.console = console;
        this.chameleon = chameleon;
        this.powered = powered;
        this.siege = siege;
    }

    public Location getLocation() {
        return location;
    }

    public String getConsole() {
        return console;
    }

    public String getChameleon() {
        return chameleon;
    }

    public String getPowered() {
        return powered;
    }

    public String getSiege() {
        return siege;
    }
}
