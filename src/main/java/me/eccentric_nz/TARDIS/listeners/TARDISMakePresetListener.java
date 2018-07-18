/*
 * Copyright (C) 2018 eccentric_nz
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
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
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
    private final String AIR = Material.AIR.createBlockData().toString();
    private final String GLASS = TARDISConstants.GLASS.toString();

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
                String[] split = plugin.getTrackerKeeper().getPreset().get(uuid).split(":");
                String name = split[0];
                String bool = split[1];
                Location block_loc = block.getLocation();
                World w = block_loc.getWorld();
                int fx = block_loc.getBlockX();
                int fy = block_loc.getBlockY();
                int fz = block_loc.getBlockZ();
                TARDISMessage.send(player, "PRESET_SCAN");
                StringBuilder sb_blueprint = new StringBuilder("[");
                StringBuilder sb_stained = new StringBuilder("[");
                StringBuilder sb_glass = new StringBuilder("[");
                for (int c = 0; c < 10; c++) {
                    sb_blueprint.append("[");
                    sb_stained.append("[");
                    sb_glass.append("[");
                    for (int y = fy; y < (fy + 4); y++) {
                        Block b = w.getBlockAt(fx + orderx[c], y, fz + orderz[c]);
                        Material material = b.getType();
                        BlockData data = b.getBlockData();
                        String dataStr = data.getAsString();
                        if (material.equals(Material.SPONGE)) {
                            dataStr = AIR; // convert sponge to air
                        }
                        if (y == (fy + 3)) {
                            sb_blueprint.append(data.getAsString());
                            if (TARDISMaterials.not_glass.contains(material)) {
                                sb_stained.append(dataStr);
                                sb_glass.append(dataStr);
                            } else {
                                String colour = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(material).toString();
                                sb_stained.append(colour);
                                sb_glass.append(GLASS);
                            }
                        } else {
                            sb_blueprint.append(data.getAsString()).append(",");
                            if (TARDISMaterials.not_glass.contains(material)) {
                                sb_stained.append(dataStr).append(",");
                                sb_glass.append(dataStr).append(",");
                            } else {
                                String colour = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(material).toString();
                                sb_stained.append(colour).append(",");
                                sb_glass.append(GLASS).append(",");
                            }
                        }
                    }
                    if (c == 9) {
                        sb_blueprint.append("]");
                        sb_stained.append("]");
                        sb_glass.append("]");
                    } else {
                        sb_blueprint.append("],");
                        sb_stained.append("],");
                        sb_glass.append("],");
                    }
                }
                sb_blueprint.append("]");
                sb_stained.append("]");
                sb_glass.append("]");
                String blueprints = sb_blueprint.toString();
                String stained = sb_stained.toString();
                String glass = sb_glass.toString();
                String filename = "custom_preset_" + name + ".txt";
                String file = plugin.getDataFolder() + File.separator + filename;
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
                    bw.write("##start custom blueprint");
                    bw.newLine();
                    bw.newLine();
                    bw.write("#start custom blueprint");
                    bw.newLine();
                    bw.write(blueprints);
                    bw.newLine();
                    bw.write("##start custom stain");
                    bw.newLine();
                    bw.write(stained);
                    bw.newLine();
                    bw.write("##start custom glass");
                    bw.newLine();
                    bw.write(glass);
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
