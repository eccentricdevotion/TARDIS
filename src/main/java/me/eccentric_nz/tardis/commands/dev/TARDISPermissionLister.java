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
package me.eccentric_nz.tardis.commands.dev;

import me.eccentric_nz.tardis.TardisPlugin;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class TardisPermissionLister {

    private final TardisPlugin plugin;

    public TardisPermissionLister(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    void listPerms(CommandSender sender) {
        List<String> perms = new ArrayList<>(Objects.requireNonNull(plugin.getGeneralKeeper().getPluginYAML().getConfigurationSection("permissions")).getKeys(true));
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

    private boolean notThese(String perm) {
        return !perm.contains("children") && !perm.contains("description") && !perm.contains("default") && !perm.contains("*");
    }
}
