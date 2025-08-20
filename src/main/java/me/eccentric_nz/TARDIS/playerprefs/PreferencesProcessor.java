/*
 * Copyright (C) 2025 eccentric_nz
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

import io.papermc.paper.dialog.DialogResponseView;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.artron.TARDISBeaconToggler;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISBuildCommand;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISJunkPreference;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetArtronLevel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.FlightMode;
import me.eccentric_nz.TARDIS.enumeration.Hum;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.forcefield.TARDISForceField;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PreferencesProcessor {

    private final TARDIS plugin;
    private final Player player;
    private final List<Material> keys = new ArrayList<>();

    public PreferencesProcessor(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        plugin.getBlocksConfig().getStringList("keys").forEach((m) -> {
            try {
                keys.add(Material.valueOf(m));
            } catch (IllegalArgumentException e) {
                plugin.debug("Illegal Key value!");
            }
        });
    }

    public void process(DialogResponseView response) {
        /* payload
         {announce_repeaters_on:0b,auto_on:0b,auto_powerup_on:0b,auto_rescue_on:0b,auto_siege_on:0b,beacon_on:0b,
         close_gui_on:1b,dialogs_on:1b,dnd_on:0b,dynamic_lamps_on:0b,eps_message:"",eps_on:0b,farm_on:1b,
         flight:"NORMAL",forcefield:0b,hads_on:1b,hads_type:"DISPLACEMENT",hum:"ALIEN",info_on:1b,iso_on:0b,
         junk_mode:0b,key:"TRIAL_KEY",minecart_on:0b,open_display_door_on:1b,quotes_on:0b,renderer_on:1b,sfx_on:0b,
         sign_on:1b,submarine_on:0b,telepathy_on:0b,travelbar_on:1b}
        */
        String raw = response.payload().string();
//        plugin.debug(raw);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        rs.resultSet();
        Tardis tardis = rs.getTardis();
        HashMap<String, Object> set = new HashMap<>();
        for (String part : raw.substring(1, raw.length() - 1).split(",")) {
            // if the key is 'id' ignore it
            if (part.startsWith("id")) {
                continue;
            }
            if (!part.contains(":")) {
                continue;
            }
            // get the key and value - split(":")
            String[] colon = part.split(":");
            if (colon.length > 1) {
                switch (colon[0]) {
                    case "beacon_on" -> {
                        // get tardis id
                        ResultSetTardisID rsi = new ResultSetTardisID(plugin);
                        if (rsi.fromUUID(player.getUniqueId().toString())) {
                            new TARDISBeaconToggler(plugin).flickSwitch(player.getUniqueId(), rsi.getTardisId(), colon[1].equals("1b"));
                            set.put("beacon_on", colon[1].equals("1b") ? 1 : 0);
                        }
                    }
                    case "build" -> {
                        if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("allow.wg_flag_set")) {
                            String onoff = colon[1].equals("1b") ? "on" : "off";
                            new TARDISBuildCommand(plugin).toggleCompanionBuilding(player, new String[]{"", onoff});
                        }
                    }
                    case "flight" -> {
                        FlightMode flightMode = FlightMode.valueOf(StringUtils.strip(colon[1], "\""));
                        set.put("flying_mode", flightMode.getMode());
                    }
                    case "forcefield" -> {
                        if (!TARDISPermission.hasPermission(player, "tardis.forcefield")) {
                            return;
                        }
                        // get current setting
                        if (colon[1].equals("1b") && !plugin.getTrackerKeeper().getActiveForceFields().containsKey(player.getUniqueId())) {
                            // check power
                            ResultSetArtronLevel rsal = new ResultSetArtronLevel(plugin, player.getUniqueId().toString());
                            if (rsal.resultset()) {
                                if (rsal.getArtronLevel() <= plugin.getArtronConfig().getInt("standby")) {
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_LOW");
                                } else if (TARDISForceField.addToTracker(player)) {
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "PREF_WAS_ON", "The TARDIS force field");
                                }
                            } else {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_LEVEL");
                            }
                        } else if (colon[1].equals("0b") && plugin.getTrackerKeeper().getActiveForceFields().containsKey(player.getUniqueId())) {
                            plugin.getTrackerKeeper().getActiveForceFields().remove(player.getUniqueId());
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "PREF_WAS_OFF", "The TARDIS force field");
                        }
                    }
                    case "junk_mode" -> {
                        if ((tardis.getPreset().equals(ChameleonPreset.JUNK_MODE) && colon[1].equals("0b"))
                                || (!tardis.getPreset().equals(ChameleonPreset.JUNK_MODE) && colon[1].equals("1b"))) {
                            new TARDISJunkPreference(plugin).toggle(player, colon[1].equals("1b") ? "off" : "on");
                        }
                    }
                    case "hads_type", "eps_message" -> set.put(colon[0], StringUtils.strip(colon[1], "\""));
                    case "hum" -> {
                        Hum go;
                        go = Hum.valueOf(StringUtils.strip(colon[1], "\""));
                        String hum_set = (go.equals(Hum.RANDOM)) ? "" : go.toString().toLowerCase(Locale.ROOT);
                        set.put("hum", hum_set);
                    }
                    case "isomorphic" -> {
                        int iso = (colon[1].equals("0b")) ? 0 : 1;
                        if ((tardis.isIsomorphicOn() && iso == 1) || (!tardis.isIsomorphicOn() && iso == 0)) {
                            continue;
                        }
                        HashMap<String, Object> seti = new HashMap<>();
                        seti.put("iso_on", iso);
                        HashMap<String, Object> wheret = new HashMap<>();
                        wheret.put("tardis_id", tardis.getTardisId());
                        plugin.getQueryFactory().doUpdate("tardis", seti, wheret);
                    }
                    case "key" -> {
                        String setMaterial = StringUtils.strip(colon[1], "\"").toUpperCase(Locale.ROOT);
                        Material go;
                        try {
                            go = Material.valueOf(setMaterial);
                        } catch (IllegalArgumentException e) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "MATERIAL_NOT_VALID");
                            return;
                        }
                        if (go.isBlock()) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "KEY_NO_BLOCK");
                            return;
                        }
                        if (plugin.getConfig().getBoolean("travel.give_key") && !plugin.getConfig().getBoolean("allow.all_blocks") && !keys.contains(go)) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "MATERIAL_NOT_VALID");
                            return;
                        }
                        String field = ("sqlite".equals(plugin.getConfig().getString("storage.database"))) ? "key" : "key_item";
                        set.put(field, setMaterial);
                    }
                    case "lock_containers" -> {
                        if (plugin.isWorldGuardOnServer() && plugin.getUtils().inTARDISWorld(player)) {
                            if (colon[1].equals("1b")) {
                                plugin.getWorldGuardUtils().lockContainers(player.getWorld(), player.getName());
                            } else {
                                plugin.getWorldGuardUtils().unlockContainers(player.getWorld(), player.getName());
                            }
                        }
                    }
                    default -> set.put(colon[0], colon[1].equals("1b") ? 1 : 0);
                }
            }
        }
        if (!set.isEmpty()) {
            HashMap<String, Object> whereP = new HashMap<>();
            whereP.put("uuid", player.getUniqueId().toString());
            plugin.getQueryFactory().doUpdate("player_prefs", set, whereP);
            plugin.getMessenger().message(player, TardisModule.TARDIS, "Preferences saved.");
        }
    }
}
