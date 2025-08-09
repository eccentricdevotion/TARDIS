package me.eccentric_nz.TARDIS.files;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TARDISGeneratorUpdater {

    private final TARDIS plugin;
    private final FileConfiguration generatorConfig;

    public TARDISGeneratorUpdater(TARDIS plugin, FileConfiguration generatorConfig) {
        this.plugin = plugin;
        this.generatorConfig = generatorConfig;
    }

    public void checkTrees() {
        if (!generatorConfig.contains("gallifrey.terracotta.wood")) {
            generatorConfig.set("gallifrey.red_sand.wood", "STRIPPED_BIRCH_LOG");
            generatorConfig.set("gallifrey.red_sand.leaves", "COBWEB");
            generatorConfig.set("gallifrey.red_sand.decoration", "RED_WOOL");
            generatorConfig.setComments("gallifrey", List.of("materials to use for gallifrey trees"));
            generatorConfig.setComments("gallifrey.red_sand", List.of("trees that grow on red sand"));
            generatorConfig.set("gallifrey.terracotta.wood", "STRIPPED_BIRCH_LOG");
            generatorConfig.set("gallifrey.terracotta.leaves", "COBWEB");
            generatorConfig.set("gallifrey.terracotta.decoration", "RED_WOOL");
            generatorConfig.setComments("gallifrey.terracotta", List.of("trees that grow on terracotta"));
            generatorConfig.set("skaro.wood", "ACACIA_LOG");
            generatorConfig.set("skaro.leaves", "SLIME_BLOCK");
            generatorConfig.set("skaro.decoration", "HONEY_BLOCK");
            generatorConfig.setComments("skaro", List.of("materials to use for skaro trees"));
            generatorConfig.setComments("skaro.wood", List.of("trees that grow on sand"));
            try {
                String generatorPath = plugin.getDataFolder() + File.separator + "generator.yml";
                generatorConfig.save(new File(generatorPath));
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Updated generator.yml");
            } catch (IOException io) {
                plugin.debug("Could not save generator.yml, " + io.getMessage());
            }
        }
    }
}
