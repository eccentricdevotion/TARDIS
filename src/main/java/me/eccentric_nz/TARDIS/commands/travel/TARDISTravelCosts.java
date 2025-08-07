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
package me.eccentric_nz.TARDIS.commands.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.List;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTravelCosts {

    private final TARDIS plugin;
    private final List<String> costs = List.of("random", "random_circuit", "travel", "comehere", "hide", "rebuild", "autonomous", "backdoor");

    public TARDISTravelCosts(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean action(Player player) {
        plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_COSTS");
        for (String s : costs) {
            String c = (s.equals("rebuild")) ? plugin.getArtronConfig().getString("random") : plugin.getArtronConfig().getString(s);
            plugin.getMessenger().sendWithColours(player, "    " + s + ": ", "#FFFFFF",  c, "#55FFFF");
        }
        return true;
    }
}
