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
package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.artron.ArtronAbandoned;
import me.eccentric_nz.TARDIS.artron.actions.ArtronChargeAction;
import me.eccentric_nz.TARDIS.artron.actions.ArtronInitAction;
import me.eccentric_nz.TARDIS.artron.actions.ArtronTransferAction;
import me.eccentric_nz.TARDIS.control.TARDISPowerButton;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ArtronRightClick {

    private final TARDIS plugin;

    public ArtronRightClick(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(int id, Player player, Location location) {
        // get tardis data
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            if (tardis.getPreset().equals(ChameleonPreset.JUNK)) {
                return;
            }
            boolean abandoned = tardis.isAbandoned();
            int current_level = tardis.getArtronLevel();
            boolean init = tardis.isTardisInit();
            boolean lights = tardis.isLightsOn();
            Material item = player.getInventory().getItemInMainHand().getType();
            Material full = Material.valueOf(plugin.getArtronConfig().getString("full_charge_item"));
            Material cell = plugin.getFigura().getShapedRecipes().get("Artron Storage Cell").getResult().getType();
            // determine key item
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
            String key;
            boolean hasPrefs = false;
            if (rsp.resultSet()) {
                hasPrefs = true;
                key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("preferences.key");
            } else {
                key = plugin.getConfig().getString("preferences.key");
            }
            if (item.equals(full) || item.equals(cell)) {
                if (!init) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NO_INIT");
                    return;
                }
                new ArtronChargeAction(plugin).add(player, item, full, location, current_level, id);
            } else if (item.equals(Material.getMaterial(key))) {
                // has the TARDIS been initialised?
                if (!init) {
                    new ArtronInitAction(plugin).powerUp(location, tardis, player, id);
                } else {
                    if (abandoned) {
                        // transfer ownership to the player who clicked
                        new ArtronAbandoned(plugin).claim(player, id, location, tardis);
                    } else if (plugin.getConfig().getBoolean("allow.power_down")) {
                        // toggle power
                        new TARDISPowerButton(plugin, id, player, tardis.getPreset(), tardis.isPoweredOn(), tardis.isHidden(), lights, player.getLocation(), current_level, tardis.getSchematic().getLights()).clickButton();
                    }
                }
            } else if (player.isSneaking()) {
                if (!init) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NO_INIT");
                    return;
                }
                new ArtronTransferAction(plugin).add(current_level, rsp.getArtronLevel(), player, id, hasPrefs);
            } else {
                if (!init) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NO_INIT");
                    return;
                }
                // just tell us how much energy we have
                plugin.getMessenger().sendArtron(player, id, 0);
            }
        }
    }
}
