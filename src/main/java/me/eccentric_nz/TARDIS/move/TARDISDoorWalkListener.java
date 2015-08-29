/*
 * Copyright (C) 2014 eccentric_nz
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

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISEmergencyRelocation;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCompanions;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFarmer;
import me.eccentric_nz.TARDIS.mobfarming.TARDISMob;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISResourcePackChanger;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Door;

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
    @SuppressWarnings("deprecation")
    @EventHandler(ignoreCancelled = true)
    public void onDoorInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            // only proceed if they are clicking a door!
            if (plugin.getGeneralKeeper().getDoors().contains(blockType) || blockType.equals(Material.TRAP_DOOR)) {
                final Player player = event.getPlayer();
                if (player.hasPermission("tardis.enter")) {
                    Action action = event.getAction();
                    final UUID playerUUID = player.getUniqueId();
                    World playerWorld = player.getLocation().getWorld();
                    Location block_loc = block.getLocation();
                    byte doorData = block.getData();
                    String bw = block_loc.getWorld().getName();
                    int bx = block_loc.getBlockX();
                    int by = block_loc.getBlockY();
                    int bz = block_loc.getBlockZ();
                    if (doorData >= 8 && !blockType.equals(Material.TRAP_DOOR)) {
                        by = (by - 1);
                        block = block.getRelative(BlockFace.DOWN);
                    }
                    String doorloc = bw + ":" + bx + ":" + by + ":" + bz;
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("door_location", doorloc);
                    ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
                    if (rsd.resultSet()) {
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setUseItemInHand(Event.Result.DENY);
                        event.setCancelled(true);
                        final int id = rsd.getTardis_id();
                        if (plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                            TARDISMessage.send(player, "NOT_WHILE_MAT");
                            return;
                        }
                        QueryFactory qf = new QueryFactory(plugin);
                        COMPASS dd = rsd.getDoor_direction();
                        int doortype = rsd.getDoor_type();
                        int end_doortype;
                        switch (doortype) {
                            case 0: // outside preset door
                                end_doortype = 1;
                                break;
                            case 2: // outside backdoor
                                end_doortype = 3;
                                break;
                            case 3: // inside backdoor
                                end_doortype = 2;
                                break;
                            default: // 1, 4 TARDIS inside door, secondary inside door
                                end_doortype = 0;
                                break;
                        }
                        ItemStack stack = player.getItemInHand();
                        Material material = stack.getType();
                        // get key material
                        HashMap<String, Object> wherepp = new HashMap<String, Object>();
                        wherepp.put("uuid", playerUUID.toString());
                        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
                        String key;
                        boolean hasPrefs = false;
                        boolean willFarm = false;
                        if (rsp.resultSet()) {
                            hasPrefs = true;
                            key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("preferences.key");
                            willFarm = rsp.isFarmOn();
                        } else {
                            key = plugin.getConfig().getString("preferences.key");
                        }
                        final boolean minecart = rsp.isMinecartOn();
                        Material m = Material.getMaterial(key);
                        if (action == Action.LEFT_CLICK_BLOCK) {
                            // must be the owner
                            HashMap<String, Object> oid = new HashMap<String, Object>();
                            oid.put("uuid", player.getUniqueId().toString());
                            ResultSetTardis rs = new ResultSetTardis(plugin, oid, "", false);
                            if (rs.resultSet()) {
                                if (rs.getTardis_id() != id) {
                                    TARDISMessage.send(player, "DOOR_LOCK_UNLOCK");
                                    return;
                                }
                                // must use key to lock / unlock door
                                if (material.equals(m)) {
                                    int locked = (rsd.isLocked()) ? 0 : 1;
                                    String message = (rsd.isLocked()) ? plugin.getLanguage().getString("DOOR_UNLOCK") : plugin.getLanguage().getString("DOOR_DEADLOCK");
                                    HashMap<String, Object> setl = new HashMap<String, Object>();
                                    setl.put("locked", locked);
                                    HashMap<String, Object> wherel = new HashMap<String, Object>();
                                    wherel.put("tardis_id", rsd.getTardis_id());
                                    // always lock / unlock both doors
                                    qf.doUpdate("doors", setl, wherel);
                                    TARDISMessage.send(player, "DOOR_LOCK", message);
                                }
                            }
                        }
                        if (action == Action.RIGHT_CLICK_BLOCK && !player.isSneaking()) {
                            if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                TARDISMessage.send(player, "SIEGE_NO_EXIT");
                                return;
                            }
                            if (plugin.getTrackerKeeper().getInVortex().contains(id)) {
                                TARDISMessage.send(player, "NOT_WHILE_MAT");
                                return;
                            }
                            // handbrake must be on
                            HashMap<String, Object> tid = new HashMap<String, Object>();
                            tid.put("tardis_id", id);
                            ResultSetTardis rs = new ResultSetTardis(plugin, tid, "", false);
                            if (rs.resultSet()) {
                                if (!rs.isHandbrake_on()) {
                                    TARDISMessage.send(player, "HANDBRAKE_ENGAGE");
                                    return;
                                }
                                // must be Time Lord or companion
                                ResultSetCompanions rsc = new ResultSetCompanions(plugin, id);
                                if (rsc.getCompanions().contains(playerUUID)) {
                                    if (!rsd.isLocked()) {
                                        // toogle the door open/closed
                                        if (plugin.getGeneralKeeper().getDoors().contains(blockType)) {
                                            if (doortype == 0 || doortype == 1) {
                                                boolean open = TARDISStaticUtils.isOpen(block, dd);
                                                if (!material.equals(m) && doortype == 0 && !open) {
                                                    // must use key to open the outer door
                                                    String[] split = plugin.getRecipesConfig().getString("shaped.Sonic Screwdriver.result").split(":");
                                                    Material sonic = Material.valueOf(split[0]);
                                                    if (!material.equals(sonic) || !player.hasPermission("tardis.sonic.admin")) {
                                                        TARDISMessage.send(player, "NOT_KEY", key);
                                                    }
                                                    return;
                                                }
                                                // toggle the door
                                                new TARDISDoorToggler(plugin, block, player, minecart, open, id).toggleDoors();
                                            }
                                        } else if (blockType.equals(Material.TRAP_DOOR)) {
                                            int open = 1;
                                            byte door_data = block.getData();
                                            switch (dd) {
                                                case NORTH:
                                                    if (door_data == 1) {
                                                        block.setData((byte) 5, false);
                                                    } else {
                                                        block.setData((byte) 1, false);
                                                        open = 2;
                                                    }
                                                    break;
                                                case WEST:
                                                    if (door_data == 3) {
                                                        block.setData((byte) 7, false);
                                                    } else {
                                                        block.setData((byte) 3, false);
                                                        open = 2;
                                                    }
                                                    break;
                                                case SOUTH:
                                                    if (door_data == 0) {
                                                        block.setData((byte) 4, false);
                                                    } else {
                                                        block.setData((byte) 0, false);
                                                        open = 2;
                                                    }
                                                    break;
                                                default:
                                                    if (door_data == 2) {
                                                        block.setData((byte) 6, false);
                                                    } else {
                                                        block.setData((byte) 2, false);
                                                        open = 2;
                                                    }
                                                    break;
                                            }
                                            playDoorSound(player, open, player.getLocation(), minecart);
                                        }
                                    } else {
                                        if (rs.getUuid() != playerUUID) {
                                            TARDISMessage.send(player, "DOOR_DEADLOCKED");
                                        } else {
                                            TARDISMessage.send(player, "DOOR_UNLOCK");
                                        }
                                    }
                                }
                            }
                        } else if (action == Action.RIGHT_CLICK_BLOCK && player.isSneaking()) {
                            if (!material.equals(m) && doortype == 0) {
                                // must use key to open and close the outer door
                                TARDISMessage.send(player, "NOT_KEY", key);
                                return;
                            }
                            if (rsd.isLocked()) {
                                TARDISMessage.send(player, "DOOR_DEADLOCKED");
                                return;
                            }
                            if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                TARDISMessage.send(player, "SIEGE_NO_EXIT");
                                return;
                            }
                            HashMap<String, Object> tid = new HashMap<String, Object>();
                            tid.put("tardis_id", id);
                            ResultSetTardis rs = new ResultSetTardis(plugin, tid, "", false);
                            if (rs.resultSet()) {
                                int artron = rs.getArtron_level();
                                int required = plugin.getArtronConfig().getInt("backdoor");
                                UUID tlUUID = rs.getUuid();
                                PRESET preset = rs.getPreset();
                                float yaw = player.getLocation().getYaw();
                                float pitch = player.getLocation().getPitch();
                                String companions = rs.getCompanions();
                                boolean hb = rs.isHandbrake_on();
                                HashMap<String, Object> wherecl = new HashMap<String, Object>();
                                wherecl.put("tardis_id", rs.getTardis_id());
                                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                                if (!rsc.resultSet()) {
                                    // emergency TARDIS relocation
                                    new TARDISEmergencyRelocation(plugin).relocate(id, player);
                                    return;
                                }
                                COMPASS d_backup = rsc.getDirection();
                                // get quotes player prefs
                                boolean userQuotes;
                                boolean userTP;
                                if (hasPrefs) {
                                    userQuotes = rsp.isQuotesOn();
                                    userTP = rsp.isTextureOn();
                                } else {
                                    userQuotes = true;
                                    userTP = false;
                                }
                                // get players direction
                                COMPASS pd = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
                                // get the other door direction
                                final COMPASS d;
                                HashMap<String, Object> other = new HashMap<String, Object>();
                                other.put("tardis_id", id);
                                other.put("door_type", end_doortype);
                                ResultSetDoors rse = new ResultSetDoors(plugin, other, false);
                                if (rse.resultSet()) {
                                    d = rse.getDoor_direction();
                                } else {
                                    d = d_backup;
                                }
                                switch (doortype) {
                                    case 1:
                                    case 4:
                                        // is the TARDIS materialising?
                                        if (plugin.getTrackerKeeper().getInVortex().contains(id)) {
                                            TARDISMessage.send(player, "LOST_IN_VORTEX");
                                            return;
                                        }
                                        Location exitLoc;
                                        // player is in the TARDIS - always exit to current location
                                        Block door_bottom;
                                        Door door = (Door) block.getState().getData();
                                        door_bottom = (door.isTopHalf()) ? block.getRelative(BlockFace.DOWN) : block;
                                        boolean opened = isDoorOpen(door_bottom.getData(), dd);
                                        if (opened && preset.hasDoor()) {
                                            exitLoc = TARDISLocationGetters.getLocationFromDB(rse.getDoor_location(), 0.0f, 0.0f);
                                        } else {
                                            exitLoc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ(), yaw, pitch);
                                        }
                                        if (hb) {
                                            // change the yaw if the door directions are different
                                            if (!dd.equals(d)) {
                                                yaw += adjustYaw(dd, d);
                                            }
                                            exitLoc.setYaw(yaw);
                                            // get location from database
                                            final Location exitTardis = exitLoc;
                                            // make location safe ie. outside of the bluebox
                                            double ex = exitTardis.getX();
                                            double ez = exitTardis.getZ();
                                            if (opened) {
                                                exitTardis.setX(ex + 0.5);
                                                exitTardis.setZ(ez + 0.5);
                                            } else {
                                                switch (d) {
                                                    case NORTH:
                                                        exitTardis.setX(ex + 0.5);
                                                        exitTardis.setZ(ez + 2.5);
                                                        break;
                                                    case EAST:
                                                        exitTardis.setX(ex - 1.5);
                                                        exitTardis.setZ(ez + 0.5);
                                                        break;
                                                    case SOUTH:
                                                        exitTardis.setX(ex + 0.5);
                                                        exitTardis.setZ(ez - 1.5);
                                                        break;
                                                    case WEST:
                                                        exitTardis.setX(ex + 2.5);
                                                        exitTardis.setZ(ez + 0.5);
                                                        break;
                                                }
                                            }
                                            // exit TARDIS!
                                            movePlayer(player, exitTardis, true, playerWorld, userQuotes, 2, minecart);
                                            if (plugin.getConfig().getBoolean("allow.mob_farming") && player.hasPermission("tardis.farm")) {
                                                TARDISFarmer tf = new TARDISFarmer(plugin);
                                                final List<TARDISMob> pets = tf.exitPets(player);
                                                if (pets != null && pets.size() > 0) {
                                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            movePets(pets, exitTardis, player, d, false);
                                                        }
                                                    }, 10L);
                                                }
                                            }
                                            if (plugin.getConfig().getBoolean("allow.tp_switch") && userTP) {
                                                new TARDISResourcePackChanger(plugin).changeRP(player, rsp.getTextureOut());
                                            }
                                            // remove player from traveller table
                                            removeTraveller(playerUUID);
                                        } else {
                                            TARDISMessage.send(player, "LOST_IN_VORTEX");
                                        }
                                        break;
                                    case 0:
                                        // is the TARDIS materialising?
                                        if (plugin.getTrackerKeeper().getInVortex().contains(id)) {
                                            TARDISMessage.send(player, "LOST_IN_VORTEX");
                                            return;
                                        }
                                        boolean chkCompanion = false;
                                        if (!playerUUID.equals(tlUUID)) {
                                            if (companions != null && !companions.isEmpty()) {
                                                // is the player in the comapnion list
                                                String[] companionData = companions.split(":");
                                                for (String c : companionData) {
                                                    if (c.equalsIgnoreCase(player.getUniqueId().toString())) {
                                                        chkCompanion = true;
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        if (playerUUID.equals(tlUUID) || chkCompanion == true || player.hasPermission("tardis.skeletonkey")) {
                                            // get INNER TARDIS location
                                            TARDISDoorLocation idl = getDoor(1, id);
                                            Location tmp_loc = idl.getL();
                                            World cw = idl.getW();
                                            COMPASS innerD = idl.getD();
                                            // check for entities near the police box
                                            List<TARDISMob> pets = null;
                                            if (plugin.getConfig().getBoolean("allow.mob_farming") && player.hasPermission("tardis.farm") && !plugin.getTrackerKeeper().getFarming().contains(player.getUniqueId()) && willFarm) {
                                                plugin.getTrackerKeeper().getFarming().add(player.getUniqueId());
                                                TARDISFarmer tf = new TARDISFarmer(plugin);
                                                pets = tf.farmAnimals(block_loc, d, id, player.getPlayer(), tmp_loc.getWorld().getName(), playerWorld.getName());
                                            }
                                            // if WorldGuard is on the server check for TARDIS region protection and add admin as member
                                            if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard") && player.hasPermission("tardis.skeletonkey")) {
                                                plugin.getWorldGuardUtils().addMemberToRegion(cw, rs.getOwner(), player.getName());
                                            }
                                            // enter TARDIS!
                                            cw.getChunkAt(tmp_loc).load();
                                            tmp_loc.setPitch(pitch);
                                            // get inner door direction so we can adjust yaw if necessary
                                            if (!innerD.equals(pd)) {
                                                yaw += adjustYaw(pd, innerD);
                                            }
                                            tmp_loc.setYaw(yaw);
                                            final Location tardis_loc = tmp_loc;
                                            movePlayer(player, tardis_loc, false, playerWorld, userQuotes, 1, minecart);
                                            if (pets != null && pets.size() > 0) {
                                                movePets(pets, tardis_loc, player, d, true);
                                            }
                                            if (plugin.getConfig().getBoolean("allow.tp_switch") && userTP) {
                                                if (!rsp.getTextureIn().isEmpty()) {
                                                    new TARDISResourcePackChanger(plugin).changeRP(player, rsp.getTextureIn());
                                                }
                                            }
                                            // put player into travellers table
                                            // remove them first as they may have exited incorrectly and we only want them listed once
                                            removeTraveller(playerUUID);
                                            HashMap<String, Object> set = new HashMap<String, Object>();
                                            set.put("tardis_id", id);
                                            set.put("uuid", playerUUID.toString());
                                            qf.doSyncInsert("travellers", set);
                                        }
                                        break;
                                    case 2:
                                        if (artron < required) {
                                            TARDISMessage.send(player, "NOT_ENOUGH_DOOR_ENERGY");
                                            return;
                                        }
                                        // always enter by the back door
                                        TARDISDoorLocation ibdl = getDoor(3, id);
                                        Location ibd_loc = ibdl.getL();
                                        if (ibd_loc == null) {
                                            TARDISMessage.send(player, "DOOR_BACK_IN");
                                            return;
                                        }
                                        COMPASS ibdd = ibdl.getD();
                                        COMPASS ipd = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, true));
                                        if (!ibdd.equals(ipd)) {
                                            yaw += adjustYaw(ipd, ibdd) + 180F;
                                        }
                                        ibd_loc.setYaw(yaw);
                                        ibd_loc.setPitch(pitch);
                                        final Location inner_loc = ibd_loc;
                                        movePlayer(player, inner_loc, false, playerWorld, userQuotes, 1, minecart);
                                        if (plugin.getConfig().getBoolean("allow.tp_switch") && userTP) {
                                            if (!rsp.getTextureIn().isEmpty()) {
                                                new TARDISResourcePackChanger(plugin).changeRP(player, rsp.getTextureIn());
                                            }
                                        }
                                        // put player into travellers table
                                        removeTraveller(playerUUID);
                                        HashMap<String, Object> set = new HashMap<String, Object>();
                                        set.put("tardis_id", id);
                                        set.put("uuid", playerUUID.toString());
                                        qf.doSyncInsert("travellers", set);
                                        HashMap<String, Object> wheree = new HashMap<String, Object>();
                                        wheree.put("tardis_id", id);
                                        int cost = (0 - plugin.getArtronConfig().getInt("backdoor"));
                                        qf.alterEnergyLevel("tardis", cost, wheree, player);
                                        break;
                                    case 3:
                                        if (artron < required) {
                                            TARDISMessage.send(player, "NOT_ENOUGH_DOOR_ENERGY");
                                            return;
                                        }
                                        // always exit to outer back door
                                        TARDISDoorLocation obdl = getDoor(2, id);
                                        Location obd_loc = obdl.getL();
                                        if (obd_loc == null) {
                                            TARDISMessage.send(player, "DOOR_BACK_OUT");
                                            return;
                                        }
                                        if (obd_loc.getWorld().getEnvironment().equals(Environment.THE_END) && (!player.hasPermission("tardis.end") || !plugin.getConfig().getBoolean("travel.the_end"))) {
                                            TARDISMessage.send(player, "NO_PERM_TRAVEL", "End");
                                            return;
                                        }
                                        if (obd_loc.getWorld().getEnvironment().equals(Environment.NETHER) && (!player.hasPermission("tardis.nether") || !plugin.getConfig().getBoolean("travel.nether"))) {
                                            TARDISMessage.send(player, "NO_PERM_TRAVEL", "Nether");
                                            return;
                                        }
                                        COMPASS obdd = obdl.getD();
                                        COMPASS opd = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
                                        if (!obdd.equals(opd)) {
                                            yaw += adjustYaw(opd, obdd);
                                        }
                                        obd_loc.setYaw(yaw);
                                        obd_loc.setPitch(pitch);
                                        final Location outer_loc = obd_loc;
                                        movePlayer(player, outer_loc, true, playerWorld, userQuotes, 2, minecart);
                                        if (plugin.getConfig().getBoolean("allow.tp_switch") && userTP) {
                                            new TARDISResourcePackChanger(plugin).changeRP(player, rsp.getTextureOut());
                                        }
                                        // remove player from traveller table
                                        removeTraveller(playerUUID);
                                        // take energy
                                        HashMap<String, Object> wherea = new HashMap<String, Object>();
                                        wherea.put("tardis_id", id);
                                        int costa = (0 - plugin.getArtronConfig().getInt("backdoor"));
                                        qf.alterEnergyLevel("tardis", costa, wherea, player);
                                        break;
                                    default:
                                        // do nothing
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
