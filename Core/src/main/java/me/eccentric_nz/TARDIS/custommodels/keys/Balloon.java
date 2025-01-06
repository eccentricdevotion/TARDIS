package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Balloon {

    WHITE_BALLOON(new NamespacedKey(TARDIS.plugin, "white_balloon")),
    ORANGE_BALLOON(new NamespacedKey(TARDIS.plugin, "orange_balloon")),
    MAGENTA_BALLOON(new NamespacedKey(TARDIS.plugin, "magenta_balloon")),
    LIGHT_BLUE_BALLOON(new NamespacedKey(TARDIS.plugin, "light_blue_balloon")),
    YELLOW_BALLOON(new NamespacedKey(TARDIS.plugin, "yellow_balloon")),
    LIME_BALLOON(new NamespacedKey(TARDIS.plugin, "lime_balloon")),
    PINK_BALLOON(new NamespacedKey(TARDIS.plugin, "pink_balloon")),
    GRAY_BALLOON(new NamespacedKey(TARDIS.plugin, "gray_balloon")),
    LIGHT_GRAY_BALLOON(new NamespacedKey(TARDIS.plugin, "light_gray_balloon")),
    CYAN_BALLOON(new NamespacedKey(TARDIS.plugin, "cyan_balloon")),
    PURPLE_BALLOON(new NamespacedKey(TARDIS.plugin, "purple_balloon")),
    BLUE_BALLOON(new NamespacedKey(TARDIS.plugin, "blue_balloon")),
    BROWN_BALLOON(new NamespacedKey(TARDIS.plugin, "brown_balloon")),
    GREEN_BALLOON(new NamespacedKey(TARDIS.plugin, "green_balloon")),
    RED_BALLOON(new NamespacedKey(TARDIS.plugin, "red_balloon")),
    BLACK_BALLOON(new NamespacedKey(TARDIS.plugin, "black_balloon"));

    private final NamespacedKey key;

    Balloon(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
