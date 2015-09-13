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
package me.eccentric_nz.TARDIS.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
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
public class TARDISSQLiteDatabaseUpdater {

    private final List<String> areaupdates = new ArrayList<String>();
    private final List<String> blockupdates = new ArrayList<String>();
    private final List<String> countupdates = new ArrayList<String>();
    private final List<String> destupdates = new ArrayList<String>();
    private final List<String> doorupdates = new ArrayList<String>();
    private final List<String> gravityupdates = new ArrayList<String>();
    private final List<String> prefsupdates = new ArrayList<String>();
    private final List<String> tardisupdates = new ArrayList<String>();
    private final List<String> inventoryupdates = new ArrayList<String>();
    private final List<String> uuidUpdates = Arrays.asList("achievements", "ars", "player_prefs", "storage", "t_count", "tardis", "travellers");
    private final long now = System.currentTimeMillis();
    private final Statement statement;
    private final TARDIS plugin;
    private final String prefix;

    public TARDISSQLiteDatabaseUpdater(TARDIS plugin, Statement statement) {
        this.plugin = plugin;
        this.prefix = this.plugin.getPrefix();
        this.statement = statement;
        areaupdates.add("y INTEGER");
        areaupdates.add("parking_distance INTEGER DEFAULT 2");
        blockupdates.add("police_box INTEGER DEFAULT 0");
        countupdates.add("grace INTEGER DEFAULT 0");
        destupdates.add("bind TEXT DEFAULT ''");
        destupdates.add("type INTEGER DEFAULT 0");
        destupdates.add("direction TEXT DEFAULT ''");
        destupdates.add("submarine INTEGER DEFAULT 0");
        destupdates.add("slot INTEGER DEFAULT '-1'");
        doorupdates.add("locked INTEGER DEFAULT 0");
        gravityupdates.add("distance INTEGER DEFAULT 11");
        gravityupdates.add("velocity REAL DEFAULT 0.5");
        prefsupdates.add("artron_level INTEGER DEFAULT 0");
        prefsupdates.add("auto_on INTEGER DEFAULT 0");
        prefsupdates.add("auto_siege_on INTEGER DEFAULT 0");
        prefsupdates.add("beacon_on INTEGER DEFAULT 1");
        prefsupdates.add("build_on INTEGER DEFAULT 1");
        prefsupdates.add("ctm_on INTEGER DEFAULT 0");
        prefsupdates.add("difficulty INTEGER DEFAULT 0");
        prefsupdates.add("dnd_on INTEGER DEFAULT 0");
        prefsupdates.add("eps_message TEXT DEFAULT ''");
        prefsupdates.add("eps_on INTEGER DEFAULT 0");
        prefsupdates.add("farm_on INTEGER DEFAULT 0");
        prefsupdates.add("floor TEXT DEFAULT 'LIGHT_GREY_WOOL'");
        prefsupdates.add("flying_mode INTEGER DEFAULT 1");
        prefsupdates.add("hads_on INTEGER DEFAULT 1");
        prefsupdates.add("key TEXT DEFAULT ''");
        prefsupdates.add("lamp INTEGER");
        prefsupdates.add("language TEXT DEFAULT 'AUTO_DETECT'");
        prefsupdates.add("lanterns_on INTEGER DEFAULT 0");
        prefsupdates.add("minecart_on INTEGER DEFAULT 0");
        prefsupdates.add("renderer_on INTEGER DEFAULT 1");
        prefsupdates.add("siege_floor TEXT DEFAULT 'BLACK_CLAY'");
        prefsupdates.add("siege_wall TEXT DEFAULT 'GREY_CLAY'");
        prefsupdates.add("sign_on INTEGER DEFAULT 1");
        prefsupdates.add("submarine_on INTEGER DEFAULT 0");
        prefsupdates.add("texture_in TEXT DEFAULT ''");
        prefsupdates.add("texture_on INTEGER DEFAULT 0");
        prefsupdates.add("texture_out TEXT DEFAULT 'default'");
        prefsupdates.add("travelbar_on INTEGER DEFAULT 0");
        prefsupdates.add("wall TEXT DEFAULT 'ORANGE_WOOL'");
        prefsupdates.add("wool_lights_on INTEGER DEFAULT 0");
        tardisupdates.add("adapti_on INTEGER DEFAULT 0");
        tardisupdates.add("artron_level INTEGER DEFAULT 0");
        tardisupdates.add("beacon TEXT DEFAULT ''");
        tardisupdates.add("chameleon_data INTEGER DEFAULT 11");
        tardisupdates.add("chameleon_id INTEGER DEFAULT 35");
        tardisupdates.add("chameleon_preset TEXT DEFAULT 'NEW'");
        tardisupdates.add("chameleon_demat TEXT DEFAULT 'NEW'");
        tardisupdates.add("condenser TEXT DEFAULT ''");
        tardisupdates.add("creeper TEXT DEFAULT ''");
        tardisupdates.add("eps TEXT DEFAULT ''");
        tardisupdates.add("farm TEXT DEFAULT ''");
        tardisupdates.add("handbrake_on INTEGER DEFAULT 1");
        tardisupdates.add("hutch TEXT DEFAULT ''");
        tardisupdates.add("hidden INTEGER DEFAULT 0");
        tardisupdates.add("iso_on INTEGER DEFAULT 0");
        tardisupdates.add("lastuse INTEGER DEFAULT " + now);
        tardisupdates.add("powered_on INTEGER DEFAULT 0");
        tardisupdates.add("lights_on INTEGER DEFAULT 1");
        tardisupdates.add("rail TEXT DEFAULT ''");
        tardisupdates.add("recharging INTEGER DEFAULT 0");
        tardisupdates.add("renderer TEXT DEFAULT ''");
        tardisupdates.add("scanner TEXT DEFAULT ''");
        tardisupdates.add("siege_on INTEGER DEFAULT 0");
        tardisupdates.add("stable TEXT DEFAULT ''");
        tardisupdates.add("tardis_init INTEGER DEFAULT 0");
        tardisupdates.add("tips INTEGER DEFAULT '-1'");
        tardisupdates.add("village TEXT DEFAULT ''");
        tardisupdates.add("zero TEXT DEFAULT ''");
        tardisupdates.add("last_known_name TEXT COLLATE NOCASE DEFAULT ''");
        inventoryupdates.add("attributes TEXT DEFAULT ''");
        inventoryupdates.add("armour_attributes TEXT DEFAULT ''");
    }

