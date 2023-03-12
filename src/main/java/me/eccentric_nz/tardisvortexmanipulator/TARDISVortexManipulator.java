package me.eccentric_nz.tardisvortexmanipulator;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.TardisAPI;
import me.eccentric_nz.tardisvortexmanipulator.command.*;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMDatabase;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMMySQL;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMSQLite;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMGUIListener;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMMessageGUIListener;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMSavesGUIListener;
import me.eccentric_nz.tardisvortexmanipulator.listeners.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.module.ModuleDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TARDISVortexManipulator extends JavaPlugin {

    public static TARDISVortexManipulator plugin;
    private final TVMDatabase service = TVMDatabase.getInstance();
    private final List<Location> blocks = new ArrayList<>();
    private final List<UUID> beaconSetters = new ArrayList<>();
    private final List<UUID> travellers = new ArrayList<>();
    private String pluginName;
    private TardisAPI tardisapi;
    private TARDIS tardis;
    private PluginManager pm;
    private String prefix;

    @Override
    public void onDisable() {
        // place any custom disable code here.
    }

    @Override
    public void onEnable() {
        plugin = this;
        PluginDescriptionFile pdfFile = getDescription();
        pluginName = ChatColor.GOLD + "[" + pdfFile.getName() + "]" + ChatColor.RESET + " ";
        saveDefaultConfig();
        new TVMConfig(this).checkConfig();
        pm = getServer().getPluginManager();
        /*
         * Get TARDIS
         */
        Plugin p = pm.getPlugin("TARDIS");
        if (p == null || !pm.isPluginEnabled("TARDIS")) {
            System.err.println("[TARDISVortexManipulator] Cannot find TARDIS!");
            pm.disablePlugin(this);
            return;
        }
        tardis = (TARDIS) p;
        ModuleDescriptor.Version minVersion = ModuleDescriptor.Version.parse("4.7.5");
        // TARDIS version = something like 4.7.5-b2339 or 4.7.5-b11.07.21-5:24
        String version = tardis.getDescription().getVersion().split("-")[0];
        ModuleDescriptor.Version tardisVersion = ModuleDescriptor.Version.parse(version);
        if (tardisVersion.compareTo(minVersion) < 0) {
            System.err.println("[TARDISVortexManipulator] You need a newer version of TARDIS (v4.11.0)!");
            pm.disablePlugin(this);
            return;
        }
        tardisapi = tardis.getTardisAPI();
        prefix = getConfig().getString("storage.mysql.prefix");
        loadDatabase();
        registerListeners();
        registerCommands();
        ShapedRecipe recipe = new TVMRecipe(this).makeRecipe();
        getServer().addRecipe(recipe);
        tardisapi.addShapedRecipe("vortex-manipulator", recipe);
        startRecharger();
    }

    public String getPluginName() {
        return pluginName;
    }

    public TardisAPI getTardisAPI() {
        return tardisapi;
    }

    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets up the database.
     */
    private void loadDatabase() {
        String dbtype = getConfig().getString("storage.database");
        try {
            if (dbtype.equals("sqlite")) {
                String path = getDataFolder() + File.separator + "TVM.db";
                service.setConnection(path);
                TVMSQLite sqlite = new TVMSQLite(this);
                sqlite.createTables();
            } else {
                service.setConnection();
                TVMMySQL mysql = new TVMMySQL(this);
                mysql.createTables();
            }
        } catch (Exception e) {
            getServer().getConsoleSender().sendMessage(pluginName + "Connection and Tables Error: " + e);
        }
    }

    private void registerListeners() {
        pm.registerEvents(new TVMBlockListener(this), this);
        pm.registerEvents(new TVMCraftListener(this), this);
        pm.registerEvents(new TVMDeathListener(this), this);
        pm.registerEvents(new TVMEquipListener(this), this);
        pm.registerEvents(new TVMGUIListener(this), this);
        pm.registerEvents(new TVMMessageGUIListener(this), this);
        pm.registerEvents(new TVMMoveListener(this), this);
        pm.registerEvents(new TVMSavesGUIListener(this), this);
    }

    private void registerCommands() {
        getCommand("vm").setExecutor(new TVMCommand(this));
        getCommand("vma").setExecutor(new TVMCommandActivate(this));
        getCommand("vmb").setExecutor(new TVMCommandBeacon(this));
        getCommand("vmh").setExecutor(new TVMCommandHelp(this));
        getCommand("vmh").setTabCompleter(new TVMTabCompleteHelp());
        getCommand("vml").setExecutor(new TVMCommandLifesigns(this));
        getCommand("vmm").setExecutor(new TVMCommandMessage(this));
        getCommand("vmm").setTabCompleter(new TVMTabCompleteMessage());
        getCommand("vmr").setExecutor(new TVMCommandRemove(this));
        getCommand("vms").setExecutor(new TVMCommandSave(this));
        getCommand("vmg").setExecutor(new TVMCommandGive(this));
        getCommand("vmd").setExecutor(new TVMCommandConvert(this));
    }

    private void startRecharger() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new TVMTachyonRunnable(this), 1200L, getConfig().getLong("tachyon_use.recharge_interval"));
    }

    public List<Location> getBlocks() {
        return blocks;
    }

    public List<UUID> getBeaconSetters() {
        return beaconSetters;
    }

    public List<UUID> getTravellers() {
        return travellers;
    }

    /**
     * Outputs a message to the console. Requires debug: true in config.yml
     *
     * @param o the Object to print to the console
     */
    public void debug(Object o) {
        if (getConfig().getBoolean("debug") == true) {
            getServer().getConsoleSender().sendMessage(pluginName + "Debug: " + o);
        }
    }
}
