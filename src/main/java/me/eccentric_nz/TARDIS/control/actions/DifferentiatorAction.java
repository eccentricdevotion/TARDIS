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
package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * The relativity differentiator is a component inside the TARDIS that allows it to travel in space.
 *
 * @author eccentric_nz
 */
public class DifferentiatorAction {

    private final TARDIS plugin;
    public DifferentiatorAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void bleep(Block block, int id, Player player) {
        int mode;
        if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.EXTERIOR_FLIGHT)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Exterior Flight");
            mode = 0;
        } else {
            Comparator comparator = (Comparator) block.getBlockData();
            String sound = comparator.getMode().equals(Comparator.Mode.SUBTRACT) ? "differentiator_off" : "differentiator_on";
            TARDISSounds.playTARDISSound(block.getLocation(), sound);
            // save control state
            mode = comparator.getMode().equals(Comparator.Mode.SUBTRACT) ? 0 : 1;
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("secondary", mode);
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("type", 47);
        plugin.getQueryFactory().doUpdate("controls", set, where);
    }
}
