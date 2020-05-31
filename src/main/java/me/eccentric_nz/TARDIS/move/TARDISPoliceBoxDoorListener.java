package me.eccentric_nz.TARDIS.move;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.ResultSetCompanions;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.Material;
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

public class TARDISPoliceBoxDoorListener implements Listener {

    private final TARDIS plugin;

    public TARDISPoliceBoxDoorListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemFrameClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof ItemFrame) {
            ItemFrame frame = (ItemFrame) event.getRightClicked();
            UUID uuid = player.getUniqueId();
            ItemStack dye = frame.getItem();
            if (dye != null && TARDISConstants.DYES.contains(dye.getType()) && dye.hasItemMeta()) {
                ItemMeta dim = dye.getItemMeta();
                if (dim.hasCustomModelData()) {
                    int cmd = dim.getCustomModelData();
                    if ((cmd == 1001 || cmd == 1002) && player.hasPermission("tardis.enter")) {
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
                                if (!rs.getTardis().isHandbrake_on()) {
                                    TARDISMessage.send(player, "HANDBRAKE_ENGAGE");
                                    return;
                                }
                                // must be Time Lord or companion
                                ResultSetCompanions rsc = new ResultSetCompanions(plugin, id);
                                if (rsc.getCompanions().contains(playerUUID) || rs.getTardis().isAbandoned()) {
                                    if (!rsd.isLocked()) {
                                        if (open) {
                                            // get key material
                                            ResultSetPlayerPrefs rspref = new ResultSetPlayerPrefs(plugin, uuid.toString());
                                            String key;
                                            if (rspref.resultSet()) {
                                                key = (!rspref.getKey().isEmpty()) ? rspref.getKey() : plugin.getConfig().getString("preferences.key");
                                            } else {
                                                key = plugin.getConfig().getString("preferences.key");
                                            }
                                            Material m = Material.valueOf(key);
                                            if (player.getInventory().getItemInMainHand().getType().equals(m)) {
                                                // create portal & open inner door
                                                new TARDISInnerDoorOpener(plugin, uuid, id).openDoor();
                                                dim.setCustomModelData(1002);
                                                dye.setItemMeta(dim);
                                                frame.setItem(dye);
                                                playDoorSound(true, location);
                                            }
                                        } else {
                                            if (rs.getTardis().isAbandoned()) {
                                                TARDISMessage.send(player, "ABANDONED_DOOR");
                                                return;
                                            }
                                            // close portal & inner door
                                            new TARDISInnerDoorCloser(plugin, uuid, id).closeDoor();
                                            dim.setCustomModelData(1001);
                                            dye.setItemMeta(dim);
                                            frame.setItem(dye);
                                            playDoorSound(false, location);
                                        }
                                    } else if (rs.getTardis().getUuid() != playerUUID) {
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
