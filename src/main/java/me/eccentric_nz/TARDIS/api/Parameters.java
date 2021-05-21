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

    public Parameters(Player p, List<Flag> flags) {
        player = p;
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
                    respectGreifPrevention = true;
                    break;
                case RESPECT_REDPROTECT:
                    respectRedProtect = true;
                    break;
                case RESPECT_TOWNY:
                    respectTowny = true;
                    break;
                case RESPECT_WORLDBORDER:
                    repectWorldBorder = true;
                    break;
                case RESPECT_WORLDGUARD:
                    respectWorldguard = true;
                    break;
                case SPACE_TARDIS:
                    spaceTardis = true;
                    break;
                default:
                    break;
            }
        }
    }

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

    public boolean repectWorldBorder() {
        return repectWorldBorder;
    }

    public boolean respectFactions() {
        return respectFactions;
    }

    public boolean respectGreifPrevention() {
        return respectGreifPrevention;
    }

    public boolean respectRedProtect() {
        return respectRedProtect;
    }

    public boolean respectTowny() {
        return respectTowny;
    }

    public boolean respectWorldguard() {
        return respectWorldguard;
    }

    boolean spaceTardis() {
        return spaceTardis;
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
}
