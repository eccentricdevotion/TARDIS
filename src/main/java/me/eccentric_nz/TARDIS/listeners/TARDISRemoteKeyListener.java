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
package me.eccentric_nz.TARDIS.listeners;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.artron.TARDISPoliceBoxLampToggler;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISHideCommand;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISRebuildCommand;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.move.TARDISDoorToggler;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * At one point, the Tenth Doctor installed a system that allowed him to lock
 * the TARDIS remotely using a fob. As a joke, the TARDIS roof light flashed and
 * a alarm chirp sound was heard, similar to that used on vehicles on Earth.
 *
 * @author eccentric_nz
 */
public class TARDISRemoteKeyListener implements Listener {

    private final TARDIS plugin;
    private final Material rkey;

    public TARDISRemoteKeyListener(TARDIS plugin) {
        this.plugin = plugin;
        this.rkey = Material.valueOf(this.plugin.getRecipesConfig().getString("shaped.TARDIS Remote Key.result"));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (!action.equals(Action.LEFT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        final Player player = event.getPlayer();
        // check item in hand
        ItemStack is = player.getItemInHand();
        if (is == null || !is.getType().equals(rkey)) {
            return;
        }
        if (is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("TARDIS Remote Key")) {
            String uuid = player.getUniqueId().toString();
            // has TARDIS?
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", uuid);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                return;
            }
            final int id = rs.getTardis_id();
            final PRESET preset = rs.getPreset();
            if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                return;
            }
            boolean hidden = rs.isHidden();
            if (action.equals(Action.LEFT_CLICK_AIR)) {
                final boolean powered = rs.isPowered_on();
                // get the TARDIS current location
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("tardis_id", id);
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
                if (!rsc.resultSet()) {
                    return;
                }
                Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                QueryFactory qf = new QueryFactory(plugin);
                HashMap<String, Object> whered = new HashMap<String, Object>();
                whered.put("tardis_id", id);
                ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
                if (rsd.resultSet()) {
                    // toggle door lock
                    int locked = (rsd.isLocked()) ? 0 : 1;
                    HashMap<String, Object> setl = new HashMap<String, Object>();
                    setl.put("locked", locked);
                    HashMap<String, Object> wherel = new HashMap<String, Object>();
                    wherel.put("tardis_id", id);
                    // always lock / unlock both doors
                    qf.doUpdate("doors", setl, wherel);
                    String message = (rsd.isLocked()) ? plugin.getLanguage().getString("DOOR_UNLOCK") : plugin.getLanguage().getString("DOOR_DEADLOCK");
                    TARDISMessage.send(player, "DOOR_LOCK", message);
                    final TARDISPoliceBoxLampToggler tpblt = new TARDISPoliceBoxLampToggler(plugin);
                    TARDISSounds.playTARDISSoundNearby(l, "tardis_lock");
                    tpblt.toggleLamp(id, !powered);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            tpblt.toggleLamp(id, powered);
                        }
                    }, 6L);
                }
            } else {
                if (preset.equals(PRESET.INVISIBLE)) {
                    HashMap<String, Object> whered = new HashMap<String, Object>();
                    whered.put("tardis_id", id);
                    whered.put("door_type", 1);
                    ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
                    if (rsd.resultSet()) {
                        // get inner door block
                        Block block = TARDISLocationGetters.getLocationFromDB(rsd.getDoor_location(), 0.0f, 0.0f).getBlock();
                        COMPASS dd = rsd.getDoor_direction();
                        boolean open = TARDISStaticUtils.isOpen(block, dd);
                        // toggle door / portals
                        new TARDISDoorToggler(plugin, block, player, false, open, id).toggleDoors();
                        String message = (open) ? "DOOR_CLOSED" : "DOOR_OPENED";
                        TARDISMessage.send(player, message);
                    }
                } else {
                    // toggle hidden
                    if (hidden) {
                        // rebuild
                        TARDISSounds.playTARDISSound(player.getLocation(), player, "tardis_rebuild");
                        new TARDISRebuildCommand(plugin).rebuildPreset(player);
                    } else {
                        // hide
                        TARDISSounds.playTARDISSound(player.getLocation(), player, "tardis_hide");
                        new TARDISHideCommand(plugin).hide(player);
                    }
                }
            }
        }
    }
}
