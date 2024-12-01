package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Sparkler {

    SPARKLER_UNLIT(new NamespacedKey(TARDIS.plugin, "products/sparklers/sparkler_unlit")),
    SPARKLER_ORANGE(new NamespacedKey(TARDIS.plugin, "products/sparklers/sparkler_orange")),
    SPARKLER_BLUE(new NamespacedKey(TARDIS.plugin, "products/sparklers/sparkler_blue")),
    SPARKLER_GREEN(new NamespacedKey(TARDIS.plugin, "products/sparklers/sparkler_green")),
    SPARKLER_PURPLE(new NamespacedKey(TARDIS.plugin, "products/sparklers/sparkler_purple")),
    SPARKLER_RED(new NamespacedKey(TARDIS.plugin, "products/sparklers/sparkler_red")),
    SPARKLER_ORANGE_LIT(new NamespacedKey(TARDIS.plugin, "products/sparklers/sparkler_orange_lit")),
    SPARKLER_BLUE_LIT(new NamespacedKey(TARDIS.plugin, "products/sparklers/sparkler_blue_lit")),
    SPARKLER_GREEN_LIT(new NamespacedKey(TARDIS.plugin, "products/sparklers/sparkler_green_lit")),
    SPARKLER_PURPLE_LIT(new NamespacedKey(TARDIS.plugin, "products/sparklers/sparkler_purple_lit")),
    SPARKLER_RED_LIT(new NamespacedKey(TARDIS.plugin, "products/sparklers/sparkler_red_lit"));

    private final NamespacedKey key;

    Sparkler(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
