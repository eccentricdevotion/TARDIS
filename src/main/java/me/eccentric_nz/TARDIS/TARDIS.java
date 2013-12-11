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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import me.eccentric_nz.TARDIS.ARS.TARDISARSListener;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderInner;
import me.eccentric_nz.TARDIS.builders.TARDISPresetBuilderFactory;
import me.eccentric_nz.TARDIS.builders.TARDISSpace;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonListener;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonPreset;
import me.eccentric_nz.TARDIS.chameleon.TARDISPresetListener;
import me.eccentric_nz.TARDIS.chameleon.TARDISStainedGlassLookup;
import me.eccentric_nz.TARDIS.commands.admin.TARDISAdminCommands;
import me.eccentric_nz.TARDIS.commands.TARDISAreaCommands;
import me.eccentric_nz.TARDIS.commands.TARDISAreaTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISBindCommands;
import me.eccentric_nz.TARDIS.commands.TARDISBindTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISBookCommands;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISCommands;
import me.eccentric_nz.TARDIS.commands.TARDISGravityCommands;
import me.eccentric_nz.TARDIS.commands.TARDISGravityTabComplete;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsCommands;
import me.eccentric_nz.TARDIS.commands.TARDISRecipeCommands;
import me.eccentric_nz.TARDIS.commands.TARDISRecipeTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISRoomCommands;
import me.eccentric_nz.TARDIS.commands.TARDISTextureCommands;
import me.eccentric_nz.TARDIS.commands.TARDISTextureTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISTravelCommands;
import me.eccentric_nz.TARDIS.commands.TARDISTravelTabComplete;
import me.eccentric_nz.TARDIS.commands.admin.TARDISAdminTabComplete;
import me.eccentric_nz.TARDIS.commands.admin.TARDISGiveCommand;
import me.eccentric_nz.TARDIS.commands.admin.TARDISGiveTabComplete;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsTabComplete;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISTabComplete;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.TARDISControlsConverter;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.TARDISLocationsConverter;
import me.eccentric_nz.TARDIS.database.TARDISMySQLDatabase;
import me.eccentric_nz.TARDIS.database.TARDISSQLiteDatabase;
import me.eccentric_nz.TARDIS.destroyers.TARDISDestroyerInner;
import me.eccentric_nz.TARDIS.destroyers.TARDISPresetDestroyerFactory;
import me.eccentric_nz.TARDIS.files.TARDISBlockLoader;
import me.eccentric_nz.TARDIS.files.TARDISConfiguration;
import me.eccentric_nz.TARDIS.files.TARDISMakeRoomCSV;
import me.eccentric_nz.TARDIS.files.TARDISMakeTardisCSV;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import me.eccentric_nz.TARDIS.info.TARDISInformationSystemListener;
import me.eccentric_nz.TARDIS.listeners.TARDISAdminMenuListener;
import me.eccentric_nz.TARDIS.listeners.TARDISAnvilListener;
import me.eccentric_nz.TARDIS.listeners.TARDISAreaListener;
import me.eccentric_nz.TARDIS.listeners.TARDISAreaSignListener;
import me.eccentric_nz.TARDIS.listeners.TARDISArtronCapacitorListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBindListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockBreakListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockDamageListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockPlaceListener;
import me.eccentric_nz.TARDIS.listeners.TARDISButtonListener;
import me.eccentric_nz.TARDIS.listeners.TARDISChatListener;
import me.eccentric_nz.TARDIS.listeners.TARDISChunkListener;
import me.eccentric_nz.TARDIS.listeners.TARDISCondenserListener;
import me.eccentric_nz.TARDIS.listeners.TARDISCraftListener;
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
import me.eccentric_nz.TARDIS.listeners.TARDISJettisonSeeder;
import me.eccentric_nz.TARDIS.listeners.TARDISJoinListener;
import me.eccentric_nz.TARDIS.listeners.TARDISKeyboardListener;
import me.eccentric_nz.TARDIS.listeners.TARDISLightningListener;
import me.eccentric_nz.TARDIS.listeners.TARDISMinecartListener;
import me.eccentric_nz.TARDIS.listeners.TARDISNPCListener;
import me.eccentric_nz.TARDIS.listeners.TARDISQuitListener;
import me.eccentric_nz.TARDIS.listeners.TARDISRecipeListener;
import me.eccentric_nz.TARDIS.listeners.TARDISRoomSeeder;
import me.eccentric_nz.TARDIS.listeners.TARDISSaveSignListener;
import me.eccentric_nz.TARDIS.listeners.TARDISScannerListener;
import me.eccentric_nz.TARDIS.listeners.TARDISSeedBlockListener;
import me.eccentric_nz.TARDIS.listeners.TARDISSignListener;
import me.eccentric_nz.TARDIS.listeners.TARDISStattenheimListener;
import me.eccentric_nz.TARDIS.listeners.TARDISTeleportListener;
import me.eccentric_nz.TARDIS.listeners.TARDISTemporalLocatorListener;
import me.eccentric_nz.TARDIS.listeners.TARDISTerminalListener;
import me.eccentric_nz.TARDIS.listeners.TARDISTimeLordDeathListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockPhysicsListener;
import me.eccentric_nz.TARDIS.listeners.TARDISMakePresetListener;
import me.eccentric_nz.TARDIS.listeners.TARDISPistonListener;
import me.eccentric_nz.TARDIS.listeners.TARDISTagListener;
import me.eccentric_nz.TARDIS.listeners.TARDISUpdateListener;
import me.eccentric_nz.TARDIS.listeners.TARDISWorldResetListener;
import me.eccentric_nz.TARDIS.rooms.TARDISCondenserData;
import me.eccentric_nz.TARDIS.rooms.TARDISSeedData;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import me.eccentric_nz.TARDIS.utility.Version;
import me.eccentric_nz.TARDIS.travel.TARDISArea;
import me.eccentric_nz.TARDIS.utility.TARDISCreeperChecker;
import me.eccentric_nz.TARDIS.utility.TARDISFactionsChecker;
import me.eccentric_nz.TARDIS.recipes.TARDISShapedRecipe;
import me.eccentric_nz.TARDIS.recipes.TARDISShapelessRecipe;
//import me.eccentric_nz.TARDIS.recipes.TARDISFurnaceRecipe;
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
import org.bukkit.block.Sign;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
    private static final ArrayList<String> quotes = new ArrayList<String>();
    TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
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
    public TARDISBuilderInner builderI = new TARDISBuilderInner(this);
    public TARDISPresetBuilderFactory builderP = new TARDISPresetBuilderFactory(this);
    public TARDISDestroyerInner destroyerI = new TARDISDestroyerInner(this);
    public TARDISPresetDestroyerFactory destroyerP = new TARDISPresetDestroyerFactory(this);
    public TARDISStainedGlassLookup lookup = new TARDISStainedGlassLookup();
    public TARDISArea ta = new TARDISArea(this);
    public TARDISWorldGuardUtils wgutils;
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
    public HashMap<String, String> trackPreset = new HashMap<String, String>();
    public HashMap<String, TARDISSeedData> trackRoomSeed = new HashMap<String, TARDISSeedData>();
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
    public List<Block> pistons = new ArrayList<Block>();
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
    public FileConfiguration achievementConfig;
    private FileConfiguration artronConfig;
    private FileConfiguration blocksConfig;
    private FileConfiguration roomsConfig;
    private FileConfiguration tagConfig;
    private FileConfiguration recipesConfig;
    public TARDISButtonListener buttonListener;
    public TARDISDoorListener doorListener;
    public TARDISChameleonPreset presets;
    public TARDISMultiverseInventoriesChecker tmic;
    public TARDISWalls tw;
    private Calendar beforecal;
    private Calendar aftercal;
    private boolean hasVersion = false;
    public TARDISShapedRecipe figura;
    public TARDISShapelessRecipe incomposita;
    //public TARDISFurnaceRecipe fornacis;

    @Override
    public void onEnable() {
        pdfFile = getDescription();
        pluginName = ChatColor.GOLD + "[" + pdfFile.getName() + "]" + ChatColor.RESET + " ";
        plugin = this;
        console = getServer().getConsoleSender();

        String[] v = Bukkit.getServer().getBukkitVersion().split("-");
        Version bukkitversion = (!v[0].equalsIgnoreCase("unknown")) ? new Version(v[0]) : new Version("1.6.4");
        Version minversion = new Version("1.7.2");
        // check CraftBukkit version
        if (bukkitversion.compareTo(minversion) >= 0) {
            hasVersion = true;
            saveDefaultConfig();
            loadCustomConfigs();
            TARDISConfiguration tc = new TARDISConfiguration(this);
            tc.checkConfig();
            checkTCG();
            checkDefaultWorld();
            seeds = getSeeds();
            tw = new TARDISWalls();
            loadDatabase();
            loadFiles();
            registerListeners();
            loadCommands();
            startSound();
            loadWorldGuard();
            loadTowny();
            loadWorldBorder();
            loadFactions();

            quote = quotes();
            quotelen = quote.size();

            TARDISCreeperChecker cc = new TARDISCreeperChecker(this);
            cc.startCreeperCheck();
            if (pm.isPluginEnabled("TARDISChunkGenerator")) {
                TARDISSpace alwaysNight = new TARDISSpace(this);
                if (getConfig().getBoolean("keep_night")) {
                    alwaysNight.keepNight();
                }
            }
            TARDISBlockLoader bl = new TARDISBlockLoader(this);
            bl.loadProtectBlocks();
            bl.loadGravityWells();
            loadPerms();
            loadBooks();
            if (!getConfig().getBoolean("conversion_done")) {
                new TARDISControlsConverter(this).convertControls();
            }
            if (!getConfig().getBoolean("location_conversion_done")) {
                new TARDISLocationsConverter(this).convert();
            }
            tp = getServerTP();
            // copy maps
            checkMaps();
            // register recipes
            figura = new TARDISShapedRecipe(this);
            figura.addShapedRecipes();
            incomposita = new TARDISShapelessRecipe(this);
            incomposita.addShapelessRecipes();

            presets = new TARDISChameleonPreset();
            presets.makePresets();
            if (pm.isPluginEnabled("Multiverse-Inventories")) {
                tmic = new TARDISMultiverseInventoriesChecker(this);
            }
            setDates();
        } else {
            console.sendMessage(pluginName + "This plugin requires CraftBukkit 1.7.2 or higher, disabling...");
            pm.disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (hasVersion) {
            updateTagStats();
            saveConfig();
            closeDatabase();
            resetTime();
        }
    }

    /**
     * Sets up the database.
     */
    private void loadDatabase() {
        String dbtype = getConfig().getString("database");
        try {
            if (dbtype.equals("sqlite")) {
                String path = getDataFolder() + File.separator + "TARDIS.db";
                service.setConnection(path);
                TARDISSQLiteDatabase sqlite = new TARDISSQLiteDatabase(this);
                sqlite.createTables();
            } else {
                service.setConnection();
                TARDISMySQLDatabase mysql = new TARDISMySQLDatabase(this);
                mysql.createTables();
            }
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
        } catch (SQLException e) {
            console.sendMessage(pluginName + "Could not close database connection: " + e);
        }
    }

    /**
     * Loads the custom config files.
     */
    private void loadCustomConfigs() {
        //TODO - change file copy method - just send the file name and process it there?
        tardisCSV.copy(getDataFolder() + File.separator + "achievements.yml", getResource("achievements.yml"));
        tardisCSV.copy(getDataFolder() + File.separator + "artron.yml", getResource("artron.yml"));
        tardisCSV.copy(getDataFolder() + File.separator + "blocks.yml", getResource("blocks.yml"));
        tardisCSV.copy(getDataFolder() + File.separator + "rooms.yml", getResource("rooms.yml"));
        tardisCSV.copy(getDataFolder() + File.separator + "tag.yml", getResource("tag.yml"));
        tardisCSV.copy(getDataFolder() + File.separator + "recipes.yml", getResource("recipes.yml"));
        this.achievementConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "achievements.yml"));
        if (this.achievementConfig.getString("travel.message").equals("Life of the party!")) {
            this.achievementConfig.set("travel.message", "There and back again!");
            try {
                this.achievementConfig.save(getDataFolder() + File.separator + "achievements.yml");
            } catch (IOException io) {
                debug("Could not save achievements.yml " + io);
            }
        }
        this.artronConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "artron.yml"));
        this.blocksConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "blocks.yml"));
        this.roomsConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "rooms.yml"));
        this.tagConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "tag.yml"));
        this.recipesConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "recipes.yml"));
    }

    /**
     * Registers all the listeners for the various events required to use the
     * TARDIS.
     */
    private void registerListeners() {
        if (getConfig().getBoolean("use_block_stack")) {
            pm.registerEvents(new TARDISBlockPlaceListener(this), this);
        }
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
        pm.registerEvents(new TARDISQuitListener(this), this);
        pm.registerEvents(new TARDISKeyboardListener(this), this);
        pm.registerEvents(new TARDISBlockPhysicsListener(this), this);
        pm.registerEvents(new TARDISTagListener(this), this);
        pm.registerEvents(new TARDISMakePresetListener(this), this);
        pm.registerEvents(new TARDISPistonListener(this), this);
        pm.registerEvents(new TARDISTerminalListener(this), this);
        pm.registerEvents(new TARDISChameleonListener(this), this);
        pm.registerEvents(new TARDISPresetListener(this), this);
        pm.registerEvents(new TARDISARSListener(this), this);
        pm.registerEvents(new TARDISSaveSignListener(this), this);
        pm.registerEvents(new TARDISAreaSignListener(this), this);
        pm.registerEvents(new TARDISStattenheimListener(this), this);
        pm.registerEvents(new TARDISHotbarListener(this), this);
        pm.registerEvents(new TARDISAdminMenuListener(this), this);
        pm.registerEvents(new TARDISTemporalLocatorListener(this), this);
        pm.registerEvents(new TARDISRecipeListener(this), this);
        pm.registerEvents(new TARDISSeedBlockListener(this), this);
        pm.registerEvents(new TARDISCraftListener(this), this);
        pm.registerEvents(new TARDISChatListener(this), this);
        pm.registerEvents(new TARDISMinecartListener(this), this);
        pm.registerEvents(new TARDISHorseListener(this), this);
        pm.registerEvents(new TARDISTeleportListener(this), this);
        pm.registerEvents(new TARDISAnvilListener(this), this);
        pm.registerEvents(new TARDISInformationSystemListener(this), this);
        if (getNPCManager()) {
            pm.registerEvents(new TARDISNPCListener(this), this);
        }
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
        getCommand("tardis").setTabCompleter(new TARDISTabComplete(this));
        tardisAdminCommand = new TARDISAdminCommands(this);
        getCommand("tardisadmin").setExecutor(tardisAdminCommand);
        getCommand("tardisadmin").setTabCompleter(new TARDISAdminTabComplete(this));
        getCommand("tardisarea").setExecutor(new TARDISAreaCommands(this));
        getCommand("tardisarea").setTabCompleter(new TARDISAreaTabComplete());
        getCommand("tardisbind").setExecutor(new TARDISBindCommands(this));
        getCommand("tardisbind").setTabCompleter(new TARDISBindTabComplete());
        getCommand("tardisbook").setExecutor(new TARDISBookCommands(this));
        getCommand("tardisgive").setExecutor(new TARDISGiveCommand(this));
        getCommand("tardisgive").setTabCompleter(new TARDISGiveTabComplete());
        getCommand("tardisgravity").setExecutor(new TARDISGravityCommands(this));
        getCommand("tardisgravity").setTabCompleter(new TARDISGravityTabComplete());
        getCommand("tardisprefs").setExecutor(new TARDISPrefsCommands(this));
        getCommand("tardisprefs").setTabCompleter(new TARDISPrefsTabComplete());
        getCommand("tardisrecipe").setExecutor(new TARDISRecipeCommands(this));
        getCommand("tardisrecipe").setTabCompleter(new TARDISRecipeTabComplete());
        getCommand("tardisroom").setExecutor(new TARDISRoomCommands(this));
        getCommand("tardistexture").setExecutor(new TARDISTextureCommands(this));
        getCommand("tardistexture").setTabCompleter(new TARDISTextureTabComplete());
        getCommand("tardistravel").setExecutor(new TARDISTravelCommands(this));
        getCommand("tardistravel").setTabCompleter(new TARDISTravelTabComplete(this));
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
        File bookDir = new File(getDataFolder() + File.separator + "books");
        if (!bookDir.exists()) {
            boolean result = bookDir.mkdir();
            if (result) {
                bookDir.setWritable(true);
                bookDir.setExecutable(true);
                console.sendMessage(pluginName + "Created books directory.");
            }
        }
        Set<String> booknames = achievementConfig.getKeys(false);
        for (String b : booknames) {
            tardisCSV.copy(getDataFolder() + File.separator + "books" + File.separator + b + ".txt", getResource(b + ".txt"));
        }
    }

    /**
     * Starts a repeating tasks that plays TARDIS sound effects to players while
     * they are inside the TARDIS.
     */
    private void startSound() {
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                TARDISSounds.randomTARDISSound();
            }
        }, 60L, 1500L);
    }

    /**
     * Checks if the WorldGuard plugin is available, and loads support if it is.
     */
    private void loadWorldGuard() {
        if (pm.getPlugin("WorldGuard") != null) {
            worldGuardOnServer = true;
            wgutils = new TARDISWorldGuardUtils(this);
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
            Version version = new Version(getServer().getPluginManager().getPlugin("Factions").getDescription().getVersion());
            Version min_version = new Version("2.0");
            if (version.compareTo(min_version) >= 0) {
                factionsOnServer = true;
                factionschk = new TARDISFactionsChecker(this);
            } else {
                console.sendMessage(pluginName + "This version of TARDIS is not compatible with Factions " + version.toString() + ", please update to Factions 2.0 or higher.");
            }
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
                    } catch (IOException e) {
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
        saveConfig();
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
     * Default, we'll have to host it on DropBo
     *
     * @return The server specified texture pack.
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
            TARDISMakeTardisCSV copier = new TARDISMakeTardisCSV(this);
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

    private void setDates() {
        int month = getTagConfig().getInt("month") - 1;
        int day = getTagConfig().getInt("day");
        beforecal = Calendar.getInstance();
        beforecal.set(Calendar.HOUR, 0);
        beforecal.set(Calendar.MINUTE, 0);
        beforecal.set(Calendar.SECOND, 0);
        beforecal.set(Calendar.MONTH, month);
        beforecal.set(Calendar.DATE, day);
        aftercal = Calendar.getInstance();
        aftercal.set(Calendar.HOUR, 23);
        aftercal.set(Calendar.MINUTE, 59);
        aftercal.set(Calendar.SECOND, 59);
        aftercal.set(Calendar.MONTH, month);
        aftercal.set(Calendar.DATE, day);
        // reset config
        getTagConfig().set("it", "");
    }

    private void updateTagStats() {
        String it = getTagConfig().getString("it");
        if (!it.equals("")) {
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("player", getTagConfig().getString("it"));
            long time = System.currentTimeMillis() - getTagConfig().getLong("time");
            set.put("time", time);
            new QueryFactory(this).doSyncInsert("tag", set);
        }
    }

    private void checkDefaultWorld() {
        if (!getConfig().getBoolean("default_world")) {
            return;
        }
        if (getServer().getWorld(getConfig().getString("default_world_name")) == null) {
            console.sendMessage(pluginName + "Default world specified, but it doesn't exist! Trying to create it now...");
            new TARDISSpace(this).createDefaultWorld(getConfig().getString("default_world_name"));
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
        return achievementConfig;
    }

    public FileConfiguration getArtronConfig() {
        return artronConfig;
    }

    public FileConfiguration getBlocksConfig() {
        return blocksConfig;
    }

    public FileConfiguration getRoomsConfig() {
        return roomsConfig;
    }

    public FileConfiguration getTagConfig() {
        return tagConfig;
    }

    public FileConfiguration getRecipesConfig() {
        return recipesConfig;
    }

    public Calendar getBeforeCal() {
        return beforecal;
    }

    public Calendar getAfterCal() {
        return aftercal;
    }
}
