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
package me.eccentric_nz.TARDIS.travel;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.tardischunkgenerator.custombiome.BiomeUtilities;
import me.eccentric_nz.tardischunkgenerator.custombiome.TerraBiomeLocator;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.terraform.coregen.bukkit.TerraformGenerator;

import java.util.HashMap;
import java.util.Locale;

public class TARDISBiomeFinder {

    private final TARDIS plugin;

    public TARDISBiomeFinder(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void run(World w, String search, Player player, int id, COMPASS direction, Location current) {
        Location tb;
        plugin.getMessenger().sendStatus(player, "BIOME_SEARCH");
        if (TARDIS.plugin.getServer().getPluginManager().isPluginEnabled("TerraformGenerator") && w.getGenerator() instanceof TerraformGenerator) {
            tb = new TerraBiomeLocator(w, current, search).execute();
        } else {
            try {
                Biome biome = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME).get(new NamespacedKey("minecraft", search.toLowerCase(Locale.ROOT)));
                if (biome == null || biome.equals(Biome.THE_VOID)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_TRAVEL_NOT_VALID");
                    return;
                }
                tb = BiomeUtilities.searchBiome(w, biome, current);
            } catch (IllegalArgumentException iae) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_NOT_VALID");
                return;
            }
        }
        if (tb == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_NOT_FOUND");
            return;
        }
        // found a biome
        if (!plugin.getPluginRespect().getRespect(tb, new Parameters(player, Flag.getDefaultFlags()))) {
            if (plugin.getConfig().getBoolean("travel.no_destination_malfunctions")) {
                plugin.getTrackerKeeper().getMalfunction().put(id, true);
            } else {
                // cancel
                plugin.getMessenger().send(player, TardisModule.TARDIS, "PROTECTED");
                return;
            }
        }
        World bw = tb.getWorld();
        // check location
        while (!bw.getChunkAt(tb).isLoaded()) {
            bw.getChunkAt(tb).load();
        }
        int highest = tb.getWorld().getHighestBlockYAt(tb);
        if (tb.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
            highest = TARDISStaticLocationGetters.getNetherHighest(tb);
        }
        tb.setY(highest + 1);
        int[] start_loc = TARDISTimeTravel.getStartLocation(tb, direction);
        int tmp_y = tb.getBlockY();
        for (int up = 0; up < 10; up++) {
            int count = TARDISTimeTravel.safeLocation(start_loc[0], tmp_y + up, start_loc[2], start_loc[1], start_loc[3], tb.getWorld(), direction);
            if (count == 0) {
                tb.setY(tmp_y + up);
                break;
            }
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("world", tb.getWorld().getName());
        set.put("x", tb.getBlockX());
        set.put("y", tb.getBlockY());
        set.put("z", tb.getBlockZ());
        set.put("direction", direction.toString());
        set.put("submarine", 0);
        HashMap<String, Object> tid = new HashMap<>();
        tid.put("tardis_id", id);
        plugin.getQueryFactory().doSyncUpdate("next", set, tid);
        plugin.getMessenger().send(player, "BIOME_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.BIOME));
        plugin.getTrackerKeeper().getRescue().remove(id);
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            new TARDISLand(plugin, id, player).exitVortex();
            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.BIOME, id));
        }
    }
}
