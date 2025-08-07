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

import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import org.bukkit.Material;

public enum TardisLight {

    BULB(TARDISDisplayItem.LIGHT_BULB_ON, TARDISDisplayItem.LIGHT_BULB, TARDISDisplayItem.LIGHT_BULB_CLOISTER),
    CLASSIC(TARDISDisplayItem.LIGHT_CLASSIC_ON, TARDISDisplayItem.LIGHT_CLASSIC, TARDISDisplayItem.LIGHT_CLASSIC_CLOISTER),
    CLASSIC_OFFSET(TARDISDisplayItem.LIGHT_CLASSIC_OFFSET_ON, TARDISDisplayItem.LIGHT_CLASSIC_OFFSET, TARDISDisplayItem.LIGHT_CLASSIC_OFFSET_CLOISTER),
    TENTH(Material.REDSTONE_LAMP, TARDISDisplayItem.LIGHT_TENTH_ON, TARDISDisplayItem.LIGHT_TENTH, TARDISDisplayItem.LIGHT_TENTH_CLOISTER),
    ELEVENTH(TARDISDisplayItem.LIGHT_ELEVENTH_ON, TARDISDisplayItem.LIGHT_ELEVENTH, TARDISDisplayItem.LIGHT_ELEVENTH_CLOISTER),
    TWELFTH(TARDISDisplayItem.LIGHT_TWELFTH_ON, TARDISDisplayItem.LIGHT_TWELFTH, TARDISDisplayItem.LIGHT_TWELFTH_CLOISTER),
    THIRTEENTH(TARDISDisplayItem.LIGHT_THIRTEENTH_ON, TARDISDisplayItem.LIGHT_THIRTEENTH, TARDISDisplayItem.LIGHT_THIRTEENTH_CLOISTER),
    LAMP(Material.REDSTONE_LAMP, TARDISDisplayItem.LIGHT_LAMP_ON, TARDISDisplayItem.LIGHT_LAMP),
    LANTERN(TARDISDisplayItem.LIGHT_LANTERN_ON, TARDISDisplayItem.LIGHT_LANTERN),
    BLUE_LAMP(TARDISDisplayItem.BLUE_LAMP_ON, TARDISDisplayItem.BLUE_LAMP),
    GREEN_LAMP(TARDISDisplayItem.GREEN_LAMP_ON, TARDISDisplayItem.GREEN_LAMP),
    PURPLE_LAMP(TARDISDisplayItem.PURPLE_LAMP_ON, TARDISDisplayItem.PURPLE_LAMP),
    RED_LAMP(TARDISDisplayItem.RED_LAMP_ON, TARDISDisplayItem.RED_LAMP),
    VARIABLE(TARDISDisplayItem.LIGHT_VARIABLE_ON, TARDISDisplayItem.LIGHT_VARIABLE),
    VARIABLE_BLUE(TARDISDisplayItem.LIGHT_VARIABLE_BLUE_ON, TARDISDisplayItem.LIGHT_VARIABLE_BLUE),
    VARIABLE_GREEN(TARDISDisplayItem.LIGHT_VARIABLE_GREEN_ON, TARDISDisplayItem.LIGHT_VARIABLE_GREEN),
    VARIABLE_ORANGE(TARDISDisplayItem.LIGHT_VARIABLE_ORANGE_ON, TARDISDisplayItem.LIGHT_VARIABLE_ORANGE),
    VARIABLE_PINK(TARDISDisplayItem.LIGHT_VARIABLE_PINK_ON, TARDISDisplayItem.LIGHT_VARIABLE_PINK),
    VARIABLE_PURPLE(TARDISDisplayItem.LIGHT_VARIABLE_PURPLE_ON, TARDISDisplayItem.LIGHT_VARIABLE_PURPLE),
    VARIABLE_RED(TARDISDisplayItem.LIGHT_VARIABLE_CLOISTER, TARDISDisplayItem.LIGHT_VARIABLE),
    VARIABLE_YELLOW(TARDISDisplayItem.LIGHT_VARIABLE_YELLOW_ON, TARDISDisplayItem.LIGHT_VARIABLE_YELLOW);

    private final Material material;
    private final TARDISDisplayItem on;
    private final TARDISDisplayItem off;
    private final TARDISDisplayItem cloister;

    TardisLight(TARDISDisplayItem on, TARDISDisplayItem off, TARDISDisplayItem cloister) {
        this.material = Material.SEA_LANTERN;
        this.on = on;
        this.off = off;
        this.cloister = cloister;
    }

    TardisLight(TARDISDisplayItem on, TARDISDisplayItem off) {
        this.material = Material.SEA_LANTERN;
        this.on = on;
        this.off = off;
        this.cloister = TARDISDisplayItem.NONE;
    }

    TardisLight(Material material, TARDISDisplayItem on, TARDISDisplayItem off) {
        this.material = material;
        this.on = on;
        this.off = off;
        this.cloister = TARDISDisplayItem.NONE;
    }

    TardisLight(Material material, TARDISDisplayItem on, TARDISDisplayItem off, TARDISDisplayItem cloister) {
        this.material = material;
        this.on = on;
        this.off = off;
        this.cloister = cloister;
    }

    public static TARDISDisplayItem getToggled(TARDISDisplayItem tdi) {
        for (TardisLight light : TardisLight.values()) {
            if (light.getOff() == tdi) {
                return light.getOn();
            }
            if (light.getOn() == tdi) {
                return light.getOff();
            }
        }
        return null;
    }

    public static TardisLight getFromDisplayItem(TARDISDisplayItem tdi) {
        String s = tdi.toString();
        return switch (tdi) {
            case RED_LAMP, GREEN_LAMP, PURPLE_LAMP, BLUE_LAMP -> TardisLight.valueOf(s);
            // remove _ON
            case RED_LAMP_ON, GREEN_LAMP_ON, PURPLE_LAMP_ON, BLUE_LAMP_ON -> TardisLight.valueOf(s.substring(0, s.length() - 3));
            // remove LIGHT_
            case LIGHT_BULB, LIGHT_CLASSIC, LIGHT_CLASSIC_OFFSET, LIGHT_TENTH, LIGHT_ELEVENTH, LIGHT_TWELFTH, LIGHT_THIRTEENTH, LIGHT_LAMP, LIGHT_LANTERN, LIGHT_VARIABLE -> TardisLight.valueOf(s.substring(6));
            // remove LIGHT_ and _ON
            case LIGHT_BULB_ON, LIGHT_CLASSIC_ON, LIGHT_CLASSIC_OFFSET_ON, LIGHT_TENTH_ON, LIGHT_ELEVENTH_ON, LIGHT_TWELFTH_ON, LIGHT_THIRTEENTH_ON, LIGHT_LAMP_ON, LIGHT_LANTERN_ON, LIGHT_VARIABLE_ON, LIGHT_VARIABLE_BLUE_ON, LIGHT_VARIABLE_GREEN_ON,
                 LIGHT_VARIABLE_ORANGE_ON, LIGHT_VARIABLE_PINK_ON, LIGHT_VARIABLE_PURPLE_ON, LIGHT_VARIABLE_YELLOW_ON -> TardisLight.valueOf(s.substring(6, s.length() - 3));
            default -> TENTH;
        };
    }

    public Material getMaterial() {
        return material;
    }

    public TARDISDisplayItem getOn() {
        return on;
    }

    public TARDISDisplayItem getOff() {
        return off;
    }

    public TARDISDisplayItem getCloister() {
        return cloister;
    }
}
