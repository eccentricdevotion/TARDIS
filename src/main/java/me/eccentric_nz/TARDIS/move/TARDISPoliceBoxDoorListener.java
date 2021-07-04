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
package me.eccentric_nz.TARDIS.move;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.control.TARDISPowerButton;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCompanions;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFarmer;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFollowerSpawner;
import me.eccentric_nz.TARDIS.mobfarming.TARDISPetsAndFollowers;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.TARDISResourcePackChanger;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class TARDISPoliceBoxDoorListener extends TARDISDoorListener implements Listener {

    public TARDISPoliceBoxDoorListener(TARDIS plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemFrameClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof ItemFrame frame) {
            UUID uuid = player.getUniqueId();
            ItemStack dye = frame.getItem();
            if (dye != null && TARDISConstants.DYES.contains(dye.getType()) && dye.hasItemMeta()) {
                ItemMeta dim = dye.getItemMeta();
                if (dim.hasCustomModelData()) {
                    int cmd = dim.getCustomModelData();
                    if ((cmd == 1001 || cmd == 1002) && TARDISPermission.hasPermission(player, "tardis.enter")) {
                        UUID playerUUID = player.getUniqueId();
                        // get TARDIS from location
                        Location location = frame.getLocation();
                        String doorloc = location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("door_location", doorloc);
                        where.put("door_type", 0);
                        ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
                        if (rsd.resultSet()) {
                            event.setCancelled(true);
                            int id = rsd.getTardis_id();
                            boolean open = cmd < 1002;
                            if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                TARDISMessage.send(player, "SIEGE_NO_EXIT");
                                return;
                            }
                            if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                                TARDISMessage.send(player, "NOT_WHILE_MAT");
                                return;
                            }
                            // handbrake must be on
                            HashMap<String, Object> tid = new HashMap<>();
                            tid.put("tardis_id", id);
                            ResultSetTardis rs = new ResultSetTardis(plugin, tid, "", false, 2);
                            if (rs.resultSet()) {
                                Tardis tardis = rs.getTardis();
                                if (!tardis.isHandbrake_on()) {
                                    TARDISMessage.send(player, "HANDBRAKE_ENGAGE");
                                    return;
                                }
                                // must be Time Lord or companion
                                ResultSetCompanions rsc = new ResultSetCompanions(plugin, id);
                                if (rsc.getCompanions().contains(playerUUID) || tardis.isAbandoned()) {
                                    if (!rsd.isLocked()) {
                                        if (open) {
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
                                            if (player.getInventory().getItemInMainHand().getType().equals(m)) {
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
                                                    TARDISPetsAndFollowers petsAndFollowers = null;
                                                    if (plugin.getConfig().getBoolean("allow.mob_farming") && TARDISPermission.hasPermission(player, "tardis.farm") && !plugin.getTrackerKeeper().getFarming().contains(playerUUID) && willFarm) {
                                                        plugin.getTrackerKeeper().getFarming().add(playerUUID);
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
                                                    // get inner door direction so we can adjust yaw if necessary
                                                    float yaw = player.getLocation().getYaw();
                                                    if (!innerD.equals(pd)) {
                                                        yaw += adjustYaw(pd, innerD);
                                                    }
                                                    tardis_loc.setYaw(yaw);
                                                    movePlayer(player, tardis_loc, false, playerWorld, rspref.isQuotesOn(), 1, rspref.isMinecartOn(), false);
                                                    if (petsAndFollowers != null) {
                                                        if (petsAndFollowers.getPets().size() > 0) {
                                                            movePets(petsAndFollowers.getPets(), tardis_loc, player, d, true);
                                                        }
                                                        if (petsAndFollowers.getFollowers().size() > 0) {
                                                            new TARDISFollowerSpawner(plugin).spawn(petsAndFollowers.getFollowers(), tardis_loc, player, d, true);
                                                        }
                                                    }
                                                    if (plugin.getConfig().getBoolean("allow.tp_switch") && rspref.isTextureOn()) {
                                                        if (!rspref.getTextureIn().isEmpty()) {
                                                            new TARDISResourcePackChanger(plugin).changeRP(player, rspref.getTextureIn());
                                                        }
                                                    }
                                                    if (canPowerUp && !tardis.isPowered_on() && !tardis.isAbandoned()) {
                                                        // power up the TARDIS
                                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISPowerButton(plugin, id, player, tardis.getPreset(), false, tardis.isHidden(), tardis.isLights_on(), player.getLocation(), tardis.getArtron_level(), tardis.getSchematic().hasLanterns()).clickButton(), 20L);
                                                    }
                                                    // put player into travellers table
                                                    // remove them first as they may have exited incorrectly and we only want them listed once
                                                    removeTraveller(playerUUID);
                                                    HashMap<String, Object> set = new HashMap<>();
                                                    set.put("tardis_id", id);
                                                    set.put("uuid", playerUUID.toString());
                                                    plugin.getQueryFactory().doSyncInsert("travellers", set);
                                                } else {
                                                    // create portal & open inner door
                                                    new TARDISInnerDoorOpener(plugin, uuid, id).openDoor();
                                                    dim.setCustomModelData(1002);
                                                    dye.setItemMeta(dim);
                                                    frame.setItem(dye, false);
                                                }
                                                playDoorSound(true, location);
                                            }
                                        } else {
                                            if (tardis.isAbandoned()) {
                                                TARDISMessage.send(player, "ABANDONED_DOOR");
                                                return;
                                            }
                                            // close portal & inner door
                                            new TARDISInnerDoorCloser(plugin, uuid, id).closeDoor();
                                            dim.setCustomModelData(1001);
                                            dye.setItemMeta(dim);
                                            frame.setItem(dye, false);
                                            playDoorSound(false, location);
                                        }
                                    } else if (tardis.getUuid() != playerUUID) {
                                        TARDISMessage.send(player, "DOOR_DEADLOCKED");
                                    } else {
                                        TARDISMessage.send(player, "DOOR_UNLOCK");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Plays a door sound when the blue police box oak trapdoor is clicked.
     *
     * @param open which sound to play, open (true), close (false)
     * @param l    a location to play the sound at
     */
    private void playDoorSound(boolean open, Location l) {
        if (open) {
            TARDISSounds.playTARDISSound(l, "tardis_door_open");
        } else {
            TARDISSounds.playTARDISSound(l, "tardis_door_close");
        }
    }
}
