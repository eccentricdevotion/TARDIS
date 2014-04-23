/*
 * Copyright (C) 2014 eccentric_nz
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.eccentric_nz.TARDIS.api.TARDII;
import me.eccentric_nz.TARDIS.artron.TARDISCondensables;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderInner;
import me.eccentric_nz.TARDIS.builders.TARDISPresetBuilderFactory;
import me.eccentric_nz.TARDIS.builders.TARDISSpace;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonPreset;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.TARDISControlsConverter;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.TARDISLocationsConverter;
import me.eccentric_nz.TARDIS.database.TARDISMySQLDatabase;
import me.eccentric_nz.TARDIS.database.TARDISSQLiteDatabase;
import me.eccentric_nz.TARDIS.database.TARDISUUIDConverter;
import me.eccentric_nz.TARDIS.destroyers.TARDISDestroyerInner;
import me.eccentric_nz.TARDIS.destroyers.TARDISPresetDestroyerFactory;
import me.eccentric_nz.TARDIS.files.TARDISBlockLoader;
import me.eccentric_nz.TARDIS.files.TARDISConfiguration;
import me.eccentric_nz.TARDIS.files.TARDISMakeRoomCSV;
import me.eccentric_nz.TARDIS.files.TARDISMakeTardisCSV;
import me.eccentric_nz.TARDIS.recipes.TARDISShapedRecipe;
import me.eccentric_nz.TARDIS.recipes.TARDISShapelessRecipe;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import me.eccentric_nz.TARDIS.rooms.TARDISZeroRoomRunnable;
import me.eccentric_nz.TARDIS.travel.TARDISArea;
import me.eccentric_nz.TARDIS.travel.TARDISPluginRespect;
import me.eccentric_nz.TARDIS.utility.TARDISCreeperChecker;
import me.eccentric_nz.TARDIS.utility.TARDISMapChecker;
import me.eccentric_nz.TARDIS.utility.TARDISMultiverseInventoriesChecker;
import me.eccentric_nz.TARDIS.utility.TARDISPerceptionFilter;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISUtils;
import me.eccentric_nz.TARDIS.utility.TARDISWorldGuardUtils;
import me.eccentric_nz.TARDIS.utility.Version;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
    TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    //public TARDISFurnaceRecipe fornacis;
    private Calendar afterCal;
    private Calendar beforeCal;
    private ConsoleCommandSender console;
    private File quotesfile = null;
    private FileConfiguration achievementConfig;
    private FileConfiguration artronConfig;
    private FileConfiguration blocksConfig;
    private FileConfiguration condensablesConfig;
    private FileConfiguration kitsConfig;
    private FileConfiguration recipesConfig;
    private FileConfiguration roomsConfig;
    private FileConfiguration tagConfig;
    private HashMap<String, Integer> condensables;
    private PluginDescriptionFile pdfFile;
    private String pluginName;
    private String resourcePack;
    private TARDISChameleonPreset presets;
    private TARDISMultiverseInventoriesChecker TMIChecker;
    private TARDISPerceptionFilter filter;
    private TARDISPluginRespect pluginRespect;
    private TARDISShapedRecipe figura;
    private TARDISShapelessRecipe incomposita;
    private TARDISUtils utils;
    private TARDISWalls tardisWalls;
    private TARDISWorldGuardUtils worldGuardUtils;
    private boolean hasVersion = false;
    private boolean mySpawn = false;
    private boolean worldGuardOnServer;
    private boolean horseSpeedOnServer;
    private boolean projRassilonOnServer;
    private PluginManager pm;
    private final TARDISArea tardisArea = new TARDISArea(this);
    private final TARDISBuilderInner interiorBuilder = new TARDISBuilderInner(this);
    private final TARDISBuilderInstanceKeeper buildKeeper = new TARDISBuilderInstanceKeeper();
    private final TARDISDestroyerInner interiorDestroyer = new TARDISDestroyerInner(this);
    private TARDISGeneralInstanceKeeper generalKeeper;
    private final TARDISMakeRoomCSV roomCSV = new TARDISMakeRoomCSV(this);
    private final TARDISMakeTardisCSV tardisCSV = new TARDISMakeTardisCSV(this);
    private final TARDISPresetBuilderFactory presetBuilder = new TARDISPresetBuilderFactory(this);
    private final TARDISPresetDestroyerFactory presetDestroyer = new TARDISPresetDestroyerFactory(this);
    private final TARDISTrackerInstanceKeeper trackerKeeper = new TARDISTrackerInstanceKeeper();

    public TARDIS() {
        this.worldGuardOnServer = false;
        this.horseSpeedOnServer = false;
        this.projRassilonOnServer = false;
    }

    @Override
    public void onEnable() {
        pm = this.getServer().getPluginManager();
        pdfFile = getDescription();
        pluginName = ChatColor.GOLD + "[" + pdfFile.getName() + "]" + ChatColor.RESET + " ";
        plugin = this;
        console = getServer().getConsoleSender();
        Version bukkitversion = getServerVersion(getServer().getVersion());
        Version minversion = new Version("1.7.9");
        // check CraftBukkit version
        if (bukkitversion.compareTo(minversion) >= 0) {
            hasVersion = true;
            saveDefaultConfig();
            loadCustomConfigs();
            TARDISConfiguration tc = new TARDISConfiguration(this);
            tc.checkConfig();
            loadDatabase();
            // update database add and populate uuid fields
            if (!getConfig().getBoolean("conversions.uuid_conversion_done")) {
                TARDISUUIDConverter uc = new TARDISUUIDConverter(this);
                if (!uc.convert()) {
                    // conversion failed
                    console.sendMessage(pluginName + ChatColor.RED + "UUID conversion failed, disabling...");
                    pm.disablePlugin(this);
                    return;
                } else {
                    getConfig().set("conversions.uuid_conversion_done", true);
                    saveConfig();
                    console.sendMessage(pluginName + "UUID conversion successful :)");
                }
            }
            checkTCG();
            checkDefaultWorld();
            utils = new TARDISUtils(this);
            buildKeeper.setSeeds(getSeeds());
            tardisWalls = new TARDISWalls();
            loadFiles();
            generalKeeper = new TARDISGeneralInstanceKeeper(this);
            generalKeeper.setQuotes(quotes());
            new TARDISListenerRegisterer(this).registerListeners();
            new TARDISCommandSetter(this).loadCommands();
            startSound();
            loadWorldGuard();
            loadPluginRespect();
            loadHorseSpeed();
            loadProjRassilon();
            startZeroHealing();

            new TARDISCreeperChecker(this).startCreeperCheck();
            if (pm.isPluginEnabled("TARDISChunkGenerator")) {
                TARDISSpace alwaysNight = new TARDISSpace(this);
                if (getConfig().getBoolean("creation.keep_night")) {
                    alwaysNight.keepNight();
                }
            }
            TARDISBlockLoader bl = new TARDISBlockLoader(this);
            bl.loadProtectBlocks();
            bl.loadGravityWells();
            if (worldGuardOnServer && getConfig().getBoolean("allow.wg_flag_set")) {
                bl.loadAntiBuild();
            }
            loadPerms();
            loadBooks();
            if (!getConfig().getBoolean("conversions.conversion_done")) {
                new TARDISControlsConverter(this).convertControls();
            }
            if (!getConfig().getBoolean("conversions.location_conversion_done")) {
                new TARDISLocationsConverter(this).convert();
            }
            resourcePack = getServerTP();
            // copy maps
            new TARDISMapChecker(this).checkMaps();
            // register recipes
            figura = new TARDISShapedRecipe(this);
            figura.addShapedRecipes();
            incomposita = new TARDISShapelessRecipe(this);
            incomposita.addShapelessRecipes();

            presets = new TARDISChameleonPreset();
            presets.makePresets();
            if (pm.isPluginEnabled("Multiverse-Inventories")) {
                TMIChecker = new TARDISMultiverseInventoriesChecker(this);
            }
            setDates();
            filter = new TARDISPerceptionFilter(this);
            filter.createPerceptionFilter();
            TARDISCondensables cond = new TARDISCondensables(this);
            cond.makeCondensables();
            condensables = cond.getCondensables();
        } else {
            console.sendMessage(pluginName + ChatColor.RED + "This plugin requires CraftBukkit 1.7.9 or higher, disabling...");
            pm.disablePlugin(this);
        }
    }

    private Version getServerVersion(String s) {
        Pattern pat = Pattern.compile("\\((.+?)\\)", Pattern.DOTALL);
        Matcher mat = pat.matcher(s);
        String v;
        if (mat.find()) {
            String[] split = mat.group(1).split(" ");
            v = split[1];
        } else {
            v = "1.7.2";
        }
        return new Version(v);
    }

    @Override
    public void onDisable() {
        if (hasVersion) {
            updateTagStats();
            saveConfig();
            closeDatabase();
            resetTime();
            getServer().getScheduler().cancelTasks(this);
        }
    }

    /**
     * Sets up the datardisAreabase.
     */
    private void loadDatabase() {
        String dbtype = getConfig().getString("storage.database");
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
     * Loads the custom configuration files.
     */
    private void loadCustomConfigs() {
        //TODO - change file copy method - just send the file name and process it there?
        tardisCSV.copy("achievements.yml");
        tardisCSV.copy("artron.yml");
        tardisCSV.copy("blocks.yml");
        tardisCSV.copy("rooms.yml");
        tardisCSV.copy("tag.yml");
        tardisCSV.copy("recipes.yml");
        tardisCSV.copy("kits.yml");
        tardisCSV.copy("condensables.yml");
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
        this.kitsConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "kits.yml"));
        this.condensablesConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "condensables.yml"));
    }

    /**
     * Builds the schematics used to create TARDISes and rooms. Also loads the
     * quotes from the quotes file.
     */
    private void loadFiles() {
        tardisCSV.loadCSV();
        roomCSV.loadCSV();
        quotesfile = tardisCSV.copy("quotes.txt");
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
     * StardisArearts a repeating tardisAreask that plays TARDIS sound effects
     * to players while they are inside the TARDIS.
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
     * StardisArearts a repeating tardisAreask that heals players 1/2 a heart
     * per cycle when they are in the Zero room.
     */
    private void startZeroHealing() {
        if (getConfig().getBoolean("allow.zero_room")) {
            this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TARDISZeroRoomRunnable(this), 20L, getConfig().getLong("preferences.heal_speed"));
        }
    }

    /**
     * Checks if the WorldGuard plugin is available, and loads support if it is.
     */
    private void loadWorldGuard() {
        if (pm.getPlugin("WorldGuard") != null) {
            debug("Hooking into WorldGuard!");
            worldGuardOnServer = true;
            worldGuardUtils = new TARDISWorldGuardUtils(this);
        }
    }

    /**
     * Checks if the TARDISHorseSpeed plugin is available, and loads support if
     * it is.
     */
    private void loadHorseSpeed() {
        if (pm.getPlugin("TARDISHorseSpeed") != null) {
            debug("Hooking into TARDISHorseSpeed!");
            horseSpeedOnServer = true;
        }
    }

    /**
     * Checks if the ProjectRassilon plugin is available, and loads support if
     * it is.
     */
    private void loadProjRassilon() {
        if (pm.getPlugin("ProjectRassilon") != null) {
            debug("Hooking into ProjectRassilon!");
            projRassilonOnServer = true;
        }
    }

    private void loadPluginRespect() {
        pluginRespect = new TARDISPluginRespect(this);
        pluginRespect.loadFactions();
        pluginRespect.loadTowny();
        pluginRespect.loadWorldBorder();
    }

    /**
     * Loads the permissions handler for TARDIS worlds if the relevant
     * permissions plugin is enabled. Currently supports GroupManager and
     * bPermissions (as they have per world config files).
     */
    private void loadPerms() {
        if (pm.getPlugin("GroupManager") != null || pm.getPlugin("bPermissions") != null || pm.getPlugin("PermissionsEx") != null) {
            // copy default permissions file if not present
            tardisCSV.copy("permissions.txt");
            if (getConfig().getBoolean("creation.create_worlds")) {
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
        ArrayList<String> quotes = new ArrayList<String>();
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
        if (getConfig().getBoolean("creation.create_worlds")) {
            if (getConfig().getBoolean("creation.default_world")) {
                getConfig().set("creation.default_world", false);
                saveConfig();
                console.sendMessage(pluginName + ChatColor.RED + "default_world was disabled as create_worlds is true!");
            }
            if (pm.getPlugin("TARDISChunkGenerator") == null || (pm.getPlugin("Multiverse-Core") == null && pm.getPlugin("MultiWorld") == null && pm.getPlugin("My Worlds") == null)) {
                getConfig().set("creation.create_worlds", false);
                saveConfig();
                console.sendMessage(pluginName + ChatColor.RED + "Create Worlds was disabled as it requires a multi-world plugin and TARDISChunkGenerator!");
            }
        }
    }

    /**
     * Gets the server default resource pack. Will use the Minecraft default
     * pack if none is specified. Until Minecraft/Bukkit lets us set the RP back
     * to Default, we'll have to host it on DropBox
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
        for (UUID key : trackerKeeper.getTrackSetTime().keySet()) {
            Player p = this.getServer().getPlayer(key);
            if (p != null) {
                p.resetPlayerTime();
            }
        }
    }

    private void setDates() {
        int month = getTagConfig().getInt("month") - 1;
        int day = getTagConfig().getInt("day");
        beforeCal = Calendar.getInstance();
        beforeCal.set(Calendar.HOUR, 0);
        beforeCal.set(Calendar.MINUTE, 0);
        beforeCal.set(Calendar.SECOND, 0);
        beforeCal.set(Calendar.MONTH, month);
        beforeCal.set(Calendar.DATE, day);
        afterCal = Calendar.getInstance();
        afterCal.set(Calendar.HOUR, 23);
        afterCal.set(Calendar.MINUTE, 59);
        afterCal.set(Calendar.SECOND, 59);
        afterCal.set(Calendar.MONTH, month);
        afterCal.set(Calendar.DATE, day);
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
        if (!getConfig().getBoolean("creation.default_world")) {
            return;
        }
        if (getServer().getWorld(getConfig().getString("creation.default_world_name")) == null) {
            console.sendMessage(pluginName + "Default world specified, but it doesn't exist! Trying to create it now...");
            new TARDISSpace(this).createDefaultWorld(getConfig().getString("creation.default_world_name"));
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

    public FileConfiguration getAchievementConfig() {
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

    public FileConfiguration getKitsConfig() {
        return kitsConfig;
    }

    public FileConfiguration getCondensablesConfig() {
        return condensablesConfig;
    }

    public TARDISUtils getUtils() {
        return utils;
    }

    public TARDISPluginRespect getPluginRespect() {
        return pluginRespect;
    }

    public TARDISBuilderInner getInteriorBuilder() {
        return interiorBuilder;
    }

    public TARDISPresetBuilderFactory getPresetBuilder() {
        return presetBuilder;
    }

    public TARDISDestroyerInner getInteriorDestroyer() {
        return interiorDestroyer;
    }

    public TARDISPresetDestroyerFactory getPresetDestroyer() {
        return presetDestroyer;
    }

    public TARDISArea getTardisArea() {
        return tardisArea;
    }

    public TARDISWorldGuardUtils getWorldGuardUtils() {
        return worldGuardUtils;
    }

    public TARDISChameleonPreset getPresets() {
        return presets;
    }

    public TARDISMultiverseInventoriesChecker getTMIChecker() {
        return TMIChecker;
    }

    public TARDISWalls getTardisWalls() {
        return tardisWalls;
    }

    public TARDISShapedRecipe getFigura() {
        return figura;
    }

    public TARDISShapelessRecipe getIncomposita() {
        return incomposita;
    }

    public TARDISPerceptionFilter getFilter() {
        return filter;
    }

    public Calendar getBeforeCal() {
        return beforeCal;
    }

    public Calendar getAfterCal() {
        return afterCal;
    }

    public HashMap<String, Integer> getCondensables() {
        return condensables;
    }

    public TARDISGeneralInstanceKeeper getGeneralKeeper() {
        return generalKeeper;
    }

    public TARDISBuilderInstanceKeeper getBuildKeeper() {
        return buildKeeper;
    }

    public TARDISTrackerInstanceKeeper getTrackerKeeper() {
        return trackerKeeper;
    }

    public ConsoleCommandSender getConsole() {
        return console;
    }

    public String getPluginName() {
        return pluginName;
    }

    public String getResourcePack() {
        return resourcePack;
    }

    public boolean isMySpawn() {
        return mySpawn;
    }

    public void setMySpawn(boolean mySpawn) {
        this.mySpawn = mySpawn;
    }

    public boolean isWorldGuardOnServer() {
        return worldGuardOnServer;
    }

    public boolean isHorseSpeedOnServer() {
        return horseSpeedOnServer;
    }

    public boolean isProjRassilonOnServer() {
        return projRassilonOnServer;
    }

    public PluginManager getPM() {
        return pm;
    }

    public TARDISMakeTardisCSV getTardisCSV() {
        return tardisCSV;
    }

    public TARDII getTARDII() {
        return new TARDII();
    }
}
