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
package me.eccentric_nz.TARDIS.commands.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.travel.TARDISCaveFinder;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class CaveCommand {

    private final TARDIS plugin;

    public CaveCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean action(Player player, int id) {
        if (!TARDISPermission.hasPermission(player, "tardis.timetravel.cave")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NO_PERM_CAVE");
            return true;
        }
        if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.TELEPATHIC_CIRCUIT)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Telepathic Circuit");
            return true;
        }
        // check for telepathic circuit
        if (plugin.getConfig().getBoolean("difficulty.circuits") && !plugin.getUtils().inGracePeriod(player, true)) {
            TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
            if (!tcc.hasTelepathic()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TELEPATHIC_CIRCUIT");
                return true;
            }
        }
        // find a cave
        Location cave = new TARDISCaveFinder(plugin).searchCave(player, id);
        if (cave == null) {
            plugin.getMessenger().sendStatus(player, "CAVE_NOT_FOUND");
            return true;
        }
        // check respect
        if (!plugin.getPluginRespect().getRespect(cave, new Parameters(player, Flag.getDefaultFlags()))) {
            if (plugin.getConfig().getBoolean("travel.no_destination_malfunctions")) {
                plugin.getTrackerKeeper().getMalfunction().put(id, true);
            } else {
                return true;
            }
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("world", cave.getWorld().getName());
        set.put("x", cave.getBlockX());
        set.put("y", cave.getBlockY());
        set.put("z", cave.getBlockZ());
        set.put("submarine", 0);
        HashMap<String, Object> tid = new HashMap<>();
        tid.put("tardis_id", id);
        plugin.getQueryFactory().doSyncUpdate("next", set, tid);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_LOADED", "Cave", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.CAVE));
        plugin.getTrackerKeeper().getRescue().remove(id);
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            new TARDISLand(plugin, id, player).exitVortex();
            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.CAVE, id));
        }
        return true;
    }
}
