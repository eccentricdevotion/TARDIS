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
package me.eccentric_nz.tardischemistry.block;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.customblocks.TARDISMushroomBlock;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardischemistry.compound.CompoundInventory;
import me.eccentric_nz.tardischemistry.constructor.ConstructorInventory;
import me.eccentric_nz.tardischemistry.element.ElementInventory;
import me.eccentric_nz.tardischemistry.lab.LabInventory;
import me.eccentric_nz.tardischemistry.product.ProductInventory;
import me.eccentric_nz.tardischemistry.reducer.ReducerInventory;
import net.kyori.adventure.text.Component;
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
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashSet;
import java.util.Set;

public class ChemistryBlockListener implements Listener {

    private final TARDIS plugin;
    private final Set<Material> blocks = new HashSet<>();

    public ChemistryBlockListener(TARDIS plugin) {
        this.plugin = plugin;
        blocks.add(Material.LIGHT_GRAY_CONCRETE);
        blocks.add(Material.ORANGE_CONCRETE);
        blocks.add(Material.MAGENTA_CONCRETE);
        blocks.add(Material.LIGHT_BLUE_CONCRETE);
        blocks.add(Material.YELLOW_CONCRETE);
        blocks.add(Material.LIME_CONCRETE);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChemistryBlockInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getHand() != null && event.getHand().equals(EquipmentSlot.HAND)) {
            Block block = event.getClickedBlock();
            if (block == null) {
                return;
            }
            Material material = block.getType();
            if (!material.equals(Material.BARRIER)) {
                return;
            }
            // get the display item entity
            ItemDisplay display = TARDISDisplayItemUtils.get(block);
            if (display == null) {
                return;
            }
            ItemStack is = display.getItemStack();
            if (!blocks.contains(is.getType())) {
                return;
            }
            Player player = event.getPlayer();
            InventoryHolder menu;
            switch (is.getType()) {
                case LIGHT_GRAY_CONCRETE -> {
                    // atomic elements
                    if (TARDISPermission.hasPermission(player, "tardis.chemistry.creative")) {
                        menu = new ElementInventory(plugin);
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CHEMISTRY_SUB_PERM", "Atomic elements");
                        return;
                    }
                }
                case ORANGE_CONCRETE -> {
                    // chemical compounds
                    if (TARDISPermission.hasPermission(player, "tardis.compound.create")) {
                        menu = new CompoundInventory(plugin);
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CHEMISTRY_SUB_PERM", "Chemical compounds");
                        return;
                    }
                }
                case MAGENTA_CONCRETE -> {
                    // reducer
                    if (TARDISPermission.hasPermission(player, "tardis.reducer.use")) {
                        menu = new ReducerInventory(plugin);
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CHEMISTRY_SUB_PERM", "Material reducer");
                        return;
                    }
                }
                case LIGHT_BLUE_CONCRETE -> {
                    // constructor
                    if (TARDISPermission.hasPermission(player, "tardis.construct.build")) {
                        menu = new ConstructorInventory(plugin);
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CHEMISTRY_SUB_PERM", "Element constructor");
                        return;
                    }
                }
                case YELLOW_CONCRETE -> {
                    // lab
                    if (TARDISPermission.hasPermission(player, "tardis.lab.combine")) {
                        menu = new LabInventory(plugin);
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CHEMISTRY_SUB_PERM", "Lab table");
                        return;
                    }
                }
                default -> { // Product crafting
                    // product
                    if (TARDISPermission.hasPermission(player, "tardis.products.craft")) {
                        menu = new ProductInventory(plugin);
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CHEMISTRY_SUB_PERM", "Product crafting");
                        return;
                    }
                }
            }
            player.openInventory(menu.getInventory());
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
                ItemStack is = ItemStack.of(tdi.getMaterial(), 1);
                ItemMeta im = is.getItemMeta();
                im.displayName(Component.text(tdi.getDisplayName()));
                im.setItemModel(tdi.getCustomModel());
                im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, tdi.getCustomModel().getKey());
                is.setItemMeta(im);
                block.setBlockData(TARDISConstants.AIR);
                block.getWorld().dropItemNaturally(event.getPlayer().getLocation(), is);
                if (tdi == TARDISDisplayItem.HEAT_BLOCK) {
                    plugin.getTrackerKeeper().getHeatBlocks().remove(block.getLocation().toString());
                }
            }
        }
    }
}
