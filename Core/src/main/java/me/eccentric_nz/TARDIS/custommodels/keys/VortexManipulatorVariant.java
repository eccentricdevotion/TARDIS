package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum VortexManipulatorVariant {

    PREDICTIVE(new NamespacedKey(TARDIS.plugin, "button_predictive")),
    PERCENT_ZERO(new NamespacedKey(TARDIS.plugin, "button_percent_zero")),
    PERCENT_TEN(new NamespacedKey(TARDIS.plugin, "button_percent_ten")),
    PERCENT_TWENTY(new NamespacedKey(TARDIS.plugin, "button_percent_twenty")),
    PERCENT_THIRTY(new NamespacedKey(TARDIS.plugin, "button_percent_thirty")),
    PERCENT_FORTY(new NamespacedKey(TARDIS.plugin, "button_percent_forty")),
    PERCENT_FIFTY(new NamespacedKey(TARDIS.plugin, "button_percent_fifty")),
    PERCENT_SIXTY(new NamespacedKey(TARDIS.plugin, "button_percent_sixty")),
    PERCENT_SEVENTY(new NamespacedKey(TARDIS.plugin, "button_percent_seventy")),
    PERCENT_EIGHTY(new NamespacedKey(TARDIS.plugin, "button_percent_eighty")),
    PERCENT_NINETY(new NamespacedKey(TARDIS.plugin, "button_percent_ninety")),
    PERCENT_HUNDRED(new NamespacedKey(TARDIS.plugin, "button_percent_hundred")),
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

    VortexManipulatorVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
