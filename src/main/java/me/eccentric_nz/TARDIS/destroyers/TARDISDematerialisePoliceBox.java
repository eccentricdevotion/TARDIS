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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.destroyers;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
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
    private ItemMeta im;
    private Material dye;

    public TARDISDematerialisePoliceBox(TARDIS plugin, DestroyData dd, PRESET preset) {
        this.plugin = plugin;
        this.dd = dd;
        loops = dd.getThrottle().getLoops();
        this.preset = preset;
    }

    @Override
    public void run() {
        World world = dd.getLocation().getWorld();
        if (i < loops) {
            i++;
            int cmd;
            switch (i % 3) {
                case 2: // stained
                    cmd = 1003;
                    break;
                case 1: // glass
                    cmd = 1004;
                    break;
                default: // preset
                    cmd = 1001;
                    break;
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
                dye = getDyeMaterial(preset);
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
                        String sound;
                        switch (spaceTimeThrottle) {
                            case WARP:
                            case RAPID:
                            case FASTER:
                                sound = "tardis_takeoff_" + spaceTimeThrottle.toString().toLowerCase();
                                break;
                            default: // NORMAL
                                sound = "tardis_takeoff";
                                break;
                        }
                        TARDISSounds.playTARDISSound(dd.getLocation(), sound);
                    } else {
                        world.playSound(dd.getLocation(), Sound.ENTITY_MINECART_INSIDE, 1.0F, 0.0F);
                    }
                }
            }
            im = is.getItemMeta();
            im.setCustomModelData(cmd);
            is.setItemMeta(im);
            frame.setItem(is);
            frame.setFixed(true);
            frame.setVisible(false);
        } else {
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
            new TARDISDeinstantPreset(plugin).instaDestroyPreset(dd, false, preset);
        }
    }

    private Material getDyeMaterial(PRESET preset) {
        String split = preset.toString().replace("POLICE_BOX_", "");
        String dye = split + "_DYE";
        return Material.valueOf(dye);
    }

    public void setTask(int task) {
        this.task = task;
    }
}
