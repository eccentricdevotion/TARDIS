package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MusicDisc {

    AREA_DISK(new NamespacedKey(TARDIS.plugin, "disks/area_disk")),
    BIOME_DISK(new NamespacedKey(TARDIS.plugin, "disks/biome_disk")),
    SAVE_DISK(new NamespacedKey(TARDIS.plugin, "disks/save_disk")),
    CONTROL_DISK(new NamespacedKey(TARDIS.plugin, "disks/control_disk")),
    PRESET_DISK(new NamespacedKey(TARDIS.plugin, "disks/preset_disk")),
    BLUEPRINT_DISK(new NamespacedKey(TARDIS.plugin, "disks/blueprint_disk")),
    BLANK_DISK(new NamespacedKey(TARDIS.plugin, "disks/blank_disk")),
    PLAYER_DISK(new NamespacedKey(TARDIS.plugin, "disks/player_disk")),
    HANDLES_DISK(new NamespacedKey(TARDIS.plugin, "disks/handles_disk"));

    private final NamespacedKey key;

    MusicDisc(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
