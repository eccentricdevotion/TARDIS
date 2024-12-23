package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum EmptyChildVariant {

    BUTTON_EMPTY_CHILD(new NamespacedKey(TARDIS.plugin, "button_empty_child")),
    EMPTY_CHILD_HEAD(new NamespacedKey(TARDIS.plugin, "empty_child_head")),
    EMPTY_CHILD_STATIC(new NamespacedKey(TARDIS.plugin, "empty_child_static")),
    EMPTY_CHILD_MASK(new NamespacedKey(TARDIS.plugin, "empty_child_player_mask")),
    // TODO fix this
    EMPTY_CHILD_OVERLAY(new NamespacedKey(TARDIS.plugin, "empty_child_player_mask"));

    private final NamespacedKey key;

    EmptyChildVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

