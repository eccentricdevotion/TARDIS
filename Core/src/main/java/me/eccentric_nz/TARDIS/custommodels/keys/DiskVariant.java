package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum DiskVariant {

    AREA_DISK(new NamespacedKey(TARDIS.plugin, "area_disk")),
    BIOME_DISK(new NamespacedKey(TARDIS.plugin, "biome_disk")),
    SAVE_DISK(new NamespacedKey(TARDIS.plugin, "save_disk")),
    CONTROL_DISK(new NamespacedKey(TARDIS.plugin, "control_disk")),
    PRESET_DISK(new NamespacedKey(TARDIS.plugin, "preset_disk")),
    BLUEPRINT_DISK(new NamespacedKey(TARDIS.plugin, "blueprint_disk")),
    BLANK_DISK(new NamespacedKey(TARDIS.plugin, "blank_disk")),
    PLAYER_DISK(new NamespacedKey(TARDIS.plugin, "player_disk")),
    HANDLES_DISK(new NamespacedKey(TARDIS.plugin, "handles_disk"));

    private final NamespacedKey key;

    DiskVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
