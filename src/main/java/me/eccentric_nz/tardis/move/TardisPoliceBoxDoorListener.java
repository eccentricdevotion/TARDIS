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
package me.eccentric_nz.tardis.move;

import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.control.TardisPowerButton;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetCompanions;
import me.eccentric_nz.tardis.database.resultset.ResultSetDoors;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.mobfarming.TardisFarmer;
import me.eccentric_nz.tardis.mobfarming.TardisFollowerSpawner;
import me.eccentric_nz.tardis.mobfarming.TardisPetsAndFollowers;
import me.eccentric_nz.tardis.travel.TardisDoorLocation;
import me.eccentric_nz.tardis.utility.TardisResourcePackChanger;
import me.eccentric_nz.tardis.utility.TardisSounds;
import me.eccentric_nz.tardis.utility.TardisStaticUtils;
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
import java.util.Objects;
import java.util.UUID;

public class TardisPoliceBoxDoorListener extends TardisDoorListener implements Listener {

    public TardisPoliceBoxDoorListener(TardisPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemFrameClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof ItemFrame frame) {
            UUID uuid = player.getUniqueId();
            ItemStack dye = frame.getItem();
            if (TardisConstants.DYES.contains(dye.getType()) && dye.hasItemMeta()) {
                ItemMeta dim = dye.getItemMeta();
                assert dim != null;
                if (dim.hasCustomModelData()) {
                    int cmd = dim.getCustomModelData();
                    if ((cmd == 1001 || cmd == 1002) && TardisPermission.hasPermission(player, "tardis.enter")) {
                        UUID playerUuid = player.getUniqueId();
                        // get TARDIS from location
                        Location location = frame.getLocation();
                        String doorloc = Objects.requireNonNull(location.getWorld()).getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("door_location", doorloc);
                        where.put("door_type", 0);
                        ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
                        if (rsd.resultSet()) {
                            event.setCancelled(true);
                            int id = rsd.getTardisId();
                            boolean open = cmd < 1002;
                            if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                TardisMessage.send(player, "SIEGE_NO_EXIT");
                                return;
                            }
                            if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                                TardisMessage.send(player, "NOT_WHILE_MAT");
                                return;
                            }
                            // handbrake must be on
                            HashMap<String, Object> tid = new HashMap<>();
                            tid.put("tardis_id", id);
                            ResultSetTardis rs = new ResultSetTardis(plugin, tid, "", false, 2);
                            if (rs.resultSet()) {
                                Tardis tardis = rs.getTardis();
                                if (!tardis.isHandbrakeOn()) {
                                    TardisMessage.send(player, "HANDBRAKE_ENGAGE");
                                    return;
                                }
                                // must be Time Lord or companion
                                ResultSetCompanions rsc = new ResultSetCompanions(plugin, id);
                                if (rsc.getCompanions().contains(playerUuid) || tardis.isAbandoned()) {
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
                                                if (rspref.isAutoPowerUpOn() && plugin.getConfig().getBoolean("allow.power_down")) {
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
                                                    TardisDoorLocation idl = getDoor(1, id);
                                                    Location tardis_loc = idl.getL();
                                                    World cw = idl.getW();
                                                    CardinalDirection innerD = idl.getD();
                                                    CardinalDirection d = rsd.getDoorDirection();
                                                    CardinalDirection pd = CardinalDirection.valueOf(TardisStaticUtils.getPlayersDirection(player, false));
                                                    World playerWorld = location.getWorld();
                                                    // check for entities near the police box
                                                    TardisPetsAndFollowers petsAndFollowers = null;
                                                    if (plugin.getConfig().getBoolean("allow.mob_farming") && TardisPermission.hasPermission(player, "tardis.farm") && !plugin.getTrackerKeeper().getFarming().contains(playerUuid) && willFarm) {
                                                        plugin.getTrackerKeeper().getFarming().add(playerUuid);
                                                        TardisFarmer tf = new TardisFarmer(plugin);
                                                        petsAndFollowers = tf.farmAnimals(location, d, id, player.getPlayer(), Objects.requireNonNull(tardis_loc.getWorld()).getName(), playerWorld.getName());
                                                    }
                                                    // if WorldGuard is on the server check for TARDIS region protection and add admin as member
                                                    if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard") && TardisPermission.hasPermission(player, "tardis.skeletonkey")) {
                                                        plugin.getWorldGuardUtils().addMemberToRegion(cw, tardis.getOwner(), player.getName());
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
                                                            new TardisFollowerSpawner(plugin).spawn(petsAndFollowers.getFollowers(), tardis_loc, player, d, true);
                                                        }
                                                    }
                                                    if (plugin.getConfig().getBoolean("allow.tp_switch") && rspref.isTextureOn()) {
                                                        if (!rspref.getTextureIn().isEmpty()) {
                                                            new TardisResourcePackChanger(plugin).changeResourcePack(player, rspref.getTextureIn());
                                                        }
                                                    }
                                                    if (canPowerUp && !tardis.isPowered() && !tardis.isAbandoned()) {
                                                        // power up the TARDIS
                                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TardisPowerButton(plugin, id, player, tardis.getPreset(), false, tardis.isHidden(), tardis.isLightsOn(), player.getLocation(), tardis.getArtronLevel(), tardis.getSchematic().hasLanterns()).clickButton(), 20L);
                                                    }
                                                    // put player into travellers table
                                                    // remove them first as they may have exited incorrectly and we only want them listed once
                                                    removeTraveller(playerUuid);
                                                    HashMap<String, Object> set = new HashMap<>();
                                                    set.put("tardis_id", id);
                                                    set.put("uuid", playerUuid.toString());
                                                    plugin.getQueryFactory().doSyncInsert("travellers", set);
                                                } else {
                                                    // create portal & open inner door
                                                    new TardisInnerDoorOpener(plugin, uuid, id).openDoor();
                                                    dim.setCustomModelData(1002);
                                                    dye.setItemMeta(dim);
                                                    frame.setItem(dye, false);
                                                }
                                                playDoorSound(true, location);
                                            }
                                        } else {
                                            if (tardis.isAbandoned()) {
                                                TardisMessage.send(player, "ABANDONED_DOOR");
                                                return;
                                            }
                                            // close portal & inner door
                                            new TardisInnerDoorCloser(plugin, uuid, id).closeDoor();
                                            dim.setCustomModelData(1001);
                                            dye.setItemMeta(dim);
                                            frame.setItem(dye, false);
                                            playDoorSound(false, location);
                                        }
                                    } else if (tardis.getUuid() != playerUuid) {
                                        TardisMessage.send(player, "DOOR_DEADLOCKED");
                                    } else {
                                        TardisMessage.send(player, "DOOR_UNLOCK");
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
            TardisSounds.playTardisSound(l, "tardis_door_open");
        } else {
            TardisSounds.playTardisSound(l, "tardis_door_close");
        }
    }
}
