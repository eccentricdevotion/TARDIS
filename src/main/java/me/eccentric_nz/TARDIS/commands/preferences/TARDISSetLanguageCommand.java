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
package me.eccentric_nz.TARDIS.commands.preferences;

import com.memetix.mst.Language;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import static me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsCommands.ucfirst;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSetLanguageCommand {

    private final TARDIS plugin;

    public TARDISSetLanguageCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setLanguagePref(Player player, String[] args, QueryFactory qf) {
        String pref = args[0];
        if (args.length < 2) {
            TARDISMessage.send(player, plugin.getPluginName() + "You need to specify a " + pref);
            return false;
        }
        Language lang;
        try {
            lang = Language.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            TARDISMessage.send(player, plugin.getPluginName() + "Invalid language specified, try using tab key completion!");
            return true;
        }
        HashMap<String, Object> setl = new HashMap<String, Object>();
        setl.put(pref, lang.toString());
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("player", player.getName());
        qf.doUpdate("player_prefs", setl, where);
        TARDISMessage.send(player, plugin.getPluginName() + ucfirst(pref) + " saved.");
        return true;
    }
}
