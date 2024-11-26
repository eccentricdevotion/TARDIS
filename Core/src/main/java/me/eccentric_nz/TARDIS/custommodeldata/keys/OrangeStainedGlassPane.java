package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum OrangeStainedGlassPane {

    IMPOSSIBLE_ASTRONAUT_PACK(new NamespacedKey(TARDIS.plugin, "lazarus/impossible_astronaut_pack"));

    private final NamespacedKey key;

    OrangeStainedGlassPane(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
