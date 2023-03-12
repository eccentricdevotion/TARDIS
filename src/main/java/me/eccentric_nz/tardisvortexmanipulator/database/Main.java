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

import java.io.*;
import java.sql.*;

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
     * @throws IOException
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
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(mysql, false))) {
                bw.write("-- TARDISVortexManipulator SQL Dump");
                bw.newLine();
                bw.newLine();
                bw.write("SET SQL_MODE = \"NO_AUTO_VALUE_ON_ZERO\";");
                bw.newLine();
                bw.newLine();
                Statement statement = connection.createStatement();
                int i = 0;
                for (SQL.TABLE table : SQL.TABLE.values()) {
                    console.println("Reading and writing " + table.toString() + " table");
                    bw.write(SQL.SEPARATOR);
                    bw.newLine();
                    bw.newLine();
                    bw.write(SQL.COMMENT);
                    bw.newLine();
                    bw.write(SQL.STRUCTURE + table.toString());
                    bw.newLine();
                    bw.write(SQL.COMMENT);
                    bw.newLine();
                    bw.newLine();
                    bw.write(String.format(SQL.CREATES.get(i), prefix));
                    bw.newLine();
                    bw.newLine();
                    String count = "SELECT COUNT(*) AS count FROM " + table.toString();
                    ResultSet rsc = statement.executeQuery(count);
                    if (rsc.isBeforeFirst()) {
                        rsc.next();
                        int c = rsc.getInt("count");
                        console.println("Found " + c + " " + table.toString() + " records");
                        String query = "SELECT * FROM " + table.toString();
                        ResultSet rs = statement.executeQuery(query);
                        if (rs.isBeforeFirst()) {
                            int b = 1;
                            bw.write(SQL.COMMENT);
                            bw.newLine();
                            bw.write(SQL.DUMP + table.toString());
                            bw.newLine();
                            bw.write(SQL.COMMENT);
                            bw.newLine();
                            bw.newLine();
                            bw.write(String.format(SQL.INSERTS.get(i), prefix));
                            bw.newLine();
                            while (rs.next()) {
                                String end = (b == c) ? ";" : ",";
                                b++;
                                String str;
                                switch (table) {
                                    case beacons -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("beacon_id"), rs.getString("uuid"), rs.getString("location"), rs.getString("block_type"), rs.getInt("data")) + end;
                                        bw.write(str);
                                    }
                                    case manipulator -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getString("uuid"), rs.getInt("tachyon_level")) + end;
                                        bw.write(str);
                                    }
                                    case messages -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("message_id"), rs.getString("uuid_to"), rs.getString("uuid_from"), rs.getString("message"), rs.getString("date"), rs.getInt("read")) + end;
                                        bw.write(str);
                                    }
                                    case saves -> {
                                        str = String.format(SQL.VALUES.get(i), rs.getInt("save_id"), rs.getString("uuid"), rs.getString("save_name"), rs.getString("world"), rs.getFloat("x"), rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw"), rs.getFloat("pitch")) + end;
                                        bw.write(str);
                                    }
                                    default -> {
                                    }
                                }
                                bw.newLine();
                            }
                        }
                    }
                    i++;
                }
                bw.write(SQL.SEPARATOR);
            }
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
