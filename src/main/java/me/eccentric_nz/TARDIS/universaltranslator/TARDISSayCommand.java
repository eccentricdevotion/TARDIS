/*
 * Copyright (C) 2013 eccentric_nz
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

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
    private final String UT = ChatColor.GOLD + "[TARDIS Universal Translator]" + ChatColor.RESET;

    public TARDISSayCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardissay")) {
            if (!sender.hasPermission("tardis.translate")) {
                sender.sendMessage(plugin.getPluginName() + MESSAGE.NO_PERMS.getText());
                return false;
            }
            if (args.length < 2) {
                sender.sendMessage(plugin.getPluginName() + MESSAGE.TOO_FEW_ARGS.getText());
                return false;
            }
            String preferedLang = "ENGLISH";
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("player", sender.getName());
            ResultSetPlayerPrefs rs = new ResultSetPlayerPrefs(plugin, where);
            if (rs.resultSet() && !rs.getLanguage().isEmpty()) {
                preferedLang = rs.getLanguage();
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                sb.append(" ").append(args[i]);
            }
            String whatToTranslate = sb.substring(1).toString();
            String lang = args[0].toUpperCase();
            try {
                Language to = Language.valueOf(lang);
                Language from = Language.valueOf(preferedLang);
                Translate.setClientId("TARDISforBukkit");
                Translate.setClientSecret("+ziAoNOXlyGLTwLdhxi5bHrCuc6/0MUidZ4sz55xANE=");
                try {
                    String translatedText = Translate.execute(whatToTranslate, from, to);
                    if (sender instanceof Player) {
                        ((Player) sender).chat(UT + " " + translatedText);
                    } else {
                        plugin.getServer().dispatchCommand(sender, "say " + UT + " " + translatedText);
                    }
                    return true;
                } catch (Exception ex) {
                    plugin.debug("Could not get translation! " + ex);
                }
            } catch (IllegalArgumentException e) {
                sender.sendMessage(UT + "Invalid language specified, try using tab key completion!");
            }
        }
        return false;
    }
}
