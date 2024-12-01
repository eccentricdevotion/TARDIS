package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ParticleItem {

    BLOCK(new NamespacedKey(TARDIS.plugin, "gui/particle/block")),
    BLOCK_INFO(new NamespacedKey(TARDIS.plugin, "gui/particle/block_info")),
    COLOUR(new NamespacedKey(TARDIS.plugin, "gui/particle/colour")),
    COLOUR_INFO(new NamespacedKey(TARDIS.plugin, "gui/particle/colour_info")),
    DENSITY(new NamespacedKey(TARDIS.plugin, "gui/particle/density")),
    EFFECT(new NamespacedKey(TARDIS.plugin, "gui/particle/effect")),
    EFFECT_INFO(new NamespacedKey(TARDIS.plugin, "gui/particle/effect_info")),
    EFFECT_SELECTED(new NamespacedKey(TARDIS.plugin, "gui/particle/effect_selected")),
    SHAPE(new NamespacedKey(TARDIS.plugin, "gui/particle/shape")),
    SHAPE_INFO(new NamespacedKey(TARDIS.plugin, "gui/particle/shape_info")),
    SHAPE_SELECTED(new NamespacedKey(TARDIS.plugin, "gui/particle/shape_selected")),
    SPEED(new NamespacedKey(TARDIS.plugin, "gui/particle/speed")),
    TEST(new NamespacedKey(TARDIS.plugin, "gui/particle/test"));

    private final NamespacedKey key;

    ParticleItem(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
