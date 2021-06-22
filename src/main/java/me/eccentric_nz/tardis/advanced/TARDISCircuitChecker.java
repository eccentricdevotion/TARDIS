/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.advanced;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetDiskStorage;
import me.eccentric_nz.tardis.utility.TardisNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * The synchronic feedback circuit, along with the multi-loop stabiliser, was an essential component for a smooth
 * rematerialisation of a TARDIS.
 *
 * @author eccentric_nz
 */
public class TardisCircuitChecker {

    private final TardisPlugin plugin;
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

    public TardisCircuitChecker(TardisPlugin plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        ars = false;
        chameleon = false;
        input = false;
        invisibility = false;
        materialisation = false;
        memory = false;
        randomiser = false;
        scanner = false;
        temporal = false;
        arsUses = 0;
        chameleonUses = 0;
        inputUses = 0;
        invisibilityUses = 0;
        materialisationUses = 0;
        memoryUses = 0;
        randomiserUses = 0;
        scannerUses = 0;
        temporalUses = 0;
    }

    /**
     * Checks the TARDIS's Advanced Console inventory to see which circuits are installed.
     */
    public void getCircuits() { // TODO Make this "get" method actually return something.
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetDiskStorage resultSetDiskStorage = new ResultSetDiskStorage(plugin, where);
        if (resultSetDiskStorage.resultSet()) {
            ItemStack[] itemStacks;
            try {
                itemStacks = TardisInventorySerializer.itemStacksFromString(resultSetDiskStorage.getConsole());
                for (ItemStack itemStack : itemStacks) {
                    if (itemStack != null && itemStack.hasItemMeta()) {
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        assert itemMeta != null;
                        if (itemMeta.hasDisplayName()) {
                            String displayName = itemMeta.getDisplayName();
                            if (displayName.equals("TARDIS ARS Circuit")) {
                                ars = true;
                                arsUses = getUses(itemMeta);
                            }
                            if (displayName.equals("TARDIS Chameleon Circuit")) {
                                chameleon = true;
                                chameleonUses = getUses(itemMeta);
                            }
                            if (displayName.equals("TARDIS Input Circuit")) {
                                input = true;
                                inputUses = getUses(itemMeta);
                            }
                            if (displayName.equals("TARDIS Invisibility Circuit")) {
                                invisibility = true;
                                invisibilityUses = getUses(itemMeta);
                            }
                            if (displayName.equals("TARDIS Materialisation Circuit")) {
                                materialisation = true;
                                materialisationUses = getUses(itemMeta);
                            }
                            if (displayName.equals("TARDIS Memory Circuit")) {
                                memory = true;
                                memoryUses = getUses(itemMeta);
                            }
                            if (displayName.equals("TARDIS Randomiser Circuit")) {
                                randomiser = true;
                                randomiserUses = getUses(itemMeta);
                            }
                            if (displayName.equals("TARDIS Scanner Circuit")) {
                                scanner = true;
                                scannerUses = getUses(itemMeta);
                            }
                            if (displayName.equals("TARDIS Temporal Circuit")) {
                                temporal = true;
                                temporalUses = getUses(itemMeta);
                            }
                        }
                    }
                }
            } catch (IOException ioException) {
                plugin.debug("Could not get console items: " + ioException);
            }
        }
    }

    public boolean hasArs() {
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
     * @param itemMeta the ItemMeta to check
     * @return the number of uses
     */
    private int getUses(ItemMeta itemMeta) {
        int uses = 0;
        if (itemMeta.hasLore()) {
            List<String> lore = itemMeta.getLore();
            assert lore != null;
            String stripped = ChatColor.stripColor(lore.get(1));
            if (!stripped.equals("unlimited")) {
                uses = TardisNumberParsers.parseInt(stripped);
            }
        }
        return uses;
    }
}
