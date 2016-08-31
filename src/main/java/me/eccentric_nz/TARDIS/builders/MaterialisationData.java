/*
 * Copyright (C) 2016 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.builders;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Biome;

/**
 * Data class for building the TARDIS exterior.
 *
 * @author eccentric_nz
 */
public class MaterialisationData {

    private final TARDIS plugin;
    private final String uuid;
    private Biome biome;
    private COMPASS direction;
    private Location location;
    private Material lamp = Material.REDSTONE_LAMP_OFF;
    private OfflinePlayer player;
    private boolean outside;
    private boolean submarine;
    private boolean siege;
    private int tardisID;

    public MaterialisationData(TARDIS plugin, String uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        // get player preferences
        setPlayerDefaults(this.uuid);
    }

    public Biome getBiome() {
        return biome;
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
    }

    public COMPASS getDirection() {
        return direction;
    }

    public void setDirection(COMPASS direction) {
        this.direction = direction;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Material getLamp() {
        return lamp;
    }

    public void setLamp(Material lamp) {
        this.lamp = lamp;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public void setPlayer(OfflinePlayer player) {
        this.player = player;
    }

    public boolean isOutside() {
        return outside;
    }

    public void setOutside(boolean outside) {
        this.outside = outside;
    }

    public boolean isSubmarine() {
        return submarine;
    }

    public void setSubmarine(boolean submarine) {
        this.submarine = submarine;
    }

    public boolean isSiege() {
        return siege;
    }

    public void setSiege(boolean siege) {
        this.siege = siege;
    }

    public int getTardisID() {
        return tardisID;
    }

    public void setTardisID(int tardisID) {
        this.tardisID = tardisID;
    }

    public void setPlayerDefaults(String uuid) {
        HashMap<String, Object> wherep = new HashMap<String, Object>();
        wherep.put("uuid", uuid);
        final ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherep);
        if (rsp.resultSet()) {
            lamp = rsp.getLamp();
        }
    }
}
