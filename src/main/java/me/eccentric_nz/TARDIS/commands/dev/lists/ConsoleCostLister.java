package me.eccentric_nz.TARDIS.commands.dev.lists;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.schematic.SchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        Pair<String, Integer> small = new Pair<>("small", 0);
        Pair<String, Integer> medium = new Pair<>("medium", 0);
        Pair<String, Integer> tall = new Pair<>("tall", 0);
        Pair<String, Integer> wide = new Pair<>("wide", 0);
        Pair<String, Integer> massive = new Pair<>("massive", 0);
        List<CostData> calculated = new ArrayList<>();
        for (Map.Entry<String, HashMap<String, Integer>> c : consoleBlockCounts.entrySet()) {
            int cost = 0;
            for (Map.Entry<String, Integer> entry : c.getValue().entrySet()) {
                String bid = entry.getKey();
                if (plugin.getBuildKeeper().getBlockConversion().containsKey(bid)) {
                    bid = plugin.getBuildKeeper().getBlockConversion().get(bid);
                }
                if (plugin.getCondensables().containsKey(bid)) {
                    int value = entry.getValue() * plugin.getCondensables().get(bid);
                    cost += value;
                } else {
                    plugin.debug("Not in condensables: " + entry.getKey());
                }
            }
            int artron_cost = Math.round(cost / 2.0F);
            float tmp_cost = artron_cost / 10.0f;
            int config_cost = (((int) tmp_cost) / 25) * 25;
            int current_cost = plugin.getArtronConfig().getInt("upgrades." + c.getKey());
            ConsoleSize size = Desktops.getBY_PERMS().get(c.getKey()).getConsoleSize();
            switch (size) {
                case SMALL -> {
                    if (artron_cost > small.getSecond()) {
                        small = new Pair<>(c.getKey(), artron_cost);
                    }
                }
                case MEDIUM -> {
                    if (artron_cost > medium.getSecond()) {
                        medium = new Pair<>(c.getKey(), artron_cost);
                    }
                }
                case TALL -> {
                    if (artron_cost > tall.getSecond()) {
                        tall = new Pair<>(c.getKey(), artron_cost);
                    }
                }
                case WIDE -> {
                    if (artron_cost > wide.getSecond()) {
                        wide = new Pair<>(c.getKey(), artron_cost);
                    }
                }
                default -> {
                    if (artron_cost > massive.getSecond()) {
                        massive = new Pair<>(c.getKey(), artron_cost);
                    }
                }
            }
            calculated.add(new CostData(TARDISStringUtils.capitalise(c.getKey()), artron_cost, config_cost, current_cost, size));
        }
        plugin.debug("| Desktop | Condensed Artron | Current cost | Updated cost | Air's formula | Size |");
        plugin.debug("| ------- | ---------------- | ------------ | ------------ | ------------- | ---- |");
        for (CostData data : calculated) {
            // airomis formula
            int formula_cost = switch (data.size()) {
                case SMALL -> (int) (((float) data.artron() / (float) small.getSecond()) * 5000);
                case MEDIUM -> (int) (((float) data.artron() / (float) medium.getSecond()) * 10000);
                case TALL -> (int) (((float) data.artron() / (float) tall.getSecond()) * 15000);
                case WIDE -> (int) (((float) data.artron() / (float) wide.getSecond()) * 20000);
                default -> (int) (((float) data.artron() / (float) massive.getSecond()) * 25000);
            };
            plugin.debug("| " + data.name() + " | " + data.artron() + " | " + data.current() + " | " + data.updated() + " | " + formula_cost + " | " + data.size() + " |");
        }
        plugin.debug("Most expensive:");
        plugin.debug("SMALL " + small.getFirst() + ", " + small.getSecond());
        plugin.debug("MEDIUM " + medium.getFirst() + ", " + medium.getSecond());
        plugin.debug("TALL " + tall.getFirst() + ", " + tall.getSecond());
        plugin.debug("WIDE " + wide.getFirst() + ", " + wide.getSecond());
        plugin.debug("MASSIVE " + massive.getFirst() + ", " + massive.getSecond());
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
