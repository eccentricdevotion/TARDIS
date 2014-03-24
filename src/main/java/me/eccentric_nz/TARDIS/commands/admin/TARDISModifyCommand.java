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
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
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
            sender.sendMessage(plugin.getPluginName() + MESSAGE.TOO_FEW_ARGS.getText());
            return true;
        }
        if (!args[1].toLowerCase().equals("mv") && !args[1].toLowerCase().equals("wg")) {
            sender.sendMessage(plugin.getPluginName() + "The second argument must be 'mv' or 'wg'!");
            return true;
        }
        if (args[1].toLowerCase().equals("mv") && !plugin.getPM().isPluginEnabled("Multiverse-Core")) {
            sender.sendMessage(plugin.getPluginName() + "Multiverse-Core is not enabled!");
            return true;
        }
        if (args[1].toLowerCase().equals("wg") && !plugin.isWorldGuardOnServer()) {
            sender.sendMessage(plugin.getPluginName() + "WorldGuard is not enabled!");
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
                    p.sendMessage(plugin.getPluginName() + "Multiverse settings for " + w.getName() + " modified!");
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
                        p.sendMessage(plugin.getPluginName() + "WorldGuard build flag removed for " + w.getName());
                    }
                }
            }
        }
        plugin.getServer().dispatchCommand(plugin.getConsole(), "wg reload");
    }
}
