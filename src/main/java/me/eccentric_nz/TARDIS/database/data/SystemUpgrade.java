/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.database.data;

import me.eccentric_nz.TARDIS.upgrades.SystemTree;

import java.util.HashMap;

public class SystemUpgrade {

    private final int artronLevel;
    private final HashMap<SystemTree, Boolean> upgrades = new HashMap<>();

    public SystemUpgrade(int artronLevel, boolean architecture, boolean chameleon, boolean rooms, boolean desktop, boolean feature, boolean saves, boolean monitor, boolean forceField, boolean tools, boolean locator, boolean telepathic, boolean stattenheimRemote, boolean navigation, boolean distance1, boolean distance2, boolean distance3, boolean interDimension, boolean throttle, boolean faster, boolean rapid, boolean warp, boolean flight) {
        this.artronLevel = artronLevel;
        this.upgrades.put(SystemTree.UPGRADE_TREE, true);
        this.upgrades.put(SystemTree.ARCHITECTURE, architecture);
        this.upgrades.put(SystemTree.CHAMELEON_CIRCUIT, chameleon);
        this.upgrades.put(SystemTree.ROOM_GROWING, rooms);
        this.upgrades.put(SystemTree.DESKTOP_THEME, desktop);
        this.upgrades.put(SystemTree.FEATURE, feature);
        this.upgrades.put(SystemTree.SAVES, saves);
        this.upgrades.put(SystemTree.MONITOR, monitor);
        this.upgrades.put(SystemTree.FORCE_FIELD, forceField);
        this.upgrades.put(SystemTree.TOOLS, tools);
        this.upgrades.put(SystemTree.TARDIS_LOCATOR, locator);
        this.upgrades.put(SystemTree.TELEPATHIC_CIRCUIT , telepathic);
        this.upgrades.put(SystemTree.STATTENHEIM_REMOTE, stattenheimRemote);
        this.upgrades.put(SystemTree.NAVIGATION, navigation);
        this.upgrades.put(SystemTree.DISTANCE_1, distance1);
        this.upgrades.put(SystemTree.DISTANCE_2, distance2);
        this.upgrades.put(SystemTree.DISTANCE_3 , distance3);
        this.upgrades.put(SystemTree.INTER_DIMENSIONAL_TRAVEL, interDimension);
        this.upgrades.put(SystemTree.THROTTLE, throttle);
        this.upgrades.put(SystemTree.FASTER, faster);
        this.upgrades.put(SystemTree.RAPID, rapid);
        this.upgrades.put(SystemTree.WARP, warp);
        this.upgrades.put(SystemTree.EXTERIOR_FLIGHT, flight);
    }

    public int getArtronLevel() {
        return artronLevel;
    }

    public HashMap<SystemTree, Boolean> getUpgrades() {
        return upgrades;
    }
}
