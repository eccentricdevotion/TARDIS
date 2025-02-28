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
package me.eccentric_nz.TARDIS.move;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.TARDISEmergencyRelocation;
import me.eccentric_nz.TARDIS.control.TARDISPowerButton;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.doors.DoorLockAction;
import me.eccentric_nz.TARDIS.doors.DoorUtility;
import me.eccentric_nz.TARDIS.doors.inner.*;
import me.eccentric_nz.TARDIS.doors.outer.*;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.flight.TARDISTakeoff;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFarmer;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFollowerSpawner;
import me.eccentric_nz.TARDIS.mobfarming.TARDISPetsAndFollowers;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

/**
 * During TARDIS operation, a distinctive grinding and whirring sound is usually
 * heard. River Song once demonstrated a TARDIS was capable of materialising
 * silently, teasing the Doctor that the noise was actually caused by him
 * leaving the brakes on.
 *
 * @author eccentric_nz
 */
public class TARDISDoorWalkListener extends TARDISDoorListener implements Listener {

    public TARDISDoorWalkListener(TARDIS plugin) {
        super(plugin);
    }

    /**
     * Listens for player interaction with TARDIS doors. If the door is
     * right-clicked with the TARDIS key (configurable) it will teleport the
     * player either into or out of the TARDIS.
     *
     * @param event a player clicking a block
     */
    @EventHandler(ignoreCancelled = true)
    public void onDoorInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            // only proceed if they are clicking a door!
            if (Tag.DOORS.isTagged(blockType) || Tag.TRAPDOORS.isTagged(blockType) || Tag.BUTTONS.isTagged(blockType)) {
                Player player = event.getPlayer();
                if (TARDISPermission.hasPermission(player, "tardis.enter")) {
                    Action action = event.getAction();
                    UUID playerUUID = player.getUniqueId();
                    World playerWorld = player.getLocation().getWorld();
                    Location block_loc = block.getLocation();
                    String bw = block_loc.getWorld().getName();
                    int bx = block_loc.getBlockX();
                    int by = block_loc.getBlockY();
                    int bz = block_loc.getBlockZ();
                    if (Tag.DOORS.isTagged(blockType)) {
                        Bisected bisected = (Bisected) block.getBlockData();
                        if (bisected.getHalf().equals(Bisected.Half.TOP)) {
                            by = (by - 1);
                            block = block.getRelative(BlockFace.DOWN);
                        }
                    }
                    String doorloc = bw + ":" + bx + ":" + by + ":" + bz;
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("door_location", doorloc);
                    ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
                    if (rsd.resultSet()) {
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setUseItemInHand(Event.Result.DENY);
                        event.setCancelled(true);
                        int id = rsd.getTardis_id();
                        if (plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_MAT");
                            return;
                        }
                        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "LOST_IN_VORTEX");
                            return;
                        }
                        COMPASS dd = rsd.getDoor_direction();
                        int doortype = rsd.getDoor_type();
                        int end_doortype = switch (doortype) {
                            case 0 -> 1;  // outside preset door
                            case 2 -> 3;  // outside backdoor
                            case 3 -> 2;  // inside backdoor
                            default -> 0; // 1, 4 TARDIS inside door, secondary inside door
                        };
                        ItemStack stack = player.getInventory().getItemInMainHand();
                        Material material = stack.getType();
                        // get key material
                        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, playerUUID.toString());
                        String key;
                        boolean willFarm = false;
                        boolean canPowerUp = false;
                        boolean userQuotes = true;
                        if (rsp.resultSet()) {
                            key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("preferences.key");
                            willFarm = rsp.isFarmOn();
                            userQuotes = rsp.isQuotesOn();
                            if (rsp.isAutoPowerUp() && plugin.getConfig().getBoolean("allow.power_down")) {
                                // check TARDIS is not abandoned
                                HashMap<String, Object> tid = new HashMap<>();
                                tid.put("tardis_id", id);
                                ResultSetTardis rs = new ResultSetTardis(plugin, tid, "", false, 2);
                                if (rs.resultSet()) {
                                    canPowerUp = !rs.getTardis().isAbandoned();
                                }
                            }
                        } else {
                            key = plugin.getConfig().getString("preferences.key");
                        }
                        boolean minecart = rsp.isMinecartOn();
                        Material m = Material.getMaterial(key);
                        if (action == Action.LEFT_CLICK_BLOCK) {
                            new DoorLockAction(plugin).lockUnlock(player, m, id, rsd.isLocked(), true);
                        }
                        if (action == Action.RIGHT_CLICK_BLOCK && !player.isSneaking() && !Tag.BUTTONS.isTagged(blockType)) {
                            if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                plugin.getMessenger().sendStatus(player, "SIEGE_NO_EXIT");
                                return;
                            }
                            if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_MAT");
                                return;
                            }
                            // handbrake must be on
                            HashMap<String, Object> tid = new HashMap<>();
                            tid.put("tardis_id", id);
                            ResultSetTardis rs = new ResultSetTardis(plugin, tid, "", false, 2);
                            if (rs.resultSet()) {
                                if (!rs.getTardis().isHandbrakeOn()) {
                                    plugin.getMessenger().sendStatus(player, "HANDBRAKE_ENGAGE");
                                    return;
                                }
                                // must be Time Lord or companion
                                ResultSetCompanions rsc = new ResultSetCompanions(plugin, id);
                                if (rsc.getCompanions().contains(playerUUID) || rs.getTardis().isAbandoned()) {
                                    if (!rsd.isLocked()) {
                                        // toggle the door open/closed
                                        if (Tag.DOORS.isTagged(blockType) || blockType.equals(Material.OAK_TRAPDOOR)) {
                                            if (doortype == 0 || doortype == 1) {
                                                boolean open = TARDISStaticUtils.isDoorOpen(block);
                                                if ((!material.equals(m) && doortype == 0 && !open) && !plugin.getConfig().getBoolean("preferences.any_key")) {
                                                    // must use key to open the outer door
                                                    if (!material.equals(Material.BLAZE_ROD) || !TARDISPermission.hasPermission(player, "tardis.sonic.admin")) {
                                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_KEY", key);
                                                    }
                                                    return;
                                                }
                                                if (open && rs.getTardis().isAbandoned()) {
                                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ABANDONED_DOOR");
                                                    return;
                                                }
                                                if (plugin.getTrackerKeeper().getHasClickedHandbrake().contains(id) && doortype == 1) {
                                                    plugin.getTrackerKeeper().getHasClickedHandbrake().removeAll(Collections.singleton(id));
                                                    // toggle handbrake && dematerialise
                                                    new TARDISTakeoff(plugin).run(id, player, rs.getTardis().getBeacon());
                                                }
                                                // toggle the door
                                                DoorUtility.playDoorSound(player, open, block.getLocation(), minecart);
                                                if (doortype == 1) {
                                                    boolean outerDisplayDoor = rs.getTardis().getPreset().usesArmourStand();
                                                    // inner door
                                                    if (open) {
                                                        // close door
                                                        new InnerMinecraftDoorCloser(plugin).close(block, id, playerUUID);
                                                        // close doors / deactivate portal
                                                        if (outerDisplayDoor) {
                                                            new OuterDisplayDoorCloser(plugin).close(new OuterDoor(plugin, id).getDisplay(), id, playerUUID);
                                                        } else if (rs.getTardis().getPreset().hasDoor()) {
                                                            new OuterMinecraftDoorCloser(plugin).close(new OuterDoor(plugin, id).getMinecraft(rs.getTardis().getPreset()), id, playerUUID);
                                                        }
                                                    } else {
                                                        // open inner
                                                        new InnerMinecraftDoorOpener(plugin).open(block, id, playerUUID);
                                                        // open outer
                                                        if (outerDisplayDoor) {
                                                            new OuterDisplayDoorOpener(plugin).open(new OuterDoor(plugin, id).getDisplay(), id, playerUUID);
                                                        } else if (rs.getTardis().getPreset().hasDoor()) {
                                                            new OuterMinecraftDoorOpener(plugin).open(new OuterDoor(plugin, id).getMinecraft(rs.getTardis().getPreset()), id, player);
                                                        }
                                                    }
                                                } else {
                                                    Inner innerDisplayDoor = new InnerDoor(plugin, id).get();
                                                    // outer door
                                                    if (open) {
                                                        // close outer
                                                        new OuterMinecraftDoorCloser(plugin).close(block, id, playerUUID);
                                                        // close inner
                                                        if (innerDisplayDoor.display()) {
                                                            new InnerDisplayDoorCloser(plugin).close(innerDisplayDoor.block(), id, playerUUID, true);
                                                        } else {
                                                            new InnerMinecraftDoorCloser(plugin).close(innerDisplayDoor.block(), id, playerUUID);
                                                        }
                                                    } else {
                                                        // open outer
                                                        new OuterMinecraftDoorOpener(plugin).open(block, id, player);
                                                        // open inner
                                                        if (innerDisplayDoor.display()) {
                                                            new InnerDisplayDoorOpener(plugin).open(innerDisplayDoor.block(), id, playerUUID, true);
                                                        } else {
                                                            new InnerMinecraftDoorOpener(plugin).open(innerDisplayDoor.block(), id, playerUUID);
                                                        }
                                                    }
                                                }
                                            }
                                        } else if (Tag.TRAPDOORS.isTagged(blockType)) {
                                            TrapDoor door_data = (TrapDoor) block.getBlockData();
                                            door_data.setOpen(!door_data.isOpen());
                                        }
                                    } else if (!rs.getTardis().getUuid().equals(playerUUID)) {
                                        plugin.getMessenger().sendStatus(player, "DOOR_DEADLOCKED");
                                    } else {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "DOOR_NEED_UNLOCK");
                                    }
                                } else {
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_COMPANION");
                                }
                            }
                        } else if (action == Action.RIGHT_CLICK_BLOCK && (player.isSneaking() || Tag.BUTTONS.isTagged(blockType))) {
                            if ((!material.equals(m) && doortype == 0) && !plugin.getConfig().getBoolean("preferences.any_key")) {
                                // must use key to open and close the outer door
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_KEY", key);
                                return;
                            }
                            if (rsd.isLocked()) {
                                plugin.getMessenger().sendStatus(player, "DOOR_DEADLOCKED");
                                return;
                            }
                            if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                plugin.getMessenger().sendStatus(player, "SIEGE_NO_EXIT");
                                return;
                            }
                            HashMap<String, Object> tid = new HashMap<>();
                            tid.put("tardis_id", id);
                            ResultSetTardis rs = new ResultSetTardis(plugin, tid, "", false, 2);
                            if (rs.resultSet()) {
                                Tardis tardis = rs.getTardis();
                                if (!tardis.isHandbrakeOn()) {
                                    plugin.getMessenger().sendStatus(player, "HANDBRAKE_ENGAGE");
                                    return;
                                }
                                int artron = tardis.getArtronLevel();
                                int required = plugin.getArtronConfig().getInt("backdoor");
                                UUID tlUUID = tardis.getUuid();
                                ChameleonPreset preset = tardis.getPreset();
                                float yaw = player.getLocation().getYaw();
                                float pitch = player.getLocation().getPitch();
                                String companions = tardis.getCompanions();
                                boolean hb = tardis.isHandbrakeOn();
                                boolean po = !tardis.isPoweredOn() && !tardis.isAbandoned();
                                ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, tardis.getTardisId());
                                if (!rsc.resultSet()) {
                                    // emergency TARDIS relocation
                                    new TARDISEmergencyRelocation(plugin).relocate(id, player);
                                    return;
                                }
                                COMPASS d_backup = rsc.getDirection();
                                // get players direction
                                COMPASS pd = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
                                // get the other door direction
                                COMPASS d;
                                HashMap<String, Object> other = new HashMap<>();
                                other.put("tardis_id", id);
                                other.put("door_type", end_doortype);
                                ResultSetDoors rse = new ResultSetDoors(plugin, other, false);
                                if (rse.resultSet() && !tardis.getPreset().usesArmourStand()) {
                                    d = rse.getDoor_direction();
                                } else {
                                    d = d_backup;
                                }
                                switch (doortype) {
                                    case 1, 4 -> {
                                        // is the TARDIS materialising?
                                        if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "LOST_IN_VORTEX");
                                            return;
                                        }
                                        // Can't SHIFT-click if INVISIBLE preset
                                        if (preset.equals(ChameleonPreset.INVISIBLE)) {
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "INVISIBILITY_SNEAK");
                                            return;
                                        }
                                        Location exitLoc;
                                        // player is in the TARDIS - always exit to current location
                                        Block door_bottom;
                                        Door door = (Door) block.getBlockData();
                                        door_bottom = (door.getHalf().equals(Bisected.Half.TOP)) ? block.getRelative(BlockFace.DOWN) : block;
                                        boolean opened = TARDISStaticUtils.isDoorOpen(door_bottom);
                                        if (opened && preset.hasDoor()) {
                                            exitLoc = TARDISStaticLocationGetters.getLocationFromDB(rse.getDoor_location());
                                        } else {
                                            exitLoc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ(), yaw, pitch);
                                        }
                                        if (hb && exitLoc != null) {
                                            // change the yaw if the door directions are different
                                            if (!dd.equals(d)) {
                                                yaw += adjustYaw(dd, d);
                                            }
                                            exitLoc.setYaw(yaw);
                                            // get location from database
                                            // make location safe i.e. on the outside of the blue box
                                            double ex = exitLoc.getX();
                                            double ez = exitLoc.getZ();
                                            if (opened) {
                                                exitLoc.setX(ex + 0.5);
                                                exitLoc.setZ(ez + 0.5);
                                            } else {
                                                switch (d) {
                                                    case NORTH_WEST -> {
                                                        exitLoc.setX(ex + 2);
                                                        exitLoc.setZ(ez + 2);
                                                    }
                                                    case NORTH -> {
                                                        exitLoc.setX(ex + 0.5);
                                                        exitLoc.setZ(ez + 2.5);
                                                    }
                                                    case NORTH_EAST -> {
                                                        exitLoc.setX(ex - 1);
                                                        exitLoc.setZ(ez + 2);
                                                    }
                                                    case EAST -> {
                                                        exitLoc.setX(ex - 1.5);
                                                        exitLoc.setZ(ez + 0.5);
                                                    }
                                                    case SOUTH_EAST -> {
                                                        exitLoc.setX(ex - 1);
                                                        exitLoc.setZ(ez - 1);
                                                    }
                                                    case SOUTH -> {
                                                        exitLoc.setX(ex + 0.5);
                                                        exitLoc.setZ(ez - 1.5);
                                                    }
                                                    case SOUTH_WEST -> {
                                                        exitLoc.setX(ex + 2);
                                                        exitLoc.setZ(ez - 1);
                                                    }
                                                    // WEST
                                                    default -> {
                                                        exitLoc.setX(ex + 2.5);
                                                        exitLoc.setZ(ez + 0.5);
                                                    }
                                                }
                                            }
                                            // exit TARDIS!
                                            movePlayer(player, exitLoc, true, playerWorld, userQuotes, 2, minecart, false);
                                            if (plugin.getConfig().getBoolean("allow.mob_farming") && TARDISPermission.hasPermission(player, "tardis.farm")) {
                                                TARDISFarmer tf = new TARDISFarmer(plugin);
                                                TARDISPetsAndFollowers petsAndFollowers = tf.exitPets(player);
                                                if (petsAndFollowers != null) {
                                                    if (!petsAndFollowers.getPets().isEmpty()) {
                                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> movePets(petsAndFollowers.getPets(), exitLoc, player, d, false), 10L);
                                                    }
                                                    if (!petsAndFollowers.getFollowers().isEmpty()) {
                                                        new TARDISFollowerSpawner(plugin).spawn(petsAndFollowers.getFollowers(), exitLoc, player, d, false);
                                                    }
                                                }
                                            }
                                            // remove player from traveller table
                                            removeTraveller(playerUUID);
                                        } else {
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "LOST_IN_VORTEX");
                                        }
                                    }
                                    case 0 -> {
                                        // is the TARDIS materialising?
                                        if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "LOST_IN_VORTEX");
                                            return;
                                        }
                                        boolean chkCompanion = false;
                                        if (!playerUUID.equals(tlUUID)) {
                                            if (companions != null && !companions.isEmpty()) {
                                                // is the player in the companion list
                                                if (companions.equalsIgnoreCase("everyone")) {
                                                    chkCompanion = true;
                                                } else {
                                                    String[] companionData = companions.split(":");
                                                    for (String c : companionData) {
                                                        if (c.equalsIgnoreCase(playerUUID.toString())) {
                                                            chkCompanion = true;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (playerUUID.equals(tlUUID) || chkCompanion || TARDISPermission.hasPermission(player, "tardis.skeletonkey") || tardis.isAbandoned()) {
                                            // get INNER TARDIS location
                                            TARDISDoorLocation idl = getDoor(1, id);
                                            Location tardis_loc = idl.getL();
                                            World cw = idl.getW();
                                            COMPASS innerD = idl.getD();
                                            // check for entities near the police box
                                            TARDISPetsAndFollowers petsAndFollowers = null;
                                            if (plugin.getConfig().getBoolean("allow.mob_farming") && TARDISPermission.hasPermission(player, "tardis.farm") && !plugin.getTrackerKeeper().getFarming().contains(playerUUID) && willFarm) {
                                                plugin.getTrackerKeeper().getFarming().add(playerUUID);
                                                TARDISFarmer tf = new TARDISFarmer(plugin);
                                                petsAndFollowers = tf.farmAnimals(block_loc, d, id, player.getPlayer(), tardis_loc.getWorld().getName(), playerWorld.getName());
                                            }
                                            // if WorldGuard is on the server check for TARDIS region protection and add admin as member
                                            if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard") && TARDISPermission.hasPermission(player, "tardis.skeletonkey")) {
                                                plugin.getWorldGuardUtils().addMemberToRegion(cw, tardis.getOwner(), player.getUniqueId());
                                            }
                                            // enter TARDIS!
                                            cw.getChunkAt(tardis_loc).load();
                                            tardis_loc.setPitch(pitch);
                                            // get inner door direction, so we can adjust yaw if necessary
                                            if (!innerD.equals(pd)) {
                                                yaw += adjustYaw(pd, innerD);
                                            }
                                            tardis_loc.setYaw(yaw);
                                            movePlayer(player, tardis_loc, false, playerWorld, userQuotes, 1, minecart, false);
                                            if (petsAndFollowers != null) {
                                                if (!petsAndFollowers.getPets().isEmpty()) {
                                                    movePets(petsAndFollowers.getPets(), tardis_loc, player, d, true);
                                                }
                                                if (!petsAndFollowers.getFollowers().isEmpty()) {
                                                    new TARDISFollowerSpawner(plugin).spawn(petsAndFollowers.getFollowers(), tardis_loc, player, d, true);
                                                }
                                            }
                                            if (canPowerUp && po) {
                                                // power up the TARDIS
                                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISPowerButton(plugin, id, player, tardis.getPreset(), false, tardis.isHidden(), tardis.isLightsOn(), player.getLocation(), artron, tardis.getSchematic().getLights()).clickButton(), 20L);
                                            }
                                            // put player into travellers table
                                            // remove them first as they may have exited incorrectly, and we only want them listed once
                                            removeTraveller(playerUUID);
                                            HashMap<String, Object> set = new HashMap<>();
                                            set.put("tardis_id", id);
                                            set.put("uuid", playerUUID.toString());
                                            plugin.getQueryFactory().doSyncInsert("travellers", set);
                                        } else {
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_COMPANION");
                                        }
                                    }
                                    case 2 -> {
                                        if (artron < required) {
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_DOOR_ENERGY");
                                            return;
                                        }
                                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_ENTER");
                                            return;
                                        }
                                        if (preset.equals(ChameleonPreset.JUNK_MODE)) {
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "JUNK_NO_ENTRY");
                                            return;
                                        }
                                        // always enter by the back door
                                        TARDISDoorLocation ibdl = getDoor(3, id);
                                        Location inner_loc = ibdl.getL();
                                        if (inner_loc == null) {
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "DOOR_BACK_IN");
                                            return;
                                        }
                                        COMPASS ibdd = ibdl.getD();
                                        COMPASS ipd = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, true));
                                        if (!ibdd.equals(ipd)) {
                                            yaw += adjustYaw(ipd, ibdd) + 180F;
                                        }
                                        inner_loc.setYaw(yaw);
                                        inner_loc.setPitch(pitch);
                                        movePlayer(player, inner_loc, false, playerWorld, userQuotes, 1, minecart, false);
                                        // put player into travellers table
                                        removeTraveller(playerUUID);
                                        HashMap<String, Object> set = new HashMap<>();
                                        set.put("tardis_id", id);
                                        set.put("uuid", playerUUID.toString());
                                        plugin.getQueryFactory().doSyncInsert("travellers", set);
                                        HashMap<String, Object> wheree = new HashMap<>();
                                        wheree.put("tardis_id", id);
                                        int cost = (-plugin.getArtronConfig().getInt("backdoor"));
                                        plugin.getQueryFactory().alterEnergyLevel("tardis", cost, wheree, player);
                                    }
                                    case 3 -> {
                                        if (artron < required) {
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_DOOR_ENERGY");
                                            return;
                                        }
                                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                            plugin.getMessenger().sendStatus(player, "SIEGE_NO_EXIT");
                                            return;
                                        }
                                        // always exit to outer back door
                                        TARDISDoorLocation obdl = getDoor(2, id);
                                        Location outer_loc = obdl.getL();
                                        if (outer_loc == null) {
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "DOOR_BACK_OUT");
                                            return;
                                        }
                                        // backdoor is located in the end
                                        if (outer_loc.getWorld().getEnvironment().equals(Environment.THE_END)) {
                                            // check enabled
                                            if (!plugin.getConfig().getBoolean("travel.the_end")) {
                                                plugin.getMessenger().send(player, TardisModule.TARDIS, "ANCIENT", "End");
                                                return;
                                            }
                                            // check permission
                                            if (!TARDISPermission.hasPermission(player, "tardis.end")) {
                                                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_TRAVEL", "End");
                                                return;
                                            }
                                            // check traveled to
                                            if (plugin.getConfig().getBoolean("travel.allow_end_after_visit") && !new ResultSetTravelledTo(plugin).resultSet(playerUUID.toString(), "THE_END")) {
                                                plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NOT_VISITED", "End");
                                                return;
                                            }
                                        }
                                        // backdoor located in the nether
                                        if (outer_loc.getWorld().getEnvironment().equals(Environment.NETHER)) {
                                            // check enabled
                                            if (!plugin.getConfig().getBoolean("travel.nether")) {
                                                plugin.getMessenger().send(player, TardisModule.TARDIS, "ANCIENT", "Nether");
                                                return;
                                            }
                                            // check permission
                                            if (!TARDISPermission.hasPermission(player, "tardis.nether")) {
                                                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_TRAVEL", "Nether");
                                                return;
                                            }
                                            // check traveled to
                                            if (plugin.getConfig().getBoolean("travel.allow_nether_after_visit") && !new ResultSetTravelledTo(plugin).resultSet(playerUUID.toString(), "NETHER")) {
                                                plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NOT_VISITED", "Nether");
                                                return;
                                            }
                                        }
                                        COMPASS obdd = obdl.getD();
                                        COMPASS opd = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
                                        if (!obdd.equals(opd)) {
                                            yaw += adjustYaw(opd, obdd);
                                        }
                                        outer_loc.setYaw(yaw);
                                        outer_loc.setPitch(pitch);
                                        movePlayer(player, outer_loc, true, playerWorld, userQuotes, 2, minecart, false);
                                        // remove player from traveller table
                                        removeTraveller(playerUUID);
                                        // take energy
                                        HashMap<String, Object> wherea = new HashMap<>();
                                        wherea.put("tardis_id", id);
                                        int costa = (-plugin.getArtronConfig().getInt("backdoor"));
                                        plugin.getQueryFactory().alterEnergyLevel("tardis", costa, wherea, player);
                                    }
                                    default -> {
                                        // do nothing
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
