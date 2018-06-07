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
package me.eccentric_nz.TARDIS.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Program;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Programming is a process used by Cybermen to control humans. To program a human, the person has to be dead. A control
 * is installed in the person, powered by electricity, turning the person into an agent of the Cybermen. Control over
 * programmed humans can be shorted out by another signal, but that kills whatever might be left of the person.
 *
 * @author eccentric_nz
 */
public class TARDISHandlesProcessor {

    private final TARDIS plugin;
    private final Program program;
    private final Player player;

    public TARDISHandlesProcessor(TARDIS plugin, Program program, Player player) {
        this.plugin = plugin;
        this.program = program;
        this.player = player;
    }

    public void processDisk() {
        int i = 0;
        for (ItemStack is : program.getInventory()) {
            if (is != null) {
                TARDISHandlesBlock thb = TARDISHandlesBlock.BY_NAME.get(is.getItemMeta().getDisplayName());
                switch (thb) {
                    case FOR:
                        break;
                    case IF:
                        break;
                    case VARIABLE:
                        break;
                    case X:
                    case Y:
                    case Z:
                        break;
                }
            }
            i++;
        }
    }
}
