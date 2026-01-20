package me.eccentric_nz.TARDIS.commands.dev.lists;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.schematic.SchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.block.data.BlockData;

import java.util.HashMap;
import java.util.Map;

public class ConsoleCostLister {

    private final TARDIS plugin;
    private final HashMap<String, HashMap<String, Integer>> consoleBlockCounts = new HashMap<>();

    public ConsoleCostLister(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void actualArtron() {
        for (String fileName : Desktops.getBY_PERMS().keySet()) {
            HashMap<String, Integer> blockTypes = new HashMap<>();
            // get JSON
            JsonObject obj = SchematicGZip.getObject(plugin, "consoles", fileName, false);
            if (obj == null) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "The supplied file [" + fileName + ".tschm] is not a TARDIS JSON schematic!");
            } else {
                // get dimensions
                JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
                int h = dimensions.get("height").getAsInt();
                int w = dimensions.get("width").getAsInt();
                int l = dimensions.get("length").getAsInt();
                // get input array
                JsonArray arr = obj.get("input").getAsJsonArray();
                // loop like crazy
                for (int level = 0; level < h; level++) {
                    JsonArray floor = arr.get(level).getAsJsonArray();
                    for (int row = 0; row < w; row++) {
                        JsonArray r = floor.get(row).getAsJsonArray();
                        for (int col = 0; col < l; col++) {
                            JsonObject c = r.get(col).getAsJsonObject();
                            String bid = getMaterialAsString(c.get("data").getAsString());
                            if (plugin.getBuildKeeper().getIgnoreBlocks().contains(bid)) {
                                continue;
                            }
                            if (blockTypes.containsKey(bid)) {
                                Integer count = blockTypes.get(bid) + 1;
                                blockTypes.put(bid, count);
                            } else {
                                blockTypes.put(bid, 1);
                            }
                        }
                    }
                }
            }
            consoleBlockCounts.put(fileName, blockTypes);
        }
        for (Map.Entry<String, HashMap<String, Integer>> c : consoleBlockCounts.entrySet()) {
            int cost = 0;
            for (Map.Entry<String, Integer> entry : c.getValue().entrySet()) {
                if (plugin.getCondensables().containsKey(entry.getKey())) {
                    int value = entry.getValue() * plugin.getCondensables().get(entry.getKey());
                    cost += value;
                } else {
                    plugin.debug("Not in condensables: " + entry.getKey());
                }
            }
            int artron_cost = Math.round(cost / 2.0F);
            float tmp_cost = artron_cost / 10.0f;
            int config_cost = (((int) tmp_cost) / 25) * 25;
            int current_cost = plugin.getArtronConfig().getInt("upgrades." + c.getKey());
            plugin.debug("| " + TARDISStringUtils.capitalise(c.getKey()) + " | " + artron_cost + " | " + config_cost + " |" + current_cost + " |");
        }
    }

    private String getMaterialAsString(String data) {
        String bid = "STONE";
        try {
            BlockData block = plugin.getServer().createBlockData(data);
            bid = block.getMaterial().toString();
        } catch (IllegalArgumentException ignored) {
        }
        return bid;
    }
}
