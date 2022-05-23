/*
 * Copyright (C) 2022 eccentric_nz
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

import java.io.*;
import java.sql.*;
import java.util.MissingFormatArgumentException;

/**
 * @author eccentric_nz
 */
public class Main {

    public static void main(String[] args) {
        UserInterface.main(args);
    }

    /**
     * Reads an SQLite database and dumps the records as SQL statements to a file.
     *
     * @param console the output window of the tool
     * @param sqlite  the SQLite file to migrate
     * @param mysql   the SQL file to write to
     * @param prefix  the desired table prefix
     * @throws IOException if the file cannot be read
     */
    public static void process(PrintWriter console, File sqlite, File mysql, String prefix) throws IOException {
        if (!sqlite.canRead()) {
            console.println("Specified original file " + sqlite + " does not exist or cannot be read!");
            return;
        }
        if (mysql.exists()) {
            console.println("Specified output file " + mysql + " exists, please remove it before running this program!");
            return;
        }
        if (!mysql.createNewFile()) {
            console.println("Could not create specified output file " + mysql + " please ensure that it is in a valid directory which can be written to.");
            return;
        }
        if (!prefix.isEmpty()) {
            console.println("***** Using prefix: " + prefix);
        }
        console.println("***** Starting conversion process, please wait.");
        Connection connection = null;
        try {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + sqlite.getCanonicalPath());
            } catch (ClassNotFoundException e) {
                console.println("***** ERROR: SQLite JDBC driver not found!");
                return;
            }
            if (connection == null) {
                console.println("***** ERROR: Could not connect to SQLite database!");
                return;
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(mysql, false));
            bw.write("-- TARDIS SQL Dump");
            bw.newLine();
            bw.newLine();
            bw.write("SET SQL_MODE = \"NO_AUTO_VALUE_ON_ZERO\";");
            bw.newLine();
            bw.newLine();
            Statement statement = connection.createStatement();
            int i = 0;
            for (Table table : Table.values()) {
                console.println("Reading and writing " + table.toString() + " table");
                bw.write(SQL.SEPARATOR);
                bw.newLine();
                bw.newLine();
                bw.write(SQL.COMMENT);
                bw.newLine();
                bw.write(SQL.STRUCTURE + table);
                bw.newLine();
                bw.write(SQL.COMMENT);
                bw.newLine();
                bw.newLine();
                try {
                    bw.write(String.format(SQL.CREATES.get(i), prefix));
                } catch (MissingFormatArgumentException e) {
                    console.println("CREATES " + table);
                }
                bw.newLine();
                bw.newLine();
                String count = "SELECT COUNT(*) AS count FROM " + table;
                ResultSet rsc = statement.executeQuery(count);
                if (rsc.isBeforeFirst()) {
                    rsc.next();
                    int c = rsc.getInt("count");
                    console.println("Found " + c + " " + table + " records");
                    String query = "SELECT * FROM " + table;
                    ResultSet rs = statement.executeQuery(query);
                    if (rs.isBeforeFirst()) {
                        int b = 1;
                        bw.write(SQL.COMMENT);
                        bw.newLine();
                        bw.write(SQL.DUMP + table);
                        bw.newLine();
                        bw.write(SQL.COMMENT);
                        bw.newLine();
                        bw.newLine();
                        try {
                            bw.write(String.format(SQL.INSERTS.get(i), prefix));
                        } catch (MissingFormatArgumentException e) {
                            console.println("INSERT " + table);
                        }
                        bw.newLine();
                        while (rs.next()) {
                            boolean section = (b % 100 == 0);
                            String end = (b == c || section) ? ";" : ",";
                            b++;
                            String str;
                            try {
                                switch (table) {
                                    case achievements -> {
                                        String player = rs.getString("player");
                                        if (rs.wasNull()) {
                                            player = "";
                                        }
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("a_id"), rs.getString("uuid"), player, rs.getString("name"), rs.getString("amount"), rs.getInt("completed")) + end;
                                        bw.write(str);
                                    }
                                    case arched -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getString("uuid"), rs.getString("arch_name"), rs.getLong("arch_time")) + end;
                                        bw.write(str);
                                    }
                                    case archive -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("archive_id"), rs.getString("uuid"), rs.getString("name"), rs.getString("console_size"), rs.getInt("beacon"), rs.getInt("lanterns"), rs.getInt("use"), rs.getInt("y"), rs.getString("data"), rs.getString("description")) + end;
                                        bw.write(str);
                                    }
                                    case areas -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("area_id"), rs.getString("area_name"), rs.getString("world"), rs.getInt("minx"), rs.getInt("minz"), rs.getInt("maxx"), rs.getInt("maxz"), rs.getInt("y"), rs.getInt("parking_distance"), rs.getString("invisibility"), rs.getString("direction")) + end;
                                        bw.write(str);
                                    }
                                    case ars -> {
                                        String ars_player = rs.getString("player");
                                        if (rs.wasNull()) {
                                            ars_player = "";
                                        }
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("ars_id"), rs.getInt("tardis_id"), rs.getString("uuid"), ars_player, rs.getInt("ars_x_east"), rs.getInt("ars_z_south"), rs.getInt("ars_y_layer"), rs.getString("json")) + end;
                                        bw.write(str);
                                    }
                                    case back -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("back_id"), rs.getInt("tardis_id"), rs.getString("world"), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"), rs.getString("direction"), rs.getInt("submarine")) + end;
                                        bw.write(str);
                                    }
                                    case bind -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("bind_id"), rs.getInt("tardis_id"), rs.getInt("type"), rs.getString("location"), rs.getString("name")) + end;
                                        bw.write(str);
                                    }
                                    case blocks -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("b_id"), rs.getInt("tardis_id"), rs.getString("location"), rs.getString("data"), rs.getInt("police_box")) + end;
                                        bw.write(str);
                                    }
                                    case blueprint -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("bp_id"), rs.getString("uuid"), rs.getString("permission")) + end;
                                        bw.write(str);
                                    }
                                    case chameleon -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("chameleon_id"), rs.getInt("tardis_id"), rs.getString("blueprintData"), rs.getString("stainData"), rs.getString("glassData"), rs.getString("line1"), rs.getString("line2"), rs.getString("line3"), rs.getString("line4")) + end;
                                        bw.write(str);
                                    }
                                    case chunks -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("chunk_id"), rs.getInt("tardis_id"), rs.getString("world"), rs.getInt("x"), rs.getInt("z")) + end;
                                        bw.write(str);
                                    }
                                    case condenser -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("c_id"), rs.getInt("tardis_id"), rs.getString("block_data"), rs.getInt("block_count")) + end;
                                        bw.write(str);
                                    }
                                    case controls -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("c_id"), rs.getInt("tardis_id"), rs.getInt("type"), rs.getString("location"), rs.getInt("secondary")) + end;
                                        bw.write(str);
                                    }
                                    case current -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("current_id"), rs.getInt("tardis_id"), rs.getString("world"), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"), rs.getString("direction"), rs.getInt("submarine"), rs.getString("biome")) + end;
                                        bw.write(str);
                                    }
                                    case destinations -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("dest_id"), rs.getInt("tardis_id"), rs.getString("dest_name"), rs.getString("world"), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"), rs.getString("direction"), rs.getString("preset"), rs.getString("bind"), rs.getInt("type"), rs.getInt("submarine"), rs.getInt("slot"), rs.getInt("icon")) + end;
                                        bw.write(str);
                                    }
                                    case doors -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("door_id"), rs.getInt("tardis_id"), rs.getInt("door_type"), rs.getString("door_location"), rs.getString("door_direction"), rs.getInt("locked")) + end;
                                        bw.write(str);
                                    }
                                    case farming -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("farm_id"), rs.getInt("tardis_id"), rs.getString("apiary"), rs.getString("aquarium"), rs.getString("bamboo"), rs.getString("birdcage"), rs.getString("farm"), rs.getString("geode"), rs.getString("hutch"), rs.getString("igloo"), rs.getString("stable"), rs.getString("stall"), rs.getString("village")) + end;
                                        bw.write(str);
                                    }
                                    case forcefield -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getString("uuid"), rs.getString("location")) + end;
                                        bw.write(str);
                                    }
                                    case gravity_well -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("g_id"), rs.getInt("tardis_id"), rs.getString("location"), rs.getString("direction"), rs.getInt("distance"), rs.getFloat("velocity")) + end;
                                        bw.write(str);
                                    }
                                    case homes -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("home_id"), rs.getInt("tardis_id"), rs.getString("world"), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"), rs.getString("direction"), rs.getInt("submarine"), rs.getString("preset")) + end;
                                        bw.write(str);
                                    }
                                    case inventories -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("id"), rs.getString("uuid"), rs.getString("player"), rs.getInt("arch"), rs.getString("inventory"), rs.getString("armour"), rs.getString("attributes"), rs.getString("armour_attributes")) + end;
                                        bw.write(str);
                                    }
                                    case junk -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("id"), rs.getString("uuid"), rs.getInt("tardis_id"), rs.getString("save_sign"), rs.getString("handbrake"), rs.getString("wall"), rs.getString("floor"), rs.getString("preset")) + end;
                                        bw.write(str);
                                    }
                                    case lamps -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("l_id"), rs.getInt("tardis_id"), rs.getString("location")) + end;
                                        bw.write(str);
                                    }
                                    case movers -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getString("uuid")) + end;
                                        bw.write(str);
                                    }
                                    case next -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("next_id"), rs.getInt("tardis_id"), rs.getString("world"), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"), rs.getString("direction"), rs.getInt("submarine")) + end;
                                        bw.write(str);
                                    }
                                    case paper_bag -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("paper_bag_id"), rs.getString("uuid"), rs.getString("flavour_1"), rs.getInt("amount_1"), rs.getString("flavour_2"), rs.getInt("amount_2"), rs.getString("flavour_3"), rs.getInt("amount_3"), rs.getString("flavour_4"), rs.getInt("amount_4")) + end;
                                        bw.write(str);
                                    }
                                    case player_prefs -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("pp_id"), rs.getString("uuid"), rs.getString("player"), rs.getString("key"), rs.getInt("sfx_on"), rs.getInt("quotes_on"), rs.getInt("artron_level"), rs.getString("wall"), rs.getString("floor"), rs.getString("siege_wall"), rs.getString("siege_floor"), rs.getInt("auto_on"), rs.getString("auto_type"), rs.getString("auto_default"), rs.getInt("beacon_on"), rs.getInt("hads_on"), rs.getString("hads_type"), rs.getInt("build_on"), rs.getInt("close_gui_on"), rs.getInt("eps_on"), rs.getString("eps_message").replace("'", "\\'"), rs.getString("language"), rs.getInt("texture_on"), rs.getString("texture_in"), rs.getString("texture_out"), rs.getInt("submarine_on"), rs.getInt("dnd_on"), rs.getInt("minecart_on"), rs.getInt("renderer_on"), rs.getInt("wool_lights_on"), rs.getInt("sign_on"), rs.getInt("telepathy_on"), rs.getInt("travelbar_on"), rs.getInt("farm_on"), rs.getInt("lanterns_on"), rs.getInt("auto_siege_on"), rs.getInt("flying_mode"), rs.getInt("throttle"), rs.getInt("difficulty"), rs.getInt("auto_powerup_on"), rs.getInt("auto_rescue_on"), rs.getString("hum")) + end;
                                        bw.write(str);
                                    }
                                    case portals -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("portal_id"), rs.getString("portal"), rs.getString("teleport"), rs.getString("direction"), rs.getInt("tardis_id"), rs.getInt("abandoned")) + end;
                                        bw.write(str);
                                    }
                                    case programs -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("program_id"), rs.getString("uuid"), rs.getString("name"), rs.getString("inventory"), rs.getString("parsed"), rs.getInt("checked")) + end;
                                        bw.write(str);
                                    }
                                    case reminders -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("reminder_id"), rs.getString("uuid"), rs.getString("reminder"), rs.getLong("time")) + end;
                                        bw.write(str);
                                    }
                                    case room_progress -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("progress_id"), rs.getString("direction"), rs.getString("room"), rs.getInt("tardis_id"), rs.getInt("progress_row"), rs.getInt("progress_column"), rs.getInt("progress_level"), rs.getString("middle_type"), rs.getString("floor_type"), rs.getString("post_blocks")) + end;
                                        bw.write(str);
                                    }
                                    case seeds -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("seed_id"), rs.getString("schematic"), rs.getString("wall"), rs.getString("floor"), rs.getString("location")) + end;
                                        bw.write(str);
                                    }
                                    case siege -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("siege_id"), rs.getString("uuid"), rs.getInt("tardis_id")) + end;
                                        bw.write(str);
                                    }
                                    case sonic -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("sonic_id"), rs.getString("uuid"), rs.getInt("activated"), rs.getString("sonic_type"), rs.getInt("model"), rs.getInt("bio"), rs.getInt("diamond"), rs.getInt("emerald"), rs.getInt("redstone"), rs.getInt("painter"), rs.getInt("ignite"), rs.getInt("arrow"), rs.getInt("knockback"), rs.getString("sonic_uuid")) + end;
                                        bw.write(str);
                                    }
                                    case storage -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("storage_id"), rs.getInt("tardis_id"), rs.getString("uuid"), rs.getString("owner"), rs.getString("saves_one"), rs.getString("saves_two"), rs.getString("areas"), rs.getString("presets_one"), rs.getString("presets_two"), rs.getString("biomes_one"), rs.getString("biomes_two"), rs.getString("players"), rs.getString("circuits"), rs.getString("console")) + end;
                                        bw.write(str);
                                    }
                                    case t_count -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("t_id"), rs.getString("uuid"), rs.getString("player"), rs.getInt("count"), rs.getInt("grace"), rs.getInt("repair")) + end;
                                        bw.write(str);
                                    }
                                    case tag -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("tag_id"), rs.getString("player"), rs.getLong("time")) + end;
                                        bw.write(str);
                                    }
                                    case tardis -> {
                                        String replaced = rs.getString("replaced");
                                        if (rs.wasNull()) {
                                            replaced = "";
                                        }
                                        String companions = rs.getString("companions");
                                        if (rs.wasNull()) {
                                            companions = "";
                                        }
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("tardis_id"), rs.getString("uuid"), rs.getString("owner"), rs.getString("last_known_name"), rs.getString("chunk"), rs.getInt("tips"), rs.getString("size"), rs.getInt("abandoned"), rs.getInt("artron_level"), replaced, companions, rs.getInt("handbrake_on"), rs.getInt("iso_on"), rs.getInt("hidden"), rs.getInt("recharging"), rs.getInt("tardis_init"), rs.getInt("adapti_on"), rs.getString("chameleon_preset"), rs.getString("chameleon_demat"), rs.getString("creeper"), rs.getString("beacon"), rs.getString("eps"), rs.getString("rail"), rs.getString("renderer"), rs.getString("zero"), rs.getString("rotor"), rs.getInt("powered_on"), rs.getInt("lights_on"), rs.getInt("siege_on"), rs.getLong("lastuse"), rs.getInt("monsters")) + end;
                                        bw.write(str);
                                    }
                                    case transmats -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("transmat_id"), rs.getInt("tardis_id"), rs.getString("name"), rs.getString("world"), rs.getFloat("x"), rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw")) + end;
                                        bw.write(str);
                                    }
                                    case travel_stats -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("travel_stats_id"), rs.getString("travel_type"), rs.getInt("tardis_id"), rs.getString("uuid")) + end;
                                        bw.write(str);
                                    }
                                    case traveled_to -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getString("uuid"), rs.getString("environment")) + end;
                                        bw.write(str);
                                    }
                                    case travellers -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("traveller_id"), rs.getInt("tardis_id"), rs.getString("uuid"), rs.getString("player")) + end;
                                        bw.write(str);
                                    }
                                    case vaults -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("v_id"), rs.getInt("tardis_id"), rs.getString("location"), rs.getString("chest_type"), rs.getInt("x"), rs.getInt("y"), rs.getInt("z")) + end;
                                        bw.write(str);
                                    }
                                    case thevoid -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("tardis_id")) + end;
                                        bw.write(str);
                                    }
                                    case vortex -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("tardis_id"), rs.getInt("task")) + end;
                                        bw.write(str);
                                    }
                                    default -> {
                                    }
                                }
                            } catch (MissingFormatArgumentException e) {
                                console.println("VALUES " + table);
                            }
                            bw.newLine();
                            if (section) {
                                try {
                                    bw.write(String.format(SQL.INSERTS.get(i), prefix));
                                } catch (MissingFormatArgumentException e) {
                                    console.println("INSERTS " + table);
                                }
                                bw.newLine();
                            }
                        }
                    }
                }
                i++;
            }
            bw.write(SQL.SEPARATOR);
            bw.close();
        } catch (IOException ex) {
            console.println("***** Input/Output ERROR: " + ex.getMessage());
            return;
        } catch (SQLException ex) {
            console.println("***** SQL ERROR: " + ex.getMessage());
            return;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    console.println("***** SQL ERROR: " + ex.getMessage());
                }
            }
        }
        console.println("***** Your SQLite database has been converted!");
    }
}
