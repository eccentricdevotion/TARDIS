/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Astromony {

    STARS(new NamespacedKey(TARDIS.plugin, "telescope_stars")),
    SUN(new NamespacedKey(TARDIS.plugin, "telescope_sun")),
    SUN_FLARE(new NamespacedKey(TARDIS.plugin, "telescope_sun_flare")),
    MERCURY(new NamespacedKey(TARDIS.plugin, "telescope_mercury")),
    VENUS(new NamespacedKey(TARDIS.plugin, "telescope_venus")),
    EARTH(new NamespacedKey(TARDIS.plugin, "telescope_earth")),
    MARS(new NamespacedKey(TARDIS.plugin, "telescope_mars")),
    MARS_SURFACE_1(new NamespacedKey(TARDIS.plugin, "telescope_mars_surface_1")),
    MARS_SURFACE_2(new NamespacedKey(TARDIS.plugin, "telescope_mars_surface_2")),
    PSYCHE_ASTEROID(new NamespacedKey(TARDIS.plugin, "telescope_psyche_asteroid")),
    JUPITER(new NamespacedKey(TARDIS.plugin, "telescope_jupiter")),
    SATURN(new NamespacedKey(TARDIS.plugin, "telescope_saturn")),
    SATURN_CLOSEUP(new NamespacedKey(TARDIS.plugin, "telescope_saturn_closeup")),
    NEPTUNE(new NamespacedKey(TARDIS.plugin, "telescope_neptune")),
    URANUS(new NamespacedKey(TARDIS.plugin, "telescope_uranus")),
    PLUTO(new NamespacedKey(TARDIS.plugin, "telescope_pluto")),
    THE_MOON(new NamespacedKey(TARDIS.plugin, "telescope_the_moon")),
    BLOOD_MOON(new NamespacedKey(TARDIS.plugin, "telescope_blood_moon")),
    PHOBOS(new NamespacedKey(TARDIS.plugin, "telescope_phobos")),
    IO(new NamespacedKey(TARDIS.plugin, "telescope_io")),
    EUROPA(new NamespacedKey(TARDIS.plugin, "telescope_europa")),
    GANYMEDE(new NamespacedKey(TARDIS.plugin, "telescope_ganymede")),
    CALISTO(new NamespacedKey(TARDIS.plugin, "telescope_calisto")),
    SOUTHERN_CROSS(new NamespacedKey(TARDIS.plugin, "telescope_southern_cross")),
    MILKY_WAY(new NamespacedKey(TARDIS.plugin, "telescope_milky_way")),
    ALPHA_CENTAURI(new NamespacedKey(TARDIS.plugin, "telescope_alpha_centauri")),
    CANOPUS(new NamespacedKey(TARDIS.plugin, "telescope_canopus")),
    BETELGEUSE(new NamespacedKey(TARDIS.plugin, "telescope_betelgeuse")),
    COMET(new NamespacedKey(TARDIS.plugin, "telescope_comet")),
    HALLEYS_COMET(new NamespacedKey(TARDIS.plugin, "telescope_halleys_comet")),
    KOHUTEK_COMET(new NamespacedKey(TARDIS.plugin, "telescope_kohutek_comet")),
    HORSEHEAD_NEBULA(new NamespacedKey(TARDIS.plugin, "telescope_horsehead_nebula")),
    CRAB_NEBULA(new NamespacedKey(TARDIS.plugin, "telescope_crab_nebula")),
    CATSEYE_NEBULA(new NamespacedKey(TARDIS.plugin, "telescope_catseye_nebula")),
    CYGNUS_LOOP_NEBULA(new NamespacedKey(TARDIS.plugin, "telescope_cygnus_loop_nebula")),
    STAR_BUBBLE_NEBULA(new NamespacedKey(TARDIS.plugin, "telescope_star_bubble_nebula")),
    TWIN_JET_NEBULA(new NamespacedKey(TARDIS.plugin, "telescope_twin_jet_nebula")),
    HELIX_NEBULA(new NamespacedKey(TARDIS.plugin, "telescope_helix_nebula")),
    SUPERMASSIVE_BLACK_HOLE(new NamespacedKey(TARDIS.plugin, "telescope_supermassive_black_hole")),
    GALAXY(new NamespacedKey(TARDIS.plugin, "telescope_galaxy")),
    ANOTHER_GALAXY(new NamespacedKey(TARDIS.plugin, "telescope_another_galaxy")),
    GAS_CLOUD(new NamespacedKey(TARDIS.plugin, "telescope_gas_cloud")),
    SUPERNOVA(new NamespacedKey(TARDIS.plugin, "telescope_supernova"));

    private final NamespacedKey key;

    Astromony(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
