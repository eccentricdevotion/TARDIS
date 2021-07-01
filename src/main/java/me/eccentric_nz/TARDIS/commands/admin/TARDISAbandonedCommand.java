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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISAbandoned;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TARDISAbandonedCommand {

    private final TARDIS plugin;

    TARDISAbandonedCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean spawn(CommandSender sender, String[] args) {
        if (!plugin.getConfig().getBoolean("abandon.enabled")) {
            TARDISMessage.send(sender, "ABANDONED_DISABLED");
            return true;
        }
        if (!plugin.getConfig().getBoolean("creation.default_world")) {
            TARDISMessage.send(sender, "ABANDONED_SPAWN");
            return true;
        }
        // tardisadmin spawn_abandoned Schematic PRESET COMPASS world x y z
        if (args.length < 4) {
            TARDISMessage.send(sender, "TOO_FEW_ARGS");
            TARDISMessage.send(sender, "ABANDONED_ARGS");
            return true;
        }
        String schm = args[1].toUpperCase(Locale.ENGLISH);
        if (!Consoles.getBY_NAMES().containsKey(schm)) {
            TARDISMessage.send(sender, "TOO_FEW_ARGS");
            return true;
        }
        Schematic s = Consoles.getBY_NAMES().get(schm);
        PRESET preset;
        try {
            preset = PRESET.valueOf(args[2].toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            TARDISMessage.send(sender, "ABANDONED_PRESET");
            return true;
        }
        COMPASS d;
        try {
            d = COMPASS.valueOf(args[3].toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            TARDISMessage.send(sender, "ABANDONED_COMPASS");
            return true;
        }
        Location l;
        if (sender instanceof Player) {
            Player p = (Player) sender;
            l = p.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16).getRelative(BlockFace.UP).getLocation();
        } else {
            if (args.length < 8) {
                TARDISMessage.send(sender, "TOO_FEW_ARGS");
                TARDISMessage.send(sender, "ABANDONED_ARGS");

                return true;
            }
            World w = TARDISAliasResolver.getWorldFromAlias(args[4]);
            if (w == null) {
                TARDISMessage.send(sender, "WORLD_NOT_FOUND");
                return true;
            }
            int x = TARDISNumberParsers.parseInt(args[5]);
            int y = TARDISNumberParsers.parseInt(args[6]);
            int z = TARDISNumberParsers.parseInt(args[7]);
            if (x == 0 || y == 0 || z == 0) {
                TARDISMessage.send(sender, "WORLD_NOT_FOUND");
                return true;
            }
            l = new Location(w, x, y, z);
        }
        new TARDISAbandoned(plugin).spawn(l, s, preset, d, (sender instanceof Player) ? (Player) sender : null);
        return true;
    }
}
