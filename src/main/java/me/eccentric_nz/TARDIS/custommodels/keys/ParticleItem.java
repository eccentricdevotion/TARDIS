package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ParticleItem {

    BLOCK(new NamespacedKey(TARDIS.plugin, "particle_block")),
    BLOCK_INFO(new NamespacedKey(TARDIS.plugin, "particle_block_info")),
    COLOUR(new NamespacedKey(TARDIS.plugin, "particle_colour")),
    COLOUR_INFO(new NamespacedKey(TARDIS.plugin, "particle_colour_info")),
    DENSITY(new NamespacedKey(TARDIS.plugin, "particle_density")),
    EFFECT(new NamespacedKey(TARDIS.plugin, "particle_effect")),
    EFFECT_INFO(new NamespacedKey(TARDIS.plugin, "particle_effect_info")),
    EFFECT_SELECTED(new NamespacedKey(TARDIS.plugin, "particle_effect_selected")),
    SHAPE(new NamespacedKey(TARDIS.plugin, "particle_shape")),
    SHAPE_INFO(new NamespacedKey(TARDIS.plugin, "particle_shape_info")),
    SHAPE_SELECTED(new NamespacedKey(TARDIS.plugin, "particle_shape_selected")),
    SPEED(new NamespacedKey(TARDIS.plugin, "particle_speed")),
    TEST(new NamespacedKey(TARDIS.plugin, "particle_test"));

    private final NamespacedKey key;

    ParticleItem(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
