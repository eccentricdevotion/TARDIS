/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.builders;

import me.eccentric_nz.tardis.enumeration.COMPASS;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.planets.TARDISBiome;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

/**
 * Data class for building the tardis exterior.
 *
 * @author eccentric_nz
 */
public class MaterialisationData {

    private TARDISBiome tardisBiome;
    private COMPASS direction;
    private Location location;
    private OfflinePlayer player;
    private boolean outside;
    private boolean submarine;
    private boolean siege;
    private int tardisId;
    private SpaceTimeThrottle throttle;

    public TARDISBiome getTardisBiome() {
        return tardisBiome;
    }

    public void setTardisBiome(TARDISBiome tardisBiome) {
        this.tardisBiome = tardisBiome;
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

    public int getTardisId() {
        return tardisId;
    }

    public void setTardisId(int tardisId) {
        this.tardisId = tardisId;
    }

    public SpaceTimeThrottle getThrottle() {
        return throttle;
    }

    public void setThrottle(SpaceTimeThrottle throttle) {
        this.throttle = throttle;
    }
}
