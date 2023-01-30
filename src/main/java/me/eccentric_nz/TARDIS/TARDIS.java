/*
 * Copyright (C) 2023 eccentric_nz
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

import io.papermc.lib.PaperLib;
import me.eccentric_nz.TARDIS.ARS.ARSConverter;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.api.TARDII;
import me.eccentric_nz.TARDIS.arch.TARDISArchPersister;
import me.eccentric_nz.TARDIS.artron.TARDISArtronFurnaceParticle;
import me.eccentric_nz.TARDIS.artron.TARDISCondensables;
import me.eccentric_nz.TARDIS.artron.TARDISStandbyMode;
import me.eccentric_nz.TARDIS.bStats.TARDISStats;
import me.eccentric_nz.TARDIS.builders.TARDISPresetBuilderFactory;
import me.eccentric_nz.TARDIS.builders.TARDISSeedBlockPersister;
import me.eccentric_nz.TARDIS.chameleon.ConstructsConverter;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonPreset;
import me.eccentric_nz.TARDIS.chatGUI.TARDISChatGUIJSON;
import me.eccentric_nz.TARDIS.chemistry.block.ChemistryBlockRecipes;
import me.eccentric_nz.TARDIS.chemistry.lab.BleachRecipe;
import me.eccentric_nz.TARDIS.chemistry.lab.HeatBlockRunnable;
import me.eccentric_nz.TARDIS.chemistry.product.GlowStickRunnable;
import me.eccentric_nz.TARDIS.control.TARDISControlRunnable;
import me.eccentric_nz.TARDIS.database.*;
import me.eccentric_nz.TARDIS.database.converters.*;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTIPS;
import me.eccentric_nz.TARDIS.destroyers.TARDISDestroyerInner;
import me.eccentric_nz.TARDIS.destroyers.TARDISPresetDestroyerFactory;
import me.eccentric_nz.TARDIS.dynmap.TARDISDynmap;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.InventoryManager;
import me.eccentric_nz.TARDIS.enumeration.Language;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.files.*;
import me.eccentric_nz.TARDIS.flight.TARDISVortexPersister;
import me.eccentric_nz.TARDIS.forcefield.TARDISForceField;
import me.eccentric_nz.TARDIS.forcefield.TARDISForceFieldPersister;
import me.eccentric_nz.TARDIS.hads.TARDISHadsPersister;
import me.eccentric_nz.TARDIS.handles.TARDISHandlesRunnable;
import me.eccentric_nz.TARDIS.handles.TARDISHandlesUpdater;
import me.eccentric_nz.TARDIS.info.TARDISInformationSystemListener;
import me.eccentric_nz.TARDIS.junk.TARDISJunkReturnRunnable;
import me.eccentric_nz.TARDIS.move.TARDISMonsterRunnable;
import me.eccentric_nz.TARDIS.move.TARDISPortalPersister;
import me.eccentric_nz.TARDIS.move.TARDISSpectaclesRunnable;
import me.eccentric_nz.TARDIS.placeholders.TARDISPlaceholderExpansion;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.planets.TARDISSpace;
import me.eccentric_nz.TARDIS.recipes.*;
import me.eccentric_nz.TARDIS.rooms.TARDISRoomPersister;
import me.eccentric_nz.TARDIS.rooms.TARDISZeroRoomRunnable;
import me.eccentric_nz.TARDIS.siegemode.TARDISSiegePersister;
import me.eccentric_nz.TARDIS.siegemode.TARDISSiegeRunnable;
import me.eccentric_nz.TARDIS.travel.TARDISArea;
import me.eccentric_nz.TARDIS.travel.TARDISPluginRespect;
import me.eccentric_nz.TARDIS.utility.*;
import me.eccentric_nz.TARDIS.utility.logging.TARDISBlockLogger;
import me.eccentric_nz.tardischunkgenerator.TARDISHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.lang.module.ModuleDescriptor;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The main class where everything is enabled and disabled.
 * <p>
 * "TARDIS" is an acronym meaning "Time And Relative Dimension In Space". TARDISes move through time and space by
 * "disappearing there and reappearing here", a process known as "de- and re-materialisation". TARDISes are used for the
 * observation of various places and times.
 *
 * @author eccentric_nz
 */
public class TARDIS extends JavaPlugin {

    /**
     * static getter for TARDIS plugin
     */
    public static TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final TARDISArea tardisArea = new TARDISArea(this);
    private final TARDISBuilderInstanceKeeper buildKeeper = new TARDISBuilderInstanceKeeper();
    private final TARDISDestroyerInner interiorDestroyer = new TARDISDestroyerInner(this);
    private final TARDISFileCopier tardisCopier = new TARDISFileCopier(this);
    private final TARDISPresetBuilderFactory presetBuilder = new TARDISPresetBuilderFactory(this);
    private final TARDISPresetDestroyerFactory presetDestroyer = new TARDISPresetDestroyerFactory(this);
    private final TARDISTrackerInstanceKeeper trackerKeeper = new TARDISTrackerInstanceKeeper();
    private final TARDISChatGUIJSON jsonKeeper = new TARDISChatGUIJSON();
    private final List<String> cleanUpWorlds = new ArrayList<>();
    private final HashMap<String, String> versions = new HashMap<>();
    //    public TARDISFurnaceRecipe fornacis;
    private Calendar afterCal;
    private Calendar beforeCal;
    private ConsoleCommandSender console;
    private File quotesfile = null;
    private FileConfiguration achievementConfig;
    private FileConfiguration artronConfig;
    private FileConfiguration blocksConfig;
    private FileConfiguration condensablesConfig;
    private FileConfiguration customConsolesConfig;
    private FileConfiguration kitsConfig;
    private FileConfiguration language;
    private FileConfiguration signs;
    private FileConfiguration chameleonGuis;
    private FileConfiguration recipesConfig;
    private FileConfiguration roomsConfig;
    private FileConfiguration tagConfig;
    private FileConfiguration planetsConfig;
    private FileConfiguration handlesConfig;
    private FileConfiguration adaptiveConfig;
    private HashMap<String, Integer> condensables;
    private BukkitTask standbyTask;
    private String pluginName;
    private String resourcePack;
    private TARDISChameleonPreset presets;
    private TARDISPerceptionFilter filter;
    private TARDISPluginRespect pluginRespect;
    private TARDISSeedRecipe obstructionum;
    private TARDISShapedRecipe figura;
    private TARDISShapelessRecipe incomposita;
    private TARDISUtils utils;
    private TARDISLocationGetters locationUtils;
    private TARDISWorldGuardUtils worldGuardUtils;
    private boolean hasVersion = false;
    private boolean tardisSpawn = false;
    private boolean worldGuardOnServer;
    private boolean disguisesOnServer;
    private InventoryManager invManager;
    private PluginManager pm;
    private TARDISGeneralInstanceKeeper generalKeeper;
    private TARDISHelper tardisHelper = null;
    private TARDISMultiverseHelper mvHelper = null;
    private String prefix;
    private Difficulty difficulty;
    private WorldManager worldManager;
    private BukkitTask recordingTask;
    private NamespacedKey oldBlockKey;
    private NamespacedKey customBlockKey;
    private NamespacedKey timeLordUuidKey;
    private NamespacedKey blueprintKey;
    private NamespacedKey sonicUuidKey;
    private PersistentDataType<byte[], UUID> persistentDataTypeUUID;
    private QueryFactory queryFactory;
    private boolean updateFound = false;
    private int buildNumber = 0;
    private int updateNumber = 0;
    private TARDISBlockLogger blockLogger;
    private TARDISDynmap tardisDynmap;

