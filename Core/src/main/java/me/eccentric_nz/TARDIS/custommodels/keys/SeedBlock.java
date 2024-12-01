package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SeedBlock {

    ANCIENT(new NamespacedKey(TARDIS.plugin, "seed/ancient")),
    ARS(new NamespacedKey(TARDIS.plugin, "seed/ars")),
    BIGGER(new NamespacedKey(TARDIS.plugin, "seed/bigger")),
    BONE(new NamespacedKey(TARDIS.plugin, "seed/bone")),
    BUDGET(new NamespacedKey(TARDIS.plugin, "seed/budget")),
    CAVE(new NamespacedKey(TARDIS.plugin, "seed/cave")),
    COPPER(new NamespacedKey(TARDIS.plugin, "seed/copper")),
    CORAL(new NamespacedKey(TARDIS.plugin, "seed/coral")),
    CURSED(new NamespacedKey(TARDIS.plugin, "seed/cursed")),
    DELTA(new NamespacedKey(TARDIS.plugin, "seed/delta")),
    DELUXE(new NamespacedKey(TARDIS.plugin, "seed/deluxe")),
    DIVISION(new NamespacedKey(TARDIS.plugin, "seed/division")),
    ELEVENTH(new NamespacedKey(TARDIS.plugin, "seed/eleventh")),
    ENDER(new NamespacedKey(TARDIS.plugin, "seed/ender")),
    FACTORY(new NamespacedKey(TARDIS.plugin, "seed/factory")),
    FIFTEENTH(new NamespacedKey(TARDIS.plugin, "seed/fifteenth")),
    FUGITIVE(new NamespacedKey(TARDIS.plugin, "seed/fugitive")),
    HOSPITAL(new NamespacedKey(TARDIS.plugin, "seed/hospital")),
    MASTER(new NamespacedKey(TARDIS.plugin, "seed/master")),
    MECHANICAL(new NamespacedKey(TARDIS.plugin, "seed/mechanical")),
    ORIGINAL(new NamespacedKey(TARDIS.plugin, "seed/original")),
    PLANK(new NamespacedKey(TARDIS.plugin, "seed/plank")),
    PYRAMID(new NamespacedKey(TARDIS.plugin, "seed/pyramid")),
    REDSTONE(new NamespacedKey(TARDIS.plugin, "seed/redstone")),
    ROTOR(new NamespacedKey(TARDIS.plugin, "seed/rotor")),
    RUSTIC(new NamespacedKey(TARDIS.plugin, "seed/rustic")),
    STEAMPUNK(new NamespacedKey(TARDIS.plugin, "seed/steampunk")),
    THIRTEENTH(new NamespacedKey(TARDIS.plugin, "seed/thirteenth")),
    TOM(new NamespacedKey(TARDIS.plugin, "seed/tom")),
    TWELFTH(new NamespacedKey(TARDIS.plugin, "seed/twelfth")),
    WAR(new NamespacedKey(TARDIS.plugin, "seed/war")),
    WEATHERED(new NamespacedKey(TARDIS.plugin, "seed/weathered")),
    LEGACY_BIGGER(new NamespacedKey(TARDIS.plugin, "seed/legacy_bigger")),
    LEGACY_DELUXE(new NamespacedKey(TARDIS.plugin, "seed/legacy_deluxe")),
    LEGACY_ELEVENTH(new NamespacedKey(TARDIS.plugin, "seed/legacy_eleventh")),
    LEGACY_REDSTONE(new NamespacedKey(TARDIS.plugin, "seed/legacy_redstone")),
    CUSTOM(new NamespacedKey(TARDIS.plugin, "seed/custom")),
    GROW(new NamespacedKey(TARDIS.plugin, "seed/grow")),
    SMALL(new NamespacedKey(TARDIS.plugin, "seed/small")),
    MEDIUM(new NamespacedKey(TARDIS.plugin, "seed/medium")),
    TALL(new NamespacedKey(TARDIS.plugin, "seed/tall"));

    private final NamespacedKey key;

    SeedBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
