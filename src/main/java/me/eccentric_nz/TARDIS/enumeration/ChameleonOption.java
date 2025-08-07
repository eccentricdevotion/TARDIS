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
package me.eccentric_nz.TARDIS.enumeration;

import net.kyori.adventure.text.format.NamedTextColor;

/**
 * @author eccentric_nz
 */
public enum ChameleonOption {

    FACTORY(20, "DISABLED", NamedTextColor.RED, "SET_ON", NamedTextColor.GREEN),
    ADAPTIVE(21, "SET_ON", NamedTextColor.GREEN, "SET_OFF", NamedTextColor.RED),
    INVISIBLE(22, "SET_ON", NamedTextColor.GREEN, "SET_OFF", NamedTextColor.RED),
    PRESET(23, "SET_ON", NamedTextColor.GREEN, "SET_OFF", NamedTextColor.RED),
    CONSTRUCT(24, "SET_ON", NamedTextColor.GREEN, "SET_OFF", NamedTextColor.RED);

    private final int slot;
    private final String on;
    private final NamedTextColor onColour;
    private final String off;
    private final NamedTextColor offColour;

    ChameleonOption(int slot, String on, NamedTextColor onColour, String off, NamedTextColor offColour) {
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

    public NamedTextColor getOnColour() {
        return onColour;
    }

    public String getOff() {
        return off;
    }

    public NamedTextColor getOffColour() {
        return offColour;
    }
}
