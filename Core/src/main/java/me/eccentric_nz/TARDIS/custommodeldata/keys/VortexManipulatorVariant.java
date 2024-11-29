package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum VortexManipulatorVariant {

    PREDICTIVE(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/predictive")),
    PERCENT_ZERO(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/percent_zero")),
    PERCENT_TEN(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/percent_ten")),
    PERCENT_TWENTY(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/percent_twenty")),
    PERCENT_THIRTY(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/percent_thirty")),
    PERCENT_FORTY(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/percent_forty")),
    PERCENT_FIFTY(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/percent_fifty")),
    PERCENT_SIXTY(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/percent_sixty")),
    PERCENT_SEVENTY(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/percent_seventy")),
    PERCENT_EIGHTY(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/percent_eighty")),
    PERCENT_NINETY(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/percent_ninety")),
    PERCENT_HUNDRED(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/percent_hundred")),
    BEACON(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/beacon")),
    DELETE(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/delete")),
    DISPLAY(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/display")),
    ZERO(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/zero")),
    ONE(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/one")),
    TWO(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/two")),
    THREE(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/three")),
    FOUR(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/four")),
    FIVE(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/five")),
    SIX(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/six")),
    SEVEN(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/seven")),
    EIGHT(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/eight")),
    NINE(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/nine")),
    HASH(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/hash")),
    LIFE(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/life")),
    LOAD(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/load")),
    MESSAGE(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/message")),
    NEXT(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/next")),
    PREV(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/prev")),
    PAGE(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/page")),
    READ(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/read")),
    STAR(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/star")),
    WARP(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/warp")),
    WORLD(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/world")),
    X(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/x")),
    Y(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/y")),
    Z(new NamespacedKey(TARDIS.plugin, "gui/vortex_manipulator/z"));

    private final NamespacedKey key;

    VortexManipulatorVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
