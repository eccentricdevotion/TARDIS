package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum TotemOfUndying {

    FIRST(new NamespacedKey(TARDIS.plugin, "regeneration/first")),
    SECOND(new NamespacedKey(TARDIS.plugin, "regeneration/second")),
    THIRD(new NamespacedKey(TARDIS.plugin, "regeneration/third")),
    FOURTH(new NamespacedKey(TARDIS.plugin, "regeneration/fourth")),
    FIFTH(new NamespacedKey(TARDIS.plugin, "regeneration/fifth")),
    SIXTH(new NamespacedKey(TARDIS.plugin, "regeneration/sixth")),
    SEVENTH(new NamespacedKey(TARDIS.plugin, "regeneration/seventh")),
    EIGHTH(new NamespacedKey(TARDIS.plugin, "regeneration/eighth")),
    NINTH(new NamespacedKey(TARDIS.plugin, "regeneration/ninth")),
    TENTH(new NamespacedKey(TARDIS.plugin, "regeneration/tenth")),
    ELEVENTH(new NamespacedKey(TARDIS.plugin, "regeneration/eleventh")),
    TWELFTH(new NamespacedKey(TARDIS.plugin, "regeneration/twelfth")),
    THIRTEENTH(new NamespacedKey(TARDIS.plugin, "regeneration/thirteenth")),
    FOURTEENTH(new NamespacedKey(TARDIS.plugin, "regeneration/fourteenth")),
    FIFTEENTH(new NamespacedKey(TARDIS.plugin, "regeneration/fifteenth")),
    WAR(new NamespacedKey(TARDIS.plugin, "regeneration/war")),
    RASSILON(new NamespacedKey(TARDIS.plugin, "regeneration/rassilon"));

    private final NamespacedKey key;

    TotemOfUndying(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
