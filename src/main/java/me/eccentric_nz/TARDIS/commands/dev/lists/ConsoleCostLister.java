package me.eccentric_nz.TARDIS.commands.dev.lists;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.artron.costs.ArtronValues;
import me.eccentric_nz.TARDIS.artron.costs.Calculator;
import me.eccentric_nz.TARDIS.artron.costs.CostData;
import me.eccentric_nz.TARDIS.artron.costs.Processor;

public class ConsoleCostLister {

    private final TARDIS plugin;

    public ConsoleCostLister(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void actualArtron() {
        ArtronValues artronValues = new Processor(plugin).getValues();
        plugin.debug("| Desktop | Condensed Artron | Old cost     | Updated cost | Mapped cost | Size |");
        plugin.debug("| ------- | ---------------- | ------------ | ------------ | ----------- | ---- |");
        for (CostData data : artronValues.calculated()) {
            int cost = Calculator.getMapped(data, artronValues.highest());
            plugin.debug("| `" + data.name() + "` | " + data.artron() + " | " + data.current() + " | " + data.updated() + " | " + cost + " | " + data.size() + " |");
        }
    }
}
