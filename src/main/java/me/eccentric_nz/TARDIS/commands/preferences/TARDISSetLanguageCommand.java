/*
 * Copyright (C) 2022 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.preferences;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.universaltranslator.Language;
import me.eccentric_nz.TARDIS.universaltranslator.TranslateData;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISSetLanguageCommand {

    private final TARDIS plugin;

    public TARDISSetLanguageCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean setLanguagePref(Player player, String[] args) {
        String pref = args[0];
        if (args.length < 2) {
            TARDISMessage.send(player, "PREF_NEED", pref);
            return false;
        }
        UUID uuid = player.getUniqueId();
        String to = args[1].toUpperCase(Locale.ENGLISH);
        if (args[0].equalsIgnoreCase("translate") && args[1].equalsIgnoreCase("off")) {
            plugin.getTrackerKeeper().getTranslators().remove(uuid);
            TARDISMessage.send(player, "TRANSLATE_OFF");
        } else {
            Language langTo;
            try {
                langTo = Language.valueOf(to);
            } catch (IllegalArgumentException e) {
                TARDISMessage.send(player, "LANG_NOT_VALID");
                return true;
            }
            if (args[0].equalsIgnoreCase("translate")) {
                if (args.length < 3) {
                    TARDISMessage.send(player, "PREF_NEED", "language to translate from");
                    return false;
                }
                String from = args[2].toUpperCase(Locale.ENGLISH);
                Language langFrom;
                try {
                    langFrom = Language.valueOf(from);
                } catch (IllegalArgumentException e) {
                    TARDISMessage.send(player, "LANG_NOT_VALID");
                    return true;
                }
                if (args.length < 4) {
                    TARDISMessage.send(player, "PREF_NEED", "player name");
                    return false;
                }
                OfflinePlayer sender = plugin.getServer().getOfflinePlayer(args[3]);
                if (sender == null) {
                    TARDISMessage.send(player, "PLAYER_NOT_VALID");
                    return true;
                }
                String name = sender.getName();
                TranslateData data = new TranslateData(langTo, langFrom, name);
                plugin.getTrackerKeeper().getTranslators().put(uuid, data);
                TARDISMessage.send(player, "TRANSLATE_ON", name);
            } else {
                HashMap<String, Object> setl = new HashMap<>();
                setl.put(pref, to);
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid.toString());
                plugin.getQueryFactory().doUpdate("player_prefs", setl, where);
                TARDISMessage.send(player, "PREF_SET", TARDISStringUtils.uppercaseFirst(pref));
            }
        }
        return true;
    }
}
