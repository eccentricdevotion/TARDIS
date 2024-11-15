package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Clock {

    MORNING(new NamespacedKey(TARDIS.plugin, "item/gui/clock/morning")),
    NOON(new NamespacedKey(TARDIS.plugin, "item/gui/clock/noon")),
    NIGHT(new NamespacedKey(TARDIS.plugin, "item/gui/clock/night")),
    MIDNIGHT(new NamespacedKey(TARDIS.plugin, "item/gui/clock/midnight")),
    SEVEN_AM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/seven_am")),
    EIGHT_AM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/eight_am")),
    NINE_AM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/nine_am")),
    TEN_AM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/ten_am")),
    ELEVEN_AM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/eleven_am")),
    TWELVE_PM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/twelve_pm")),
    ONE_PM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/one_pm")),
    TWO_PM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/two_pm")),
    THREE_PM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/three_pm")),
    FOUR_PM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/four_pm")),
    FIVE_PM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/five_pm")),
    SIX_PM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/six_pm")),
    SEVEN_PM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/seven_pm")),
    EIGHT_PM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/eight_pm")),
    NINE_PM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/nine_pm")),
    TEN_PM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/ten_pm")),
    ELEVEN_PM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/eleven_pm")),
    TWELVE_AM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/twelve_am")),
    ONE_AM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/one_am")),
    TWO_AM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/two_am")),
    THREE_AM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/three_am")),
    FOUR_AM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/four_am")),
    FIVE_AM(new NamespacedKey(TARDIS.plugin, "item/gui/clock/five_am")),
    FOB_WATCH(new NamespacedKey(TARDIS.plugin, "item/tardis/fob_watch")),
    VORTEX_MANIPULATOR(new NamespacedKey(TARDIS.plugin, "item/tardis/vortex_manipulator"));

    private final NamespacedKey key;

    Clock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
