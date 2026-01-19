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

public enum Table {

    /*
    "ars", "back", "bind", "blocks", "chameleon", "chunks", "condenser", "controls", "current", "destinations", "dispersed", "doors", "eyes", "farming", "gardens", "gravity_well", "homes", "interactions", "junk", "lamps", "next", "room_progress", "tardis", "thevoid", "transmats", "travellers", "vaults"
     */

    // tardis
    achievements("a_id"),
    arched("uuid"),
    archive,
    areas,
    area_locations,
    ars(true),
    artron_powered("a_id", true),
    back(true),
    bind("bind_id", true),
    blocks("b_id", true),
    blueprint("bp_id"),
    camera("c_id"),
    chameleon(true),
    colour,
    chunks(true),
    condenser("c_id", true),
    controls("c_id", true),
    current(true),
    custom_preset("custom_id", true),
    deaths("uuid"),
    destinations("dest_id", true),
    dispersed("d_id", true),
    doors(true),
    eyes("eye_id", true),
    farming("farm_id", true),
    farming_prefs("farm_id"),
    flight("f_id"),
    forcefield("uuid"),
    games("game_id"),
    gardens("garden_id", true),
    gravity_well("g_id", true),
    happy(true),
    homes(true),
    interactions("i_id", true),
    inventories("id"),
    junk("id", true),
    lamps("l_id", true),
    light_prefs("lp_id"),
    movers("uuid"),
    next(true),
    paper_bag,
    particle_prefs("pp_id"),
    player_prefs("pp_id"),
    plots("plot_id"),
    portals(true),
    previewers("uuid"),
    programs,
    reminders,
    room_progress("progress_id", true),
    seeds,
    sensors(true),
    siege(true),
    skins,
    sonic,
    storage,
    system_upgrades("sys_id"),
    t_count("t_id"),
    tag,
    tardis("tardis_id", true),
    transmats(true),
    travel_stats("travel_stats_id"),
    traveled_to("uuid"),
    travellers,
    vaults("v_id", true),
    thevoid("tardis_id", true),
    vortex("tardis_id", true),
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
    private final boolean clean;

    Table(String rowId, boolean clean) {
        this.rowId = rowId;
        this.clean = clean;
    }

    Table(String rowId) {
        this.rowId = rowId;
        this.clean = false;
    }

    Table(boolean clean) {
        String name = this.toString();
        this.rowId = name.endsWith("s") ? name.substring(0, name.length() - 1) + "_id" : name + "_id";
        this.clean = clean;
    }

    Table() {
        String name = this.toString();
        this.rowId = name.endsWith("s") ? name.substring(0, name.length() - 1) + "_id" : name + "_id";
        this.clean = false;
    }

    public String getRowId() {
        return rowId;
    }

    public boolean shouldClean() {
        return clean;
    }
}
