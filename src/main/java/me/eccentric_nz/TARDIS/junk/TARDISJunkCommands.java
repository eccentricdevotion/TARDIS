/*
 * Copyright (C) 2015 eccentric_nz
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
package me.eccentric_nz.TARDIS.junk;

import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJunkCommands implements CommandExecutor {

    private final TARDIS plugin;
    private final List<String> firstArgs = new ArrayList<String>();

    public TARDISJunkCommands(TARDIS plugin) {
        this.plugin = plugin;
        this.firstArgs.add("create");
        this.firstArgs.add("delete");
        this.firstArgs.add("find");
        this.firstArgs.add("return");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardisjunk then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisjunk")) {
            if (args.length == 0) {
                // find Junk TARDIS
                return new TARDISJunkFind(plugin).find(sender);
            }
            String first = args[0].toLowerCase();
            if (args.length == 1 && firstArgs.contains(first)) {
                if (first.equals("create")) {
                    Player p = null;
                    if (sender instanceof Player) {
                        p = (Player) sender;
                    }
                    if (p == null) {
                        TARDISMessage.send(sender, "CMD_PLAYER");
                        return false;
                    }
                    return new TARDISJunkCreator(plugin, p).createJunkTARDIS();
                }
                if (first.equals("find")) {
                    return new TARDISJunkFind(plugin).find(sender);
                }
                if (first.equals("return")) {
                    return new TARDISJunkReturn(plugin).recall(sender);
                }
                if (first.equals("delete")) {
                    return new TARDISJunkDelete(plugin).delete(sender);
                }
                return true;
            }
        }
        return false;
    }
}
