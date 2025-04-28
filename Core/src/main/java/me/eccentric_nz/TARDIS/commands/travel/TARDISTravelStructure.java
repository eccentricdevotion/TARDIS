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
package me.eccentric_nz.TARDIS.commands.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.travel.TARDISStructureLocation;
import me.eccentric_nz.TARDIS.travel.TARDISStructureTravel;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISTravelStructure {

    private final TARDIS plugin;

    public TARDISTravelStructure(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean action(Player player, String[] args, int id) {
        if (!plugin.getConfig().getBoolean("allow.village_travel")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NO_VILLAGE");
            return true;
        }
        if (!TARDISPermission.hasPermission(player, "tardis.timetravel.village")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NO_PERM_VILLAGE");
            return true;
        }
        if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.TELEPATHIC_CIRCUIT)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Telepathic Circuit");
            return true;
        }
        TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
        tcc.getCircuits();
        // check for telepathic circuit
        if (plugin.getConfig().getBoolean("difficulty.circuits") && !plugin.getUtils().inGracePeriod(player, true) && !tcc.hasTelepathic()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TELEPATHIC_CIRCUIT");
            return true;
        }
        // damage circuit if configured
        if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.telepathic") > 0) {
            // decrement uses
            int uses_left = tcc.getTelepathicUses();
            new TARDISCircuitDamager(plugin, DiskCircuit.TELEPATHIC, uses_left, id, player).damage();
        }
        // find a village / nether fortress / end city
        TARDISStructureLocation randomStructure = new TARDISStructureTravel(plugin).getRandom(player, id, args);
        if (randomStructure == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "VILLAGE_NOT_FOUND");
            return true;
        }
        Location village = randomStructure.getLocation();
        // check respect
        if (!plugin.getPluginRespect().getRespect(village, new Parameters(player, Flag.getDefaultFlags()))) {
            if (plugin.getConfig().getBoolean("travel.no_destination_malfunctions")) {
                plugin.getTrackerKeeper().getMalfunction().put(id, true);
            } else {
                return true;
            }
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("world", village.getWorld().getName());
        set.put("x", village.getBlockX());
        set.put("y", village.getBlockY());
        set.put("z", village.getBlockZ());
        set.put("submarine", 0);
        TravelType travelType;
        switch (village.getWorld().getEnvironment()) {
            case THE_END -> travelType = TravelType.VILLAGE_THE_END;
            case NETHER -> travelType = TravelType.VILLAGE_NETHER;
            default -> travelType = TravelType.VILLAGE_OVERWORLD;
        }
        HashMap<String, Object> tid = new HashMap<>();
        tid.put("tardis_id", id);
        plugin.getQueryFactory().doSyncUpdate("next", set, tid);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_LOADED", randomStructure.getWhich(), !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), travelType));
        plugin.getTrackerKeeper().getRescue().remove(id);
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            new TARDISLand(plugin, id, player).exitVortex();
            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, travelType, id));
        }
        return true;
    }
}
