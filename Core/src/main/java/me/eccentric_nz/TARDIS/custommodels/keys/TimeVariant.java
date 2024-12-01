package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum TimeVariant {

    MORNING(new NamespacedKey(TARDIS.plugin, "gui/clock/morning")),
    NOON(new NamespacedKey(TARDIS.plugin, "gui/clock/noon")),
    NIGHT(new NamespacedKey(TARDIS.plugin, "gui/clock/night")),
    MIDNIGHT(new NamespacedKey(TARDIS.plugin, "gui/clock/midnight")),
    SEVEN_AM(new NamespacedKey(TARDIS.plugin, "gui/clock/seven_am")),
    EIGHT_AM(new NamespacedKey(TARDIS.plugin, "gui/clock/eight_am")),
    NINE_AM(new NamespacedKey(TARDIS.plugin, "gui/clock/nine_am")),
    TEN_AM(new NamespacedKey(TARDIS.plugin, "gui/clock/ten_am")),
    ELEVEN_AM(new NamespacedKey(TARDIS.plugin, "gui/clock/eleven_am")),
    TWELVE_PM(new NamespacedKey(TARDIS.plugin, "gui/clock/twelve_pm")),
    ONE_PM(new NamespacedKey(TARDIS.plugin, "gui/clock/one_pm")),
    TWO_PM(new NamespacedKey(TARDIS.plugin, "gui/clock/two_pm")),
    THREE_PM(new NamespacedKey(TARDIS.plugin, "gui/clock/three_pm")),
    FOUR_PM(new NamespacedKey(TARDIS.plugin, "gui/clock/four_pm")),
    FIVE_PM(new NamespacedKey(TARDIS.plugin, "gui/clock/five_pm")),
    SIX_PM(new NamespacedKey(TARDIS.plugin, "gui/clock/six_pm")),
    SEVEN_PM(new NamespacedKey(TARDIS.plugin, "gui/clock/seven_pm")),
    EIGHT_PM(new NamespacedKey(TARDIS.plugin, "gui/clock/eight_pm")),
    NINE_PM(new NamespacedKey(TARDIS.plugin, "gui/clock/nine_pm")),
    TEN_PM(new NamespacedKey(TARDIS.plugin, "gui/clock/ten_pm")),
    ELEVEN_PM(new NamespacedKey(TARDIS.plugin, "gui/clock/eleven_pm")),
    TWELVE_AM(new NamespacedKey(TARDIS.plugin, "gui/clock/twelve_am")),
    ONE_AM(new NamespacedKey(TARDIS.plugin, "gui/clock/one_am")),
    TWO_AM(new NamespacedKey(TARDIS.plugin, "gui/clock/two_am")),
    THREE_AM(new NamespacedKey(TARDIS.plugin, "gui/clock/three_am")),
    FOUR_AM(new NamespacedKey(TARDIS.plugin, "gui/clock/four_am")),
    FIVE_AM(new NamespacedKey(TARDIS.plugin, "gui/clock/five_am"));

    private final NamespacedKey key;

    TimeVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
