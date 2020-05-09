package me.eccentric_nz.TARDIS.update;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetARS;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.HashMap;

public class UpdateARS {

    private final TARDIS plugin;

    public UpdateARS(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(Block block, SCHEMATIC schm, int id, String uuid) {
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
            if (schm.getPermission().equals("coral") || schm.getPermission().equals("deluxe") || schm.getPermission().equals("eleventh") || schm.getPermission().equals("master")) {
                empty[0][4][4] = controlBlock;
                empty[0][4][5] = controlBlock;
                empty[0][5][4] = controlBlock;
                empty[0][5][5] = controlBlock;
                empty[1][4][5] = controlBlock;
                empty[1][5][4] = controlBlock;
                empty[1][5][5] = controlBlock;
            } else if (schm.getPermission().equals("bigger") || schm.getPermission().equals("redstone") || schm.getPermission().equals("twelfth") || schm.getPermission().equals("thirteenth")) {
                empty[1][4][5] = controlBlock;
                empty[1][5][4] = controlBlock;
                empty[1][5][5] = controlBlock;
            }
            empty[1][4][4] = controlBlock;
            JsonArray json = new JsonParser().parse(new Gson().toJson(empty)).getAsJsonArray();
            HashMap<String, Object> seta = new HashMap<>();
            seta.put("tardis_id", id);
            seta.put("uuid", uuid);
            seta.put("json", json.toString());
            plugin.getQueryFactory().doInsert("ars", seta);
        }
        if (Tag.SIGNS.isTagged(block.getType())) {
            // add text to sign
            Sign as = (Sign) block.getState();
            as.setLine(0, "TARDIS");
            as.setLine(1, plugin.getSigns().getStringList("ars").get(0));
            as.setLine(2, plugin.getSigns().getStringList("ars").get(1));
            as.setLine(3, plugin.getSigns().getStringList("ars").get(2));
            as.update();
        }
    }
}
