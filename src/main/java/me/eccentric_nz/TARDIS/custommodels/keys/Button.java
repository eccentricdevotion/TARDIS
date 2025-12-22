/*
 * Copyright (C) 2025 eccentric_nz
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

public enum Button {

    ADAPT(new NamespacedKey(TARDIS.plugin, "button_adapt")),
    ADD_COMPANION(new NamespacedKey(TARDIS.plugin, "button_add_companion")),
    ADJACENT(new NamespacedKey(TARDIS.plugin, "button_adjacent")),
    ADMIN(new NamespacedKey(TARDIS.plugin, "button_admin")),
    AGE(new NamespacedKey(TARDIS.plugin, "button_age")),
    AREAS(new NamespacedKey(TARDIS.plugin, "button_areas")),
    ARS(new NamespacedKey(TARDIS.plugin, "button_ars")),
    ARTRON(new NamespacedKey(TARDIS.plugin, "button_artron")),
    AUTONOMOUS_PREFERENCES(new NamespacedKey(TARDIS.plugin, "button_auto_prefs")),
    BACK(new NamespacedKey(TARDIS.plugin, "button_back")),
    CANCEL(new NamespacedKey(TARDIS.plugin, "button_cancel")),
    CHAMELEON(new NamespacedKey(TARDIS.plugin, "button_chameleon")),
    CHARACTER(new NamespacedKey(TARDIS.plugin, "button_character")),
    COMPANION(new NamespacedKey(TARDIS.plugin, "button_companion")),
    COMPANION_ALL(new NamespacedKey(TARDIS.plugin, "button_companion_all")),
    COMPANION_LIST(new NamespacedKey(TARDIS.plugin, "button_companion_list")),
    CONSTRUCT(new NamespacedKey(TARDIS.plugin, "button_construct")),
    CUSTOM(new NamespacedKey(TARDIS.plugin, "button_custom")),
    DEACTIVATE(new NamespacedKey(TARDIS.plugin, "button_deactivate")),
    DEATH(new NamespacedKey(TARDIS.plugin, "button_death")),
    DELETE(new NamespacedKey(TARDIS.plugin, "button_delete")),
    DIRECTION(new NamespacedKey(TARDIS.plugin, "button_direction")),
    DNA(new NamespacedKey(TARDIS.plugin, "button_dna")),
    DOCTOR(new NamespacedKey(TARDIS.plugin, "button_doctor")),
    FARMING_PREFERENCES(new NamespacedKey(TARDIS.plugin, "button_farm_prefs")),
    FLIGHT_MODE(new NamespacedKey(TARDIS.plugin, "button_flight_mode")),
    HANDBRAKE(new NamespacedKey(TARDIS.plugin, "button_handbrake")),
    HIDE(new NamespacedKey(TARDIS.plugin, "button_hide")),
    HOSTILE(new NamespacedKey(TARDIS.plugin, "button_hostile")),
    INFO(new NamespacedKey(TARDIS.plugin, "button_info")),
    INVISIBLE(new NamespacedKey(TARDIS.plugin, "button_invisible")),
    LOCK(new NamespacedKey(TARDIS.plugin, "button_lock")),
    MASTER_OFF(new NamespacedKey(TARDIS.plugin, "button_master_off")),
    MASTER_ON(new NamespacedKey(TARDIS.plugin, "button_master_on")),
    MONSTER(new NamespacedKey(TARDIS.plugin, "button_monster")),
    NEUTRAL(new NamespacedKey(TARDIS.plugin, "button_neutral")),
    OPTIONS(new NamespacedKey(TARDIS.plugin, "button_opts")),
    PARTICLES(new NamespacedKey(TARDIS.plugin, "button_particles")),
    PASSIVE(new NamespacedKey(TARDIS.plugin, "button_passive")),
    PREFERENCES(new NamespacedKey(TARDIS.plugin, "button_prefs")),
    RANDOM(new NamespacedKey(TARDIS.plugin, "button_random")),
    REBUILD(new NamespacedKey(TARDIS.plugin, "button_rebuild")),
    REMOVE(new NamespacedKey(TARDIS.plugin, "button_remove")),
    RESTORE(new NamespacedKey(TARDIS.plugin, "button_restore")),
    ROOMS_WORLD(new NamespacedKey(TARDIS.plugin, "button_rooms_world")),
    SAVES(new NamespacedKey(TARDIS.plugin, "button_saves")),
    SCANNER(new NamespacedKey(TARDIS.plugin, "button_scanner")),
    SHORT(new NamespacedKey(TARDIS.plugin, "button_short")),
    SKINS(new NamespacedKey(TARDIS.plugin, "button_skins")),
    SONIC(new NamespacedKey(TARDIS.plugin, "button_sonic")),
    SYSTEM_UPGRADES(new NamespacedKey(TARDIS.plugin, "button_system_upgrades")),
    TARDIS_MAP(new NamespacedKey(TARDIS.plugin, "button_tardis_map")),
    TEMPORAL(new NamespacedKey(TARDIS.plugin, "button_temp")),
    TERMINAL(new NamespacedKey(TARDIS.plugin, "button_term")),
    THEME(new NamespacedKey(TARDIS.plugin, "button_theme")),
    THROTTLE(new NamespacedKey(TARDIS.plugin, "button_throttle")),
    TRANSMAT(new NamespacedKey(TARDIS.plugin, "button_transmat")),
    TWA(new NamespacedKey(TARDIS.plugin, "button_twa")),
    TYPE(new NamespacedKey(TARDIS.plugin, "button_type")),
    ZERO(new NamespacedKey(TARDIS.plugin, "button_zero"));

    private final NamespacedKey key;

    Button(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
