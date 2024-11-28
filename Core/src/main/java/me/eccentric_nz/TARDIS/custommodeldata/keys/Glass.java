package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Glass {

    MONITOR_FRAME_LEFT(new NamespacedKey(TARDIS.plugin, "tardis/monitor_frame_left")),
    MONITOR_FRAME_MIDDLE(new NamespacedKey(TARDIS.plugin, "tardis/monitor_frame_middle")),
    MONITOR_FRAME_RIGHT(new NamespacedKey(TARDIS.plugin, "tardis/monitor_frame_right")),
    HELMET(new NamespacedKey(TARDIS.plugin, "space_suit/helmet")),
    TV(new NamespacedKey(TARDIS.plugin, "lazarus/tv")),
    OFF(new NamespacedKey(TARDIS.plugin, "block/lights/off")),
    ON(new NamespacedKey(TARDIS.plugin, "block/lights/on")),
    CLOISTER(new NamespacedKey(TARDIS.plugin, "block/lights/cloister")),
    VARIABLE(new NamespacedKey(TARDIS.plugin, "block/lights/variable")),
    BLUE(new NamespacedKey(TARDIS.plugin, "block/lights/blue")),
    GREEN(new NamespacedKey(TARDIS.plugin, "block/lights/green")),
    ORANGE(new NamespacedKey(TARDIS.plugin, "block/lights/orange")),
    PINK(new NamespacedKey(TARDIS.plugin, "block/lights/pink")),
    PURPLE(new NamespacedKey(TARDIS.plugin, "block/lights/purple")),
    YELLOW(new NamespacedKey(TARDIS.plugin, "block/lights/yellow")),
    BLUE_OFF(new NamespacedKey(TARDIS.plugin, "block/lights/blue_off")),
    GREEN_OFF(new NamespacedKey(TARDIS.plugin, "block/lights/green_off")),
    ORANGE_OFF(new NamespacedKey(TARDIS.plugin, "block/lights/orange_off")),
    PINK_OFF(new NamespacedKey(TARDIS.plugin, "block/lights/pink_off")),
    PURPLE_OFF(new NamespacedKey(TARDIS.plugin, "block/lights/purple_off")),
    YELLOW_OFF(new NamespacedKey(TARDIS.plugin, "block/lights/yellow_off"));

    private final NamespacedKey key;

    Glass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
