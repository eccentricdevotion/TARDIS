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
package me.eccentric_nz.tardis.commands.preferences;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.commands.TardisCommandHelper;
import me.eccentric_nz.tardis.database.resultset.ResultSetArtronLevel;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.forcefield.TardisForceField;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.sonic.TardisSonicMenuInventory;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Command /tardisprefs [arguments].
 * <p>
 * Children begin instruction at the Time Lord Academy, at the age of 8, in a special ceremony. The Gallifreyans are
 * forced to look into the Untempered Schism, which shows the entirety of the Time Vortex and the power that the Time
 * Lords have.
 *
 * @author eccentric_nz
 */
public class TardisPrefsCommands implements CommandExecutor {

    private final TardisPlugin plugin;
    private final List<String> firstArgs = new ArrayList<>();

    public TardisPrefsCommands(TardisPlugin plugin) {
        this.plugin = plugin;
        firstArgs.add("auto");
        firstArgs.add("auto_powerup");
        firstArgs.add("auto_rescue");
        firstArgs.add("auto_siege");
        firstArgs.add("beacon");
        firstArgs.add("build");
        firstArgs.add("close_gui");
        firstArgs.add("difficulty");
        firstArgs.add("dnd");
        firstArgs.add("eps");
        firstArgs.add("eps_message");
        firstArgs.add("farm");
        firstArgs.add("flight");
        firstArgs.add("floor");
        firstArgs.add("forcefield");
        firstArgs.add("hads");
        firstArgs.add("hads_type");
        firstArgs.add("hum");
        firstArgs.add("isomorphic");
        firstArgs.add("key");
        firstArgs.add("key_menu");
        firstArgs.add("junk");
        firstArgs.add("language");
        firstArgs.add("lanterns");
        firstArgs.add("lock_containers");
        firstArgs.add("minecart");
        firstArgs.add("quotes");
        firstArgs.add("renderer");
        firstArgs.add("sfx");
        firstArgs.add("siege_floor");
        firstArgs.add("siege_wall");
        firstArgs.add("sign");
        firstArgs.add("sonic");
        firstArgs.add("submarine");
        firstArgs.add("telepathy");
        firstArgs.add("travelbar");
        firstArgs.add("wall");
        firstArgs.add("wool_lights");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        // If the player typed /tardisprefs then do the following...
        // check there is the right number of arguments
        if (cmd.getName().equalsIgnoreCase("tardisprefs")) {
            if (args.length == 0) {
                new TardisCommandHelper(plugin).getCommand("tardisprefs", sender);
                return true;
            }
            if (player == null) {
                TardisMessage.send(sender, "CMD_PLAYER");
                return true;
            }
            String pref = args[0].toLowerCase(Locale.ENGLISH);
            if (firstArgs.contains(pref)) {
                if (TardisPermission.hasPermission(player, "tardis.timetravel")) {
                    if (pref.equals("sonic")) {
                        // open sonic prefs menu
                        ItemStack[] sonics = new TardisSonicMenuInventory(plugin).getMenu();
                        Inventory sim = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Sonic Prefs Menu");
                        sim.setContents(sonics);
                        player.openInventory(sim);
                        return true;
                    }
                    if (pref.equals("key_menu")) {
                        // open sonic prefs menu
                        ItemStack[] keys = new TardisKeyMenuInventory().getMenu();
                        Inventory sim = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "TARDIS Key Prefs Menu");
                        sim.setContents(keys);
                        player.openInventory(sim);
                        return true;
                    }
                    String uuid = player.getUniqueId().toString();
                    // get the players preferences
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
                    HashMap<String, Object> set = new HashMap<>();
                    // if no prefs record found, make one
                    if (!rsp.resultSet()) {
                        set.put("uuid", uuid);
                        plugin.getQueryFactory().doInsert("player_prefs", set);
                    }
                    switch (pref) {
                        case "difficulty":
                            return new TardisSetDifficultyCommand(plugin).setDiff(player, args);
                        case "eps_message":
                            return new TardisEpsMessageCommand().setMessage(player, args);
                        case "flight":
                            return new TardisSetFlightCommand().setMode(player, args);
                        case "hads_type":
                            return new TardisHadsTypeCommand().setHadsPref(player, args);
                        case "hum":
                            return new TardisHumCommand().setHumPref(player, args);
                        case "isomorphic":
                            return new TardisIsomorphicCommand(plugin).toggleIsomorphicControls(player);
                        case "key":
                            return new TardisSetKeyCommand(plugin).setKeyPref(player, args);
                        case "language":
                            return new TardisSetLanguageCommand().setLanguagePref(player, args);
                        case "wall":
                        case "floor":
                        case "siege_wall":
                        case "siege_floor":
                            return new TardisFloorCommand().setFloorOrWallBlock(player, args);
                        default:
                            if (args.length < 2 || (!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("off"))) {
                                TardisMessage.send(player, "PREF_ON_OFF", pref);
                                return false;
                            }
                            if (pref.equals("forcefield") && TardisPermission.hasPermission(player, "tardis.forcefield")) {
                                // add tardis + location
                                if (args[1].equalsIgnoreCase("on")) {
                                    // check power
                                    ResultSetArtronLevel rsal = new ResultSetArtronLevel(plugin, uuid);
                                    if (rsal.resultset()) {
                                        if (rsal.getArtronLevel() <= plugin.getArtronConfig().getInt("standby")) {
                                            TardisMessage.send(player, "POWER_LOW");
                                            return true;
                                        }
                                        if (TardisForceField.addToTracker(player)) {
                                            TardisMessage.send(player, "PREF_WAS_ON", "The TARDIS force field");
                                        }
                                    } else {
                                        TardisMessage.send(player, "POWER_LEVEL");
                                        return true;
                                    }
                                } else {
                                    plugin.getTrackerKeeper().getActiveForceFields().remove(player.getUniqueId());
                                    TardisMessage.send(player, "PREF_WAS_OFF", "The TARDIS force field");
                                }
                                return true;
                            }
                            return switch (pref) {
                                case "build" -> new TardisBuildCommand(plugin).toggleCompanionBuilding(player, args);
                                case "junk" -> new TardisJunkPreference(plugin).toggle(player, args[1]);
                                default -> new TardisToggleOnOffCommand(plugin).toggle(player, args);
                            };
                    }
                } else {
                    TardisMessage.send(player, "NO_PERMS");
                    return false;
                }
            } else {
                TardisMessage.send(player, "PREF_NOT_VALID");
            }
        }
        return false;
    }
}