package me.eccentric_nz.TARDIS.commands.dev;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.schematic.SchematicGZip;

public class ChainCommand {

    private final TARDIS plugin;

    public ChainCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean checkSchematics() {
        for (String fileName : Desktops.getBY_PERMS().keySet()) {
            // get JSON
            JsonObject obj = SchematicGZip.getObject(plugin, "consoles", fileName, false);
            if (obj == null) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "The supplied file [" + fileName + ".tschm] is not a TARDIS JSON schematic!");
                return false;
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
                            validateBlockData(c.get("data").getAsString(), fileName);
                        }
                    }
                }
            }
        }
        return true;
    }

    private void validateBlockData(String data, String fileName) {
        try {
            plugin.getServer().createBlockData(data);
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.SEVERE, "The file [" + fileName + ".tschm] contains invalid block data!");
            plugin.getMessenger().sendWithColours(plugin.getConsole(), TardisModule.SEVERE, "The invalid data was: ", "#FFFFFF", data, "#00AAAA");
        }
    }
}
