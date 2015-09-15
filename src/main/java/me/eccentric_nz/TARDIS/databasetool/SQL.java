/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.databasetool;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author eccentric_nz
 */
public class SQL {

    public static enum TABLE {

        achievements,
        arched,
        areas,
        ars,
        back,
        blocks,
        chameleon,
        chunks,
        condenser,
        controls,
        current,
        destinations,
        doors,
        gravity_well,
        homes,
        inventories,
        lamps,
        movers,
        next,
        player_prefs,
        portals,
        siege,
        storage,
        t_count,
        tag,
        tardis,
        travellers,
        vaults;
    }

    public static final List<String> CREATES = Arrays.asList(
            "CREATE TABLE IF NOT EXISTS %sachievements (a_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', player varchar(32) DEFAULT '', `name` varchar(32) DEFAULT '', amount text, completed int(1) DEFAULT '0', PRIMARY KEY (a_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %sarched (uuid varchar(48) DEFAULT '', arch_name varchar(16) DEFAULT '', arch_time bigint(20) DEFAULT '0', PRIMARY KEY (uuid)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %sareas (area_id int(11) NOT NULL AUTO_INCREMENT, area_name varchar(64) DEFAULT '', world varchar(64) DEFAULT '', minx int(7) DEFAULT '0', minz int(7) DEFAULT '0', maxx int(7) DEFAULT '0', maxz int(7) DEFAULT '0', y int(3) DEFAULT '0', parking_distance int(2) DEFAULT '2', PRIMARY KEY (area_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %sars (ars_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', uuid varchar(48) DEFAULT '', player varchar(32) DEFAULT '', ars_x_east int(2) DEFAULT '2', ars_z_south int(2) DEFAULT '2', ars_y_layer int(1) DEFAULT '1', json text, PRIMARY KEY (ars_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %sback (back_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', submarine int(1) DEFAULT '0', PRIMARY KEY (back_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %sblocks (b_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', location varchar(512) DEFAULT '', `block` int(6) DEFAULT '0', `data` int(6) DEFAULT '0', police_box int(1) DEFAULT '0', PRIMARY KEY (b_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %schameleon (chameleon_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', blueprintID varchar(256) DEFAULT '', blueprintData varchar(256) DEFAULT '', stainID varchar(256) DEFAULT '', stainData varchar(256) DEFAULT '', glassID varchar(256) DEFAULT '', glassData varchar(256) DEFAULT '', PRIMARY KEY (chameleon_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %schunks (chunk_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '0', x int(7) DEFAULT '0', z int(7) DEFAULT '0', PRIMARY KEY (chunk_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %scondenser (c_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', block_data varchar(32) DEFAULT '', block_count int(11) DEFAULT '0', PRIMARY KEY (c_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %scontrols (c_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', `type` int(3) DEFAULT '0', location varchar(512) DEFAULT '', secondary int(1) DEFAULT '0', PRIMARY KEY (c_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %scurrent (current_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', submarine int(1) DEFAULT '0', biome varchar(64) DEFAULT '', PRIMARY KEY (current_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %sdestinations (dest_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', dest_name varchar(64) DEFAULT '', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', bind varchar(64) DEFAULT '', `type` int(3) DEFAULT '0', submarine int(1) DEFAULT '0', slot int(1) DEFAULT '-1', PRIMARY KEY (dest_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %sdoors (door_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', door_type int(1) DEFAULT '0', door_location varchar(512) DEFAULT '', door_direction varchar(5) DEFAULT 'SOUTH', locked int(1) DEFAULT '0', PRIMARY KEY (door_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %sgravity_well (g_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', location varchar(512) DEFAULT '', direction int(2) DEFAULT '0', distance int(3) DEFAULT '11', velocity float DEFAULT '0.5', PRIMARY KEY (g_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %shomes (home_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', submarine int(1) DEFAULT '0', PRIMARY KEY (home_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %sinventories (id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', player varchar(24) DEFAULT '', arch int(1), inventory text, armour text, attributes text, armour_attributes text, PRIMARY KEY (id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %slamps (l_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', location varchar(512) DEFAULT '', PRIMARY KEY (l_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %smovers (uuid varchar(48) NOT NULL, PRIMARY KEY (uuid)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %snext (next_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', submarine int(1) DEFAULT '0', PRIMARY KEY (next_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %splayer_prefs (pp_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', player varchar(32), `key_item` varchar(32) DEFAULT '', sfx_on int(1) DEFAULT '0', quotes_on int(1) DEFAULT '0', artron_level int(11) DEFAULT '0', wall varchar(64) DEFAULT 'ORANGE_WOOL', floor varchar(64) DEFAULT 'LIGHT_GREY_WOOL', siege_wall varchar(64) DEFAULT 'GREY_CLAY', siege_floor varchar(64) DEFAULT 'BLACK_CLAY', auto_on int(1) DEFAULT '0', beacon_on int(1) DEFAULT '1', hads_on int(1) DEFAULT '1', build_on int(1) DEFAULT '1', eps_on int(1) DEFAULT '0', eps_message text, lamp int(6) DEFAULT '0', language varchar(32) DEFAULT 'AUTO_DETECT', texture_on int(1) DEFAULT '0', texture_in varchar(512) DEFAULT '', texture_out varchar(512) DEFAULT 'default', submarine_on int(1) DEFAULT '0', dnd_on int(1) DEFAULT '0', minecart_on int(1) DEFAULT '0', renderer_on int(1) DEFAULT '1', wool_lights_on int(1) DEFAULT '0', ctm_on int(1) DEFAULT '0', sign_on int(1) DEFAULT '1', travelbar_on int(1) DEFAULT '0', farm_on int(1) DEFAULT '0', lanterns_on int(1) DEFAULT '0', auto_siege_on int(1) DEFAULT '0', flying_mode int(1) DEFAULT '1', difficulty int(1) DEFAULT '0', PRIMARY KEY (pp_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %sportals (portal_id int(11) NOT NULL AUTO_INCREMENT, portal varchar(512) DEFAULT '', teleport varchar(512) DEFAULT '', direction varchar(5) DEFAULT '', tardis_id int(11) DEFAULT '0', PRIMARY KEY (portal_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %ssiege (siege_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', tardis_id int(11) DEFAULT '0', PRIMARY KEY (siege_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %sstorage (storage_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', uuid varchar(48) DEFAULT '', owner varchar(32) DEFAULT '', saves_one text NULL, saves_two text NULL, areas text NULL, presets_one text NULL, presets_two text NULL, biomes_one text NULL, biomes_two text NULL, players text NULL, circuits text NULL, console text NULL, PRIMARY KEY (storage_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %st_count (t_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', player varchar(32) DEFAULT '', count int(3) DEFAULT '0', grace int(3) DEFAULT '0', PRIMARY KEY (t_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %stag (tag_id int(11) NOT NULL AUTO_INCREMENT, player varchar(32) DEFAULT '', `time` int(11) DEFAULT '0', PRIMARY KEY (tag_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %stardis (tardis_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', owner varchar(32) DEFAULT '', last_known_name varchar(32) DEFAULT '', chunk varchar(64) DEFAULT '', tips int(4) DEFAULT '0', size varchar(32) DEFAULT '', artron_level int(11) DEFAULT '0', replaced text NULL, companions text NULL, chameleon varchar(512) DEFAULT '', handbrake_on int(1) DEFAULT '1', iso_on int(1) DEFAULT '0', hidden int(1) DEFAULT '0', recharging int(1) DEFAULT '0', tardis_init int(1) DEFAULT '0', adapti_on int(1) DEFAULT '0', chamele_on int(1) DEFAULT '0', chameleon_preset varchar(32) DEFAULT 'NEW', chameleon_demat varchar(32) DEFAULT 'NEW', chameleon_id int(6) DEFAULT '35', chameleon_data int(6) DEFAULT '11', save_sign varchar(512) DEFAULT '', creeper varchar(512) DEFAULT '', condenser varchar(512) DEFAULT '', scanner varchar(512) DEFAULT '', farm varchar(512) DEFAULT '', stable varchar(512) DEFAULT '', beacon varchar(512) DEFAULT '', eps varchar(512) DEFAULT '', rail varchar(512) DEFAULT '', village varchar(512) DEFAULT '', renderer varchar(512) DEFAULT '', zero varchar(512) DEFAULT '', hutch varchar(512) DEFAULT '', powered_on int(1) DEFAULT '0', lights_on int(1) DEFAULT '1', siege_on int(1) DEFAULT '0', lastuse bigint(20), PRIMARY KEY (tardis_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %stravellers (traveller_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', uuid varchar(48) DEFAULT '', player varchar(32) DEFAULT '', PRIMARY KEY (traveller_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %svaults (v_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', location varchar(512) DEFAULT '', x int(11) DEFAULT '0', y int(3) DEFAULT '0', z int(11) DEFAULT '0', PRIMARY KEY (v_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;"
    );

