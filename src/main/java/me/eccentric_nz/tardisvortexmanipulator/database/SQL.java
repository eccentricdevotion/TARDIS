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
package me.eccentric_nz.tardisvortexmanipulator.database;

import java.util.Arrays;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class SQL {

    public static final List<String> CREATES = Arrays.asList(
            "CREATE TABLE IF NOT EXISTS %sbeacons (beacon_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', location varchar(512) DEFAULT '', block_type varchar(32) DEFAULT '', data int(2) DEFAULT '0', PRIMARY KEY (beacon_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %smanipulator (uuid varchar(48) NOT NULL, tachyon_level int(11) DEFAULT '0', PRIMARY KEY (uuid)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %smessages (message_id int(11) NOT NULL AUTO_INCREMENT, uuid_to varchar(48) DEFAULT '', uuid_from varchar(48) DEFAULT '', message text NULL, date bigint(20), `read` int(1) DEFAULT '0', PRIMARY KEY (message_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;",
            "CREATE TABLE IF NOT EXISTS %ssaves (save_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', save_name varchar(64) DEFAULT '', world varchar(64) DEFAULT '', x float DEFAULT '0', y float DEFAULT '0', z float DEFAULT '0', yaw float DEFAULT '0', pitch float DEFAULT '0', PRIMARY KEY (save_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;"
    );
    public static final List<String> INSERTS = Arrays.asList(
            "INSERT INTO `%sbeacons` (`beacon_id`, `uuid`, `location`, `block_type`, `data`) VALUES ",
            "INSERT INTO `%smanipulator` (`uuid`, `tachyon_level`) VALUES ",
            "INSERT INTO `%smessages` (`message_id`, `uuid_to`, `uuid_from`, `message`, `date`, `read`) VALUES ",
            "INSERT INTO `%ssaves` (`save_id`, `uuid`, `save_name`, `world`, `x`, `y`, `z`, `yaw`, `pitch`) VALUES "
    );
    public static final List<String> VALUES = Arrays.asList(
            "(%s, '%s', '%s', '%s', %s)",
            "('%s', %s)",
            "(%s, '%s', '%s', '%s', '%s', %s)",
            "(%s, '%s', '%s', '%s', %s, %s, %s, %s, %s)"
    );
    public static final String COMMENT = "--";
    public static final String DUMP = "-- Dumping data for table ";
    public static final String STRUCTURE = "-- Table structure for table ";
    public static final String SEPARATOR = "-- --------------------------------------------------------";

    public static enum TABLE {

        beacons,
        manipulator,
        messages,
        saves
    }
}
