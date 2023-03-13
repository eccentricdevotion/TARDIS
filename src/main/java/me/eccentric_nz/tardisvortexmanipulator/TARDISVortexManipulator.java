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
        new TVMConfig(plugin).checkConfig();
        registerListeners();
        registerCommands();
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

    private void registerCommands() {
        plugin.getCommand("vm").setExecutor(new TVMCommand(plugin));
        plugin.getCommand("vma").setExecutor(new TVMCommandActivate(plugin));
        plugin.getCommand("vmb").setExecutor(new TVMCommandBeacon(plugin));
        plugin.getCommand("vmh").setExecutor(new TVMCommandHelp(plugin));
        plugin.getCommand("vmh").setTabCompleter(new TVMTabCompleteHelp());
        plugin.getCommand("vml").setExecutor(new TVMCommandLifesigns(plugin));
        plugin.getCommand("vmm").setExecutor(new TVMCommandMessage(plugin));
        plugin.getCommand("vmm").setTabCompleter(new TVMTabCompleteMessage());
        plugin.getCommand("vmr").setExecutor(new TVMCommandRemove(plugin));
        plugin.getCommand("vms").setExecutor(new TVMCommandSave(plugin));
        plugin.getCommand("vmg").setExecutor(new TVMCommandGive(plugin));
    }

    private void startRecharger() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TVMTachyonRunnable(plugin), 1200L, plugin.getVortexConfig().getLong("tachyon_use.recharge_interval"));
    }
}
