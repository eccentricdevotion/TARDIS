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
package me.eccentric_nz.TARDIS.advanced;

import java.io.IOException;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetDiskStorage;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The synchronic feedback circuit, along with the multi-loop stabiliser, was an
 * essential component for a smooth rematerialisation of a TARDIS.
 *
 * @author eccentric_nz
 */
public class TARDISCircuitChecker {

    private final TARDIS plugin;
    private final int id;
    private boolean ars;
    private boolean chameleon;
    private boolean input;
    private boolean materialisation;
    private boolean memory;
    private boolean scanner;
    private boolean temporal;

    public TARDISCircuitChecker(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        this.ars = false;
        this.chameleon = false;
        this.input = false;
        this.materialisation = false;
        this.memory = false;
        this.scanner = false;
        this.temporal = false;
    }

    /**
     * Checks the TARDIS's Advanced Console inventory to see which circuits are
     * installed.
     */
    public void getCircuits() {
        plugin.debug("Checking circuits for TARDIS ID: " + id);
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        plugin.debug("Querying database - storage table...");
        ResultSetDiskStorage rs = new ResultSetDiskStorage(plugin, where);
        if (rs.resultSet()) {
            plugin.debug("Record found for TARDIS ID: " + id);
            ItemStack[] items;
            try {
                plugin.debug("Deserializing inventory data to item stacks...");
                items = TARDISSerializeInventory.itemStacksFromString(rs.getConsole());
                plugin.debug("Checking item stacks...");
                for (ItemStack is : items) {
                    if (is != null && is.hasItemMeta()) {
                        ItemMeta im = is.getItemMeta();
                        if (im.hasDisplayName()) {
                            String dn = im.getDisplayName();
                            plugin.debug("Item has display name: " + dn);
                            if (dn.equals("TARDIS ARS Circuit")) {
                                plugin.debug("Found: ARS Circuit");
                                this.ars = true;
                            }
                            if (dn.equals("TARDIS Chameleon Circuit")) {
                                plugin.debug("Found: Chameleon Circuit");
                                this.chameleon = true;
                            }
                            if (dn.equals("TARDIS Input Circuit")) {
                                plugin.debug("Found: Input Circuit");
                                this.input = true;
                            }
                            if (dn.equals("TARDIS Materialisation Circuit")) {
                                plugin.debug("Found: Materialisation Circuit");
                                this.materialisation = true;
                            }
                            if (dn.equals("TARDIS Memory Circuit")) {
                                plugin.debug("Found: Memory Circuit");
                                this.memory = true;
                            }
                            if (dn.equals("TARDIS Scanner Circuit")) {
                                plugin.debug("Found: Scanner Circuit");
                                this.scanner = true;
                            }
                            if (dn.equals("TARDIS Temporal Circuit")) {
                                plugin.debug("Found: Temporal Circuit");
                                this.temporal = true;
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                plugin.debug("Could not get console items: " + ex);
            }
        }
    }

    public boolean hasARS() {
        return ars;
    }

    public boolean hasChameleon() {
        return chameleon;
    }

    public boolean hasInput() {
        return input;
    }

    public boolean hasMaterialisation() {
        return materialisation;
    }

    public boolean hasMemory() {
        return memory;
    }

    public boolean hasScanner() {
        return scanner;
    }

    public boolean hasTemporal() {
        return temporal;
    }
}
