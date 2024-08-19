/*
 * Copyright (C) 2024 eccentric_nz
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

public enum Table {

    // tardis
    achievements("a_id"),
    arched("uuid"),
    archive,
    areas,
    area_locations,
    ars,
    artron_powered("a_id"),
    back,
    bind("bind_id"),
    blocks("b_id"),
    blueprint("bp_id"),
    camera("c_id"),
    chameleon,
    colour,
    chunks,
    condenser("c_id"),
    controls("c_id"),
    current,
    destinations("dest_id"),
    dispersed("d_id"),
    doors,
    eyes("eye_id"),
    farming("farm_id"),
    farming_prefs("farm_id"),
    flight("f_id"),
    forcefield("uuid"),
    gardens("garden_id"),
    gravity_well("g_id"),
    homes,
    interactions("i_id"),
    inventories("id"),
    junk("id"),
    lamps("l_id"),
    light_prefs("lp_id"),
    movers("uuid"),
    next,
    paper_bag,
    particle_prefs("pp_id"),
    player_prefs("pp_id"),
    portals,
    programs,
    reminders,
    room_progress("progress_id"),
    seeds,
    sensors,
    siege,
    skins,
    sonic,
    storage,
    system_upgrades("sys_id"),
    t_count("t_id"),
    tag,
    tardis("tardis_id"),
    transmats,
    travel_stats("travel_stats_id"),
    traveled_to("uuid"),
    travellers,
    vaults("v_id"),
    thevoid("tardis_id"),
    vortex("tardis_id"),
    // shop
    items,
    // vortex manipulator
    beacons,
    manipulator("uuid"),
    messages,
    saves,
    // weeping angels
    followers("uuid");

    private final String rowId;

    Table(String rowId) {
        this.rowId = rowId;
    }

    Table() {
        String name = this.toString();
        this.rowId = name.endsWith("s") ? name.substring(0, name.length() - 1) + "_id" : name + "_id";
    }

    public String getRowId() {
        return rowId;
    }
}
