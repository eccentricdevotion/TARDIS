package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SeedBlock {

    ANCIENT(new NamespacedKey(TARDIS.plugin, "block/seed/ancient")),
    ARS(new NamespacedKey(TARDIS.plugin, "block/seed/ars")),
    BIGGER(new NamespacedKey(TARDIS.plugin, "block/seed/bigger")),
    BONE(new NamespacedKey(TARDIS.plugin, "block/seed/bone")),
    BUDGET(new NamespacedKey(TARDIS.plugin, "block/seed/budget")),
    CAVE(new NamespacedKey(TARDIS.plugin, "block/seed/cave")),
    COPPER(new NamespacedKey(TARDIS.plugin, "block/seed/copper")),
    CORAL(new NamespacedKey(TARDIS.plugin, "block/seed/coral")),
    CURSED(new NamespacedKey(TARDIS.plugin, "block/seed/cursed")),
    DELTA(new NamespacedKey(TARDIS.plugin, "block/seed/delta")),
    DELUXE(new NamespacedKey(TARDIS.plugin, "block/seed/deluxe")),
    DIVISION(new NamespacedKey(TARDIS.plugin, "block/seed/division")),
    ELEVENTH(new NamespacedKey(TARDIS.plugin, "block/seed/eleventh")),
    ENDER(new NamespacedKey(TARDIS.plugin, "block/seed/ender")),
    FACTORY(new NamespacedKey(TARDIS.plugin, "block/seed/factory")),
    FIFTEENTH(new NamespacedKey(TARDIS.plugin, "block/seed/fifteenth")),
    FUGITIVE(new NamespacedKey(TARDIS.plugin, "block/seed/fugitive")),
    HOSPITAL(new NamespacedKey(TARDIS.plugin, "block/seed/hospital")),
    MASTER(new NamespacedKey(TARDIS.plugin, "block/seed/master")),
    MECHANICAL(new NamespacedKey(TARDIS.plugin, "block/seed/mechanical")),
    ORIGINAL(new NamespacedKey(TARDIS.plugin, "block/seed/original")),
    PLANK(new NamespacedKey(TARDIS.plugin, "block/seed/plank")),
    PYRAMID(new NamespacedKey(TARDIS.plugin, "block/seed/pyramid")),
    REDSTONE(new NamespacedKey(TARDIS.plugin, "block/seed/redstone")),
    ROTOR(new NamespacedKey(TARDIS.plugin, "block/seed/rotor")),
    RUSTIC(new NamespacedKey(TARDIS.plugin, "block/seed/rustic")),
    STEAMPUNK(new NamespacedKey(TARDIS.plugin, "seed/steampunk")),
    THIRTEENTH(new NamespacedKey(TARDIS.plugin, "block/seed/thirteenth")),
    TOM(new NamespacedKey(TARDIS.plugin, "block/seed/tom")),
    TWELFTH(new NamespacedKey(TARDIS.plugin, "block/seed/twelfth")),
    WAR(new NamespacedKey(TARDIS.plugin, "block/seed/war")),
    WEATHERED(new NamespacedKey(TARDIS.plugin, "block/seed/weathered")),
    LEGACY_BIGGER(new NamespacedKey(TARDIS.plugin, "block/seed/legacy_bigger")),
    LEGACY_DELUXE(new NamespacedKey(TARDIS.plugin, "block/seed/legacy_deluxe")),
    LEGACY_ELEVENTH(new NamespacedKey(TARDIS.plugin, "block/seed/legacy_eleventh")),
    LEGACY_REDSTONE(new NamespacedKey(TARDIS.plugin, "block/seed/legacy_redstone")),
    CUSTOM(new NamespacedKey(TARDIS.plugin, "block/seed/custom"));

    private final NamespacedKey key;

    SeedBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
