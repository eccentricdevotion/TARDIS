/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.doors.outer;

import com.destroystokyo.paper.MaterialTags;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.control.TARDISPowerButton;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCompanions;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.doors.inner.*;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFarmer;
import me.eccentric_nz.TARDIS.mobfarming.FollowerSpawner;
import me.eccentric_nz.TARDIS.mobfarming.PetsAndFollowers;
import me.eccentric_nz.TARDIS.move.TARDISDoorListener;
import me.eccentric_nz.TARDIS.sonic.actions.TARDISSonicSound;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class OuterDisplayDoorAction extends TARDISDoorListener {

    public OuterDisplayDoorAction(TARDIS plugin) {
        super(plugin);
    }

    public void processClick(int id, Player player, ArmorStand stand) {
        EntityEquipment ee = stand.getEquipment();
        ItemStack dye = ee.getHelmet();
        if (dye != null && (TARDISConstants.DYES.contains(dye.getType()) || plugin.getUtils().isCustomModel(dye)) && dye.hasItemMeta()) {
            ItemMeta dim = dye.getItemMeta();
            if (dim.hasItemModel()) {
                String model = dim.getItemModel().getKey();
                if ((model.contains("_open") || model.contains("_closed")) && TARDISPermission.hasPermission(player, "tardis.enter")) {
                    UUID uuid = player.getUniqueId();
                    // get TARDIS from location
                    Location location = stand.getLocation();
                    String doorloc = TARDISStaticLocationGetters.makeLocationStr(location);
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("door_location", doorloc);
                    where.put("door_type", 0);
                    ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
                    if (rsd.resultSet()) {
                        boolean closed = model.contains("_closed");
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
                        ResultSetTardis rs = new ResultSetTardis(plugin, tid, "", false);
                        if (rs.resultSet()) {
                            Tardis tardis = rs.getTardis();
                            if (!tardis.isHandbrakeOn()) {
                                plugin.getMessenger().sendStatus(player, "HANDBRAKE_ENGAGE");
                                return;
                            }
                            // must be Time Lord or companion
                            ResultSetCompanions rsc = new ResultSetCompanions(plugin, id);
                            if (rsc.getCompanions().contains(uuid) || tardis.isAbandoned()) {
                                if (!rsd.isLocked()) {
                                    Inner innerDisplayDoor = new InnerDoor(plugin, id).get();
                                    if (closed) {
                                        // get key material
                                        ResultSetPlayerPrefs rspref = new ResultSetPlayerPrefs(plugin, uuid.toString());
                                        String key;
                                        boolean willFarm = false;
                                        boolean canPowerUp = false;
                                        if (rspref.resultSet()) {
                                            key = (!rspref.getKey().isEmpty()) ? rspref.getKey() : plugin.getConfig().getString("preferences.key");
                                            willFarm = rspref.isFarmOn();
                                            if (rspref.isAutoPowerUp() && plugin.getConfig().getBoolean("allow.power_down")) {
                                                // check TARDIS is not abandoned
                                                canPowerUp = !tardis.isAbandoned();
                                            }
                                        } else {
                                            key = plugin.getConfig().getString("preferences.key");
                                        }
                                        Material m = Material.valueOf(key);
                                        ItemStack hand = player.getInventory().getItemInMainHand();
                                        if (hand.getType().equals(m) || plugin.getConfig().getBoolean("preferences.any_key")) {
                                            if (player.isSneaking()) {
                                                // tp to the interior
                                                // get INNER TARDIS location
                                                TARDISDoorLocation idl = getDoor(1, id);
                                                Location tardis_loc = idl.getL();
                                                World cw = idl.getW();
                                                COMPASS innerD = idl.getD();
                                                COMPASS d = rsd.getDoor_direction();
                                                COMPASS pd = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
                                                World playerWorld = location.getWorld();
                                                // check for entities near the police box
                                                PetsAndFollowers petsAndFollowers = null;
                                                if (plugin.getConfig().getBoolean("allow.mob_farming") && TARDISPermission.hasPermission(player, "tardis.farm") && !plugin.getTrackerKeeper().getFarming().contains(uuid) && willFarm) {
                                                    plugin.getTrackerKeeper().getFarming().add(uuid);
                                                    TARDISFarmer tf = new TARDISFarmer(plugin);
                                                    petsAndFollowers = tf.farmAnimals(location, d, id, player.getPlayer(), tardis_loc.getWorld().getName(), playerWorld.getName());
                                                }
                                                // if WorldGuard is on the server check for TARDIS region protection and add admin as member
                                                if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard") && TARDISPermission.hasPermission(player, "tardis.skeletonkey")) {
                                                    plugin.getWorldGuardUtils().addMemberToRegion(cw, tardis.getOwner(), player.getUniqueId());
                                                }
                                                // enter TARDIS!
                                                cw.getChunkAt(tardis_loc).load();
                                                tardis_loc.setPitch(player.getLocation().getPitch());
                                                // get inner door direction, so we can adjust yaw if necessary
                                                float yaw = player.getLocation().getYaw();
                                                if (!innerD.equals(pd)) {
                                                    yaw += adjustYaw(pd, innerD);
                                                }
                                                tardis_loc.setYaw(yaw);
                                                movePlayer(player, tardis_loc, false, playerWorld, rspref.isQuotesOn(), 1, rspref.isMinecartOn(), false);
                                                if (petsAndFollowers != null) {
                                                    if (!petsAndFollowers.getPets().isEmpty()) {
                                                        movePets(petsAndFollowers.getPets(), tardis_loc, player, d, true);
                                                    }
                                                    if (!petsAndFollowers.getFollowers().isEmpty()) {
                                                        new FollowerSpawner(plugin).spawn(petsAndFollowers.getFollowers(), tardis_loc, player, d, true);
                                                    }
                                                }
                                                if (canPowerUp && !tardis.isPoweredOn() && !tardis.isAbandoned()) {
                                                    // power up the TARDIS
                                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISPowerButton(plugin, id, player, tardis.getPreset(), false, tardis.isHidden(), tardis.isLightsOn(), player.getLocation(), tardis.getArtronLevel(), tardis.getSchematic().getLights()).clickButton(), 20L);
                                                }
                                                // put player into travellers table
                                                // remove them first as they may have exited incorrectly, and we only want them listed once
                                                removeTraveller(uuid);
                                                HashMap<String, Object> set = new HashMap<>();
                                                set.put("tardis_id", id);
                                                set.put("uuid", uuid.toString());
                                                plugin.getQueryFactory().doSyncInsert("travellers", set);
                                            } else {
                                                // open outer
                                                new OuterDisplayDoorOpener(plugin).open(stand, id);
                                                // open inner
                                                if (innerDisplayDoor.display()) {
                                                    new InnerDisplayDoorOpener(plugin).open(innerDisplayDoor.block(), id, true);
                                                } else {
                                                    new InnerMinecraftDoorOpener(plugin).open(innerDisplayDoor.block(), id);
                                                }
                                            }
                                            TARDISSounds.playDoorSound(true, location);
                                        } else if (TARDISStaticUtils.isSonic(hand) && MaterialTags.DYES.isTagged(dye.getType()) && tardis.getUuid().equals(uuid)) {
                                            ItemMeta im = hand.getItemMeta();
                                            List<Component> lore = im.lore();
                                            if (TARDISPermission.hasPermission(player, "tardis.sonic.paint") && lore != null && lore.contains(Component.text("Painter Upgrade"))) {
                                                // check for dye in slot
                                                PlayerInventory inv = player.getInventory();
                                                ItemStack colour = inv.getItem(8);
                                                if (colour == null || !MaterialTags.DYES.isTagged(colour.getType())) {
                                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SONIC_DYE");
                                                    return;
                                                }
                                                // dye = item frame item
                                                if (dye.getType() == colour.getType()) {
                                                    // same colour - do nothing
                                                    return;
                                                }
                                                long now = System.currentTimeMillis();
                                                TARDISSonicSound.playSonicSound(plugin, player, now, 600L, "sonic_short");
                                                ItemStack sub = ItemStack.of(colour.getType());
                                                sub.setItemMeta(colour.getItemMeta());
                                                ee.setHelmet(sub, true);
                                                // remove one dye
                                                int a = colour.getAmount();
                                                int a2 = a - 1;
                                                if (a2 > 0) {
                                                    inv.getItem(8).setAmount(a2);
                                                } else {
                                                    inv.setItem(8, null);
                                                }
                                                player.updateInventory();
                                                // update database
                                                HashMap<String, Object> set = new HashMap<>();
                                                String c = "POLICE_BOX_" + colour.getType().toString().replace("_DYE", "");
                                                set.put("chameleon_preset", c);
                                                set.put("chameleon_demat", c);
                                                HashMap<String, Object> wheret = new HashMap<>();
                                                wheret.put("tardis_id", tardis.getTardisId());
                                                plugin.getQueryFactory().doUpdate("tardis", set, wheret);
                                            }
                                        }
                                    } else {
                                        if (tardis.isAbandoned()) {
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ABANDONED_DOOR");
                                            return;
                                        }
                                        // close outer
                                        new OuterDisplayDoorCloser(plugin).close(stand, id, uuid);
                                        // close inner
                                        if (innerDisplayDoor.display()) {
                                            new InnerDisplayDoorCloser(plugin).close(innerDisplayDoor.block(), id, uuid, true);
                                        } else {
                                            new InnerMinecraftDoorCloser(plugin).close(innerDisplayDoor.block(), id, uuid);
                                        }
                                    }
                                } else if (!tardis.getUuid().equals(uuid)) {
                                    plugin.getMessenger().sendStatus(player, "DOOR_DEADLOCKED");
                                } else {
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "DOOR_NEED_UNLOCK");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
