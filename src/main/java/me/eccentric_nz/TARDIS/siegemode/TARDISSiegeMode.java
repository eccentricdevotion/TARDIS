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
package me.eccentric_nz.TARDIS.siegemode;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;

/**
 * Siege mode is a feature of the TARDIS that can be activated using a lever
 * under the console to prevent entry or exit. Additionally, it makes the TARDIS
 * impervious to all external damage. Siege mode requires power to activate or
 * deactivate.
 *
 * @author eccentric_nz
 */
public class TARDISSiegeMode {

    private final TARDIS plugin;

    public TARDISSiegeMode(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void toggle(int id, Player p) {

    }
}
