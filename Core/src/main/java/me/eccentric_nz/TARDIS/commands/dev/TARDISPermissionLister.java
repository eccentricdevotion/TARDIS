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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TARDISPermissionLister {

    private final TARDIS plugin;

    public TARDISPermissionLister(TARDIS plugin) {
        this.plugin = plugin;
    }

    void listPerms(CommandSender sender) {
        List<String> perms = new ArrayList<>(plugin.getGeneralKeeper().getPluginYAML().getConfigurationSection("permissions").getKeys(true));
        perms.sort(Comparator.naturalOrder());
        String lastPerm = "";
        for (int i = perms.size() - 1; i >= 0; i--) {
            String perm = perms.get(i);
            if (perm.contains(".") && notThese(perm)) {
                if (!lastPerm.contains(perm)) {
                    sender.sendMessage(perm);
                    lastPerm = perm;
                }
            }
        }
    }
    
    void listPermsHtml(CommandSender sender) {
        List<String> lines = new ArrayList<>();
        List<String> perms = new ArrayList<>(plugin.getGeneralKeeper().getPluginYAML().getConfigurationSection("permissions").getKeys(true));
        String def = "op";
        String desc = "";
        int count = 0;
        for (int i = perms.size() - 1; i >= 0; i--) {
            String perm = perms.get(i);
            if (perm.contains(".")) {
                if (perm.contains("default")) {
                    def = plugin.getGeneralKeeper().getPluginYAML().getString("permissions." + perm);
                } else if (perm.contains("description")) {
                    desc = plugin.getGeneralKeeper().getPluginYAML().getString("permissions." + perm);
                } else {
                    if (perm.contains("children")) {
                        def = plugin.getGeneralKeeper().getPluginYAML().getString("permissions." + perm);
                        desc = "";
                    }
                    if (!def.contains("MemorySection")) {
                        String trimmed = perm;
                        if (perm.contains("children")) {
                            trimmed = perm.substring(perm.indexOf("children.") + 9);
                            count++;
                        }
                        lines.add("<tr" + ((perm.contains("children") ? " class=\"child\"" : "")) + "><td" + ((perm.contains("children") ? " colspan=\"2\"" : "")) + "><code>" + trimmed + "</code></td>" + ((desc.isEmpty()) ? "" : "<td" + ((!perm.contains("children") ? " colspan=\"2\"" : "")) + ">" + desc + "</td>") + "<td>"+ def + "</td></tr>");
                    } else if (perm.endsWith("children")) {
                        String trimmed = perm.substring(0, perm.length() - 9);
                        String path = "permissions." + trimmed + ".description";
                        String dd = plugin.getGeneralKeeper().getPluginYAML().getString(path);
                        lines.add("<tr class=\"child\"><td rowspan=\"" + count + "\"> &nbsp; &mdash; children of <code>" + trimmed + "</code></td>");
                        lines.add("<tr><td><code>" + trimmed + "</code></td><td colspan=\"2\">" + dd + "</td><td>op</td></tr>");
                        count = 0;
                    }
                }
            }
        }
        Collections.reverse(lines);
        for (String l : lines) {
            sender.sendMessage(l);
        }
    }

    private boolean notThese(String perm) {
        return !perm.contains("children") && !perm.contains("description") && !perm.contains("default") && !perm.contains("*");
    }
}
