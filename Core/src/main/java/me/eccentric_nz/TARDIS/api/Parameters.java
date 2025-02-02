/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.api;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class Parameters {

    private final Player player;
    private boolean messagePlayer = false;
    private boolean permsArea = false;
    private boolean permsNether = false;
    private boolean permsTheEnd = false;
    private boolean repectWorldBorder = false;
    private boolean respectFactions = false;
    private boolean respectGreifPrevention = false;
    private boolean respectRedProtect = false;
    private boolean respectTowny = false;
    private boolean respectWorldguard = false;
    private boolean spaceTardis = false;
    private COMPASS compass;

    /**
     * Data holder for parameters related to finding a TARDIS time Travel
     * location
     *
     * @param player the player to check the parameters for
     * @param flags a list of flags to check
     */
    public Parameters(Player player, List<Flag> flags) {
        this.player = player;
        for (Flag f : flags) {
            switch (f) {
                case MESSAGE_PLAYER -> messagePlayer = true;
                case PERMS_AREA -> permsArea = true;
                case PERMS_NETHER -> permsNether = true;
                case PERMS_THEEND -> permsTheEnd = true;
                case RESPECT_FACTIONS -> respectFactions = true;
                case RESPECT_GRIEFPREVENTION -> respectGreifPrevention = true;
                case RESPECT_REDPROTECT -> respectRedProtect = true;
                case RESPECT_TOWNY -> respectTowny = true;
                case RESPECT_WORLDBORDER -> repectWorldBorder = true;
                case RESPECT_WORLDGUARD -> respectWorldguard = true;
                case SPACE_TARDIS -> spaceTardis = true;
                default -> {
                }
            }
        }
    }

    /**
     * Whether to message the player
     *
     * @return true if we should message the player
     */
    public boolean messagePlayer() {
        return messagePlayer;
    }

    /**
     * Check whether the player has permission for a TARDIS area
     *
     * @return true if the player has permission
     */
    public boolean permsArea() {
        return permsArea;
    }

    /**
     * Check whether the player has permission to travel to the Nether
     *
     * @return true if the player has permission
     */
    public boolean permsNether() {
        return permsNether;
    }

    /**
     * Check whether the player has permission to travel to The End
     *
     * @return true if the player has permission
     */
    public boolean permsTheEnd() {
        return permsTheEnd;
    }

    /**
     * Whether to check the world border
     *
     * @return true if we're checking world borders
     */
    public boolean repectWorldBorder() {
        return repectWorldBorder;
    }

    /**
     * Check if we should be respecting Faction areas
     *
     * @return the value of config option 'preferences.respect_factions'
     */
    public boolean respectFactions() {
        return respectFactions;
    }

    /**
     * Check if we should be respecting Greif Prevention claims
     *
     * @return the value of config option 'preferences.respect_grief_prevention'
     */
    public boolean respectGreifPrevention() {
        return respectGreifPrevention;
    }

    /**
     * Check if we should be respecting RedProtect areas
     *
     * @return the value of config option 'preferences.respect_red_protect'
     */
    public boolean respectRedProtect() {
        return respectRedProtect;
    }

    /**
     * Check if we should be respecting Towny areas
     *
     * @return the value of config option 'preferences.respect_towny'
     */
    public boolean respectTowny() {
        return respectTowny;
    }

    /**
     * Check if we should be respecting WorldGuard regions
     *
     * @return the value of config option 'preferences.respect_worldguard'
     */
    public boolean respectWorldguard() {
        return respectWorldguard;
    }

    /**
     * Check if there is space for a TARDIS to land
     *
     * @return true if there is space
     */
    boolean spaceTardis() {
        return spaceTardis;
    }

    /**
     * Gets the TARDIS exterior direction
     *
     * @return the direction
     */
    public COMPASS getCompass() {
        return compass;
    }

    /**
     * Sets the TARDIS exterior direction
     *
     * @param compass the direction to set
     */
    public void setCompass(COMPASS compass) {
        this.compass = compass;
    }

    /**
     * Gets the player these parameters apply to
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }
}
