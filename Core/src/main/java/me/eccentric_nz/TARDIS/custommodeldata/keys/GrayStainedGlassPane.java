package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GrayStainedGlassPane {

    WEEPING_ANGEL(new NamespacedKey(TARDIS.plugin, "block/weeping_angel/weeping_angel")),
    WEEPING_ANGEL_OPEN(new NamespacedKey(TARDIS.plugin, "block/weeping_angel/weeping_angel_open")),
    WEEPING_ANGEL_STAINED(new NamespacedKey(TARDIS.plugin, "block/weeping_angel/weeping_angel_stained")),
    WEEPING_ANGEL_GLASS(new NamespacedKey(TARDIS.plugin, "block/weeping_angel/weeping_angel_glass"));

    private final NamespacedKey key;

    GrayStainedGlassPane(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
