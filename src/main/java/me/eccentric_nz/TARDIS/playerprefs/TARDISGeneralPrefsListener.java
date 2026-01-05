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
package me.eccentric_nz.TARDIS.playerprefs;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.artron.TARDISBeaconToggler;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.preferences.BuildCommand;
import me.eccentric_nz.TARDIS.commands.preferences.IsomorphicCommand;
import me.eccentric_nz.TARDIS.commands.preferences.LabelsCommand;
import me.eccentric_nz.TARDIS.custommodels.GUIPlayerPreferences;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.forcefield.TARDISForceField;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISGeneralPrefsListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<String, String> lookup = new HashMap<>();

    public TARDISGeneralPrefsListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        lookup.put("Announce Repeaters", "announce_repeaters_on");
        lookup.put("Auto Power Up", "auto_powerup_on");
        lookup.put("Auto-rescue", "auto_rescue_on");
        lookup.put("Autonomous Siege", "auto_siege_on");
        lookup.put("Autonomous", "auto_on");
        lookup.put("Beacon", "beacon_on");
        lookup.put("Close GUI", "close_gui_on");
        lookup.put("Companion Build", "build_on");
        lookup.put("Dialogs", "dialogs_on");
        lookup.put("Do Not Disturb", "dnd_on");
        lookup.put("Emergency Programme One", "eps_on");
        lookup.put("Exterior Rendering Room", "renderer_on");
        lookup.put("Hostile Action Displacement System", "hads_on");
        lookup.put("Interior SFX", "sfx_on");
        lookup.put("Info GUI", "info_on");
        lookup.put("Minecart Sounds", "minecart_on");
        lookup.put("Mob Farming", "farm_on");
        lookup.put("Preset Sign", "sign_on");
        lookup.put("Open Display Door", "open_display_door_on");
        lookup.put("Submarine Mode", "submarine_on");
        lookup.put("Telepathic Circuit", "telepathy_on");
        lookup.put("Travel Bar", "travelbar_on");
        lookup.put("Who Quotes", "quotes_on");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPrefsMenuClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISGeneralPrefsInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 35) {
            return;
        }
        ItemStack is = event.getView().getItem(slot);
        if (is == null) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        ItemMeta im = is.getItemMeta();
        if (slot == 33) {
            // back
            player.openInventory(new TARDISPrefsMenuInventory(plugin, uuid).getInventory());
            return;
        }
        if (slot == 35) {
            // close
            close(player);
            return;
        }
        if (slot == GUIPlayerPreferences.FORCE_FIELD.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("Force Field")) {
            // toggle force field on / off
            if (TARDISPermission.hasPermission(player, "tardis.forcefield")) {
                // check they have upgrade
                if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.FORCE_FIELD)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Force Field");
                    return;
                }
                List<Component> lore = im.lore();
                boolean bool = (ComponentUtils.stripColour(lore.getFirst()).equals(plugin.getLanguage().getString("SET_OFF", "OFF")));
                if (bool) {
                    // check power
                    ResultSetArtronLevel rsal = new ResultSetArtronLevel(plugin, uuid.toString());
                    if (rsal.resultset()) {
                        if (rsal.getArtronLevel() <= plugin.getArtronConfig().getInt("standby")) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_LOW");
                            return;
                        }
                        if (TARDISForceField.addToTracker(player)) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "PREF_WAS_ON", "The TARDIS force field");
                        }
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_LEVEL");
                        return;
                    }
                } else {
                    plugin.getTrackerKeeper().getActiveForceFields().remove(player.getUniqueId());
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "PREF_WAS_OFF", "The TARDIS force field");
                }
                close(player);
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            }
            return;
        }
        List<Component> lore = im.lore();
        boolean bool = (ComponentUtils.stripColour(lore.getFirst()).equals(plugin.getLanguage().getString("SET_ON", "ON")));
        String value = (bool) ? plugin.getLanguage().getString("SET_OFF", "OFF") : plugin.getLanguage().getString("SET_ON", "ON");
        int b = (bool) ? 0 : 1;
        String which = ComponentUtils.stripColour(im.displayName());
        // get tardis record
        Tardis tardis = null;
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("uuid", uuid.toString());
        ResultSetTardis rsp = new ResultSetTardis(plugin, wherep, "", false);
        if (rsp.resultSet()) {
            tardis = rsp.getTardis();
        }
        switch (which) {
            case "Console Labels" -> {
                String[] args = new String[]{"console_labels", bool ? "off" : "on"};
                new LabelsCommand(plugin).toggle(player, args);
                return;
            }
            case "Isometric" -> {
                if (tardis!=null && (tardis.isIsomorphicOn() && !bool || !tardis.isIsomorphicOn() && bool)) {
                    new IsomorphicCommand(plugin).toggleIsomorphicControls(player);
                }
            }
            case "Junk TARDIS" -> {
                // must be on the outside of the TARDIS
                HashMap<String, Object> wheret = new HashMap<>();
                wheret.put("uuid", uuid);
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                if (rst.resultSet()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "JUNK_PRESET_OUTSIDE");
                    return;
                }
                if (plugin.getTrackerKeeper().getRebuildCooldown().containsKey(uuid)) {
                    long now = System.currentTimeMillis();
                    long cooldown = plugin.getConfig().getLong("police_box.rebuild_cooldown");
                    long then = plugin.getTrackerKeeper().getRebuildCooldown().get(uuid) + cooldown;
                    if (now < then) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "COOLDOWN", String.format("%d", cooldown / 1000));
                        return;
                    }
                }
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid.toString());
                ResultSetJunk rsj = new ResultSetJunk(plugin, where);
                boolean has = rsj.resultSet();
                if (tardis != null) {
                    String current = tardis.getPreset().toString();
                    // make sure is opposite
                    if (current.equals("JUNK_MODE") && !bool) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "JUNK_ALREADY_ON");
                        return;
                    }
                    if (!current.equals("JUNK_MODE") && bool) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "JUNK_ALREADY_OFF");
                        return;
                    }
                    int id = tardis.getTardisId();
                    String cham_set;
                    HashMap<String, Object> setj = new HashMap<>();
                    if (has) {
                        // update record with current preset
                        HashMap<String, Object> wherej = new HashMap<>();
                        wherej.put("uuid", uuid.toString());
                        setj.put("preset", current);
                        plugin.getQueryFactory().doSyncUpdate("junk", setj, wherej);
                    } else {
                        // create a junk record
                        setj.put("uuid", uuid.toString());
                        setj.put("tardis_id", id);
                        setj.put("preset", current);
                        plugin.getQueryFactory().doSyncInsert("junk", setj);
                    }
                    HashMap<String, Object> whereu = new HashMap<>();
                    whereu.put("uuid", uuid.toString());
                    HashMap<String, Object> sett = new HashMap<>();
                    String message = "JUNK_PRESET_ON";
                    if (bool) {
                        // restore saved preset
                        String preset = (has) ? rsj.getPreset().toString() : current;
                        sett.put("chameleon_preset", preset);
                        sett.put("chameleon_demat", "JUNK_MODE");
                        message = "JUNK_PRESET_OFF";
                        cham_set = preset;
                        plugin.getTrackerKeeper().getJunkPlayers().remove(uuid);
                    } else {
                        // save JUNK_MODE preset
                        sett.put("chameleon_preset", "JUNK_MODE");
                        sett.put("chameleon_demat", current);
                        // set chameleon adaption to OFF
                        sett.put("adapti_on", 0);
                        cham_set = "JUNK_MODE";
                        plugin.getTrackerKeeper().getJunkPlayers().put(uuid, id);
                    }
                    plugin.getQueryFactory().doSyncUpdate("tardis", sett, whereu);
                    // set the Chameleon Circuit sign(s)
                    HashMap<String, Object> wherec = new HashMap<>();
                    wherec.put("tardis_id", id);
                    wherec.put("type", 31);
                    ResultSetControls rsc = new ResultSetControls(plugin, wherec, true);
                    if (rsc.resultSet()) {
                        for (HashMap<String, String> map : rsc.getData()) {
                            TARDISStaticUtils.setSign(map.get("location"), 3, cham_set, player);
                        }
                    }
                    // rebuild
                    plugin.getMessenger().send(player, TardisModule.TARDIS, message);
                    player.performCommand("tardis rebuild");
                }
            }
            case "Companion Build" -> {
                String[] args = new String[2];
                args[0] = "";
                args[1] = value;
                new BuildCommand(plugin).toggleCompanionBuilding(player, args);
            }
            case "Lock Containers" -> {
                if (bool) {
                    plugin.getWorldGuardUtils().unlockContainers(player.getWorld(), player.getName());
                } else {
                    plugin.getWorldGuardUtils().lockContainers(player.getWorld(), player.getName());
                }
            }
            default -> {
                HashMap<String, Object> set = new HashMap<>();
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid.toString());
                if (ComponentUtils.stripColour(im.displayName()).equals("HADS Type")) {
                    value = (ComponentUtils.stripColour(lore.getFirst()).equals("DISPLACEMENT")) ? "DISPERSAL" : "DISPLACEMENT";
                    set.put("hads_type", value);
                } else {
                    set.put(lookup.get(ComponentUtils.stripColour(im.displayName())), b);
                }
                plugin.getQueryFactory().doUpdate("player_prefs", set, where);
            }
        }
        lore.set(0, Component.text(value));
        im.lore(lore);
        GUIPlayerPreferences gui = GUIPlayerPreferences.fromString(which);
        CustomModelDataComponent component = im.getCustomModelDataComponent();
        component.setFloats(value.equals(plugin.getLanguage().getString("SET_ON", "ON")) ? gui.getOnFloats() : gui.getOffFloats());
        im.setCustomModelDataComponent(component);
        is.setItemMeta(im);
        if (ComponentUtils.stripColour(im.displayName()).equals("Beacon")) {
            // get tardis id
            ResultSetTardisID rsi = new ResultSetTardisID(plugin);
            if (rsi.fromUUID(uuid.toString())) {
                new TARDISBeaconToggler(plugin).flickSwitch(uuid, rsi.getTardisId(), !bool);
            }
        }
    }
}
