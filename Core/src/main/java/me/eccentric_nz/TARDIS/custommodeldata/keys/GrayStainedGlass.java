package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GrayStainedGlass {

    FOLDER(new NamespacedKey(TARDIS.plugin, "equipment/folder")),
    STARS(new NamespacedKey(TARDIS.plugin, "telescope/stars")),
    SUN(new NamespacedKey(TARDIS.plugin, "telescope/sun")),
    SUN_FLARE(new NamespacedKey(TARDIS.plugin, "telescope/sun_flare")),
    MERCURY(new NamespacedKey(TARDIS.plugin, "telescope/mercury")),
    VENUS(new NamespacedKey(TARDIS.plugin, "telescope/venus")),
    EARTH(new NamespacedKey(TARDIS.plugin, "telescope/earth")),
    MARS(new NamespacedKey(TARDIS.plugin, "telescope/mars")),
    MARS_SURFACE_1(new NamespacedKey(TARDIS.plugin, "telescope/mars_surface_1")),
    MARS_SURFACE_2(new NamespacedKey(TARDIS.plugin, "telescope/mars_surface_2")),
    PSYCHE_ASTEROID(new NamespacedKey(TARDIS.plugin, "telescope/psyche_asteroid")),
    JUPITER(new NamespacedKey(TARDIS.plugin, "telescope/jupiter")),
    SATURN(new NamespacedKey(TARDIS.plugin, "telescope/saturn")),
    SATURN_CLOSEUP(new NamespacedKey(TARDIS.plugin, "telescope/saturn_closeup")),
    NEPTUNE(new NamespacedKey(TARDIS.plugin, "telescope/neptune")),
    URANUS(new NamespacedKey(TARDIS.plugin, "telescope/uranus")),
    PLUTO(new NamespacedKey(TARDIS.plugin, "telescope/pluto")),
    THE_MOON(new NamespacedKey(TARDIS.plugin, "telescope/the_moon")),
    BLOOD_MOON(new NamespacedKey(TARDIS.plugin, "telescope/blood_moon")),
    PHOBOS(new NamespacedKey(TARDIS.plugin, "telescope/phobos")),
    IO(new NamespacedKey(TARDIS.plugin, "telescope/io")),
    EUROPA(new NamespacedKey(TARDIS.plugin, "telescope/europa")),
    GANYMEDE(new NamespacedKey(TARDIS.plugin, "telescope/ganymede")),
    CALISTO(new NamespacedKey(TARDIS.plugin, "telescope/calisto")),
    SOUTHERN_CROSS(new NamespacedKey(TARDIS.plugin, "telescope/southern_cross")),
    MILKY_WAY(new NamespacedKey(TARDIS.plugin, "telescope/milky_way")),
    ALPHA_CENTAURI(new NamespacedKey(TARDIS.plugin, "telescope/alpha_centauri")),
    CANOPUS(new NamespacedKey(TARDIS.plugin, "telescope/canopus")),
    BETELGEUSE(new NamespacedKey(TARDIS.plugin, "telescope/betelgeuse")),
    COMET(new NamespacedKey(TARDIS.plugin, "telescope/comet")),
    HALLEYS_COMET(new NamespacedKey(TARDIS.plugin, "telescope/halleys_comet")),
    KOHUTEK_COMET(new NamespacedKey(TARDIS.plugin, "telescope/kohutek_comet")),
    HORSEHEAD_NEBULA(new NamespacedKey(TARDIS.plugin, "telescope/horsehead_nebula")),
    CRAB_NEBULA(new NamespacedKey(TARDIS.plugin, "telescope/crab_nebula")),
    CATSEYE_NEBULA(new NamespacedKey(TARDIS.plugin, "telescope/catseye_nebula")),
    CYGNUS_LOOP_NEBULA(new NamespacedKey(TARDIS.plugin, "telescope/cygnus_loop_nebula")),
    STAR_BUBBLE_NEBULA(new NamespacedKey(TARDIS.plugin, "telescope/star_bubble_nebula")),
    TWIN_JET_NEBULA(new NamespacedKey(TARDIS.plugin, "telescope/twin_jet_nebula")),
    HELIX_NEBULA(new NamespacedKey(TARDIS.plugin, "telescope/helix_nebula")),
    SUPERMASSIVE_BLACK_HOLE(new NamespacedKey(TARDIS.plugin, "telescope/supermassive_black_hole")),
    GALAXY(new NamespacedKey(TARDIS.plugin, "telescope/galaxy")),
    ANOTHER_GALAXY(new NamespacedKey(TARDIS.plugin, "telescope/another_galaxy")),
    GAS_CLOUD(new NamespacedKey(TARDIS.plugin, "telescope/gas_cloud")),
    SUPERNOVA(new NamespacedKey(TARDIS.plugin, "telescope/supernova"));

    private final NamespacedKey key;

    GrayStainedGlass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
