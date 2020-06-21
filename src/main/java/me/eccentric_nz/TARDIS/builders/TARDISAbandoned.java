/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISCreationEvent;
import me.eccentric_nz.TARDIS.enumeration.Adaption;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISAbandoned {

    private final TARDIS plugin;

    public TARDISAbandoned(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void spawn(Location l, Schematic schm, PRESET preset, COMPASS d, Player player) {
        Chunk chunk = l.getChunk();
        // get this chunk's co-ords
        String cw = plugin.getConfig().getString("creation.default_world_name");
        World chunkworld = plugin.getServer().getWorld(cw);
        int cx = chunk.getX();
        int cz = chunk.getZ();
        // save data to database (tardis table)
        String biome = l.getBlock().getBiome().toString();
        String chun = cw + ":" + cx + ":" + cz;
        HashMap<String, Object> set = new HashMap<>();
        set.put("uuid", UUID.randomUUID().toString());
        set.put("owner", "");
        set.put("chunk", chun);
        set.put("size", schm.getPermission().toUpperCase(Locale.ENGLISH));
        set.put("abandoned", 1);
        set.put("lastuse", Long.MAX_VALUE);
        set.put("chameleon_preset", preset.toString());
        set.put("chameleon_demat", preset.toString());
        int lastInsertId = plugin.getQueryFactory().doSyncInsert("tardis", set);
        // populate home, current, next and back tables
        HashMap<String, Object> setlocs = new HashMap<>();
        setlocs.put("tardis_id", lastInsertId);
        setlocs.put("world", l.getWorld().getName());
        setlocs.put("x", l.getBlockX());
        setlocs.put("y", l.getBlockY());
        setlocs.put("z", l.getBlockZ());
        setlocs.put("direction", d.toString());
        plugin.getQueryFactory().insertLocations(setlocs, biome, lastInsertId);
        // turn the block stack into a TARDIS
        BuildData bd = new BuildData(plugin, null);
        bd.setDirection(d);
        bd.setLocation(l);
        bd.setMalfunction(false);
        bd.setOutside(true);
        bd.setRebuild(false);
        bd.setSubmarine(l.getBlock().getType().equals(Material.WATER));
        bd.setTardisID(lastInsertId);
        bd.setPlayer(player);
        plugin.getPM().callEvent(new TARDISCreationEvent(null, lastInsertId, l));
        TARDISBuildAbandoned builder = new TARDISBuildAbandoned(plugin, schm, chunkworld, lastInsertId, player);
        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, builder, 1L, 3L);
        builder.setTask(task);
        // delay building exterior
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            plugin.getTrackerKeeper().getMaterialising().add(bd.getTardisID());
            TARDISMaterialisePreset runnable = new TARDISMaterialisePreset(plugin, bd, preset, Material.BLUE_WOOL.createBlockData(), Adaption.OFF, 3);
            int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
            runnable.setTask(taskID);
            TARDISSounds.playTARDISSound(bd.getLocation(), "tardis_land_fast");
        }, schm.getConsoleSize().getDelay());
    }
}
