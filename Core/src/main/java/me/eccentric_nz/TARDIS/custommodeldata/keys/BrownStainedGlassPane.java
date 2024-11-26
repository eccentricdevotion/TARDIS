package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BrownStainedGlassPane {

    DOCTOR(new NamespacedKey(TARDIS.plugin, "lazarus/doctor")),
    COMPANION(new NamespacedKey(TARDIS.plugin, "lazarus/companion")),
    CHARACTER(new NamespacedKey(TARDIS.plugin, "lazarus/character")),
    MONSTER(new NamespacedKey(TARDIS.plugin, "lazarus/monster"));

    private final NamespacedKey key;

    BrownStainedGlassPane(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
