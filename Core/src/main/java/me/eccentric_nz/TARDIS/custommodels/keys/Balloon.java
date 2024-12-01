package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Balloon {

    WHITE_BALLOON(new NamespacedKey(TARDIS.plugin, "products/balloons/white_balloon")),
    ORANGE_BALLOON(new NamespacedKey(TARDIS.plugin, "products/balloons/orange_balloon")),
    MAGENTA_BALLOON(new NamespacedKey(TARDIS.plugin, "products/balloons/magenta_balloon")),
    LIGHT_BLUE_BALLOON(new NamespacedKey(TARDIS.plugin, "products/balloons/light_blue_balloon")),
    YELLOW_BALLOON(new NamespacedKey(TARDIS.plugin, "products/balloons/yellow_balloon")),
    LIME_BALLOON(new NamespacedKey(TARDIS.plugin, "products/balloons/lime_balloon")),
    PINK_BALLOON(new NamespacedKey(TARDIS.plugin, "products/balloons/pink_balloon")),
    GRAY_BALLOON(new NamespacedKey(TARDIS.plugin, "products/balloons/gray_balloon")),
    LIGHT_GRAY_BALLOON(new NamespacedKey(TARDIS.plugin, "products/balloons/light_gray_balloon")),
    CYAN_BALLOON(new NamespacedKey(TARDIS.plugin, "products/balloons/cyan_balloon")),
    PURPLE_BALLOON(new NamespacedKey(TARDIS.plugin, "products/balloons/purple_balloon")),
    BLUE_BALLOON(new NamespacedKey(TARDIS.plugin, "products/balloons/blue_balloon")),
    BROWN_BALLOON(new NamespacedKey(TARDIS.plugin, "products/balloons/brown_balloon")),
    GREEN_BALLOON(new NamespacedKey(TARDIS.plugin, "products/balloons/green_balloon")),
    RED_BALLOON(new NamespacedKey(TARDIS.plugin, "products/balloons/red_balloon")),
    BLACK_BALLOON(new NamespacedKey(TARDIS.plugin, "products/balloons/black_balloon"));

    private final NamespacedKey key;

    Balloon(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
