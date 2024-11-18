package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Bone {

    K9(new NamespacedKey(TARDIS.plugin, "monster/k9/k9")),
    BUTTON_K9(new NamespacedKey(TARDIS.plugin, "genetic/k9")),
    K9_0(new NamespacedKey(TARDIS.plugin, "monster/k9/frames/k9_0")),
    K9_1(new NamespacedKey(TARDIS.plugin, "monster/k9/frames/k9_1")),
    K9_2(new NamespacedKey(TARDIS.plugin, "monster/k9/frames/k9_2")),
    K9_3(new NamespacedKey(TARDIS.plugin, "monster/k9/frames/k9_3")),
    K9_4(new NamespacedKey(TARDIS.plugin, "monster/k9/frames/k9_4")),
    WAND(new NamespacedKey(TARDIS.plugin, "tardis/wand"));

    private final NamespacedKey key;

    Bone(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

