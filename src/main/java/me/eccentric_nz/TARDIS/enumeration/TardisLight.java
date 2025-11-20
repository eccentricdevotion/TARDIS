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

import me.eccentric_nz.TARDIS.customblocks.TARDISBlockDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISChemistryDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISLightDisplayItem;
import org.bukkit.Material;

import static me.eccentric_nz.TARDIS.customblocks.TARDISChemistryDisplayItem.*;
import static me.eccentric_nz.TARDIS.customblocks.TARDISLightDisplayItem.*;

public enum TardisLight {

    BULB(TARDISLightDisplayItem.LIGHT_BULB_ON, TARDISLightDisplayItem.LIGHT_BULB, TARDISLightDisplayItem.LIGHT_BULB_CLOISTER),
    CLASSIC(TARDISLightDisplayItem.LIGHT_CLASSIC_ON, TARDISLightDisplayItem.LIGHT_CLASSIC, TARDISLightDisplayItem.LIGHT_CLASSIC_CLOISTER),
    CLASSIC_OFFSET(TARDISLightDisplayItem.LIGHT_CLASSIC_OFFSET_ON, TARDISLightDisplayItem.LIGHT_CLASSIC_OFFSET, TARDISLightDisplayItem.LIGHT_CLASSIC_OFFSET_CLOISTER),
    TENTH(Material.REDSTONE_LAMP, TARDISLightDisplayItem.LIGHT_TENTH_ON, TARDISLightDisplayItem.LIGHT_TENTH, TARDISLightDisplayItem.LIGHT_TENTH_CLOISTER),
    ELEVENTH(TARDISLightDisplayItem.LIGHT_ELEVENTH_ON, TARDISLightDisplayItem.LIGHT_ELEVENTH, TARDISLightDisplayItem.LIGHT_ELEVENTH_CLOISTER),
    TWELFTH(TARDISLightDisplayItem.LIGHT_TWELFTH_ON, TARDISLightDisplayItem.LIGHT_TWELFTH, TARDISLightDisplayItem.LIGHT_TWELFTH_CLOISTER),
    THIRTEENTH(TARDISLightDisplayItem.LIGHT_THIRTEENTH_ON, TARDISLightDisplayItem.LIGHT_THIRTEENTH, TARDISLightDisplayItem.LIGHT_THIRTEENTH_CLOISTER),
    LAMP(Material.REDSTONE_LAMP, TARDISLightDisplayItem.LIGHT_LAMP_ON, TARDISLightDisplayItem.LIGHT_LAMP),
    LANTERN(TARDISLightDisplayItem.LIGHT_LANTERN_ON, TARDISLightDisplayItem.LIGHT_LANTERN),
    BLUE_LAMP(TARDISChemistryDisplayItem.BLUE_LAMP_ON, TARDISChemistryDisplayItem.BLUE_LAMP),
    GREEN_LAMP(TARDISChemistryDisplayItem.GREEN_LAMP_ON, TARDISChemistryDisplayItem.GREEN_LAMP),
    PURPLE_LAMP(TARDISChemistryDisplayItem.PURPLE_LAMP_ON, TARDISChemistryDisplayItem.PURPLE_LAMP),
    RED_LAMP(TARDISChemistryDisplayItem.RED_LAMP_ON, TARDISChemistryDisplayItem.RED_LAMP),
    VARIABLE(TARDISLightDisplayItem.LIGHT_VARIABLE_ON, TARDISLightDisplayItem.LIGHT_VARIABLE),
    VARIABLE_BLUE(TARDISLightDisplayItem.LIGHT_VARIABLE_BLUE_ON, TARDISLightDisplayItem.LIGHT_VARIABLE_BLUE),
    VARIABLE_GREEN(TARDISLightDisplayItem.LIGHT_VARIABLE_GREEN_ON, TARDISLightDisplayItem.LIGHT_VARIABLE_GREEN),
    VARIABLE_ORANGE(TARDISLightDisplayItem.LIGHT_VARIABLE_ORANGE_ON, TARDISLightDisplayItem.LIGHT_VARIABLE_ORANGE),
    VARIABLE_PINK(TARDISLightDisplayItem.LIGHT_VARIABLE_PINK_ON, TARDISLightDisplayItem.LIGHT_VARIABLE_PINK),
    VARIABLE_PURPLE(TARDISLightDisplayItem.LIGHT_VARIABLE_PURPLE_ON, TARDISLightDisplayItem.LIGHT_VARIABLE_PURPLE),
    VARIABLE_RED(TARDISLightDisplayItem.LIGHT_VARIABLE_CLOISTER, TARDISLightDisplayItem.LIGHT_VARIABLE),
    VARIABLE_YELLOW(TARDISLightDisplayItem.LIGHT_VARIABLE_YELLOW_ON, TARDISLightDisplayItem.LIGHT_VARIABLE_YELLOW);

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
        this.cloister = TARDISBlockDisplayItem.NONE;
    }

    TardisLight(Material material, TARDISDisplayItem on, TARDISDisplayItem off) {
        this.material = material;
        this.on = on;
        this.off = off;
        this.cloister = TARDISBlockDisplayItem.NONE;
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
            case TARDISChemistryDisplayItem.RED_LAMP, TARDISChemistryDisplayItem.GREEN_LAMP,
                 TARDISChemistryDisplayItem.PURPLE_LAMP, TARDISChemistryDisplayItem.BLUE_LAMP -> TardisLight.valueOf(s);
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
