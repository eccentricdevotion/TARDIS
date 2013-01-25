/*
 * Copyright (C) 2012 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS;

import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.travel.TARDISArea;
import me.eccentric_nz.TARDIS.thirdparty.ImprovedOfflinePlayer_api;
import me.eccentric_nz.TARDIS.thirdparty.MetricsLite;
import me.eccentric_nz.TARDIS.utility.TARDISUtils;
import me.eccentric_nz.TARDIS.utility.TARDISWorldGuardChecker;
import me.eccentric_nz.TARDIS.files.TARDISConfiguration;
import me.eccentric_nz.TARDIS.commands.TARDISPrefsCommands;
import me.eccentric_nz.TARDIS.commands.TARDISCommands;
import me.eccentric_nz.TARDIS.commands.TARDISAreaCommands;
import me.eccentric_nz.TARDIS.commands.TARDISTravelCommands;
import me.eccentric_nz.TARDIS.commands.TARDISAdminCommands;
import me.eccentric_nz.TARDIS.destroyers.TARDISDestroyerInner;
import me.eccentric_nz.TARDIS.destroyers.TARDISDestroyerPoliceBox;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderInner;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderPoliceBox;
import me.eccentric_nz.TARDIS.listeners.TARDISFireListener;
import me.eccentric_nz.TARDIS.listeners.TARDISExplosionListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockPlaceListener;
import me.eccentric_nz.TARDIS.listeners.TARDISWitherDragonListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockBreakListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockDamageListener;
import me.eccentric_nz.TARDIS.listeners.TARDISDoorListener;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import me.eccentric_nz.TARDIS.builders.TARDISSpace;
import me.eccentric_nz.TARDIS.files.TARDISMakeCSV;
import me.eccentric_nz.TARDIS.files.TARDISUpdateChecker;
import me.eccentric_nz.TARDIS.listeners.TARDISAreaListener;
import me.eccentric_nz.TARDIS.listeners.TARDISArtronCapacitorListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBindListener;
import me.eccentric_nz.TARDIS.listeners.TARDISButtonListener;
import me.eccentric_nz.TARDIS.listeners.TARDISCreeperDeathListener;
import me.eccentric_nz.TARDIS.listeners.TARDISGravityWellListener;
import me.eccentric_nz.TARDIS.listeners.TARDISHandbrakeListener;
import me.eccentric_nz.TARDIS.listeners.TARDISJettisonSeeder;
import me.eccentric_nz.TARDIS.listeners.TARDISLightningListener;
import me.eccentric_nz.TARDIS.listeners.TARDISRoomSeeder;
import me.eccentric_nz.TARDIS.listeners.TARDISSignListener;
import me.eccentric_nz.TARDIS.listeners.TARDISUpdateListener;
import me.eccentric_nz.TARDIS.travel.TARDISUpdateTravellerCount;
import me.eccentric_nz.TARDIS.utility.TARDISCreeperChecker;
import me.eccentric_nz.TARDIS.utility.TARDISTownyChecker;
import me.eccentric_nz.TARDIS.utility.TARDISWorldBorderChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class where everything is enabled and disabled.
 *
 * "TARDIS" is an acronym meaning "Time And Relative Dimension In Space".
 * TARDISes move through time and space by "disappearing there and reappearing
 * here", a process known as "de- and re-materialisation". TARDISes are used for
 * the observation of various places and times.
 *
 * @author eccentric_nz
 */
public class TARDIS extends JavaPlugin {

