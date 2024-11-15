package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum DaylightDetector {

    TELEPATHIC_CIRCUIT(new NamespacedKey(TARDIS.plugin, "block/controls/telepathic_circuit"));

    private final NamespacedKey key;

    DaylightDetector(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
