package me.eccentric_nz.TARDIS.commands.sudo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetARS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisConsole;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.Schematic;

import java.util.HashMap;

public class ARSUtility {
    
    public static void checkARS(TARDIS plugin, int id, String uuid) {
        // does the player have an ARS record yet?
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
            // get TARDIS console size
            ResultSetTardisConsole rstc = new ResultSetTardisConsole(plugin);
            if (rstc.fromUUID(uuid)) {
                Schematic schm = rstc.getSchematic();
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
        }
    }
}
