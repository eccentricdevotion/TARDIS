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
package me.eccentric_nz.TARDIS.customblocks;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoTracker;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDeadlock;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.doors.*;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.Updateable;
import me.eccentric_nz.TARDIS.sonic.actions.TARDISSonicLight;
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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISDisplayBlockListener implements Listener {

    private final TARDIS plugin;

    public TARDISDisplayBlockListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Place an item display entity and a barrier block to simulate a custom TARDIS block.
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
        if (which.isLight() || which == TARDISDisplayItem.DOOR || which == TARDISDisplayItem.CLASSIC_DOOR) {
            if (which.isLight()) {
                Levelled light = TARDISConstants.LIGHT;
                light.setLevel((which.isLit() ? 15 : 0));
                data = light;
            } else {
                data = null;
            }
            // set an Interaction entity
            TARDISDisplayItemUtils.set(location, cmd, which == TARDISDisplayItem.DOOR || which == TARDISDisplayItem.CLASSIC_DOOR);
        } else {
            data = TARDISConstants.BARRIER;
        }
        if (data != null) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> location.getBlock().setBlockData(data), 1L);
        }
        double ay = (which == TARDISDisplayItem.DOOR || which == TARDISDisplayItem.CLASSIC_DOOR) ? 0.0d : 0.5d;
        // set an ItemDisplay entity
        ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location.add(0.5d, ay, 0.5d), EntityType.ITEM_DISPLAY);
        display.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, which.getCustomModelData());
        display.setItemStack(single);
        display.setPersistent(true);
        display.setInvulnerable(true);
        if (which == TARDISDisplayItem.DOOR || which == TARDISDisplayItem.CLASSIC_DOOR) {
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);
            float yaw = DoorUtility.getLookAtYaw(player);
            // set display rotation
            display.setRotation(yaw, 0);
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
     * Remove an item display / interaction entity when breaking a TARDIS block in creative gamemode.
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
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block != null) {
            Player player = event.getPlayer();
            if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                if (block.getType().equals(Material.BARRIER) && Tag.ITEMS_PICKAXES.isTagged(player.getInventory().getItemInMainHand().getType())) {
                    Location l = block.getLocation();
                    ItemDisplay breaking = null;
                    ItemDisplay fake = null;
                    for (Entity e : l.getWorld().getNearbyEntities(block.getBoundingBox().expand(0.1d), (d) -> d.getType() == EntityType.ITEM_DISPLAY)) {
                        if (e instanceof ItemDisplay display) {
                            ItemStack is = display.getItemStack();
                            if (is != null) {
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
                    }
                    processInteraction(fake, breaking, player, l, block, null);
                }
            }
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && Tag.ITEMS_DECORATED_POT_SHERDS.isTagged(player.getInventory().getItemInMainHand().getType())) {
                // check custom model data == 10000
                ItemStack dis = player.getInventory().getItemInMainHand();
                if (!dis.hasItemMeta()) {
                    return;
                }
                ItemMeta dim = dis.getItemMeta();
                if (!dim.hasCustomModelData() || dim.getCustomModelData() != 10000) {
                    return;
                }
                // set a door
                event.setCancelled(true);
                Location location = event.getClickedBlock().getRelative(BlockFace.UP).getLocation();
                DoorUtility.set(plugin, player, location);
                if (player.getGameMode() != GameMode.CREATIVE) {
                    int amount = dis.getAmount() - 1;
                    if (amount < 1) {
                        player.getInventory().setItemInMainHand(null);
                    } else {
                        dis.setAmount(amount);
                        player.getInventory().setItemInMainHand(dis);
                    }
                }
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
        if (event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        if (event.getRightClicked() instanceof Interaction interaction) {
            if (interaction.getPersistentDataContainer().has(plugin.getStandUuidKey(), plugin.getPersistentDataTypeUUID())) {
                Player player = event.getPlayer();
                UUID uuid = interaction.getPersistentDataContainer().get(plugin.getStandUuidKey(), plugin.getPersistentDataTypeUUID());
                if (uuid == null) {
                    return;
                }
                ArmorStand stand = (ArmorStand) plugin.getServer().getEntity(uuid);
                if (stand == null) {
                    return;
                }
                int id = interaction.getPersistentDataContainer().get(plugin.getTardisIdKey(), PersistentDataType.INTEGER);
                // toggle the door
                new DoorToggleAction(plugin).openClose(id, player, stand);
            } else {
                // get the item display entity
                ItemDisplay display = TARDISDisplayItemUtils.get(interaction);
                if (display != null) {
                    event.setCancelled(true);
                    Player player = event.getPlayer();
                    ItemStack inHand = player.getInventory().getItemInMainHand();
                    if (isRedstoneSonic(inHand)) {
                        TARDISSonicLight tsl = new TARDISSonicLight(plugin);
                        if (player.isSneaking() && plugin.getConfig().getBoolean("allow.add_lights")) {
                            // add the light to the lamps table
                            tsl.addLamp(interaction.getLocation().getBlock(), player);
                        } else {
                            // toggle the lamp
                            tsl.toggle(display, interaction.getLocation().getBlock());
                        }
                    } else if (Tag.ITEMS_PICKAXES.isTagged(inHand.getType())) {
                        Location l = interaction.getLocation();
                        ItemDisplay breaking = null;
                        for (Entity e : l.getWorld().getNearbyEntities(l, 0.55d, 0.55d, 0.55d, (d) -> d.getType() == EntityType.ITEM_DISPLAY)) {
                            if (e instanceof ItemDisplay item) {
                                ItemStack stack = item.getItemStack();
                                if (stack != null && stack.hasItemMeta()) {
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
                        if (itemStack != null) {
                            ItemMeta im = itemStack.getItemMeta();
                            int cmd = im.getCustomModelData();
                            cmd++;
                            if (cmd > 10010) {
                                cmd = 10000;
                            }
                            im.setCustomModelData(cmd);
                            itemStack.setItemMeta(im);
                            display.setItemStack(itemStack);
                        }
                    } else {
                        TARDISDisplayItem tdi = TARDISDisplayItemUtils.get(display);
                        if (tdi != null) {
                            if (tdi == TARDISDisplayItem.CUSTOM_DOOR || tdi == TARDISDisplayItem.DOOR || tdi == TARDISDisplayItem.DOOR_OPEN || tdi == TARDISDisplayItem.DOOR_BOTH_OPEN || tdi == TARDISDisplayItem.CLASSIC_DOOR || tdi == TARDISDisplayItem.CLASSIC_DOOR_OPEN) {
                                if (!player.isOp() && !plugin.getUtils().inTARDISWorld(player)) {
                                    return;
                                }
                                Block block = interaction.getLocation().getBlock();
                                UUID playerUUID = player.getUniqueId();
                                if (plugin.getTrackerKeeper().getUpdatePlayers().containsKey(playerUUID)) {
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
                                    int id = tardis.getTardisId();
                                    new UpdateDoor(plugin).process(Updateable.DOOR, block, false, id, player);
                                    plugin.getTrackerKeeper().getUpdatePlayers().remove(playerUUID);
                                    TARDISSudoTracker.SUDOERS.remove(playerUUID);
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_SET", "double door");
                                    return;
                                }
                                if (player.isSneaking()) {
                                    if (tdi == TARDISDisplayItem.DOOR || tdi == TARDISDisplayItem.CLASSIC_DOOR || (tdi == TARDISDisplayItem.CUSTOM_DOOR && isCustomClosed(display))) {
                                        // move to outside
                                        new DisplayItemDoorMover(plugin).exit(player, block);
                                        return;
                                    }
                                    if (tdi == TARDISDisplayItem.DOOR_OPEN || (tdi == TARDISDisplayItem.CUSTOM_DOOR && !isCustomClosed(display))) {
                                        // open right hand door as well
                                        ItemStack itemStack = display.getItemStack();
                                        if (itemStack != null) {
                                            ItemMeta im = itemStack.getItemMeta();
                                            // get custom model data
                                            int cmd = tdi == TARDISDisplayItem.DOOR_OPEN ? 10005 : Door.getCMD(itemStack.getType());
                                            if (cmd != -1) {
                                                im.setCustomModelData(cmd);
                                                itemStack.setItemMeta(im);
                                                display.setItemStack(itemStack);
                                                // close doors / deactivate portal
                                                new DisplayItemDoorToggler(plugin).openClose(player, block, true, TARDISDisplayItem.DOOR_OPEN);
                                            }
                                        }
                                    }
                                } else {
                                    // check if door is deadlocked
                                    ResultSetDeadlock rsd = new ResultSetDeadlock(plugin, display.getLocation());
                                    if (rsd.resultSet() && rsd.isLocked()) {
                                        plugin.getMessenger().sendStatus(player, "DOOR_DEADLOCKED");
                                        return;
                                    }
                                    ItemStack itemStack = display.getItemStack();
                                    if (itemStack != null) {
                                        ItemMeta im = itemStack.getItemMeta();
                                        switch (tdi) {
                                            case DOOR -> {
                                                // open doors / activate portal
                                                new DoorAnimator(plugin, display).open();
                                                new DisplayItemDoorToggler(plugin).openClose(player, block, false, TARDISDisplayItem.DOOR);
                                            }
                                            case DOOR_OPEN, DOOR_BOTH_OPEN -> {
                                                // close doors / deactivate portal
                                                new DoorAnimator(plugin, display).close();
                                                new DisplayItemDoorToggler(plugin).openClose(player, block, true, TARDISDisplayItem.DOOR_OPEN);
                                            }
                                            case CLASSIC_DOOR -> {
                                                // open doors / activate portal
                                                new DoorAnimator(plugin, display).open();
                                                new DisplayItemDoorToggler(plugin).openClose(player, block, false, TARDISDisplayItem.CLASSIC_DOOR);
                                            }
                                            case CLASSIC_DOOR_OPEN -> {
                                                // close doors / deactivate portal
                                                new DoorAnimator(plugin, display).close();
                                                new DisplayItemDoorToggler(plugin).openClose(player, block, true, TARDISDisplayItem.CLASSIC_DOOR_OPEN);
                                            }
                                            case CUSTOM_DOOR -> {
                                                // get if door is open
                                                boolean close = im.getCustomModelData() > 10000;
                                                DoorAnimator animator = new DoorAnimator(plugin, display);
                                                if (close) {
                                                    animator.close();
                                                } else {
                                                    animator.open();
                                                }
                                                new DisplayItemDoorToggler(plugin).openClose(player, block, close, TARDISDisplayItem.CUSTOM_DOOR);
                                            }
                                        }
                                    }
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
                        } else if (plugin.getTrackerKeeper().getUpdatePlayers().containsKey(player.getUniqueId())) {
                            // check if display is double door
                            ItemStack is = display.getItemStack();
                            if (!is.hasItemMeta()) {
                                return;
                            }
                            ItemMeta im = is.getItemMeta();
                            if (!im.hasCustomModelData()) {
                                return;
                            }
                            int cmd = im.getCustomModelData();
                            if (cmd == 10001 || cmd == 10002 || cmd == 10003) {
                                // set custom model data to 10000
                                im.setCustomModelData(10000);
                                is.setItemMeta(im);
                                display.setItemStack(is);
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_DOUBLE_DOOR");
                                plugin.getTrackerKeeper().getUpdatePlayers().remove(player.getUniqueId());
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isCustomClosed(ItemDisplay display) {
        if (display == null) {
            return false;
        }
        ItemStack is = display.getItemStack();
        if (is == null) {
            return false;
        }
        ItemMeta im = is.getItemMeta();
        int cmd = im.hasCustomModelData()? im.getCustomModelData() : -1;
        return cmd == 10000;
    }

    private boolean isPlaceable(Material material) {
        return material.isSolid() && !Tag.DOORS.isTagged(material) && !material.hasGravity() && !Tag.PRESSURE_PLATES.isTagged(material);
    }

    private boolean isRedstoneSonic(ItemStack is) {
        if (is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.hasDisplayName()) {
                if (im.getDisplayName().endsWith("Sonic Screwdriver")) {
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
            // remove lamp record if light
            TARDISDisplayItem tdi = TARDISDisplayItemUtils.get(fake);
            if (tdi != null && tdi.isLight()) {
                new TARDISSonicLight(plugin).removeLamp(block, player);
            }
            return;
        }
        if (breaking != null && fake != null) {
            ItemStack is = breaking.getItemStack();
            if (is != null) {
                int destroy = is.getItemMeta().getPersistentDataContainer().get(plugin.getDestroyKey(), PersistentDataType.INTEGER);
                if (destroy == 9) {
                    if (player.getGameMode().equals(GameMode.SURVIVAL) && fake.getItemStack() != null) {
                        l.getWorld().dropItemNaturally(l, fake.getItemStack());
                    }
                    breaking.remove();
                    fake.remove();
                    if (interaction != null) {
                        interaction.remove();
                    }
                    block.setType(Material.AIR);
                    // remove lamp record if light
                    TARDISDisplayItem tdi = TARDISDisplayItemUtils.get(fake);
                    if (tdi != null && tdi.isLight()) {
                        new TARDISSonicLight(plugin).removeLamp(block, player);
                    }
                } else {
                    // update breaking item stack
                    ItemMeta im = is.getItemMeta();
                    im.getPersistentDataContainer().set(plugin.getDestroyKey(), PersistentDataType.INTEGER, destroy + 1);
                    im.setCustomModelData(im.getCustomModelData() + 1);
                    is.setItemMeta(im);
                    breaking.setItemStack(is);
                    // set a delayed task to reset the breaking animation
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> breaking.remove(), 60);
                }
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

    @EventHandler
    public void onDoorInteract(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player && event.getEntity() instanceof Interaction interaction && interaction.getPersistentDataContainer().has(plugin.getTardisIdKey(), PersistentDataType.INTEGER)) {
            int id = interaction.getPersistentDataContainer().get(plugin.getTardisIdKey(), PersistentDataType.INTEGER);
            if (plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_MAT");
                return;
            }
            if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "LOST_IN_VORTEX");
                return;
            }
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            where.put("door_type", !plugin.getUtils().inTARDISWorld(player) ? 0 : 1);
            ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
            if (rsd.resultSet()) {
                event.setCancelled(true);
                // get key material
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
                String key;
                if (rsp.resultSet()) {
                    key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("preferences.key");
                } else {
                    key = plugin.getConfig().getString("preferences.key");
                }
                Material m = Material.getMaterial(key);
                new DoorLockAction(plugin).lockUnlock(player, m, id, rsd.isLocked(), !plugin.getConfig().getBoolean("preferences.open_door_policy"));
            }
        }
    }
}
