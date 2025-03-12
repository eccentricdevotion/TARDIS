package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.ARS.TARDISARS;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class ComponentCommand {

    private final TARDIS plugin;
    private String template = """
            {
              "type": "minecraft:select",
              "property": "component",
              "component": "minecraft:custom_name",
              "fallback": {
                "type": "minecraft:model",
                "model": "minecraft:block/%s"
              },
              "cases": [
                {
                  "when": {
                    "text": "",
                    "extra": [
                        "%s"
                    ]
                  },
                  "model": {
                    "type": "minecraft:model",
                    "model": "tardis:item/gui/room/%s"
                  }
                }
              ]
            }
            """;

    public ComponentCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void write() {
        try {
            File dir = new File(plugin.getDataFolder() + File.separator + "component_models");
            if (!dir.exists()) {
                if (dir.mkdir()) {
                    dir.setWritable(true);
                    dir.setExecutable(true);
                }
            }
            for (TARDISARS ars : TARDISARS.values()) {
                if (!ars.getMaterial().isEmpty()) {
                    String material = ars.getMaterial().toLowerCase(Locale.ROOT);
                    String json = String.format(template, material, ars.getDescriptiveName(), ars.toString().toLowerCase(Locale.ROOT));
                    File file = new File(plugin.getDataFolder() + File.separator + "component_models" + File.separator + material + ".json");
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
                    bw.write(json);
                    bw.close();
                }
            }
            plugin.debug("Component model writing complete.");
        } catch (IOException e) {
            plugin.debug(e.getMessage());
        }
    }
}