    /**
     * Adds new fields to tables in the database.
     */
    public void updateTables() {
        int i = 0;
        try {
            for (String u : uuidUpdates) {
                String a_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + u + "' AND sql LIKE '%uuid%'";
                ResultSet rsu = statement.executeQuery(a_query);
                if (!rsu.next()) {
                    i++;
                    String u_alter = "ALTER TABLE " + prefix + u + " ADD uuid TEXT DEFAULT ''";
                    statement.executeUpdate(u_alter);
                }
            }
            for (String a : areaupdates) {
                String[] asplit = a.split(" ");
                String acheck = asplit[0] + " " + asplit[1].substring(0, 3);
                String a_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "areas' AND sql LIKE '%" + acheck + "%'";
                ResultSet rsa = statement.executeQuery(a_query);
                if (!rsa.next()) {
                    i++;
                    String a_alter = "ALTER TABLE " + prefix + "areas ADD " + a;
                    statement.executeUpdate(a_alter);
                }
            }
            for (String b : blockupdates) {
                String[] bsplit = b.split(" ");
                String b_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "blocks' AND sql LIKE '%" + bsplit[0] + "%'";
                ResultSet rsb = statement.executeQuery(b_query);
                if (!rsb.next()) {
                    i++;
                    String b_alter = "ALTER TABLE " + prefix + "blocks ADD " + b;
                    statement.executeUpdate(b_alter);
                }
            }
            for (String c : countupdates) {
                String[] csplit = c.split(" ");
                String c_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "t_count' AND sql LIKE '%" + csplit[0] + "%'";
                ResultSet rsc = statement.executeQuery(c_query);
                if (!rsc.next()) {
                    i++;
                    String c_alter = "ALTER TABLE " + prefix + "t_count ADD " + c;
                    statement.executeUpdate(c_alter);
                }
            }
            for (String d : destupdates) {
                String[] dsplit = d.split(" ");
                String d_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "destinations' AND sql LIKE '%" + dsplit[0] + "%'";
                ResultSet rsd = statement.executeQuery(d_query);
                if (!rsd.next()) {
                    i++;
                    String d_alter = "ALTER TABLE " + prefix + "destinations ADD " + d;
                    statement.executeUpdate(d_alter);
                }
            }
            for (String o : doorupdates) {
                String[] osplit = o.split(" ");
                String o_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "doors' AND sql LIKE '%" + osplit[0] + "%'";
                ResultSet rso = statement.executeQuery(o_query);
                if (!rso.next()) {
                    i++;
                    String o_alter = "ALTER TABLE " + prefix + "doors ADD " + o;
                    statement.executeUpdate(o_alter);
                }
            }
            for (String g : gravityupdates) {
                String[] gsplit = g.split(" ");
                String g_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "gravity_well' AND sql LIKE '%" + gsplit[0] + "%'";
                ResultSet rsg = statement.executeQuery(g_query);
                if (!rsg.next()) {
                    i++;
                    String g_alter = "ALTER TABLE " + prefix + "gravity_well ADD " + g;
                    statement.executeUpdate(g_alter);
                }
            }
            for (String p : prefsupdates) {
                String[] psplit = p.split(" ");
                String pcheck = psplit[0] + " " + psplit[1].substring(0, 3);
                String p_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "player_prefs' AND sql LIKE '%" + pcheck + "%'";
                ResultSet rsp = statement.executeQuery(p_query);
                if (!rsp.next()) {
                    i++;
                    String p_alter = "ALTER TABLE " + prefix + "player_prefs ADD " + p;
                    statement.executeUpdate(p_alter);
                }
            }
            for (String t : tardisupdates) {
                String[] tsplit = t.split(" ");
                String t_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "tardis' AND sql LIKE '%" + tsplit[0] + "%'";
                ResultSet rst = statement.executeQuery(t_query);
                if (!rst.next()) {
                    i++;
                    String t_alter = "ALTER TABLE " + prefix + "tardis ADD " + t;
                    statement.executeUpdate(t_alter);
                }
            }
            for (String v : inventoryupdates) {
                String[] vsplit = v.split(" ");
                String v_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "inventories' AND sql LIKE '%" + vsplit[0] + "%'";
                ResultSet rsv = statement.executeQuery(v_query);
                if (!rsv.next()) {
                    i++;
                    String v_alter = "ALTER TABLE " + prefix + "inventories ADD " + v;
                    statement.executeUpdate(v_alter);
                }
            }
            // add biome to current location
            String bio_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "current' AND sql LIKE '%biome%'";
            ResultSet rsbio = statement.executeQuery(bio_query);
            if (!rsbio.next()) {
                i++;
                String bio_alter = "ALTER TABLE " + prefix + "current ADD biome TEXT DEFAULT ''";
                statement.executeUpdate(bio_alter);
            }
        } catch (SQLException e) {
            plugin.debug("SQLite database add fields error: " + e.getMessage() + e.getErrorCode());
        }
        if (i > 0) {
            plugin.getConsole().sendMessage(TARDIS.plugin.getPluginName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " fields to the SQLite database!");
        }
    }
}
