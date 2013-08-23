/*
 * Copyright (C) 2013 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderInner;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderPoliceBox;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonPreset;
import me.eccentric_nz.TARDIS.builders.TARDISSpace;
import me.eccentric_nz.TARDIS.commands.TARDISAdminCommands;
import me.eccentric_nz.TARDIS.commands.TARDISAreaCommands;
import me.eccentric_nz.TARDIS.commands.TARDISBindCommands;
import me.eccentric_nz.TARDIS.commands.TARDISBookCommands;
import me.eccentric_nz.TARDIS.commands.TARDISCommands;
import me.eccentric_nz.TARDIS.commands.TARDISGravityCommands;
import me.eccentric_nz.TARDIS.commands.TARDISPrefsCommands;
import me.eccentric_nz.TARDIS.commands.TARDISRecipeCommands;
import me.eccentric_nz.TARDIS.commands.TARDISRoomCommands;
import me.eccentric_nz.TARDIS.commands.TARDISTextureCommands;
import me.eccentric_nz.TARDIS.commands.TARDISTravelCommands;
import me.eccentric_nz.TARDIS.database.TARDISControlsConverter;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import me.eccentric_nz.TARDIS.destroyers.TARDISDestroyerInner;
import me.eccentric_nz.TARDIS.destroyers.TARDISDestroyerPoliceBox;
import me.eccentric_nz.TARDIS.files.TARDISBlockLoader;
import me.eccentric_nz.TARDIS.files.TARDISConfiguration;
import me.eccentric_nz.TARDIS.files.TARDISMakeRoomCSV;
import me.eccentric_nz.TARDIS.files.TARDISMakeTardisCSV;
import me.eccentric_nz.TARDIS.files.TARDISUpdateChecker;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import me.eccentric_nz.TARDIS.ARS.TARDISARSListener;
import me.eccentric_nz.TARDIS.database.ResultSetPoliceBox;
import me.eccentric_nz.TARDIS.listeners.TARDISAdminMenuListener;
import me.eccentric_nz.TARDIS.listeners.TARDISAnvilListener;
import me.eccentric_nz.TARDIS.listeners.TARDISAreaListener;
import me.eccentric_nz.TARDIS.listeners.TARDISArtronCapacitorListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBindListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockBreakListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockDamageListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockPlaceListener;
import me.eccentric_nz.TARDIS.listeners.TARDISButtonListener;
import me.eccentric_nz.TARDIS.listeners.TARDISChatListener;
import me.eccentric_nz.TARDIS.listeners.TARDISChunkListener;
import me.eccentric_nz.TARDIS.listeners.TARDISCondenserListener;
import me.eccentric_nz.TARDIS.listeners.TARDISCreeperDeathListener;
import me.eccentric_nz.TARDIS.listeners.TARDISDoorListener;
import me.eccentric_nz.TARDIS.listeners.TARDISEntityGriefListener;
import me.eccentric_nz.TARDIS.listeners.TARDISExplosionListener;
import me.eccentric_nz.TARDIS.listeners.TARDISFireListener;
import me.eccentric_nz.TARDIS.listeners.TARDISGravityWellListener;
import me.eccentric_nz.TARDIS.listeners.TARDISHandbrakeListener;
import me.eccentric_nz.TARDIS.listeners.TARDISHorseListener;
import me.eccentric_nz.TARDIS.listeners.TARDISHotbarListener;
import me.eccentric_nz.TARDIS.listeners.TARDISIceMeltListener;
import me.eccentric_nz.TARDIS.info.TARDISInformationSystemListener;
import me.eccentric_nz.TARDIS.listeners.TARDISAreaSignListener;
import me.eccentric_nz.TARDIS.listeners.TARDISJettisonSeeder;
import me.eccentric_nz.TARDIS.listeners.TARDISJoinListener;
import me.eccentric_nz.TARDIS.listeners.TARDISKeyboardListener;
import me.eccentric_nz.TARDIS.listeners.TARDISLightningListener;
import me.eccentric_nz.TARDIS.listeners.TARDISMinecartListener;
import me.eccentric_nz.TARDIS.listeners.TARDISNPCListener;
import me.eccentric_nz.TARDIS.listeners.TARDISRecipeListener;
import me.eccentric_nz.TARDIS.listeners.TARDISRoomSeeder;
import me.eccentric_nz.TARDIS.listeners.TARDISSaveSignListener;
import me.eccentric_nz.TARDIS.listeners.TARDISScannerListener;
import me.eccentric_nz.TARDIS.listeners.TARDISSignListener;
import me.eccentric_nz.TARDIS.listeners.TARDISStattenheimListener;
import me.eccentric_nz.TARDIS.listeners.TARDISTeleportListener;
import me.eccentric_nz.TARDIS.listeners.TARDISTemporalLocatorListener;
import me.eccentric_nz.TARDIS.listeners.TARDISTerminalListener;
import me.eccentric_nz.TARDIS.listeners.TARDISTimeLordDeathListener;
import me.eccentric_nz.TARDIS.listeners.TARDISUpdateListener;
import me.eccentric_nz.TARDIS.listeners.TARDISWorldResetListener;
import me.eccentric_nz.TARDIS.rooms.TARDISCondenserData;
import me.eccentric_nz.TARDIS.thirdparty.MetricsLite;
import me.eccentric_nz.TARDIS.thirdparty.Version;
import me.eccentric_nz.TARDIS.travel.TARDISArea;
import me.eccentric_nz.TARDIS.utility.TARDISItemRecipes;
import me.eccentric_nz.TARDIS.utility.TARDISCreeperChecker;
import me.eccentric_nz.TARDIS.utility.TARDISFactionsChecker;
import me.eccentric_nz.TARDIS.utility.TARDISMultiverseInventoriesChecker;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISTownyChecker;
import me.eccentric_nz.TARDIS.utility.TARDISUtils;
import me.eccentric_nz.TARDIS.utility.TARDISWorldBorderChecker;
import me.eccentric_nz.TARDIS.utility.TARDISWorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
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

    public static TARDIS plugin;
    private static ArrayList<String> quotes = new ArrayList<String>();
    TARDISDatabase service = TARDISDatabase.getInstance();
    public TARDISMakeTardisCSV tardisCSV = new TARDISMakeTardisCSV(this);
    public TARDISMakeRoomCSV roomCSV = new TARDISMakeRoomCSV(this);
    public PluginDescriptionFile pdfFile;
    public File arsSchematicFile = null;
    public File budgetSchematicFile = null;
    public File biggerSchematicFile = null;
    public File deluxeSchematicFile = null;
    public File eleventhSchematicFile = null;
    public File redstoneSchematicFile = null;
    public File steampunkSchematicFile = null;
    public File plankSchematicFile = null;
    public File tomSchematicFile = null;
    public File customSchematicFile = null;
    public File arsSchematicCSV = null;
    public File budgetSchematicCSV = null;
    public File biggerSchematicCSV = null;
    public File deluxeSchematicCSV = null;
    public File eleventhSchematicCSV = null;
    public File redstoneSchematicCSV = null;
    public File steampunkSchematicCSV = null;
    public File plankSchematicCSV = null;
    public File tomSchematicCSV = null;
    public File customSchematicCSV = null;
    public File quotesfile = null;
    public String[][][] arsschematic;
    public String[][][] budgetschematic;
    public String[][][] biggerschematic;
    public String[][][] deluxeschematic;
    public String[][][] eleventhschematic;
    public String[][][] redstoneschematic;
    public String[][][] steampunkschematic;
    public String[][][] plankschematic;
    public String[][][] tomschematic;
    public String[][][] customschematic;
    public HashMap<String, String[][][]> room_schematics = new HashMap<String, String[][][]>();
    public short[] arsdimensions = new short[3];
    public short[] budgetdimensions = new short[3];
    public short[] biggerdimensions = new short[3];
    public short[] deluxedimensions = new short[3];
    public short[] eleventhdimensions = new short[3];
    public short[] redstonedimensions = new short[3];
    public short[] steampunkdimensions = new short[3];
    public short[] plankdimensions = new short[3];
    public short[] tomdimensions = new short[3];
    public short[] customdimensions = new short[3];
    public HashMap<String, short[]> room_dimensions = new HashMap<String, short[]>();
    public TARDISUtils utils = new TARDISUtils(this);
    public TARDISCommands tardisCommand;
    public TARDISAdminCommands tardisAdminCommand;
    public TARDISBuilderInner buildI = new TARDISBuilderInner(this);
    public TARDISBuilderPoliceBox buildPB = new TARDISBuilderPoliceBox(this);
    public TARDISDestroyerInner destroyI = new TARDISDestroyerInner(this);
    public TARDISDestroyerPoliceBox destroyPB = new TARDISDestroyerPoliceBox(this);
    public TARDISArea ta = new TARDISArea(this);
    public TARDISWorldGuardUtils wgchk;
    public TARDISTownyChecker tychk;
    public TARDISWorldBorderChecker borderchk;
    public TARDISFactionsChecker factionschk;
    public PluginManager pm = Bukkit.getServer().getPluginManager();
    public HashMap<String, String> trackPlayers = new HashMap<String, String>();
    public HashMap<String, Integer> trackBinder = new HashMap<String, Integer>();
    public HashMap<String, String> trackChat = new HashMap<String, String>();
    public HashMap<String, String> trackName = new HashMap<String, String>();
    public HashMap<String, String> trackBlock = new HashMap<String, String>();
    public HashMap<String, String> trackEnd = new HashMap<String, String>();
    public HashMap<String, String> trackPerm = new HashMap<String, String>();
    public HashMap<String, String> trackDest = new HashMap<String, String>();
    public HashMap<String, String> trackRoomSeed = new HashMap<String, String>();
    public HashMap<String, String> trackJettison = new HashMap<String, String>();
    public HashMap<String, String> trackSecondary = new HashMap<String, String>();
    public HashMap<String, Double[]> trackGravity = new HashMap<String, Double[]>();
    public HashMap<Integer, String> trackRescue = new HashMap<Integer, String>();
    public HashMap<Integer, Integer> trackDamage = new HashMap<Integer, Integer>();
    public HashMap<Integer, Integer> tardisHasDestination = new HashMap<Integer, Integer>();
    public HashMap<String, Block> trackExterminate = new HashMap<String, Block>();
    public HashMap<String, Long> trackSetTime = new HashMap<String, Long>();
    public HashMap<String, TARDISInfoMenu> trackInfoMenu = new HashMap<String, TARDISInfoMenu>();
    public List<String> trackRecipeView = new ArrayList<String>();
    public List<String> trackReset = new ArrayList<String>();
    public List<String> trackFarming = new ArrayList<String>();
    public List<Integer> trackMinecart = new ArrayList<Integer>();
    public List<Integer> trackSubmarine = new ArrayList<Integer>();
    public List<Integer> trackARS = new ArrayList<Integer>();
    public ArrayList<Integer> tardisMaterialising = new ArrayList<Integer>();
    public ArrayList<Integer> tardisDematerialising = new ArrayList<Integer>();
    public List<Chunk> tardisChunkList = new ArrayList<Chunk>();
    public List<Chunk> roomChunkList = new ArrayList<Chunk>();
    public HashMap<String, Double[]> gravityUpList = new HashMap<String, Double[]>();
    public List<String> gravityDownList = new ArrayList<String>();
    public HashMap<String, Sign> trackSign = new HashMap<String, Sign>();
    public HashMap<String, Double[]> gravityNorthList = new HashMap<String, Double[]>();
    public HashMap<String, Double[]> gravityWestList = new HashMap<String, Double[]>();
    public HashMap<String, Double[]> gravitySouthList = new HashMap<String, Double[]>();
    public HashMap<String, Double[]> gravityEastList = new HashMap<String, Double[]>();
    public HashMap<String, Integer> protectBlockMap = new HashMap<String, Integer>();
    public HashMap<String, TARDISCondenserData> roomCondenserData = new HashMap<String, TARDISCondenserData>();
    public List<Integer> npcIDs = new ArrayList<Integer>();
    public ArrayList<String> quote;
    public HashMap<Material, String> seeds;
    public int quotelen;
    public boolean worldGuardOnServer = false;
    public boolean townyOnServer = false;
    public boolean borderOnServer = false;
    public boolean factionsOnServer = false;
    public ConsoleCommandSender console;
    public String pluginName;
    public boolean myspawn = false;
    public HashMap<String, HashMap<String, Integer>> roomBlockCounts = new HashMap<String, HashMap<String, Integer>>();
    public String tp;
    public FileConfiguration achivement_config;
    private FileConfiguration artron_config;
    private FileConfiguration blocks_config;
    private FileConfiguration rooms_config;
    public TARDISButtonListener buttonListener;
    public TARDISDoorListener doorListener;
    public Version bukkitversion;
    public Version preemeraldversion = new Version("1.3.1");
    public Version prewoodbuttonversion = new Version("1.4.2");
    public Version preIMversion = new Version("1.4.5");
    public Version precomparatorversion = new Version("1.5");
    public Version precarpetversion = new Version("1.6");
    public Version SUBversion;
    public Version preSUBversion = new Version("1.0");
    public TARDISTabCompleteAPI apiHandler;
    public TARDISChameleonPreset presets;
    public TARDISMultiverseInventoriesChecker tmic;

    @Override
    public void onEnable() {
        pdfFile = getDescription();
        pluginName = ChatColor.GOLD + "[" + pdfFile.getName() + "]" + ChatColor.RESET + " ";
        plugin = this;
        console = getServer().getConsoleSender();
        String[] v = Bukkit.getServer().getBukkitVersion().split("-");
        bukkitversion = (!v[0].equalsIgnoreCase("unknown")) ? new Version(v[0]) : new Version("1.4.7");
        SUBversion = (!v[0].equalsIgnoreCase("unknown")) ? new Version(v[1].substring(1, v[1].length())) : new Version("4.7");

        saveDefaultConfig();
        loadCustomConfigs();
        TARDISConfiguration tc = new TARDISConfiguration(this);
        tc.checkConfig();
        checkTCG();
        seeds = getSeeds();
        loadDatabase();
        loadFiles();
        registerListeners();
        loadCommands();
        loadMetrics();
        startSound();
        loadWorldGuard();
        loadTowny();
        loadWorldBorder();
        loadFactions();

        quote = quotes();
        quotelen = quote.size();
        if (getConfig().getBoolean("check_for_updates")) {
            TARDISUpdateChecker update = new TARDISUpdateChecker(this);
            update.checkVersion(null);
        }

        TARDISCreeperChecker cc = new TARDISCreeperChecker(this);
        cc.startCreeperCheck();
        if (pm.isPluginEnabled("TARDISChunkGenerator")) {
            TARDISSpace alwaysNight = new TARDISSpace(this);
            if (getConfig().getBoolean("keep_night")) {
                alwaysNight.keepNight();
            }
        }
        new ResultSetPoliceBox(this).loadChunks();
        TARDISBlockLoader bl = new TARDISBlockLoader(this);
        bl.loadProtectBlocks();
        bl.loadGravityWells();
        loadPerms();
        loadBooks();
        if (!getConfig().getBoolean("conversion_done")) {
            new TARDISControlsConverter(this).convertControls();
        }
        tp = getServerTP();
        //new TARDISPasteBox(this).loadBoxes();
        if (bukkitversion.compareTo(preIMversion) >= 0) {
            // copy maps
            checkMaps();
            // register recipes
            TARDISItemRecipes r = new TARDISItemRecipes(this);
            r.locator();
            r.locatorCircuit();
            r.materialisationCircuit();
            r.sonic();
            r.stattenheim();
            r.stattenheimCircuit();
        }
        presets = new TARDISChameleonPreset();
        presets.makePresets();
        if (pm.isPluginEnabled("Multiverse-Inventories")) {
            tmic = new TARDISMultiverseInventoriesChecker(this);
        }
    }

    @Override
    public void onDisable() {
        saveConfig();
        closeDatabase();
        resetTime();
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
     * Loads the custom config files.
     */
    private void loadCustomConfigs() {
        tardisCSV.copy(getDataFolder() + File.separator + "achievements.yml", getResource("achievements.yml"));
        tardisCSV.copy(getDataFolder() + File.separator + "artron.yml", getResource("artron.yml"));
        tardisCSV.copy(getDataFolder() + File.separator + "blocks.yml", getResource("blocks.yml"));
        tardisCSV.copy(getDataFolder() + File.separator + "rooms.yml", getResource("rooms.yml"));
        this.achivement_config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "achievements.yml"));
        if (this.achivement_config.getString("travel.message").equals("Life of the party!")) {
            this.achivement_config.set("travel.message", "There and back again!");
            try {
                this.achivement_config.save(getDataFolder() + File.separator + "achievements.yml");
            } catch (IOException io) {
                debug("Could not save achievements.yml " + io);
            }
        }
        this.artron_config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "artron.yml"));
        this.blocks_config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "blocks.yml"));
        this.rooms_config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "rooms.yml"));
    }

    /**
     * Registers all the listeners for the various events required to use the
     * TARDIS.
     */
    private void registerListeners() {
        pm.registerEvents(new TARDISBlockPlaceListener(this), this);
        pm.registerEvents(new TARDISBlockBreakListener(this), this);
        this.doorListener = new TARDISDoorListener(this);
        pm.registerEvents(doorListener, this);
        this.buttonListener = new TARDISButtonListener(this);
        pm.registerEvents(buttonListener, this);
        pm.registerEvents(new TARDISSignListener(this), this);
        pm.registerEvents(new TARDISUpdateListener(this), this);
        pm.registerEvents(new TARDISAreaListener(this), this);
        pm.registerEvents(new TARDISFireListener(this), this);
        pm.registerEvents(new TARDISBlockDamageListener(this), this);
        pm.registerEvents(new TARDISExplosionListener(this), this);
        pm.registerEvents(new TARDISEntityGriefListener(this), this);
        pm.registerEvents(new TARDISLightningListener(this), this);
        pm.registerEvents(new TARDISCreeperDeathListener(this), this);
        pm.registerEvents(new TARDISArtronCapacitorListener(this), this);
        pm.registerEvents(new TARDISRoomSeeder(this), this);
        pm.registerEvents(new TARDISJettisonSeeder(this), this);
        pm.registerEvents(new TARDISBindListener(this), this);
        pm.registerEvents(new TARDISHandbrakeListener(this), this);
        pm.registerEvents(new TARDISGravityWellListener(this), this);
        pm.registerEvents(new TARDISCondenserListener(this), this);
        pm.registerEvents(new TARDISIceMeltListener(this), this);
        pm.registerEvents(new TARDISChunkListener(this), this);
        pm.registerEvents(new TARDISScannerListener(this), this);
        pm.registerEvents(new TARDISTimeLordDeathListener(this), this);
        pm.registerEvents(new TARDISJoinListener(this), this);
        pm.registerEvents(new TARDISKeyboardListener(this), this);
        if (bukkitversion.compareTo(preIMversion) >= 0) {
            pm.registerEvents(new TARDISTerminalListener(this), this);
            pm.registerEvents(new TARDISARSListener(this), this);
            pm.registerEvents(new TARDISSaveSignListener(this), this);
            pm.registerEvents(new TARDISAreaSignListener(this), this);
            pm.registerEvents(new TARDISStattenheimListener(this), this);
            pm.registerEvents(new TARDISHotbarListener(this), this);
            pm.registerEvents(new TARDISAdminMenuListener(this), this);
            pm.registerEvents(new TARDISTemporalLocatorListener(this), this);
            pm.registerEvents(new TARDISRecipeListener(this), this);
        }
        if (getNPCManager()) {
            pm.registerEvents(new TARDISNPCListener(this), this);
        }
        if (bukkitversion.compareTo(precomparatorversion) >= 0) {
            pm.registerEvents(new TARDISChatListener(this), this);
            pm.registerEvents(new TARDISMinecartListener(this), this);
        }
        if (bukkitversion.compareTo(precarpetversion) >= 0) {
            pm.registerEvents(new TARDISHorseListener(this), this);
        }
        pm.registerEvents(new TARDISTeleportListener(this), this);
        if (bukkitversion.compareTo(prewoodbuttonversion) >= 0) {
            pm.registerEvents(new TARDISAnvilListener(this), this);
        }
        pm.registerEvents(new TARDISInformationSystemListener(this), this);
        if (pm.isPluginEnabled("Multiverse-Adventure")) {
            pm.registerEvents(new TARDISWorldResetListener(this), this);
        }
    }

    /**
     * Loads all the commands that the TARDIS uses.
     */
    private void loadCommands() {
        tardisCommand = new TARDISCommands(this);
        getCommand("tardis").setExecutor(tardisCommand);
        tardisAdminCommand = new TARDISAdminCommands(this);
        getCommand("tardisadmin").setExecutor(tardisAdminCommand);
        getCommand("tardisarea").setExecutor(new TARDISAreaCommands(this));
        getCommand("tardisbind").setExecutor(new TARDISBindCommands(this));
        getCommand("tardisbook").setExecutor(new TARDISBookCommands(this));
        getCommand("tardisgravity").setExecutor(new TARDISGravityCommands(this));
        getCommand("tardisprefs").setExecutor(new TARDISPrefsCommands(this));
        getCommand("tardisroom").setExecutor(new TARDISRoomCommands(this));
        getCommand("tardistexture").setExecutor(new TARDISTextureCommands(this));
        getCommand("tardistravel").setExecutor(new TARDISTravelCommands(this));
        getCommand("tardisrecipe").setExecutor(new TARDISRecipeCommands(this));
        if (this.bukkitversion.compareTo(this.preemeraldversion) > 0) {
            // need to dynamically load these classes
            try {
                final Class<?> clazz = Class.forName("me.eccentric_nz.TARDIS.TARDISLoader_TabComplete");
                if (TARDISTabCompleteAPI.class.isAssignableFrom(clazz)) { // Make sure it actually implements the API
                    apiHandler = (TARDISTabCompleteAPI) clazz.getConstructor().newInstance(); // Set the handler
                }
                apiHandler.loadTabCompletion();
            } catch (Exception e) {
                debug("Could not load Tab Completion classes " + e.getMessage());
            }
        }
    }

    /**
     * Builds the schematics used to create TARDISes and rooms. Also loads the
     * quotes from the quotes file.
     */
    private void loadFiles() {
        tardisCSV.loadCSV();
        roomCSV.loadCSV();
        quotesfile = tardisCSV.copy(getDataFolder() + File.separator + TARDISConstants.QUOTES_FILE_NAME, getResource(TARDISConstants.QUOTES_FILE_NAME));
    }

    /**
     * Saves the default book files to the /plugins/TARDIS/books directory.
     */
    private void loadBooks() {
        // copy book files
        File bookDir = new File(plugin.getDataFolder() + File.separator + "books");
        if (!bookDir.exists()) {
            boolean result = bookDir.mkdir();
            if (result) {
                bookDir.setWritable(true);
                bookDir.setExecutable(true);
                console.sendMessage(pluginName + "Created books directory.");
            }
        }
        Set<String> booknames = achivement_config.getKeys(false);
        for (String b : booknames) {
            tardisCSV.copy(getDataFolder() + File.separator + "books" + File.separator + b + ".txt", getResource(b + ".txt"));
        }
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
        if (plugin.pm.getPlugin("Spout") != null && getConfig().getBoolean("sfx")) {
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
        if (pm.getPlugin("WorldGuard") != null) {
            worldGuardOnServer = true;
            wgchk = new TARDISWorldGuardUtils(this);
        }
    }

    /**
     * Checks if the Towny plugin is available, and loads support if it is.
     */
    private void loadTowny() {
        if (pm.getPlugin("Towny") != null) {
            townyOnServer = true;
            tychk = new TARDISTownyChecker(this);
        }
    }

    /**
     * Checks if the WorldBorder plugin is available, and loads support if it
     * is.
     */
    private void loadWorldBorder() {
        if (pm.getPlugin("WorldBorder") != null) {
            borderOnServer = true;
            borderchk = new TARDISWorldBorderChecker(this);
        }
    }

    /**
     * Checks if the Factions plugin is available, and loads support if it is.
     */
    private void loadFactions() {
        if (pm.getPlugin("Factions") != null) {
            factionsOnServer = true;
            factionschk = new TARDISFactionsChecker(this);
        }
    }

    /**
     * Loads the permissions handler for TARDIS worlds if the relevant
     * permissions plugin is enabled. Currently supports GroupManager and
     * bPermissions (as they have per world config files).
     */
    private void loadPerms() {
        if (pm.getPlugin("GroupManager") != null || pm.getPlugin("bPermissions") != null || pm.getPlugin("PermissionsEx") != null) {
            // copy default permissions file if not present
            tardisCSV.copy(getDataFolder() + File.separator + "permissions.txt", getResource("permissions.txt"));
            if (getConfig().getBoolean("create_worlds")) {
                console.sendMessage(pluginName + "World specific permissions plugin detected please edit plugins/TARDIS/permissions.txt");
            }
        }
    }

    /**
     * Loads the quotes from a text file.
     *
     * @return an ArrayList of quotes
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
                        debug("Error closing quotes reader! " + e.getMessage());
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
        Set<String> rooms = getRoomsConfig().getConfigurationSection("rooms").getKeys(false);
        for (String s : rooms) {
            if (!getRoomsConfig().contains("rooms." + s + ".user")) {
                // set user supplied rooms as `user: true`
                getRoomsConfig().set("rooms." + s + ".user", true);
            }
            if (getRoomsConfig().getBoolean("rooms." + s + ".enabled")) {
                Material m = Material.valueOf(getRoomsConfig().getString("rooms." + s + ".seed"));
                map.put(m, s);
            }
        }
        plugin.saveConfig();
        return map;
    }

    private void checkTCG() {
        if (getConfig().getBoolean("create_worlds")) {
            if (getConfig().getBoolean("default_world")) {
                getConfig().set("default_world", false);
                saveConfig();
                console.sendMessage(pluginName + ChatColor.RED + "default_world was disabled as create_worlds is true!");
            }
            if (pm.getPlugin("TARDISChunkGenerator") == null || (pm.getPlugin("Multiverse-Core") == null && pm.getPlugin("MultiWorld") == null && pm.getPlugin("My Worlds") == null)) {
                getConfig().set("create_worlds", false);
                saveConfig();
                console.sendMessage(pluginName + ChatColor.RED + "Create Worlds was disabled as it requires a multi-world plugin and TARDISChunkGenerator!");
            }
        }
    }

    private boolean getNPCManager() {
        if (pm.getPlugin("Citizens") != null && pm.getPlugin("Citizens").isEnabled() && getConfig().getBoolean("emergency_npc")) {
            debug("Enabling Emergency Program One!");
            return true;
        } else {
            // set emergency_npc false as Citizens not found
            getConfig().set("emergency_npc", false);
            saveConfig();
            debug("Emergency Program One was disabled as it requires the Citizens plugin!");
            return false;
        }
    }

    /**
     * Gets the server default texture pack. Will use the Minecraft default pack
     * if none is specified. Until Minecraft/Bukkit lets us set the TP back to
     * Default, we'll have to host it on DropBox.
     */
    public String getServerTP() {
        String link = "https://dl.dropboxusercontent.com/u/53758864/Minecraft_Default.zip";
        FileInputStream in = null;
        try {
            Properties properties = new Properties();
            String path = "server.properties";
            in = new FileInputStream(path);
            properties.load(in);
            String texture_pack = properties.getProperty("texture-pack");
            return (texture_pack != null && texture_pack.isEmpty()) ? link : texture_pack;
        } catch (FileNotFoundException ex) {
            debug("Could not find server.properties!");
            return link;
        } catch (IOException ex) {
            debug("Could not read server.properties!");
            return link;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                debug("Could not close server.properties!");
            }
        }
    }

    /**
     * Resets any player who is 'Temporally Located' back to normal time.
     */
    private void resetTime() {
        for (String key : trackSetTime.keySet()) {
            Player p = this.getServer().getPlayer(key);
            if (p != null) {
                p.resetPlayerTime();
            }
        }
    }

    private void checkMaps() {
        // get server's main world folder
        // is there a worlds container?
        File container = getServer().getWorldContainer();
        String s_world = getServer().getWorlds().get(0).getName();
        String server_world = s_world + File.separator + "data" + File.separator;
        String map = "map_1963.dat";
        String root = container.getAbsolutePath() + File.separator + server_world;
        File file = new File(root, map);
        if (!file.exists()) {
            String map2 = "map_1964.dat";
            String map3 = "map_1965.dat";
            console.sendMessage(pluginName + ChatColor.RED + "Could not find TARDIS map files, some recipes will not work!");
            console.sendMessage(pluginName + "Copying map files to the TARDIS folder...");
            TARDISMakeTardisCSV copier = new TARDISMakeTardisCSV(plugin);
            copier.copy(getDataFolder() + File.separator + map, getResource(map));
            copier.copy(getDataFolder() + File.separator + map2, getResource(map2));
            copier.copy(getDataFolder() + File.separator + map3, getResource(map3));
            console.sendMessage(pluginName + "Please move the map files to the main world [" + s_world + "] data folder.");
            getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    Set<OfflinePlayer> ops = getServer().getOperators();
                    for (OfflinePlayer olp : ops) {
                        if (olp.isOnline()) {
                            Player p = (Player) olp;
                            p.sendMessage(pluginName + ChatColor.RED + "Could not find TARDIS map files, some recipes will not work!");
                        }
                    }
                }
            }, 200L);
        }
    }

    /**
     * Outputs a message to the console. Requires debug: true in config.yml
     *
     * @param o the Object to print to the console
     */
    public void debug(Object o) {
        if (getConfig().getBoolean("debug") == true) {
            console.sendMessage(pluginName + "Debug: " + o);
        }
    }

    public FileConfiguration getAchivementConfig() {
        return achivement_config;
    }

    public FileConfiguration getArtronConfig() {
        return artron_config;
    }

    public FileConfiguration getBlocksConfig() {
        return blocks_config;
    }

    public FileConfiguration getRoomsConfig() {
        return rooms_config;
    }
}
