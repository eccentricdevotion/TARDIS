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

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chemistry.product.LampToggler;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoTracker;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.Updateable;
import me.eccentric_nz.TARDIS.update.UpdateDoor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

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
        ItemStack single = is.clone();
        single.setAmount(1);
        if (!is.hasItemMeta()) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if (!im.hasDisplayName() || !im.getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.INTEGER)) {
            return;
        }
        if (im.getDisplayName().equals(ChatColor.GOLD + "TARDIS Seed Block")) {
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
        if (which.isLight() || which == TARDISDisplayItem.DOOR) {
            if (which.isLight()) {
                Levelled light = TARDISConstants.LIGHT;
                light.setLevel((which.isLit() ? 15 : 0));
                data = light;
            } else {
                data = null;
            }
            // set an Interaction entity
            TARDISDisplayItemUtils.set(location, cmd, which == TARDISDisplayItem.DOOR);
        } else {
            data = TARDISConstants.BARRIER;
        }
        if (data != null) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                location.getBlock().setBlockData(data);
            }, 1L);
        }
        double ay = (which == TARDISDisplayItem.DOOR) ? 0.0d : 0.5d;
        ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location.add(0.5d, ay, 0.5d), EntityType.ITEM_DISPLAY);
        display.setItemStack(single);
        display.setPersistent(true);
        display.setInvulnerable(true);
        if (which == TARDISDisplayItem.DOOR) {
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);
        }
        if (player.getGameMode() != GameMode.CREATIVE) {
            int amount = is.getAmount() - 1;
            if (amount < 1) {
                player.getInventory().setItemInMainHand(null);
            } else {
                is.setAmount(amount);
                player.getInventory().setItemInMainHand(is);
            }
        }
    }

    /**
     * Remove an item display / interaction entity when breaking a TARDIS block
     * in creative gamemode.
     *
     * @param event The TARDIS block break event
     */
    @EventHandler
    public void onDisplayBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE) {
            return;
        }
        Block block = event.getBlock();
        if (block.getType() != Material.LIGHT && block.getType() != Material.BARRIER) {
            return;
        }
        TARDISDisplayItemUtils.remove(block);
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

    /**
     * Simulate breaking a TARDIS light block
     *
     * @param event an entity interact event
     */
    @EventHandler
    public void onInteractionClick(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof Interaction interaction) {
            // get the item display entity
            ItemDisplay display = TARDISDisplayItemUtils.get(interaction);
            if (display != null) {
                event.setCancelled(true);
                Player player = event.getPlayer();
                ItemStack inHand = player.getInventory().getItemInMainHand();
                if (isRedstoneSonic(inHand)) {
                    // toggle the lamp
                    ItemStack lamp = display.getItemStack();
                    Block light = interaction.getLocation().getBlock();
                    TARDISDisplayItem tdi = TARDISDisplayItemUtils.get(display);
                    // check the block is a chemistry lamp block
                    if (tdi != null && tdi.isLight()) {
                        TARDISDisplayItem toggled = TardisLight.getToggled(tdi);
                        ItemMeta im = lamp.getItemMeta();
                        ItemStack change = new ItemStack(toggled.getMaterial(), 1);
                        if (toggled.isLit()) {
                            // create light source
                            LampToggler.setLightlevel(light, 15);
                        } else {
                            // delete light source - should eventually get rid of this...
                            LampToggler.deleteLight(light);
                            // set light level to zero
                            LampToggler.setLightlevel(light, 0);
                        }
                        change.setItemMeta(im);
                        display.setItemStack(change);
                    }
                } else if (Tag.ITEMS_PICKAXES.isTagged(inHand.getType())) {
                    Location l = interaction.getLocation();
                    ItemDisplay breaking = null;
                    for (Entity e : l.getWorld().getNearbyEntities(l, 0.55d, 0.55d, 0.55d, (d) -> d.getType() == EntityType.ITEM_DISPLAY)) {
                        if (e instanceof ItemDisplay item) {
                            ItemStack stack = item.getItemStack();
                            if (stack.hasItemMeta()) {
                                if (stack.getItemMeta().getPersistentDataContainer().has(plugin.getDestroyKey(), PersistentDataType.INTEGER)) {
                                    breaking = item;
                                }
                            }
                        }
                    }
                    processInteraction(display, breaking, player, l, l.getBlock(), interaction);
                } else if (inHand.getType() == Material.END_ROD && player.hasPermission("tardis.admin")) {
                    // toggle custom model data of item display's item stack
                    ItemStack itemStack = display.getItemStack();
                    ItemMeta im = itemStack.getItemMeta();
                    int cmd = im.getCustomModelData();
                    switch (cmd) {
                        case 10001 -> cmd = 10002;
                        case 10002 -> cmd = 10003;
                        case 10005 -> cmd = 10010;
                        case 10010 -> cmd = 10005;
                        default -> cmd = 10001;
                    }
                    im.setCustomModelData(cmd);
                    itemStack.setItemMeta(im);
                    display.setItemStack(itemStack);
                } else {
                    TARDISDisplayItem tdi = TARDISDisplayItemUtils.get(display);
                    if (tdi != null && (tdi == TARDISDisplayItem.DOOR || tdi == TARDISDisplayItem.DOOR_OPEN || tdi == TARDISDisplayItem.DOOR_BOTH_OPEN)) {
                        if (!plugin.getUtils().inTARDISWorld(player)) {
                            return;
                        }
                        Block block = interaction.getLocation().getBlock();
                        UUID playerUUID = player.getUniqueId();
                        if (plugin.getTrackerKeeper().getUpdatePlayers().containsKey(player.getUniqueId())) {
                            String uuid = (TARDISSudoTracker.SUDOERS.containsKey(playerUUID)) ? TARDISSudoTracker.SUDOERS.get(playerUUID).toString() : playerUUID.toString();
                            HashMap<String, Object> where = new HashMap<>();
                            where.put("uuid", uuid);
                            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                            if (!rs.resultSet()) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                                plugin.getTrackerKeeper().getUpdatePlayers().remove(playerUUID);
                                return;
                            }
                            Tardis tardis = rs.getTardis();
                            int id = tardis.getTardis_id();
                            new UpdateDoor(plugin).process(Updateable.DOOR, block, false, id, player);
                            plugin.getTrackerKeeper().getUpdatePlayers().remove(playerUUID);
                            TARDISSudoTracker.SUDOERS.remove(playerUUID);
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_SET", "double door");
                            return;
                        }
                        if (player.isSneaking()) {
                            if (tdi == TARDISDisplayItem.DOOR) {
                                // move to outside
                                new DisplayItemDoorMover(plugin).exit(player, block);
                            }
                            if (tdi == TARDISDisplayItem.DOOR_OPEN) {
                                // open right hand door as well
                                ItemStack itemStack = display.getItemStack();
                                ItemMeta im = itemStack.getItemMeta();
                                im.setCustomModelData(10003);
                                itemStack.setItemMeta(im);
                                display.setItemStack(itemStack);
                                // close doors / decativate portal
                                new DisplayItemDoorToggler(plugin).openClose(player, block, true);
                            }
                        } else {
                            ItemStack itemStack = display.getItemStack();
                            ItemMeta im = itemStack.getItemMeta();
                            switch (tdi) {
                                case DOOR -> {
                                    // open doors / activate portal
                                    im.setCustomModelData(10002);
                                    new DisplayItemDoorToggler(plugin).openClose(player, block, false);
                                }
                                case DOOR_OPEN -> {
                                    // close doors / decativate portal
                                    im.setCustomModelData(10001);
                                    new DisplayItemDoorToggler(plugin).openClose(player, block, true);
                                }
                                default -> {
                                    // just close doors
                                    im.setCustomModelData(10001);
                                }
                            }
                            itemStack.setItemMeta(im);
                            display.setItemStack(itemStack);
                        }
                    } else if (player.isSneaking() && tdi.isLight()) {
                        Material toPlace = inHand.getType();
                        if (isPlaceable(toPlace)) {
                            // place block in hand on top
                            Block block = interaction.getLocation().getBlock().getRelative(BlockFace.UP);
                            if (block.getType().isAir()) {
                                block.setType(toPlace);
                                // if directional block rotate based on player direction
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                    BlockData data = block.getBlockData();
                                    if (data instanceof Directional directional) {
                                        directional.setFacing(data instanceof Stairs ? player.getFacing() : player.getFacing().getOppositeFace());
                                        block.setBlockData(data);
                                    }
                                    if (data instanceof Rotatable rotatable) {
                                        rotatable.setRotation(player.getFacing().getOppositeFace());
                                        block.setBlockData(data);
                                    }
                                }, 1);
                                if (player.getGameMode() == GameMode.SURVIVAL) {
                                    // remove a block from inventory
                                    int amount = inHand.getAmount() - 1;
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
                }
            }
        }
    }

    private boolean isPlaceable(Material material) {
        if (!material.isSolid() || Tag.DOORS.isTagged(material) || material.hasGravity() || Tag.PRESSURE_PLATES.isTagged(material)) {
            return false;
        }
        return true;
    }

    private boolean isRedstoneSonic(ItemStack is) {
        if (is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.hasDisplayName()) {
                if (ChatColor.stripColor(im.getDisplayName()).equals("Sonic Screwdriver")) {
                    List<String> lore = im.getLore();
                    return lore != null && lore.contains("Redstone Upgrade");
                }
            }
        }
        return false;
    }

    private void processInteraction(ItemDisplay fake, ItemDisplay breaking, Player player, Location l, Block block, Interaction interaction) {
        if (fake != null && player.getGameMode().equals(GameMode.CREATIVE)) {
            fake.remove();
            if (interaction != null) {
                interaction.remove();
            }
            block.setType(Material.AIR);
            return;
        }
        if (breaking != null && fake != null) {
            ItemStack is = breaking.getItemStack();
            int destroy = is.getItemMeta().getPersistentDataContainer().get(plugin.getDestroyKey(), PersistentDataType.INTEGER);
            if (destroy == 9) {
                if (player.getGameMode().equals(GameMode.SURVIVAL)) {
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
                // set a delayed task to reset the breaking animation
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    breaking.remove();
                }, 60);
            }
        } else if (breaking == null) {
            // only one item display entity...
            // so spawn a destroy entity
            ItemStack destroy = new ItemStack(Material.GRAVEL);
            ItemMeta dim = destroy.getItemMeta();
            dim.getPersistentDataContainer().set(plugin.getDestroyKey(), PersistentDataType.INTEGER, 0);
            dim.setCustomModelData(10000);
            destroy.setItemMeta(dim);
            Vector v = (interaction != null) ? new Vector(0, 0.5d, 0) : new Vector(0.5d, 0.5d, 0.5d);
            ItemDisplay display = (ItemDisplay) l.getWorld().spawnEntity(l.clone().add(v), EntityType.ITEM_DISPLAY);
            display.setItemStack(destroy);
            display.setPersistent(true);
            display.setInvulnerable(true);
        }
    }
}
