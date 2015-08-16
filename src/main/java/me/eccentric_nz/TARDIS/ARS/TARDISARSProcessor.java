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
package me.eccentric_nz.TARDIS.ARS;

import java.util.HashMap;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.Chunk;

/**
 * Preprocessor for checking changes in the Architectural Reconfiguration
 * System.
 *
 * @author eccentric_nz
 */
public class TARDISARSProcessor {

    private final TARDIS plugin;
    private final int id;
    private final int limit;
    private String error = "ENERGY_NOT_ENOUGH";
    private HashMap<TARDISARSSlot, ARS> changed;
    private HashMap<TARDISARSJettison, ARS> jettison;

    public TARDISARSProcessor(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        this.limit = this.plugin.getConfig().getInt("growth.ars_limit");
    }

    public boolean compare3DArray(int[][][] start, int[][][] end) {
        changed = new HashMap<TARDISARSSlot, ARS>();
        jettison = new HashMap<TARDISARSJettison, ARS>();
        Chunk c = plugin.getLocationUtils().getTARDISChunk(id);
        for (int l = 0; l < 3; l++) {
            for (int x = 0; x < 9; x++) {
                for (int z = 0; z < 9; z++) {
                    if (start[l][x][z] != end[l][x][z]) {
                        if (end[l][x][z] == 46) {
                            // found TNT in this slot
                            TARDISARSJettison slot = new TARDISARSJettison();
                            slot.setChunk(c);
                            slot.setY(l);
                            slot.setX(x);
                            slot.setZ(z);
                            jettison.put(slot, TARDISARS.ARSFor(start[l][x][z]));
                        } else {
                            if (end[l][x][z] == 24) {
                                if (l == 2 || ((l + 1) < 3 && end[l + 1][x][z] == 24)) {
                                    // only remember the bottom slot of an anti-gravity well
                                    TARDISARSSlot slot = new TARDISARSSlot();
                                    slot.setChunk(c);
                                    slot.setY(l);
                                    slot.setX(x);
                                    slot.setZ(z);
                                    changed.put(slot, TARDISARS.ARSFor(end[l][x][z]));
                                }
                            } else if (end[l][x][z] == 48) {
                                if (l == 0 || ((l - 1) > 0 && end[l - 1][x][z] == 48)) {
                                    // only remember the top slot of a gravity well
                                    TARDISARSSlot slot = new TARDISARSSlot();
                                    slot.setChunk(c);
                                    slot.setY(l);
                                    slot.setX(x);
                                    slot.setZ(z);
                                    changed.put(slot, TARDISARS.ARSFor(end[l][x][z]));
                                }
                            } else {
                                TARDISARSSlot slot = new TARDISARSSlot();
                                slot.setChunk(c);
                                slot.setY(l);
                                slot.setX(x);
                                slot.setZ(z);
                                changed.put(slot, TARDISARS.ARSFor(end[l][x][z]));
                            }
                        }
                    }
                }
            }
        }
        boolean overlimit = (limit > 0 && changed.size() > limit);
        if (overlimit) {
            error = "ARS_LIMIT";
        }
        return jettison.size() > 0 || (changed.size() > 0 && !overlimit);
    }

    public boolean checkCosts(HashMap<TARDISARSSlot, ARS> changed, HashMap<TARDISARSJettison, ARS> jettison) {
        if (changed.size() > 0) {
            int totalcost = 0;
            int recoveredcost = 0;
            // calculate energy gained by jettisons
            for (Map.Entry<TARDISARSJettison, ARS> c : jettison.entrySet()) {
                if (c.getValue() != null) {
                    recoveredcost += Math.round((plugin.getArtronConfig().getInt("jettison") / 100F) * plugin.getRoomsConfig().getInt("rooms." + c.getValue().getActualName() + ".cost"));
                }
            }
            for (Map.Entry<TARDISARSSlot, ARS> c : changed.entrySet()) {
                totalcost += plugin.getRoomsConfig().getInt("rooms." + c.getValue().getActualName() + ".cost");
            }
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (rs.resultSet()) {
                int energy = rs.getArtron_level();
                // check available energy vs cost
                if (totalcost - recoveredcost > energy) {
                    this.error = "ENERGY_NOT_ENOUGH";
                    return false;
                }
            }
        }
        return true;
    }

    public HashMap<TARDISARSSlot, ARS> getChanged() {
        return changed;
    }

    public HashMap<TARDISARSJettison, ARS> getJettison() {
        return jettison;
    }

    public String getError() {
        return error;
    }
}
