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
package me.eccentric_nz.TARDIS.universaltranslator;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Locale;

/**
 * A Time Control Unit is a golden sphere about the size of a Cricket ball. It
 * is stored in the Secondary Control Room. All TARDISes have one of these
 * devices, which can be used to remotely control a TARDIS by broadcasting
 * Stattenheim signals that travel along the time contours in the Space/Time
 * Vortex.
 *
 * @author eccentric_nz
 */
public class TARDISSayCommand implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISSayCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardissay")) {
            if (!TARDISPermission.hasPermission(sender, "tardis.translate")) {
                plugin.getMessenger().send(sender, TardisModule.TRANSLATOR, "NO_PERMS");
                return false;
            }
            if (args.length < 2) {
                plugin.getMessenger().send(sender, TardisModule.TRANSLATOR, "TOO_FEW_ARGS");
                return false;
            }
            String preferedLang = "ENGLISH";
            if (sender instanceof Player player) {
                ResultSetPlayerPrefs rs = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
                if (rs.resultSet() && !rs.getLanguage().isEmpty()) {
                    if (!rs.getLanguage().equalsIgnoreCase("AUTO_DETECT")) {
                        preferedLang = rs.getLanguage();
                    }
                }
            }
            String whatToTranslate = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            String lang = args[0].toUpperCase(Locale.ROOT);
            try {
                Language from = Language.valueOf(preferedLang);
                Language to = Language.valueOf(lang);
                try {
                    LingvaTranslate translate = new LingvaTranslate(plugin, from.getCode(), to.getCode(), whatToTranslate);
                    translate.fetchAsync((hasResult, translated) -> {
                        if (hasResult) {
                            plugin.getServer().dispatchCommand(sender, "say [" + TardisModule.TRANSLATOR.getName() + "] " + translated.getTranslated());
                        }
                    });
                    return true;
                } catch (CommandException ex) {
                    plugin.debug("Could not get translation! " + ex.getMessage());
                    plugin.getMessenger().send(sender, TardisModule.TRANSLATOR, "YT_UNAVAILABLE");
                }
            } catch (IllegalArgumentException e) {
                plugin.getMessenger().send(sender, TardisModule.TRANSLATOR, "LANG_NOT_VALID");
            }
        }
        return false;
    }
}
