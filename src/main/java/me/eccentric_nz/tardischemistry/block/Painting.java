package me.eccentric_nz.tardischemistry.block;

import java.util.Locale;

public enum Painting {

    AORTA,
    BEAKER,
    BONES,
    CHEMISTRY,
    EYE_CHART,
    LAVA,
    MAGNATISE,
    MELT,
    PARTICLES,
    PERIODIC_TABLE,
    PI,
    SPECTACLES,
    SULPHUR,
    WORLD,
    GALLIFREY_FALLS_NO_MORE,
    EXPLODING_TARDIS;

    public String getName() {
        return toString().toLowerCase(Locale.ROOT);
    }
}
