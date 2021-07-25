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
package me.eccentric_nz.TARDIS.chemistry.lab;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SuperFertisliserListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<Material, TreeType> TREE_LOOKUP = new HashMap<>() {
        {
            put(Material.OAK_SAPLING, TreeType.TREE);
            put(Material.DARK_OAK_SAPLING, TreeType.DARK_OAK);
            put(Material.ACACIA_SAPLING, TreeType.ACACIA);
            put(Material.JUNGLE_SAPLING, TreeType.SMALL_JUNGLE);
            put(Material.SPRUCE_SAPLING, TreeType.REDWOOD);
            put(Material.BIRCH_SAPLING, TreeType.BIRCH);
            put(Material.CHORUS_FLOWER, TreeType.CHORUS_PLANT);
            put(Material.RED_MUSHROOM, TreeType.RED_MUSHROOM);
            put(Material.BROWN_MUSHROOM, TreeType.BROWN_MUSHROOM);
            put(Material.CRIMSON_FUNGUS, TreeType.CRIMSON_FUNGUS);
            put(Material.WARPED_FUNGUS, TreeType.WARPED_FUNGUS);
        }
    };
    private final Set<Material> TREES = new HashSet<>();

    public SuperFertisliserListener(TARDIS plugin) {
        this.plugin = plugin;
        TREES.add(Material.OAK_SAPLING);
        TREES.add(Material.DARK_OAK_SAPLING);
        TREES.add(Material.ACACIA_SAPLING);
        TREES.add(Material.JUNGLE_SAPLING);
        TREES.add(Material.SPRUCE_SAPLING);
        TREES.add(Material.BIRCH_SAPLING);
        TREES.add(Material.CHORUS_FLOWER);
        TREES.add(Material.RED_MUSHROOM);
        TREES.add(Material.BROWN_MUSHROOM);
        TREES.add(Material.CRIMSON_FUNGUS);
        TREES.add(Material.WARPED_FUNGUS);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSuperFertilise(BlockFertilizeEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            ItemStack is = player.getInventory().getItemInMainHand();
            if (is != null && is.getType() == Material.BONE_MEAL && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("Super Fertiliser") && is.getItemMeta().hasCustomModelData()) {
                event.setCancelled(true);
                Block block = event.getBlock();
                boolean removeItem = false;
                if (plugin.getPluginRespect().getRespect(block.getLocation(), new Parameters(player, Flag.getNoMessageFlags()))) {
                    switch (block.getType()) {
                        case PUMPKIN_STEM:
                        case MELON_STEM:
                        case CARROTS:
                        case WHEAT:
                        case POTATOES:
                        case BEETROOTS:
                        case SWEET_BERRY_BUSH:
                            Ageable ageable = (Ageable) block.getBlockData();
                            ageable.setAge(ageable.getMaximumAge());
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> block.setBlockData(ageable), 3L);
                            removeItem = true;
                            break;
                        case BAMBOO_SAPLING:
                            Bamboo bamboo = (Bamboo) Material.BAMBOO.createBlockData();
                            bamboo.setAge(1);
                            bamboo.setStage(1);
                            block.setBlockData(bamboo);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                Block last = block;
                                for (int i = 1; i < 8; i++) {
                                    Block b = block.getRelative(BlockFace.UP, i);
                                    last = b;
                                    if (b.getType().isAir()) {
                                        b.setBlockData(bamboo);
                                    } else {
                                        break;
                                    }
                                }
                                bamboo.setLeaves(Bamboo.Leaves.LARGE);
                                last.setBlockData(bamboo);
                            }, 3L);
                            removeItem = true;
                            break;
                        default:
                            break;
                    }
                }
                if (removeItem && !player.getGameMode().equals(GameMode.CREATIVE)) {
                    int amount = is.getAmount() - 1;
                    if (amount > 0) {
                        player.getInventory().getItemInMainHand().setAmount(amount);
                    } else {
                        player.getInventory().setItemInMainHand(null);
                    }
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSuperFertiliseInteract(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block != null && TREES.contains(block.getType())) {
            Player player = event.getPlayer();
            ItemStack is = player.getInventory().getItemInMainHand();
            if (is != null && is.getType() == Material.BONE_MEAL && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("Super Fertiliser") && is.getItemMeta().hasCustomModelData()) {
                event.setCancelled(true);
                TreeType treeType = TREE_LOOKUP.get(block.getType());
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    block.setBlockData(TARDISConstants.AIR);
                    block.getWorld().generateTree(block.getLocation(), treeType);
                }, 3L);
                int amount = is.getAmount() - 1;
                if (amount > 0) {
                    player.getInventory().getItemInMainHand().setAmount(amount);
                } else {
                    player.getInventory().setItemInMainHand(null);
                }
                player.updateInventory();
            }
        }
    }
}
