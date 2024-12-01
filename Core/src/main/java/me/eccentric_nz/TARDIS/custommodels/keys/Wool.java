package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Wool {

    BLANK(new NamespacedKey(TARDIS.plugin, "gui/map/blank")),
    BLUE_BOX(new NamespacedKey(TARDIS.plugin, "tardis/blue_box")),
    THE_MOMENT(new NamespacedKey(TARDIS.plugin, "the_moment")),
    UP(new NamespacedKey(TARDIS.plugin, "gui/arrow/up")),
    DOWN(new NamespacedKey(TARDIS.plugin, "gui/arrow/down")),
    LEFT(new NamespacedKey(TARDIS.plugin, "gui/bowl/left")),
    RIGHT(new NamespacedKey(TARDIS.plugin, "gui/bowl/right")),
    NOT_UPGRADED(new NamespacedKey(TARDIS.plugin, "sonic/not_upgraded")),
    PLACE(new NamespacedKey(TARDIS.plugin, "sonic/place")),
    WAITING(new NamespacedKey(TARDIS.plugin, "sonic/waiting")),
    COG(new NamespacedKey(TARDIS.plugin, "tardis/cog")),
    CLASSIC(new NamespacedKey(TARDIS.plugin, "lights/classic")),
    TENTH(new NamespacedKey(TARDIS.plugin, "lights/tenth")),
    ELEVENTH(new NamespacedKey(TARDIS.plugin, "lights/eleventh")),
    TWELFTH(new NamespacedKey(TARDIS.plugin, "lights/twelfth")),
    THIRTEENTH(new NamespacedKey(TARDIS.plugin, "lights/thirteenth")),
    CLASSIC_OFFSET(new NamespacedKey(TARDIS.plugin, "lights/classic_offset")),
    SCROLL_RIGHT(new NamespacedKey(TARDIS.plugin, "gui/map/scroll_right")),
    ENABLED(new NamespacedKey(TARDIS.plugin, "sonic/enabled")),
    HEXAGON(new NamespacedKey(TARDIS.plugin, "tardis/hexagon")),
    RECONFIGURE(new NamespacedKey(TARDIS.plugin, "gui/map/reconfigure")),
    SCROLL_LEFT(new NamespacedKey(TARDIS.plugin, "gui/map/scroll_left")),
    DISABLED(new NamespacedKey(TARDIS.plugin, "sonic/disabled")),
    LEVEL_BOTTOM(new NamespacedKey(TARDIS.plugin, "gui/map/level_bottom")),
    LEVEL_MAIN(new NamespacedKey(TARDIS.plugin, "gui/map/level_main")),
    LEVEL_TOP(new NamespacedKey(TARDIS.plugin, "gui/map/level_top")),
    ROUNDEL(new NamespacedKey(TARDIS.plugin, "tardis/roundel")),
    ROUNDEL_OFFSET(new NamespacedKey(TARDIS.plugin, "tardis/roundel_offset")),
    LEVEL_BOTTOM_ACTIVE(new NamespacedKey(TARDIS.plugin, "gui/map/level_bottom_active")),
    LEVEL_MAIN_ACTIVE(new NamespacedKey(TARDIS.plugin, "gui/map/level_main_active")),
    LEVEL_TOP_ACTIVE(new NamespacedKey(TARDIS.plugin, "gui/map/level_top_active"));

    private final NamespacedKey key;

    Wool(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
