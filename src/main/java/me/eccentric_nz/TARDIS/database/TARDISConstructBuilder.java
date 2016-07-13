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

package me.eccentric_nz.TARDIS.database;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;

/**
 *
 * @author eccentric_nz
 */
public class TARDISConstructBuilder {

    private final TARDIS plugin;
    private final PRESET preset;
    private final int id;
    private final int bid;
    private final int data;

    public TARDISConstructBuilder(TARDIS plugin, PRESET preset, int id, int bid, int data) {
        this.plugin = plugin;
        this.preset = preset;
        this.id = id;
        this.bid = bid;
        this.data = data;
    }

    public void buildAndSave() {
        String jsonBlueID;
        String jsonBlueData;
        String jsonStainID;
        String jsonStainData;
        String jsonGlassID;
        String jsonGlassData;
        // determine stained data
        byte stain = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(bid);
        if (preset.equals(PRESET.NEW)) {
            jsonBlueID = "[[" + bid + "," + bid + "," + bid + ",44],[" + bid + "," + bid + "," + bid + ",44],[" + bid + "," + bid + "," + bid + ",44],[" + bid + "," + bid + "," + bid + ",44],[" + bid + "," + bid + "," + bid + ",44],[" + bid + "," + bid + "," + bid + ",44],[" + bid + "," + bid + "," + bid + ",44],[71,71," + bid + ",44],[0,0,152,124],[0,0,68,0]]";
            jsonBlueData = "[[" + data + "," + data + "," + data + ",0],[" + data + "," + data + "," + data + ",0],[" + data + "," + data + "," + data + ",0],[" + data + "," + data + "," + data + ",0],[" + data + "," + data + "," + data + ",0],[" + data + "," + data + "," + data + ",0],[" + data + "," + data + "," + data + ",0],[0,9," + data + ",0],[0,0," + data + ",0],[0,0,4,0]]";
            jsonStainID = "[[95,95,95,95],[95,95,95,95],[95,95,95,95],[95,95,95,95],[95,95,95,95],[95,95,95,95],[95,95,95,95],[71,71,95,95],[0,0,95,123],[0,0,68,0]]";
            jsonStainData = "[[" + stain + "," + stain + "," + stain + ",8],[" + stain + "," + stain + "," + stain + ",8],[" + stain + "," + stain + "," + stain + ",8],[" + stain + "," + stain + "," + stain + ",8],[" + stain + "," + stain + "," + stain + ",8],[" + stain + "," + stain + "," + stain + ",8],[" + stain + "," + stain + "," + stain + ",8],[0,9," + stain + ",8],[0,0," + stain + ",4],[0,0,4,0]]";
            jsonGlassID = "[[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[71,71,20,20],[0,0,20,123],[0,0,68,0]]";
            jsonGlassData = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,9,0,0],[0,0,0,0],[0,0,4,0]]";
        } else {
            jsonBlueID = "[[" + bid + "," + bid + "," + bid + ",0],[" + bid + "," + bid + "," + bid + ",0],[" + bid + "," + bid + "," + bid + ",0],[" + bid + "," + bid + "," + bid + ",0],[" + bid + "," + bid + "," + bid + ",0],[" + bid + "," + bid + "," + bid + ",0],[" + bid + "," + bid + "," + bid + ",0],[71,71," + bid + ",0],[0,0,152,50],[0,0,68,0]]";
            jsonBlueData = "[[" + data + "," + data + "," + data + ",0],[" + data + "," + data + "," + data + ",0],[" + data + "," + data + "," + data + ",0],[" + data + "," + data + "," + data + ",0],[" + data + "," + data + "," + data + ",0],[" + data + "," + data + "," + data + ",0],[" + data + "," + data + "," + data + ",0],[0,9," + data + ",0],[0,0,0,5],[0,0,4,0]]";
            jsonStainID = "[[95,95,95,0],[95,95,95,0],[95,95,95,0],[95,95,95,0],[95,95,95,0],[95,95,95,0],[95,95,95,0],[71,71,95,0],[0,0,95,0],[0,0,68,0]]";
            jsonStainData = "[[" + stain + "," + stain + "," + stain + ",0],[" + stain + "," + stain + "," + stain + ",0],[" + stain + "," + stain + "," + stain + ",0],[" + stain + "," + stain + "," + stain + ",0],[" + stain + "," + stain + "," + stain + ",0],[" + stain + "," + stain + "," + stain + ",0],[" + stain + "," + stain + "," + stain + ",0],[0,9," + stain + ",0],[0,0," + stain + ",4],[0,0,4,0]]";
            jsonGlassID = "[[20,20,20,0],[20,20,20,0],[20,20,20,0],[20,20,20,0],[20,20,20,0],[20,20,20,0],[20,20,20,0],[71,71,20,0],[0,0,20,0],[0,0,68,0]]";
            jsonGlassData = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,9,0,0],[0,0,0,0],[0,0,4,0]]";
        }
        // save chameleon construct
        HashMap<String, Object> wherec = new HashMap<String, Object>();
        wherec.put("tardis_id", id);
        ResultSetChameleon rsc = new ResultSetChameleon(plugin, wherec);
        QueryFactory qf = new QueryFactory(plugin);
        HashMap<String, Object> set = new HashMap<String, Object>();
        set.put("blueprintID", jsonBlueID);
        set.put("blueprintData", jsonBlueData);
        set.put("stainID", jsonStainID);
        set.put("stainData", jsonStainData);
        set.put("glassID", jsonGlassID);
        set.put("glassData", jsonGlassData);
        if (rsc.resultSet()) {
            // update
            HashMap<String, Object> whereu = new HashMap<String, Object>();
            whereu.put("tardis_id", id);
            qf.doUpdate("chameleon", set, whereu);
        } else {
            // insert
            set.put("tardis_id", id);
            qf.doInsert("chameleon", set);
        }
        // set preset to CONSTRUCT
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        HashMap<String, Object> sett = new HashMap<String, Object>();
        sett.put("chameleon_preset", "CONSTRUCT");
        qf.doUpdate("tardis", sett, where);
    }
}
