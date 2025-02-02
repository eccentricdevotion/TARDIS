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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISAbandoned;
import me.eccentric_nz.TARDIS.enumeration.*;
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
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ABANDONED_DISABLED");
            return true;
        }
        if (!plugin.getConfig().getBoolean("creation.default_world")) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ABANDONED_SPAWN");
            return true;
        }
        // tardisadmin spawn_abandoned Schematic PRESET COMPASS world x y z
        if (args.length < 4) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ABANDONED_ARGS");
            return true;
        }
        String schm = args[1].toUpperCase(Locale.ROOT);
        if (!Consoles.getBY_NAMES().containsKey(schm)) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
            return true;
        }
        Schematic s = Consoles.getBY_NAMES().get(schm);
        ChameleonPreset preset;
        if (plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false).contains(args[2])) {
            preset = ChameleonPreset.ITEM;
        } else {
            try {
                preset = ChameleonPreset.valueOf(args[2].toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "ABANDONED_PRESET");
                return true;
            }
        }
        COMPASS d;
        try {
            d = COMPASS.valueOf(args[3].toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ABANDONED_COMPASS");
            return true;
        }
        Location l;
        if (sender instanceof Player p) {
            l = p.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16).getRelative(BlockFace.UP).getLocation();
        } else {
            if (args.length < 8) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "ABANDONED_ARGS");
                return true;
            }
            World w = TARDISAliasResolver.getWorldFromAlias(args[4]);
            if (w == null) {
                plugin.getMessenger().sendColouredCommand(sender, "WORLD_NOT_FOUND", "/tardisworld", plugin);
                return true;
            }
            int x = TARDISNumberParsers.parseInt(args[5]);
            int y = TARDISNumberParsers.parseInt(args[6]);
            int z = TARDISNumberParsers.parseInt(args[7]);
            if (x == 0 || y == 0 || z == 0) {
                plugin.getMessenger().sendColouredCommand(sender, "WORLD_NOT_FOUND", "/tardisworld", plugin);
                return true;
            }
            l = new Location(w, x, y, z);
        }
        new TARDISAbandoned(plugin).spawn(l, s, preset, args[2], d, (sender instanceof Player) ? (Player) sender : null);
        return true;
    }
}
