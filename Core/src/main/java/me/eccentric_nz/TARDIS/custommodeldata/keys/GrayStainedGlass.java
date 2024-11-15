package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GrayStainedGlass {


    TINT_GRAY(new NamespacedKey(TARDIS.plugin, "block/lights/tint_gray")),
    FOLDER(new NamespacedKey(TARDIS.plugin, "item/equipment/folder")),
    STARS(new NamespacedKey(TARDIS.plugin, "item/telescope/stars")),
    SUN(new NamespacedKey(TARDIS.plugin, "item/telescope/sun")),
    SUN_FLARE(new NamespacedKey(TARDIS.plugin, "item/telescope/sun_flare")),
    MERCURY(new NamespacedKey(TARDIS.plugin, "item/telescope/mercury")),
    VENUS(new NamespacedKey(TARDIS.plugin, "item/telescope/venus")),
    EARTH(new NamespacedKey(TARDIS.plugin, "item/telescope/earth")),
    MARS(new NamespacedKey(TARDIS.plugin, "item/telescope/mars")),
    MARS_SURFACE_1(new NamespacedKey(TARDIS.plugin, "item/telescope/mars_surface_1")),
    MARS_SURFACE_2(new NamespacedKey(TARDIS.plugin, "item/telescope/mars_surface_2")),
    PSYCHE_ASTEROID(new NamespacedKey(TARDIS.plugin, "item/telescope/psyche_asteroid")),
    JUPITER(new NamespacedKey(TARDIS.plugin, "item/telescope/jupiter")),
    SATURN(new NamespacedKey(TARDIS.plugin, "item/telescope/saturn")),
    SATURN_CLOSEUP(new NamespacedKey(TARDIS.plugin, "item/telescope/saturn_closeup")),
    NEPTUNE(new NamespacedKey(TARDIS.plugin, "item/telescope/neptune")),
    URANUS(new NamespacedKey(TARDIS.plugin, "item/telescope/uranus")),
    PLUTO(new NamespacedKey(TARDIS.plugin, "item/telescope/pluto")),
    THE_MOON(new NamespacedKey(TARDIS.plugin, "item/telescope/the_moon")),
    BLOOD_MOON(new NamespacedKey(TARDIS.plugin, "item/telescope/blood_moon")),
    PHOBOS(new NamespacedKey(TARDIS.plugin, "item/telescope/phobos")),
    IO(new NamespacedKey(TARDIS.plugin, "item/telescope/io")),
    EUROPA(new NamespacedKey(TARDIS.plugin, "item/telescope/europa")),
    GANYMEDE(new NamespacedKey(TARDIS.plugin, "item/telescope/ganymede")),
    CALISTO(new NamespacedKey(TARDIS.plugin, "item/telescope/calisto")),
    SOUTHERN_CROSS(new NamespacedKey(TARDIS.plugin, "item/telescope/southern_cross")),
    MILKY_WAY(new NamespacedKey(TARDIS.plugin, "item/telescope/milky_way")),
    ALPHA_CENTAURI(new NamespacedKey(TARDIS.plugin, "item/telescope/alpha_centauri")),
    CANOPUS(new NamespacedKey(TARDIS.plugin, "item/telescope/canopus")),
    BETELGEUSE(new NamespacedKey(TARDIS.plugin, "item/telescope/betelgeuse")),
    COMET(new NamespacedKey(TARDIS.plugin, "item/telescope/comet")),
    HALLEYS_COMET(new NamespacedKey(TARDIS.plugin, "item/telescope/halleys_comet")),
    KOHUTEK_COMET(new NamespacedKey(TARDIS.plugin, "item/telescope/kohutek_comet")),
    HORSEHEAD_NEBULA(new NamespacedKey(TARDIS.plugin, "item/telescope/horsehead_nebula")),
    CRAB_NEBULA(new NamespacedKey(TARDIS.plugin, "item/telescope/crab_nebula")),
    CATSEYE_NEBULA(new NamespacedKey(TARDIS.plugin, "item/telescope/catseye_nebula")),
    CYGNUS_LOOP_NEBULA(new NamespacedKey(TARDIS.plugin, "item/telescope/cygnus_loop_nebula")),
    STAR_BUBBLE_NEBULA(new NamespacedKey(TARDIS.plugin, "item/telescope/star_bubble_nebula")),
    TWIN_JET_NEBULA(new NamespacedKey(TARDIS.plugin, "item/telescope/twin_jet_nebula")),
    HELIX_NEBULA(new NamespacedKey(TARDIS.plugin, "item/telescope/helix_nebula")),
    SUPERMASSIVE_BLACK_HOLE(new NamespacedKey(TARDIS.plugin, "item/telescope/supermassive_black_hole")),
    GALAXY(new NamespacedKey(TARDIS.plugin, "item/telescope/galaxy")),
    ANOTHER_GALAXY(new NamespacedKey(TARDIS.plugin, "item/telescope/another_galaxy")),
    GAS_CLOUD(new NamespacedKey(TARDIS.plugin, "item/telescope/gas_cloud")),
    SUPERNOVA(new NamespacedKey(TARDIS.plugin, "item/telescope/supernova"));

    private final NamespacedKey key;

    GrayStainedGlass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
