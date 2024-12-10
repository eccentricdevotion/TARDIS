package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BowTieVariant {

    // TODO change to a different material - WARPED_BUTTON?
    BOWTIE_WHITE(new NamespacedKey(TARDIS.plugin, "bowtie_white")),
    BOWTIE_ORANGE(new NamespacedKey(TARDIS.plugin, "bowtie_orange")),
    BOWTIE_MAGENTA(new NamespacedKey(TARDIS.plugin, "bowtie_magenta")),
    BOWTIE_LIGHT_BLUE(new NamespacedKey(TARDIS.plugin, "bowtie_light_blue")),
    BOWTIE_YELLOW(new NamespacedKey(TARDIS.plugin, "bowtie_yellow")),
    BOWTIE_LIME(new NamespacedKey(TARDIS.plugin, "bowtie_lime")),
    BOWTIE_PINK(new NamespacedKey(TARDIS.plugin, "bowtie_pink")),
    BOWTIE_GRAY(new NamespacedKey(TARDIS.plugin, "bowtie_gray")),
    BOWTIE_LIGHT_GRAY(new NamespacedKey(TARDIS.plugin, "bowtie_light_gray")),
    BOWTIE_CYAN(new NamespacedKey(TARDIS.plugin, "bowtie_cyan")),
    BOWTIE_PURPLE(new NamespacedKey(TARDIS.plugin, "bowtie_purple")),
    BOWTIE_BLUE(new NamespacedKey(TARDIS.plugin, "bowtie_blue")),
    BOWTIE_BROWN(new NamespacedKey(TARDIS.plugin, "bowtie_brown")),
    BOWTIE_GREEN(new NamespacedKey(TARDIS.plugin, "bowtie_green")),
    BOWTIE_RED(new NamespacedKey(TARDIS.plugin, "bowtie_red")),
    BOWTIE_BLACK(new NamespacedKey(TARDIS.plugin, "bowtie_black"));

    private final NamespacedKey key;

    BowTieVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
