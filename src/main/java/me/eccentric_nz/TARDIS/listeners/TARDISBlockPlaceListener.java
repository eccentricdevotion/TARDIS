/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.chemistry.product.LampToggler;
import me.eccentric_nz.TARDIS.custommodeldata.TARDISMushroomBlock;
import me.eccentric_nz.TARDIS.custommodeldata.TARDISMushroomBlockData;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

/**
 * TARDISes are bioships that are grown from a species of coral presumably indigenous to Gallifrey.
 * <p>
 * The TARDIS had a drawing room, which the Doctor claimed to be his "private study". Inside it were momentos of his
 * many incarnations' travels.
 *
 * @author eccentric_nz
 */
public class TARDISBlockPlaceListener implements Listener {

    private final TARDIS plugin;

    public TARDISBlockPlaceListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for a player placing a block. If the player places a brown mushroom block with a TARDIS namespaced key,
     * then convert it to one of the unused brown mushroom block states.
     *
     * @param event a player placing a block
     */

    @EventHandler(ignoreCancelled = true)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (plugin.getTrackerKeeper().getZeroRoomOccupants().contains(player.getUniqueId())) {
            event.setCancelled(true);
            TARDISMessage.send(player, "NOT_IN_ZERO");
            return;
        }
        String blockStr = event.getBlockPlaced().getLocation().toString();
        if (plugin.getGeneralKeeper().getProtectBlockMap().containsKey(blockStr)) {
            event.setCancelled(true);
            TARDISMessage.send(player, "NO_PLACE");
        }
        ItemStack is = event.getItemInHand();
        if ((is.getType().equals(Material.BROWN_MUSHROOM_BLOCK) || is.getType().equals(Material.RED_MUSHROOM_BLOCK) || is.getType().equals(Material.MUSHROOM_STEM))) {
            if (is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                boolean light = false;
                if (im.getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.INTEGER)) {
                    int which = im.getPersistentDataContainer().get(plugin.getCustomBlockKey(), PersistentDataType.INTEGER);
                    MultipleFacing multipleFacing;
                    if (is.getType().equals(Material.BROWN_MUSHROOM_BLOCK)) {
                        multipleFacing = (MultipleFacing) plugin.getServer().createBlockData(TARDISMushroomBlockData.BROWN_MUSHROOM_DATA.get(which));
                    } else if (is.getType().equals(Material.RED_MUSHROOM_BLOCK)) {
                        multipleFacing = (MultipleFacing) plugin.getServer().createBlockData(TARDISMushroomBlockData.RED_MUSHROOM_DATA.get(which));
                    } else {
                        multipleFacing = (MultipleFacing) plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(which));
                        light = (which > 0 && which < 5);
                        if (plugin.getConfig().getBoolean("allow.chemistry") && which == 5) {
                            // remember heat block location
                            plugin.getTrackerKeeper().getHeatBlocks().add(blockStr);
                        }
                    }
                    if (light) {
                        multipleFacing = TARDISMushroomBlock.getChemistryStemOn(multipleFacing);
                        LampToggler.createLight(event.getBlockPlaced());
                    }
                    event.getBlockPlaced().setBlockData(multipleFacing);
                    return;
                }
            } else {
                BlockData data;
                if (is.getType().equals(Material.BROWN_MUSHROOM_BLOCK)) {
                    data = plugin.getServer().createBlockData(TARDISMushroomBlockData.BROWN_MUSHROOM_DATA_ALL);
                } else if (is.getType().equals(Material.RED_MUSHROOM_BLOCK)) {
                    data = plugin.getServer().createBlockData(TARDISMushroomBlockData.RED_MUSHROOM_DATA_ALL);
                } else {
                    data = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA_ALL);
                }
                event.getBlockPlaced().setBlockData(data, false);
                setNextToMushroomBlock(player, event.getBlockPlaced());
                return;
            }
        }
        if (!TARDISPermission.hasPermission(player, "tardis.rift")) {
            return;
        }
        if (!is.getType().equals(Material.BEACON) || !is.hasItemMeta()) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if (im.hasDisplayName() && im.getDisplayName().equals("Rift Manipulator")) {
            // make sure they're not inside the TARDIS
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
            if (rst.resultSet()) {
                event.setCancelled(true);
                TARDISMessage.send(player, "RIFT_OUTSIDE");
                return;
            }
            // add recharger to to config
            Location l = event.getBlockPlaced().getLocation();
            String name = "rift_" + player.getName() + "_" + TARDISConstants.RANDOM.nextInt(Integer.MAX_VALUE);
            while (plugin.getConfig().contains("rechargers." + name)) {
                name = "rift_" + player.getName() + "_" + TARDISConstants.RANDOM.nextInt(Integer.MAX_VALUE);
            }
            plugin.getConfig().set("rechargers." + name + ".world", l.getWorld().getName());
            plugin.getConfig().set("rechargers." + name + ".x", l.getBlockX());
            plugin.getConfig().set("rechargers." + name + ".y", l.getBlockY());
            plugin.getConfig().set("rechargers." + name + ".z", l.getBlockZ());
            plugin.getConfig().set("rechargers." + name + ".uuid", player.getUniqueId().toString());
            plugin.saveConfig();
            TARDISMessage.send(player, "RIFT_SUCCESS");
        }
    }

    private void setNextToMushroomBlock(Player player, Block block) {
        Material material = block.getType();
        for (int i = 0; i < 6; i++) {
            for (BlockFace face : BlockFace.values()) {
                Block b = block.getRelative(face, i);
                if (b.getType().equals(material)) {
                    player.sendBlockChange(b.getLocation(), b.getBlockData());
                }
            }
        }
    }
}
