package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum NetherWart {

    BANNAKAFFALATTA_SPIKES(new NamespacedKey(TARDIS.plugin, "lazarus/bannakaffalatta_spikes"));

    private final NamespacedKey key;

    NetherWart(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
