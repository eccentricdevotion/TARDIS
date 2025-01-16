package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Sample {

    EMPTY_SCREEN(new NamespacedKey(TARDIS.plugin, "screen_empty_screen")),
    ARBOREAL_FIELD(new NamespacedKey(TARDIS.plugin, "screen_arboreal_field")),
    BEETLE(new NamespacedKey(TARDIS.plugin, "screen_beetle")),
    BEETLE_HEAD(new NamespacedKey(TARDIS.plugin, "screen_beetle_head")),
    ENDEOSTIG(new NamespacedKey(TARDIS.plugin, "screen_endeostig")),
    FEATHER(new NamespacedKey(TARDIS.plugin, "screen_feather")),
    FRUCTICOSA_FLOWER(new NamespacedKey(TARDIS.plugin, "screen_fructicosa_flower")),
    FUNGUS_1(new NamespacedKey(TARDIS.plugin, "screen_fungus_1")),
    FUNGUS_2(new NamespacedKey(TARDIS.plugin, "screen_fungus_2")),
    GIARDIA_MURIS(new NamespacedKey(TARDIS.plugin, "screen_giardia_muris")),
    GLUE(new NamespacedKey(TARDIS.plugin, "screen_glue")),
    INSECT_LEG(new NamespacedKey(TARDIS.plugin, "screen_insect_leg")),
    INSECT_PALP(new NamespacedKey(TARDIS.plugin, "screen_insect_palp")),
    LIZARD_PARASITE(new NamespacedKey(TARDIS.plugin, "screen_lizard_parasite")),
    POLYESTER(new NamespacedKey(TARDIS.plugin, "screen_polyester")),
    RAYON(new NamespacedKey(TARDIS.plugin, "screen_rayon")),
    RED_BLOOD_CELLS(new NamespacedKey(TARDIS.plugin, "screen_red_blood_cells")),
    SEED(new NamespacedKey(TARDIS.plugin, "screen_seed")),
    SPONGE(new NamespacedKey(TARDIS.plugin, "screen_sponge")),
    TUBERCULOSIS_BACTERIA(new NamespacedKey(TARDIS.plugin, "screen_tuberculosis_bacteria")),
    WILD_STRAWBERRY(new NamespacedKey(TARDIS.plugin, "screen_wild_strawberry"));

    private final NamespacedKey key;

    Sample(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
