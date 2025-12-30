/*
 * Copyright (C) 2026 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
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
    private boolean telepathic;
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
    private int telepathicUses;

    public TARDISCircuitChecker(TARDIS plugin, int id) {
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
        telepathic = false;
        temporal = false;
        arsUses = 0;
        chameleonUses = 0;
        inputUses = 0;
        invisibilityUses = 0;
        materialisationUses = 0;
        memoryUses = 0;
        randomiserUses = 0;
        scannerUses = 0;
        telepathicUses = 0;
        temporalUses = 0;
    }

    /**
     * Checks the TARDIS's Advanced Console inventory to see which circuits are installed.
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
                            String dn = ComponentUtils.stripColour(im.displayName());
                            if (dn.endsWith("TARDIS ARS Circuit")) {
                                ars = true;
                                arsUses = getUses(im);
                            }
                            if (dn.endsWith("TARDIS Chameleon Circuit")) {
                                chameleon = true;
                                chameleonUses = getUses(im);
                            }
                            if (dn.endsWith("TARDIS Input Circuit")) {
                                input = true;
                                inputUses = getUses(im);
                            }
                            if (dn.endsWith("TARDIS Invisibility Circuit")) {
                                invisibility = true;
                                invisibilityUses = getUses(im);
                            }
                            if (dn.endsWith("TARDIS Materialisation Circuit")) {
                                materialisation = true;
                                materialisationUses = getUses(im);
                            }
                            if (dn.endsWith("TARDIS Memory Circuit")) {
                                memory = true;
                                memoryUses = getUses(im);
                            }
                            if (dn.endsWith("TARDIS Randomiser Circuit")) {
                                randomiser = true;
                                randomiserUses = getUses(im);
                            }
                            if (dn.endsWith("TARDIS Scanner Circuit")) {
                                scanner = true;
                                scannerUses = getUses(im);
                            }
                            if (dn.endsWith("TARDIS Telepathic Circuit")) {
                                telepathic = true;
                                telepathicUses = getUses(im);
                            }
                            if (dn.endsWith("TARDIS Temporal Circuit")) {
                                temporal = true;
                                temporalUses = getUses(im);
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

    public boolean hasTelepathic() {
        return telepathic;
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

    public int getTelepathicUses() {
        return telepathicUses;
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
            List<Component> lore = im.lore();
            String stripped = ComponentUtils.stripColour(lore.get(1));
            if (!stripped.equals("unlimited")) {
                uses = TARDISNumberParsers.parseInt(stripped);
            }
        }
        return uses;
    }

    public int getUses(DiskCircuit circuit) {
        switch (circuit) {
            case ARS -> {
                return arsUses;
            }
            case CHAMELEON -> {
                return chameleonUses;
            }
            case INPUT -> {
                return inputUses;
            }
            case INVISIBILITY -> {
                return invisibilityUses;
            }
            case MATERIALISATION -> {
                return materialisationUses;
            }
            case MEMORY -> {
                return memoryUses;
            }
            case RANDOMISER -> {
                return randomiserUses;
            }
            case SCANNER -> {
                return scannerUses;
            }
            case TELEPATHIC -> {
                return telepathicUses;
            }
            case TEMPORAL -> {
                return temporalUses;
            }
            default -> {
                return 1;
            }
        }
    }
}
