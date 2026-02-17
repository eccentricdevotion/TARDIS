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
package me.eccentric_nz.TARDIS.commands.config;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Config;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.TARDISWorlds;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class ReloadCommand {

    private final TARDIS plugin;

    public ReloadCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean reloadConfig(CommandSender sender) {
        plugin.reloadConfig();
        // check worlds
        TARDISWorlds tc = new TARDISWorlds(plugin);
        tc.doWorlds();
        plugin.saveConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "RELOADED");
        return true;
    }

    public boolean reloadOtherConfig(CommandSender sender, String module) {
        try {
            Config config = Config.valueOf(module.toLowerCase(Locale.ROOT));
            File file = new File(plugin.getDataFolder(), config + ".yml");
            switch (config) {
                case achievements -> plugin.getAchievementConfig().load(file);
                case adaptive -> plugin.getAdaptiveConfig().load(file);
                case artron -> plugin.getArtronConfig().load(file);
                case blaster -> plugin.getBlasterConfig().load(file);
                case blocks -> plugin.getBlocksConfig().load(file);
                case chameleon_guis -> plugin.getChameleonGuis().load(new File(plugin.getDataFolder(), "language" + File.separator + "chameleon_guis.yml"));
                case condensables -> plugin.getCondensablesConfig().load(file);
                case generator -> plugin.getGeneratorConfig().load(file);
                case handles -> plugin.getHandlesConfig().load(file);
                case items -> plugin.getItemsConfig().load(file);
                case kits -> plugin.getKitsConfig().load(file);
                case monsters -> plugin.getMonstersConfig().load(file);
                case rooms -> plugin.getRoomsConfig().load(file);
                case shop -> plugin.getShopConfig().load(file);
                case signs -> plugin.getSigns().load(new File(plugin.getDataFolder(), "language" + File.separator + "signs.yml"));
                case vortex_manipulator -> plugin.getVortexConfig().load(file);
                default -> {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "RELOAD_NOT_THESE", module);
                    return true;
                }
            }
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "RELOAD_SUCCESS", config.toString());
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "RELOAD_FILE_BAD", module);
            return true;
        } catch (InvalidConfigurationException | IOException e) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "RELOAD_FAIL", module);
            return true;
        }
        return true;
    }
}
