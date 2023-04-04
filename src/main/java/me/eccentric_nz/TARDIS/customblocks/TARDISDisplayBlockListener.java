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
package me.eccentric_nz.TARDIS.customblocks;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chemistry.product.LampToggler;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Light;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * @author eccentric_nz
 */
public class TARDISDisplayBlockListener implements Listener {

    private final TARDIS plugin;

    public TARDISDisplayBlockListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Place an item display entity and a barrier block to simulate a custom
     * TARDIS block.
     *
     * @param event The TARDIS block placement event
     */
    @EventHandler
    public void onDisplayBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (!is.hasItemMeta()) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if (!im.hasDisplayName() || !im.getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.INTEGER)) {
            return;
        }
        int cmd = im.getPersistentDataContainer().get(plugin.getCustomBlockKey(), PersistentDataType.INTEGER);
        TARDISDisplayItem which = TARDISDisplayItem.getByMaterialAndData(is.getType(), cmd);
        if (which == null) {
            return;
        }
        Location location = event.getBlock().getLocation();
        event.setCancelled(true);
        BlockData data;
        if (which.isLight()) {
            Light light = (Light) Material.LIGHT.createBlockData();
            light.setLevel((which.isLit() ? 15 : 0));
            data = light;
            // set an Interaction entity
            TARDISDisplayItemUtils.set(location, cmd);
        } else {
            data = TARDISConstants.BARRIER;
        }
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            location.getBlock().setBlockData(data);
        }, 1L);
        ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location.add(0.5d, 0.5d, 0.5d), EntityType.ITEM_DISPLAY);
        display.setItemStack(is);
        display.setPersistent(true);
        display.setInvulnerable(true);
        int amount = is.getAmount() - 1;
        if (amount < 1) {
            player.getInventory().setItemInMainHand(null);
        } else {
            is.setAmount(amount);
            player.getInventory().setItemInMainHand(is);
        }
    }

    /**
     * Simulate breaking a TARDIS display block
     *
     * @param event a block interact event
     */
    @EventHandler
    public void onDisplayBlockInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND) || !event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block != null) {
            Player player = event.getPlayer();
            if (block.getType().equals(Material.BARRIER) && Tag.ITEMS_PICKAXES.isTagged(player.getInventory().getItemInMainHand().getType())) {
                Location l = block.getLocation();
                ItemDisplay breaking = null;
                ItemDisplay fake = null;
                for (Entity e : l.getWorld().getNearbyEntities(block.getBoundingBox().expand(0.1d), (d) -> d.getType() == EntityType.ITEM_DISPLAY)) {
                    if (e instanceof ItemDisplay display) {
                        ItemStack is = display.getItemStack();
                        if (is.hasItemMeta()) {
                            if (is.getItemMeta().getPersistentDataContainer().has(plugin.getDestroyKey(), PersistentDataType.INTEGER)) {
                                breaking = display;
                            }
                            if (is.getItemMeta().getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.INTEGER)) {
                                fake = display;
                            }
                        }
                    }
                }
                processInteraction(fake, breaking, player, l, block, null);
            }
        }
    }

    @EventHandler
    public void onInteractionClick(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof Interaction interaction) {
            // get the item display entity
            ItemDisplay display = TARDISDisplayItemUtils.get(interaction);
            if (display != null) {
                Player player = event.getPlayer();
                ItemStack inHand = player.getInventory().getItemInMainHand();
                if (isSonic(inHand)) {
                    // toggle the lamp
                    ItemStack is = display.getItemStack();
                    ItemStack change = null;
                    Block light = interaction.getLocation().getBlock();
                    if (is != null) {
                        ItemMeta im = is.getItemMeta();
                        // check the block is a chemistry lamp block
                        if (is.getType() == Material.SEA_LANTERN) {
                            change = new ItemStack(Material.REDSTONE_LAMP, 1);
                            // delete light source - should eventually get rid of this...
                            LampToggler.deleteLight(light);
                            // set light level to zero
                            LampToggler.setLightlevel(light, 0);
                        } else if (is.getType() == Material.REDSTONE_LAMP) {
                            change = new ItemStack(Material.SEA_LANTERN, 1);
                            // create light source
                            LampToggler.setLightlevel(light, 15);
                        }
                        if (change != null) {
                            change.setItemMeta(im);
                            display.setItemStack(change);
                        }
                    }
                } else if (Tag.ITEMS_PICKAXES.isTagged(inHand.getType())) {
                    Location l = display.getLocation();
                    Block block = l.getBlock();
                    ItemDisplay breaking = null;
                    ItemDisplay fake = null;
                    for (Entity e : l.getWorld().getNearbyEntities(block.getBoundingBox().expand(0.1d), (d) -> d.getType() == EntityType.ITEM_DISPLAY)) {
                        if (e instanceof ItemDisplay item) {
                            ItemStack is = display.getItemStack();
                            if (is.hasItemMeta()) {
                                if (is.getItemMeta().getPersistentDataContainer().has(plugin.getDestroyKey(), PersistentDataType.INTEGER)) {
                                    breaking = item;
                                }
                                if (is.getItemMeta().getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.INTEGER)) {
                                    fake = item;
                                }
                            }
                        }
                    }
                    processInteraction(fake, breaking, player, l, block, interaction);
                }
            }
        }
    }

    private boolean isSonic(ItemStack is) {
        if (is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.hasDisplayName()) {
                return (ChatColor.stripColor(im.getDisplayName()).equals("Sonic Screwdriver"));
            }
        }
        return false;
    }

    private void processInteraction(ItemDisplay fake, ItemDisplay breaking, Player player, Location l, Block block, Interaction interaction) {
        if (fake != null && player.getGameMode().equals(GameMode.CREATIVE)) {
            fake.remove();
            block.setType(Material.AIR);
            return;
        }
        if (breaking != null && fake != null) {
            ItemStack is = breaking.getItemStack();
            int destroy = is.getItemMeta().getPersistentDataContainer().get(plugin.getDestroyKey(), PersistentDataType.INTEGER);
            if (destroy > 4) {
                if (player.getGameMode().equals(GameMode.SURVIVAL)) {
                    // TODO fix item duplication
                    l.getWorld().dropItemNaturally(l, fake.getItemStack());
                }
                breaking.remove();
                fake.remove();
                if (interaction != null) {
                    interaction.remove();
                }
                block.setType(Material.AIR);
            } else {
                // update breaking item stack
                ItemMeta im = is.getItemMeta();
                im.getPersistentDataContainer().set(plugin.getDestroyKey(), PersistentDataType.INTEGER, destroy + 1);
                im.setCustomModelData(im.getCustomModelData() + 1);
                is.setItemMeta(im);
                breaking.setItemStack(is);
                // set a delayed task to reset the breaking animation?
            }
        } else if (fake != null) {
            // only one item display entity...
            // so spawn a destroy entity
            ItemStack is = new ItemStack(Material.GRAVEL);
            ItemMeta im = is.getItemMeta();
            im.getPersistentDataContainer().set(plugin.getDestroyKey(), PersistentDataType.INTEGER, 1);
            im.setCustomModelData(10001);
            is.setItemMeta(im);
            ItemDisplay display = (ItemDisplay) l.getWorld().spawnEntity(l.add(0.5d, 0.5d, 0.5d), EntityType.ITEM_DISPLAY);
            display.setItemStack(is);
            display.setPersistent(true);
            display.setInvulnerable(true);
        }
    }
}
