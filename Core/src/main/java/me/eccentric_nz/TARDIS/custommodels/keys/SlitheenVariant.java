package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SlitheenVariant {

    BUTTON_SLITHEEN(new NamespacedKey(TARDIS.plugin, "button_slitheen")),
    SLITHEEN_HEAD(new NamespacedKey(TARDIS.plugin, "slitheen_head")),
    SLITHEEN_SUIT(new NamespacedKey(TARDIS.plugin, "slitheen_suit")),
    SLITHEEN_CLAW_LEFT(new NamespacedKey(TARDIS.plugin, "slitheen_claw_left")),
    SLITHEEN_CLAW_RIGHT(new NamespacedKey(TARDIS.plugin, "slitheen_claw_right")),
    SLITHEEN_STATIC(new NamespacedKey(TARDIS.plugin, "slitheen_static"));

    private final NamespacedKey key;

    SlitheenVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