    public static final List<String> INSERTS = Arrays.asList(
            "INSERT INTO `%sachievements` (`a_id`, `uuid`, `player`, `name`, `amount`, `completed`) VALUES",
            "INSERT INTO `%sarched` (`uuid`, `arch_name`, `arch_time`) VALUES",
            "INSERT INTO `%sareas` (`area_id`, `area_name`, `world`, `minx`, `minz`, `maxx`, `maxz`, `y`, `parking_distance`) VALUES",
            "INSERT INTO `%sars` (`ars_id`, `tardis_id`, `uuid`, `player`, `ars_x_east`, `ars_z_south`, `ars_y_layer`, `json`) VALUES",
            "INSERT INTO `%sback` (`back_id`, `tardis_id`, `world`, `x`, `y`, `z`, `direction`, `submarine`) VALUES",
            "INSERT INTO `%sblocks` (`b_id`, `tardis_id`, `location`, `block`, `data`, `police_box`) VALUES",
            "INSERT INTO `%schameleon` (`chameleon_id`, `tardis_id`, `blueprintID`, `blueprintData`, `stainID`, `stainData`, `glassID`, `glassData`) VALUES",
            "INSERT INTO `%schunks` (`chunk_id`, `tardis_id`, `world`, `x`, `z`) VALUES",
            "INSERT INTO `%scondenser` (`c_id`, `tardis_id`, `block_data`, `block_count`) VALUES",
            "INSERT INTO `%scontrols` (`c_id`, `tardis_id`, `type`, `location`, `secondary`) VALUES",
            "INSERT INTO `%scurrent` (`current_id`, `tardis_id`, `world`, `x`, `y`, `z`, `direction`, `submarine`, `biome`) VALUES",
            "INSERT INTO `%sdestinations` (`dest_id`, `tardis_id`, `dest_name`, `world`, `x`, `y`, `z`, `direction`, `bind`, `type`, `submarine`, `slot`) VALUES",
            "INSERT INTO `%sdoors` (`door_id`, `tardis_id`, `door_type`, `door_location`, `door_direction`, `locked`) VALUES",
            "INSERT INTO `%sgravity_well` (`g_id`, `tardis_id`, `location`, `direction`, `distance`, `velocity`) VALUES",
            "INSERT INTO `%shomes` (`home_id`, `tardis_id`, `world`, `x`, `y`, `z`, `direction`, `submarine`) VALUES",
            "INSERT INTO `%sinventories` (`id`, `uuid`, `player`, `arch`, `inventory`, `armour`, `attributes`, `armour_attributes`) VALUES",
            "INSERT INTO `%slamps` (`l_id`, `tardis_id`, `location`) VALUES",
            "INSERT INTO `%smovers` (`uuid`) VALUES",
            "INSERT INTO `%snext` (`next_id`, `tardis_id`, `world`, `x`, `y`, `z`, `direction`, `submarine`) VALUES",
            "INSERT INTO `%splayer_prefs` (`pp_id`, `uuid`, `player`, `key_item`, `sfx_on`, `quotes_on`, `artron_level`, `wall`, `floor`,  `siege_wall`, `siege_floor`, `auto_on`, `beacon_on`, `hads_on`, `build_on`, `eps_on`, `eps_message`, `lamp`, `language`, `texture_on`, `texture_in`, `texture_out`, `submarine_on`, `dnd_on`, `minecart_on`, `renderer_on`, `wool_lights_on`, `ctm_on`, `sign_on`, `travelbar_on`, `farm_on`,  `lanterns_on`, `auto_siege_on`, `flying_mode`, `difficulty`) VALUES",
            "INSERT INTO `%sportals` (`portal_id`, `portal`, `teleport`, `direction`, `tardis_id`) VALUES",
            "INSERT INTO `%ssiege` (`siege_id`, `uuid`, `tardis_id`) VALUES",
            "INSERT INTO `%sstorage` (`storage_id`, `tardis_id`, `uuid`, `owner`, `saves_one`, `saves_two`, `areas`, `presets_one`, `presets_two`, `biomes_one`, `biomes_two`, `players`, `circuits`, `console`) VALUES",
            "INSERT INTO `%st_count` (`t_id`, `uuid`, `player`, `count`, `grace`) VALUES",
            "INSERT INTO `%stag` (`tag_id`, `player`, `time`) VALUES",
            "INSERT INTO `%stardis` (`tardis_id`, `uuid`, `owner`, `last_known_name`, `chunk`, `tips`, `size`, `artron_level`, `replaced`, `companions`, `chameleon`, `handbrake_on`, `iso_on`, `hidden`, `recharging`, `tardis_init`, `adapti_on`, `chamele_on`, `chameleon_preset`, `chameleon_demat`, `chameleon_id`, `chameleon_data`, `save_sign`, `creeper`, `condenser`, `scanner`, `farm`, `stable`, `beacon`, `eps`, `rail`, `village`, `renderer`, `zero`, `hutch`, `powered_on`, `lights_on`, `lastuse`) VALUES",
            "INSERT INTO `%stravellers` (`traveller_id`, `tardis_id`, `uuid`, `player`) VALUES",
            "INSERT INTO `%svaults` (`v_id`, `tardis_id`, `location`, `x`, `y`, `z`) VALUES"
    );

