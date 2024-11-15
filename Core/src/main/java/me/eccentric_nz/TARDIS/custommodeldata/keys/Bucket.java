package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Bucket {

    DELETE(new NamespacedKey(TARDIS.plugin, "gui/bucket/delete")),
    DEACTIVATE(new NamespacedKey(TARDIS.plugin, "gui/bucket/deactivate")),
    REMOVE(new NamespacedKey(TARDIS.plugin, "gui/bucket/remove")),
    ARTRON_BATTERY(new NamespacedKey(TARDIS.plugin, "tardis/artron_battery")),
    BLASTER_BATTERY(new NamespacedKey(TARDIS.plugin, "tardis/blaster_battery")),
    ARTRON_CAPACITOR(new NamespacedKey(TARDIS.plugin, "tardis/artron_capacitor")),
    ARTRON_CAPACITOR_DAMAGED(new NamespacedKey(TARDIS.plugin, "tardis/artron_capacitor_damaged"));

    private final NamespacedKey key;

    Bucket(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
