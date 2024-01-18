/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.enumeration;

import net.md_5.bungee.api.ChatColor;

/**
 * @author eccentric_nz
 */
public enum ChameleonOption {

    FACTORY(20, "DISABLED", ChatColor.RED, "SET_ON", ChatColor.GREEN),
    ADAPTIVE(21, "SET_ON", ChatColor.GREEN, "SET_OFF", ChatColor.RED),
    INVISIBLE(22, "SET_ON", ChatColor.GREEN, "SET_OFF", ChatColor.RED),
    PRESET(23, "SET_ON", ChatColor.GREEN, "SET_OFF", ChatColor.RED),
    CONSTRUCT(24, "SET_ON", ChatColor.GREEN, "SET_OFF", ChatColor.RED);

    private final int slot;
    private final String on;
    private final ChatColor onColour;
    private final String off;
    private final ChatColor offColour;

    ChameleonOption(int slot, String on, ChatColor onColour, String off, ChatColor offColour) {
        this.slot = slot;
        this.on = on;
        this.off = off;
        this.onColour = onColour;
        this.offColour = offColour;
    }

    public int getSlot() {
        return slot;
    }

    public String getOn() {
        return on;
    }

    public ChatColor getOnColour() {
        return onColour;
    }

    public String getOff() {
        return off;
    }

    public ChatColor getOffColour() {
        return offColour;
    }
}
