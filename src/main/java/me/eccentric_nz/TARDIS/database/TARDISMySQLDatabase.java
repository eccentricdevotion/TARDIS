/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 * Singleton class to get the database connection.
 *
 * Many facts, figures, and formulas are contained within the Matrix - a
 * supercomputer and micro-universe used by the High Council of the Time Lords
 * as a storehouse of knowledge to predict future events.
 *
 * @author eccentric_nz
 */
public class TARDISMySQLDatabase {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private Statement statement = null;
    private final TARDIS plugin;

    public TARDISMySQLDatabase(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates the TARDIS default tables in the database.
     */
    public void createTables() {
        try {
            statement = connection.createStatement();
            // Table structure for table 'achievements'
            String achievementsQuery = "CREATE TABLE IF NOT EXISTS achievements (a_id int(11) NOT NULL AUTO_INCREMENT, player varchar(32) DEFAULT '', `name` varchar(32) DEFAULT '', amount text, completed int(1) DEFAULT '0', PRIMARY KEY (a_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(achievementsQuery);

            // Table structure for table 'areas'
            String areasQuery = "CREATE TABLE IF NOT EXISTS areas (area_id int(11) NOT NULL AUTO_INCREMENT, area_name varchar(64) DEFAULT '', world varchar(64) DEFAULT '', minx int(7) DEFAULT '0', minz int(7) DEFAULT '0', maxx int(7) DEFAULT '0', maxz int(7) DEFAULT '0', y int(3) DEFAULT '0', PRIMARY KEY (area_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(areasQuery);

            // Table structure for table 'ars'
            String arsQuery = "CREATE TABLE IF NOT EXISTS ars (ars_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', player varchar(32) DEFAULT '', ars_x_east int(2) DEFAULT '2', ars_z_south int(2) DEFAULT '2', ars_y_layer int(1) DEFAULT '1', json text, PRIMARY KEY (ars_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(arsQuery);

            // Table structure for table 'back'
            String backQuery = "CREATE TABLE IF NOT EXISTS back (back_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', submarine int(1) DEFAULT '0', PRIMARY KEY (back_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(backQuery);

            // Table structure for table 'blocks'
            String blocksQuery = "CREATE TABLE IF NOT EXISTS blocks (b_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', location varchar(512) DEFAULT '', `block` int(6) DEFAULT '0', `data` int(6) DEFAULT '0', police_box int(1) DEFAULT '0', PRIMARY KEY (b_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(blocksQuery);

            // Table structure for table 'chunks'
            String chunksQuery = "CREATE TABLE IF NOT EXISTS chunks (chunk_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '0', x int(7) DEFAULT '0', z int(7) DEFAULT '0', PRIMARY KEY (chunk_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(chunksQuery);

// Table structure for table 'condenser'
            String condenserQuery = "CREATE TABLE IF NOT EXISTS condenser (c_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', block_data varchar(32) DEFAULT '', block_count int(11) DEFAULT '0', PRIMARY KEY (c_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(condenserQuery);

            // Table structure for table 'controls'
            String controlsQuery = "CREATE TABLE IF NOT EXISTS controls (c_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', `type` int(3) DEFAULT '0', location varchar(512) DEFAULT '', secondary int(1) DEFAULT '0', PRIMARY KEY (c_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(controlsQuery);

            // Table structure for table 'current'
            String currentQuery = "CREATE TABLE IF NOT EXISTS current (current_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', submarine int(1) DEFAULT '0', PRIMARY KEY (current_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(currentQuery);

            // Table structure for table 'destinations'
            String destinationsQuery = "CREATE TABLE IF NOT EXISTS destinations (dest_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', dest_name varchar(64) DEFAULT '', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', bind varchar(64) DEFAULT '', `type` int(3) DEFAULT '0', submarine int(1) DEFAULT '0', PRIMARY KEY (dest_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(destinationsQuery);

            // Table structure for table 'doors'
            String doorsQuery = "CREATE TABLE IF NOT EXISTS doors (door_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', door_type int(1) DEFAULT '0', door_location varchar(512) DEFAULT '', door_direction varchar(5) DEFAULT 'SOUTH', locked int(1) DEFAULT '0', PRIMARY KEY (door_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(doorsQuery);

            // Table structure for table 'gravity_well'
            String gravity_wellQuery = "CREATE TABLE IF NOT EXISTS gravity_well (g_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', location varchar(512) DEFAULT '', direction int(2) DEFAULT '0', distance int(3) DEFAULT '11', velocity float DEFAULT '0.5', PRIMARY KEY (g_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(gravity_wellQuery);

            // Table structure for table 'homes'
            String homesQuery = "CREATE TABLE IF NOT EXISTS homes (home_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', submarine int(1) DEFAULT '0', PRIMARY KEY (home_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(homesQuery);

            // Table structure for table 'lamps'
            String lampsQuery = "CREATE TABLE IF NOT EXISTS lamps (l_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', location varchar(512) DEFAULT '', PRIMARY KEY (l_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(lampsQuery);

            // Table structure for table 'next'
            String nextQuery = "CREATE TABLE IF NOT EXISTS `next` (next_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', world varchar(64) DEFAULT '', x int(7) DEFAULT '0', y int(3) DEFAULT '0', z int(7) DEFAULT '0', direction varchar(5) DEFAULT '', submarine int(1) DEFAULT '0', PRIMARY KEY (next_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(nextQuery);

            // Table structure for table 'player_prefs'
            String player_prefsQuery = "CREATE TABLE IF NOT EXISTS player_prefs (pp_id int(11) NOT NULL AUTO_INCREMENT, player varchar(32), `key_item` varchar(32) DEFAULT 'STICK', sfx_on int(1) DEFAULT '0', platform_on int(1) DEFAULT '0', quotes_on int(1) DEFAULT '0', artron_level int(11) DEFAULT '0', wall varchar(64) DEFAULT 'ORANGE_WOOL', floor varchar(64) DEFAULT 'LIGHT_GREY_WOOL', auto_on int(1) DEFAULT '0', beacon_on int(11) DEFAULT '1', hads_on int(11) DEFAULT '1', eps_on int(1) DEFAULT '0', eps_message text, lamp int(6) DEFAULT '0', texture_on int(1) DEFAULT '0', texture_in varchar(512) DEFAULT '', texture_out varchar(512) DEFAULT 'default', submarine_on int(1) DEFAULT '0', dnd_on int(1) DEFAULT '0', PRIMARY KEY (pp_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(player_prefsQuery);

            // Table structure for table 'tag'
            String tagQuery = "CREATE TABLE IF NOT EXISTS tag (tag_id int(11) NOT NULL AUTO_INCREMENT, player varchar(32) DEFAULT '', `time` int(11) DEFAULT '0', PRIMARY KEY (tag_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(tagQuery);

            // Table structure for table 'tardis'
            String tardisQuery = "CREATE TABLE IF NOT EXISTS tardis (tardis_id int(11) NOT NULL AUTO_INCREMENT, `owner` varchar(32) DEFAULT '', chunk varchar(64) DEFAULT '', tips int(4) DEFAULT '0', size varchar(32) DEFAULT '', artron_level int(11) DEFAULT '0', replaced text NULL, companions text NULL, platform text NULL, chameleon varchar(512) DEFAULT '', handbrake_on int(1) DEFAULT '1', iso_on int(1) DEFAULT '0', hidden int(1) DEFAULT '0', recharging int(1) DEFAULT '0', tardis_init int(1) DEFAULT '0', adapti_on int(1) DEFAULT '0', chamele_on int(1) DEFAULT '0', chameleon_preset varchar(32) DEFAULT 'NEW', chameleon_demat varchar(32) DEFAULT 'NEW', chameleon_id int(6) DEFAULT '35', chameleon_data int(6) DEFAULT '11', middle_id int(6) DEFAULT '35', middle_data int(6) DEFAULT '1', save_sign varchar(512) DEFAULT '', creeper varchar(512) DEFAULT '', condenser varchar(512) DEFAULT '', scanner varchar(512) DEFAULT '', farm varchar(512) DEFAULT '', stable varchar(512) DEFAULT '', beacon varchar(512) DEFAULT '', eps varchar(512) DEFAULT '', rail varchar(512) DEFAULT '', village varchar(512) DEFAULT '', lastuse bigint(20), PRIMARY KEY (tardis_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(tardisQuery);

            // Table structure for table 'travellers'
            String travellersQuery = "CREATE TABLE IF NOT EXISTS travellers (traveller_id int(11) NOT NULL AUTO_INCREMENT, tardis_id int(11) DEFAULT '0', player varchar(32) DEFAULT '', PRIMARY KEY (traveller_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(travellersQuery);

            // Table structure for table 't_count'
            String t_countQuery = "CREATE TABLE IF NOT EXISTS t_count (t_id int(11) NOT NULL AUTO_INCREMENT, player varchar(32) DEFAULT '', count int(3) DEFAULT '0', PRIMARY KEY (t_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(t_countQuery);

        } catch (SQLException e) {
            plugin.console.sendMessage(TARDIS.plugin.pluginName + "MySQL create table error: " + e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.console.sendMessage(TARDIS.plugin.pluginName + "MySQL close statement error: " + e);
            }
        }
    }
}
