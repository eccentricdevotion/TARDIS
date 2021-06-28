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
package me.eccentric_nz.tardis.ars;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisArtron;
import org.bukkit.Chunk;

import java.util.HashMap;
import java.util.Map;

/**
 * Preprocessor for checking changes in the Architectural Reconfiguration System.
 *
 * @author eccentric_nz
 */
class TardisArsProcessor {

    private final TardisPlugin plugin;
    private final int id;
    private final int limit;
    private String error = "ENERGY_NOT_ENOUGH";
    private HashMap<TardisArsSlot, Ars> changed;
    private HashMap<TardisArsJettison, Ars> jettison;

    public TardisArsProcessor(TardisPlugin plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        limit = this.plugin.getConfig().getInt("growth.ars_limit");
    }

    public boolean compare3DArray(String[][][] start, String[][][] end) {
        changed = new HashMap<>();
        jettison = new HashMap<>();
        Chunk c = plugin.getLocationUtils().getTardisChunk(id);
        for (int l = 0; l < 3; l++) {
            for (int x = 0; x < 9; x++) {
                for (int z = 0; z < 9; z++) {
                    if (!start[l][x][z].equals(end[l][x][z])) {
                        if (end[l][x][z].equals("TNT")) {
                            // found TNT in this slot
                            TardisArsJettison slot = new TardisArsJettison();
                            slot.setChunk(c);
                            slot.setY(l);
                            slot.setX(x);
                            slot.setZ(z);
                            jettison.put(slot, TardisArs.arsFor(start[l][x][z]));
                            // if it is a gravity well on the top or bottom levels jettison the other half too
                            if (start[l][x][z].equals("SANDSTONE") && l == 2) {
                                TardisArsJettison uslot = new TardisArsJettison();
                                uslot.setChunk(c);
                                uslot.setY(3);
                                uslot.setX(x);
                                uslot.setZ(z);
                                jettison.put(uslot, TardisArs.ANTIGRAVITY);
                            }
                            if (start[l][x][z].equals("MOSSY_COBBLESTONE") && l == 0) {
                                TardisArsJettison lslot = new TardisArsJettison();
                                lslot.setChunk(c);
                                lslot.setY(-1);
                                lslot.setX(x);
                                lslot.setZ(z);
                                jettison.put(lslot, TardisArs.GRAVITY);
                            }
                        } else {
                            switch (end[l][x][z]) {
                                case "SANDSTONE":
                                    if (l == 0 || (l == 1 && !end[l - 1][x][z].equals("SANDSTONE")) || (l == 2 && !end[l - 1][x][z].equals("SANDSTONE")) || (l == 2 && end[l - 1][x][z].equals("SANDSTONE") && end[l - 2][x][z].equals("SANDSTONE"))) {
                                        // only remember the bottom slot of an anti-gravity well
                                        TardisArsSlot slot = new TardisArsSlot();
                                        slot.setChunk(c);
                                        slot.setY(l);
                                        slot.setX(x);
                                        slot.setZ(z);
                                        changed.put(slot, TardisArs.arsFor(end[l][x][z]));
                                    }
                                    break;
                                case "MOSSY_COBBLESTONE":
                                    if (l == 2 || (l == 1 && !end[l + 1][x][z].equals("MOSSY_COBBLESTONE")) || (l == 0 && !end[l + 1][x][z].equals("MOSSY_COBBLESTONE")) || (l == 0 && end[l + 1][x][z].equals("MOSSY_COBBLESTONE") && end[l + 2][x][z].equals("MOSSY_COBBLESTONE"))) {
                                        // only remember the top slot of a gravity well
                                        TardisArsSlot slot = new TardisArsSlot();
                                        slot.setChunk(c);
                                        slot.setY(l - 1);
                                        slot.setX(x);
                                        slot.setZ(z);
                                        changed.put(slot, TardisArs.arsFor(end[l][x][z]));
                                    }
                                    break;
                                default:
                                    TardisArsSlot slot = new TardisArsSlot();
                                    slot.setChunk(c);
                                    slot.setY(l);
                                    slot.setX(x);
                                    slot.setZ(z);
                                    changed.put(slot, TardisArs.arsFor(end[l][x][z]));
                                    break;
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

    boolean checkCosts(HashMap<TardisArsSlot, Ars> changed, HashMap<TardisArsJettison, Ars> jettison) {
        if (changed.size() > 0) {
            int totalcost = 0;
            int recoveredcost = 0;
            // calculate energy gained by jettisons
            for (Map.Entry<TardisArsJettison, Ars> c : jettison.entrySet()) {
                if (c.getValue() != null) {
                    recoveredcost += Math.round((plugin.getArtronConfig().getInt("jettison") / 100F) * plugin.getRoomsConfig().getInt("rooms." + c.getValue().toString() + ".cost"));
                }
            }
            for (Map.Entry<TardisArsSlot, Ars> c : changed.entrySet()) {
                int cost = plugin.getRoomsConfig().getInt("rooms." + c.getValue().toString() + ".cost");
                totalcost += cost;
            }
            ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
            if (rs.fromID(id)) {
                int energy = rs.getArtronLevel();
                // check available energy vs cost
                if (totalcost - recoveredcost > energy) {
                    error = "ENERGY_NOT_ENOUGH";
                    return false;
                }
            }
        }
        return true;
    }

    public HashMap<TardisArsSlot, Ars> getChanged() {
        return changed;
    }

    public HashMap<TardisArsJettison, Ars> getJettison() {
        return jettison;
    }

    public String getError() {
        return error;
    }
}
