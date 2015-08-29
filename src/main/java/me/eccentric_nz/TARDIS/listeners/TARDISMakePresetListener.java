/*
 * Copyright (C) 2014 eccentric_nz
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
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

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
    private final List<Integer> not_glass = new ArrayList<Integer>();

    public TARDISMakePresetListener(TARDIS plugin) {
        this.plugin = plugin;
        this.orderx = new int[]{0, 1, 2, 2, 2, 1, 0, 0, 1, -1};
        this.orderz = new int[]{0, 0, 0, 1, 2, 2, 2, 1, 1, 1};
        this.not_glass.add(0); // air
        this.not_glass.add(20); // glass
        this.not_glass.add(50); // torch
        this.not_glass.add(63); // sign post
        this.not_glass.add(64); // wood door
        this.not_glass.add(68); // wall sign
        this.not_glass.add(71); // iron door
        this.not_glass.add(75); // redstone torch off
        this.not_glass.add(76); // redstone torch on
        this.not_glass.add(96); // trap door
        this.not_glass.add(106); // vine
        this.not_glass.add(123); // redstone lamp off
        this.not_glass.add(124); // redstone lamp on
        this.not_glass.add(193); // spruce door
        this.not_glass.add(194); // birch door
        this.not_glass.add(195); // jungle door
        this.not_glass.add(196); // acacia door
        this.not_glass.add(197); // dark oak door
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
                StringBuilder sb_stain_id = new StringBuilder("[");
                StringBuilder sb_stain_data = new StringBuilder("[");
                StringBuilder sb_glass_id = new StringBuilder("[");
                StringBuilder sb_glass_data = new StringBuilder("[");
                for (int c = 0; c < 10; c++) {
                    sb_id.append("[");
                    sb_data.append("[");
                    sb_stain_id.append("[");
                    sb_stain_data.append("[");
                    sb_glass_id.append("[");
                    sb_glass_data.append("[");
                    for (int y = fy; y < (fy + 4); y++) {
                        Block b = w.getBlockAt(fx + orderx[c], y, fz + orderz[c]);
                        int id = b.getTypeId();
                        if (id == 19) {
                            id = 0; // convert sponge to air
                        }
                        byte data = b.getData();
                        if (y == (fy + 3)) {
                            sb_id.append(id);
                            sb_data.append(data);
                            if (not_glass.contains(id)) {
                                sb_stain_id.append(id);
                                sb_stain_data.append(data);
                                sb_glass_id.append(id);
                                sb_glass_data.append(data);
                            } else {
                                sb_stain_id.append(95);
                                byte colour = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(id);
                                if (colour == -1) {
                                    // use the same data as the original block
                                    colour = data;
                                }
                                sb_stain_data.append(colour); // get the appropiately coloured stained glass
                                sb_glass_id.append(20);
                                sb_glass_data.append(0);
                            }
                        } else {
                            sb_id.append(id).append(",");
                            sb_data.append(data).append(",");
                            if (not_glass.contains(id)) {
                                sb_stain_id.append(id).append(",");
                                sb_stain_data.append(data).append(",");
                                sb_glass_id.append(id).append(",");
                                sb_glass_data.append(data).append(",");
                            } else {
                                sb_stain_id.append(95).append(",");
                                byte colour = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(id);
                                if (colour == -1) {
                                    // use the same data as the original block
                                    colour = data;
                                }
                                sb_stain_data.append(colour).append(","); // get the appropiately coloured stained glass
                                sb_glass_id.append(20).append(",");
                                sb_glass_data.append(0).append(",");
                            }
                        }
                    }
                    if (c == 9) {
                        sb_id.append("]");
                        sb_data.append("]");
                        sb_stain_id.append("]");
                        sb_stain_data.append("]");
                        sb_glass_id.append("]");
                        sb_glass_data.append("]");
                    } else {
                        sb_id.append("],");
                        sb_data.append("],");
                        sb_stain_id.append("],");
                        sb_stain_data.append("],");
                        sb_glass_id.append("],");
                        sb_glass_data.append("],");
                    }
                }
                sb_id.append("]");
                sb_data.append("]");
                sb_stain_id.append("]");
                sb_stain_data.append("]");
                sb_glass_id.append("]");
                sb_glass_data.append("]");
                String ids = sb_id.toString();
                String datas = sb_data.toString();
                String stain_ids = sb_stain_id.toString();
                String stain_datas = sb_stain_data.toString();
                String glass_ids = sb_glass_id.toString();
                String glass_datas = sb_glass_data.toString();
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
                    bw.write("#data");
                    bw.newLine();
                    bw.write(stain_datas);
                    bw.newLine();
                    bw.write("##start custom glass");
                    bw.newLine();
                    bw.write("#id");
                    bw.newLine();
                    bw.write(glass_ids);
                    bw.newLine();
                    bw.write("#data");
                    bw.newLine();
                    bw.write(glass_datas);
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
