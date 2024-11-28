package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BirchButton {

    HANDLES_OFF(new NamespacedKey(TARDIS.plugin, "handles/handles_off")),
    HANDLES_ON(new NamespacedKey(TARDIS.plugin, "handles/handles_on")),
    COMMUNICATOR(new NamespacedKey(TARDIS.plugin, "handles/communicator")),
    // TODO make texture file
    COMMUNICATOR_OVERLAY(new NamespacedKey(TARDIS.plugin, "handles/communicator_overlay"));

    private final NamespacedKey key;

    BirchButton(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
