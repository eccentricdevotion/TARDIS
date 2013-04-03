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
import me.eccentric_nz.TARDIS.commands.TARDISBindCommands;
import me.eccentric_nz.TARDIS.destroyers.TARDISDestroyerInner;
import me.eccentric_nz.TARDIS.destroyers.TARDISDestroyerPoliceBox;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderInner;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderPoliceBox;
import me.eccentric_nz.TARDIS.listeners.TARDISFireListener;
import me.eccentric_nz.TARDIS.listeners.TARDISExplosionListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockPlaceListener;
import me.eccentric_nz.TARDIS.listeners.TARDISEntityGriefListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockBreakListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockDamageListener;
import me.eccentric_nz.TARDIS.listeners.TARDISDoorListener;
import me.eccentric_nz.TARDIS.listeners.TARDISScannerListener;
import me.eccentric_nz.TARDIS.listeners.TARDISTimeLordDeathListener;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import me.eccentric_nz.TARDIS.builders.TARDISSpace;
import me.eccentric_nz.TARDIS.commands.TARDISBookCommands;
import me.eccentric_nz.TARDIS.commands.TARDISGravityCommands;
import me.eccentric_nz.TARDIS.commands.TARDISRoomCommands;
import me.eccentric_nz.TARDIS.files.TARDISBlockLoader;
import me.eccentric_nz.TARDIS.files.TARDISMakeRoomCSV;
import me.eccentric_nz.TARDIS.files.TARDISMakeTardisCSV;
import me.eccentric_nz.TARDIS.files.TARDISUpdateChecker;
import me.eccentric_nz.TARDIS.listeners.TARDISAreaListener;
import me.eccentric_nz.TARDIS.listeners.TARDISArtronCapacitorListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBindListener;
import me.eccentric_nz.TARDIS.listeners.TARDISButtonListener;
import me.eccentric_nz.TARDIS.listeners.TARDISCondenserListener;
import me.eccentric_nz.TARDIS.listeners.TARDISCreeperDeathListener;
import me.eccentric_nz.TARDIS.listeners.TARDISGravityWellListener;
import me.eccentric_nz.TARDIS.listeners.TARDISHandbrakeListener;
import me.eccentric_nz.TARDIS.listeners.TARDISIceMeltListener;
import me.eccentric_nz.TARDIS.listeners.TARDISJettisonSeeder;
import me.eccentric_nz.TARDIS.listeners.TARDISLightningListener;
import me.eccentric_nz.TARDIS.listeners.TARDISChunkListener;
import me.eccentric_nz.TARDIS.listeners.TARDISRoomSeeder;
import me.eccentric_nz.TARDIS.listeners.TARDISSignListener;
import me.eccentric_nz.TARDIS.listeners.TARDISUpdateListener;
import me.eccentric_nz.TARDIS.rooms.TARDISCondenserData;
import me.eccentric_nz.TARDIS.travel.TARDISUpdateTravellerCount;
import me.eccentric_nz.TARDIS.utility.TARDISCreeperChecker;
import me.eccentric_nz.TARDIS.utility.TARDISFactionsChecker;
import me.eccentric_nz.TARDIS.utility.TARDISTownyChecker;
import me.eccentric_nz.TARDIS.utility.TARDISWorldBorderChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
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
    public TARDISMakeTardisCSV tardisCSV = new TARDISMakeTardisCSV(this);
    public TARDISMakeRoomCSV roomCSV = new TARDISMakeRoomCSV(this);
    public PluginDescriptionFile pdfFile;
    public File budgetSchematicFile = null;
    public File biggerSchematicFile = null;
    public File deluxeSchematicFile = null;
    public File eleventhSchematicFile = null;
    public File redstoneSchematicFile = null;
    public File budgetSchematicCSV = null;
    public File biggerSchematicCSV = null;
    public File deluxeSchematicCSV = null;
    public File eleventhSchematicCSV = null;
    public File redstoneSchematicCSV = null;
    public File quotesfile = null;
    public String[][][] budgetschematic;
    public String[][][] biggerschematic;
    public String[][][] deluxeschematic;
    public String[][][] eleventhschematic;
    public String[][][] redstoneschematic;
    public HashMap<String, String[][][]> room_schematics = new HashMap<String, String[][][]>();
    public short[] budgetdimensions = new short[3];
    public short[] biggerdimensions = new short[3];
    public short[] deluxedimensions = new short[3];
    public short[] eleventhdimensions = new short[3];
    public short[] redstonedimensions = new short[3];
    public HashMap<String, short[]> room_dimensions = new HashMap<String, short[]>();
    public static TARDIS plugin;
    public TARDISUtils utils = new TARDISUtils(this);
    public TARDISCommands tardisCommand;
    private TARDISAdminCommands tardisAdminCommand;
    private TARDISPrefsCommands tardisPrefsCommand;
    private TARDISTravelCommands tardisTravelCommand;
    private TARDISAreaCommands tardisAreaCommand;
    private TARDISBindCommands tardisBindCommand;
    private TARDISRoomCommands tardisRoomCommand;
    private TARDISBookCommands tardisBookCommand;
    private TARDISGravityCommands tardisGravityCommand;
    public TARDISBuilderInner buildI = new TARDISBuilderInner(this);
    public TARDISBuilderPoliceBox buildPB = new TARDISBuilderPoliceBox(this);
    public TARDISDestroyerInner destroyI = new TARDISDestroyerInner(this);
    public TARDISDestroyerPoliceBox destroyPB = new TARDISDestroyerPoliceBox(this);
    public TARDISArea ta = new TARDISArea(this);
    public TARDISWorldGuardChecker wgchk;
    public TARDISTownyChecker tychk;
    public TARDISWorldBorderChecker borderchk;
    public TARDISFactionsChecker factionschk;
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
    TARDISEntityGriefListener entityListener = new TARDISEntityGriefListener(this);
    TARDISLightningListener lightningListener = new TARDISLightningListener(this);
    TARDISCreeperDeathListener creeperListener = new TARDISCreeperDeathListener(this);
    TARDISArtronCapacitorListener energyListener = new TARDISArtronCapacitorListener(this);
    TARDISRoomSeeder seedListener = new TARDISRoomSeeder(this);
    TARDISJettisonSeeder jettisonListener = new TARDISJettisonSeeder(this);
    TARDISBindListener bindListener = new TARDISBindListener(this);
    TARDISHandbrakeListener handbrakeListener = new TARDISHandbrakeListener(this);
    TARDISGravityWellListener gravityListener = new TARDISGravityWellListener(this);
    TARDISCondenserListener condenserListener = new TARDISCondenserListener(this);
    TARDISIceMeltListener meltListener = new TARDISIceMeltListener(this);
    TARDISChunkListener roomChunkListener = new TARDISChunkListener(this);
    TARDISScannerListener scannerListener = new TARDISScannerListener(this);
    TARDISTimeLordDeathListener deathListener = new TARDISTimeLordDeathListener(this);
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
    public HashMap<String, Double[]> trackGravity = new HashMap<String, Double[]>();
    public HashMap<Integer, Integer> trackTravellers = new HashMap<Integer, Integer>();
    public HashMap<Integer, Integer> tardisHasDestination = new HashMap<Integer, Integer>();
    public HashMap<String, Block> trackExterminate = new HashMap<String, Block>();
    public ArrayList<Integer> tardisHasTravelled = new ArrayList<Integer>();
    public ArrayList<Integer> tardisMaterialising = new ArrayList<Integer>();
    public List<Chunk> tardisChunkList = new ArrayList<Chunk>();
    public List<Chunk> roomChunkList = new ArrayList<Chunk>();
    public HashMap<String, Double[]> gravityUpList = new HashMap<String, Double[]>();
    public List<String> gravityDownList = new ArrayList<String>();
    public HashMap<String, Double[]> gravityNorthList = new HashMap<String, Double[]>();
    public HashMap<String, Double[]> gravityWestList = new HashMap<String, Double[]>();
    public HashMap<String, Double[]> gravitySouthList = new HashMap<String, Double[]>();
    public HashMap<String, Double[]> gravityEastList = new HashMap<String, Double[]>();
    public HashMap<String, Integer> protectBlockMap = new HashMap<String, Integer>();
    public HashMap<String, TARDISCondenserData> roomCondenserData = new HashMap<String, TARDISCondenserData>();
    private static ArrayList<String> quotes = new ArrayList<String>();
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

    @Override
    public void onEnable() {
        pdfFile = getDescription();
        pluginName = ChatColor.GOLD + "[" + pdfFile.getName() + "]" + ChatColor.RESET + " ";
        plugin = this;
        console = getServer().getConsoleSender();

        if (loadClasses()) {
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
            loadFactions();

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
            if (pm.isPluginEnabled("TARDISChunkGenerator")) {
                TARDISSpace alwaysNight = new TARDISSpace(this);
                if (getConfig().getBoolean("keep_night")) {
                    alwaysNight.keepNight();
                }
            }
            loadChunks();
            TARDISBlockLoader bl = new TARDISBlockLoader(this);
            bl.loadProtectBlocks();
            bl.loadGravityWells();
            loadPerms();
        }
    }

    @Override
    public void onDisable() {
        // save chunks to file so we can reload them onenable next server startup
        saveChunks();
        saveConfig();
        closeDatabase();
    }

    /**
     * Used to load net.minecraft.server methods for various versions of
     * CraftBukkit.
     */
    private boolean loadClasses() {
        boolean found;
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
            found = true;
        } catch (final Exception e) {
            this.getLogger().severe("Could not load support for this CraftBukkit version.");
            this.getLogger().info("Check for updates at http://dev.bukkit.org/server-mods/tardis/");
            this.setEnabled(false);
            found = false;
        }
        console.sendMessage(pluginName + "Loading support for CB " + version);
        return found;
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
        pm.registerEvents(entityListener, this);
        pm.registerEvents(lightningListener, this);
        pm.registerEvents(creeperListener, this);
        pm.registerEvents(energyListener, this);
        pm.registerEvents(seedListener, this);
        pm.registerEvents(jettisonListener, this);
        pm.registerEvents(bindListener, this);
        pm.registerEvents(handbrakeListener, this);
        pm.registerEvents(gravityListener, this);
        pm.registerEvents(condenserListener, this);
        pm.registerEvents(meltListener, this);
        pm.registerEvents(roomChunkListener, this);
        pm.registerEvents(scannerListener, this);
        pm.registerEvents(deathListener, this);
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
        tardisBindCommand = new TARDISBindCommands(this);
        tardisGravityCommand = new TARDISGravityCommands(this);
        tardisRoomCommand = new TARDISRoomCommands(this);
        tardisBookCommand = new TARDISBookCommands(this);
        getCommand("tardis").setExecutor(tardisCommand);
        getCommand("tardisadmin").setExecutor(tardisAdminCommand);
        getCommand("tardisprefs").setExecutor(tardisPrefsCommand);
        getCommand("tardistravel").setExecutor(tardisTravelCommand);
        getCommand("tardisarea").setExecutor(tardisAreaCommand);
        getCommand("tardisbind").setExecutor(tardisBindCommand);
        getCommand("tardisgravity").setExecutor(tardisGravityCommand);
        getCommand("tardisroom").setExecutor(tardisRoomCommand);
        getCommand("tardisbook").setExecutor(tardisBookCommand);
    }

    /**
     * Builds the schematics used to create TARDISes and rooms. Also loads the
     * quotes from the quotes file.
     */
    private void loadFiles() {
        tardisCSV.loadCSV();
        roomCSV.loadCSV();
        quotesfile = tardisCSV.copy(getDataFolder() + File.separator + TARDISConstants.QUOTES_FILE_NAME, getResource(TARDISConstants.QUOTES_FILE_NAME));
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
        tardisCSV.copy(getDataFolder() + File.separator + "books" + File.separator + "lore.txt", getResource("lore.txt"));
        tardisCSV.copy(getDataFolder() + "achievements.yml", getResource("achievements.yml"));
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
            wgchk = new TARDISWorldGuardChecker(this);
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
        if (pm.getPlugin("GroupManager") != null || pm.getPlugin("bPermissions") != null) {
            // copy default permissions file if not present
            tardisCSV.copy(getDataFolder() + File.separator + "permissions.txt", getResource("permissions.txt"));
            console.sendMessage(pluginName + "World specific permissions plugin detected please edit plugins/TARDIS/permissions.txt");
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
        Set<String> rooms = getConfig().getConfigurationSection("rooms").getKeys(false);
        for (String s : rooms) {
            Material m = Material.valueOf(getConfig().getString("rooms." + s + ".seed"));
            map.put(m, s);
        }
        return map;
    }

    private void checkTCG() {
        if (getConfig().getBoolean("create_worlds")) {
            if (pm.getPlugin("TARDISChunkGenerator") == null || (pm.getPlugin("Multiverse-Core") == null && pm.getPlugin("MultiWorld") == null)) {
                getConfig().set("create_worlds", false);
                saveConfig();
                console.sendMessage(pluginName + ChatColor.RED + "Create Worlds was disabled as it requires a multi-world plugin and TARDISChunkGenerator!");
            }
        }
    }

    /**
     * Saves the list of chunks that are being stopped from unloading. Chunk
     * locations are either rooms being grown or Police Box locations. The file
     * is read onEnable() and the tardisChunkList is re-populated.
     */
    public void saveChunks() {
        debug("Saving Police Box chunks to file!");
        if (tardisChunkList.size() > 0) {
            String file = getDataFolder() + File.separator + "chunks.txt";
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
                for (Chunk c : tardisChunkList) {
                    String line = c.getWorld().getName() + ":" + c.getX() + ":" + c.getZ();
                    bw.write(line);
                    bw.newLine();
                }
                bw.close();
            } catch (IOException e) {
                debug("Could not create and write to chunks.txt! " + e.getMessage());
            }
        }
    }

    /**
     * Loads chunks from file to stop them from being unloaded. Chunk locations
     * are either rooms being grown or Police Box locations.
     */
    public void loadChunks() {
        File file = new File(getDataFolder() + File.separator + "chunks.txt");
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] split = line.split(":");
                    World w = getServer().getWorld(split[0]);
                    int x = 0, z = 0;
                    try {
                        x = Integer.parseInt(split[1]);
                        z = Integer.parseInt(split[2]);
                    } catch (NumberFormatException nfe) {
                    }
                    Chunk c = w.getChunkAt(x, z);
                    tardisChunkList.add(c);
                    c.load();
                }
                br.close();
                debug("Loading chunks from chunks.txt!");
            } catch (IOException e) {
                debug("Could not create and write to chunks.txt! " + e.getMessage());
            }
        }
    }

    /**
     * Outputs a message to the console. Requires debug: true in config.yml
     */
    public void debug(Object o) {
        if (getConfig().getBoolean("debug") == true) {
            console.sendMessage(pluginName + "Debug: " + o);
        }
    }
}