    /**
     * Constructor
     */
    public TARDIS() {
        worldGuardOnServer = false;
        invManager = InventoryManager.NONE;
        versions.put("GriefPrevention", "16.13");
        versions.put("LibsDisguises", "10.0.26");
        versions.put("MultiWorld", "5.2");
        versions.put("Multiverse-Adventure", "2.5");
        versions.put("Multiverse-Core", "4.0");
        versions.put("Multiverse-Inventories", "3.0");
        versions.put("MultiInv", "3.3.6");
        versions.put("My_Worlds", "1.16.1");
        versions.put("PerWorldInventory", "2.3.0");
        versions.put("TARDISChunkGenerator", "4.12.0");
        versions.put("Towny", "0.95");
        versions.put("WorldBorder", "1.9.0");
        versions.put("WorldGuard", "7.0.8");
    }

    private ModuleDescriptor.Version getServerVersion(String s) {
        Pattern pat = Pattern.compile("\\((.+?)\\)", Pattern.DOTALL);
        Matcher mat = pat.matcher(s);
        String v;
        if (mat.find()) {
            String[] split = mat.group(1).split(" ");
            String[] tmp = split[1].split("-");
            if (tmp.length > 1) {
                v = tmp[0];
            } else {
                v = split[1];
            }
        } else {
            v = "1.13";
        }
        return ModuleDescriptor.Version.parse(v);
    }

