/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.interior.AbandonedBuilder;
import me.eccentric_nz.TARDIS.discovery.StructureVariant;
import me.eccentric_nz.TARDIS.enumeration.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class CreateAbandonedCommand {

    private final TARDIS plugin;

    public CreateAbandonedCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void spawn(CommandSender sender, String s, String p, String dir, World world, int x, int y, int z) {
        if (!plugin.getConfig().getBoolean("abandon.enabled")) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ABANDONED_DISABLED");
            return;
        }
        if (!plugin.getConfig().getBoolean("creation.default_world")) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ABANDONED_SPAWN");
            return;
        }
        if (!Desktops.getBY_NAMES().containsKey(s)) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
            return;
        }
        Schematic schematic = Desktops.getBY_NAMES().get(s);
        ChameleonPreset preset;
        if (plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false).contains(p)) {
            preset = ChameleonPreset.ITEM;
        } else {
            try {
                preset = ChameleonPreset.valueOf(p.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "ABANDONED_PRESET");
                return;
            }
        }
        COMPASS d;
        try {
            d = COMPASS.valueOf(dir.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ABANDONED_COMPASS");
            return;
        }
        Location l = new Location(world, x, y, z);
        // get fade variant
        StructureVariant variant = null;
        if (schematic.getPermission().equals("fade")) {
            StructureVariant[] structureVariants = TARDIS.plugin.getBuildKeeper().getFadeVariants().getLookup().values().toArray(new StructureVariant[0]);
            variant = structureVariants[TARDISConstants.RANDOM.nextInt(structureVariants.length)];
        }
        new AbandonedBuilder(plugin).spawn(l, schematic, variant, preset, s, d, (sender instanceof Player) ? (Player) sender : null);
    }
}
