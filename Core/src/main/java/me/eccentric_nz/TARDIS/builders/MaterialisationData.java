/*
 * Copyright (C) 2024 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetExteriorLightLevel;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

/**
 * Data class for building the TARDIS exterior.
 *
 * @author eccentric_nz
 */
public class MaterialisationData {

    private COMPASS direction;
    private Location location;
    private OfflinePlayer player;
    private boolean outside;
    private boolean submarine;
    private boolean siege;
    private int tardisID;
    private int exteriorLampLevel;
    private SpaceTimeThrottle throttle;
    private boolean particles;

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
        // set exterior lamp
        ResultSetExteriorLightLevel rs = new ResultSetExteriorLightLevel(TARDIS.plugin, tardisID);
        if (rs.resultSet()) {
            exteriorLampLevel = LightLevel.exterior_level[rs.getLevel()];
        } else {
            exteriorLampLevel = 8;
        }
    }

    public int getExteriorLampLevel() {
        return exteriorLampLevel;
    }

    public SpaceTimeThrottle getThrottle() {
        return throttle;
    }

    public void setThrottle(SpaceTimeThrottle throttle) {
        this.throttle = throttle;
    }

    public boolean hasParticles() {
        return particles;
    }

    public void setParticles(boolean particles) {
        this.particles = particles;
    }
}