    private boolean checkPluginVersion(String plg, String min) {
        if (pm.isPluginEnabled(plg)) {
            Plugin check = pm.getPlugin(plg);
            ModuleDescriptor.Version minVersion = ModuleDescriptor.Version.parse(min);
            String preSplit = check.getDescription().getVersion();
            String[] split = preSplit.split("-");
            try {
                ModuleDescriptor.Version version;
                if (plg.equals("TARDISChunkGenerator") && preSplit.startsWith("1")) {
                    version = ModuleDescriptor.Version.parse("1");
                } else if (plg.equals("WorldGuard") && preSplit.contains(";")) {
                    // eg 6.2.1;84bc322
                    String[] semi = split[0].split(";");
                    version = ModuleDescriptor.Version.parse(semi[0]);
                } else if (plg.equals("Towny") && preSplit.contains(" ")) {
                    // eg 0.93.1.0 Pre-Release 4
                    String[] space = split[0].split(" ");
                    version = ModuleDescriptor.Version.parse(space[0]);
                } else {
                    version = ModuleDescriptor.Version.parse(split[0]);
                }
                return (version.compareTo(minVersion) >= 0);
            } catch (IllegalArgumentException e) {
                getServer().getLogger().log(Level.WARNING, "TARDIS failed to get the version for {0}.", plg);
                getServer().getLogger().log(Level.WARNING, "This could cause issues with enabling the plugin.");
                getServer().getLogger().log(Level.WARNING, "Please check you have at least v{0}", min);
                getServer().getLogger().log(Level.WARNING, "The invalid version format was {0}", preSplit);
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onDisable() {
        if (hasVersion) {
            if (tardisDynmap != null) {
                tardisDynmap.disable();
            }
            // force TARDISes to materialise (next restart) if interrupted
            for (int id : getTrackerKeeper().getDematerialising()) {
                if (getTrackerKeeper().getHasDestination().containsKey(id)) {
                    getTrackerKeeper().getDestinationVortex().put(id, -1);
                }
            }
            for (int id : getTrackerKeeper().getMaterialising()) {
                getTrackerKeeper().getDestinationVortex().put(id, -2);
            }
            // persist any room growing
            new TARDISRoomPersister(this).saveProgress();
            TARDISPerceptionFilter.removePerceptionFilter();
            debug("Perception Filters removed");
            if (getConfig().getBoolean("preferences.walk_in_tardis")) {
                new TARDISPortalPersister(this).save();
            }
            if (disguisesOnServer && getConfig().getBoolean("arch.enabled")) {
                new TARDISArchPersister(this).saveAll();
            }
            if (getConfig().getBoolean("siege.enabled")) {
                new TARDISSiegePersister(this).saveCubes();
            }
            if (getConfig().getBoolean("allow.hads")) {
                new TARDISHadsPersister(this).save();
            }
            new TARDISVortexPersister(this).save();
            if (getConfig().getInt("allow.force_field") > 0) {
                new TARDISForceFieldPersister(this).save();
            }
            new TARDISSeedBlockPersister(this).save();
            updateTagStats();
            debug("Updated Tag stats");
            getServer().getScheduler().cancelTasks(this);
            debug("Cancelling all scheduled tasks");
            resetTime();
            debug("Resetting player time(s)");
            closeDatabase();
            debug("Closing database");
            debug("TARDIS disabled successfully!");
        }
    }

    @Override
    public void onEnable() {
        pm = getServer().getPluginManager();
        pluginName = ChatColor.GOLD + "[" + getDescription().getName() + "]" + ChatColor.RESET + " ";
        plugin = this;
        oldBlockKey = new NamespacedKey(this, "customBlock");
        customBlockKey = new NamespacedKey(this, "custom_block");
        timeLordUuidKey = new NamespacedKey(this, "timelord_uuid");
        blueprintKey = new NamespacedKey(this, "blueprint");
        sonicUuidKey = new NamespacedKey(this, "sonic_uuid");
        persistentDataTypeUUID = new TARDISUUIDDataType();
        console = getServer().getConsoleSender();
        ModuleDescriptor.Version serverVersion = getServerVersion(getServer().getVersion());
        ModuleDescriptor.Version minVersion = ModuleDescriptor.Version.parse("1.19.3");
        // check server version
        if (serverVersion.compareTo(minVersion) >= 0) {
            if (!PaperLib.isPaper() && !PaperLib.isSpigot()) {
                console.sendMessage(pluginName + ChatColor.RED + "TARDIS no longer supports servers running CraftBukkit. Please use Spigot or Paper instead!)");
                hasVersion = false;
                pm.disablePlugin(this);
                return;
            }
            // TARDISChunkGenerator needs to be enabled
            if (!loadHelper()) {
                console.sendMessage(pluginName + ChatColor.RED + "This plugin requires TARDISChunkGenerator to function, disabling...");
                hasVersion = false;
                pm.disablePlugin(this);
                return;
            }
            for (Map.Entry<String, String> plg : versions.entrySet()) {
                if (!checkPluginVersion(plg.getKey(), plg.getValue())) {
                    console.sendMessage(pluginName + ChatColor.RED + "This plugin requires " + plg.getKey() + " to be v" + plg.getValue() + " or higher, disabling...");
                    hasVersion = false;
                    pm.disablePlugin(this);
                    return;
                }
            }
            hasVersion = true;
            PaperLib.suggestPaper(this);
            worldManager = WorldManager.getWorldManager();
            saveDefaultConfig();
            reloadConfig();
            loadCustomConfigs();
            loadLanguage();
            loadSigns();
            loadChameleonGUIs();
            new TARDISConfiguration(this).checkConfig();
            prefix = getConfig().getString("storage.mysql.prefix");
            loadDatabase();
            queryFactory = new QueryFactory(this);
            int conversions = 0;
            // update database materials
            if (!getConfig().getBoolean("conversions.ars_materials")) {
                new ARSConverter(this).convertARS();
                getConfig().set("conversions.ars_materials", true);
                conversions++;
            }
            if (!getConfig().getBoolean("conversions.constructs")) {
                new ConstructsConverter(this).convertConstructs();
                getConfig().set("conversions.constructs", true);
                conversions++;
            }
            if (!getConfig().getBoolean("conversions.controls")) {
                new TARDISControlsConverter(this).update();
                getConfig().set("conversions.controls", true);
                conversions++;
            }
            if (!getConfig().getBoolean("conversions.bind")) {
                new TARDISBindConverter(this).update();
                getConfig().set("conversions.bind", true);
                conversions++;
            }
            if (!getConfig().getBoolean("conversions.icons")) {
                new TARDISSaveIconUpdate(this).addIcons();
                getConfig().set("conversions.icons", true);
                conversions++;
            }
            if (!getConfig().getBoolean("conversions.datapacks_1_18")) {
                TARDISChecker.updateDimension("gallifrey");
                TARDISChecker.updateDimension("siluria");
                TARDISChecker.updateDimension("skaro");
                getConfig().set("conversions.datapacks_1_18", true);
                conversions++;
            }
            if (!getConfig().getBoolean("conversions.archive_wall_data")) {
                new TARDISWallConverter(this).processArchives();
                getConfig().set("conversions.archive_wall_data", true);
                conversions++;
            }
            loadMultiverse();
            loadInventoryManager();
            checkTCG();
            checkDefaultWorld();
            cleanUpWorlds();
            TARDISAliasResolver.createAliasMap();
            utils = new TARDISUtils(this);
            locationUtils = new TARDISLocationGetters(this);
            buildKeeper.setSeeds(getSeeds());
            new ResultSetTIPS(this).fillUsedSlotList();
            new TARDISConsoleLoader(this).addSchematics();
            loadFiles();
            disguisesOnServer = pm.isPluginEnabled("LibsDisguises");
            generalKeeper = new TARDISGeneralInstanceKeeper(this);
            generalKeeper.setQuotes(quotes());
            try {
                difficulty = Difficulty.valueOf(getConfig().getString("preferences.difficulty", "EASY").toUpperCase(Locale.ENGLISH));
            } catch (IllegalArgumentException e) {
                debug("Could not determine difficulty setting, using EASY");
                difficulty = Difficulty.EASY;
            }
            // register recipes
            obstructionum = new TARDISSeedRecipe(this);
            obstructionum.addSeedRecipes();
            figura = new TARDISShapedRecipe(this);
            figura.addShapedRecipes();
            incomposita = new TARDISShapelessRecipe(this);
            incomposita.addShapelessRecipes();
            new TARDISSmithingRecipe(this).addSmithingRecipes();
            TARDISInformationSystemListener info = new TARDISListenerRegisterer(this).registerListeners();
            new TARDISCommandSetter(this, info).loadCommands();
            startSound();
            startReminders();
            loadWorldGuard();
            loadPluginRespect();
            startZeroHealing();
            startSiegeTicks();
            if (pm.isPluginEnabled("dynmap") && getConfig().getBoolean("dynmap.enabled")) {
                tardisDynmap = new TARDISDynmap(this);
                tardisDynmap.enable();
            }
            if (!getConfig().getBoolean("conversions.condenser_materials") || !getConfig().getBoolean("conversions.player_prefs_materials") || !getConfig().getBoolean("conversions.block_materials")) {
                TARDISMaterialIDConverter tmic = new TARDISMaterialIDConverter(this);
                tmic.checkCondenserData();
                tmic.checkPlayerPrefsData();
                tmic.checkBlockData();
                new TARDISFarmingConverter(this).update();
            }
            if (!getConfig().getBoolean("conversions.block_wall_signs")) {
                new TARDISWallSignConverter(this).convertSignBlocks();
                getConfig().set("conversions.block_wall_signs", true);
            }
            TARDISBlockLoader bl = new TARDISBlockLoader(this);
            bl.loadGravityWells();
            bl.loadProtectedBlocks();
            if (worldGuardOnServer && getConfig().getBoolean("allow.wg_flag_set")) {
                bl.loadAntiBuild();
            }
            loadPerms();
            loadBooks();
            resourcePack = getServerTP();
            // copy advancements to tardis datapack
            new TARDISChecker(this).checkAdvancements();
            presets = new TARDISChameleonPreset();
            presets.makePresets();
            if (getConfig().getBoolean("preferences.walk_in_tardis")) {
                new TARDISPortalPersister(this).load();
                getServer().getScheduler().scheduleSyncRepeatingTask(this, new TARDISMonsterRunnable(this), 2400, 2400);
            }
            if (getConfig().getBoolean("allow.3d_doors")) {
                getServer().getScheduler().scheduleSyncRepeatingTask(this, new TARDISSpectaclesRunnable(this), 120, 100);
            }
            if (disguisesOnServer && getConfig().getBoolean("arch.enabled")) {
                new TARDISArchPersister(this).checkAll();
            }
            if (getConfig().getBoolean("siege.enabled")) {
                TARDISSiegePersister tsp = new TARDISSiegePersister(this);
                tsp.loadSiege();
                tsp.loadCubes();
            }
            if (getConfig().getBoolean("allow.hads")) {
                TARDISHadsPersister thp = new TARDISHadsPersister(this);
                thp.load();
            }
            TARDISTimeRotorLoader trl = new TARDISTimeRotorLoader(this);
            trl.load();
            if (getConfig().getBoolean("allow.chemistry")) {
                new ChemistryBlockRecipes(this).addRecipes();
                new BleachRecipe(this).setRecipes();
                getServer().getScheduler().scheduleSyncRepeatingTask(this, new GlowStickRunnable(this), 200, 200);
                getServer().getScheduler().scheduleSyncRepeatingTask(this, new HeatBlockRunnable(this), 200, 80);
            }
            if (getConfig().getInt("allow.force_field") > 0) {
                getServer().getScheduler().scheduleSyncRepeatingTask(this, new TARDISForceField(this), 20, 5);
            }
            // hook CoreProtectAPI
            blockLogger = new TARDISBlockLogger(this);
            if (pm.getPlugin("CoreProtect") != null) {
                debug("Logging block changes with CoreProtect.");
                blockLogger.enableLogger();
            }
            new TARDISVortexPersister(this).load();
            new TARDISJunkPlayerPersister(this).load();
            new TARDISSeedBlockPersister(this).load();
            setDates();
            startStandBy();
            if (getConfig().getBoolean("allow.perception_filter")) {
                filter = new TARDISPerceptionFilter(this);
                filter.createPerceptionFilter();
            }
            TARDISCondensables cond = new TARDISCondensables(this);
            cond.makeCondensables();
            condensables = cond.getCondensables();
            checkDropChests();
            if (artronConfig.getBoolean("artron_furnace.particles")) {
                new TARDISArtronFurnaceParticle(this).addParticles();
            }
            if (getConfig().getBoolean("junk.enabled") && getConfig().getLong("junk.return") > 0) {
                generalKeeper.setJunkTime(System.currentTimeMillis());
                long delay = getConfig().getLong("junk.return") * 20;
                getServer().getScheduler().scheduleSyncRepeatingTask(this, new TARDISJunkReturnRunnable(this), delay, delay);
            }
            startRecorderTask();
            getServer().getScheduler().scheduleSyncRepeatingTask(this, new TARDISControlRunnable(this), 200, 200);
            getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
                if (!TARDISAchievementFactory.checkAdvancement("tardis")) {
                    getLogger().log(Level.INFO, getLanguage().getString("ADVANCEMENT_RELOAD"));
                    getServer().reloadData();
                }
            }, 199);
            // check TARDIS build
            if (getConfig().getBoolean("preferences.notify_update")) {
                getServer().getScheduler().runTaskAsynchronously(this, new TARDISUpdateChecker(this, null));
            }
            // check Spigot build
            getServer().getScheduler().runTaskAsynchronously(this, new TARDISSpigotChecker(this));
            // resume any room growing
            new TARDISRoomPersister(this).resume();
            if (getConfig().getInt("allow.force_field") > 0) {
                new TARDISForceFieldPersister(this).load();
            }
            // hook PlaceholderAPI
            if (pm.getPlugin("PlaceholderAPI") != null) {
                debug("Registering expansion with PlaceholderAPI.");
                new TARDISPlaceholderExpansion(this).register();
            }
            if (!getConfig().getBoolean("conversions.restore_biomes")) {
                getServer().getScheduler().scheduleSyncDelayedTask(this, () -> new TARDISBiomeConverter(this).convertBiomes(), 1200);
                getConfig().set("conversions.restore_biomes", true);
                conversions++;
            }
            if (conversions > 0) {
                saveConfig();
            }
            // start bStats metrics
            new TARDISStats(this).startMetrics();
        } else {
            console.sendMessage(pluginName + ChatColor.RED + "This plugin requires Spigot/Paper " + minVersion + " or higher, disabling...");
            pm.disablePlugin(this);
        }
    }

    /**
     * Gets the MySQL database prefix for TARDIS tables
     *
     * @return the prefix from the config
     */
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
     * Loads the configured language file.
     */
    private void loadLanguage() {
        // copy language files
        File langDir = new File(getDataFolder() + File.separator + "language");
        if (!langDir.exists()) {
            boolean result = langDir.mkdir();
            if (result && langDir.setWritable(true) && langDir.setExecutable(true)) {
                console.sendMessage(pluginName + "Created language directory.");
            }
        }
        // always copy English default
        TARDISFileCopier.copy(getDataFolder() + File.separator + "language" + File.separator + "en.yml", getResource("en.yml"), true);
        // only copy ru.yml if it doesn't exist
        TARDISFileCopier.copy(getDataFolder() + File.separator + "language" + File.separator + "ru.yml", getResource("ru.yml"), false);
        // get configured language
        String lang = getConfig().getString("preferences.language");
        // check file exists
        File file;
        file = new File(getDataFolder() + File.separator + "language" + File.separator + lang + ".yml");
        if (!file.isFile()) {
            // load English
            file = new File(getDataFolder() + File.separator + "language" + File.separator + "en.yml");
            lang = "en";
        }
        // load the language
        console.sendMessage(pluginName + "Loading language: " + Language.valueOf(lang).getLang());
        language = YamlConfiguration.loadConfiguration(file);
        // update the language configuration
        new TARDISLanguageUpdater(this).update();
    }

    /**
     * Loads the signs file.
     */
    private void loadSigns() {
        // check file exists
        File file;
        file = new File(getDataFolder() + File.separator + "language" + File.separator + "signs.yml");
        if (!file.exists()) {
            // copy sign file
            TARDISFileCopier.copy(getDataFolder() + File.separator + "language" + File.separator + "signs.yml", getResource("signs.yml"), true);
            file = new File(getDataFolder() + File.separator + "language" + File.separator + "signs.yml");
        }
        // load the language
        signs = YamlConfiguration.loadConfiguration(file);
        new TARDISSignsUpdater(plugin, signs).checkSignsConfig();
    }

    /**
     * Loads the Chameleon GUIs file.
     */
    private void loadChameleonGUIs() {
        // check file exists
        File file;
        file = new File(getDataFolder() + File.separator + "language" + File.separator + "chameleon_guis.yml");
        if (!file.exists()) {
            // copy sign file
            TARDISFileCopier.copy(getDataFolder() + File.separator + "language" + File.separator + "chameleon_guis.yml", getResource("chameleon_guis.yml"), true);
            file = new File(getDataFolder() + File.separator + "language" + File.separator + "chameleon_guis.yml");
        }
        // load the language
        chameleonGuis = YamlConfiguration.loadConfiguration(file);
        new TARDISChameleonGuiUpdater(plugin, chameleonGuis).checkChameleonConfig();
    }

    /**
     * Loads the custom configuration files.
     */
    private void loadCustomConfigs() {
        List<String> files = Arrays.asList("achievements.yml", "adaptive.yml", "artron.yml", "blocks.yml", "rooms.yml", "planets.yml", "handles.yml", "tag.yml", "recipes.yml", "kits.yml", "condensables.yml", "custom_consoles.yml");
        for (String f : files) {
            tardisCopier.copy(f);
        }
        planetsConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "planets.yml"));
        new TARDISPlanetsUpdater(this, planetsConfig).checkPlanetsConfig();
        roomsConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "rooms.yml"));
        new TARDISRoomsUpdater(this, roomsConfig).checkRoomsConfig();
        artronConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "artron.yml"));
        new TARDISArtronUpdater(this).checkArtronConfig();
        blocksConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "blocks.yml"));
        new TARDISBlocksUpdater(this, blocksConfig).checkBlocksConfig();
        recipesConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "recipes.yml"));
        new TARDISRecipesUpdater(this, recipesConfig).addRecipes();
        condensablesConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "condensables.yml"));
        new TARDISCondensablesUpdater(this).checkCondensables();
        customConsolesConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "custom_consoles.yml"));
        kitsConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "kits.yml"));
        achievementConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "achievements.yml"));
        tagConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "tag.yml"));
        handlesConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "handles.yml"));
        new TARDISHandlesUpdater(this, handlesConfig).checkHandles();
        adaptiveConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "adaptive.yml"));
    }

    /**
     * Builds the schematics used to create TARDISes and rooms. Also loads the quotes from the quotes file.
     */
    private void loadFiles() {
        tardisCopier.copyFiles();
        new TARDISRoomMap(this).load();
        quotesfile = tardisCopier.copy("quotes.txt");
    }

    /**
     * Saves the default book files to the /plugins/TARDIS/books directory.
     */
    private void loadBooks() {
        // copy book files
        File bookDir = new File(getDataFolder() + File.separator + "books");
        if (!bookDir.exists()) {
            boolean result = bookDir.mkdir();
            if (result && bookDir.setWritable(true) && bookDir.setExecutable(true)) {
                console.sendMessage(pluginName + "Created books directory.");
            }
        }
        Set<String> booknames = achievementConfig.getKeys(false);
        booknames.forEach((b) -> TARDISFileCopier.copy(getDataFolder() + File.separator + "books" + File.separator + b + ".txt", getResource(b + ".txt"), false));
    }

    /**
     * Starts a repeating task that plays TARDIS sound effects to players while they are inside the TARDIS.
     */
    private void startSound() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> new TARDISHumSounds().playTARDISHum(), 60, 1500);
    }

    /**
     * Starts a repeating task that schedules reminders added to a players Handles cyberhead companion.
     */
    private void startReminders() {
        if (getHandlesConfig().getBoolean("reminders.enabled")) {
            getServer().getScheduler().scheduleSyncRepeatingTask(this, new TARDISHandlesRunnable(this), 120, getHandlesConfig().getLong("reminders.schedule"));
        }
    }

    /**
     * Starts a repeating task that removes Artron Energy from the TARDIS while it is in standby mode (ie not
     * travelling). Only runs if `standby_time` in artron.yml is greater than 0 (the default is 6000 or every 5
     * minutes).
     */
    public void startStandBy() {
        if (getConfig().getBoolean("allow.power_down")) {
            long repeat = getArtronConfig().getLong("standby_time");
            if (repeat <= 0) {
                return;
            }
            standbyTask = getServer().getScheduler().runTaskTimerAsynchronously(this, new TARDISStandbyMode(this), 6000, repeat);
        }
    }

    /**
     * Starts a repeating task that removes Artron Energy from the TARDIS while it is in Siege Mode. Only runs if
     * `siege_ticks` in artron.yml is greater than 0 (the default is 1500 or every 1 minute 15 seconds).
     */
    private void startSiegeTicks() {
        if (getConfig().getBoolean("siege.enabled")) {
            long ticks = getArtronConfig().getLong("siege_ticks");
            if (ticks <= 0) {
                return;
            }
            getServer().getScheduler().scheduleSyncRepeatingTask(this, new TARDISSiegeRunnable(this), 1500, ticks);
        }
    }

    /**
     * Starts a repeating task that heals players 1/2 a heart per cycle when they are in the Zero room.
     */
    private void startZeroHealing() {
        if (getConfig().getBoolean("allow.zero_room")) {
            getServer().getScheduler().scheduleSyncRepeatingTask(this, new TARDISZeroRoomRunnable(this), 20, getConfig().getLong("preferences.heal_speed"));
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

    private void loadInventoryManager() {
        if (pm.isPluginEnabled("Multiverse-Inventories")) {
            invManager = InventoryManager.MULTIVERSE;
        }
        if (pm.isPluginEnabled("GameModeInventories")) {
            invManager = InventoryManager.GAMEMODE;
        }
    }

    /**
     * Gets the inventory manager that the server is using
     *
     * @return the {@link InventoryManager}
     */
    public InventoryManager getInvManager() {
        return invManager;
    }

    /**
     * Checks if the Multiverse-Core plugin is available, and loads support if it is.
     */
    private void loadMultiverse() {
        if (worldManager.equals(WorldManager.MULTIVERSE)) {
            Plugin mvplugin = pm.getPlugin("Multiverse-Core");
            debug("Hooking into Multiverse-Core!");
            mvHelper = new TARDISMultiverseHelper(mvplugin);
        }
    }

    /**
     * Gets the Multiverse plugin utility class
     *
     * @return the utility class
     */
    public TARDISMultiverseHelper getMVHelper() {
        return mvHelper;
    }

    /**
     * Checks if the TARDISChunkGenerator plugin is available, and loads support if it is.
     *
     * @return true if the plugin is enabled, if false the TARDIS plugin will disable itself
     */
    private boolean loadHelper() {
        Plugin tcg = pm.getPlugin("TARDISChunkGenerator");
        if (tcg != null && tcg.isEnabled()) {
            debug("Hooking into TARDISChunkGenerator!");
            tardisHelper = (TARDISHelper) getPM().getPlugin("TARDISChunkGenerator");
            return true;
        }
        return false;
    }

    /**
     * Gets the TARDISChunkGenerator helper utility for accessing CraftBukkit and NMS methods
     *
     * @return the helper utility
     */
    public TARDISHelper getTardisHelper() {
        return tardisHelper;
    }

    private void loadPluginRespect() {
        pluginRespect = new TARDISPluginRespect(this);
        pluginRespect.loadFactions();
        pluginRespect.loadTowny();
        pluginRespect.loadWorldBorder();
        pluginRespect.loadGriefPrevention();
        pluginRespect.loadRedProtect();
    }

    /**
     * Loads the permissions handler for TARDIS worlds if the relevant permissions plugin is enabled. Currently only
     * supports GroupManager and bPermissions (as they have per world config files).
     */
    private void loadPerms() {
        if (pm.getPlugin("GroupManager") != null || pm.getPlugin("bPermissions") != null || pm.getPlugin("PermissionsEx") != null) {
            // copy default permissions file if not present
            tardisCopier.copy("permissions.txt");
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
    private ArrayList<String> quotes() {
        ArrayList<String> quotes = new ArrayList<>();
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
     * Reads the config file and places the configured seed material for each room type into a HashMap.
     */
    private HashMap<Material, String> getSeeds() {
        HashMap<Material, String> map = new HashMap<>();
        Set<String> rooms = getRoomsConfig().getConfigurationSection("rooms").getKeys(false);
        int r = 0;
        for (String s : rooms) {
            if (!getRoomsConfig().contains("rooms." + s + ".user")) {
                // set user supplied rooms as `user: true`
                getRoomsConfig().set("rooms." + s + ".user", true);
                r++;
            }
            if (getRoomsConfig().getBoolean("rooms." + s + ".enabled")) {
                try {
                    Material m = Material.valueOf(getRoomsConfig().getString("rooms." + s + ".seed"));
                    map.put(m, s);
                } catch (IllegalArgumentException e) {
                    debug("Invalid room seed: " + getRoomsConfig().getString("rooms." + s + ".seed"));
                }
            }
        }
        if (r > 0) {
            try {
                getRoomsConfig().save(new File(getDataFolder(), "rooms.yml"));
            } catch (IOException io) {
                debug("Could not save rooms.yml, " + io.getMessage());
            }
        }
        return map;
    }

    private void checkTCG() {
        if (getConfig().getBoolean("creation.create_worlds")) {
            if (getConfig().getBoolean("abandon.enabled")) {
                getConfig().set("abandon.enabled", false);
                saveConfig();
                console.sendMessage(pluginName + ChatColor.RED + "Abandoned TARDISes were disabled as create_worlds is true!");
            }
            if (getConfig().getBoolean("creation.default_world")) {
                getConfig().set("creation.default_world", false);
                saveConfig();
                console.sendMessage(pluginName + ChatColor.RED + "default_world was disabled as create_worlds is true!");
            }
            if (pm.getPlugin("TARDISChunkGenerator") == null) {
                getConfig().set("creation.create_worlds", false);
                saveConfig();
                console.sendMessage(pluginName + ChatColor.RED + "Create Worlds was disabled as it requires TARDISChunkGenerator!");
            }
        }
        if (getConfig().getBoolean("creation.create_worlds_with_perms") && getConfig().getBoolean("abandon.enabled")) {
            getConfig().set("abandon.enabled", false);
            saveConfig();
            console.sendMessage(pluginName + ChatColor.RED + "Abandoned TARDISes were disabled as create_worlds_with_perms is true!");
        }
    }

    private void cleanUpWorlds() {
        getCleanUpWorlds().forEach((w) -> new TARDISWorldRemover(plugin).cleanWorld(w));
    }

    /**
     * Gets the server default resource pack. Will use the Minecraft default pack if none is specified. Until
     * Minecraft/Bukkit lets us set the RP back to Default, we'll have to host it on DropBox
     *
     * @return The server specified texture pack.
     */
    private String getServerTP() {
        String link = "https://www.dropbox.com/s/utka3zxmer7f19g/Default.zip?dl=1";
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
        trackerKeeper.getSetTime().keySet().forEach((key) -> {
            Player p = getServer().getPlayer(key);
            if (p != null) {
                p.resetPlayerTime();
            }
        });
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
            HashMap<String, Object> set = new HashMap<>();
            set.put("player", getTagConfig().getString("it"));
            long time = System.currentTimeMillis() - getTagConfig().getLong("time");
            set.put("time", time);
            getQueryFactory().doSyncInsert("tag", set);
        }
    }

    private void checkDefaultWorld() {
        if (!getConfig().getBoolean("creation.default_world")) {
            return;
        }
        String defWorld = getConfig().getString("creation.default_world_name", "TARDIS_TimeVortex");
        if (getServer().getWorld(defWorld) == null) {
            console.sendMessage(pluginName + "Default world specified, but it doesn't exist! Trying to create it now...");
            new TARDISSpace(this).createDefaultWorld(defWorld);
        }
    }

    /**
     * Removes unused drop chest database records from the vaults table.
     */
    private void checkDropChests() {
        getServer().getScheduler().scheduleSyncDelayedTask(this, new TARDISVaultChecker(this), 2400);
    }

    /**
     * Gets whether TARDISWeepingAngels is the correct version
     *
     * @return true if TWA is the correct version
     */
    public boolean checkTWA() {
        if (getPM().isPluginEnabled("TARDISWeepingAngels")) {
            Plugin twa = getPM().getPlugin("TARDISWeepingAngels");
            ModuleDescriptor.Version version = ModuleDescriptor.Version.parse(twa.getDescription().getVersion());
            return (version.compareTo(ModuleDescriptor.Version.parse("3.3.1")) >= 0);
        } else {
            return false;
        }
    }

    /**
     * Outputs a message to the console. Requires debug: true in config.yml
     *
     * @param o the Object to print to the console
     */
    public void debug(Object o) {
        if (getConfig().getBoolean("debug")) {
            console.sendMessage(pluginName + "Debug: " + o);
        }
    }

    /**
     * Gets the achievements configuration
     *
     * @return the achievements configuration
     */
    public FileConfiguration getAchievementConfig() {
        return achievementConfig;
    }

    /**
     * Gets the artron configuration
     *
     * @return the artron configuration
     */
    public FileConfiguration getArtronConfig() {
        return artronConfig;
    }

    /**
     * Gets the blocks configuration
     *
     * @return the blocks configuration
     */
    public FileConfiguration getBlocksConfig() {
        return blocksConfig;
    }

    /**
     * Gets the rooms configuration
     *
     * @return the rooms configuration
     */
    public FileConfiguration getRoomsConfig() {
        return roomsConfig;
    }

    /**
     * Gets the planets configuration
     *
     * @return the planets configuration
     */
    public FileConfiguration getPlanetsConfig() {
        return planetsConfig;
    }

    /**
     * Gets the tag configuration
     *
     * @return the tag configuration
     */
    public FileConfiguration getTagConfig() {
        return tagConfig;
    }

    /**
     * Gets the recipes configuration
     *
     * @return the recipes configuration
     */
    public FileConfiguration getRecipesConfig() {
        return recipesConfig;
    }

    /**
     * Gets the kits configuration
     *
     * @return the kits configuration
     */
    public FileConfiguration getKitsConfig() {
        return kitsConfig;
    }

    /**
     * Gets the condensables configuration
     *
     * @return the condensables configuration
     */
    public FileConfiguration getCondensablesConfig() {
        return condensablesConfig;
    }

    /**
     * Gets the custom consoles configuration
     *
     * @return the custom consoles configuration
     */
    public FileConfiguration getCustomConsolesConfig() {
        return customConsolesConfig;
    }

    /**
     * Gets the language configuration
     *
     * @return the language configuration
     */
    public FileConfiguration getLanguage() {
        return language;
    }

    /**
     * Set the language configuration
     *
     * @param language the language configuration to use
     */
    public void setLanguage(FileConfiguration language) {
        this.language = language;
    }

    /**
     * Gets the signs configuration
     *
     * @return the signs configuration
     */
    public FileConfiguration getSigns() {
        return signs;
    }

    /**
     * Gets the chameleon guis configuration
     *
     * @return the chameleon guis configuration
     */
    public FileConfiguration getChameleonGuis() {
        return chameleonGuis;
    }

    /**
     * Gets the handles configuration
     *
     * @return the handles configuration
     */
    public FileConfiguration getHandlesConfig() {
        return handlesConfig;
    }

    /**
     * Gets the adaptive biome configuration
     *
     * @return the adaptive biome configuration
     */
    public FileConfiguration getAdaptiveConfig() {
        return adaptiveConfig;
    }

    /**
     * Gets the TARDIS Utilities class
     *
     * @return the TARDIS Utilities class
     */
    public TARDISUtils getUtils() {
        return utils;
    }

    /**
     * Gets the TARDIS Location Utilities class
     *
     * @return the TARDIS Location Utilities class
     */
    public TARDISLocationGetters getLocationUtils() {
        return locationUtils;
    }

    /**
     * Gets the TARDIS Plugin Respect Utilities class
     *
     * @return the TARDIS Plugin Respect Utilities class
     */
    public TARDISPluginRespect getPluginRespect() {
        return pluginRespect;
    }

    /**
     * Gets the TARDIS Preset Builder
     *
     * @return the TARDIS Preset Builder
     */
    public TARDISPresetBuilderFactory getPresetBuilder() {
        return presetBuilder;
    }

    /**
     * Gets the TARDIS Interior Destroyer
     *
     * @return the TARDIS Interior Destroyer
     */
    public TARDISDestroyerInner getInteriorDestroyer() {
        return interiorDestroyer;
    }

    /**
     * Gets the TARDIS Preset Destroyer Factory
     *
     * @return the TARDIS Preset Destroyer Factory
     */
    public TARDISPresetDestroyerFactory getPresetDestroyer() {
        return presetDestroyer;
    }

    /**
     * Gets the TARDIS Area Utilities class
     *
     * @return the TARDIS Area Utilities class
     */
    public TARDISArea getTardisArea() {
        return tardisArea;
    }

    /**
     * Gets the WorldGuard Utilities class
     *
     * @return the WorldGuard Utilities class
     */
    public TARDISWorldGuardUtils getWorldGuardUtils() {
        return worldGuardUtils;
    }

    /**
     * Gets the Chameleon presets
     *
     * @return the Chameleon presets
     */
    public TARDISChameleonPreset getPresets() {
        return presets;
    }

    /**
     * Gets the TARDIS Shaped Recipes
     *
     * @return the TARDIS Shaped Recipes
     */
    public TARDISShapedRecipe getFigura() {
        return figura;
    }

    /**
     * Gets the TARDIS Seed Recipes
     *
     * @return the TARDIS Seed Recipes
     */
    public TARDISSeedRecipe getObstructionum() {
        return obstructionum;
    }

    /**
     * Gets the TARDIS Shapeless Recipes
     *
     * @return the ARDIS Shapeless Recipes
     */
    public TARDISShapelessRecipe getIncomposita() {
        return incomposita;
    }

    /**
     * Gets the TARDIS Perception Filter
     *
     * @return the TARDIS Perception Filter
     */
    public TARDISPerceptionFilter getFilter() {
        return filter;
    }

    /**
     * Gets a calendar date for the Tag the Ood game
     *
     * @return a calendar
     */
    public Calendar getBeforeCal() {
        return beforeCal;
    }

    /**
     * Gets a calendar date for the Tag the Ood game
     *
     * @return a calendar
     */
    public Calendar getAfterCal() {
        return afterCal;
    }

    /**
     * Gets a map of the TARDIS Condensable values
     *
     * @return a map of the TARDIS Condensable values
     */
    public HashMap<String, Integer> getCondensables() {
        return condensables;
    }

    /**
     * Gets the TARDIS General Keeper of useful things
     *
     * @return the TARDIS General Keeper
     */
    public TARDISGeneralInstanceKeeper getGeneralKeeper() {
        return generalKeeper;
    }

    /**
     * Gets the TARDIS Builder utility class
     *
     * @return the TARDIS Builder utility class
     */
    public TARDISBuilderInstanceKeeper getBuildKeeper() {
        return buildKeeper;
    }

    /**
     * Gets the TARDIS Tracker class
     *
     * @return the TARDIS Tracker class
     */
    public TARDISTrackerInstanceKeeper getTrackerKeeper() {
        return trackerKeeper;
    }

    /**
     * Gets the TARDIS Chat GUI JSON class
     *
     * @return the TARDIS Chat GUI JSON class
     */
    public TARDISChatGUIJSON getJsonKeeper() {
        return jsonKeeper;
    }

    /**
     * Gets the server's Console Command Sender
     *
     * @return the server's Console Command Sender
     */
    public ConsoleCommandSender getConsole() {
        return console;
    }

    /**
     * Gets the formatted TARDIS plugin name for messaging
     *
     * @return the formatted TARDIS plugin name
     */
    public String getPluginName() {
        return pluginName;
    }

    /**
     * Gets the server's default resource pack URL
     *
     * @return the server's default resource pack URL
     */
    public String getResourcePack() {
        return resourcePack;
    }

    /**
     * Gets whether there is a TARDIS plugin spawn
     *
     * @return true if it is a TARDIS plugin spawn
     */
    public boolean isTardisSpawn() {
        return tardisSpawn;
    }

    /**
     * Sets whether a spawn event is a TARDIS plugin spawn and allows it to happen
     *
     * @param tardisSpawn true if this is a TARDIS plugin spawn
     */
    public void setTardisSpawn(boolean tardisSpawn) {
        this.tardisSpawn = tardisSpawn;
    }

    /**
     * Gets whether the WorldGuard plugin is enabled on the server
     *
     * @return true if WorldGuard is enabled
     */
    public boolean isWorldGuardOnServer() {
        return worldGuardOnServer;
    }

    /**
     * Gets whether the LibsDisguises plugin is enabled on the server
     *
     * @return true if LibsDisguises is enabled
     */
    public boolean isDisguisesOnServer() {
        return disguisesOnServer;
    }

    /**
     * Gets the server's Plugin Manager
     *
     * @return the server's Plugin Manager
     */
    public PluginManager getPM() {
        return pm;
    }

    /**
     * Gets the TARDIS File Copier
     *
     * @return the TARDIS File Copier
     */
    public TARDISFileCopier getTardisCopier() {
        return tardisCopier;
    }

    /**
     * Gets the TARDIS API
     *
     * @return the TARDIS API
     */
    public TARDII getTardisAPI() {
        return new TARDII();
    }

    /**
     * Gets the TARDIS Artron Energy Standby Task
     *
     * @return the TARDIS Artron Energy Standby Task
     */
    public BukkitTask getStandbyTask() {
        return standbyTask;
    }

    /**
     * Gets a list of worlds to clean up from the planets configuration
     *
     * @return a list of world names
     */
    public List<String> getCleanUpWorlds() {
        return cleanUpWorlds;
    }

    /**
     * Gets the TARDIS Difficulty level
     *
     * @return the TARDIS Difficulty level
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the TARDIS Difficulty level
     *
     * @param difficulty the {@link Difficulty} level to set
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Gets the TARDIS World Manager
     *
     * @return the TARDIS World Manager
     */
    public WorldManager getWorldManager() {
        return worldManager;
    }

    private void startRecorderTask() {
        int recorder_tick_delay = 5;
        // we schedule it once, it will reschedule itself
        recordingTask = getServer().getScheduler().runTaskLaterAsynchronously(this, new TARDISRecordingTask(this), recorder_tick_delay);
    }

    /**
     * Sets the TARDIS Block RecordingTask
     *
     * @param recordingTask the BukkitTask to set
     */
    public void setRecordingTask(BukkitTask recordingTask) {
        this.recordingTask = recordingTask;
    }

    /**
     * Gets the deprecated TARDIS Custom Block NamespacedKey
     *
     * @return the deprecated TARDIS Custom Block NamespacedKey
     */
    public NamespacedKey getOldBlockKey() {
        return oldBlockKey;
    }

    /**
     * Gets the TARDIS Custom Block NamespacedKey
     *
     * @return the TARDIS Custom Block NamespacedKey
     */
    public NamespacedKey getCustomBlockKey() {
        return customBlockKey;
    }

    /**
     * Gets the Time Lord UUID NamespacedKey
     *
     * @return the Time Lord UUID NamespacedKey
     */
    public NamespacedKey getTimeLordUuidKey() {
        return timeLordUuidKey;
    }

    /**
     * Gets the TARDIS Blueprint NamespacedKey
     *
     * @return the TARDIS Blueprint NamespacedKey
     */
    public NamespacedKey getBlueprintKey() {
        return blueprintKey;
    }

    /**
     * Gets the Sonic Screwdriver UUID NamespacedKey
     *
     * @return the Sonic Screwdriver UUID NamespacedKey
     */
    public NamespacedKey getSonicUuidKey() {
        return sonicUuidKey;
    }

    /**
     * Gets the TARDIS UUID Persistent Data Type
     *
     * @return the TARDIS UUID Persistent Data Type
     */
    public PersistentDataType<byte[], UUID> getPersistentDataTypeUUID() {
        return persistentDataTypeUUID;
    }

    /**
     * Gets the TARDIS database Query Factory
     *
     * @return the Query Factory
     */
    public QueryFactory getQueryFactory() {
        return queryFactory;
    }

    /**
     * Gets whether an update was found
     *
     * @return true if an update was found
     */
    public boolean isUpdateFound() {
        return updateFound;
    }

    /**
     * Sets whether an update was found
     *
     * @param updateFound true if an update was found
     */
    public void setUpdateFound(boolean updateFound) {
        this.updateFound = updateFound;
    }

    /**
     * Gets the Build Number
     *
     * @return the Build Number
     */
    public int getBuildNumber() {
        return buildNumber;
    }

    /**
     * Sets the Build Number
     *
     * @param buildNumber the build number to set
     */
    public void setBuildNumber(int buildNumber) {
        this.buildNumber = buildNumber;
    }

    /**
     * Gets the Update Number
     *
     * @return the Update Number
     */
    public int getUpdateNumber() {
        return updateNumber;
    }

    /**
     * Sets the Update Number
     *
     * @param updateNumber the update number to set
     */
    public void setUpdateNumber(int updateNumber) {
        this.updateNumber = updateNumber;
    }

    /**
     * Gets the TARDIS Block Logger
     *
     * @return the TARDIS Block Logger
     */
    public TARDISBlockLogger getBlockLogger() {
        return blockLogger;
    }

    /**
     * Saves the Planets Configuration
     */
    public void savePlanetsConfig() {
        try {
            String planetsPath = plugin.getDataFolder() + File.separator + "planets.yml";
            planetsConfig.save(new File(planetsPath));
        } catch (IOException io) {
            plugin.debug("Could not save planets.yml, " + io.getMessage());
        }
    }

    public TARDISDynmap getTardisDynmap() {
        return tardisDynmap;
    }

    public void setTardisDynmap(TARDISDynmap tardisDynmap) {
        this.tardisDynmap = tardisDynmap;
    }
}
