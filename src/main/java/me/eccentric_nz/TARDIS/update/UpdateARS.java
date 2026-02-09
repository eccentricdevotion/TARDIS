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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.update;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetARS;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import net.kyori.adventure.text.Component;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;

import java.util.HashMap;

public class UpdateARS {

    private final TARDIS plugin;

    UpdateARS(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(Block block, Schematic schm, int id, String uuid) {
        // insert control
        plugin.getQueryFactory().insertControl(id, 10, block.getLocation().toString(), 0);
        // check if they already have an ARS record (they may have used `/tardis arsremove`)
        HashMap<String, Object> wherer = new HashMap<>();
        wherer.put("tardis_id", id);
        ResultSetARS rsa = new ResultSetARS(plugin, wherer);
        if (!rsa.resultSet()) {
            // create default json
            String[][][] empty = new String[3][9][9];
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 9; x++) {
                    for (int z = 0; z < 9; z++) {
                        empty[y][x][z] = "STONE";
                    }
                }
            }
            String controlBlock = schm.getSeedMaterial().toString();
            if (schm.getConsoleSize() == ConsoleSize.MASSIVE) {
                // the 8 slots on the same level &
                empty[1][4][5] = controlBlock;
                empty[1][4][6] = controlBlock;
                empty[1][5][4] = controlBlock;
                empty[1][5][5] = controlBlock;
                empty[1][5][6] = controlBlock;
                empty[1][6][4] = controlBlock;
                empty[1][6][5] = controlBlock;
                empty[1][6][6] = controlBlock;
                // the 9 slots on the level above
                empty[2][4][4] = controlBlock;
                empty[2][4][5] = controlBlock;
                empty[2][4][6] = controlBlock;
                empty[2][5][4] = controlBlock;
                empty[2][5][5] = controlBlock;
                empty[2][5][6] = controlBlock;
                empty[2][6][4] = controlBlock;
                empty[2][6][5] = controlBlock;
                empty[2][6][6] = controlBlock;
            } else if (schm.getConsoleSize() == ConsoleSize.WIDE) {
                // the 8 slots on the same level
                empty[1][4][5] = controlBlock;
                empty[1][4][6] = controlBlock;
                empty[1][5][4] = controlBlock;
                empty[1][5][5] = controlBlock;
                empty[1][5][6] = controlBlock;
                empty[1][6][4] = controlBlock;
                empty[1][6][5] = controlBlock;
                empty[1][6][6] = controlBlock;
            } else if (schm.getConsoleSize() == ConsoleSize.TALL) {
                empty[0][4][4] = controlBlock;
                empty[0][4][5] = controlBlock;
                empty[0][5][4] = controlBlock;
                empty[0][5][5] = controlBlock;
                empty[1][4][5] = controlBlock;
                empty[1][5][4] = controlBlock;
                empty[1][5][5] = controlBlock;
            } else if (schm.getConsoleSize() == ConsoleSize.MEDIUM) {
                empty[1][4][5] = controlBlock;
                empty[1][5][4] = controlBlock;
                empty[1][5][5] = controlBlock;
            }
            empty[1][4][4] = controlBlock;
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            JsonArray json = JsonParser.parseString(gson.toJson(empty)).getAsJsonArray();
            HashMap<String, Object> seta = new HashMap<>();
            seta.put("tardis_id", id);
            seta.put("uuid", uuid);
            seta.put("json", json.toString());
            plugin.getQueryFactory().doInsert("ars", seta);
        }
        if (Tag.SIGNS.isTagged(block.getType())) {
            // add text to sign
            Sign as = (Sign) block.getState();
            SignSide front = as.getSide(Side.FRONT);
            front.line(0, Component.text("TARDIS"));
            front.line(1, Component.text(plugin.getSigns().getStringList("ars").getFirst()));
            front.line(2, Component.text(plugin.getSigns().getStringList("ars").get(1)));
            front.line(3, Component.text(plugin.getSigns().getStringList("ars").get(2)));
            as.setWaxed(true);
            as.update();
        }
    }
}
