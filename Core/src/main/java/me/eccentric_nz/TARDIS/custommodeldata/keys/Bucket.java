package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Bucket {

    DELETE(new NamespacedKey(TARDIS.plugin, "gui/bucket/delete")),
    DEACTIVATE(new NamespacedKey(TARDIS.plugin, "gui/bucket/deactivate")),
    REMOVE(new NamespacedKey(TARDIS.plugin, "gui/bucket/remove"));

    private final NamespacedKey key;

    Bucket(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
