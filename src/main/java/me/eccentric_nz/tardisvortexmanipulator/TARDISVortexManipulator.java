package me.eccentric_nz.tardisvortexmanipulator;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisvortexmanipulator.command.*;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMGUIListener;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMMessageGUIListener;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMSavesGUIListener;
import me.eccentric_nz.tardisvortexmanipulator.listeners.*;
import org.bukkit.inventory.ShapedRecipe;

public class TARDISVortexManipulator {

    private final TARDIS plugin;

    public TARDISVortexManipulator(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        plugin.setTvmSettings(new TVMSettings());
        new VortexManipulatorConfig(plugin).checkConfig();
        registerListeners();
        registerCommand();
        ShapedRecipe recipe = new TVMRecipe(plugin).makeRecipe();
        plugin.getServer().addRecipe(recipe);
        plugin.getTardisAPI().addShapedRecipe("vortex-manipulator", recipe);
        startRecharger();
    }

    private void registerListeners() {
        plugin.getPM().registerEvents(new TVMBlockListener(plugin), plugin);
        plugin.getPM().registerEvents(new TVMCraftListener(plugin), plugin);
        plugin.getPM().registerEvents(new TVMDeathListener(plugin), plugin);
        plugin.getPM().registerEvents(new TVMEquipListener(plugin), plugin);
        plugin.getPM().registerEvents(new TVMGUIListener(plugin), plugin);
        plugin.getPM().registerEvents(new TVMMessageGUIListener(plugin), plugin);
        plugin.getPM().registerEvents(new TVMMoveListener(plugin), plugin);
        plugin.getPM().registerEvents(new TVMSavesGUIListener(plugin), plugin);
    }

    private void registerCommand() {
        plugin.getCommand("vm").setExecutor(new TVMCommand(plugin));
        plugin.getCommand("vm").setTabCompleter(new TVMTabComplete());
    }

    private void startRecharger() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TVMTachyonRunnable(plugin), 1200L, plugin.getVortexConfig().getLong("tachyon_use.recharge_interval"));
    }
}
