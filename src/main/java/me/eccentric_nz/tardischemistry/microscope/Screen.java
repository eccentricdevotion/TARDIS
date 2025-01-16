package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.custommodels.keys.Sample;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.NamespacedKey;

public enum Screen {

    EMPTY_SCREEN(Sample.EMPTY_SCREEN.getKey()),
    ARBOREAL_FIELD(Sample.ARBOREAL_FIELD.getKey()),
    BEETLE(Sample.BEETLE.getKey()),
    BEETLE_HEAD(Sample.BEETLE_HEAD.getKey()),
    ENDEOSTIG(Sample.ENDEOSTIG.getKey()),
    FEATHER(Sample.FEATHER.getKey()),
    FRUCTICOSA_FLOWER(Sample.FRUCTICOSA_FLOWER.getKey()),
    FUNGUS_1(Sample.FUNGUS_1.getKey()),
    FUNGUS_2(Sample.FUNGUS_2.getKey()),
    GIARDIA_MURIS(Sample.GIARDIA_MURIS.getKey()),
    GLUE(Sample.GLUE.getKey()),
    INSECT_LEG(Sample.INSECT_LEG.getKey()),
    INSECT_PALP(Sample.INSECT_PALP.getKey()),
    LIZARD_PARASITE(Sample.LIZARD_PARASITE.getKey()),
    POLYESTER(Sample.POLYESTER.getKey()),
    RAYON(Sample.RAYON.getKey()),
    RED_BLOOD_CELLS(Sample.RED_BLOOD_CELLS.getKey()),
    SEED(Sample.SEED.getKey()),
    SPONGE(Sample.SPONGE.getKey()),
    TUBERCULOSIS_BACTERIA(Sample.TUBERCULOSIS_BACTERIA.getKey()),
    WILD_STRAWBERRY(Sample.WILD_STRAWBERRY.getKey());

    private final NamespacedKey model;

    Screen(NamespacedKey model) {
        this.model = model;
    }

    public NamespacedKey getModel() {
        return model;
    }

    public String getName() {
        return TARDISStringUtils.sentenceCase(toString());
    }
}