    public static final List<String> VALUES = Arrays.asList(
            "(%s, '%s', '%s', '%s', '%s', %s)",
            "('%s', '%s', %s)",
            "(%s, '%s', '%s', %s, %s, %s, %s, %s, %s)",
            "(%s, %s, '%s', '%s', %s, %s, %s, '%s')",
            "(%s, %s, '%s', %s, %s, %s, '%s', %s)",
            "(%s, %s, '%s', %s, %s, %s)",
            "(%s, %s, '%s', '%s', '%s', '%s', '%s', '%s')",
            "(%s, %s, '%s', %s, %s)",
            "(%s, %s, '%s', %s)",
            "(%s, %s, %s, '%s', %s)",
            "(%s, %s, '%s', %s, %s, %s, '%s', %s, '%s')",
            "(%s, %s, '%s', '%s', %s, %s, %s, '%s', '%s', %s, %s, %s)",
            "(%s, %s, %s, '%s', '%s', %s)",
            "(%s, %s, '%s', %s, %s, %s)",
            "(%s, %s, '%s', %s, %s, %s, '%s', %s)",
            "(%s, '%s', '%s', %s, '%s', '%s', '%s', '%s')",
            "(%s, %s, '%s')",
            "('%s')",
            "(%s, %s, '%s', %s, %s, %s, '%s', %s)",
            "(%s, '%s', '%s', '%s', %s, %s, %s, '%s', '%s', '%s', '%s', %s, %s, %s, %s, %s, '%s', %s, '%s', %s, '%s', '%s', %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
            "(%s, '%s', '%s', '%s', %s)",
            "(%s, '%s', %s)",
            "(%s, %s, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
            "(%s, '%s', '%s', %s, %s)",
            "(%s, '%s', %s)",
            "(%s, '%s', '%s', '%s', '%s', %s, '%s', %s, '%s', '%s', '%s', %s, %s, %s, %s, %s, %s, %s, '%s', '%s', %s, %s, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, %s, %s)",
            "(%s, %s, '%s', '%s')",
            "(%s, %s, '%s', %s, %s, %s)"
    );

    public static final String COMMENT = "--";
    public static final String DUMP = "-- Dumping data for table ";
    public static final String STRUCTURE = "-- Table structure for table ";
    public static final String SEPARATOR = "-- --------------------------------------------------------";
}