    public ImprovedOfflinePlayer_api iopHandler;
    TARDISDatabase service = TARDISDatabase.getInstance();
    public TARDISMakeCSV csv = new TARDISMakeCSV(this);
    public PluginDescriptionFile pdfFile;
    public File budgetSchematicFile = null;
    public File biggerSchematicFile = null;
    public File deluxeSchematicFile = null;
    public File arboretumSchematicFile = null;
    public File bedroomSchematicFile = null;
    public File kitchenSchematicFile = null;
    public File librarySchematicFile = null;
    public File passageSchematicFile = null;
    public File poolSchematicFile = null;
    public File vaultSchematicFile = null;
    public File emptySchematicFile = null;
    public File gravitySchematicFile = null;
    public File budgetSchematicCSV = null;
    public File biggerSchematicCSV = null;
    public File deluxeSchematicCSV = null;
    public File arboretumSchematicCSV = null;
    public File bedroomSchematicCSV = null;
    public File kitchenSchematicCSV = null;
    public File librarySchematicCSV = null;
    public File passageSchematicCSV = null;
    public File passageSchematicCSV_EW = null;
    public File poolSchematicCSV = null;
    public File vaultSchematicCSV = null;
    public File emptySchematicCSV = null;
    public File gravitySchematicCSV = null;
    public File quotesfile = null;
    public String[][][] budgetschematic;
    public String[][][] biggerschematic;
    public String[][][] deluxeschematic;
    public String[][][] arboretumschematic;
    public String[][][] bedroomschematic;
    public String[][][] kitchenschematic;
    public String[][][] libraryschematic;
    public String[][][] passageschematic;
    public String[][][] passageschematic_EW;
    public String[][][] poolschematic;
    public String[][][] vaultschematic;
    public String[][][] emptyschematic;
    public String[][][] gravityschematic;
    public short[] budgetdimensions = new short[3];
    public short[] biggerdimensions = new short[3];
    public short[] deluxedimensions = new short[3];
    public short[] passagedimensions = new short[3];
    public short[] arboretumdimensions = new short[3];
    public short[] pooldimensions = new short[3];
    public short[] roomdimensions = new short[3];
    public short[] gravitydimensions = new short[3];
    public static TARDIS plugin;
    public TARDISUtils utils = new TARDISUtils(this);
    private TARDISCommands tardisCommand;
    private TARDISAdminCommands tardisAdminCommand;
    private TARDISPrefsCommands tardisPrefsCommand;
    private TARDISTravelCommands tardisTravelCommand;
    private TARDISAreaCommands tardisAreaCommand;
    public TARDISBuilderInner buildI = new TARDISBuilderInner(this);
    public TARDISBuilderPoliceBox buildPB = new TARDISBuilderPoliceBox(this);
    public TARDISDestroyerInner destroyI = new TARDISDestroyerInner(this);
    public TARDISDestroyerPoliceBox destroyPB = new TARDISDestroyerPoliceBox(this);
    public TARDISArea ta = new TARDISArea(this);
    public TARDISWorldGuardChecker wgchk;
    public TARDISTownyChecker tychk;
    public TARDISWorldBorderChecker borderchk;
    TARDISBlockPlaceListener blockPlaceListener = new TARDISBlockPlaceListener(this);
    TARDISBlockBreakListener blockBreakListener = new TARDISBlockBreakListener(this);
    TARDISDoorListener doorListener = new TARDISDoorListener(this);
    TARDISButtonListener buttonListener = new TARDISButtonListener(this);
    TARDISSignListener signListener = new TARDISSignListener(this);
    TARDISAreaListener areaListener = new TARDISAreaListener(this);
    TARDISUpdateListener updateListener = new TARDISUpdateListener(this);
    TARDISFireListener protectListener = new TARDISFireListener(this);
    TARDISBlockDamageListener damageListener = new TARDISBlockDamageListener(this);
    TARDISExplosionListener explosionListener = new TARDISExplosionListener(this);
    TARDISWitherDragonListener dragonListener = new TARDISWitherDragonListener(this);
    TARDISLightningListener lightningListener = new TARDISLightningListener(this);
    TARDISCreeperDeathListener creeperListener = new TARDISCreeperDeathListener(this);
    TARDISArtronCapacitorListener energyListener = new TARDISArtronCapacitorListener(this);
    TARDISRoomSeeder seedListener = new TARDISRoomSeeder(this);
    TARDISJettisonSeeder jettisonListener = new TARDISJettisonSeeder(this);
    TARDISBindListener bindListener = new TARDISBindListener(this);
    TARDISHandbrakeListener handbrakeListener = new TARDISHandbrakeListener(this);
    TARDISGravityWellListener gravityListener = new TARDISGravityWellListener(this);
    public PluginManager pm = Bukkit.getServer().getPluginManager();
    public HashMap<String, String> trackPlayers = new HashMap<String, String>();
    public HashMap<String, Integer> trackBinder = new HashMap<String, Integer>();
    public HashMap<String, String> trackName = new HashMap<String, String>();
    public HashMap<String, String> trackBlock = new HashMap<String, String>();
    public HashMap<String, String> trackEnd = new HashMap<String, String>();
    public HashMap<String, String> trackPerm = new HashMap<String, String>();
    public HashMap<String, String> trackDest = new HashMap<String, String>();
    public HashMap<String, String> trackRoomSeed = new HashMap<String, String>();
    public HashMap<String, String> trackJettison = new HashMap<String, String>();
    public HashMap<Integer, Integer> trackTravellers = new HashMap<Integer, Integer>();
    public HashMap<Integer, Integer> tardisHasTravelled = new HashMap<Integer, Integer>();
    public ArrayList<Integer> tardisMaterilising = new ArrayList<Integer>();
    private static ArrayList<String> quotes = new ArrayList<String>();
    public ArrayList<String> quote;
    public HashMap<Material, String> seeds;
    public int quotelen;
    public boolean worldGuardOnServer = false;
    public boolean townyOnServer = false;
    public boolean borderOnServer = false;
    public ConsoleCommandSender console;
    public String pluginName;
    public String TARDIS_KEY;
    public boolean myspawn = false;

