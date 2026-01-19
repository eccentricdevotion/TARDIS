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
public class SQL_INSERT {

    static final List<String> LIST = List.of(

            "INSERT INTO `%sachievements` (`a_id`, `uuid`, `player`, `name`, `amount`, `completed`) VALUES ",

            "INSERT INTO `%sarched` (`uuid`, `arch_name`, `arch_time`) VALUES ",

            "INSERT INTO `%sarchive` (`archive_id`, `uuid`, `name`, `console_size`, `beacon`, `lanterns`, `use`, `y`, `data`, `description`) VALUES ",

            "INSERT INTO `%sareas` (`area_id`, `area_name`, `world`, `minx`, `minz`, `maxx`, `maxz`, `y`, `parking_distance`, `invisibility`, `direction`, `grid`) VALUES ",

            "INSERT INTO `%sarea_locations` (`area_location_id`, `area_id`, `world`, `x`, `y`, `z`) VALUES ",

            "INSERT INTO `%sars` (`ars_id`, `tardis_id`, `uuid`, `player`, `ars_x_east`, `ars_z_south`, `ars_y_layer`, `json`) VALUES ",

            "INSERT INTO `%sartron_powered` (`a_id`, `tardis_id`, `location`) VALUES ",

            "INSERT INTO `%sback` (`back_id`, `tardis_id`, `world`, `x`, `y`, `z`, `direction`, `submarine`) VALUES ",

            "INSERT INTO `%sbind` (`bind_id`, `tardis_id`, `type`, `location`, `name`) VALUES ",

            "INSERT INTO `%sblocks` (`b_id`, `tardis_id`, `location`, `data`, `police_box`) VALUES ",

            "INSERT INTO `%sblueprint` (`bp_id`, `uuid`, `permission`) VALUES ",

            "INSERT INTO `%scamera` (`c_id`, `uuid`, `location`) VALUES ",

            "INSERT INTO `%schameleon` (`chameleon_id`, `tardis_id`, `blueprintData`, `stainData`, `glassData`, `line1`, `line2`, `line3`, `line4`, `active`) VALUES ",

            "INSERT INTO `%scolour` (`colour_id`, `tardis_id`, `red`, `green`, `blue`) VALUES ",

            "INSERT INTO `%schunks` (`chunk_id`, `tardis_id`, `world`, `x`, `z`) VALUES ",

            "INSERT INTO `%scondenser` (`c_id`, `tardis_id`, `block_data`, `block_count`) VALUES ",

            "INSERT INTO `%scontrols` (`c_id`, `tardis_id`, `type`, `location`, `secondary`) VALUES ",

            "INSERT INTO `%scurrent` (`current_id`, `tardis_id`, `world`, `x`, `y`, `z`, `direction`, `submarine`, `biome`) VALUES ",

            "INSERT INTO `%scustom_preset` (`custom_id`, `tardis_id`, `preset`) VALUES ",

            "INSERT INTO `%sdeaths` (`uuid`, `world`, `x`, `y`, `z`, `direction`, `submarine`) VALUES ",

            "INSERT INTO `%sdestinations` (`dest_id`, `tardis_id`, `dest_name`, `world`, `x`, `y`, `z`, `direction`, `preset`, `bind`, `type`, `submarine`, `slot`, `icon`, `autonomous`) VALUES ",

            "INSERT INTO `%sdispersed` (`d_id`, `uuid`, `world`, `x`, `y`, `z`, `tardis_id`) VALUES ",

            "INSERT INTO `%sdoors` (`door_id`, `tardis_id`, `door_type`, `door_location`, `door_direction`, `locked`) VALUES ",

            "INSERT INTO `%seyes` (`eye_id`, `tardis_id`, `capacitors`, `damaged`, `task`) VALUES ",

            "INSERT INTO `%sfarming` (`farm_id`, `tardis_id`, `allay`, `apiary`, `aquarium`, `bamboo`, `birdcage`, `farm`, `geode`, `happy`, `hutch`, `igloo`, `iistubil`, `lava`, `mangrove`, `nautilus`, `pen`, `stable`, `stall`, `village`) VALUES ",

            "INSERT INTO `%sfarming_prefs` (`farm_id`, `uuid`, `allay`, `apiary`, `aquarium`, `bamboo`, `birdcage`, `farm`, `geode`, `happy`, `hutch`, `igloo`, `iistubil`, `lava`, `mangrove`, `nautilus`, `pen`, `stable`, `stall`, `village`) VALUES ",

            "INSERT INTO `%sflight` (`f_id`, `uuid`, `tardis_id`, `location`, `stand`, `display`) VALUES ",

            "INSERT INTO `%sforcefield` (`uuid`, `location`) VALUES ",

            "INSERT INTO `%sgames` (`game_id`, `tardis_id`, `player_location`, `tetris_board`, `tetris_sign`, `pong_display`, `pong_uuids`) VALUES ",

            "INSERT INTO `%sgardens` (`garden_id`, `tardis_id`, `world`, `minx`, `maxx`, `y`, `minz`, `maxz`) VALUES ",

            "INSERT INTO `%sgravity_well` (`g_id`, `tardis_id`, `location`, `direction`, `distance`, `velocity`) VALUES ",

            "INSERT INTO `%shappy` (`happy_id`, `tardis_id`, `slots`) VALUES ",

            "INSERT INTO `%shomes` (`home_id`, `tardis_id`, `world`, `x`, `y`, `z`, `direction`, `submarine`, `preset`) VALUES ",

            "INSERT INTO `%sinteractions` (`i_id`, `tardis_id`, `uuid`, `control`, `state`) VALUES ",

            "INSERT INTO `%sinventories` (`id`, `uuid`, `player`, `arch`, `inventory`, `armour`, `attributes`, `armour_attributes`) VALUES ",

            "INSERT INTO `%sjunk` (`id`, `uuid`, `tardis_id`, `save_sign`, `handbrake`, `wall`, `floor`, `preset`) VALUES ",

            "INSERT INTO `%slamps` (`l_id`, `tardis_id`, `location`, `material_on`, `material_off`, `percentage`) VALUES ",

            "INSERT INTO `%slight_prefs` (`lp_id`, `tardis_id`, `light`, `material`, `pattern`, `delays`, `levels`) VALUES ",

            "INSERT INTO `%smovers` (`uuid`) VALUES ",

            "INSERT INTO `%snext` (`next_id`, `tardis_id`, `world`, `x`, `y`, `z`, `direction`, `submarine`) VALUES ",

            "INSERT INTO `%spaper_bag` (`paper_bag_id`, `uuid`, `flavour_1`, `amount_1`, `flavour_2`, `amount_2`, `flavour_3`, `amount_3`, `flavour_4`, `amount_4`) VALUES ",

            "INSERT INTO `%sparticle_prefs` (`pp_id`, `uuid`, `effect`, `shape`, `density`, `speed`, `colour`, `block`, `particles_on`) VALUES ",

            "INSERT INTO `%splayer_prefs` (`pp_id`, `uuid`, `player`, `key_item`, `sfx_on`, `quotes_on`, `artron_level`, `wall`, `floor`,  `siege_wall`, `siege_floor`, `announce_repeaters_on`, `auto_on`, `auto_type`, `auto_default`, `beacon_on`, `hads_on`, `hads_type`, `build_on`, `close_gui_on`, `eps_on`, `eps_message`, `language`, `submarine_on`, `dnd_on`, `dynamic_lamps_on`, `minecart_on`, `renderer_on`, `sign_on`, `telepathy_on`, `travelbar_on`, `info_on`, `farm_on`,  `lights`, `auto_siege_on`, `flying_mode`, `throttle`, `auto_powerup_on`, `auto_rescue_on`, `hum`, `regenerations`, `regen_block_on`, `dialogs_on`, `open_display_door_on`) VALUES ",

            "INSERT INTO `%splots` (`plot_id`, `uuid`, `world`, `chunk_x`, `chunk_z`, `size`, `name`) VALUES ",

            "INSERT INTO `%sportals` (`portal_id`, `portal`, `teleport`, `direction`, `tardis_id`, `abandoned`) VALUES ",

            "INSERT INTO `%spreviewers` (`uuid`, `world`, `x`, `y`, `z`, `yaw`, `pitch`, `gamemode`, `tardis_id`) VALUES ",

            "INSERT INTO `%sprograms` (`program_id`, `uuid`, `name`, `inventory`, `parsed`, `checked`) VALUES ",

            "INSERT INTO `%sreminders` (`reminder_id`, `uuid`, `reminder`, `time`) VALUES ",

            "INSERT INTO `%sroom_progress` (`progress_id`, `direction`, `room`, `location`, `tardis_id`, `progress_row`, `progress_column`, `progress_level`, `middle_type`, `floor_type`, `post_blocks`) VALUES ",

            "INSERT INTO `%sseeds` (`seed_id`, `schematic`, `wall`, `floor`, `location`) VALUES ",

            "INSERT INTO `%ssensors` (`sensor_id`, `tardis_id`, `charging`, `flight`, `handbrake`, `malfunction`, `power`) VALUES ",

            "INSERT INTO `%ssiege` (`siege_id`, `uuid`, `tardis_id`) VALUES ",

            "INSERT INTO `%sskins` (`skin_id`, `uuid`, `skin`) VALUES ",

            "INSERT INTO `%ssonic` (`sonic_id`, `uuid`, `activated`, `model`, `bio`, `diamond`, `emerald`, `redstone`, `painter`, `ignite`, `arrow`, `knockback`, `brush`, `conversion`, `sonic_uuid`, `last_scanned`, `scan_type`) VALUES ",

            "INSERT INTO `%sstorage` (`storage_id`, `tardis_id`, `uuid`, `owner`, `saves_one`, `saves_two`, `areas`, `presets_one`, `presets_two`, `biomes_one`, `biomes_two`, `players`, `circuits`, `console`, `versions`) VALUES ",

            "INSERT INTO `%ssystem_upgrades` (`sys_id`, `tardis_id`, `uuid`, `architecture`, `chameleon`, `rooms`, `desktop`, `feature`, `saves`, `monitor`, `force_field`, `tools`, `locator`, `telepathic`, `stattenheim_remote`, `navigation`, `distance_1`, `distance_2`, `distance_3`, `inter_dimension`, `throttle`, `faster`, `rapid`, `warp`, `flight`) VALUES ",

            "INSERT INTO `%st_count` (`t_id`, `uuid`, `player`, `count`, `grace`, `repair`) VALUES ",

            "INSERT INTO `%stag` (`tag_id`, `player`, `time`) VALUES ",

            "INSERT INTO `%stardis` (`tardis_id`, `uuid`, `owner`, `last_known_name`, `chunk`, `tips`, `size`, `abandoned`, `artron_level`, `replaced`, `companions`, `handbrake_on`, `iso_on`, `hidden`, `recharging`, `tardis_init`, `adapti_on`, `chameleon_preset`, `chameleon_demat`, `creeper`, `beacon`, `eps`, `rail`, `renderer`, `zero`, `rotor`, `powered_on`, `lights_on`, `siege_on`, `lastuse`, `monsters`, `furnaces`, `bedrock`) VALUES ",

            "INSERT INTO `%stransmats` (`transmat_id`, `tardis_id`, `name`, `world`, `x`, `y`, `z`, `yaw`) VALUES ",

            "INSERT INTO `%stravel_stats` (`travel_stats_id`, `travel_type`, `tardis_id`, `uuid`) VALUES ",

            "INSERT INTO `%straveled_to` (`uuid`, `environment`) VALUES ",

            "INSERT INTO `%stravellers` (`traveller_id`, `tardis_id`, `uuid`, `player`) VALUES ",

            "INSERT INTO `%svaults` (`v_id`, `tardis_id`, `location`, `chest_type`, `x`, `y`, `z`) VALUES ",

            "INSERT INTO `%sthevoid` (`tardis_id`) VALUES ",

            "INSERT INTO `%svortex` (`tardis_id`, `task`) VALUES ",

            //shop
            "INSERT INTO `%sitems` (`item_id`, `item`, `location`, `cost`) VALUES ",

            // vortex manipulator
            "INSERT INTO `%sbeacons` (`beacon_id`, `uuid`, `location`, `block_data`) VALUES ",

            "INSERT INTO `%smanipulator` (`uuid`, `tachyon_level`) VALUES ",

            "INSERT INTO `%smessages` (`message_id`, `uuid_to`, `uuid_from`, `message`, `date`, `read`) VALUES ",

            "INSERT INTO `%ssaves` (`save_id`, `uuid`, `save_name`, `world`, `x`, `y`, `z`, `yaw`, `pitch`) VALUES ",

            "INSERT INTO `%sfollowers` (`uuid`, `owner`, `species`, `following`, `option`, `colour`, `ammo`) VALUES "

    );
    static final String COMMENT = "--";
    static final String DUMP = "-- Dumping data for table ";
    static final String STRUCTURE = "-- Table structure for table ";
    static final String SEPARATOR = "-- --------------------------------------------------------";
}
