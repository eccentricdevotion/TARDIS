package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum OrangeStainedGlassPane {

    IMPOSSIBLE_ASTRONAUT_PACK(new NamespacedKey(TARDIS.plugin, "item/lazarus/impossible_astronaut_pack")),
    ORANGE_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "item/products/glow_sticks/orange_glow_stick")),
    ORANGE_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "item/products/glow_sticks/orange_glow_stick_active"));

    private final NamespacedKey key;

    OrangeStainedGlassPane(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
