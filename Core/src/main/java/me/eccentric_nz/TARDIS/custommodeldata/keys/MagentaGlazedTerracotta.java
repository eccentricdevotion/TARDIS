package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MagentaGlazedTerracotta {


    SYS_BOTH_DOWN(new NamespacedKey(TARDIS.plugin, "circuit/sys_both_down")),

    SYS_LEFT_DOWN(new NamespacedKey(TARDIS.plugin, "circuit/sys_left_down")),

    SYS_HORIZONTAL(new NamespacedKey(TARDIS.plugin, "circuit/sys_horizontal")),

    SYS_RIGHT_DOWN(new NamespacedKey(TARDIS.plugin, "circuit/sys_right_down")),

    SYS_BLANK(new NamespacedKey(TARDIS.plugin, "circuit/sys_blank")),

    SYS_CLOSE(new NamespacedKey(TARDIS.plugin, "circuit/sys_close")),

    SYS_VERTICAL(new NamespacedKey(TARDIS.plugin, "circuit/sys_vertical"));

    private final NamespacedKey key;

    MagentaGlazedTerracotta(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
