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
package me.eccentric_nz.TARDIS.commands.preferences;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetArtronLevel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.forcefield.ForceField;
import me.eccentric_nz.TARDIS.playerprefs.TARDISKeyMenuInventory;
import me.eccentric_nz.TARDIS.playerprefs.TARDISSonicMenuInventory;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Command /tardisprefs [arguments].
 * <p>
 * Children begin instruction at the Time Lord Academy, at the age of 8, in a
 * special ceremony. The Gallifreyans are forced to look into the Untempered
 * Schism, which shows the entirety of the Time Vortex and the power that the
 * Time Lords have.
 *
 * @author eccentric_nz
 */
public class TARDISPrefsCommands implements CommandExecutor {

    private static final ImmutableList<String> firstArgs = ImmutableList.of(
            "announce_repeaters", "auto", "auto_powerup", "auto_rescue", "auto_siege",
            "beacon", "build",
            "close_gui", "console_labels",
            "dialogs", "dnd", "dynamic_lamps",
            "effect", "eps", "eps_message",
            "farm", "flight", "floor", "forcefield",
            "hads", "hads_type", "hum",
            "info", "isomorphic",
            "key", "key_menu",
            "junk",
            "language", "lights", "lock_containers",
            "minecart",
            "open_display_door",
            "quotes",
            "regen_block", "renderer",
            "sfx", "shape", "siege_floor", "siege_wall", "sign", "silence_mobs", "sonic", "speed", "submarine",
            "telepathy", "translate", "travelbar",
            "wall"
    );
    private final TARDIS plugin;

    public TARDISPrefsCommands(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Returns all the root arguments for the command.
     *
     * @return a list of root arguments
     */
    public static ImmutableList<String> getRootArgs() {
        return firstArgs;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player player) {
            // If the player typed /tardisprefs then do the following...
            // check there is the right number of arguments
            if (cmd.getName().equalsIgnoreCase("tardisprefs")) {
                if (args.length == 0) {
                    new TARDISCommandHelper(plugin).getCommand("tardisprefs", sender);
                    return true;
                }
                String pref = args[0].toLowerCase(Locale.ROOT);
                if (!firstArgs.contains(pref)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "PREF_NOT_VALID");
                    return true;
                }
                if (!TARDISPermission.hasPermission(player, "tardis.timetravel")
                        || TARDISPermission.isNegated(player, "tardis.prefs." + pref)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
                    return true;
                }
                if (pref.equals("sonic")) {
                    // open sonic prefs menu
                    player.openInventory(new TARDISSonicMenuInventory(plugin).getInventory());
                    return true;
                }
                if (pref.equals("key_menu")) {
                    // open key prefs menu
                    player.openInventory(new TARDISKeyMenuInventory(plugin).getInventory());
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
                    case "console_labels" -> {
                        return new LabelsCommand(plugin).toggle(player, args[1]);
                    }
                    case "eps_message" -> {
                        String message;
                        int count = args.length;
                        ItemStack bq = player.getInventory().getItemInMainHand();
                        if (bq.getType().equals(Material.WRITABLE_BOOK) || bq.getType().equals(Material.WRITTEN_BOOK)) {
                            BookMeta bm = (BookMeta) bq.getItemMeta();
                            List<Component> pages = bm.pages();
                            StringBuilder sb = new StringBuilder();
                            pages.forEach((s) -> sb.append(ComponentUtils.stripColour(s)).append(" "));
                            message = sb.toString();
                        } else {
                            if (count < 2) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "EP1_NEED");
                                return false;
                            }
                            message = String.join(" ", Arrays.copyOfRange(args, 1, count));
                        }
                        return new EPSMessageCommand(plugin).setMessage(player, message);
                    }
                    case "flight" -> {
                        if (args.length < 2) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "FLIGHT_NEED");
                            return false;
                        }
                        return new SetFlightCommand(plugin).setMode(player, args[1]);
                    }
                    case "hads_type" -> {
                        if (args.length < 2) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "HADS_NEED");
                            return false;
                        }
                        return new HadsTypeCommand(plugin).setHadsPref(player, args[1]);
                    }
                    case "hum" -> {
                        if (args.length < 2) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "HUM_NEED");
                            return false;
                        }
                        return new HumCommand(plugin).setHumPref(player, args[1]);
                    }
                    case "isomorphic" -> {
                        return new IsomorphicCommand(plugin).toggleIsomorphicControls(player);
                    }
                    case "key" -> {
                        if (args.length < 2) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "KEY_NEED");
                            return false;
                        }
                        return new SetKeyCommand(plugin).setKeyPref(player, args[1]);
                    }
                    case "language", "translate" -> {
                        return new SetLanguageCommand(plugin).setLanguagePref(player, args);
                    }
                    case "lights" -> {
                        if (args.length < 2) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "LIGHT_NEED");
                            return false;
                        }
                        return new LightsCommand(plugin).setLightsPref(player, args[1]);
                    }
                    case "wall", "floor", "siege_wall", "siege_floor" -> {
                        if (args.length < 2) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "PREF_WALL", pref);
                            return false;
                        }
                        return new FloorCommand(plugin).setFloorOrWallBlock(player, pref, args[1]);
                    }
                    case "effect", "shape", "speed" -> {
                        if (args.length < 2) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                            return true;
                        }
                        String which = args[0].toLowerCase(Locale.ROOT);
                        return new ParticlePrefsCommand(plugin).setPartclePref(player, which, args[1]);
                    }
                    case "silence_mobs" -> {
                        return new SilenceMobsCommand(plugin).toggle(player, args[1]);
                    }
                    default -> {
                        if (args.length < 2 || (!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("off"))) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "PREF_ON_OFF", pref);
                            return false;
                        }
                        if (pref.equals("forcefield") && TARDISPermission.hasPermission(player, "tardis.forcefield")) {
                            // add tardis + location
                            if (args[1].equalsIgnoreCase("on")) {
                                // check power
                                ResultSetArtronLevel rsal = new ResultSetArtronLevel(plugin, uuid);
                                if (rsal.resultset()) {
                                    if (rsal.getArtronLevel() <= plugin.getArtronConfig().getInt("standby")) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_LOW");
                                        return true;
                                    }
                                    if (ForceField.addToTracker(player)) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "PREF_WAS_ON", "The TARDIS force field");
                                    }
                                } else {
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_LEVEL");
                                    return true;
                                }
                            } else {
                                plugin.getTrackerKeeper().getActiveForceFields().remove(player.getUniqueId());
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "PREF_WAS_OFF", "The TARDIS force field");
                            }
                            return true;
                        }
                        return switch (pref) {
                            case "build" -> new BuildCommand(plugin).toggleCompanionBuilding(player, args[1]);
                            case "junk" -> new JunkPreference(plugin).toggle(player, args[1]);
                            default -> new ToggleOnOffCommand(plugin).toggle(player, args[0], args[1]);
                        };
                    }
                }
            }
        }
        return false;
    }
}
