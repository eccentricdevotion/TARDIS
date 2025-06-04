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
package me.eccentric_nz.TARDIS.customblocks;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoTracker;
import me.eccentric_nz.TARDIS.custommodels.keys.BlockBreak;
import me.eccentric_nz.TARDIS.custommodels.keys.TardisDoorVariant;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.doors.Door;
import me.eccentric_nz.TARDIS.doors.DoorAnimator;
import me.eccentric_nz.TARDIS.doors.DoorLockAction;
import me.eccentric_nz.TARDIS.doors.DoorUtility;
import me.eccentric_nz.TARDIS.doors.inner.InnerDisplayDoorCloser;
import me.eccentric_nz.TARDIS.doors.inner.InnerDisplayDoorExtra;
import me.eccentric_nz.TARDIS.doors.inner.InnerDisplayDoorMover;
import me.eccentric_nz.TARDIS.doors.inner.InnerDisplayDoorOpener;
import me.eccentric_nz.TARDIS.doors.outer.*;
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
        if (!im.hasDisplayName() || !im.getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.STRING)) {
            return;
        }
        if (im.getDisplayName().equals(ChatColor.GOLD + "TARDIS Seed Block") || im.getDisplayName().endsWith("Console")) {
            return;
        }
        String key = im.getPersistentDataContainer().get(plugin.getCustomBlockKey(), PersistentDataType.STRING);
        NamespacedKey model = new NamespacedKey(plugin, key);
        TARDISDisplayItem which = TARDISDisplayItem.getByModel(model);
        if (which == null) {
            return;
        }
        Location location = event.getBlock().getLocation();
        event.setCancelled(true);
        BlockData data;
        if (which.isLight() || which == TARDISDisplayItem.DOOR || which == TARDISDisplayItem.CLASSIC_DOOR || which == TARDISDisplayItem.BONE_DOOR || which == TARDISDisplayItem.CONSOLE_LAMP) {
            if (which.isLight() || which == TARDISDisplayItem.CONSOLE_LAMP) {
                Levelled light = TARDISConstants.LIGHT;
                light.setLevel((which.isLit() ? 15 : 0));
                data = light;
            } else {
                data = null;
            }
            // set an Interaction entity
            TARDISDisplayItemUtils.set(location, model.getKey(), which == TARDISDisplayItem.DOOR || which == TARDISDisplayItem.CLASSIC_DOOR || which == TARDISDisplayItem.BONE_DOOR);
        } else {
            data = TARDISConstants.BARRIER;
        }
        if (data != null) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> location.getBlock().setBlockData(data), 1L);
        }
        double ay = (which == TARDISDisplayItem.DOOR || which == TARDISDisplayItem.CLASSIC_DOOR || which == TARDISDisplayItem.BONE_DOOR) ? 0.0d : 0.5d;
        // set an ItemDisplay entity
        ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location.add(0.5d, ay, 0.5d), EntityType.ITEM_DISPLAY);
        display.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, which.getCustomModel().getKey());
        display.setItemStack(single);
        display.setPersistent(true);
        display.setInvulnerable(true);
        if (which == TARDISDisplayItem.DOOR || which == TARDISDisplayItem.CLASSIC_DOOR || which == TARDISDisplayItem.BONE_DOOR || which == TARDISDisplayItem.TELEVISION) {
            if (which != TARDISDisplayItem.TELEVISION) {
                display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);
            }
            float yaw = DoorUtility.getLookAtYaw(player);
            // set display rotation
            display.setRotation(yaw, 0);
        }
        if (which == TARDISDisplayItem.ARTRON_CAPACITOR_STORAGE) {
            // is there an Artron Furnace in the surrounding blocks?
            Block furnace = ArtronFurnaceUtils.find(event.getBlock(), plugin);
            if (furnace != null) {
                // remember this Artron Capacitor Storage block
                ArtronFurnaceUtils.register(furnace.getLocation().toString(), player, plugin);
            }
        }
        if (which == TARDISDisplayItem.CONSOLE_LAMP) {
            // get player's tardis
            ResultSetTardisID rst = new ResultSetTardisID(plugin);
            if (rst.fromUUID(player.getUniqueId().toString())) {
                // upsert a control record
                plugin.getQueryFactory().insertSyncControl(rst.getTardisId(), 56, event.getBlockPlaced().getLocation().toString(), 0);
            }
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
        if (plugin.getArtronConfig().getBoolean("artron_furnace.tardis_powered")) {
            ArtronFurnaceUtils.removeFromCapacitor(block, event.getPlayer(), plugin);
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
                                    if (is.getItemMeta().getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.STRING)) {
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
                ItemStack dis = player.getInventory().getItemInMainHand();
                if (!dis.hasItemMeta()) {
                    return;
                }
                ItemMeta dim = dis.getItemMeta();
                if (!dim.hasItemModel() || !dim.getItemModel().getKey().contains("_closed")) {
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
            // is this a modelled police box interaction door?
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
                if (stand.getEquipment() == null) {
                    return;
                }
                int id = interaction.getPersistentDataContainer().get(plugin.getTardisIdKey(), PersistentDataType.INTEGER);
                // toggle the door
                new OuterDisplayDoorAction(plugin).processClick(id, player, stand);
            } else {
                // get the item display entity
                ItemDisplay display = TARDISDisplayItemUtils.get(interaction);
                if (display == null) {
                    return;
                }
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
                } else {
                    TARDISDisplayItem tdi = TARDISDisplayItemUtils.get(display);
                    if (tdi != null) {
                        // is it an inner door?
                        if (tdi == TARDISDisplayItem.CUSTOM_DOOR || tdi == TARDISDisplayItem.DOOR_BOTH_OPEN
                                || tdi == TARDISDisplayItem.DOOR || tdi == TARDISDisplayItem.DOOR_OPEN
                                || tdi == TARDISDisplayItem.CLASSIC_DOOR || tdi == TARDISDisplayItem.CLASSIC_DOOR_OPEN
                                || tdi == TARDISDisplayItem.BONE_DOOR || tdi == TARDISDisplayItem.BONE_DOOR_OPEN
                        ) {
                            if (!player.isOp() && !plugin.getUtils().inTARDISWorld(player)) {
                                return;
                            }
                            Block block = interaction.getLocation().getBlock();
                            UUID playerUUID = player.getUniqueId();
                            UUID uuid = (TARDISSudoTracker.SUDOERS.containsKey(playerUUID)) ? TARDISSudoTracker.SUDOERS.get(playerUUID) : playerUUID;
                            HashMap<String, Object> where = new HashMap<>();
                            where.put("uuid", uuid);
                            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                            if (!rs.resultSet()) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                                plugin.getTrackerKeeper().getUpdatePlayers().remove(playerUUID);
                                return;
                            }
                            Tardis tardis = rs.getTardis();
                            int id = tardis.getTardisId();
                            // is the player updating the door?
                            if (plugin.getTrackerKeeper().getUpdatePlayers().containsKey(playerUUID)) {
                                new UpdateDoor(plugin).process(Updateable.DOOR, block, false, id, player);
                                plugin.getTrackerKeeper().getUpdatePlayers().remove(playerUUID);
                                TARDISSudoTracker.SUDOERS.remove(playerUUID);
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_SET", "double door");
                                return;
                            }
                            // should we just teleport out?
                            if (player.isSneaking()) {
                                if (tdi == TARDISDisplayItem.DOOR || tdi == TARDISDisplayItem.CLASSIC_DOOR || tdi == TARDISDisplayItem.BONE_DOOR || (tdi == TARDISDisplayItem.CUSTOM_DOOR && isCustomClosed(display))) {
                                    // move to outside
                                    new InnerDisplayDoorMover(plugin).exit(player, block);
                                    return;
                                }
                                if (tdi == TARDISDisplayItem.DOOR_OPEN || (tdi == TARDISDisplayItem.CUSTOM_DOOR && !isCustomClosed(display))) {
                                    // open right hand door as well
                                    ItemStack itemStack = display.getItemStack();
                                    if (itemStack != null) {
                                        ItemMeta im = itemStack.getItemMeta();
                                        // get custom model data
                                        NamespacedKey cmd = tdi == TARDISDisplayItem.DOOR_OPEN ? TardisDoorVariant.TARDIS_DOOR_OPEN.getKey() : Door.getExtraModel(itemStack.getType());
                                        if (cmd != null) {
                                            im.setItemModel(cmd);
                                            itemStack.setItemMeta(im);
                                            display.setItemStack(itemStack);
                                            // close doors / deactivate portal
                                            new InnerDisplayDoorExtra(plugin).deactivate(block, id, playerUUID);
//                                            new DisplayItemDoorToggler(plugin).openClose(player, block, false, TARDISDisplayItem.DOOR_OPEN);
                                        }
                                    }
                                }
                            } else {
                                boolean outerDisplayDoor = tardis.getPreset().usesArmourStand();
                                // open door?
                                // check if door is deadlocked
                                ResultSetDeadlock rsd = new ResultSetDeadlock(plugin, display.getLocation());
                                if (rsd.resultSet() && rsd.isLocked()) {
                                    plugin.getMessenger().sendStatus(player, "DOOR_DEADLOCKED");
                                    return;
                                }
                                ItemStack itemStack = display.getItemStack();
                                if (itemStack != null) {
                                    ItemMeta im = itemStack.getItemMeta();
                                    boolean open = !im.getItemModel().getKey().contains("_closed");
                                    new DoorAnimator(plugin, display).animate(open);
                                    if (open) {
                                        // close iner
                                        new InnerDisplayDoorCloser(plugin).close(block, id, playerUUID, false);
                                        // close outer
                                        if (outerDisplayDoor) {
                                            new OuterDisplayDoorCloser(plugin).close(new OuterDoor(plugin, id).getDisplay(), id, playerUUID);
                                        } else if (tardis.getPreset().hasDoor()) {
                                            new OuterMinecraftDoorCloser(plugin).close(new OuterDoor(plugin, id).getMinecraft(), id, playerUUID);
                                        }
                                    } else {
                                        // open inner
                                        new InnerDisplayDoorOpener(plugin).open(block, id, false);
                                        // open outer
                                        if (outerDisplayDoor) {
                                            new OuterDisplayDoorOpener(plugin).open(new OuterDoor(plugin, id).getDisplay(), id);
                                        } else if (tardis.getPreset().hasDoor()) {
                                            new OuterMinecraftDoorOpener(plugin).open(new OuterDoor(plugin, id).getMinecraft(), id, player);
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
                        if (!im.hasItemModel()) {
                            return;
                        }
                        String model = im.getItemModel().getKey();
                        String[] split = model.split("_");
                        String last = split[split.length - 1];
                        NamespacedKey key = new NamespacedKey(plugin, model.replace(last, "closed"));
                        // set custom model to closed
                        im.setItemModel(key);
                        is.setItemMeta(im);
                        display.setItemStack(is);
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_DOUBLE_DOOR");
                        plugin.getTrackerKeeper().getUpdatePlayers().remove(player.getUniqueId());
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
        String cmd = im.hasItemModel() ? im.getItemModel().getKey() : "null";
        return cmd.endsWith("_open");
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
            // remove lamp/block records if light
            TARDISDisplayItem tdi = TARDISDisplayItemUtils.get(fake);
            if (tdi != null) {
                if (tdi.isLight()) {
                    new TARDISSonicLight(plugin).removeLamp(block, player);
                }
                if (tdi.isVariable()) {
                    // remove all item displays
                    TARDISDisplayItemUtils.remove(l.getBlock());
                }
            }
            return;
        }
        if (breaking != null && fake != null) {
            ItemStack is = breaking.getItemStack();
            if (is != null) {
                int destroy = is.getItemMeta().getPersistentDataContainer().get(plugin.getDestroyKey(), PersistentDataType.INTEGER);
                if (destroy == 9) {
                    TARDISDisplayItem tdi = TARDISDisplayItemUtils.get(fake);
                    if (player.getGameMode().equals(GameMode.SURVIVAL) && fake.getItemStack() != null) {
                        if (tdi != null && tdi.isVariable()) {
                            // get material
                            for (Entity e : block.getWorld().getNearbyEntities(block.getLocation().add(0.5d, 0.5d, 0.5d), 0.55d, 0.55d, 0.55d, (d) -> d.getType() == EntityType.ITEM_DISPLAY)) {
                                if (e != fake && e != breaking) {
                                    ItemDisplay vd = (ItemDisplay) e;
                                    ItemStack vis = vd.getItemStack();
                                    if (vis != null) {
                                        Material variable = vis.getType();
                                        ItemStack ret = new ItemStack(Material.GLASS, 1);
                                        ItemMeta im = ret.getItemMeta();
                                        im.setDisplayName(ChatColor.WHITE + "Variable Light");
                                        im.setLore(List.of(variable.toString()));
                                        im.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.INTEGER, 1003);
                                        ret.setItemMeta(im);
                                        l.getWorld().dropItemNaturally(l, ret);
                                    }
                                }
                            }
                        } else {
                            l.getWorld().dropItemNaturally(l, fake.getItemStack());
                        }
                    }
                    breaking.remove();
                    fake.remove();
                    if (interaction != null) {
                        interaction.remove();
                    }
                    block.setType(Material.AIR);
                    // remove lamp/block records if light
                    if (tdi != null) {
                        if (tdi.isLight()) {
                            new TARDISSonicLight(plugin).removeLamp(block, player);
                        }
                        if (tdi.isVariable()) {
                            // remove all item displays
                            TARDISDisplayItemUtils.remove(l.getBlock());
                        }
                    }
                    // remove furnace record
                    if (plugin.getArtronConfig().getBoolean("artron_furnace.tardis_powered")) {
                        ArtronFurnaceUtils.removeFromCapacitor(block, player, plugin);
                    }
                } else {
                    // update breaking item stack
                    ItemMeta im = is.getItemMeta();
                    NamespacedKey model = new NamespacedKey(plugin, "destroy_" + (destroy + 1));
                    im.getPersistentDataContainer().set(plugin.getDestroyKey(), PersistentDataType.INTEGER, destroy + 1);
                    im.setItemModel(model);
                    is.setItemMeta(im);
                    breaking.setItemStack(is);
                    // set a delayed task to reset the breaking animation
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, breaking::remove, 60);
                }
            }
        } else if (breaking == null) {
            // only one item display entity...
            // so spawn a destroy entity
            ItemStack destroy = new ItemStack(Material.GRAVEL);
            ItemMeta dim = destroy.getItemMeta();
            dim.getPersistentDataContainer().set(plugin.getDestroyKey(), PersistentDataType.INTEGER, 0);
            dim.setItemModel(BlockBreak.DESTROY_0.getKey());
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
