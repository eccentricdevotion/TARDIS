/*
 * Copyright (C) 2020 eccentric_nz
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

import java.util.Arrays;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class SQL {

    public enum TABLE {

        achievements,
        arched,
        archive,
        areas,
        ars,
        back,
        bind,
        blocks,
        chameleon,
        chunks,
        condenser,
        controls,
        current,
        destinations,
        dispersed,
        doors,
        farming,
        forcefield,
        gravity_well,
        homes,
        inventories,
        junk,
        lamps,
        movers,
        next,
        paper_bag,
        player_prefs,
        portals,
        programs,
        reminders,
        room_progress,
        siege,
        sonic,
        storage,
        t_count,
        tag,
        tardis,
        transmats,
        traveled_to,
        travellers,
        vaults,
        thevoid,
        vortex
    }

    public static final List<String> CREATES = Arrays.asList(
            "CREATE TABLE IF NOT EXISTS %sachievements (a_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', player varchar(32) DEFAULT '', `name` varchar(32) DEFAULT '', amount text, completed int(1) DEFAULT '0', PRIMARY KEY (a_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sarched (uuid varchar(48) DEFAULT '', arch_name varchar(16) DEFAULT '', arch_time bigint(20) DEFAULT '0', PRIMARY KEY (uuid)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sarchive (archive_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', `name` varchar(32) DEFAULT '', console_size varchar(16) DEFAULT '', beacon int(1) DEFAULT '0', lanterns int(1) DEFAULT '0', `use` int(1) DEFAULT '0', `y` int(3) DEFAULT '64', `data` text, description varchar(256), PRIMARY KEY (archive_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sareas (area_id int(11) NOT NULL AUTO_INCREMENT, area_name varchar(64) DEFAULT '', world varchar(64) DEFAULT '', minx int(7) DEFAULT '0', minz int(7) DEFAULT '0', maxx int(7) DEFAULT '0', maxz int(7) DEFAULT '0', y int(3) DEFAULT '0', parking_distance int(2) DEFAULT '2', invisibility varchar(32) DEFAULT 'ALLOW', PRIMARY KEY (area_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sars (ars_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', uuid varchar(48) DEFAULT '', player varchar(32) DEFAULT '', ars_x_east int(2) DEFAULT '2', ars_z_south int(2) DEFAULT '2', ars_y_layer int(1) DEFAULT '1', json text, PRIMARY KEY (ars_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sback (back_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', submarine int(1) DEFAULT '0', PRIMARY KEY (back_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sbind (bind_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', `type` int(1) DEFAULT '0', location varchar(512) DEFAULT '', name varchar(32) DEFAULT '', PRIMARY KEY (bind_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sblocks (b_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', location varchar(512) DEFAULT '', `data` text, police_box int(1) DEFAULT '0', PRIMARY KEY (b_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %schameleon (chameleon_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', blueprintData text, stainData text, glassData text, line1 varchar(48) DEFAULT '', line2 varchar(48) DEFAULT '', line3 varchar(48) DEFAULT '', line4 varchar(48) DEFAULT '', asymmetric int(1) DEFAULT '0', PRIMARY KEY (chameleon_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %schunks (chunk_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '0', x int(7) DEFAULT '0', z int(7) DEFAULT '0', PRIMARY KEY (chunk_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %scondenser (c_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', block_data varchar(32) DEFAULT '', block_count int(11) DEFAULT '0', PRIMARY KEY (c_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %scontrols (c_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', `type` int(3) DEFAULT '0', location varchar(512) DEFAULT '', secondary int(1) DEFAULT '0', PRIMARY KEY (c_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %scurrent (current_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', submarine int(1) DEFAULT '0', biome varchar(64) DEFAULT '', PRIMARY KEY (current_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sdestinations (dest_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', dest_name varchar(64) DEFAULT '', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', preset varchar(32) DEFAULT '', bind varchar(64) DEFAULT '', `type` int(3) DEFAULT '0', submarine int(1) DEFAULT '0', slot int(1) DEFAULT '-1', PRIMARY KEY (dest_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sdispersed (d_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', tardis_id int(11) DEFAULT '0', PRIMARY KEY (d_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sdoors (door_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', door_type int(1) DEFAULT '0', door_location varchar(512) DEFAULT '', door_direction varchar(5) DEFAULT 'SOUTH', locked int(1) DEFAULT '0', PRIMARY KEY (door_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sfarming (farm_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', apiary varchar(512) DEFAULT '', aquarium varchar(512) DEFAULT '', bamboo varchar(512) DEFAULT '', birdcage varchar(512) DEFAULT '', farm varchar(512) DEFAULT '', hutch varchar(512) DEFAULT '', igloo varchar(512) DEFAULT '', stable varchar(512) DEFAULT '', stall varchar(512) DEFAULT '', village varchar(512) DEFAULT '', PRIMARY KEY (farm_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sforcefield (uuid varchar(48) NOT NULL DEFAULT '', location varchar(512) DEFAULT '', PRIMARY KEY (uuid)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sgravity_well (g_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', location varchar(512) DEFAULT '', direction int(2) DEFAULT '0', distance int(3) DEFAULT '11', velocity float DEFAULT '0.5', PRIMARY KEY (g_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %shomes (home_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', submarine int(1) DEFAULT '0', preset varchar(64) DEFAULT '', PRIMARY KEY (home_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sinventories (id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', player varchar(24) DEFAULT '', arch int(1), inventory text, armour text, attributes text, armour_attributes text, PRIMARY KEY (id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sjunk (id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', tardis_id int(11) DEFAULT '0', save_sign varchar(512) DEFAULT '', handbrake varchar(512) DEFAULT '', wall varchar(64) DEFAULT 'ORANGE_WOOL', floor varchar(64) DEFAULT 'LIGHT_GRAY_WOOL', preset varchar(32) DEFAULT '', PRIMARY KEY (id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %slamps (l_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', location varchar(512) DEFAULT '', PRIMARY KEY (l_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %smovers (uuid varchar(48) NOT NULL, PRIMARY KEY (uuid)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %snext (next_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', submarine int(1) DEFAULT '0', PRIMARY KEY (next_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %spaper_bag (paper_bag_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', flavour_1 varchar(12) DEFAULT '', amount_1 int(2) DEFAULT '0', flavour_2 varchar(12) DEFAULT '', amount_2 int(2) DEFAULT '0', flavour_3 varchar(12) DEFAULT '', amount_3 int(2) DEFAULT '0', flavour_4 varchar(12) DEFAULT '', amount_4 int(2) DEFAULT '0', PRIMARY KEY (paper_bag_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %splayer_prefs (pp_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', player varchar(32), `key_item` varchar(32) DEFAULT '', sfx_on int(1) DEFAULT '0', quotes_on int(1) DEFAULT '0', artron_level int(11) DEFAULT '0', wall varchar(64) DEFAULT 'ORANGE_WOOL', floor varchar(64) DEFAULT 'LIGHT_GRAY_WOOL', siege_wall varchar(64) DEFAULT 'GRAY_TERRACOTTA', siege_floor varchar(64) DEFAULT 'BLACK_TERRACOTTA', auto_on int(1) DEFAULT '0', beacon_on int(1) DEFAULT '1', hads_on int(1) DEFAULT '1', hads_type varchar(12) DEFAULT 'DISPLACEMENT', build_on int(1) DEFAULT '1', eps_on int(1) DEFAULT '0', eps_message text, lamp varchar(64) DEFAULT '', language varchar(32) DEFAULT 'ENGLISH', texture_on int(1) DEFAULT '0', texture_in varchar(512) DEFAULT '', texture_out varchar(512) DEFAULT 'default', submarine_on int(1) DEFAULT '0', dnd_on int(1) DEFAULT '0', minecart_on int(1) DEFAULT '0', renderer_on int(1) DEFAULT '1', wool_lights_on int(1) DEFAULT '0', ctm_on int(1) DEFAULT '0', sign_on int(1) DEFAULT '1', telepathy_on int(1) DEFAULT '0', travelbar_on int(1) DEFAULT '0', farm_on int(1) DEFAULT '1', lanterns_on int(1) DEFAULT '0', policebox_textures_on int(1) DEFAULT '1', auto_siege_on int(1) DEFAULT '0', flying_mode int(1) DEFAULT '1', difficulty int(1) DEFAULT '0', auto_powerup_on int(1) DEFAULT '0', auto_rescue_on int(1) DEFAULT '0', hum varchar(24) DEFAULT '', PRIMARY KEY (pp_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sportals (portal_id int(11) NOT NULL AUTO_INCREMENT, portal varchar(512) DEFAULT '', teleport varchar(512) DEFAULT '', direction varchar(5) DEFAULT '', tardis_id int(11) DEFAULT '0', abandoned int(1) DEFAULT '0', PRIMARY KEY (portal_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sprograms (program_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', name varchar(32) DEFAULT '', inventory text NULL, parsed text NULL, checked int(1) DEFAULT '1', PRIMARY KEY (program_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sreminders (reminder_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', reminder text NULL, time int(11) DEFAULT '0', PRIMARY KEY (reminder_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sroom_progress (progress_id int(11) NOT NULL, direction varchar(5) DEFAULT '', room varchar(32) DEFAULT '', location varchar(512) DEFAULT '', tardis_id int(11) DEFAULT '0', progress_row int(3) DEFAULT '0', progress_column int(3) DEFAULT '0', progress_level int(3) DEFAULT '0', middle_type varchar(64) DEFAULT '', floor_type varchar(64) DEFAULT '', post_blocks text NULL, PRIMARY KEY (progress_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %ssiege (siege_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', tardis_id int(11) DEFAULT '0', PRIMARY KEY (siege_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %ssonic (sonic_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', activated int(1) DEFAULT '0', sonic_type varchar(16) DEFAULT '', model int(11) DEFAULT '10000011', bio int(1) DEFAULT '0', diamond int(1) DEFAULT '0', emerald int(1) DEFAULT '0', redstone int(1) DEFAULT '0', painter int(1) DEFAULT '0', ignite int(1) DEFAULT '0', arrow int(1) DEFAULT '0', PRIMARY KEY (sonic_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sstorage (storage_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', uuid varchar(48) DEFAULT '', owner varchar(32) DEFAULT '', saves_one text NULL, saves_two text NULL, areas text NULL, presets_one text NULL, presets_two text NULL, biomes_one text NULL, biomes_two text NULL, players text NULL, circuits text NULL, console text NULL, PRIMARY KEY (storage_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %st_count (t_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', player varchar(32) DEFAULT '', count int(3) DEFAULT '0', grace int(3) DEFAULT '0', repair int(3) DEFAULT '0', PRIMARY KEY (t_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %stag (tag_id int(11) NOT NULL AUTO_INCREMENT, player varchar(32) DEFAULT '', `time` bigint(20) DEFAULT '0', PRIMARY KEY (tag_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %stardis (tardis_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', owner varchar(32) DEFAULT '', last_known_name varchar(32) DEFAULT '', chunk varchar(64) DEFAULT '', tips int(4) DEFAULT '0', size varchar(32) DEFAULT '', abandoned int(1) DEFAULT '0', artron_level int(11) DEFAULT '0', replaced text NULL, companions text NULL, handbrake_on int(1) DEFAULT '1', iso_on int(1) DEFAULT '0', hidden int(1) DEFAULT '0', recharging int(1) DEFAULT '0', tardis_init int(1) DEFAULT '0', adapti_on int(1) DEFAULT '0', chameleon_preset varchar(32) DEFAULT 'FACTORY', chameleon_demat varchar(32) DEFAULT 'FACTORY', creeper varchar(512) DEFAULT '', beacon varchar(512) DEFAULT '', eps varchar(512) DEFAULT '', rail varchar(512) DEFAULT '', renderer varchar(512) DEFAULT '', zero varchar(512) DEFAULT '', powered_on int(1) DEFAULT '0', lights_on int(1) DEFAULT '1', siege_on int(1) DEFAULT '0', lastuse bigint(20), monsters int(2) DEFAULT '0', PRIMARY KEY (tardis_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %stransmats (transmat_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', name varchar(64) DEFAULT '', world varchar(64) DEFAULT '', x float(7,1) DEFAULT '0', y float(3,1) DEFAULT '0', z float(7,1) DEFAULT '0', yaw float(7,1) DEFAULT '0.0', PRIMARY KEY (transmat_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %straveled_to (uuid varchar(48) NOT NULL, environment varchar(64) NOT NULL) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %stravellers (traveller_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', uuid varchar(48) DEFAULT '', player varchar(32) DEFAULT '', PRIMARY KEY (traveller_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %svaults (v_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', location varchar(512) DEFAULT '', x int(11) DEFAULT '0', y int(3) DEFAULT '0', z int(11) DEFAULT '0', chest_type varchar(8) DEFAULT 'DROP', PRIMARY KEY (v_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %sthevoid (tardis_id int(11) NOT NULL, PRIMARY KEY (tardis_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",

            "CREATE TABLE IF NOT EXISTS %svortex (tardis_id int(11) NOT NULL, task int(11) DEFAULT '0', PRIMARY KEY (tardis_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;"

    );

    static final List<String> INSERTS = Arrays.asList(
            "INSERT INTO `%sachievements` (`a_id`, `uuid`, `player`, `name`, `amount`, `completed`) VALUES ",

            "INSERT INTO `%sarched` (`uuid`, `arch_name`, `arch_time`) VALUES ",

            "INSERT INTO `%sarchive` (`archive_id`, `uuid`, `name`, `console_size`, `beacon`, `lanterns`, `use`, `y`, `data`, `description`) VALUES ",

            "INSERT INTO `%sareas` (`area_id`, `area_name`, `world`, `minx`, `minz`, `maxx`, `maxz`, `y`, `parking_distance`, `invisibility`) VALUES ",

            "INSERT INTO `%sars` (`ars_id`, `tardis_id`, `uuid`, `player`, `ars_x_east`, `ars_z_south`, `ars_y_layer`, `json`) VALUES ",

            "INSERT INTO `%sback` (`back_id`, `tardis_id`, `world`, `x`, `y`, `z`, `direction`, `submarine`) VALUES ",

            "INSERT INTO `%sbind` (`bind_id`, `tardis_id`, `type`, `location`, `name`) VALUES ",

            "INSERT INTO `%sblocks` (`b_id`, `tardis_id`, `location`, `data`, `police_box`) VALUES ",

            "INSERT INTO `%schameleon` (`chameleon_id`, `tardis_id`, `blueprintData`, `stainData`, `glassData`, `line1`, `line2`, `line3`, `line4`, `asymmetric`) VALUES ",

            "INSERT INTO `%schunks` (`chunk_id`, `tardis_id`, `world`, `x`, `z`) VALUES ",

            "INSERT INTO `%scondenser` (`c_id`, `tardis_id`, `block_data`, `block_count`) VALUES ",

            "INSERT INTO `%scontrols` (`c_id`, `tardis_id`, `type`, `location`, `secondary`) VALUES ",

            "INSERT INTO `%scurrent` (`current_id`, `tardis_id`, `world`, `x`, `y`, `z`, `direction`, `submarine`, `biome`) VALUES ",

            "INSERT INTO `%sdestinations` (`dest_id`, `tardis_id`, `dest_name`, `world`, `x`, `y`, `z`, `direction`, `preset`, `bind`, `type`, `submarine`, `slot`) VALUES ",

            "INSERT INTO `%sdispersed` (`d_id`, `uuid`, `world`, `x`, `y`, `z`, `tardis_id`) VALUES ",

            "INSERT INTO `%sdoors` (`door_id`, `tardis_id`, `door_type`, `door_location`, `door_direction`, `locked`) VALUES ",

            "INSERT INTO `%sfarming` (`farm_id`, `tardis_id`, `apiary`, `aquarium`, `bamboo`, `birdcage`, `farm`, `hutch`, `igloo`, `stable`, `stall`, `village`) VALUES ",

            "INSERT INTO `%sforcefield` (`uuid`, `location`) VALUES ",

            "INSERT INTO `%sgravity_well` (`g_id`, `tardis_id`, `location`, `direction`, `distance`, `velocity`) VALUES ",

            "INSERT INTO `%shomes` (`home_id`, `tardis_id`, `world`, `x`, `y`, `z`, `direction`, `submarine`, `preset`) VALUES ",

            "INSERT INTO `%sinventories` (`id`, `uuid`, `player`, `arch`, `inventory`, `armour`, `attributes`, `armour_attributes`) VALUES ",

            "INSERT INTO `%sjunk` (`id`, `uuid`, `tardis_id`, `save_sign`, `handbrake`, `wall`, `floor`, `preset`) VALUES ",

            "INSERT INTO `%slamps` (`l_id`, `tardis_id`, `location`) VALUES ",

            "INSERT INTO `%smovers` (`uuid`) VALUES ",

            "INSERT INTO `%snext` (`next_id`, `tardis_id`, `world`, `x`, `y`, `z`, `direction`, `submarine`) VALUES ",

            "INSERT INTO `%spaper_bag` (`paper_bag_id`, `uuid`, `flavour_1`, `amount_1`, `flavour_2`, `amount_2`, `flavour_3`, `amount_3`, `flavour_4`, `amount_4`) VALUES ",

            "INSERT INTO `%splayer_prefs` (`pp_id`, `uuid`, `player`, `key_item`, `sfx_on`, `quotes_on`, `artron_level`, `wall`, `floor`,  `siege_wall`, `siege_floor`, `auto_on`, `beacon_on`, `hads_on`, `hads_type`, `build_on`, `eps_on`, `eps_message`, `lamp`, `language`, `texture_on`, `texture_in`, `texture_out`, `submarine_on`, `dnd_on`, `minecart_on`, `renderer_on`, `wool_lights_on`, `ctm_on`, `sign_on`, `telepathy_on`, `travelbar_on`, `farm_on`,  `lanterns_on`, `policebox_textures_on`, `auto_siege_on`, `flying_mode`, `difficulty`, `auto_powerup_on`, `auto_rescue_on`, `hum`) VALUES ",

            "INSERT INTO `%sportals` (`portal_id`, `portal`, `teleport`, `direction`, `tardis_id`, `abandoned`) VALUES ",

            "INSERT INTO `%sprograms` (`program_id`, `uuid`, `name`, `inventory`, `parsed`, `checked`) VALUES ",

            "INSERT INTO `%sreminders` (`reminder_id`, `uuid`, `reminder`, `time`) VALUES ",

            "INSERT INTO `%sroom_progress` (`progress_id`, `direction`, `room`, `location`, `tardis_id`, `progress_row`, `progress_column`, `progress_level`, `middle_type`, `floor_type`, `post_blocks`) VALUES ",

            "INSERT INTO `%ssiege` (`siege_id`, `uuid`, `tardis_id`) VALUES ",

            "INSERT INTO `%ssonic` (`sonic_id`, `uuid`, `activated`, `sonic_type`, `model`, `bio`, `diamond`, `emerald`, `redstone`, `painter`, `ignite`, `arrow`) VALUES ",

            "INSERT INTO `%sstorage` (`storage_id`, `tardis_id`, `uuid`, `owner`, `saves_one`, `saves_two`, `areas`, `presets_one`, `presets_two`, `biomes_one`, `biomes_two`, `players`, `circuits`, `console`) VALUES ",

            "INSERT INTO `%st_count` (`t_id`, `uuid`, `player`, `count`, `grace`, `repair`) VALUES ",

            "INSERT INTO `%stag` (`tag_id`, `player`, `time`) VALUES ",

            "INSERT INTO `%stardis` (`tardis_id`, `uuid`, `owner`, `last_known_name`, `chunk`, `tips`, `size`, `abandoned`, `artron_level`, `replaced`, `companions`, `handbrake_on`, `iso_on`, `hidden`, `recharging`, `tardis_init`, `adapti_on`, `chameleon_preset`, `chameleon_demat`, `creeper`, `beacon`, `eps`, `rail`, `renderer`, `zero`, `powered_on`, `lights_on`, `siege_on`, `lastuse`, `monsters`) VALUES ",

            "INSERT INTO `%stransmats` (`transmat_id`, `tardis_id`, `name`, `world`, `x`, `y`, `z`, `yaw`) VALUES ",

            "INSERT INTO `%straveled_to` (`uuid`, `environment`) VALUES ",

            "INSERT INTO `%stravellers` (`traveller_id`, `tardis_id`, `uuid`, `player`) VALUES ",

            "INSERT INTO `%svaults` (`v_id`, `tardis_id`, `location`, `chest_type`, `x`, `y`, `z`) VALUES ",

            "INSERT INTO `%sthevoid` (`tardis_id`) VALUES ",

            "INSERT INTO `%svortex` (`tardis_id`, `task`) VALUES "

    );

    public static final List<String> VALUES = Arrays.asList(
            "(%s, '%s', '%s', '%s', '%s', %s)",

            "('%s', '%s', %s)",

            "(%s, '%s', '%s', '%s', %s, %s, %s, %s, '%s', '%s')",

            "(%s, '%s', '%s', %s, %s, %s, %s, %s, %s, '%s')",

            "(%s, %s, '%s', '%s', %s, %s, %s, '%s')",

            "(%s, %s, '%s', %s, %s, %s, '%s', %s)",

            "(%s, %s, %s, '%s', '%s')",

            "(%s, %s, '%s', '%s', %s)",

            "(%s, %s, '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s)",

            "(%s, %s, '%s', %s, %s)",

            "(%s, %s, '%s', %s)",

            "(%s, %s, %s, '%s', %s)",

            "(%s, %s, '%s', %s, %s, %s, '%s', %s, '%s')",

            "(%s, %s, '%s', '%s', %s, %s, %s, '%s', '%s', '%s', %s, %s, %s)",

            "(%s, %s, '%s', '%s', %s, %s, %s)",

            "(%s, %s, %s, '%s', '%s', %s)",

            "(%s, %s, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",

            "('%s', '%s')",

            "(%s, %s, '%s', %s, %s, %s)",

            "(%s, %s, '%s', %s, %s, %s, '%s', %s, '%s')",

            "(%s, '%s', '%s', %s, '%s', '%s', '%s', '%s')",

            "(%s, '%s', %s, '%s', '%s', '%s', '%s', '%s')",

            "(%s, %s, '%s')",

            "('%s')",

            "(%s, %s, '%s', %s, %s, %s, '%s', %s)",

            "(%s, '%s', '%s', %s, '%s', %s, '%s', %s, '%s', %s)",

            "(%s, '%s', '%s', '%s', %s, %s, %s, '%s', '%s', '%s', '%s', %s, %s, %s, '%s', %s, %s, '%s', '%s', '%s', %s, '%s', '%s', %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, '%s')",

            "(%s, '%s', '%s', '%s', %s, %s)",

            "(%s, '%s', '%s', '%s', '%s', %s)",

            "(%s, '%s', '%s', %s)",

            "(%s, '%s', '%s', '%s', %s, %s, %s, %s, '%s', '%s', '%s')",

            "(%s, '%s', %s)",

            "(%s, '%s', %s, '%s', %s, %s, %s, %s, %s, %s, %s, %s)",

            "(%s, %s, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",

            "(%s, '%s', '%s', %s, %s, %s)",

            "(%s, '%s', %s)",

            "(%s, '%s', '%s', '%s', '%s', %s, '%s', %s, %s, '%s', '%s', %s, %s, %s, %s, %s, %s, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, %s, %s, %s, %s)",

            "(%s, %s, '%s', '%s', %s, %s, %s, %s)",

            "(%s, %s)",

            "(%s, %s, '%s', '%s')",

            "(%s, %s, '%s', '%s', %s, %s, %s)",

            "(%s)",

            "(%s, %s)"

    );

    static final String COMMENT = "--";
    static final String DUMP = "-- Dumping data for table ";
    static final String STRUCTURE = "-- Table structure for table ";
    static final String SEPARATOR = "-- --------------------------------------------------------";
}
