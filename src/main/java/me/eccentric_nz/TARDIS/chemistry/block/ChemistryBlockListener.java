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
package me.eccentric_nz.TARDIS.chemistry.block;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.chemistry.compound.CompoundInventory;
import me.eccentric_nz.TARDIS.chemistry.constructor.ConstructorInventory;
import me.eccentric_nz.TARDIS.chemistry.element.ElementInventory;
import me.eccentric_nz.TARDIS.chemistry.lab.LabInventory;
import me.eccentric_nz.TARDIS.chemistry.product.ProductInventory;
import me.eccentric_nz.TARDIS.chemistry.reducer.ReducerInventory;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.customblocks.TARDISMushroomBlock;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ChemistryBlockListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<Material, String> blocks = new HashMap<>();

    public ChemistryBlockListener(TARDIS plugin) {
        this.plugin = plugin;
        blocks.put(Material.LIGHT_GRAY_CONCRETE, "Atomic elements");
        blocks.put(Material.ORANGE_CONCRETE, "Chemical compounds");
        blocks.put(Material.MAGENTA_CONCRETE, "Material reducer");
        blocks.put(Material.LIGHT_BLUE_CONCRETE, "Element constructor");
        blocks.put(Material.YELLOW_CONCRETE, "Lab table");
        blocks.put(Material.LIME_CONCRETE, "Product crafting");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChemistryBlockInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getHand().equals(EquipmentSlot.HAND)) {
            Block block = event.getClickedBlock();
            Material material = block.getType();
            if (!material.equals(Material.BARRIER)) {
                return;
            }
            // get the display item entity
            ItemDisplay display = TARDISDisplayItemUtils.get(block);
            if (display != null) {
                ItemStack is = display.getItemStack();
                if (is != null) {
                    String name = blocks.get(is.getType());
                    if (name == null) {
                        return;
                    }
                    Player player = event.getPlayer();
                    ItemStack[] menu;
                    Inventory inventory;
                    switch (is.getType()) {
                        case LIGHT_GRAY_CONCRETE -> {
                            // atomic elements
                            if (TARDISPermission.hasPermission(player, "tardis.chemistry.creative")) {
                                menu = new ElementInventory(plugin).getMenu();
                            } else {
                                TARDISMessage.send(player, "CHEMISTRY_SUB_PERM", "Atomic elements");
                                return;
                            }
                        }
                        case ORANGE_CONCRETE -> {
                            // chemical compounds
                            if (TARDISPermission.hasPermission(player, "tardis.compound.create")) {
                                menu = new CompoundInventory(plugin).getMenu();
                            } else {
                                TARDISMessage.send(player, "CHEMISTRY_SUB_PERM", "Chemical compounds");
                                return;
                            }
                        }
                        case MAGENTA_CONCRETE -> {
                            // reducer
                            if (TARDISPermission.hasPermission(player, "tardis.reducer.use")) {
                                menu = new ReducerInventory(plugin).getMenu();
                            } else {
                                TARDISMessage.send(player, "CHEMISTRY_SUB_PERM", "Material reducer");
                                return;
                            }
                        }
                        case LIGHT_BLUE_CONCRETE -> {
                            // constructor
                            if (TARDISPermission.hasPermission(player, "tardis.construct.build")) {
                                menu = new ConstructorInventory().getMenu();
                            } else {
                                TARDISMessage.send(player, "CHEMISTRY_SUB_PERM", "Element constructor");
                                return;
                            }
                        }
                        case YELLOW_CONCRETE -> {
                            // lab
                            if (TARDISPermission.hasPermission(player, "tardis.lab.combine")) {
                                menu = new LabInventory(plugin).getMenu();
                            } else {
                                TARDISMessage.send(player, "CHEMISTRY_SUB_PERM", "Lab table");
                                return;
                            }
                        }
                        default -> { // Product crafting
                            // product
                            if (TARDISPermission.hasPermission(player, "tardis.products.craft")) {
                                menu = new ProductInventory(plugin).getMenu();
                            } else {
                                TARDISMessage.send(player, "CHEMISTRY_SUB_PERM", "Product crafting");
                                return;
                            }
                        }
                    }
                    inventory = plugin.getServer().createInventory(player, (is.getType().equals(Material.LIGHT_GRAY_CONCRETE) ? 54 : 27), ChatColor.DARK_RED + name);
                    inventory.setContents(menu);
                    player.openInventory(inventory);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onChemistryBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        Block block = event.getBlock();
        Material mush = event.getBlock().getType();
        if (mush.equals(Material.RED_MUSHROOM_BLOCK) || mush.equals(Material.MUSHROOM_STEM)) {
            TARDISDisplayItem tdi = TARDISMushroomBlock.conversionMap.get(block.getBlockData().getAsString());
            if (tdi != null) {
                ItemStack is = new ItemStack(tdi.getMaterial(), 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(tdi.getDisplayName());
                im.setCustomModelData(tdi.getCustomModelData());
                im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, tdi.getCustomModelData());
                is.setItemMeta(im);
                block.setBlockData(TARDISConstants.AIR);
                block.getWorld().dropItemNaturally(event.getPlayer().getLocation(), is);
                if (tdi == TARDISDisplayItem.HEAT_BLOCK) {
                    plugin.getTrackerKeeper().getHeatBlocks().remove(block.getLocation().toString());
                }
            }
        }
    }

//    @EventHandler(ignoreCancelled = true)
//    public void onChemistryBlockPlace(BlockPlaceEvent event) {
//        ItemStack is = event.getItemInHand();
//        Material material = event.getBlock().getType();
//        if (is.hasItemMeta() && is.getItemMeta().getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.INTEGER) && !isMushroomBlock(material)) {
//            event.setCancelled(true);
//        }
//    }
}
