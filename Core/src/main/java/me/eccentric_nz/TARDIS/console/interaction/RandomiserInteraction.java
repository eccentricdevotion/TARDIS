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
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.exterior.TARDISEmergencyRelocation;
import me.eccentric_nz.TARDIS.console.ConsoleInteraction;
import me.eccentric_nz.TARDIS.console.models.ButtonModel;
import me.eccentric_nz.TARDIS.control.actions.ExileAction;
import me.eccentric_nz.TARDIS.control.actions.RandomDestinationAction;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetRandomInteractions;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RandomiserInteraction {

    private final TARDIS plugin;

    public RandomiserInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void generateDestination(int id, Player player, Interaction interaction) {
        if (plugin.getTrackerKeeper().getFlight().containsKey(player.getUniqueId())) {
            return;
        }
        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
            return;
        }
        // get circuit checker
        TARDISCircuitChecker tcc = null;
        if (plugin.getConfig().getBoolean("difficulty.circuits")) {
            tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
        }
        if (tcc != null && !tcc.hasInput() && !plugin.getUtils().inGracePeriod(player, false)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "INPUT_MISSING");
            return;
        }
        // get tardis record
//        HashMap<String, Object> where = new HashMap<>();
//        where.put("tardis_id", id);
//        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
//        if (!rs.resultSet()) {
        Tardis tardis = TARDISCache.BY_ID.get(id);
        if (tardis == null) {
            return;
        }
//        Tardis tardis = rs.getTardis();
        int cost = plugin.getArtronConfig().getInt("random");
        if (tardis.getArtronLevel() < cost) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ENERGY");
            return;
        }
        Current current = TARDISCache.CURRENT.get(id);
        if (current == null) {
            // emergency TARDIS relocation
            new TARDISEmergencyRelocation(plugin).relocate(id, player);
            return;
        }
        // set custom model data for random button item display
        UUID uuid = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
        if (uuid != null) {
            ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(uuid);
            new ButtonModel().setState(display, plugin, ConsoleInteraction.RANDOMISER);
        }
        COMPASS direction = current.direction();
        if (TARDISPermission.hasPermission(player, "tardis.exile") && plugin.getConfig().getBoolean("travel.exile")) {
            new ExileAction(plugin).getExile(player, id, direction);
        } else {
            // get state from WORLD, MULTIPLIER, X, Z and HELMIC_REGULATOR interactions
            ResultSetRandomInteractions rsri = new ResultSetRandomInteractions(plugin, id);
            if (rsri.resultSet()) {
                // get if HELMIC_REGULATOR is active
                if (rsri.getStates()[4] != 0) {
                    // get selected world
                    World world = getWorldFromState(rsri.getStates()[4]);
                    if (world != null) {
                        Location rand = new TARDISTimeTravel(plugin).getDestination(world, rsri.getStates()[1], rsri.getStates()[2], rsri.getStates()[3], direction, world.getEnvironment().toString(), current.location(), player);
                        RandomDestinationAction.setDestination(plugin, player, id, direction, cost, tardis.getCompanions(), tardis.getUuid(), rand);
                    }
                } else {
                    new RandomDestinationAction(plugin).getRandomDestination(player, id, rsri.getStates(), current, direction, tardis.getArtronLevel(), cost, tardis.getCompanions(), tardis.getUuid());
                }
            }
        }
    }

    private World getWorldFromState(int state) {
        for (String w : plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false)) {
            if (plugin.getPlanetsConfig().getInt("planets." + w + ".helmic_regulator_order") == state) {
                return plugin.getServer().getWorld(w);
            }
        }
        return null;
    }
}
