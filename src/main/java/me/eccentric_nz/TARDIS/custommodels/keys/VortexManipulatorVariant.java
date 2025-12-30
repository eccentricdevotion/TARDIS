/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

import java.util.List;

public enum VortexManipulatorVariant {

    PREDICTIVE(new NamespacedKey(TARDIS.plugin, "button_predictive")),
    PERCENT_ZERO(new NamespacedKey(TARDIS.plugin, "button_percent_zero"), List.of(1.0f)),
    PERCENT_TEN(new NamespacedKey(TARDIS.plugin, "button_percent_ten"), List.of(10.0f)),
    PERCENT_TWENTY(new NamespacedKey(TARDIS.plugin, "button_percent_twenty"), List.of(20.0f)),
    PERCENT_THIRTY(new NamespacedKey(TARDIS.plugin, "button_percent_thirty"), List.of(30.0f)),
    PERCENT_FORTY(new NamespacedKey(TARDIS.plugin, "button_percent_forty"), List.of(40.0f)),
    PERCENT_FIFTY(new NamespacedKey(TARDIS.plugin, "button_percent_fifty"), List.of(50.0f)),
    PERCENT_SIXTY(new NamespacedKey(TARDIS.plugin, "button_percent_sixty"), List.of(60.0f)),
    PERCENT_SEVENTY(new NamespacedKey(TARDIS.plugin, "button_percent_seventy"), List.of(70.0f)),
    PERCENT_EIGHTY(new NamespacedKey(TARDIS.plugin, "button_percent_eighty"), List.of(80.0f)),
    PERCENT_NINETY(new NamespacedKey(TARDIS.plugin, "button_percent_ninety"), List.of(90.0f)),
    PERCENT_HUNDRED(new NamespacedKey(TARDIS.plugin, "button_percent_hundred"), List.of(100.0f)),
    BEACON(new NamespacedKey(TARDIS.plugin, "button_beacon")),
    DELETE(new NamespacedKey(TARDIS.plugin, "button_vm_delete")),
    DISPLAY(new NamespacedKey(TARDIS.plugin, "button_display")),
    ZERO(new NamespacedKey(TARDIS.plugin, "button_vm_zero")),
    ONE(new NamespacedKey(TARDIS.plugin, "button_vm_one")),
    TWO(new NamespacedKey(TARDIS.plugin, "button_vm_two")),
    THREE(new NamespacedKey(TARDIS.plugin, "button_vm_three")),
    FOUR(new NamespacedKey(TARDIS.plugin, "button_vm_four")),
    FIVE(new NamespacedKey(TARDIS.plugin, "button_vm_five")),
    SIX(new NamespacedKey(TARDIS.plugin, "button_vm_six")),
    SEVEN(new NamespacedKey(TARDIS.plugin, "button_vm_seven")),
    EIGHT(new NamespacedKey(TARDIS.plugin, "button_vm_eight")),
    NINE(new NamespacedKey(TARDIS.plugin, "button_vm_nine")),
    HASH(new NamespacedKey(TARDIS.plugin, "button_hash")),
    LIFE(new NamespacedKey(TARDIS.plugin, "button_life")),
    LOAD(new NamespacedKey(TARDIS.plugin, "button_load")),
    MESSAGE(new NamespacedKey(TARDIS.plugin, "button_message")),
    NEXT(new NamespacedKey(TARDIS.plugin, "button_vm_next")),
    PREV(new NamespacedKey(TARDIS.plugin, "button_vm_prev")),
    PAGE(new NamespacedKey(TARDIS.plugin, "button_page")),
    READ(new NamespacedKey(TARDIS.plugin, "button_read")),
    STAR(new NamespacedKey(TARDIS.plugin, "button_star")),
    WARP(new NamespacedKey(TARDIS.plugin, "button_warp")),
    WORLD(new NamespacedKey(TARDIS.plugin, "button_world")),
    X(new NamespacedKey(TARDIS.plugin, "button_x")),
    Y(new NamespacedKey(TARDIS.plugin, "button_y")),
    Z(new NamespacedKey(TARDIS.plugin, "button_z"));

    private final NamespacedKey key;
    private final List<Float> floats;

    VortexManipulatorVariant(NamespacedKey key, List<Float> floats) {
        this.key = key;
        this.floats = floats;
    }

    VortexManipulatorVariant(NamespacedKey key) {
        this.key = key;
        this.floats = null;
    }


    public NamespacedKey getKey() {
        return key;
    }

    public List<Float> getFloats() {
        return floats;
    }
}
