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
package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.models.ThrottleModel;
import me.eccentric_nz.TARDIS.database.InteractionStateSaver;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class ThrottleInteraction {

    private final TARDIS plugin;

    public ThrottleInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(Player player, Interaction interaction, int id) {
        if (plugin.getTrackerKeeper().getFlight().containsKey(player.getUniqueId())) {
            return;
        }
        String uuid = player.getUniqueId().toString();
        int unary = interaction.getPersistentDataContainer().getOrDefault(plugin.getUnaryKey(), PersistentDataType.INTEGER, -1);
        // get current throttle setting
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
        if (rsp.resultSet()) {
            /*
            states should go up to fastest, then back down to slowest, rather than cycling
            NORMAL => 4
            FASTER => 3
            RAPID => 2
            WARP => 1
             */
            int delay = rsp.getThrottle() + unary;
            if (delay < 1) {
                delay = 2;
                unary = 1;
            }
            if (delay > 4) {
                delay = 3;
                unary = -1;
            }
            if (delay != 4 && plugin.getConfig().getBoolean("difficulty.system_upgrades")) {
                switch (delay) {
                    case 3 -> {
                        if (!new SystemUpgradeChecker(plugin).has(uuid, SystemTree.FASTER)) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Faster");
                            delay = 4;
                        }
                    }
                    case 2 -> {
                        if (!new SystemUpgradeChecker(plugin).has(uuid, SystemTree.RAPID)) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Rapid");
                            delay = 4;
                        }
                    }
                    case 1 -> {
                        if (!new SystemUpgradeChecker(plugin).has(uuid, SystemTree.WARP)) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Warp");
                            delay = 4;
                        }
                    }
                    default -> { }
                }
            }
            // save unary value in the interaction PDC
            interaction.getPersistentDataContainer().set(plugin.getUnaryKey(), PersistentDataType.INTEGER, unary);
            String throttle = TARDISStringUtils.capitalise(SpaceTimeThrottle.getByDelay().get(delay).toString());
            // update player prefs
            HashMap<String, Object> wherer = new HashMap<>();
            wherer.put("uuid", uuid);
            HashMap<String, Object> setr = new HashMap<>();
            setr.put("throttle", delay);
            plugin.getQueryFactory().doUpdate("player_prefs", setr, wherer);
            new InteractionStateSaver(plugin).write("THROTTLE", delay, id);
            plugin.getMessenger().announceRepeater(player, throttle);
            // set custom model data for throttle item display
            UUID model = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
            if (model != null) {
                ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(model);
                new ThrottleModel().setState(display, delay);
            }
        }
    }
}
