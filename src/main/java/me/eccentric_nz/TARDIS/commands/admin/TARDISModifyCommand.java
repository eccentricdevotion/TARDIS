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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISModifyCommand {

    private final TARDIS plugin;

    public TARDISModifyCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean alterConfig(CommandSender sender, String[] args) {
        if (args.length < 2) {
            TARDISMessage.send(sender, "TOO_FEW_ARGS");
            return true;
        }
        if (!args[1].toLowerCase().equals("mv") && !args[1].toLowerCase().equals("wg")) {
            TARDISMessage.send(sender, "ARG_PLUGIN");
            return true;
        }
        if (args[1].toLowerCase().equals("mv") && !plugin.getPM().isPluginEnabled("Multiverse-Core")) {
            TARDISMessage.send(sender, "MV_DISABLED");
            return true;
        }
        if (args[1].toLowerCase().equals("wg") && !plugin.isWorldGuardOnServer()) {
            TARDISMessage.send(sender, "WG_DISABLED");
            return true;
        }
        Player p = null;
        if (sender instanceof Player) {
            p = (Player) sender;
        }
        if (args[1].toLowerCase().equals("mv")) {
            setMobSpawning(p);
        } else {
            removeBuildFlags(p);
        }
        return true;
    }

    /**
     * Set `animals` / `monsters` true and set `animalsrate` / `monstersrate` to
     * 0 for all TARDIS worlds.
     *
     * @param p the player to message if the command is run in-game
     */
    private void setMobSpawning(Player p) {
        for (World w : plugin.getServer().getWorlds()) {
            if (w.getName().contains("TARDIS")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set animals true " + w.getName());
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set monsters true " + w.getName());
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set animalsrate 0 " + w.getName());
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set monstersrate 0 " + w.getName());
                if (p != null) {
                    TARDISMessage.send(p, "MV_MODIFIED", w.getName());
                }
            }
        }
        plugin.getServer().dispatchCommand(plugin.getConsole(), "mv reload");
    }

    /**
     * Remove the build flag if present in a TARDIS region.
     *
     * @param p the player to message if the command is run in-game
     */
    private void removeBuildFlags(Player p) {
        for (World w : plugin.getServer().getWorlds()) {
            if (w.getName().contains("TARDIS")) {
                for (String s : plugin.getWorldGuardUtils().getRegions(w)) {
                    plugin.getServer().dispatchCommand(plugin.getConsole(), "rg flag " + s + " build -w " + w.getName());
                    if (p != null) {
                        TARDISMessage.send(p, "WG_REMOVED", w.getName());
                    }
                }
            }
        }
        plugin.getServer().dispatchCommand(plugin.getConsole(), "wg reload");
    }
}
