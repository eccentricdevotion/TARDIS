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
package me.eccentric_nz.tardis.commands;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A trilanic activator is a device created and used by the Zygons to control their Skarasens. It is hand-sized and has
 * a texture like wet skin.
 *
 * @author eccentric_nz
 */
public class TardisTextureCommands implements CommandExecutor {

    private final TardisPlugin plugin;
    private final List<String> firstArgs = new ArrayList<>();

    public TardisTextureCommands(TardisPlugin plugin) {
        this.plugin = plugin;
        firstArgs.add("on");
        firstArgs.add("off");
        firstArgs.add("in");
        firstArgs.add("out");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (cmd.getName().equalsIgnoreCase("tardistexture")) {
            if (args.length == 0) {
                new TardisCommandHelper(plugin).getCommand("tardistexture", sender);
                return true;
            }
            String pref = args[0].toLowerCase(Locale.ENGLISH);
            if (!firstArgs.contains(pref)) {
                return false;
            }
            if (player == null) {
                TardisMessage.send(sender, "CMD_PLAYER");
                return false;
            }
            if (TardisPermission.hasPermission(player, "tardis.texture")) {
                // get the players preferences
                String playerUuid = player.getUniqueId().toString();
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, playerUuid);
                HashMap<String, Object> set = new HashMap<>();
                // if no prefs record found, make one
                if (!rsp.resultSet()) {
                    set.put("uuid", playerUuid);
                    plugin.getQueryFactory().doInsert("player_prefs", set);
                }
                HashMap<String, Object> upd = new HashMap<>();
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
                            new URL(args[1]);
                            if (pref.equals("in")) {
                                upd.put("texture_in", args[1]);
                            }
                            if (pref.equals("out")) {
                                upd.put("texture_out", args[1]);
                            }
                        } catch (MalformedURLException e) {
                            TardisMessage.send(player, "URL", e.getMessage());
                            return true;
                        }
                    }
                }
                if (upd.size() > 0) {
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("uuid", playerUuid);
                    plugin.getQueryFactory().doUpdate("player_prefs", upd, where);
                    TardisMessage.send(player, "PREF_TEXTURE");
                    return true;
                }
            }
        }
        return false;
    }
}
