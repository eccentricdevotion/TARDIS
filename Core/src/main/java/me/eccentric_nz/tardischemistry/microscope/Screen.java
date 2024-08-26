package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;

public enum Screen {

    EMPTY_SCREEN,
    ABOREAL_FIELD,
    BEETLE,
    BEETLE_HEAD,
    ENDEOSTIG,
    FEATHER,
    FRUCTICOSA_FLOWER,
    FUNGUS_1,
    FUNGUS_2,
    GIARDIA_MURIS,
    GLUE,
    INSECT_LEG,
    INSECT_PALP,
    LIZARD_PARASITE,
    POLYESTER,
    RAYON,
    RED_BLOOD_CELLS,
    SEED,
    SPONGE,
    TUBERCULOSIS_BACTERIA,
    WILD_STRAWBERRY;

    public int getCustomModelData() {
        return ordinal() + 10000;
    }

    public String getName() {
        return TARDISStringUtils.sentenceCase(toString());
    }
}
