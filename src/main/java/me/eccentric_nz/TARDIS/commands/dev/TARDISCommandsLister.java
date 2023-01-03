/*
 * Copyright (C) 2022 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisCommand;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISCommandsLister {

    private final TARDIS plugin;

    public TARDISCommandsLister(TARDIS plugin) {
        this.plugin = plugin;
    }

    /*
    commands:
      tardis:
        aliases: tt
        description: Manipulate TARDISs (TARDISes?) with various arguments (list, save, find, help, etc).
        bell:
          description: Toggle the TARDIS Cloister Bell on and off.
          usage: /<command> bell [on|off]
          permission: tardis.use
          permission-message: You don't have the permission <permission> to use this command
     */
    public void listTARDISCommands(CommandSender sender) {
        sender.sendMessage("<table>");
        sender.sendMessage("<tr><th>Sub command</th><th>Description</th><th>Usage</th><th>Permission</th></tr>");
        for (TardisCommand tc : TardisCommand.values()) {
            if (plugin.getGeneralKeeper().getPluginYAML().contains("commands.tardis." + tc)) {
                String perm = plugin.getGeneralKeeper().getPluginYAML().getString("commands.tardis." + tc + ".permission");
                sender.sendMessage("<tr><td id=\"" + tc + "\"><code>" + tc
                        + "</code></td><td>" + plugin.getGeneralKeeper().getPluginYAML().getString("commands.tardis." + tc + ".description")
                        + "</td><td class=\"usage\"><code>" + plugin.getGeneralKeeper().getPluginYAML().getString("commands.tardis." + tc + ".usage").replace("/<command>", "/tardis").replace("<", "&gt;").replace(">", "&lt;")
                        + "</code></td><td>" + (perm == null ? "none" : perm)
                        + "</td></tr>");
            } else if (tc == TardisCommand.colorize) {
                sender.sendMessage("<tr><td id=\"" + tc + "\"><code>" + tc + "</code></td><td colspan=\"3\">Alias to the <a href=\"#colourise\"><code>colourise</code></a> sub command.</td></tr>");
            } else if (tc == TardisCommand.theme || tc == TardisCommand.upgrade) {
                sender.sendMessage("<tr><td id=\"" + tc + "\"><code>" + tc + "</code></td><td colspan=\"3\">Alias to the <a href=\"#desktop\"><code>desktop</code></a> sub command.</td></tr>");
            } else {
                sender.sendMessage("<tr><td colspan=\"4\">********</td></tr>");
                sender.sendMessage("<tr><td colspan=\"4\">plugin.yml does not contain an entry for " + tc + "!</td></tr>");
                sender.sendMessage("<tr><td colspan=\"4\">********</td></tr>");
            }
        }
        sender.sendMessage("</table>");
    }
}
