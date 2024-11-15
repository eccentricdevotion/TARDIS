package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LightBlueStainedGlass {


    TINT_LIGHT_BLUE(new NamespacedKey(TARDIS.plugin, "block/lights/tint_light_blue")),
    SCREEN(new NamespacedKey(TARDIS.plugin, "item/equipment/screen")),
		EMPTY_SCREEN(new NamespacedKey(TARDIS.plugin, "item/screen/empty_screen")),
            ARBOREAL_FIELD(new NamespacedKey(TARDIS.plugin, "item/screen/arboreal_field")),
            BEETLE(new NamespacedKey(TARDIS.plugin, "item/screen/beetle")),
            BEETLE_HEAD(new NamespacedKey(TARDIS.plugin, "item/screen/beetle_head")),
            ENDEOSTIG(new NamespacedKey(TARDIS.plugin, "item/screen/endeostig")),
            FEATHER(new NamespacedKey(TARDIS.plugin, "item/screen/feather")),
            FRUCTICOSA_FLOWER(new NamespacedKey(TARDIS.plugin, "item/screen/fructicosa_flower")),
            FUNGUS_1(new NamespacedKey(TARDIS.plugin, "item/screen/fungus_1")),
            FUNGUS_2(new NamespacedKey(TARDIS.plugin, "item/screen/fungus_2")),
            GIARDIA_MURIS(new NamespacedKey(TARDIS.plugin, "item/screen/giardia_muris")),
            GLUE(new NamespacedKey(TARDIS.plugin, "item/screen/glue")),
            INSECT_LEG(new NamespacedKey(TARDIS.plugin, "item/screen/insect_leg")),
            INSECT_PALP(new NamespacedKey(TARDIS.plugin, "item/screen/insect_palp")),
            LIZARD_PARASITE(new NamespacedKey(TARDIS.plugin, "item/screen/lizard_parasite")),
            POLYESTER(new NamespacedKey(TARDIS.plugin, "item/screen/polyester")),
            RAYON(new NamespacedKey(TARDIS.plugin, "item/screen/rayon")),
            RED_BLOOD_CELLS(new NamespacedKey(TARDIS.plugin, "item/screen/red_blood_cells")),
            SEED(new NamespacedKey(TARDIS.plugin, "item/screen/seed")),
            SPONGE(new NamespacedKey(TARDIS.plugin, "item/screen/sponge")),
            TUBERCULOSIS_BACTERIA(new NamespacedKey(TARDIS.plugin, "item/screen/tuberculosis_bacteria")),
            WILD_STRAWBERRY(new NamespacedKey(TARDIS.plugin, "item/screen/wild_strawberry")) ;

    private final NamespacedKey key;

    LightBlueStainedGlass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
