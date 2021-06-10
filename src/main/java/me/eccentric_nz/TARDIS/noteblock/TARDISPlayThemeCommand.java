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
package me.eccentric_nz.tardis.noteblock;

import me.eccentric_nz.tardis.TARDISPlugin;
import org.bukkit.entity.Player;

public class TARDISPlayThemeCommand {

    private final TARDISPlugin plugin;

    public TARDISPlayThemeCommand(TARDISPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean playTheme(Player p) {
        if (plugin.getTrackerKeeper().getEggs().contains(p.getUniqueId())) {
            return true;
        }
        plugin.getTrackerKeeper().getEggs().add(p.getUniqueId());
        Song s = NBSDecoder.parse(plugin.getResource("theme.nbs"));
        SongPlayer sp = new SongPlayer(s);
        sp.addPlayer(p);
        sp.setPlaying(true);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTrackerKeeper().getEggs().remove(p.getUniqueId()), 2200L);
        return true;
    }
}
