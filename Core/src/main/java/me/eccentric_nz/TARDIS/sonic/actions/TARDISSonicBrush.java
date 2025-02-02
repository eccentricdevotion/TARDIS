/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.sonic.actions;

import com.google.common.collect.Iterables;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BrushableBlock;
import org.bukkit.block.data.Brushable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;

import java.util.Collection;
import java.util.UUID;

public class TARDISSonicBrush {

    public static void dust(TARDIS plugin, Block b, Player p) {
        if (TARDISSonicRespect.checkBlockRespect(plugin, p, b)) {
            if (b.getBlockData() instanceof Brushable brushable) {
                Material material = brushable.getMaterial();
                boolean isSand = (material == Material.SUSPICIOUS_SAND);
                int dusted = brushable.getDusted() + 1;
                BrushableBlock bb = (BrushableBlock) b.getState();
                ItemStack is = bb.getItem();
                if (dusted == 1 && (is == null || is.getType() == Material.AIR)) {
                    LootTable table = bb.getLootTable();
                    if (table != null) {
                        // generate loot
                        LootContext.Builder builder = new LootContext.Builder(b.getLocation()).killer(p);
                        Collection<ItemStack> col = table.populateLoot(TARDISConstants.RANDOM, builder.build());
                        if (!col.isEmpty()) {
                            is = Iterables.get(col, 0);
                            bb.setItem(is);
                            bb.setLootTable(null); // !important, otherwise item stack will revert to AIR
                            bb.update();
                        }
                    }
                }
                if (dusted == 4 && is != null && is.getType() != Material.AIR) {
                    // play a sound
                    p.playSound(b.getLocation(), isSand ? Sound.BLOCK_SUSPICIOUS_SAND_BREAK : Sound.BLOCK_SUSPICIOUS_GRAVEL_BREAK, 1.0f, 1.0f);
                    // drop the item
                    b.getWorld().dropItemNaturally(b.getLocation(), is);
                    // set the block to sand/gravel
                    b.setType(isSand ? Material.SAND : Material.GRAVEL);
                } else if (dusted < 4) {
                    UUID uuid = p.getUniqueId();
                    plugin.getTrackerKeeper().getBrushCooldown().put(uuid, System.currentTimeMillis() + 1800L);
                    brushable.setDusted(dusted);
                    b.setBlockData(brushable);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        // revert back to dusted = 0 if still suspicious and last click was more than 2 seconds ago
                        if (b.getType().equals(material) && System.currentTimeMillis() > plugin.getTrackerKeeper().getBrushCooldown().get(uuid)) {
                            brushable.setDusted(0);
                            b.setBlockData(brushable);
                            plugin.getTrackerKeeper().getBrushCooldown().remove(uuid);
                        }
                    }, 40L);
                }
            }
        }
    }
}
