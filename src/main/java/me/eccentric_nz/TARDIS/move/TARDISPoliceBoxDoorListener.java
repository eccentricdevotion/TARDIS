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
package me.eccentric_nz.TARDIS.move;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.chameleon.utils.PandoricaOpens;
import me.eccentric_nz.TARDIS.control.TARDISPowerButton;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCompanions;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFarmer;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFollowerSpawner;
import me.eccentric_nz.TARDIS.mobfarming.TARDISPetsAndFollowers;
import me.eccentric_nz.TARDIS.sonic.actions.TARDISSonicSound;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TARDISPoliceBoxDoorListener extends TARDISDoorListener implements Listener {

    public TARDISPoliceBoxDoorListener(TARDIS plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onArmourStandClick(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof ArmorStand stand) {
            UUID uuid = player.getUniqueId();
            EntityEquipment ee = stand.getEquipment();
            ItemStack dye = ee.getHelmet();
            if (dye != null && (TARDISConstants.DYES.contains(dye.getType()) || plugin.getUtils().isCustomModel(dye)) && dye.hasItemMeta()) {
                event.setCancelled(true);
                ItemMeta dim = dye.getItemMeta();
                if (dim.hasCustomModelData()) {
                    int cmd = dim.getCustomModelData();
                    if ((cmd == 1001 || cmd == 1002) && TARDISPermission.hasPermission(player, "tardis.enter")) {
                        UUID playerUUID = player.getUniqueId();
                        // get TARDIS from location
                        Location location = stand.getLocation();
                        String doorloc = location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("door_location", doorloc);
                        where.put("door_type", 0);
                        ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
                        if (rsd.resultSet()) {
                            event.setCancelled(true);
                            int id = rsd.getTardis_id();
                            boolean closed = cmd < 1002;
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
                                Tardis tardis = rs.getTardis();
                                if (!tardis.isHandbrake_on()) {
                                    plugin.getMessenger().sendStatus(player, "HANDBRAKE_ENGAGE");
                                    return;
                                }
                                // must be Time Lord or companion
                                ResultSetCompanions rsc = new ResultSetCompanions(plugin, id);
                                if (rsc.getCompanions().contains(playerUUID) || tardis.isAbandoned()) {
                                    if (!rsd.isLocked()) {
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
                                                        if (!petsAndFollowers.getPets().isEmpty()) {
                                                            movePets(petsAndFollowers.getPets(), tardis_loc, player, d, true);
                                                        }
                                                        if (!petsAndFollowers.getFollowers().isEmpty()) {
                                                            new TARDISFollowerSpawner(plugin).spawn(petsAndFollowers.getFollowers(), tardis_loc, player, d, true);
                                                        }
                                                    }
                                                    if (canPowerUp && !tardis.isPowered_on() && !tardis.isAbandoned()) {
                                                        // power up the TARDIS
                                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISPowerButton(plugin, id, player, tardis.getPreset(), false, tardis.isHidden(), tardis.isLights_on(), player.getLocation(), tardis.getArtron_level(), tardis.getSchematic().getLights()).clickButton(), 20L);
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
                                                    if (dye.getType() == Material.ENDER_PEARL) {
                                                        // animate pandorica opening
                                                        new PandoricaOpens(plugin).animate(stand, true);
                                                    } else {
                                                        dim.setCustomModelData(1002);
                                                        dye.setItemMeta(dim);
                                                        ee.setHelmet(dye, true);
                                                    }
                                                }
                                                playDoorSound(true, location);
                                            } else if (TARDISStaticUtils.isSonic(hand) && TARDISMaterials.dyes.contains(dye.getType()) && tardis.getUuid().equals(playerUUID)) {
                                                ItemMeta im = hand.getItemMeta();
                                                List<String> lore = im.getLore();
                                                if (TARDISPermission.hasPermission(player, "tardis.sonic.paint") && lore != null && lore.contains("Painter Upgrade")) {
                                                    // check for dye in slot
                                                    PlayerInventory inv = player.getInventory();
                                                    ItemStack colour = inv.getItem(8);
                                                    if (colour == null || !TARDISMaterials.dyes.contains(colour.getType())) {
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
                                                    dye.setType(colour.getType());
                                                    ee.setHelmet(dye, true);
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
                                                    wheret.put("tardis_id", tardis.getTardis_id());
                                                    plugin.getQueryFactory().doUpdate("tardis", set, wheret);
                                                }
                                            }
                                        } else {
                                            if (tardis.isAbandoned()) {
                                                plugin.getMessenger().send(player, TardisModule.TARDIS, "ABANDONED_DOOR");
                                                return;
                                            }
                                            // close portal & inner door
                                            new TARDISInnerDoorCloser(plugin, uuid, id).closeDoor();
                                            if (dye.getType() == Material.ENDER_PEARL) {
                                                new PandoricaOpens(plugin).animate(stand, false);
                                            } else {
                                                dim.setCustomModelData(1001);
                                                dye.setItemMeta(dim);
                                                ee.setHelmet(dye, true);
                                                playDoorSound(false, location);
                                            }
                                        }
                                    } else if (!tardis.getUuid().equals(playerUUID)) {
                                        plugin.getMessenger().sendStatus(player, "DOOR_DEADLOCKED");
                                    } else {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "DOOR_UNLOCK");
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
     * @param l a location to play the sound at
     */
    private void playDoorSound(boolean open, Location l) {
        if (open) {
            TARDISSounds.playTARDISSound(l, "tardis_door_open");
        } else {
            TARDISSounds.playTARDISSound(l, "tardis_door_close");
        }
    }
}
