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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSetBooleanCommand {

    private final TARDIS plugin;
    private final List<String> require_restart = Arrays.asList("use_block_stack", "use_worldguard", "wg_flag_set", "walk_in_tardis", "zero_room", "particles");

    public TARDISSetBooleanCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setConfigBool(CommandSender sender, String[] args, String section) {
        String tolower = args[0].toLowerCase();
        String first = (section.isEmpty()) ? tolower : section + "." + tolower;
        // check they typed true of false
        String tf = args[1].toLowerCase(Locale.ENGLISH);
        if (!tf.equals("true") && !tf.equals("false")) {
            TARDISMessage.send(sender, "TRUE_FALSE");
            return false;
        }
        if (first.equals("artron_furnace.particles")) {
            plugin.getArtronConfig().set(first, Boolean.valueOf(tf));
            try {
                plugin.getArtronConfig().save(new File(plugin.getDataFolder(), "artron.yml"));
            } catch (IOException ex) {
                plugin.debug("Could not save artron.yml, " + ex.getMessage());
            }
        } else {
            plugin.getConfig().set(first, Boolean.valueOf(tf));
            plugin.saveConfig();
        }
        TARDISMessage.send(sender, "CONFIG_UPDATED");
        if (require_restart.contains(tolower)) {
            TARDISMessage.send(sender, "RESTART");
        }
        return true;
    }
}
