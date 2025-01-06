package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SystemTreeVariant {

    BOTH_DOWN(new NamespacedKey(TARDIS.plugin, "sys_both_down")),
    LEFT_DOWN(new NamespacedKey(TARDIS.plugin, "sys_left_down")),
    HORIZONTAL(new NamespacedKey(TARDIS.plugin, "sys_horizontal")),
    RIGHT_DOWN(new NamespacedKey(TARDIS.plugin, "sys_right_down")),
    BLANK(new NamespacedKey(TARDIS.plugin, "sys_blank")),
    CLOSE(new NamespacedKey(TARDIS.plugin, "sys_close")),
    VERTICAL(new NamespacedKey(TARDIS.plugin, "sys_vertical")),
    LOCKED_TREE(new NamespacedKey(TARDIS.plugin, "sys_locked_tree")),
    LOCKED_BRANCH_DEPENDENT(new NamespacedKey(TARDIS.plugin, "sys_locked_branch_dependent")),
    LOCKED_BRANCH_INDEPENDENT(new NamespacedKey(TARDIS.plugin, "sys_locked_branch_independent")),
    LOCKED_DOWN(new NamespacedKey(TARDIS.plugin, "sys_locked_down")),
    LOCKED_END(new NamespacedKey(TARDIS.plugin, "sys_locked_end")),
    LOCKED_INDEPENDENT(new NamespacedKey(TARDIS.plugin, "sys_locked_independent")),
    UNLOCKED_TREE(new NamespacedKey(TARDIS.plugin, "sys_unlocked_tree")),
    UNLOCKED_BRANCH_DEPENDENT(new NamespacedKey(TARDIS.plugin, "sys_unlocked_branch_dependent")),
    UNLOCKED_BRANCH_INDEPENDENT(new NamespacedKey(TARDIS.plugin, "sys_unlocked_branch_independent")),
    UNLOCKED_DOWN(new NamespacedKey(TARDIS.plugin, "sys_unlocked_down")),
    UNLOCKED_END(new NamespacedKey(TARDIS.plugin, "sys_unlocked_end")),
    UNLOCKED_INDEPENDENT(new NamespacedKey(TARDIS.plugin, "sys_unlocked_independent"));

    private final NamespacedKey key;

    SystemTreeVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
