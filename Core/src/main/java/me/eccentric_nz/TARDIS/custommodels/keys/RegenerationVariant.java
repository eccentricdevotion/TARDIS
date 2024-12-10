package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum RegenerationVariant {

    FIRST(new NamespacedKey(TARDIS.plugin, "regeneration_first")),
    SECOND(new NamespacedKey(TARDIS.plugin, "regeneration_second")),
    THIRD(new NamespacedKey(TARDIS.plugin, "regeneration_third")),
    FOURTH(new NamespacedKey(TARDIS.plugin, "regeneration_fourth")),
    FIFTH(new NamespacedKey(TARDIS.plugin, "regeneration_fifth")),
    SIXTH(new NamespacedKey(TARDIS.plugin, "regeneration_sixth")),
    SEVENTH(new NamespacedKey(TARDIS.plugin, "regeneration_seventh")),
    EIGHTH(new NamespacedKey(TARDIS.plugin, "regeneration_eighth")),
    NINTH(new NamespacedKey(TARDIS.plugin, "regeneration_ninth")),
    TENTH(new NamespacedKey(TARDIS.plugin, "regeneration_tenth")),
    ELEVENTH(new NamespacedKey(TARDIS.plugin, "regeneration_eleventh")),
    TWELFTH(new NamespacedKey(TARDIS.plugin, "regeneration_twelfth")),
    THIRTEENTH(new NamespacedKey(TARDIS.plugin, "regeneration_thirteenth")),
    FOURTEENTH(new NamespacedKey(TARDIS.plugin, "regeneration_fourteenth")),
    FIFTEENTH(new NamespacedKey(TARDIS.plugin, "regeneration_fifteenth")),
    WAR(new NamespacedKey(TARDIS.plugin, "regeneration_war")),
    RASSILON(new NamespacedKey(TARDIS.plugin, "regeneration_rassilon"));

    private final NamespacedKey key;

    RegenerationVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
