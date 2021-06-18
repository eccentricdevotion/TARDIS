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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.chemistry.block;

import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.chemistry.compound.CompoundInventory;
import me.eccentric_nz.tardis.chemistry.constructor.ConstructorInventory;
import me.eccentric_nz.tardis.chemistry.element.ElementInventory;
import me.eccentric_nz.tardis.chemistry.lab.LabInventory;
import me.eccentric_nz.tardis.chemistry.product.ProductInventory;
import me.eccentric_nz.tardis.chemistry.reducer.ReducerInventory;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Objects;

public class ChemistryBlockListener implements Listener {

    private final TardisPlugin plugin;
    private final HashMap<String, String> blocks = new HashMap<>();
    private final HashMap<String, Integer> models = new HashMap<>();

    public ChemistryBlockListener(TardisPlugin plugin) {
        this.plugin = plugin;
        blocks.put("minecraft:red_mushroom_block[down=true,east=true,north=true,south=false,up=false,west=false]", "Atomic elements");
        blocks.put("minecraft:red_mushroom_block[down=true,east=true,north=true,south=false,up=false,west=true]", "Chemical compounds");
        blocks.put("minecraft:red_mushroom_block[down=true,east=true,north=true,south=false,up=true,west=false]", "Material reducer");
        blocks.put("minecraft:red_mushroom_block[down=true,east=true,north=true,south=false,up=true,west=true]", "Element constructor");
        blocks.put("minecraft:red_mushroom_block[down=true,east=true,north=true,south=true,up=false,west=false]", "Lab table");
        blocks.put("minecraft:red_mushroom_block[down=true,east=true,north=true,south=true,up=false,west=true]", "Product crafting");
        blocks.put("minecraft:mushroom_stem[down=true,east=false,north=false,south=true,up=true,west=false]", "Blue Lamp");
        blocks.put("minecraft:mushroom_stem[down=true,east=false,north=false,south=true,up=true,west=true]", "Green Lamp");
        blocks.put("minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=false,west=false]", "Purple Lamp");
        blocks.put("minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=false,west=true]", "Red Lamp");
        blocks.put("minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=true,west=false]", "Blue Lamp");
        blocks.put("minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=true,west=true]", "Green Lamp");
        blocks.put("minecraft:mushroom_stem[down=true,east=false,north=true,south=true,up=false,west=false]", "Purple Lamp");
        blocks.put("minecraft:mushroom_stem[down=true,east=false,north=true,south=true,up=false,west=true]", "Red Lamp");
        blocks.put("minecraft:mushroom_stem[down=false,east=false,north=false,south=false,up=false,west=true]", "Heat Block");
        models.put("Atomic elements", 40);
        models.put("Chemical compounds", 41);
        models.put("Material reducer", 42);
        models.put("Element constructor", 43);
        models.put("Lab table", 44);
        models.put("Product crafting", 45);
        models.put("Blue Lamp", 1);
        models.put("Red Lamp", 2);
        models.put("Purple Lamp", 3);
        models.put("Green Lamp", 4);
        models.put("Heat Block", 5);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChemistryBlockInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && Objects.equals(event.getHand(), EquipmentSlot.HAND)) {
            Block block = event.getClickedBlock();
            assert block != null;
            Material material = block.getType();
            if (!material.equals(Material.RED_MUSHROOM_BLOCK)) {
                return;
            }
            String name = blocks.get(block.getBlockData().getAsString());
            if (name != null) {
                Player player = event.getPlayer();
                ItemStack[] menu;
                Inventory inventory;
                switch (name) {
                    case "Atomic elements":
                        // elements
                        if (TardisPermission.hasPermission(player, "tardis.chemistry.creative")) {
                            menu = new ElementInventory(plugin).getMenu();
                        } else {
                            TardisMessage.send(player, "CHEMISTRY_SUB_PERM", name);
                            return;
                        }
                        break;
                    case "Chemical compounds":
                        // compound
                        if (TardisPermission.hasPermission(player, "tardis.compound.create")) {
                            menu = new CompoundInventory(plugin).getMenu();
                        } else {
                            TardisMessage.send(player, "CHEMISTRY_SUB_PERM", name);
                            return;
                        }
                        break;
                    case "Material reducer":
                        // reducer
                        if (TardisPermission.hasPermission(player, "tardis.reducer.use")) {
                            menu = new ReducerInventory(plugin).getMenu();
                        } else {
                            TardisMessage.send(player, "CHEMISTRY_SUB_PERM", name);
                            return;
                        }
                        break;
                    case "Element constructor":
                        // constructor
                        if (TardisPermission.hasPermission(player, "tardis.construct.build")) {
                            menu = new ConstructorInventory().getMenu();
                        } else {
                            TardisMessage.send(player, "CHEMISTRY_SUB_PERM", name);
                            return;
                        }
                        break;
                    case "Lab table":
                        // lab
                        if (TardisPermission.hasPermission(player, "tardis.lab.combine")) {
                            menu = new LabInventory(plugin).getMenu();
                        } else {
                            TardisMessage.send(player, "CHEMISTRY_SUB_PERM", name);
                            return;
                        }
                        break;
                    default:
                        // product
                        if (TardisPermission.hasPermission(player, "tardis.products.craft")) {
                            menu = new ProductInventory(plugin).getMenu();
                        } else {
                            TardisMessage.send(player, "CHEMISTRY_SUB_PERM", name);
                            return;
                        }
                        break;
                }
                inventory = plugin.getServer().createInventory(player, (name.equals("Atomic elements") ? 54 : 27), ChatColor.DARK_RED + name);
                inventory.setContents(menu);
                player.openInventory(inventory);
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
            String name = blocks.get(block.getBlockData().getAsString());
            if (name != null) {
                ItemStack is = new ItemStack(mush, 1);
                ItemMeta im = is.getItemMeta();
                assert im != null;
                im.setDisplayName(name);
                int cmd = models.get(name);
                im.setCustomModelData(10000000 + cmd);
                im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, cmd);
                is.setItemMeta(im);
                block.setBlockData(TardisConstants.AIR);
                block.getWorld().dropItemNaturally(event.getPlayer().getLocation(), is);
                if (cmd == 5) {
                    plugin.getTrackerKeeper().getHeatBlocks().remove(block.getLocation().toString());
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onChemistryBlockPlace(BlockPlaceEvent event) {
        ItemStack is = event.getItemInHand();
        Material material = event.getBlock().getType();
        if (is.hasItemMeta() && Objects.requireNonNull(is.getItemMeta()).getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.INTEGER) && !isMushroomBlock(material)) {
            event.setCancelled(true);
        }
    }

    private boolean isMushroomBlock(Material material) {
        return switch (material) {
            case MUSHROOM_STEM, RED_MUSHROOM_BLOCK, BROWN_MUSHROOM_BLOCK -> true;
            default -> false;
        };
    }
}
