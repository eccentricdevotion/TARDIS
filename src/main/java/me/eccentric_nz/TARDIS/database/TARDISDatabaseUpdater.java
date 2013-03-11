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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;

/**
 * TARDISes prefer the environment of the Space-Time Vortex to the four
 * dimensional world. They have Curiosity Circuits to encourage them to leave
 * the Vortex.
 *
 * @author eccentric_nz
 */
public class TARDISDatabaseUpdater {

    private List<String> areaupdates = new ArrayList<String>();
    private List<String> blockupdates = new ArrayList<String>();
    private List<String> destupdates = new ArrayList<String>();
    private List<String> doorupdates = new ArrayList<String>();
    private List<String> gravityupdates = new ArrayList<String>();
    private List<String> prefsupdates = new ArrayList<String>();
    private List<String> tardisupdates = new ArrayList<String>();
    private Statement statement;

    public TARDISDatabaseUpdater(Statement statement) {
        this.statement = statement;
        areaupdates.add("y INTEGER");
        blockupdates.add("police_box INTEGER DEFAULT 0");
        destupdates.add("bind TEXT DEFAULT ''");
        destupdates.add("type INTEGER DEFAULT 0");
        doorupdates.add("locked INTEGER DEFAULT 0");
        gravityupdates.add("distance INTEGER DEFAULT 11");
        gravityupdates.add("velocity REAL DEFAULT 0.5");
        prefsupdates.add("key TEXT DEFAULT ''");
        prefsupdates.add("artron_level INTEGER DEFAULT 0");
        prefsupdates.add("auto_on INTEGER DEFAULT 0");
        tardisupdates.add("artron_button TEXT DEFAULT ''");
        tardisupdates.add("artron_level INTEGER DEFAULT 0");
        tardisupdates.add("chameleon_id INTEGER DEFAULT 35");
        tardisupdates.add("chameleon_data INTEGER DEFAULT 11");
        tardisupdates.add("condenser TEXT DEFAULT ''");
        tardisupdates.add("creeper TEXT DEFAULT ''");
        tardisupdates.add("handbrake TEXT DEFAULT ''");
        tardisupdates.add("handbrake_on INT DEFAULT 1");
        tardisupdates.add("middle_data INTEGER");
        tardisupdates.add("middle_id INTEGER");
        tardisupdates.add("recharging INTEGER DEFAULT 0");
        tardisupdates.add("scanner TEXT DEFAULT ''");
        tardisupdates.add("tardis_init INTEGER DEFAULT 0");
        tardisupdates.add("farm TEXT default ''");
    }

    /**
     * Adds new fields to tables in the database.
     */
    public void updateTables() {
        int i = 0;
        try {
            for (String a : areaupdates) {
                String a_query = "SELECT sql FROM sqlite_master WHERE tbl_name = 'areas' AND sql LIKE '%" + a + "%'";
                ResultSet rsa = statement.executeQuery(a_query);
                if (!rsa.next()) {
                    i++;
                    String a_alter = "ALTER TABLE areas ADD " + a;
                    statement.executeUpdate(a_alter);
                }
            }
            for (String b : blockupdates) {
                String b_query = "SELECT sql FROM sqlite_master WHERE tbl_name = 'blocks' AND sql LIKE '%" + b + "%'";
                ResultSet rsb = statement.executeQuery(b_query);
                if (!rsb.next()) {
                    i++;
                    String b_alter = "ALTER TABLE blocks ADD " + b;
                    statement.executeUpdate(b_alter);
                }
            }
            for (String d : destupdates) {
                String d_query = "SELECT sql FROM sqlite_master WHERE tbl_name = 'destinations' AND sql LIKE '%" + d + "%'";
                ResultSet rsd = statement.executeQuery(d_query);
                if (!rsd.next()) {
                    i++;
                    String d_alter = "ALTER TABLE destinations ADD " + d;
                    statement.executeUpdate(d_alter);
                }
            }
            for (String o : doorupdates) {
                String o_query = "SELECT sql FROM sqlite_master WHERE tbl_name = 'doors' AND sql LIKE '%" + o + "%'";
                ResultSet rso = statement.executeQuery(o_query);
                if (!rso.next()) {
                    i++;
                    String o_alter = "ALTER TABLE doors ADD " + o;
                    statement.executeUpdate(o_alter);
                }
            }
            for (String g : gravityupdates) {
                String g_query = "SELECT sql FROM sqlite_master WHERE tbl_name = 'gravity_well' AND sql LIKE '%" + g + "%'";
                ResultSet rsg = statement.executeQuery(g_query);
                if (!rsg.next()) {
                    i++;
                    String g_alter = "ALTER TABLE gravity_well ADD " + g;
                    statement.executeUpdate(g_alter);
                }
            }
            for (String p : prefsupdates) {
                String p_query = "SELECT sql FROM sqlite_master WHERE tbl_name = 'player_prefs' AND sql LIKE '%" + p + "%'";
                ResultSet rsp = statement.executeQuery(p_query);
                if (!rsp.next()) {
                    i++;
                    String p_alter = "ALTER TABLE player_prefs ADD " + p;
                    statement.executeUpdate(p_alter);
                }
            }
            String ppw_query = "SELECT sql FROM sqlite_master WHERE tbl_name = 'player_prefs' AND sql LIKE '%wall TEXT DEFAULT%'";
            ResultSet rsppw = statement.executeQuery(ppw_query);
            if (!rsppw.next()) {
                String ppw_alter = "ALTER TABLE player_prefs ADD wall TEXT DEFAULT 'ORANGE_WOOL'";
                statement.executeUpdate(ppw_alter);
                i++;
            }
            String ppf_query = "SELECT sql FROM sqlite_master WHERE tbl_name = 'player_prefs' AND sql LIKE '%floor TEXT DEFAULT%'";
            ResultSet rsppf = statement.executeQuery(ppf_query);
            if (!rsppf.next()) {
                String ppf_alter = "ALTER TABLE player_prefs ADD floor TEXT DEFAULT 'LIGHT_GREY_WOOL'";
                statement.executeUpdate(ppf_alter);
                i++;
            }
            for (String t : tardisupdates) {
                String t_query = "SELECT sql FROM sqlite_master WHERE tbl_name = 'tardis' AND sql LIKE '%" + t + "%'";
                ResultSet rst = statement.executeQuery(t_query);
                if (!rst.next()) {
                    i++;
                    String t_alter = "ALTER TABLE tardis ADD " + t;
                    statement.executeUpdate(t_alter);
                }
            }
        } catch (SQLException e) {
            TARDIS.plugin.debug("Database add fields error: " + e.getMessage());
        }
        if (i > 0) {
            TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " fields to the database!");
        }
    }
}
