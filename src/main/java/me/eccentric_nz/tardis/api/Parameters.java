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
package me.eccentric_nz.tardis.api;

import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.enumeration.Flag;
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
    private boolean respectWorldBorder = false;
    private boolean respectFactions = false;
    private boolean respectGriefPrevention = false;
    private boolean respectRedProtect = false;
    private boolean respectTowny = false;
    private boolean respectWorldGuard = false;
    private boolean spaceTardis = false;
    private CardinalDirection direction;

    /**
     * Data holder for parameters related to finding a TARDIS Time Travel location
     *
     * @param player the player to check the parameters for
     * @param flags  a list of flags to check
     */
    public Parameters(Player player, List<Flag> flags) {
        this.player = player;
        for (Flag f : flags) {
            switch (f) {
                case MESSAGE_PLAYER:
                    messagePlayer = true;
                    break;
                case PERMS_AREA:
                    permsArea = true;
                    break;
                case PERMS_NETHER:
                    permsNether = true;
                    break;
                case PERMS_THEEND:
                    permsTheEnd = true;
                    break;
                case RESPECT_FACTIONS:
                    respectFactions = true;
                    break;
                case RESPECT_GRIEFPREVENTION:
                    respectGriefPrevention = true;
                    break;
                case RESPECT_REDPROTECT:
                    respectRedProtect = true;
                    break;
                case RESPECT_TOWNY:
                    respectTowny = true;
                    break;
                case RESPECT_WORLDBORDER:
                    respectWorldBorder = true;
                    break;
                case RESPECT_WORLDGUARD:
                    respectWorldGuard = true;
                    break;
                case SPACE_TARDIS:
                    spaceTardis = true;
                    break;
                default:
                    break;
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
    public boolean respectWorldBorder() {
        return respectWorldBorder;
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
     * Check if we should be respecting Grief Prevention claims
     *
     * @return the value of config option 'preferences.respect_grief_prevention'
     */
    public boolean respectGriefPrevention() {
        return respectGriefPrevention;
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
    public boolean respectWorldGuard() {
        return respectWorldGuard;
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
    public CardinalDirection getDirection() {
        return direction;
    }

    /**
     * Sets the TARDIS exterior direction
     *
     * @param direction the direction to set
     */
    public void setDirection(CardinalDirection direction) {
        this.direction = direction;
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