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
package me.eccentric_nz.TARDIS.builders.interior;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISCreationEvent;
import me.eccentric_nz.TARDIS.builders.exterior.BuildData;
import me.eccentric_nz.TARDIS.builders.exterior.MaterialiseBlockPreset;
import me.eccentric_nz.TARDIS.builders.exterior.MaterialiseModelledPreset;
import me.eccentric_nz.TARDIS.discovery.StructureVariant;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.planets.TARDISWorldResolver;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class AbandonedBuilder {

    private final TARDIS plugin;

    public AbandonedBuilder(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void spawn(Location l, Schematic schematic, StructureVariant variant, ChameleonPreset preset, String item, COMPASS d, Player player) {
        Chunk chunk = l.getChunk();
        // get this chunk's co-ords
        String cw = plugin.getConfig().getString("creation.default_world_name", "tardis_timevortex");
        World chunkworld = TARDISWorldResolver.getFromString(cw);
        int cx = chunk.getX();
        int cz = chunk.getZ();
        // save data to database (tardis table)
        String c = "minecraft:" + cw + ":" + cx + ":" + cz;
        HashMap<String, Object> set = new HashMap<>();
        set.put("uuid", UUID.randomUUID().toString());
        set.put("owner", "");
        set.put("chunk", c);
        set.put("size", schematic.getPermission().toUpperCase(Locale.ROOT));
        set.put("abandoned", 1);
        set.put("powered_on", 0);
        set.put("lastuse", Long.MAX_VALUE);
        if (preset == ChameleonPreset.ITEM) {
            set.put("chameleon_preset", "ITEM:" + item);
            set.put("chameleon_demat", "ITEM:" + item);
        } else {
            set.put("chameleon_preset", preset.toString());
            set.put("chameleon_demat", preset.toString());
        }
        int lastInsertId = plugin.getQueryFactory().doSyncInsert("tardis", set);
        // populate home, current, next and back tables
        HashMap<String, Object> locations = new HashMap<>();
        locations.put("tardis_id", lastInsertId);
        locations.put("world", l.getWorld().getKey().asString());
        locations.put("x", l.getBlockX());
        locations.put("y", l.getBlockY());
        locations.put("z", l.getBlockZ());
        locations.put("direction", d.toString());
        plugin.getQueryFactory().insertLocations(locations);
        // turn the block stack into a TARDIS
        BuildData bd = new BuildData(null);
        bd.setDirection(d);
        bd.setLocation(l);
        bd.setMalfunction(false);
        bd.setOutside(true);
        bd.setRebuild(false);
        bd.setSubmarine(l.getBlock().getType().equals(Material.WATER));
        bd.setTardisID(lastInsertId);
        bd.setPlayer(player);
        bd.setThrottle(SpaceTimeThrottle.REBUILD);
        plugin.getPM().callEvent(new TARDISCreationEvent(null, lastInsertId, l));
        if (schematic.getPermission().equals("fade")) {
            // determine variant
            AbandonedFadeBuildRunnable builder = new AbandonedFadeBuildRunnable(plugin, variant, chunkworld, lastInsertId, player);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, builder, 1L, 3L);
            builder.setTask(task);
        } else {
            AbandonedBuildRunnable builder = new AbandonedBuildRunnable(plugin, schematic, chunkworld, lastInsertId, player);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, builder, 1L, 3L);
            builder.setTask(task);
        }
        // delay building exterior
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            plugin.getTrackerKeeper().getMaterialising().add(bd.getTardisID());
            if (preset.usesArmourStand()) {
                MaterialiseModelledPreset runnable = new MaterialiseModelledPreset(plugin, bd, preset);
                int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                runnable.setTask(taskID);
            } else {
                BlockData data = (preset.equals(ChameleonPreset.FACTORY)) ? Material.LIGHT_GRAY_TERRACOTTA.createBlockData() : Material.BLUE_WOOL.createBlockData();
                MaterialiseBlockPreset runnable = new MaterialiseBlockPreset(plugin, bd, preset, data, Adaption.OFF);
                int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                runnable.setTask(taskID);
            }
            TARDISSounds.playTARDISSound(bd.getLocation(), "tardis_land_fast");
        }, schematic.getConsoleSize().getDelay());
    }
}
