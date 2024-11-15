package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum TotemOfUndying {

    FIRST(new NamespacedKey(TARDIS.plugin, "item/regeneration/first")),
    SECOND(new NamespacedKey(TARDIS.plugin, "item/regeneration/second")),
    THIRD(new NamespacedKey(TARDIS.plugin, "item/regeneration/third")),
    FOURTH(new NamespacedKey(TARDIS.plugin, "item/regeneration/fourth")),
    FIFTH(new NamespacedKey(TARDIS.plugin, "item/regeneration/fifth")),
    SIXTH(new NamespacedKey(TARDIS.plugin, "item/regeneration/sixth")),
    SEVENTH(new NamespacedKey(TARDIS.plugin, "item/regeneration/seventh")),
    EIGHTH(new NamespacedKey(TARDIS.plugin, "item/regeneration/eighth")),
    NINTH(new NamespacedKey(TARDIS.plugin, "item/regeneration/ninth")),
    TENTH(new NamespacedKey(TARDIS.plugin, "item/regeneration/tenth")),
    ELEVENTH(new NamespacedKey(TARDIS.plugin, "item/regeneration/eleventh")),
    TWELFTH(new NamespacedKey(TARDIS.plugin, "item/regeneration/twelfth")),
    THIRTEENTH(new NamespacedKey(TARDIS.plugin, "item/regeneration/thirteenth")),
    FOURTEENTH(new NamespacedKey(TARDIS.plugin, "item/regeneration/fourteenth")),
    FIFTEENTH(new NamespacedKey(TARDIS.plugin, "item/regeneration/fifteenth")),
    WAR(new NamespacedKey(TARDIS.plugin, "item/regeneration/war")),
    RASSILON(new NamespacedKey(TARDIS.plugin, "item/regeneration/rassilon"));

    private final NamespacedKey key;

    TotemOfUndying(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
