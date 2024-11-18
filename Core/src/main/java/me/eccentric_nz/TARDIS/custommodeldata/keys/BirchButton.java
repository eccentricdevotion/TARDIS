package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BirchButton {

    HANDLES_OFF(new NamespacedKey(TARDIS.plugin, "handles/handles_off")),
    HANDLES_ON(new NamespacedKey(TARDIS.plugin, "handles/handles_on")),
    BOWTIE_WHITE(new NamespacedKey(TARDIS.plugin, "bowtie/bowtie_white")),
    BOWTIE_ORANGE(new NamespacedKey(TARDIS.plugin, "bowtie/bowtie_orange")),
    BOWTIE_MAGENTA(new NamespacedKey(TARDIS.plugin, "bowtie/bowtie_magenta")),
    BOWTIE_LIGHT_BLUE(new NamespacedKey(TARDIS.plugin, "bowtie/bowtie_light_blue")),
    BOWTIE_YELLOW(new NamespacedKey(TARDIS.plugin, "bowtie/bowtie_yellow")),
    BOWTIE_LIME(new NamespacedKey(TARDIS.plugin, "bowtie/bowtie_lime")),
    BOWTIE_PINK(new NamespacedKey(TARDIS.plugin, "bowtie/bowtie_pink")),
    BOWTIE_GRAY(new NamespacedKey(TARDIS.plugin, "bowtie/bowtie_gray")),
    BOWTIE_LIGHT_GRAY(new NamespacedKey(TARDIS.plugin, "bowtie/bowtie_light_gray")),
    BOWTIE_CYAN(new NamespacedKey(TARDIS.plugin, "bowtie/bowtie_cyan")),
    BOWTIE_PURPLE(new NamespacedKey(TARDIS.plugin, "bowtie/bowtie_purple")),
    BOWTIE_BLUE(new NamespacedKey(TARDIS.plugin, "bowtie/bowtie_blue")),
    BOWTIE_BROWN(new NamespacedKey(TARDIS.plugin, "bowtie/bowtie_brown")),
    BOWTIE_GREEN(new NamespacedKey(TARDIS.plugin, "bowtie/bowtie_green")),
    BOWTIE_RED(new NamespacedKey(TARDIS.plugin, "bowtie/bowtie_red")),
    BOWTIE_BLACK(new NamespacedKey(TARDIS.plugin, "bowtie/bowtie_black")),
    THREE_D_GLASSES(new NamespacedKey(TARDIS.plugin, "tardis/3d_glasses")),
    COMMUNICATOR(new NamespacedKey(TARDIS.plugin, "handles/communicator"));

    private final NamespacedKey key;

    BirchButton(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
