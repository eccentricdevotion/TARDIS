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
public class SQL_CREATE {

    public static final List<String> LIST = List.of(

            "CREATE TABLE IF NOT EXISTS %sachievements (a_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', player varchar(32) DEFAULT '', `name` varchar(32) DEFAULT '', amount text, completed int(1) DEFAULT '0', PRIMARY KEY (a_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sarched (uuid varchar(48) DEFAULT '', arch_name varchar(16) DEFAULT '', arch_time bigint(20) DEFAULT '0', PRIMARY KEY (uuid)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sarchive (archive_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', `name` varchar(32) DEFAULT '', console_size varchar(16) DEFAULT '', beacon int(1) DEFAULT '0', lanterns int(1) DEFAULT '0', `use` int(1) DEFAULT '0', `y` int(3) DEFAULT '64', `data` text, description varchar(256), PRIMARY KEY (archive_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sareas (area_id int(11) NOT NULL AUTO_INCREMENT, area_name varchar(64) DEFAULT '', world varchar(64) DEFAULT '', minx int(7) DEFAULT '0', minz int(7) DEFAULT '0', maxx int(7) DEFAULT '0', maxz int(7) DEFAULT '0', y int(3) DEFAULT '0', parking_distance int(2) DEFAULT '2', invisibility varchar(32) DEFAULT 'ALLOW', direction varchar(5) DEFAULT '', grid int(1) DEFAULT '1', PRIMARY KEY (area_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sarea_locations (area_location_id int(11) NOT NULL AUTO_INCREMENT, area_id int(11), world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', PRIMARY KEY (area_location_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sars (ars_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', uuid varchar(48) DEFAULT '', player varchar(32) DEFAULT '', ars_x_east int(2) DEFAULT '2', ars_z_south int(2) DEFAULT '2', ars_y_layer int(1) DEFAULT '1', json text, PRIMARY KEY (ars_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sartron_powered (a_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', location varchar(512) DEFAULT '', PRIMARY KEY (a_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sback (back_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', submarine int(1) DEFAULT '0', PRIMARY KEY (back_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sbind (bind_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', `type` int(1) DEFAULT '0', location varchar(512) DEFAULT '', name varchar(32) DEFAULT '', PRIMARY KEY (bind_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sblocks (b_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', location varchar(512) DEFAULT '', `data` text, police_box int(1) DEFAULT '0', PRIMARY KEY (b_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sblueprint (bp_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', permission varchar(48) DEFAULT '', PRIMARY KEY (bp_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %scamera (c_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', location varchar(512) DEFAULT '', PRIMARY KEY (c_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %schameleon (chameleon_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', blueprintData text, stainData text, glassData text, line1 varchar(48) DEFAULT '', line2 varchar(48) DEFAULT '', line3 varchar(48) DEFAULT '', line4 varchar(48) DEFAULT '', active int(1) DEFAULT '0', PRIMARY KEY (chameleon_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %scolour (colour_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', red int(3) DEFAULT '0', green int(3) DEFAULT '0', blue int(3) DEFAULT '0', PRIMARY KEY (colour_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %schunks (chunk_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '0', x int(7) DEFAULT '0', z int(7) DEFAULT '0', PRIMARY KEY (chunk_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %scondenser (c_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', block_data varchar(32) DEFAULT '', block_count int(11) DEFAULT '0', PRIMARY KEY (c_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %scontrols (c_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', `type` int(3) DEFAULT '0', location varchar(512) DEFAULT '', secondary int(1) DEFAULT '0', PRIMARY KEY (c_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %scurrent (current_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', submarine int(1) DEFAULT '0', biome varchar(64) DEFAULT '', PRIMARY KEY (current_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %scustom_preset (custom_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', preset varchar(64) DEFAULT 'custom', PRIMARY KEY (custom_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sdeaths (uuid varchar(48) NOT NULL, world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', submarine int(1) DEFAULT '0', PRIMARY KEY (uuid)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sdestinations (dest_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', dest_name varchar(64) DEFAULT '', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', preset varchar(32) DEFAULT '', bind varchar(64) DEFAULT '', `type` int(3) DEFAULT '0', submarine int(1) DEFAULT '0', slot int(1) DEFAULT '-1', icon varchar(64) DEFAULT '', autonomous int(1) DEFAULT '0', PRIMARY KEY (dest_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sdispersed (d_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', tardis_id int(11) DEFAULT '0', PRIMARY KEY (d_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sdoors (door_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', door_type int(1) DEFAULT '0', door_location varchar(512) DEFAULT '', door_direction varchar(5) DEFAULT 'SOUTH', locked int(1) DEFAULT '0', PRIMARY KEY (door_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %seyes (eye_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', capacitors int(1) DEFAULT '1', damaged int(1) DEFAULT '0', task int(11) DEFAULT '-1', PRIMARY KEY (eye_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sfarming (farm_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', allay varchar(512) DEFAULT '', apiary varchar(512) DEFAULT '', aquarium varchar(512) DEFAULT '', bamboo varchar(512) DEFAULT '', birdcage varchar(512) DEFAULT '', farm varchar(512) DEFAULT '', geode varchar(512) DEFAULT '', happy varchar(512) DEFAULT '', hutch varchar(512) DEFAULT '', igloo varchar(512) DEFAULT '', iistubil varchar(512) DEFAULT '', lava varchar(512) DEFAULT '', mangrove varchar(512) DEFAULT '', nautilus varchar(512) DEFAULT '', pen varchar(512) DEFAULT '', stable varchar(512) DEFAULT '', stall varchar(512) DEFAULT '', village varchar(512) DEFAULT '', PRIMARY KEY (farm_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sfarming_prefs (farm_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', allay int(1) DEFAULT '1', apiary int(1) DEFAULT '1', aquarium int(1) DEFAULT '1', bamboo int(1) DEFAULT '1', birdcage int(1) DEFAULT '1', farm int(1) DEFAULT '1', geode int(1) DEFAULT '1', happy int(1) DEFAULT '1', hutch int(1) DEFAULT '1', igloo int(1) DEFAULT '1', iistubil int(1) DEFAULT '1', lava int(1) DEFAULT '1', mangrove int(1) DEFAULT '1', nautilus int(1) DEFAULT '1', pen int(1) DEFAULT '1', stable int(1) DEFAULT '1', stall int(1) DEFAULT '1', village int(1) DEFAULT '1', PRIMARY KEY (farm_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sflight (f_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', tardis_id int(11) DEFAULT '0', location varchar(512) DEFAULT '', stand varchar(48) DEFAULT '', display varchar(48) DEFAULT '', PRIMARY KEY (f_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sforcefield (uuid varchar(48) NOT NULL DEFAULT '', location varchar(512) DEFAULT '', PRIMARY KEY (uuid)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sgames (game_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', player_location varchar(512) DEFAULT '', tetris_board varchar(512) DEFAULT '', tetris_sign varchar(512) DEFAULT '', pong_display varchar(512) DEFAULT '', pong_uuids text, PRIMARY KEY (game_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sgardens (garden_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '', minx int(7) DEFAULT '0', maxx int(7) DEFAULT '0', y int(3) DEFAULT '0', minz int(7) DEFAULT '0', maxz int(7) DEFAULT '0', PRIMARY KEY (garden_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sgravity_well (g_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', location varchar(512) DEFAULT '', direction int(2) DEFAULT '0', distance int(3) DEFAULT '11', velocity float DEFAULT '0.5', PRIMARY KEY (g_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %shappy (happy_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', slots varchar(48) DEFAULT '0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0', PRIMARY KEY (happy_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %shomes (home_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', submarine int(1) DEFAULT '0', preset varchar(64) DEFAULT '', PRIMARY KEY (home_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sinteractions (i_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', uuid varchar(48) DEFAULT '', control varchar(48) DEFAULT '', state int(2) DEFAULT '0', PRIMARY KEY (i_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sinventories (id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', player varchar(24) DEFAULT '', arch int(1), inventory text, armour text, attributes text, armour_attributes text, PRIMARY KEY (id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sjunk (id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', tardis_id int(11) DEFAULT '0', save_sign varchar(512) DEFAULT '', handbrake varchar(512) DEFAULT '', wall varchar(64) DEFAULT 'ORANGE_WOOL', floor varchar(64) DEFAULT 'LIGHT_GRAY_WOOL', preset varchar(32) DEFAULT '', PRIMARY KEY (id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %slamps (l_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', location varchar(512) DEFAULT '', material_on varchar(64) DEFAULT '', material_off varchar(64) DEFAULT '', percentage float DEFAULT '1.0', PRIMARY KEY (l_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %slight_prefs (lp_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', light varchar(32) DEFAULT '', material varchar(64) DEFAULT '', pattern varchar(512) DEFAULT 'BLUE:GREEN:ORANGE:PINK:PURPLE:YELLOW:RED:WHITE:BLACK', delays varchar(64) DEFAULT '20:20:20:20:20:20:20:20:20', levels varchar(64) DEFAULT '15:15:15:15:15:15:15:15:15', PRIMARY KEY (lp_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %smovers (uuid varchar(48) NOT NULL, PRIMARY KEY (uuid)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %snext (next_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', submarine int(1) DEFAULT '0', PRIMARY KEY (next_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %spaper_bag (paper_bag_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', flavour_1 varchar(12) DEFAULT '', amount_1 int(2) DEFAULT '0', flavour_2 varchar(12) DEFAULT '', amount_2 int(2) DEFAULT '0', flavour_3 varchar(12) DEFAULT '', amount_3 int(2) DEFAULT '0', flavour_4 varchar(12) DEFAULT '', amount_4 int(2) DEFAULT '0', PRIMARY KEY (paper_bag_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sparticle_prefs (pp_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', `effect` varchar(32) DEFAULT 'ASH', `shape` varchar(32) DEFAULT 'RANDOM', `density` int(2) DEFAULT '16', `speed` int(2) DEFAULT '0', colour varchar(16) DEFAULT 'WHITE', block varchar(48) DEFAULT 'STONE', particles_on int(1) DEFAULT '0', PRIMARY KEY (pp_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %splayer_prefs (pp_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', player varchar(32), `key_item` varchar(32) DEFAULT '', sfx_on int(1) DEFAULT '0', quotes_on int(1) DEFAULT '0', artron_level int(11) DEFAULT '0', wall varchar(64) DEFAULT 'ORANGE_WOOL', floor varchar(64) DEFAULT 'LIGHT_GRAY_WOOL', siege_wall varchar(64) DEFAULT 'GRAY_TERRACOTTA', siege_floor varchar(64) DEFAULT 'BLACK_TERRACOTTA', announce_repeaters_on int(1) DEFAULT '0', auto_on int(1) DEFAULT '0', auto_type varchar(32) DEFAULT 'CLOSEST', auto_default varchar(12) DEFAULT 'HOME', beacon_on int(1) DEFAULT '1', hads_on int(1) DEFAULT '1', hads_type varchar(12) DEFAULT 'DISPLACEMENT', build_on int(1) DEFAULT '1', close_gui_on int(1) DEFAULT '1', eps_on int(1) DEFAULT '0', eps_message text, language varchar(32) DEFAULT 'ENGLISH', submarine_on int(1) DEFAULT '0', dnd_on int(1) DEFAULT '0', dynamic_lamps_on int(1) DEFAULT '0', minecart_on int(1) DEFAULT '0', renderer_on int(1) DEFAULT '1', sign_on int(1) DEFAULT '1', telepathy_on int(1) DEFAULT '0', travelbar_on int(1) DEFAULT '0', info_on int(1) DEFAULT '0', farm_on int(1) DEFAULT '1', lights varchar(32) DEFAULT 'TENTH', auto_siege_on int(1) DEFAULT '0', flying_mode int(1) DEFAULT '1', throttle int(1) DEFAULT '4', auto_powerup_on int(1) DEFAULT '0', auto_rescue_on int(1) DEFAULT '0', hum varchar(24) DEFAULT '', regenerations int(2) DEFAULT '15', regen_block_on int(1) DEFAULT '0', dialogs_on int(1) DEFAULT '0', open_display_door_on int(1) DEFAULT '0', PRIMARY KEY (pp_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %splots (plot_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', world varchar(64) DEFAULT '', chunk_x int(7) DEFAULT '0', chunk_z int(7) DEFAULT '0', size int(2) DEFAULT '3', name varchar(16) DEFAULT '', PRIMARY KEY (plot_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sportals (portal_id int(11) NOT NULL AUTO_INCREMENT, portal varchar(512) DEFAULT '', teleport varchar(512) DEFAULT '', direction varchar(5) DEFAULT '', tardis_id int(11) DEFAULT '0', abandoned int(1) DEFAULT '0', PRIMARY KEY (portal_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %spreviewers (uuid varchar(48) NOT NULL, world varchar(64) DEFAULT '', x float(7,1) DEFAULT '0', y float(3,1) DEFAULT '0', z float(7,1) DEFAULT '0', yaw float(7,1) DEFAULT '0.0', pitch float(7,1) DEFAULT '0.0', gamemode varchar(16) DEFAULT '', tardis_id int(11) DEFAULT '0', PRIMARY KEY (uuid)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sprograms (program_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', name varchar(32) DEFAULT '', inventory text NULL, parsed text NULL, checked int(1) DEFAULT '1', PRIMARY KEY (program_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sreminders (reminder_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', reminder text NULL, time int(11) DEFAULT '0', PRIMARY KEY (reminder_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sroom_progress (progress_id int(11) NOT NULL, direction varchar(5) DEFAULT '', room varchar(32) DEFAULT '', location varchar(512) DEFAULT '', tardis_id int(11) DEFAULT '0', progress_row int(3) DEFAULT '0', progress_column int(3) DEFAULT '0', progress_level int(3) DEFAULT '0', middle_type varchar(64) DEFAULT '', floor_type varchar(64) DEFAULT '', post_blocks text NULL, PRIMARY KEY (progress_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sseeds (seed_id int(11) NOT NULL AUTO_INCREMENT, schematic varchar(32) DEFAULT '', wall varchar(64) DEFAULT 'ORANGE_WOOL', floor varchar(64) DEFAULT 'LIGHT_GRAY_WOOL', location varchar(512) DEFAULT '', PRIMARY KEY (seed_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sensors (sensor_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', charging varchar(512) DEFAULT '', flight varchar(512) DEFAULT '', handbrake varchar(512) DEFAULT '', malfunction varchar(512) DEFAULT '', power varchar(512) DEFAULT '', PRIMARY KEY (sensor_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %ssiege (siege_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', tardis_id int(11) DEFAULT '0', PRIMARY KEY (siege_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sskins (skin_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', skin text, PRIMARY KEY (skin_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %ssonic (sonic_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', activated int(1) DEFAULT '0', model int(11) DEFAULT '10000011', bio int(1) DEFAULT '0', diamond int(1) DEFAULT '0', emerald int(1) DEFAULT '0', redstone int(1) DEFAULT '0', painter int(1) DEFAULT '0', ignite int(1) DEFAULT '0', arrow int(1) DEFAULT '0', knockback int(1) DEFAULT '0', brush int(1) DEFAULT '0', conversion int(1) DEFAULT '0', sonic_uuid varchar(48) DEFAULT '', last_scanned varchar(512) DEFAULT '', scan_type int(1) DEFAULT '0', PRIMARY KEY (sonic_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sstorage (storage_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', uuid varchar(48) DEFAULT '', owner varchar(32) DEFAULT '', saves_one text NULL, saves_two text NULL, areas text NULL, presets_one text NULL, presets_two text NULL, biomes_one text NULL, biomes_two text NULL, players text NULL, circuits text NULL, console text NULL, versions varchar(24) DEFAULT '0,0,0,0,0,0,0,0,0', PRIMARY KEY (storage_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %ssystem_upgrades (sys_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', uuid varchar(48) DEFAULT '', architecture int(1) DEFAULT '0', chameleon int(1) DEFAULT '0', rooms int(1) DEFAULT '0', desktop int(1) DEFAULT '0', feature int(1) DEFAULT '0', saves int(1) DEFAULT '0', monitor int(1) DEFAULT '0', force_field int(1) DEFAULT '0', tools int(1) DEFAULT '0', locator int(1) DEFAULT '0', telepathic int(1) DEFAULT '0', stattenheim_remote int(1) DEFAULT '0', navigation int(1) DEFAULT '0', distance_1 int(1) DEFAULT '0', distance_2 int(1) DEFAULT '0', distance_3 int(1) DEFAULT '0', inter_dimension int(1) DEFAULT '0', throttle int(1) DEFAULT '0', faster int(1) DEFAULT '0', rapid int(1) DEFAULT '0', warp int(1) DEFAULT '0', flight int(1) DEFAULT '0', PRIMARY KEY (sys_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %st_count (t_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', player varchar(32) DEFAULT '', count int(3) DEFAULT '0', grace int(3) DEFAULT '0', repair int(3) DEFAULT '0', PRIMARY KEY (t_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %stag (tag_id int(11) NOT NULL AUTO_INCREMENT, player varchar(32) DEFAULT '', `time` bigint(20) DEFAULT '0', PRIMARY KEY (tag_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %stardis (tardis_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', owner varchar(32) DEFAULT '', last_known_name varchar(32) DEFAULT '', chunk varchar(64) DEFAULT '', tips int(4) DEFAULT '0', size varchar(32) DEFAULT '', abandoned int(1) DEFAULT '0', artron_level int(11) DEFAULT '0', replaced text NULL, companions text NULL, handbrake_on int(1) DEFAULT '1', iso_on int(1) DEFAULT '0', hidden int(1) DEFAULT '0', recharging int(1) DEFAULT '0', tardis_init int(1) DEFAULT '0', adapti_on int(1) DEFAULT '0', chameleon_preset varchar(32) DEFAULT 'FACTORY', chameleon_demat varchar(32) DEFAULT 'FACTORY', creeper varchar(512) DEFAULT '', beacon varchar(512) DEFAULT '', eps varchar(512) DEFAULT '', rail varchar(512) DEFAULT '', renderer varchar(512) DEFAULT '', zero varchar(512) DEFAULT '', rotor varchar(48) DEFAULT '', powered_on int(1) DEFAULT '0', lights_on int(1) DEFAULT '1', siege_on int(1) DEFAULT '0', lastuse bigint(20), monsters int(2) DEFAULT '0', furnaces int(2) DEFAULT '0', bedrock int(1) DEFAULT '0', PRIMARY KEY (tardis_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %stransmats (transmat_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', name varchar(64) DEFAULT '', world varchar(64) DEFAULT '', x float(7,1) DEFAULT '0', y float(3,1) DEFAULT '0', z float(7,1) DEFAULT '0', yaw float(7,1) DEFAULT '0.0', PRIMARY KEY (transmat_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %stravel_stats (travel_stats_id int(11) NOT NULL AUTO_INCREMENT, travel_type varchar(16) DEFAULT '', tardis_id int(11) DEFAULT '0', uuid varchar(48) DEFAULT '', PRIMARY KEY (travel_stats_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %straveled_to (uuid varchar(48) NOT NULL, environment varchar(64) NOT NULL) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %stravellers (traveller_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', uuid varchar(48) DEFAULT '', player varchar(32) DEFAULT '', PRIMARY KEY (traveller_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %svaults (v_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', location varchar(512) DEFAULT '', x int(11) DEFAULT '0', y int(3) DEFAULT '0', z int(11) DEFAULT '0', chest_type varchar(8) DEFAULT 'DROP', PRIMARY KEY (v_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sthevoid (tardis_id int(11) NOT NULL, PRIMARY KEY (tardis_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %svortex (tardis_id int(11) NOT NULL, task int(11) DEFAULT '0', PRIMARY KEY (tardis_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            // shop
            "CREATE TABLE IF NOT EXISTS %sitems (item_id int(11) NOT NULL AUTO_INCREMENT, item varchar(64) DEFAULT '', location varchar(512) DEFAULT '', cost float(5,1) DEFAULT 0, PRIMARY KEY (item_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            // vortex manipulator
            "CREATE TABLE IF NOT EXISTS %sbeacons (beacon_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', location varchar(512) DEFAULT '', block_data varchar(32) DEFAULT '', PRIMARY KEY (beacon_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %smanipulator (uuid varchar(48) NOT NULL, tachyon_level int(11) DEFAULT '0', PRIMARY KEY (uuid)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %smessages (message_id int(11) NOT NULL AUTO_INCREMENT, uuid_to varchar(48) DEFAULT '', uuid_from varchar(48) DEFAULT '', message text NULL, date bigint(20), `read` int(1) DEFAULT '0', PRIMARY KEY (message_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %ssaves (save_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', save_name varchar(64) DEFAULT '', world varchar(64) DEFAULT '', x float DEFAULT '0', y float DEFAULT '0', z float DEFAULT '0', yaw float DEFAULT '0', pitch float DEFAULT '0', PRIMARY KEY (save_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sfollowers (uuid varchar(48) NOT NULL, owner varchar(48), species varchar(8) DEFAULT '', following int(1) DEFAULT '0', `option` int(1) DEFAULT '0', colour varchar(5) DEFAULT '', ammo int(3) DEFAULT '0', PRIMARY KEY (uuid)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;"
    );
}
