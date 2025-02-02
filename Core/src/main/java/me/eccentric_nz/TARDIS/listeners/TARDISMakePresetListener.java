/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public Lstainnse as published by
 * the Free Software Foundation, either version 3 of the Lstainnse, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public Lstainnse for more details.
 *
 * You should have received a copy of the GNU General Public Lstainnse
 * along with this program. If not, see <http://www.gnu.org/lstainnses/>.
 */
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISStainedGlassLookup;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

/**
 * Within the first nanosecond of landing in a new location, the TARDIS chameleon circuit analyses the surrounding area,
 * calculates a twelve-dimensional data map of all objects within a thousand-mile radius and then determines which outer
 * shell would best blend in with the environment.
 *
 * @author eccentric_nz
 */
public class TARDISMakePresetListener implements Listener {

    private final TARDIS plugin;
    private final int[] orderx;
    private final int[] orderz;
    private final String GLASS = addQuotes(TARDISConstants.GLASS.getAsString());

    public TARDISMakePresetListener(TARDIS plugin) {
        this.plugin = plugin;
        orderx = new int[]{0, 1, 2, 2, 2, 1, 0, 0, 1, -1};
        orderz = new int[]{0, 0, 0, 1, 2, 2, 2, 1, 1, 1};
    }

    /**
     * Listens for player clicking blocks. If the player's name is contained in various tracking HashMaps then we know
     * that they are trying to create a TARDIS area.
     *
     * @param event a player clicking a block
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAreaInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Block block = event.getClickedBlock();
        if (block != null) {
            if (plugin.getTrackerKeeper().getPreset().containsKey(uuid)) {
                String name = plugin.getTrackerKeeper().getPreset().get(uuid);
                Location block_loc = block.getLocation();
                World w = block_loc.getWorld();
                int fx = block_loc.getBlockX();
                int fy = block_loc.getBlockY();
                int fz = block_loc.getBlockZ();
                plugin.getMessenger().send(player, TardisModule.TARDIS, "PRESET_SCAN");
                StringBuilder sb_blue_data = new StringBuilder("[");
                StringBuilder sb_stain_data = new StringBuilder("[");
                StringBuilder sb_glass_data = new StringBuilder("[");
                for (int c = 0; c < 10; c++) {
                    sb_blue_data.append("[");
                    sb_stain_data.append("[");
                    sb_glass_data.append("[");
                    for (int y = fy; y < (fy + 4); y++) {
                        Block b = w.getBlockAt(fx + orderx[c], y, fz + orderz[c]);
                        Material material = b.getType();
                        BlockData data = b.getBlockData();
                        String dataStr = addQuotes(data.getAsString());
                        if (y == (fy + 3)) {
                            sb_blue_data.append(addQuotes(data.getAsString()));
                            if (TARDISMaterials.not_glass.contains(material)) {
                                sb_stain_data.append(dataStr);
                                sb_glass_data.append(dataStr);
                            } else {
                                Material colour = TARDISStainedGlassLookup.stainedGlassFromMaterial(w, material);
                                sb_stain_data.append(addQuotes(colour.createBlockData().getAsString()));
                                sb_glass_data.append(GLASS);
                            }
                        } else {
                            sb_blue_data.append(addQuotes(data.getAsString())).append(",");
                            if (TARDISMaterials.not_glass.contains(material)) {
                                sb_stain_data.append(dataStr).append(",");
                                sb_glass_data.append(dataStr).append(",");
                            } else {
                                Material colour = TARDISStainedGlassLookup.stainedGlassFromMaterial(w, material);
                                sb_stain_data.append(addQuotes(colour.createBlockData().getAsString())).append(",");
                                sb_glass_data.append(GLASS).append(",");
                            }
                        }
                    }
                    if (c == 9) {
                        sb_blue_data.append("]");
                        sb_stain_data.append("]");
                        sb_glass_data.append("]");
                    } else {
                        sb_blue_data.append("],");
                        sb_stain_data.append("],");
                        sb_glass_data.append("],");
                    }
                }
                sb_blue_data.append("]");
                sb_stain_data.append("]");
                sb_glass_data.append("]");
                String jsonBlue = sb_blue_data.toString();
                String jsonStain = sb_stain_data.toString();
                String jsonGlass = sb_glass_data.toString();
                String filename = "custom_preset_" + name + ".txt";
                String file = plugin.getDataFolder() + File.separator + filename;
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
                    bw.write("##start custom blueprint");
                    bw.newLine();
                    bw.write(jsonBlue);
                    bw.newLine();
                    bw.write("##start custom stain");
                    bw.newLine();
                    bw.write(jsonStain);
                    bw.newLine();
                    bw.write("##start custom glass");
                    bw.newLine();
                    bw.write(jsonGlass);
                    bw.newLine();
                    bw.write("##sign text - first line is player's name");
                    bw.newLine();
                    bw.write("#second line");
                    bw.newLine();
                    bw.write(name);
                    bw.newLine();
                    bw.write("#third line");
                    bw.newLine();
                    bw.write("PRESET");
                    bw.close();
                } catch (IOException e) {
                    plugin.debug("Could not create and write to " + filename + "! " + e.getMessage());
                }
                plugin.getTrackerKeeper().getPreset().remove(uuid);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "PRESET_DONE", filename);
            }
        }
    }

    private String addQuotes(String s) {
        return "\"" + s + "\"";
    }
}
