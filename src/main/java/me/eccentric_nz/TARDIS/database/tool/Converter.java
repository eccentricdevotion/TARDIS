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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.sql.*;
import java.util.MissingFormatArgumentException;

/**
 * @author eccentric_nz
 */
public class Converter implements Runnable {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final Connection sqliteConnection;
    private final String prefix;
    private final CommandSender sender;

    /**
     * Reads an SQLite database and inserts the records into a MySQL database.
     *
     * @param plugin an instance of the TARDIS JavaPlugin class
     * @param sender the player using the /tadmin convert_database command
     * @throws SQLException if their is an SQL error
     **/
    public Converter(TARDIS plugin, CommandSender sender) throws Exception {
        this.plugin = plugin;
        this.sender = sender;
        prefix = this.plugin.getPrefix();
        sqliteConnection = getSQLiteConnection();
    }

    @Override
    public void run() {
        if (plugin.getConfig().getString("storage.database").equals("sqlite")) {
            TARDISMessage.message(sender, plugin.getPluginName() + "You need to set the database provider to 'mysql' in the config!");
            return;
        }
        TARDISMessage.message(sender, plugin.getPluginName() + "Starting conversion process, please wait. This may cause the server to become unresponsive!");
        try {
            Statement readStatement = sqliteConnection.createStatement();
            Statement writeStatement = connection.createStatement();
            connection.setAutoCommit(false);
            int i = 0;
            for (SQL.TABLE table : SQL.TABLE.values()) {
                TARDISMessage.message(sender, plugin.getPluginName() + "Reading and writing " + table.toString() + " table");
                String count = "SELECT COUNT(*) AS count FROM " + table.toString();
                ResultSet rsc = readStatement.executeQuery(count);
                if (rsc.isBeforeFirst()) {
                    rsc.next();
                    int c = rsc.getInt("count");
                    TARDISMessage.message(sender, plugin.getPluginName() + "Found " + c + " " + table.toString() + " records");
                    String query = "SELECT * FROM " + table.toString();
                    ResultSet rs = readStatement.executeQuery(query);
                    if (rs.isBeforeFirst()) {
                        int b = 1;
                        StringBuilder sb = new StringBuilder();
                        try {
                            sb.append(String.format(SQL.INSERTS.get(i), prefix));
                        } catch (MissingFormatArgumentException e) {
                            TARDISMessage.message(sender, plugin.getPluginName() + "INSERT " + table.toString());
                        }
                        while (rs.next()) {
                            boolean section = (b % 100 == 0);
                            String end = (b == c || section) ? ";" : ", ";
                            b++;
                            String str;
                            try {
                                switch (table) {
                                    case achievements:
                                        String player = rs.getString("player");
                                        if (rs.wasNull()) {
                                            player = "";
                                        }
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("a_id"), rs.getString("uuid"), player, rs.getString("name"), rs.getString("amount"), rs.getInt("completed")) + end;
                                        sb.append(str);
                                        break;
                                    case arched:
                                        str = String.format(SQL.VALUES.get(i), rs.getString("uuid"), rs.getString("arch_name"), rs.getLong("arch_time")) + end;
                                        sb.append(str);
                                        break;
                                    case archive:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("archive_id"), rs.getString("uuid"), rs.getString("name"), rs.getString("console_size"), rs.getInt("beacon"), rs.getInt("lanterns"), rs.getInt("use"), rs.getInt("y"), rs.getString("data"), rs.getString("description")) + end;
                                        sb.append(str);
                                        break;
                                    case areas:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("area_id"), rs.getString("area_name"), rs.getString("world"), rs.getInt("minx"), rs.getInt("minz"), rs.getInt("maxx"), rs.getInt("maxz"), rs.getInt("y"), rs.getInt("parking_distance"), rs.getString("invisibility")) + end;
                                        sb.append(str);
                                        break;
                                    case ars:
                                        String ars_player = rs.getString("player");
                                        if (rs.wasNull()) {
                                            ars_player = "";
                                        }
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("ars_id"), rs.getInt("tardis_id"), rs.getString("uuid"), ars_player, rs.getInt("ars_x_east"), rs.getInt("ars_z_south"), rs.getInt("ars_y_layer"), rs.getString("json")) + end;
                                        sb.append(str);
                                        break;
                                    case back:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("back_id"), rs.getInt("tardis_id"), rs.getString("world"), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"), rs.getString("direction"), rs.getInt("submarine")) + end;
                                        sb.append(str);
                                        break;
                                    case bind:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("bind_id"), rs.getInt("tardis_id"), rs.getInt("type"), rs.getString("location"), rs.getString("name")) + end;
                                        sb.append(str);
                                        break;
                                    case blocks:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("b_id"), rs.getInt("tardis_id"), rs.getString("location"), rs.getString("data"), rs.getInt("police_box")) + end;
                                        sb.append(str);
                                        break;
                                    case chameleon:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("chameleon_id"), rs.getInt("tardis_id"), rs.getString("blueprintData"), rs.getString("stainData"), rs.getString("glassData"), rs.getString("line1"), rs.getString("line2"), rs.getString("line3"), rs.getString("line4"), rs.getInt("asymmetric")) + end;
                                        sb.append(str);
                                        break;
                                    case chunks:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("chunk_id"), rs.getInt("tardis_id"), rs.getString("world"), rs.getInt("x"), rs.getInt("z")) + end;
                                        sb.append(str);
                                        break;
                                    case condenser:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("c_id"), rs.getInt("tardis_id"), rs.getString("block_data"), rs.getInt("block_count")) + end;
                                        sb.append(str);
                                        break;
                                    case controls:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("c_id"), rs.getInt("tardis_id"), rs.getInt("type"), rs.getString("location"), rs.getInt("secondary")) + end;
                                        sb.append(str);
                                        break;
                                    case current:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("current_id"), rs.getInt("tardis_id"), rs.getString("world"), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"), rs.getString("direction"), rs.getInt("submarine"), rs.getString("biome")) + end;
                                        sb.append(str);
                                        break;
                                    case destinations:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("dest_id"), rs.getInt("tardis_id"), rs.getString("dest_name"), rs.getString("world"), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"), rs.getString("direction"), rs.getString("preset"), rs.getString("bind"), rs.getInt("type"), rs.getInt("submarine"), rs.getInt("slot")) + end;
                                        sb.append(str);
                                        break;
                                    case doors:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("door_id"), rs.getInt("tardis_id"), rs.getInt("door_type"), rs.getString("door_location"), rs.getString("door_direction"), rs.getInt("locked")) + end;
                                        sb.append(str);
                                        break;
                                    case farming:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("farm_id"), rs.getInt("tardis_id"), rs.getString("apiary"), rs.getString("aquarium"), rs.getString("bamboo"), rs.getString("birdcage"), rs.getString("farm"), rs.getString("hutch"), rs.getString("igloo"), rs.getString("stable"), rs.getString("stall"), rs.getString("village")) + end;
                                        sb.append(str);
                                        break;
                                    case forcefield:
                                        str = String.format(SQL.VALUES.get(i), rs.getString("uuid"), rs.getString("location")) + end;
                                        sb.append(str);
                                        break;
                                    case gravity_well:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("g_id"), rs.getInt("tardis_id"), rs.getString("location"), rs.getString("direction"), rs.getInt("distance"), rs.getFloat("velocity")) + end;
                                        sb.append(str);
                                        break;
                                    case homes:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("home_id"), rs.getInt("tardis_id"), rs.getString("world"), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"), rs.getString("direction"), rs.getInt("submarine"), rs.getString("preset")) + end;
                                        sb.append(str);
                                        break;
                                    case inventories:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("id"), rs.getString("uuid"), rs.getString("player"), rs.getInt("arch"), rs.getString("inventory"), rs.getString("armour"), rs.getString("attributes"), rs.getString("armour_attributes")) + end;
                                        sb.append(str);
                                        break;
                                    case junk:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("id"), rs.getString("uuid"), rs.getInt("tardis_id"), rs.getString("save_sign"), rs.getString("handbrake"), rs.getString("wall"), rs.getString("floor"), rs.getString("preset")) + end;
                                        sb.append(str);
                                        break;
                                    case lamps:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("l_id"), rs.getInt("tardis_id"), rs.getString("location")) + end;
                                        sb.append(str);
                                        break;
                                    case movers:
                                        str = String.format(SQL.VALUES.get(i), rs.getString("uuid")) + end;
                                        sb.append(str);
                                        break;
                                    case next:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("next_id"), rs.getInt("tardis_id"), rs.getString("world"), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"), rs.getString("direction"), rs.getInt("submarine")) + end;
                                        sb.append(str);
                                        break;
                                    case paper_bag:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("paper_bag_id"), rs.getString("uuid"), rs.getString("flavour_1"), rs.getInt("amount_1"), rs.getString("flavour_2"), rs.getInt("amount_2"), rs.getString("flavour_3"), rs.getInt("amount_3"), rs.getString("flavour_4"), rs.getInt("amount_4")) + end;
                                        sb.append(str);
                                        break;
                                    case player_prefs:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("pp_id"), rs.getString("uuid"), rs.getString("player"), rs.getString("key"), rs.getInt("sfx_on"), rs.getInt("quotes_on"), rs.getInt("artron_level"), rs.getString("wall"), rs.getString("floor"), rs.getString("siege_wall"), rs.getString("siege_floor"), rs.getInt("auto_on"), rs.getInt("beacon_on"), rs.getInt("hads_on"), rs.getString("hads_type"), rs.getInt("build_on"), rs.getInt("eps_on"), rs.getString("eps_message").replace("'", "\\'"), rs.getString("lamp"), rs.getString("language"), rs.getInt("texture_on"), rs.getString("texture_in"), rs.getString("texture_out"), rs.getInt("submarine_on"), rs.getInt("dnd_on"), rs.getInt("minecart_on"), rs.getInt("renderer_on"), rs.getInt("wool_lights_on"), rs.getInt("ctm_on"), rs.getInt("sign_on"), rs.getInt("telepathy_on"), rs.getInt("travelbar_on"), rs.getInt("farm_on"), rs.getInt("lanterns_on"), rs.getInt("policebox_textures_on"), rs.getInt("auto_siege_on"), rs.getInt("flying_mode"), rs.getInt("difficulty"), rs.getInt("auto_powerup_on"), rs.getInt("auto_rescue_on"), rs.getInt("font_on"), rs.getString("hum")) + end;
                                        sb.append(str);
                                        break;
                                    case portals:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("portal_id"), rs.getString("portal"), rs.getString("teleport"), rs.getString("direction"), rs.getInt("tardis_id"), rs.getInt("abandoned")) + end;
                                        sb.append(str);
                                        break;
                                    case programs:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("program_id"), rs.getString("uuid"), rs.getString("name"), rs.getString("inventory"), rs.getString("parsed"), rs.getInt("checked")) + end;
                                        sb.append(str);
                                        break;
                                    case reminders:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("reminder_id"), rs.getString("uuid"), rs.getString("reminder"), rs.getLong("time")) + end;
                                        sb.append(str);
                                        break;
                                    case room_progress:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("progress_id"), rs.getString("direction"), rs.getString("room"), rs.getInt("tardis_id"), rs.getInt("progress_row"), rs.getInt("progress_column"), rs.getInt("progress_level"), rs.getString("middle_type"), rs.getString("floor_type")) + end;
                                        sb.append(str);
                                        break;
                                    case siege:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("siege_id"), rs.getString("uuid"), rs.getInt("tardis_id")) + end;
                                        sb.append(str);
                                        break;
                                    case sonic:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("sonic_id"), rs.getString("uuid"), rs.getInt("activated"), rs.getString("sonic_type"), rs.getInt("model"), rs.getInt("bio"), rs.getInt("diamond"), rs.getInt("emerald"), rs.getInt("redstone"), rs.getInt("painter"), rs.getInt("ignite"), rs.getInt("arrow"), rs.getInt("knockback")) + end;
                                        sb.append(str);
                                        break;
                                    case storage:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("storage_id"), rs.getInt("tardis_id"), rs.getString("uuid"), rs.getString("owner"), rs.getString("saves_one"), rs.getString("saves_two"), rs.getString("areas"), rs.getString("presets_one"), rs.getString("presets_two"), rs.getString("biomes_one"), rs.getString("biomes_two"), rs.getString("players"), rs.getString("circuits"), rs.getString("console")) + end;
                                        sb.append(str);
                                        break;
                                    case t_count:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("t_id"), rs.getString("uuid"), rs.getString("player"), rs.getInt("count"), rs.getInt("grace"), rs.getInt("repair")) + end;
                                        sb.append(str);
                                        break;
                                    case tag:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("tag_id"), rs.getString("player"), rs.getLong("time")) + end;
                                        sb.append(str);
                                        break;
                                    case tardis:
                                        String replaced = rs.getString("replaced");
                                        if (rs.wasNull()) {
                                            replaced = "";
                                        }
                                        String companions = rs.getString("companions");
                                        if (rs.wasNull()) {
                                            companions = "";
                                        }
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("tardis_id"), rs.getString("uuid"), rs.getString("owner"), rs.getString("last_known_name"), rs.getString("chunk"), rs.getInt("tips"), rs.getString("size"), rs.getInt("abandoned"), rs.getInt("artron_level"), replaced, companions, rs.getInt("handbrake_on"), rs.getInt("iso_on"), rs.getInt("hidden"), rs.getInt("recharging"), rs.getInt("tardis_init"), rs.getInt("adapti_on"), rs.getString("chameleon_preset"), rs.getString("chameleon_demat"), rs.getString("creeper"), rs.getString("beacon"), rs.getString("eps"), rs.getString("rail"), rs.getString("renderer"), rs.getString("zero"), rs.getString("rotor"), rs.getInt("powered_on"), rs.getInt("lights_on"), rs.getInt("siege_on"), rs.getLong("lastuse"), rs.getInt("monsters")) + end;
                                        sb.append(str);
                                        break;
                                    case transmats:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("transmat_id"), rs.getInt("tardis_id"), rs.getString("name"), rs.getString("world"), rs.getFloat("x"), rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw")) + end;
                                        sb.append(str);
                                        break;
                                    case traveled_to:
                                        str = String.format(SQL.VALUES.get(i), rs.getString("uuid"), rs.getString("environment")) + end;
                                        sb.append(str);
                                        break;
                                    case travellers:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("traveller_id"), rs.getInt("tardis_id"), rs.getString("uuid"), rs.getString("player")) + end;
                                        sb.append(str);
                                        break;
                                    case vaults:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("v_id"), rs.getInt("tardis_id"), rs.getString("location"), rs.getString("chest_type"), rs.getInt("x"), rs.getInt("y"), rs.getInt("z")) + end;
                                        sb.append(str);
                                        break;
                                    case thevoid:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("tardis_id")) + end;
                                        sb.append(str);
                                        break;
                                    case vortex:
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("tardis_id"), rs.getInt("task")) + end;
                                        sb.append(str);
                                        break;
                                    default:
                                        break;
                                }
                            } catch (MissingFormatArgumentException e) {
                                TARDISMessage.message(sender, plugin.getPluginName() + "VALUES " + table.toString());
                            }
                            if (section) {
                                // only one statement per add to batch operation
                                writeStatement.addBatch(sb.toString());
                                // reset the string builder
                                sb.setLength(0);
                                try {
                                    sb.append(String.format(SQL.INSERTS.get(i), prefix));
                                } catch (MissingFormatArgumentException e) {
                                    TARDISMessage.message(sender, plugin.getPluginName() + "INSERTS " + table.toString());
                                }
                            }
                        }
                        String insert = sb.toString();
                        writeStatement.addBatch(insert);
                    }
                }
                i++;
            }
            writeStatement.executeBatch();
            connection.setAutoCommit(true);
        } catch (SQLException ex) {
            TARDISMessage.message(sender, plugin.getPluginName() + "***** SQL ERROR: " + ex.getMessage());
            return;
        } finally {
            if (sqliteConnection != null) {
                try {
                    sqliteConnection.close();
                } catch (SQLException ex) {
                    TARDISMessage.message(sender, plugin.getPluginName() + "***** SQL ERROR: " + ex.getMessage());
                }
            }
        }
        TARDISMessage.message(sender, plugin.getPluginName() + "***** Your SQLite database has been converted to MySQL!");
    }

    public Connection getSQLiteConnection() throws Exception {
        try {
            Class.forName("org.sqlite.JDBC");
            String path = plugin.getDataFolder() + File.separator + "TARDIS.db";
            return DriverManager.getConnection("jdbc:sqlite:" + path);
        } catch (ClassNotFoundException ignore) {
            return null;
        }
    }
}
