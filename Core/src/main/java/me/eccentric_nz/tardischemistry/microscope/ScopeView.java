package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;

public enum ScopeView {

    STARS,
    SUN,
    SUN_FLARE,
    MERCURY,
    VENUS,
    EARTH,
    MARS,
    MARS_SURFACE_1,
    MARS_SURFACE_2,
    PSYCHE_ASTEROID,
    JUPITER,
    SATURN,
    SATURN_CLOSEUP,
    NEPTUNE,
    URANUS,
    PLUTO,
    THE_MOON,
    BLOOD_MOON,
    PHOBOS,
    IO,
    EUROPA,
    GANYMEDE,
    CALISTO,
    SOUTHERN_CROSS,
    MILKY_WAY,
    ALPHA_CENTAURI,
    CANOPUS,
    BETELGEUSE,
    COMET,
    HALLEYS_COMET,
    KOHUTEK_COMET,
    HORSEHEAD_NEBULA,
    CRAB_NEBULA,
    CATSEYE_NEBULA,
    CYGNUS_LOOP_NEBULA,
    STAR_BUBBLE_NEBULA,
    TWIN_JET_NEBULA,
    HELIX_NEBULA,
    SUPERMASSIVE_BLACK_HOLE,
    GALAXY,
    ANOTHER_GALAXY,
    GAS_CLOUD,
    SUPERNOVA;

    public int getCustomModelData() {
        return ordinal() + 10000;
    }

    public String getName() {
        return TARDISStringUtils.sentenceCase(toString());
    }
}
