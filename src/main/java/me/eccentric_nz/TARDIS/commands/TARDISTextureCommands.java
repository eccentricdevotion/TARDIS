/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A trilanic activator is a device created and used by the Zygons to control
 * their Skarasens. It is hand-sized and has a texture like wet skin.
 *
 * @author eccentric_nz
 */
public class TARDISTextureCommands implements CommandExecutor {

    private final TARDIS plugin;
    private final List<String> firstArgs = new ArrayList<String>();

    public TARDISTextureCommands(TARDIS plugin) {
        this.plugin = plugin;
        firstArgs.add("on");
        firstArgs.add("off");
        firstArgs.add("in");
        firstArgs.add("out");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (cmd.getName().equalsIgnoreCase("tardistexture")) {
            if (args.length == 0) {
                return false;
            }
            String pref = args[0].toLowerCase(Locale.ENGLISH);
            if (!firstArgs.contains(pref)) {
                return false;
            }
            if (player == null) {
                sender.sendMessage(plugin.pluginName + MESSAGE.MUST_BE_PLAYER.getText());
                return false;
            }
            if (player.hasPermission("tardis.texture")) {
                // get the players preferences
                String playerNameStr = player.getName();
                HashMap<String, Object> wherepp = new HashMap<String, Object>();
                wherepp.put("player", playerNameStr);
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
                QueryFactory qf = new QueryFactory(plugin);
                HashMap<String, Object> set = new HashMap<String, Object>();
                // if no prefs record found, make one
                if (!rsp.resultSet()) {
                    set.put("player", playerNameStr);
                    qf.doInsert("player_prefs", set);
                }
                HashMap<String, Object> upd = new HashMap<String, Object>();
                if (pref.equals("on")) {
                    upd.put("texture_on", 1);
                }
                if (pref.equals("off")) {
                    upd.put("texture_on", 0);
                }
                if (pref.equals("in") || pref.equals("out")) {
                    if (args.length < 2) {
                        return false;
                    }
                    if (pref.equals("out") && args[1].equalsIgnoreCase("default")) {
                        upd.put("texture_out", "default");
                    } else {
                        try {
                            URL url = new URL(args[1]);
                            if (pref.equals("in")) {
                                upd.put("texture_in", args[1]);
                            }
                            if (pref.equals("out")) {
                                upd.put("texture_out", args[1]);
                            }
                        } catch (MalformedURLException e) {
                            player.sendMessage(plugin.pluginName + "Not a valid URL! " + e.getMessage());
                            return true;
                        }
                    }
                }
                if (upd.size() > 0) {
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("player", playerNameStr);
                    qf.doUpdate("player_prefs", upd, where);
                    player.sendMessage(plugin.pluginName + "Your texture pack preference was set.");
                    return true;
                }
            }
        }
        return false;
    }
}
