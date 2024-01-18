/*
 * Copyright (C) 2024 eccentric_nz
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

import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Phosphor lamps are used for lighting. They use electron excitation; when
 * shaken, they grow brighter.
 *
 * @author eccentric_nz
 */
class TARDISLampsRunnable implements Runnable {

    private final TARDIS plugin;
    private final List<Block> lamps;
    private final long end;
    private final TardisLight light;
    private final boolean lights_on;
    int i = 0;
    Levelled levelled = TARDISConstants.LIGHT;
    private int task;
    private Location handbrake_loc;

    TARDISLampsRunnable(TARDIS plugin, List<Block> lamps, long end, TardisLight light, boolean lights_on) {
        this.plugin = plugin;
        this.lamps = lamps;
        this.end = end;
        this.light = light;
        this.lights_on = lights_on;
    }

    @Override
    public void run() {
        if (System.currentTimeMillis() > end) {
            // set all lamps back to whatever they were when the malfunction happened
            levelled.setLevel((lights_on) ? 15 : 0);
            lamps.forEach((b) -> {
                b.setBlockData(levelled);
            });
            plugin.getServer().getScheduler().cancelTask(task);
        } else {
            // play smoke effect
            for (int j = 0; j < 9; j++) {
                handbrake_loc.getWorld().playEffect(handbrake_loc, Effect.SMOKE, j);
            }
            lamps.forEach((b) -> {
                boolean off = (i % 2 == 0);
                levelled.setLevel(off ? 0 : 15);
                if (i == 0 && b.getType().equals(Material.SEA_LANTERN) || (b.getType().equals(Material.REDSTONE_LAMP))) {
                    // convert to light display item
                    TARDISDisplayItemUtils.set(light.getOff(), b);
                } else {
                    ItemDisplay display = TARDISDisplayItemUtils.get(b);
                    // switch the itemstack
                    if (display != null) {
                        TARDISDisplayItem tdi = (off) ? light.getOff(): light.getOn();
                        ItemStack is = display.getItemStack();
                        ItemMeta im = is.getItemMeta();
                        if (tdi.getCustomModelData() == -1) {
                            im.setCustomModelData(null);
                        } else {
                            im.setCustomModelData(tdi.getCustomModelData());
                        }
                        is.setType(tdi.getMaterial());
                        is.setItemMeta(im);
                        display.setItemStack(is);
                    }
                }
                b.setBlockData(levelled);
            });
        }
        i++;
    }

    public void setTask(int task) {
        this.task = task;
    }

    public void setHandbrake(Location handbrake_loc) {
        this.handbrake_loc = handbrake_loc;
    }
}
