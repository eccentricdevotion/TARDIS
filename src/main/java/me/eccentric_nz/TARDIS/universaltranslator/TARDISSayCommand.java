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
package me.eccentric_nz.tardis.universaltranslator;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.blueprints.TARDISPermission;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * A Time Control Unit is a golden sphere about the size of a Cricket ball. It is stored in the Secondary Control Room.
 * All TARDISes have one of these devices, which can be used to remotely control a tardis by broadcasting Stattenheim
 * signals that travel along the time contours in the Space/Time Vortex.
 *
 * @author eccentric_nz
 */
public class TARDISSayCommand implements CommandExecutor {

    private final TARDISPlugin plugin;
    private final String UT = ChatColor.GOLD + "[tardis Universal Translator]" + ChatColor.RESET + " ";

    public TARDISSayCommand(TARDISPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardissay")) {
            if (!TARDISPermission.hasPermission(sender, "tardis.translate")) {
                TARDISMessage.send(sender, "NO_PERMS");
                return false;
            }
            if (args.length < 2) {
                TARDISMessage.send(sender, "TOO_FEW_ARGS");
                return false;
            }
            String preferedLang = "ENGLISH";
            if (sender instanceof Player) {
                ResultSetPlayerPrefs rs = new ResultSetPlayerPrefs(plugin, ((Player) sender).getUniqueId().toString());
                if (rs.resultSet() && !rs.getLanguage().isEmpty()) {
                    if (!rs.getLanguage().equalsIgnoreCase("AUTO_DETECT")) {
                        preferedLang = rs.getLanguage();
                    }
                }
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                sb.append(" ").append(args[i]);
            }
            String whatToTranslate = sb.substring(1);
            String lang = args[0].toUpperCase(Locale.ENGLISH);
            try {
                Language to = Language.valueOf(lang);
                Language from = Language.valueOf(preferedLang);
                Translate.setKey("trnsl.1.1.20170312T202552Z.b0bd3c7ce48fe120.8d084aec9ae76b8d17b7882cd3026202c61ee7e0");
                try {
                    String translatedText = Translate.execute(whatToTranslate, from, to);
                    if (sender instanceof Player) {
                        ((Player) sender).chat(UT + translatedText);
                    } else {
                        plugin.getServer().dispatchCommand(sender, "say " + UT + translatedText);
                    }
                    return true;
                } catch (Exception ex) {
                    plugin.debug("Could not get translation! " + ex);
                    ex.printStackTrace();
                    TARDISMessage.send(sender, "YT_UNAVAILABLE");
                }
            } catch (IllegalArgumentException e) {
                TARDISMessage.send(sender, "LANG_NOT_VALID");
            }
        }
        return false;
    }
}
