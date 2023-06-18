/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.update;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.Updateable;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TARDISUpdateBlocks {

    public static void showOptions(Player player, Updateable updateable) {
        TARDIS.plugin.getMessenger().sendWithColours(player, TardisModule.TARDIS, "'" + updateable.getName() + "'", "#55FFFF", " valid blocks:", "#FFFFFF");
        for (Material m : updateable.getMaterialChoice().getChoices()) {
            String s = m.toString();
            if (s.equals("SPAWNER")) {
                TARDIS.plugin.getMessenger().message(player, "   ANY BLOCK");
            } else {
                TARDIS.plugin.getMessenger().message(player, "  - " + TARDISStringUtils.capitalise(s));
            }
        }
    }
}
