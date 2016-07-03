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
package me.eccentric_nz.TARDIS.api;

import java.util.List;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.FLAG;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class Parameters {

    public Parameters(Player p, List<FLAG> flags) {
        this.player = p;
        for (FLAG f : flags) {
            switch (f) {
                case MESSAGE_PLAYER:
                    this.messagePlayer = true;
                    break;
                case PERMS_AREA:
                    this.permsArea = true;
                    break;
                case PERMS_NETHER:
                    this.permsNether = true;
                    break;
                case PERMS_THEEND:
                    this.permsTheEnd = true;
                    break;
                case RESPECT_FACTIONS:
                    this.respectFactions = true;
                    break;
                case RESPECT_GRIEFPREVENTION:
                    this.respectGreifPrevention = true;
                    break;
                case RESPECT_TOWNY:
                    this.respectTowny = true;
                    break;
                case RESPECT_WORLDBORDER:
                    this.repectWorldBorder = true;
                    break;
                case RESPECT_WORLDGUARD:
                    this.respectWorldguard = true;
                    break;
                case SPACE_TARDIS:
                    this.spaceTardis = true;
                    break;
                case LIMIT:
                    this.limit = true;
                    break;
                default:
                    break;
            }
        }
    }

    private boolean messagePlayer = false;
    private boolean permsArea = false;
    private boolean permsNether = false;
    private boolean permsTheEnd = false;
    private boolean permsWorld = false;
    private boolean repectWorldBorder = false;
    private boolean respectFactions = false;
    private boolean respectGreifPrevention = false;
    private boolean respectTowny = false;
    private boolean respectWorldguard = false;
    private boolean spaceTardis = false;
    private boolean limit = false;
    private COMPASS compass;
    private Player player;
    private int range;
    private int[] centre = null;

    public boolean messagePlayer() {
        return messagePlayer;
    }

    public boolean permsArea() {
        return permsArea;
    }

    public boolean permsNether() {
        return permsNether;
    }

    public boolean permsTheEnd() {
        return permsTheEnd;
    }

    public boolean permsWorld() {
        return permsWorld;
    }

    public boolean repectWorldBorder() {
        return repectWorldBorder;
    }

    public boolean respectFactions() {
        return respectFactions;
    }

    public boolean respectGreifPrevention() {
        return respectGreifPrevention;
    }

    public boolean respectTowny() {
        return respectTowny;
    }

    public boolean respectWorldguard() {
        return respectWorldguard;
    }

    public boolean spaceTardis() {
        return spaceTardis;
    }

    public boolean limit() {
        return limit;
    }

    public COMPASS getCompass() {
        return compass;
    }

    public void setCompass(COMPASS compass) {
        this.compass = compass;
    }

    public Player getPlayer() {
        return player;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int[] getCentre() {
        return centre;
    }

    public void setCentre(int x, int z) {
        this.centre = new int[2];
        centre[0] = x;
        centre[1] = z;
    }
}
