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
package me.eccentric_nz.TARDIS.database.tool;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class SQL_VALUES {

    public static final List<String> LIST = List.of(

            // achievements
            "(%s, '%s', '%s', '%s', '%s', %s)",
            // arched
            "('%s', '%s', %s)",
            // archive
            "(%s, '%s', '%s', '%s', %s, %s, %s, %s, '%s', '%s')",
            // areas
            "(%s, '%s', '%s', %s, %s, %s, %s, %s, %s, '%s', '%s', %s)",
            // area_locations
            "(%s, %s, '%s', %s, %s, %s)",
            // ars
            "(%s, %s, '%s', '%s', %s, %s, %s, '%s')",
            // artron_powered
            "(%s, %s, '%s')",
            // back
            "(%s, %s, '%s', %s, %s, %s, '%s', %s)",
            // bind
            "(%s, %s, %s, '%s', '%s')",
            // blocks
            "(%s, %s, '%s', '%s', %s)",
            // blueprint
            "(%s, '%s', '%s')",
            // camera
            "(%s, '%s', '%s')",
            // chameleon
            "(%s, %s, '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s)",
            // colour
            "(%s, %s, %s, %s, %s)",
            // chunks
            "(%s, %s, '%s', %s, %s)",
            // condenser
            "(%s, %s, '%s', %s)",
            // controls
            "(%s, %s, %s, '%s', %s)",
            // current
            "(%s, %s, '%s', %s, %s, %s, '%s', %s, '%s')",
            // custom_preset
            "(%s, %s, '%s')",
            // deaths
            "'%s', '%s', %s, %s, %s, '%s', %s",
            // destinations
            "(%s, %s, '%s', '%s', %s, %s, %s, '%s', '%s', '%s', %s, %s, %s, '%s', %s)",
            // dispersed
            "(%s, %s, '%s', '%s', %s, %s, %s)",
            // doors
            "(%s, %s, %s, '%s', '%s', %s)",
            // eyes
            "(%s, %s, %s, %s, %s)",
            // farming
            "(%s, %s, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
            // farming_prefs
            "(%s, '%s', %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
            // flight
            "(%s, '%s', %s, '%s', '%s', '%s')",
            // forcefield
            "('%s', '%s')",
            // games
            "(%s, %s, '%s', '%s', '%s', '%s', '%s')",
            // gardens
            "(%s, %s, '%s', %s, %s, %s, %s, %s)",
            // gravity_well
            "(%s, %s, '%s', %s, %s, %s)",
            // happy
            "(%s, %s, '%s')",
            // homes
            "(%s, %s, '%s', %s, %s, %s, '%s', %s, '%s')",
            // interactions
            "(%s, %s, '%s', '%s', %s)",
            // inventories
            "(%s, '%s', '%s', %s, '%s', '%s', '%s', '%s')",
            // junk
            "(%s, '%s', %s, '%s', '%s', '%s', '%s', '%s')",
            // lamps
            "(%s, %s, '%s', '%s', '%s', %s)",
            // light_prefs
            "(%s, %s, '%s', '%s', '%s', '%s', '%s')",
            // movers
            "('%s')",
            // next
            "(%s, %s, '%s', %s, %s, %s, '%s', %s)",
            // paper_bag
            "(%s, '%s', '%s', %s, '%s', %s, '%s', %s, '%s', %s)",
            // particle_prefs
            "(%s, '%s', '%s', '%s', %s, %s, '%s', '%s', %s)",
            // player prefs
            "(%s, '%s', '%s', '%s', %s, %s, %s, '%s', '%s', '%s', '%s', %s, %s, '%s', '%s', %s, %s, '%s', %s, %s, %s, '%s', '%s', %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, '%s', %s, %s, %s, %s, %s, '%s', %s, %s, %s, %s)",
            // plots
            "(%s, '%s', '%s', %s, %s, %s, '%s')",
            // portals
            "(%s, '%s', '%s', '%s', %s, %s)",
            // previewers
            "('%s', '%s', %s, %s, %s, %s, %s, '%s', %s)",
            // programs
            "(%s, '%s', '%s', '%s', '%s', %s)",
            // reminders
            "(%s, '%s', '%s', %s)",
            // room_progress
            "(%s, '%s', '%s', '%s', %s, %s, %s, %s, '%s', '%s', '%s')",
            // seeds
            "(%s, '%s', '%s', '%s', '%s')",
            // sensors
            "(%s, %s, '%s', '%s', '%s', '%s', '%s')",
            // siege
            "(%s, '%s', %s)",
            // skins
            "(%s, '%s', '%s')",
            // sonic
            "(%s, '%s', %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, '%s', '%s', %s)",
            // storage
            "(%s, %s, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
            // system_upgrades
            "(%s, %s, '%s', %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
            // t_count
            "(%s, '%s', '%s', %s, %s, %s)",
            // tag
            "(%s, '%s', %s)",
            // tardis
            "(%s, '%s', '%s', '%s', '%s', %s, '%s', %s, %s, '%s', '%s', %s, %s, %s, %s, %s, %s, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, %s, %s, %s, %s, %s, %s)",
            // transmats
            "(%s, %s, '%s', '%s', %s, %s, %s, %s)",
            // travel_stats
            "(%s, '%s', %s, '%s')",
            // traveled_to
            "(%s, %s)",
            // travellers
            "(%s, %s, '%s', '%s')",
            // vaults
            "(%s, %s, '%s', '%s', %s, %s, %s)",
            // thevoid
            "(%s)",
            // vortex
            "(%s, %s)",

            // shop - items
            "(%s, '%s', '%s', %s)",

            // vortex manipulator

            // beacons
            "(%s, '%s', '%s', '%s')",
            // manipulator
            "('%s', %s)",
            // messages
            "(%s, '%s', '%s', '%s', '%s', %s)",
            // saves
            "(%s, '%s', '%s', '%s', %s, %s, %s, %s, %s)",
            // follwers
            "('%s', '%s', '%s', %s, %s, '%s', %s)"
    );
}
