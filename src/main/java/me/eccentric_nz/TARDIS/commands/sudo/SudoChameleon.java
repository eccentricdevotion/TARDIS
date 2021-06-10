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
package me.eccentric_nz.tardis.commands.sudo;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.commands.remote.TARDISRemoteRebuildCommand;
import me.eccentric_nz.tardis.enumeration.PRESET;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 * A chameleon conversion is a repair procedure that technicians perform on TARDIS chameleon circuits. The Fourth Doctor
 * once said that the reason the TARDIS' chameleon circuit was stuck was because he had "borrowed" it from Gallifrey
 * before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
public class SudoChameleon {

    private final TARDISPlugin plugin;

    SudoChameleon(TARDISPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean setPreset(CommandSender sender, int id, String[] args, OfflinePlayer offlinePlayer) {
        try {
            PRESET preset = PRESET.valueOf(args[2].toUpperCase());
            if (preset.getSlot() == -1) {
                TARDISMessage.send(sender, "CHAM_NOT_VALID", preset.toString());
            } else {
                HashMap<String, Object> where = new HashMap<>();
                where.put("tardis_id", id);
                HashMap<String, Object> set = new HashMap<>();
                set.put("chameleon_preset", preset.toString());
                plugin.getQueryFactory().doUpdate("tardis", set, where);
                TARDISMessage.send(sender, "CHAM_SET", preset.toString());
                // perform rebuild
                return new TARDISRemoteRebuildCommand(plugin).doRemoteRebuild(sender, id, offlinePlayer, true);
            }
        } catch (IllegalArgumentException e) {
            TARDISMessage.send(sender, "ABANDONED_PRESET");
        }
        return true;
    }
}
