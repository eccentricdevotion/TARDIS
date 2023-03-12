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

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.sql.*;
import java.util.MissingFormatArgumentException;

/**
 * @author eccentric_nz
 */
public class Converter implements Runnable {

    private final TARDISVortexManipulator plugin;
    private final TVMDatabase service = TVMDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final Connection sqliteConnection;
    private final String prefix;
    private final CommandSender sender;

    /**
     * Reads an SQLite database and transfers the records to a MySQL database.
     *
     * @param plugin the output window of the tool
     * @param sender the person who ran the command
     */
    public Converter(TARDISVortexManipulator plugin, CommandSender sender) throws Exception {
        this.plugin = plugin;
        this.sender = sender;
        prefix = this.plugin.getPrefix();
        sqliteConnection = getSQLiteConnection();
    }

    @Override
    public void run() {
        if (plugin.getConfig().getString("storage.database").equals("sqlite")) {
            sender.sendMessage(plugin.getPluginName() + "You need to set the database provider to 'mysql' in the config!");
            return;
        }
        if (!prefix.isEmpty()) {
            sender.sendMessage(plugin.getPluginName() + "***** Using prefix: " + prefix);
        }
        sender.sendMessage(plugin.getPluginName() + "Starting conversion process, please wait. This may cause the server to become unresponsive!");
        try {
            Statement readStatement = sqliteConnection.createStatement();
            Statement writeStatement = connection.createStatement();
            connection.setAutoCommit(false);
            int i = 0;
            for (SQL.TABLE table : SQL.TABLE.values()) {
                sender.sendMessage(plugin.getPluginName() + "Reading and writing " + table.toString() + " table");
                String count = "SELECT COUNT(*) AS count FROM " + table.toString();
                ResultSet rsc = readStatement.executeQuery(count);
                if (rsc.isBeforeFirst()) {
                    rsc.next();
                    int c = rsc.getInt("count");
                    sender.sendMessage(plugin.getPluginName() + "Found " + c + " " + table.toString() + " records");
                    String query = "SELECT * FROM " + table.toString();
                    ResultSet rs = readStatement.executeQuery(query);
                    if (rs.isBeforeFirst()) {
                        int b = 1;
                        StringBuilder sb = new StringBuilder();
                        try {
                            sb.append(String.format(SQL.INSERTS.get(i), prefix));
                        } catch (MissingFormatArgumentException e) {
                            sender.sendMessage(plugin.getPluginName() + "INSERT " + table.toString());
                        }
                        while (rs.next()) {
                            String end = (b == c) ? ";" : ",";
                            b++;
                            String str;
                            try {
                                switch (table) {
                                    case beacons -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("beacon_id"), rs.getString("uuid"), rs.getString("location"), rs.getString("block_type"), rs.getInt("data")) + end;
                                        sb.append(str);
                                    }
                                    case manipulator -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getString("uuid"), rs.getInt("tachyon_level")) + end;
                                        sb.append(str);
                                    }
                                    case messages -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("message_id"), rs.getString("uuid_to"), rs.getString("uuid_from"), rs.getString("message"), rs.getString("date"), rs.getInt("read")) + end;
                                        sb.append(str);
                                    }
                                    case saves -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("save_id"), rs.getString("uuid"), rs.getString("save_name"), rs.getString("world"), rs.getFloat("x"), rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw"), rs.getFloat("pitch")) + end;
                                        sb.append(str);
                                    }
                                    default -> {
                                    }
                                }
                            } catch (MissingFormatArgumentException e) {
                                sender.sendMessage(plugin.getPluginName() + "VALUES " + table.toString());
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
            sender.sendMessage(plugin.getPluginName() + "***** SQL ERROR: " + ex.getMessage());
            ex.printStackTrace();
            return;
        } finally {
            if (sqliteConnection != null) {
                try {
                    sqliteConnection.close();
                } catch (SQLException ex) {
                    sender.sendMessage(plugin.getPluginName() + "***** DATABASE CLOSE ERROR: " + ex.getMessage());
                }
            }
        }
        sender.sendMessage(plugin.getPluginName() + "***** Your SQLite database has been converted to MySQL!");
    }

    public Connection getSQLiteConnection() throws Exception {
        try {
            Class.forName("org.sqlite.JDBC");
            String path = plugin.getDataFolder() + File.separator + "TVM.db";
            return DriverManager.getConnection("jdbc:sqlite:" + path);
        } catch (ClassNotFoundException ignore) {
            return null;
        }
    }
}
