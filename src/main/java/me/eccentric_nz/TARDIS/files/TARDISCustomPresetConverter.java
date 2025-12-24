package me.eccentric_nz.TARDIS.files;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;

import java.io.*;

public class TARDISCustomPresetConverter {

    private final TARDIS plugin;

    public TARDISCustomPresetConverter(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void toJson() {
        File source = new File(plugin.getDataFolder() + File.separator + "custom_preset.txt");
        File destination = new File(plugin.getDataFolder() + File.separator + "custom_presets.json");
        // get the custom preset file and read the contents
        // ignore lines that start with a #
        JsonObject root = new JsonObject();
        JsonObject custom = new JsonObject();
        JsonArray blueprint = null;
        JsonArray stained = null;
        JsonArray glass = null;
        String[] lines = new String[2];
        int i = 0;
        int l = 0;
        try (BufferedReader bufRdr = new BufferedReader(new FileReader(source))) {
            String line;
            // read each line of text file
            while ((line = bufRdr.readLine()) != null) {
                if (!line.startsWith("#")) {
                    switch (i) {
                        case 0 -> blueprint = JsonParser.parseString(line).getAsJsonArray();
                        case 1 -> stained = JsonParser.parseString(line).getAsJsonArray();
                        case 2 -> glass = JsonParser.parseString(line).getAsJsonArray();
                        default -> {
                            if (l < 2) {
                                lines[l] = line;
                                l++;
                            }
                        }
                    }
                    i++;
                }
            }
            custom.add("blueprint", blueprint);
            custom.add("stained", stained);
            custom.add("glass", glass);
            JsonArray sign = new JsonArray();
            sign.add(lines[0]);
            sign.add(lines[1]);
            custom.add("sign", sign);
            custom.addProperty("icon", "BLAST_FURNACE");
            root.add("custom", custom);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(destination), 16 * 1024)) {
                bw.write(root.toString());
                plugin.getConfig().set("conversions.custom_presets", true);
                plugin.saveConfig();
            }
        } catch (IOException io) {
            TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, "Could not read custom preset file! " + io.getMessage());
        }
    }
}