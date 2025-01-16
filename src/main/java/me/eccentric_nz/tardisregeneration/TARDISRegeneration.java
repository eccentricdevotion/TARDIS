package me.eccentric_nz.tardisregeneration;

import me.eccentric_nz.TARDIS.TARDIS;

public class TARDISRegeneration {

    private final TARDIS plugin;

    public TARDISRegeneration(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        plugin.getPM().registerEvents(new ElixirOfLifeListener(plugin), plugin);
        plugin.getPM().registerEvents(new UntemperedSchismListener(plugin), plugin);
        plugin.getPM().registerEvents(new VoidListener(plugin), plugin);
        TARDISRegenerationCommand command = new TARDISRegenerationCommand(plugin);
        plugin.getCommand("tardisregeneration").setExecutor(command);
        plugin.getCommand("tardisregeneration").setTabCompleter(command);
        // add elixir recipe
        new ElixirOfLifeRecipe(plugin).addRecipe();
        new UntemperedSchismRecipe(plugin).addRecipe();
    }
}
