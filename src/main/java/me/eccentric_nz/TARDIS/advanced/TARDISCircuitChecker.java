/*
 * Copyright (C) 2016 eccentric_nz
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
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
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
    private boolean invisibility;
    private boolean materialisation;
    private boolean memory;
    private boolean randomiser;
    private boolean scanner;
    private boolean temporal;
    private int arsUses;
    private int chameleonUses;
    private int inputUses;
    private int invisibilityUses;
    private int materialisationUses;
    private int memoryUses;
    private int randomiserUses;
    private int scannerUses;
    private int temporalUses;

    public TARDISCircuitChecker(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        this.ars = false;
        this.chameleon = false;
        this.input = false;
        this.invisibility = false;
        this.materialisation = false;
        this.memory = false;
        this.randomiser = false;
        this.scanner = false;
        this.temporal = false;
        this.arsUses = 0;
        this.chameleonUses = 0;
        this.inputUses = 0;
        this.invisibilityUses = 0;
        this.materialisationUses = 0;
        this.memoryUses = 0;
        this.randomiserUses = 0;
        this.scannerUses = 0;
        this.temporalUses = 0;
    }

    /**
     * Checks the TARDIS's Advanced Console inventory to see which circuits are
     * installed.
     */
    public void getCircuits() {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetDiskStorage rs = new ResultSetDiskStorage(plugin, where);
        if (rs.resultSet()) {
            ItemStack[] items;
            try {
                items = TARDISSerializeInventory.itemStacksFromString(rs.getConsole());
                for (ItemStack is : items) {
                    if (is != null && is.hasItemMeta()) {
                        ItemMeta im = is.getItemMeta();
                        if (im.hasDisplayName()) {
                            String dn = im.getDisplayName();
                            if (dn.equals("TARDIS ARS Circuit")) {
                                this.ars = true;
                                this.arsUses = getUses(im);
                            }
                            if (dn.equals("TARDIS Chameleon Circuit")) {
                                this.chameleon = true;
                                this.chameleonUses = getUses(im);
                            }
                            if (dn.equals("TARDIS Input Circuit")) {
                                this.input = true;
                                this.inputUses = getUses(im);
                            }
                            if (dn.equals("TARDIS Invisibility Circuit")) {
                                this.invisibility = true;
                                this.invisibilityUses = getUses(im);
                            }
                            if (dn.equals("TARDIS Materialisation Circuit")) {
                                this.materialisation = true;
                                this.materialisationUses = getUses(im);
                            }
                            if (dn.equals("TARDIS Memory Circuit")) {
                                this.memory = true;
                                this.memoryUses = getUses(im);
                            }
                            if (dn.equals("TARDIS Randomiser Circuit")) {
                                this.randomiser = true;
                                this.randomiserUses = getUses(im);
                            }
                            if (dn.equals("TARDIS Scanner Circuit")) {
                                this.scanner = true;
                                this.scannerUses = getUses(im);
                            }
                            if (dn.equals("TARDIS Temporal Circuit")) {
                                this.temporal = true;
                                this.temporalUses = getUses(im);
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

    public boolean hasInvisibility() {
        return invisibility;
    }

    public boolean hasMaterialisation() {
        return materialisation;
    }

    public boolean hasMemory() {
        return memory;
    }

    public boolean hasRandomiser() {
        return randomiser;
    }

    public boolean hasScanner() {
        return scanner;
    }

    public boolean hasTemporal() {
        return temporal;
    }

    public int getArsUses() {
        return arsUses;
    }

    public int getChameleonUses() {
        return chameleonUses;
    }

    public int getInputUses() {
        return inputUses;
    }

    public int getInvisibilityUses() {
        return invisibilityUses;
    }

    public int getMaterialisationUses() {
        return materialisationUses;
    }

    public int getMemoryUses() {
        return memoryUses;
    }

    public int getRandomiserUses() {
        return randomiserUses;
    }

    public int getScannerUses() {
        return scannerUses;
    }

    public int getTemporalUses() {
        return temporalUses;
    }

    /**
     * Get the number of uses this circuit has left.
     *
     * @param im the ItemMeta to check
     * @return the number of uses
     */
    private int getUses(ItemMeta im) {
        int uses = 0;
        if (im.hasLore()) {
            List<String> lore = im.getLore();
            String stripped = ChatColor.stripColor(lore.get(1));
            if (!stripped.equals("unlimited")) {
                uses = TARDISNumberParsers.parseInt(stripped);
            }
        }
        return uses;
    }
}
