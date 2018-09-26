/*
 * Copyright (C) 2018 eccentric_nz
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

import com.rmtheis.yandtran.translate.Language;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

import static me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsCommands.ucfirst;

/**
 * @author eccentric_nz
 */
class TARDISSetLanguageCommand {

    boolean setLanguagePref(Player player, String[] args, QueryFactory qf) {
        String pref = args[0];
        if (args.length < 2) {
            TARDISMessage.send(player, "PREF_NEED", pref);
            return false;
        }
        String l = args[1].toUpperCase(Locale.ENGLISH);
        Language lang;
        try {
            lang = Language.valueOf(l);
        } catch (IllegalArgumentException e) {
            TARDISMessage.send(player, "LANG_NOT_VALID");
            return true;
        }
        HashMap<String, Object> setl = new HashMap<>();
        setl.put(pref, l);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        qf.doUpdate("player_prefs", setl, where);
        TARDISMessage.send(player, "PREF_SET", ucfirst(pref));
        return true;
    }
}
