package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LimeGlazedTerracotta {


    LEGACY_DELUXE(new NamespacedKey(TARDIS.plugin, "block/seed/legacy_deluxe")),

    SYS_LOCKED_TREE(new NamespacedKey(TARDIS.plugin, "circuit/sys_locked_tree")),

    SYS_LOCKED_BRANCH_DEPENDENT(new NamespacedKey(TARDIS.plugin, "circuit/sys_locked_branch_dependent")),

    SYS_LOCKED_BRANCH_INDEPENDENT(new NamespacedKey(TARDIS.plugin, "circuit/sys_locked_branch_independent")),

    SYS_LOCKED_DOWN(new NamespacedKey(TARDIS.plugin, "circuit/sys_locked_down")),

    SYS_LOCKED_END(new NamespacedKey(TARDIS.plugin, "circuit/sys_locked_end")),

    SYS_LOCKED_INDEPENDENT(new NamespacedKey(TARDIS.plugin, "circuit/sys_locked_independent")),

    SYS_UNLOCKED_TREE(new NamespacedKey(TARDIS.plugin, "circuit/sys_unlocked_tree")),

    SYS_UNLOCKED_BRANCH_DEPENDENT(new NamespacedKey(TARDIS.plugin, "circuit/sys_unlocked_branch_dependent")),

    SYS_UNLOCKED_BRANCH_INDEPENDENT(new NamespacedKey(TARDIS.plugin, "circuit/sys_unlocked_branch_independent")),

    SYS_UNLOCKED_DOWN(new NamespacedKey(TARDIS.plugin, "circuit/sys_unlocked_down")),

    SYS_UNLOCKED_END(new NamespacedKey(TARDIS.plugin, "circuit/sys_unlocked_end")),

    SYS_UNLOCKED_INDEPENDENT(new NamespacedKey(TARDIS.plugin, "circuit/sys_unlocked_independent"));

    private final NamespacedKey key;

    LimeGlazedTerracotta(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
