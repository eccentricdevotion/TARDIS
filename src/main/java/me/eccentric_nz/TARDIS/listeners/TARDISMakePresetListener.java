/*
 * Copyright (C) 2016 eccentric_nz
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
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

/**
 * Within the first nanosecond of landing in a new location, the TARDIS
 * chameleon circuit analyses the surrounding area, calculates a
 * twelve-dimensional data map of all objects within a thousand-mile radius and
 * then determines which outer shell would best blend in with the environment.
 *
 * @author eccentric_nz
 */
public class TARDISMakePresetListener implements Listener {

    private final TARDIS plugin;
    private final int[] orderx;
    private final int[] orderz;
    private final List<Material> not_glass = new ArrayList<>();

    public TARDISMakePresetListener(TARDIS plugin) {
        this.plugin = plugin;
        this.orderx = new int[]{0, 1, 2, 2, 2, 1, 0, 0, 1, -1};
        this.orderz = new int[]{0, 0, 0, 1, 2, 2, 2, 1, 1, 1};
        this.not_glass.add(Material.AIR); // air
        this.not_glass.add(Material.GLASS); // glass
        this.not_glass.add(Material.TORCH); // torch
        this.not_glass.add(Material.SIGN); // sign post
        this.not_glass.add(Material.OAK_DOOR); // wood door
        this.not_glass.add(Material.BIRCH_DOOR); // wood door
        this.not_glass.add(Material.SPRUCE_DOOR); // wood door
        this.not_glass.add(Material.JUNGLE_DOOR); // wood door
        this.not_glass.add(Material.ACACIA_DOOR); // wood door
        this.not_glass.add(Material.DARK_OAK_DOOR); // wood door
        this.not_glass.add(Material.WALL_SIGN); // wall sign
        this.not_glass.add(Material.IRON_DOOR); // iron door
        this.not_glass.add(Material.REDSTONE_TORCH); // redstone torch
        this.not_glass.add(Material.REDSTONE_WALL_TORCH); // redstone torch
        this.not_glass.add(Material.OAK_TRAPDOOR); // trap door
        this.not_glass.add(Material.BIRCH_TRAPDOOR); // trap door
        this.not_glass.add(Material.SPRUCE_TRAPDOOR); // trap door
        this.not_glass.add(Material.JUNGLE_TRAPDOOR); // trap door
        this.not_glass.add(Material.ACACIA_TRAPDOOR); // trap door
        this.not_glass.add(Material.DARK_OAK_TRAPDOOR); // trap door
        this.not_glass.add(Material.VINE); // vine
        this.not_glass.add(Material.REDSTONE_LAMP); // redstone lamp
    }

    /**
     * Listens for player clicking blocks. If the player's name is contained in
     * various tracking HashMaps then we know that they are trying to create a
     * TARDIS area.
     *
     * @param event a player clicking a block
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAreaInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        Block block = event.getClickedBlock();
        if (block != null) {
            if (plugin.getTrackerKeeper().getPreset().containsKey(uuid)) {
                String[] split = plugin.getTrackerKeeper().getPreset().get(uuid).split(":");
                String name = split[0];
                String bool = split[1];
                Location block_loc = block.getLocation();
                World w = block_loc.getWorld();
                int fx = block_loc.getBlockX();
                int fy = block_loc.getBlockY();
                int fz = block_loc.getBlockZ();
                TARDISMessage.send(player, "PRESET_SCAN");
                StringBuilder sb_id = new StringBuilder("[");
                StringBuilder sb_data = new StringBuilder("[");
                StringBuilder sb_stain_mat = new StringBuilder("[");
                StringBuilder sb_glass_id = new StringBuilder("[");
                for (int c = 0; c < 10; c++) {
                    sb_id.append("[");
                    sb_data.append("[");
                    sb_stain_mat.append("[");
                    sb_glass_id.append("[");
                    for (int y = fy; y < (fy + 4); y++) {
                        Block b = w.getBlockAt(fx + orderx[c], y, fz + orderz[c]);
                        Material material = b.getType();
                        String matStr = material.toString();
                        if (material.equals(Material.SPONGE)) {
                            matStr = "AIR"; // convert sponge to air
                        }
                        BlockData data = b.getBlockData();
                        if (y == (fy + 3)) {
                            sb_id.append(matStr);
                            sb_data.append(data.getAsString());
                            if (not_glass.contains(material)) {
                                sb_stain_mat.append(matStr);
                                sb_glass_id.append(matStr);
                            } else {
                                String colour = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(material).toString();
                                sb_stain_mat.append(colour);
                                sb_glass_id.append("GLASS");
                            }
                        } else {
                            sb_id.append(matStr).append(",");
                            sb_data.append(data.getAsString()).append(",");
                            if (not_glass.contains(material)) {
                                sb_stain_mat.append(matStr).append(",");
                                sb_glass_id.append(matStr).append(",");
                            } else {
                                String colour = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(material).toString();
                                sb_stain_mat.append(colour).append(",");
                                sb_glass_id.append("GLASS,");
                            }
                        }
                    }
                    if (c == 9) {
                        sb_id.append("]");
                        sb_data.append("]");
                        sb_stain_mat.append("]");
                        sb_glass_id.append("]");
                    } else {
                        sb_id.append("],");
                        sb_data.append("],");
                        sb_stain_mat.append("],");
                        sb_glass_id.append("],");
                    }
                }
                sb_id.append("]");
                sb_data.append("]");
                sb_stain_mat.append("]");
                sb_glass_id.append("]");
                String ids = sb_id.toString();
                String datas = sb_data.toString();
                String stain_ids = sb_stain_mat.toString();
                String glass_ids = sb_glass_id.toString();
                String filename = "custom_preset_" + name + ".txt";
                String file = plugin.getDataFolder() + File.separator + filename;
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
                    bw.write("##start custom blueprint");
                    bw.newLine();
                    bw.write("#id");
                    bw.newLine();
                    bw.write(ids);
                    bw.newLine();
                    bw.write("#data");
                    bw.newLine();
                    bw.write(datas);
                    bw.newLine();
                    bw.write("##start custom stain");
                    bw.newLine();
                    bw.write("#id");
                    bw.newLine();
                    bw.write(stain_ids);
                    bw.newLine();
                    bw.write("##start custom glass");
                    bw.newLine();
                    bw.write("#id");
                    bw.newLine();
                    bw.write(glass_ids);
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
                    bw.newLine();
                    bw.write("#is the preset asymmetrical? for example are some of the corners different to others");
                    bw.newLine();
                    bw.write(bool);
                    bw.close();
                } catch (IOException e) {
                    plugin.debug("Could not create and write to " + filename + "! " + e.getMessage());
                }
                plugin.getTrackerKeeper().getPreset().remove(uuid);
                TARDISMessage.send(player, "PRESET_DONE", filename);
            }
        }
    }
}
