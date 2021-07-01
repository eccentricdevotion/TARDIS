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
package me.eccentric_nz.tardis.listeners;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.artron.TardisAdaptiveBoxLampToggler;
import me.eccentric_nz.tardis.commands.tardis.TardisHideCommand;
import me.eccentric_nz.tardis.commands.tardis.TardisRebuildCommand;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetDoors;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.move.TardisDoorToggler;
import me.eccentric_nz.tardis.utility.TardisSounds;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import me.eccentric_nz.tardis.utility.TardisStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;

/**
 * At one point, the Tenth Doctor installed a system that allowed him to lock the TARDIS remotely using a fob. As a
 * joke, the TARDIS roof light flashed and a alarm chirp sound was heard, similar to that used on vehicles on Earth.
 *
 * @author eccentric_nz
 */
public class TardisRemoteKeyListener implements Listener {

    private final TardisPlugin plugin;
    private final Material rkey;

    public TardisRemoteKeyListener(TardisPlugin plugin) {
        this.plugin = plugin;
        rkey = Material.valueOf(this.plugin.getRecipesConfig().getString("shaped.TARDIS Remote Key.result"));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Action action = event.getAction();
        Player player = event.getPlayer();
        if ((!action.equals(Action.LEFT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_AIR)) || player.isSneaking()) {
            return;
        }
        // check item in hand
        ItemStack is = player.getInventory().getItemInMainHand();
        if (is == null || !is.getType().equals(rkey)) {
            return;
        }
        if (is.hasItemMeta() && Objects.requireNonNull(is.getItemMeta()).hasDisplayName() && is.getItemMeta().getDisplayName().equals("TARDIS Remote Key")) {
            String uuid = player.getUniqueId().toString();
            // has TARDIS?
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (!rs.resultSet()) {
                return;
            }
            Tardis tardis = rs.getTardis();
            int id = tardis.getTardisId();
            boolean powered = tardis.isPowered();
            Preset preset = tardis.getPreset();
            if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                TardisMessage.send(player, "SIEGE_NO_CONTROL");
                return;
            }
            if (plugin.getTrackerKeeper().getDispersedTardises().contains(id)) {
                TardisMessage.send(player, "NOT_WHILE_DISPERSED");
                return;
            }
            boolean hidden = tardis.isHidden();
            if (action.equals(Action.LEFT_CLICK_AIR)) {
                // get the TARDIS current location
                HashMap<String, Object> wherec = new HashMap<>();
                wherec.put("tardis_id", id);
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
                if (!rsc.resultSet()) {
                    return;
                }
                Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                HashMap<String, Object> whered = new HashMap<>();
                whered.put("tardis_id", id);
                ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
                if (rsd.resultSet()) {
                    // toggle door lock
                    int locked = (rsd.isLocked()) ? 0 : 1;
                    HashMap<String, Object> setl = new HashMap<>();
                    setl.put("locked", locked);
                    HashMap<String, Object> wherel = new HashMap<>();
                    wherel.put("tardis_id", id);
                    // always lock / unlock both doors
                    plugin.getQueryFactory().doUpdate("doors", setl, wherel);
                    String message = (rsd.isLocked()) ? plugin.getLanguage().getString("DOOR_UNLOCK") : plugin.getLanguage().getString("DOOR_DEADLOCK");
                    TardisMessage.send(player, "DOOR_LOCK", message);
                    TardisAdaptiveBoxLampToggler tpblt = new TardisAdaptiveBoxLampToggler(plugin);
                    TardisSounds.playTardisSound(l, "tardis_lock");
                    tpblt.toggleLamp(id, !powered, preset);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> tpblt.toggleLamp(id, powered, preset), 6L);
                }
            } else if (preset.equals(Preset.INVISIBLE)) {
                HashMap<String, Object> whered = new HashMap<>();
                whered.put("tardis_id", id);
                whered.put("door_type", 1);
                ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
                if (rsd.resultSet()) {
                    // get inner door block
                    Block block = Objects.requireNonNull(TardisStaticLocationGetters.getLocationFromDB(rsd.getDoorLocation())).getBlock();
                    boolean open = TardisStaticUtils.isDoorOpen(block);
                    // toggle door / portals
                    new TardisDoorToggler(plugin, block, player, false, open, id).toggleDoors();
                    String message = (open) ? "DOOR_CLOSED" : "DOOR_OPENED";
                    TardisMessage.send(player, message);
                }
            } else {
                if (plugin.getTrackerKeeper().getRebuildCooldown().containsKey(player.getUniqueId())) {
                    long now = System.currentTimeMillis();
                    long cooldown = plugin.getConfig().getLong("police_box.rebuild_cooldown");
                    long then = plugin.getTrackerKeeper().getRebuildCooldown().get(player.getUniqueId()) + cooldown;
                    if (now < then) {
                        TardisMessage.send(player.getPlayer(), "COOLDOWN", String.format("%d", cooldown / 1000));
                        return;
                    }
                }
                if (!powered) {
                    TardisMessage.send(player, "POWER_DOWN");
                    return;
                }
                if (hidden) {
                    // rebuild
                    new TardisRebuildCommand(plugin).rebuildPreset(player);
                } else {
                    // hide
                    new TardisHideCommand(plugin).hide(player);
                }
            }
        }
    }
}