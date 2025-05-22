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
import me.eccentric_nz.TARDIS.TARDISCache;
import me.eccentric_nz.TARDIS.console.ConsoleInteraction;
import me.eccentric_nz.TARDIS.console.models.ButtonModel;
import me.eccentric_nz.TARDIS.control.actions.LightSwitchAction;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LightSwitchInteraction {

    private final TARDIS plugin;

    public LightSwitchInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void toggle(int id, Player player, Interaction interaction) {
        if (plugin.getTrackerKeeper().getFlight().containsKey(player.getUniqueId())) {
            return;
        }
        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
            return;
        }
//        HashMap<String, Object> where = new HashMap<>();
//        where.put("tardis_id", id);
//        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
//        if (!rs.resultSet()) {
        Tardis tardis = TARDISCache.BY_ID.get(id);
        if (tardis == null) {
            return;
        }
//        Tardis tardis = rs.getTardis();
        if (!tardis.isLightsOn() && plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPoweredOn()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN");
            return;
        }
        // set custom model data for light switch item display
        UUID uuid = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
        if (uuid != null) {
            ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(uuid);
            new ButtonModel().setState(display, plugin, ConsoleInteraction.LIGHT_SWITCH);
        }
        new LightSwitchAction(plugin, id, tardis.isLightsOn(), player, tardis.getSchematic().getLights()).flickSwitch();
    }
}
