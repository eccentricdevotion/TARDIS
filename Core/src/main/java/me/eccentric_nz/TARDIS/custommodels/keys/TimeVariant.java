package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum TimeVariant {

    MORNING(new NamespacedKey(TARDIS.plugin, "clock_morning")),
    NOON(new NamespacedKey(TARDIS.plugin, "clock_noon")),
    NIGHT(new NamespacedKey(TARDIS.plugin, "clock_night")),
    MIDNIGHT(new NamespacedKey(TARDIS.plugin, "clock_midnight")),
    SEVEN_AM(new NamespacedKey(TARDIS.plugin, "clock_seven_am")),
    EIGHT_AM(new NamespacedKey(TARDIS.plugin, "clock_eight_am")),
    NINE_AM(new NamespacedKey(TARDIS.plugin, "clock_nine_am")),
    TEN_AM(new NamespacedKey(TARDIS.plugin, "clock_ten_am")),
    ELEVEN_AM(new NamespacedKey(TARDIS.plugin, "clock_eleven_am")),
    TWELVE_PM(new NamespacedKey(TARDIS.plugin, "clock_twelve_pm")),
    ONE_PM(new NamespacedKey(TARDIS.plugin, "clock_one_pm")),
    TWO_PM(new NamespacedKey(TARDIS.plugin, "clock_two_pm")),
    THREE_PM(new NamespacedKey(TARDIS.plugin, "clock_three_pm")),
    FOUR_PM(new NamespacedKey(TARDIS.plugin, "clock_four_pm")),
    FIVE_PM(new NamespacedKey(TARDIS.plugin, "clock_five_pm")),
    SIX_PM(new NamespacedKey(TARDIS.plugin, "clock_six_pm")),
    SEVEN_PM(new NamespacedKey(TARDIS.plugin, "clock_seven_pm")),
    EIGHT_PM(new NamespacedKey(TARDIS.plugin, "clock_eight_pm")),
    NINE_PM(new NamespacedKey(TARDIS.plugin, "clock_nine_pm")),
    TEN_PM(new NamespacedKey(TARDIS.plugin, "clock_ten_pm")),
    ELEVEN_PM(new NamespacedKey(TARDIS.plugin, "clock_eleven_pm")),
    TWELVE_AM(new NamespacedKey(TARDIS.plugin, "clock_twelve_am")),
    ONE_AM(new NamespacedKey(TARDIS.plugin, "clock_one_am")),
    TWO_AM(new NamespacedKey(TARDIS.plugin, "clock_two_am")),
    THREE_AM(new NamespacedKey(TARDIS.plugin, "clock_three_am")),
    FOUR_AM(new NamespacedKey(TARDIS.plugin, "clock_four_am")),
    FIVE_AM(new NamespacedKey(TARDIS.plugin, "clock_five_am"));

    private final NamespacedKey key;

    TimeVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
