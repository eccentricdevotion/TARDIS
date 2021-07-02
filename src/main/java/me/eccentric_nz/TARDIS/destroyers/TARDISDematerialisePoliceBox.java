/*
 * Copyright (C) 2021 eccentric_nz
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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.destroyers;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderUtility;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISDematerialisePoliceBox implements Runnable {

    private final TARDIS plugin;
    private final DestroyData dd;
    private final int loops;
    private final PRESET preset;
    private int task;
    private int i;
    private ItemFrame frame;
    private ItemStack is;

    TARDISDematerialisePoliceBox(TARDIS plugin, DestroyData dd, PRESET preset) {
        this.plugin = plugin;
        this.dd = dd;
        loops = dd.getThrottle().getLoops();
        this.preset = preset;
    }

    @Override
    public void run() {
        World world = dd.getLocation().getWorld();
        Block light = dd.getLocation().getBlock().getRelative(BlockFace.UP, 2);
        if (i < loops) {
            i++;
            int cmd;
            switch (i % 3) {
                case 2 -> { // stained
                    cmd = 1003;
                    light.setBlockData(TARDISConstants.AIR);
                }
                case 1 -> { // glass
                    cmd = 1004;
                    light.setBlockData(TARDISConstants.AIR);
                }
                default -> { // preset
                    cmd = 1001;
                    light.setBlockData(TARDISConstants.LIGHT);
                }
            }
            // first run - play sound
            if (i == 1) {
                boolean found = false;
                for (Entity e : world.getNearbyEntities(dd.getLocation(), 1.0d, 1.0d, 1.0d)) {
                    if (e instanceof ItemFrame) {
                        frame = (ItemFrame) e;
                        found = true;
                    }
                }
                if (!found) {
                    // spawn item frame
                    frame = (ItemFrame) world.spawnEntity(dd.getLocation(), EntityType.ITEM_FRAME);
                }
                frame.setFacingDirection(BlockFace.UP);
                frame.setRotation(dd.getDirection().getRotation());
                Material dye = TARDISBuilderUtility.getMaterialForItemFrame(preset);
                is = new ItemStack(dye, 1);
                // only play the sound if the player is outside the TARDIS
                if (dd.isOutside()) {
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, dd.getPlayer().getUniqueId().toString());
                    boolean minecart = false;
                    SpaceTimeThrottle spaceTimeThrottle = SpaceTimeThrottle.NORMAL;
                    if (rsp.resultSet()) {
                        minecart = rsp.isMinecartOn();
                        spaceTimeThrottle = SpaceTimeThrottle.getByDelay().get(rsp.getThrottle());
                    }
                    if (!minecart) {
                        String sound = switch (spaceTimeThrottle) {
                            case WARP, RAPID, FASTER -> "tardis_takeoff_" + spaceTimeThrottle.toString().toLowerCase();
                            default -> // NORMAL
                                "tardis_takeoff";
                        };
                        TARDISSounds.playTARDISSound(dd.getLocation(), sound);
                    } else {
                        world.playSound(dd.getLocation(), Sound.ENTITY_MINECART_INSIDE, 1.0F, 0.0F);
                    }
                }
            }
            if (is != null) {
                ItemMeta im = is.getItemMeta();
                im.setCustomModelData(cmd);
                is.setItemMeta(im);
                frame.setItem(is, false);
                frame.setFixed(true);
                frame.setVisible(false);
            }
        } else {
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
            light.setBlockData(TARDISConstants.AIR);
            new TARDISDeinstantPreset(plugin).instaDestroyPreset(dd, false, preset);
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
