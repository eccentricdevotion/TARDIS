package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.custommodels.keys.Astromony;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.NamespacedKey;

public enum ScopeView {

    STARS(Astromony.STARS.getKey()),
    SUN(Astromony.SUN.getKey()),
    SUN_FLARE(Astromony.SUN_FLARE.getKey()),
    MERCURY(Astromony.MERCURY.getKey()),
    VENUS(Astromony.VENUS.getKey()),
    EARTH(Astromony.EARTH.getKey()),
    MARS(Astromony.MARS.getKey()),
    MARS_SURFACE_1(Astromony.MARS_SURFACE_1.getKey()),
    MARS_SURFACE_2(Astromony.MARS_SURFACE_2.getKey()),
    PSYCHE_ASTEROID(Astromony.PSYCHE_ASTEROID.getKey()),
    JUPITER(Astromony.JUPITER.getKey()),
    SATURN(Astromony.SATURN.getKey()),
    SATURN_CLOSEUP(Astromony.SATURN_CLOSEUP.getKey()),
    NEPTUNE(Astromony.NEPTUNE.getKey()),
    URANUS(Astromony.URANUS.getKey()),
    PLUTO(Astromony.PLUTO.getKey()),
    THE_MOON(Astromony.THE_MOON.getKey()),
    BLOOD_MOON(Astromony.BLOOD_MOON.getKey()),
    PHOBOS(Astromony.PHOBOS.getKey()),
    IO(Astromony.IO.getKey()),
    EUROPA(Astromony.EUROPA.getKey()),
    GANYMEDE(Astromony.GANYMEDE.getKey()),
    CALISTO(Astromony.CALISTO.getKey()),
    SOUTHERN_CROSS(Astromony.SOUTHERN_CROSS.getKey()),
    MILKY_WAY(Astromony.MILKY_WAY.getKey()),
    ALPHA_CENTAURI(Astromony.ALPHA_CENTAURI.getKey()),
    CANOPUS(Astromony.CANOPUS.getKey()),
    BETELGEUSE(Astromony.BETELGEUSE.getKey()),
    COMET(Astromony.COMET.getKey()),
    HALLEYS_COMET(Astromony.HALLEYS_COMET.getKey()),
    KOHUTEK_COMET(Astromony.KOHUTEK_COMET.getKey()),
    HORSEHEAD_NEBULA(Astromony.HORSEHEAD_NEBULA.getKey()),
    CRAB_NEBULA(Astromony.CRAB_NEBULA.getKey()),
    CATSEYE_NEBULA(Astromony.CATSEYE_NEBULA.getKey()),
    CYGNUS_LOOP_NEBULA(Astromony.CYGNUS_LOOP_NEBULA.getKey()),
    STAR_BUBBLE_NEBULA(Astromony.STAR_BUBBLE_NEBULA.getKey()),
    TWIN_JET_NEBULA(Astromony.TWIN_JET_NEBULA.getKey()),
    HELIX_NEBULA(Astromony.HELIX_NEBULA.getKey()),
    SUPERMASSIVE_BLACK_HOLE(Astromony.SUPERMASSIVE_BLACK_HOLE.getKey()),
    GALAXY(Astromony.GALAXY.getKey()),
    ANOTHER_GALAXY(Astromony.ANOTHER_GALAXY.getKey()),
    GAS_CLOUD(Astromony.GAS_CLOUD.getKey()),
    SUPERNOVA(Astromony.SUPERNOVA.getKey());
    
    private final NamespacedKey model;

    ScopeView(NamespacedKey model) {
        this.model = model;
    }

    public NamespacedKey getModel() {
        return model;
    }

    public String getName() {
        return TARDISStringUtils.sentenceCase(toString());
    }
}
