package me.eccentric_nz.TARDIS.artron.costs;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;

import java.io.File;
import java.io.IOException;

public class UpgradeCostSetter {

    private final TARDIS plugin;

    public UpgradeCostSetter(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void calculate() {
        ArtronValues artronValues = new Processor(plugin).getValues();
        for (CostData data : artronValues.calculated()) {
            int cost = Calculator.getMapped(data, artronValues.highest());
            plugin.getArtronConfig().set("upgrades." + TARDISStringUtils.toUnderscoredLowercase(data.name()), cost);
        }
        try {
            plugin.getArtronConfig().save(new File(plugin.getDataFolder(), "artron.yml"));
            plugin.debug("Updated desktop upgrades in artron.yml");
        } catch (IOException io) {
            plugin.debug("Could not save artron.yml, " + io);
        }
    }
}
