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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;

/**
 * Do basic SQL INSERT, UPDATE and DELETE queries.
 *
 * @author eccentric_nz
 */
public class QueryFactory {

    private TARDIS plugin;

    public QueryFactory(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Inserts data into an SQLite database table. This method executes the SQL
     * in a separate thread.
     *
     * @param table the database table name to insert the data into.
     * @param data a HashMap<String, Object> of table fields and values to
     * insert.
     * @return the number of records that were inserted
     */
    public int doInsert(String table, HashMap<String, Object> data) {
        TARDISSQLInsert insert = new TARDISSQLInsert(plugin, table, data);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, insert);
        return insert.getNum();
    }

    /**
     * Updates data in an SQLite database table. This method executes the SQL in
     * a separate thread.
     *
     * @param table the database table name to update.
     * @param data a HashMap<String, Object> of table fields and values update.
     * @param where a HashMap<String, Object> of table fields and values to
     * select the records to update.
     */
    public void doUpdate(String table, HashMap<String, Object> data, HashMap<String, Object> where) {
        TARDISSQLUpdate update = new TARDISSQLUpdate(plugin, table, data, where);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, update);
    }

    /**
     * Deletes rows from an SQLite database table. This method executes the SQL
     * in a separate thread.
     *
     * @param table the database table name to insert the data into.
     * @param where a HashMap<String, Object> of table fields and values to
     * select the records to delete.
     * @return true or false depending on whether the data was deleted
     * successfully
     */
    public boolean doDelete(String table, HashMap<String, Object> where) {
        TARDISSQLDelete delete = new TARDISSQLDelete(plugin, table, where);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, delete);
        return delete.success();
    }

    /**
     * Adds or removes Artron Energy from an SQLite database table. This method
     * executes the SQL in a separate thread.
     *
     * @param table the database table name to insert the data into.
     * @param amount the amount of energy to add or remove (use a negative
     * value)
     * @param where a HashMap<String, Object> of table fields and values to
     * select the records to alter.
     * @param p the player who receives the success message.
     */
    public void alterEnergyLevel(String table, int amount, HashMap<String, Object> where, Player p) {
        TARDISSQLAlterEnergy alter = new TARDISSQLAlterEnergy(plugin, table, amount, where, p);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, alter);
    }

    /**
     * Removes condenser block counts from an SQLite database table. This method
     * executes the SQL in a separate thread.
     *
     * @param amount the amount of blocks to remove
     * @param where a HashMap<String, Object> of table fields and values to
     * select the records to alter.
     */
    public void alterCondenserBlockCount(int amount, HashMap<String, Object> where) {
        TARDISSQLCondenserUpdate condense = new TARDISSQLCondenserUpdate(plugin, amount, where);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, condense);
    }

    /**
     * Inserts data into an SQLite database table. This method executes the SQL
     * in a separate thread.
     *
     * @param id the database table name to insert the data into.
     * @param type the type of control to insert.
     * @param l the string location of the control
     * @param s what level the control is (1 primary, 2 secondary, 3 tertiary)
     */
    public void insertControl(int id, int type, String l, int s) {
        TARDISSQLInsertControl control = new TARDISSQLInsertControl(plugin, id, type, l, s);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, control);
    }

    /**
     * Inserts data into an SQLite database table. This method executes the SQL
     * in a separate thread.
     *
     * @param data a HashMap<String, Object> of table fields and values to
     * insert.
     */
    public void insertLocations(HashMap<String, Object> data) {
        TARDISSQLInsertLocations locate = new TARDISSQLInsertLocations(plugin, data);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, locate);
    }
}
