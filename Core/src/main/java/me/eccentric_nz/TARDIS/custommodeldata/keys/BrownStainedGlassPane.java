package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BrownStainedGlassPane {

    DOCTOR(new NamespacedKey(TARDIS.plugin, "item/lazarus/doctor")),
    COMPANION(new NamespacedKey(TARDIS.plugin, "item/lazarus/companion")),
    CHARACTER(new NamespacedKey(TARDIS.plugin, "item/lazarus/character")),
    MONSTER(new NamespacedKey(TARDIS.plugin, "item/lazarus/monster")),
    BROWN_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "item/products/glow_sticks/brown_glow_stick")),
    BROWN_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "item/products/glow_sticks/brown_glow_stick_active"));

    private final NamespacedKey key;

    BrownStainedGlassPane(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