    @Override
    public void onEnable() {
        pdfFile = getDescription();
        pluginName = ChatColor.GOLD + "[" + pdfFile.getName() + "]" + ChatColor.RESET + " ";
        plugin = this;
        console = getServer().getConsoleSender();

        loadClasses();
        saveDefaultConfig();
        TARDISConfiguration tc = new TARDISConfiguration(this);
        tc.checkConfig();
        checkTCG();
        loadDatabase();
        loadFiles();
        seeds = getSeeds();
        registerListeners();
        loadCommands();
        loadMetrics();
        startSound();
        loadWorldGuard();
        loadTowny();
        loadWorldBorder();

        TARDIS_KEY = getConfig().getString("key");
        quote = quotes();
        quotelen = quote.size();
        if (getConfig().getBoolean("check_for_updates")) {
            TARDISUpdateChecker update = new TARDISUpdateChecker(this);
            update.checkVersion(null);
        }

        TARDISUpdateTravellerCount utc = new TARDISUpdateTravellerCount(this);
        utc.getTravellers();
        TARDISCreeperChecker cc = new TARDISCreeperChecker(this);
        cc.startCreeperCheck();
        TARDISSpace alwaysNight = new TARDISSpace(this);
        alwaysNight.keepNight();
    }

    @Override
    public void onDisable() {
        saveConfig();
        closeDatabase();
    }

    /**
     * Used to load net.minecraft.server methods for various versions of
     * CraftBukkit.
     */
    private void loadClasses() {
        String packageName = this.getServer().getClass().getPackage().getName();
        // Get full package string of CraftServer.
        // org.bukkit.craftbukkit.versionstring (or for pre-refactor, just org.bukkit.craftbukkit
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        // Get the last element of the package
        if (version.equals("craftbukkit")) { // If the last element of the package was "craftbukkit" we are now pre-refactor
            version = "pre";
        }
        try {
            final Class<?> clazz = Class.forName("me.eccentric_nz.TARDIS.thirdparty.ImprovedOfflinePlayer_" + version);
            // Check if we have a NMSHandler class at that location.
            if (ImprovedOfflinePlayer_api.class.isAssignableFrom(clazz)) { // Make sure it actually implements IOP
                this.iopHandler = (ImprovedOfflinePlayer_api) clazz.getConstructor().newInstance(); // Set our handler
            }
        } catch (final Exception e) {
            this.getLogger().severe("Could not find support for this CraftBukkit version.");
            this.getLogger().info("Check for updates at http://dev.bukkit.org/server-mods/tardis/");
            this.setEnabled(false);
            return;
        }
        console.sendMessage(pluginName + "Loading support for CB " + version);
    }

    /**
     * Sets up the database.
     */
    private void loadDatabase() {
        try {
            String path = getDataFolder() + File.separator + "TARDIS.db";
            service.setConnection(path);
            service.createTables();
        } catch (Exception e) {
            console.sendMessage(pluginName + "Connection and Tables Error: " + e);
        }
    }

    /**
     * Closes the database.
     */
    private void closeDatabase() {
        try {
            service.connection.close();
        } catch (Exception e) {
            console.sendMessage(pluginName + "Could not close database connection: " + e);
        }
    }

    /**
     * Registers all the listeners for the various events required to use the
     * TARDIS.
     */
    private void registerListeners() {
        pm.registerEvents(blockPlaceListener, this);
        pm.registerEvents(blockBreakListener, this);
        pm.registerEvents(doorListener, this);
        pm.registerEvents(buttonListener, this);
        pm.registerEvents(signListener, this);
        pm.registerEvents(updateListener, this);
        pm.registerEvents(areaListener, this);
        pm.registerEvents(protectListener, this);
        pm.registerEvents(damageListener, this);
        pm.registerEvents(explosionListener, this);
        pm.registerEvents(dragonListener, this);
        pm.registerEvents(lightningListener, this);
        pm.registerEvents(creeperListener, this);
        pm.registerEvents(energyListener, this);
        pm.registerEvents(seedListener, this);
        pm.registerEvents(jettisonListener, this);
        pm.registerEvents(bindListener, this);
        pm.registerEvents(handbrakeListener, this);
        pm.registerEvents(gravityListener, this);
    }

