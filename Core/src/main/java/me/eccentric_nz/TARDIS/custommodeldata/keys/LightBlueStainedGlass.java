package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LightBlueStainedGlass {


    TINT_LIGHT_BLUE(new NamespacedKey(TARDIS.plugin, "block/lights/tint_light_blue")),
    SCREEN(new NamespacedKey(TARDIS.plugin, "equipment/screen")),
		EMPTY_SCREEN(new NamespacedKey(TARDIS.plugin, "screen/empty_screen")),
            ARBOREAL_FIELD(new NamespacedKey(TARDIS.plugin, "screen/arboreal_field")),
            BEETLE(new NamespacedKey(TARDIS.plugin, "screen/beetle")),
            BEETLE_HEAD(new NamespacedKey(TARDIS.plugin, "screen/beetle_head")),
            ENDEOSTIG(new NamespacedKey(TARDIS.plugin, "screen/endeostig")),
            FEATHER(new NamespacedKey(TARDIS.plugin, "screen/feather")),
            FRUCTICOSA_FLOWER(new NamespacedKey(TARDIS.plugin, "screen/fructicosa_flower")),
            FUNGUS_1(new NamespacedKey(TARDIS.plugin, "screen/fungus_1")),
            FUNGUS_2(new NamespacedKey(TARDIS.plugin, "screen/fungus_2")),
            GIARDIA_MURIS(new NamespacedKey(TARDIS.plugin, "screen/giardia_muris")),
            GLUE(new NamespacedKey(TARDIS.plugin, "screen/glue")),
            INSECT_LEG(new NamespacedKey(TARDIS.plugin, "screen/insect_leg")),
            INSECT_PALP(new NamespacedKey(TARDIS.plugin, "screen/insect_palp")),
            LIZARD_PARASITE(new NamespacedKey(TARDIS.plugin, "screen/lizard_parasite")),
            POLYESTER(new NamespacedKey(TARDIS.plugin, "screen/polyester")),
            RAYON(new NamespacedKey(TARDIS.plugin, "screen/rayon")),
            RED_BLOOD_CELLS(new NamespacedKey(TARDIS.plugin, "screen/red_blood_cells")),
            SEED(new NamespacedKey(TARDIS.plugin, "screen/seed")),
            SPONGE(new NamespacedKey(TARDIS.plugin, "screen/sponge")),
            TUBERCULOSIS_BACTERIA(new NamespacedKey(TARDIS.plugin, "screen/tuberculosis_bacteria")),
            WILD_STRAWBERRY(new NamespacedKey(TARDIS.plugin, "screen/wild_strawberry")) ;

    private final NamespacedKey key;

    LightBlueStainedGlass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
