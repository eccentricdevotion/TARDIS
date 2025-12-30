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
package me.eccentric_nz.TARDIS.customblocks;

import me.eccentric_nz.TARDIS.ARS.TARDISARSSlot;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class TARDISDisplayBlockRoomConverter implements Runnable {

    private final TARDIS plugin;
    private final Player owner;
    private final int startX, startY, startZ;
    private final int c = 16;
    private final int h = 16;
    private final int w = 16;
    private boolean running;
    private int level = 0;
    private int row = 0;
    private int taskId;
    private World world;

    public TARDISDisplayBlockRoomConverter(TARDIS plugin, Player owner, TARDISARSSlot slot) {
        this.plugin = plugin;
        this.owner = owner;
        this.startX = slot.getX();
        this.startY = slot.getY();
        this.startZ = slot.getZ();
    }

    @Override
    public void run() {
        // initialise
        if (!running) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", owner.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                world = TARDISStaticLocationGetters.getWorldFromSplitString(tardis.getChunk());
            }
            running = true;
        }
        if (level == (h - 1) && row == (w - 1)) {
            // we're finished
            plugin.getServer().getScheduler().cancelTask(taskId);
            taskId = 0;
            plugin.getMessenger().message(owner, "Custom block display item conversion complete");
        } else {
            // check a row of blocks
            for (int col = 0; col < c; col++) {
                int x = startX + row;
                int y = startY + level;
                int z = startZ + col;
                Block block = world.getBlockAt(x, y, z);
                if (!isCustomBlock(block)) {
                    continue;
                }
                ItemDisplay display = TARDISDisplayItemUtils.getFromBoundingBox(block);
                if (display == null) {
                    continue;
                }
                // get the item stack
                ItemStack is = display.getItemStack();
                // if the item stack is not null
                if (is.getType().isAir() || !is.hasItemMeta()) {
                    continue;
                }
                ItemMeta im = is.getItemMeta();
                // get the custom name
                if (!im.hasDisplayName()) {
                    continue;
                }
                Component component = im.displayName();
                if (component == null) {
                    continue;
                }
                String name = ComponentUtils.toEnumUppercase(component);
                if (!component.children().isEmpty()) {
                    name = ComponentUtils.toEnumUppercase(component.children().getFirst());
                }
                // look up the name to get the TDI
                try {
                    TARDISDisplayItem tdi = TARDISDisplayItemRegistry.valueOf(name);
                    // set the displayname
                    im.displayName(ComponentUtils.toWhite(tdi.getDisplayName()));
                    if (name.toLowerCase(Locale.ROOT).contains("door")) {
                        // set the item model from TDI
                        im.setItemModel(tdi.getCustomModel());
                    } else {
                        // remove item model
                        im.setItemModel(null);
                    }
                    is.setItemMeta(im);
                    // set the display item's item stack
                    display.setItemStack(is);
                } catch (IllegalArgumentException ignored) {
                }
            }
            if (row < w) {
                row++;
            }
            if (row == w && level < h) {
                row = 0;
                level++;
            }
        }
    }

    private boolean isCustomBlock(Block block) {
        Material m = block.getType();
        if (m == Material.BARRIER || m == Material.LIGHT) {
            return true;
        } else {
            // look for an interaction
            Interaction interaction = TARDISDisplayItemUtils.getInteraction(block);
            return interaction != null;
        }
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