    /**
     * Loads all the commands that the TARDIS uses.
     */
    private void loadCommands() {
        tardisCommand = new TARDISCommands(this);
        tardisAdminCommand = new TARDISAdminCommands(this);
        tardisPrefsCommand = new TARDISPrefsCommands(this);
        tardisTravelCommand = new TARDISTravelCommands(this);
        tardisAreaCommand = new TARDISAreaCommands(this);
        getCommand("tardis").setExecutor(tardisCommand);
        getCommand("tardisadmin").setExecutor(tardisAdminCommand);
        getCommand("tardisprefs").setExecutor(tardisPrefsCommand);
        getCommand("tardistravel").setExecutor(tardisTravelCommand);
        getCommand("tardisarea").setExecutor(tardisAreaCommand);
    }

    /**
     * Builds the schematics used to create TARDISes and rooms. Also loads the
     * quotes from the quotes file.
     */
    private void loadFiles() {
        csv.loadCSV();
        quotesfile = csv.copy(getDataFolder() + File.separator + TARDISConstants.QUOTES_FILE_NAME, getResource(TARDISConstants.QUOTES_FILE_NAME));
    }

    /**
     * Starts the sending of plugin statistics. To stop TARDIS sending metrics
     * data, edit plugins/PluginMetrics/config.yml and set opt-out to true.
     */
    private void loadMetrics() {
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
    }

    /**
     * Starts a repeating tasks that plays TARDIS sound effects to players while
     * they are inside the TARDIS. Requires the Spout plugin to be installed on
     * the server.
     */
    private void startSound() {
        if (plugin.getServer().getPluginManager().getPlugin("Spout") != null && getConfig().getBoolean("sfx")) {
            this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    TARDISSounds.randomTARDISSound();
                }
            }, 60L, 1200L);
        }
    }

    /**
     * Checks if the WorldGuard plugin is available, and loads support if it is.
     */
    private void loadWorldGuard() {
        if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            worldGuardOnServer = true;
            wgchk = new TARDISWorldGuardChecker(this);
        }
    }

    /**
     * Checks if the Towny plugin is available, and loads support if it is.
     */
    private void loadTowny() {
        if (getServer().getPluginManager().getPlugin("Towny") != null) {
            townyOnServer = true;
            tychk = new TARDISTownyChecker(this);
        }
    }

    /**
     * Checks if the WorldBorder plugin is available, and loads support if it
     * is.
     */
    private void loadWorldBorder() {
        if (getServer().getPluginManager().getPlugin("WorldBorder") != null) {
            borderOnServer = true;
            borderchk = new TARDISWorldBorderChecker(this);
        }
    }

    /**
     * Loads the quotes from a text file.
     */
    public ArrayList<String> quotes() {
        if (quotesfile != null) {
            BufferedReader bufRdr = null;
            try {
                bufRdr = new BufferedReader(new FileReader(quotesfile));
                String line;
                //read each line of text file
                while ((line = bufRdr.readLine()) != null) {
                    quotes.add(line);
                }
                if (quotes.size() < 1) {
                    quotes.add("");
                }
            } catch (IOException io) {
                console.sendMessage(pluginName + "Could not read quotes file");
            } finally {
                if (bufRdr != null) {
                    try {
                        bufRdr.close();
                    } catch (Exception e) {
                        plugin.debug("Error closing quotes reader! " + e.getMessage());
                    }
                }
            }
        }
        return quotes;
    }

    /**
     * Reads the config file and places the configured seed material for each
     * room type into a HashMap.
     */
    private HashMap<Material, String> getSeeds() {
        HashMap<Material, String> map = new HashMap<Material, String>();
        Set<String> rooms = getConfig().getConfigurationSection("rooms").getKeys(false);
        for (String s : rooms) {
            Material m = Material.valueOf(getConfig().getString("rooms." + s + ".seed"));
            map.put(m, s);
        }
        return map;
    }

    private void checkTCG() {
        if (getConfig().getBoolean("create_worlds")) {
            if (getServer().getPluginManager().getPlugin("TARDISChunkGenerator") == null || (pm.getPlugin("Multiverse") == null && pm.getPlugin("MultiWorld") == null)) {
                getConfig().set("create_worlds", false);
                saveConfig();
                console.sendMessage(pluginName + "Create Worlds was disabled as it requires a multi-world plugin and TARDISChunkGenerator!");
            }
        }
    }

    /**
     * Outputs a message to the console. Requires debug: true in the config.
     */
    public void debug(Object o) {
        if (getConfig().getBoolean("debug") == true) {
            console.sendMessage(pluginName + "Debug: " + o);
        }
    }
}
