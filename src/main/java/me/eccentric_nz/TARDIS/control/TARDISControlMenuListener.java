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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.ARS.TARDISARSInventory;
import me.eccentric_nz.TARDIS.ARS.TARDISARSMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.chameleon.gui.TARDISChameleonInventory;
import me.eccentric_nz.TARDIS.commands.dev.SystemTreeCommand;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISHideCommand;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISRebuildCommand;
import me.eccentric_nz.TARDIS.companionGUI.TARDISCompanionAddInventory;
import me.eccentric_nz.TARDIS.companionGUI.TARDISCompanionInventory;
import me.eccentric_nz.TARDIS.control.actions.DirectionAction;
import me.eccentric_nz.TARDIS.control.actions.FastReturnAction;
import me.eccentric_nz.TARDIS.control.actions.SiegeAction;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.lights.TARDISLightsInventory;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.move.TARDISBlackWoolToggler;
import me.eccentric_nz.TARDIS.playerprefs.TARDISPrefsMenuInventory;
import me.eccentric_nz.TARDIS.rooms.TARDISExteriorRenderer;
import me.eccentric_nz.TARDIS.transmat.TARDISTransmatInventory;
import me.eccentric_nz.TARDIS.travel.TARDISAreasInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTemporalLocatorInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTerminalInventory;
import me.eccentric_nz.TARDIS.travel.dialog.TerminalDialog;
import me.eccentric_nz.TARDIS.travel.save.TARDISSavesPlanetInventory;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISControlMenuListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISControlMenuListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onControlMenuInteract(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (!(inventory.getHolder(false) instanceof TARDISControlInventory)) {
            // it's not a control menu inventory, ignore it.
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        if (slot < 0 || slot > 53) {
            return;
        }
        InventoryView view = event.getView();
        ItemStack is = view.getItem(slot);
        if (is == null) {
            return;
        }
        UUID uuid = player.getUniqueId();
        // get the TARDIS the player is in
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("uuid", uuid.toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
        if (!rst.resultSet()) {
            return;
        }
        int id = rst.getTardis_id();
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            return;
        }
        Tardis tardis = rs.getTardis();
        // check they initialised
        if (!tardis.isTardisInit()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NO_INIT");
            return;
        }
        if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPoweredOn() && slot != 6 && slot != 13 && slot != 20) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN");
            return;
        }
        if (!tardis.isHandbrakeOn()) {
            switch (slot) {
                case 4, 11, 13, 40 -> {
                    if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_TRAVELLING");
                        return;
                    }
                }
                default -> {
                }
            }
        }
        boolean lights = tardis.isLightsOn();
        int level = tardis.getArtronLevel();
        TARDISCircuitChecker tcc = null;
        if (plugin.getConfig().getBoolean("difficulty.circuits")) {
            tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
        }
        switch (slot) {
            case 0 -> {
                // random location
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
                    return;
                }
                if (tcc != null && !tcc.hasInput() && !plugin.getUtils().inGracePeriod(player, false)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "INPUT_MISSING");
                    return;
                }
                close(player, false);
                // give the GUI time to close first
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISRandomButton(plugin, player, id, level, 0, tardis.getCompanions(), tardis.getUuid()).clickButton(), 2L);
            }
            case 2 -> {
                // ars
                if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(uuid.toString(), SystemTree.ROOM_GROWING)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Room Growing");
                    return;
                }
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
                    return;
                }
                // check they're in a compatible world
                if (!plugin.getUtils().canGrowRooms(tardis.getChunk())) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ROOM_OWN_WORLD");
                    return;
                }
                // check they have permission to grow rooms
                if (!TARDISPermission.hasPermission(player, "tardis.architectural")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_ROOMS");
                    return;
                }
                if (tcc != null && !tcc.hasARS() && !plugin.getUtils().inGracePeriod(player, true)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_MISSING");
                    return;
                }
                player.openInventory(new TARDISARSInventory(plugin, player).getInventory());
            }
            case 4 -> {
                // chameleon circuit
                if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(uuid.toString(), SystemTree.CHAMELEON_CIRCUIT)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Chameleon Circuit");
                    return;
                }
                if (tcc != null && !tcc.hasChameleon()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CHAM_MISSING");
                    return;
                }
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
                    return;
                }
                if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_DISPERSED");
                    return;
                }
                // open Chameleon Circuit GUI
                player.openInventory(new TARDISChameleonInventory(plugin, tardis.getAdaption(), tardis.getPreset(), tardis.getItemPreset()).getInventory());
            }
            case 6 -> {
                // artron level
                close(player, true);
                plugin.getMessenger().sendArtron(player, id, 0);
            }
            case 8 -> {
                // zero room
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
                    return;
                }
                int zero_amount = plugin.getArtronConfig().getInt("zero");
                if (level < zero_amount) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ZERO_ENERGY");
                    return;
                }
                Location zero = TARDISStaticLocationGetters.getLocationFromDB(tardis.getZero());
                if (zero != null) {
                    close(player, false);
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ZERO_READY");
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISExteriorRenderer(plugin).transmat(player, COMPASS.SOUTH, zero), 20L);
                    plugin.getTrackerKeeper().getZeroRoomOccupants().add(uuid);
                    HashMap<String, Object> wherez = new HashMap<>();
                    wherez.put("tardis_id", id);
                    plugin.getQueryFactory().alterEnergyLevel("tardis", -zero_amount, wherez, player);
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_ZERO");
                }
            }
            case 9, 18 -> {
                // saves
                if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(uuid.toString(), SystemTree.SAVES)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Saves");
                    return;
                }
                // 9 = saves for the TARDIS the player is in
                // 18 = saves for the player's TARDIS (if they're not in their own)
                // so, determine player's TARDIS id vs occupied TARDIS id
                int whichId = tardis.getTardisId();
                if (slot == 18) {
                    ResultSetTardisID tstid = new ResultSetTardisID(plugin);
                    if (tstid.fromUUID(uuid.toString())) {
                        int pid = tstid.getTardisId();
                        if (whichId != pid) {
                            whichId = pid;
                            plugin.getTrackerKeeper().getSavesIds().put(uuid, pid);
                        }
                    }
                } else {
                    plugin.getTrackerKeeper().getSavesIds().remove(uuid);
                }
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
                    return;
                }
                if (tcc != null && !tcc.hasMemory()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_MEM_CIRCUIT");
                    return;
                }
                player.openInventory(new TARDISSavesPlanetInventory(plugin, whichId, player).getInventory());
            }
            case 11 -> {
                // desktop theme
                if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(uuid.toString(), SystemTree.DESKTOP_THEME)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Desktop Theme");
                    return;
                }
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
                    return;
                }
                if (!TARDISPermission.hasPermission(player, "tardis.upgrade")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_UPGRADE");
                    return;
                }
                new TARDISThemeButton(plugin, player, tardis.getSchematic(), level, id).clickButton();
            }
            case 13 -> {
                // siege
                close(player, true);
                new SiegeAction(plugin).clickButton(tcc, player, tardis.isPoweredOn(), id);
            }
            case 15 -> {
                // scanner
                close(player, false);
                new TARDISScanner(plugin).scan(id, player, tardis.getRenderer(), level);
            }
            //player prefs
            case 17 -> player.openInventory(new TARDISPrefsMenuInventory(plugin, uuid).getInventory());
            case 20 -> {
                // power up/down
                if (plugin.getConfig().getBoolean("allow.power_down")) {
                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                        plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NOT_IN_VORTEX");
                        return;
                    }
                    close(player, true);
                    new TARDISPowerButton(plugin, id, player, tardis.getPreset(), tardis.isPoweredOn(), tardis.isHidden(), lights, player.getLocation(), level, tardis.getSchematic().getLights()).clickButton();
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN_DISABLED");
                }
            }
            case 22 -> {
                // hide
                close(player, true);
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
                    return;
                }
                new TARDISHideCommand(plugin).hide(player);
            }
            case 24 -> {
                // TIS
                close(player, false);
                new TARDISInfoMenuButton(plugin, player).clickButton();
            }
            case 26 -> {
                // Companions Menu
                String comps = tardis.getCompanions();
                if (comps == null || comps.isEmpty()) {
                    close(player, true);
                    // open the add companions inventory
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                            player.openInventory(new TARDISCompanionAddInventory(plugin, player).getInventory()), 2L);
                    return;
                }
                String[] companionData = comps.split(":");
                player.openInventory(new TARDISCompanionInventory(plugin, companionData).getInventory());
            }
            case 27 -> {
                // fast return
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
                    return;
                }
                if (tcc != null && !tcc.hasInput() && !plugin.getUtils().inGracePeriod(player, false)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "INPUT_MISSING");
                    return;
                }
                close(player, false);
                new FastReturnAction(plugin).clickButton(player, id, tardis);
            }
            case 29 -> player.openInventory(new TARDISLightsInventory(plugin, id, player.getUniqueId()).getInventory());
            case 31 -> {
                // rebuild
                close(player, true);
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
                    return;
                }
                new TARDISRebuildCommand(plugin).rebuildPreset(player);
            }
            // transmat
            case 33 -> player.openInventory(new TARDISTransmatInventory(plugin, id, player).getInventory());
            case 35 -> new SystemTreeCommand(plugin).open(player); // system upgrades
            case 36 -> {
                // areas
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
                    return;
                }
                if (tcc != null && !tcc.hasMemory()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_MEM_CIRCUIT");
                    return;
                }
                player.openInventory(new TARDISAreasInventory(plugin, player).getInventory());
            }
            case 38 -> {
                // toggle wool
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
                    return;
                }
                close(player, true);
                new TARDISBlackWoolToggler(plugin).toggleBlocks(id, player);
            }
            case 40 -> {
                // direction
                String direction = new DirectionAction(plugin).rotate(id, player);
                if (!direction.isEmpty()) {
                    // update the lore
                    ItemStack d = view.getItem(40);
                    ItemMeta im = d.getItemMeta();
                    im.lore(List.of(Component.text(direction)));
                    d.setItemMeta(im);
                }
            }
            case 45 -> {
                // destination terminal
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
                    return;
                }
                if (level < plugin.getArtronConfig().getInt("travel")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ENERGY");
                    return;
                }
                if (tcc != null && !tcc.hasInput() && !plugin.getUtils().inGracePeriod(player, false)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "INPUT_MISSING");
                    return;
                }
                ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
                if (rspp.resultSet()) {
                    if (rspp.isDialogsOn()) {
                        close(player, false);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> Audience.audience(player).showDialog(new TerminalDialog().create()), 2L);
                    } else {
                        player.openInventory(new TARDISTerminalInventory(plugin).getInventory());
                    }
                }
            }
            // tardis map
            case 47 -> player.openInventory(new TARDISARSMap(plugin).getInventory());
            case 49 -> {
                // temporal
                if (!TARDISPermission.hasPermission(player, "tardis.temporal")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_TEMPORAL");
                    return;
                }
                if (tcc != null && !tcc.hasTemporal() && !plugin.getUtils().inGracePeriod(player, false)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TEMP_MISSING");
                    return;
                }
                player.openInventory(new TARDISTemporalLocatorInventory(plugin).getInventory());
            }
            case 51 -> {
                // space-time throttle
                // update the lore
                ItemStack spt = view.getItem(51);
                ItemMeta im = spt.getItemMeta();
                String currentThrottle = ComponentUtils.stripColour(im.lore().getFirst());
                int delay = SpaceTimeThrottle.valueOf(currentThrottle).getDelay() - 1;
                if (delay < 1) {
                    delay = 4;
                }
                if (delay != 4 && plugin.getConfig().getBoolean("difficulty.system_upgrades")) {
                    switch (delay) {
                        case 3 -> {
                            if (!new SystemUpgradeChecker(plugin).has(uuid.toString(), SystemTree.FASTER)) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Faster");
                                delay = 4;
                            }
                        }
                        case 2 -> {
                            if (!new SystemUpgradeChecker(plugin).has(uuid.toString(), SystemTree.RAPID)) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Rapid");
                                delay = 3;
                            }
                        }
                        case 1 -> {
                            if (!new SystemUpgradeChecker(plugin).has(uuid.toString(), SystemTree.WARP)) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Warp");
                                delay = 2;
                            }
                        }
                        default -> { }
                    }
                }
                String throttle = SpaceTimeThrottle.getByDelay().get(delay).toString();
                im.lore(List.of(Component.text(throttle)));
                spt.setItemMeta(im);
                // update player prefs
                HashMap<String, Object> wherer = new HashMap<>();
                wherer.put("uuid", uuid.toString());
                HashMap<String, Object> setr = new HashMap<>();
                setr.put("throttle", delay);
                plugin.getQueryFactory().doUpdate("player_prefs", setr, wherer);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "THROTTLE", throttle);
            }
            case 53 -> close(player, false); // close
            default -> {
            }
        }
    }

    private void close(Player p, boolean check) {
        boolean close = true;
        if (check) {
            ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, p.getUniqueId().toString());
            if (rspp.resultSet()) {
                close = rspp.isCloseGUIOn();
            }
        }
        if (close) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, p::closeInventory, 1L);
        }
    }
}
