package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum WeepingAngelVariant {

    BUTTON_WEEPING_ANGEL(new NamespacedKey(TARDIS.plugin, "button_weeping_angel")),
    WEEPING_ANGEL_HEAD(new NamespacedKey(TARDIS.plugin, "weeping_angel_head")),
    WEEPING_ANGEL_PLAYER_HEAD(new NamespacedKey(TARDIS.plugin, "weeping_angel_player_head")),
    WEEPING_ANGEL_STATIC(new NamespacedKey(TARDIS.plugin, "weeping_angel_static"));

    private final NamespacedKey key;

    WeepingAngelVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
