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
package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.custommodeldata.keys.TimeVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum GUITemporalLocator {

    // Temporal Locator
    BUTTON_MORN(TimeVariant.MORNING.getKey(), 0, Material.CLOCK, "0 ticks~6 AM"),
    BUTTON_NOON(TimeVariant.NOON.getKey(), 1, Material.CLOCK, "6000 ticks~12 Noon"),
    BUTTON_NIGHT(TimeVariant.NIGHT.getKey(), 2, Material.CLOCK, "12000 ticks~6 PM"),
    BUTTON_MID(TimeVariant.MIDNIGHT.getKey(), 3, Material.CLOCK, "18000 ticks~12 PM"),
    AM_7(TimeVariant.SEVEN_AM.getKey(), 4, Material.CLOCK, "1000 ticks"),
    AM_8(TimeVariant.EIGHT_AM.getKey(), 5, Material.CLOCK, "2000 ticks"),
    AM_9(TimeVariant.NINE_AM.getKey(), 6, Material.CLOCK, "3000 ticks"),
    AM_10(TimeVariant.TEN_AM.getKey(), 7, Material.CLOCK, "4000 ticks"),
    AM_11(TimeVariant.ELEVEN_AM.getKey(), 8, Material.CLOCK, "5000 ticks"),
    PM_12(TimeVariant.TWELVE_AM.getKey(), 9, Material.CLOCK, "6000 ticks"),
    PM_1(TimeVariant.ONE_PM.getKey(), 10, Material.CLOCK, "7000 ticks"),
    PM_2(TimeVariant.TWO_PM.getKey(), 11, Material.CLOCK, "8000 ticks"),
    PM_3(TimeVariant.THREE_PM.getKey(), 12, Material.CLOCK, "9000 ticks"),
    PM_4(TimeVariant.FOUR_PM.getKey(), 13, Material.CLOCK, "10000 ticks"),
    PM_5(TimeVariant.FIVE_PM.getKey(), 14, Material.CLOCK, "11000 ticks"),
    PM_6(TimeVariant.SIX_PM.getKey(), 15, Material.CLOCK, "12000 ticks"),
    PM_7(TimeVariant.SEVEN_PM.getKey(), 16, Material.CLOCK, "13000 ticks"),
    PM_8(TimeVariant.EIGHT_PM.getKey(), 17, Material.CLOCK, "14000 ticks"),
    PM_9(TimeVariant.NINE_PM.getKey(), 18, Material.CLOCK, "15000 ticks"),
    PM_10(TimeVariant.TEN_PM.getKey(), 19, Material.CLOCK, "16000 ticks"),
    PM_11(TimeVariant.ELEVEN_PM.getKey(), 20, Material.CLOCK, "17000 ticks"),
    AM_12(TimeVariant.TWELVE_AM.getKey(), 21, Material.CLOCK, "18000 ticks"),
    AM_1(TimeVariant.ONE_AM.getKey(), 22, Material.CLOCK, "19000 ticks"),
    AM_2(TimeVariant.TWO_AM.getKey(), 23, Material.CLOCK, "20000 ticks"),
    AM_3(TimeVariant.THREE_AM.getKey(), 24, Material.CLOCK, "21000 ticks"),
    AM_4(TimeVariant.FOUR_AM.getKey(), 25, Material.CLOCK, "22000 ticks"),
    AM_5(TimeVariant.FIVE_AM.getKey(), 26, Material.CLOCK, "23000 ticks");

    private final NamespacedKey model;
    private final int slot;
    private final Material material;
    private final String lore;

    GUITemporalLocator(NamespacedKey model, int slot, Material material, String lore) {
        this.model = model;
        this.slot = slot;
        this.material = material;
        this.lore = lore;
    }

    public NamespacedKey getModel() {
        return model;
    }

    public int getSlot() {
        return slot;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        String s = toString();
        if (s.startsWith("A") || s.startsWith("P")) {
            String[] split = s.split("_");
            return split[1] + " " + split[0];
        } else {
            return TARDISStringUtils.uppercaseFirst(s);
        }
    }

    public String getLore() {
        return lore;
    }
}
