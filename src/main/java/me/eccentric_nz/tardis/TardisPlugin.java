/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis;

import io.papermc.lib.PaperLib;
import me.eccentric_nz.tardis.advancement.TardisAdvancementFactory;
import me.eccentric_nz.tardis.api.Tardises;
import me.eccentric_nz.tardis.arch.TardisArchPersister;
import me.eccentric_nz.tardis.ars.ArsConverter;
import me.eccentric_nz.tardis.artron.TardisArtronFurnaceParticle;
import me.eccentric_nz.tardis.artron.TardisCondensables;
import me.eccentric_nz.tardis.artron.TardisStandbyMode;
import me.eccentric_nz.tardis.builders.TardisConsoleLoader;
import me.eccentric_nz.tardis.builders.TardisPresetBuilderFactory;
import me.eccentric_nz.tardis.builders.TardisSeedBlockPersister;
import me.eccentric_nz.tardis.chameleon.ConstructsConverter;
import me.eccentric_nz.tardis.chameleon.TardisChameleonPreset;
import me.eccentric_nz.tardis.chatgui.TardisChatGuiJson;
import me.eccentric_nz.tardis.chemistry.block.ChemistryBlockRecipes;
import me.eccentric_nz.tardis.chemistry.lab.BleachRecipe;
import me.eccentric_nz.tardis.chemistry.lab.HeatBlockRunnable;
import me.eccentric_nz.tardis.chemistry.product.GlowStickRunnable;
import me.eccentric_nz.tardis.control.TardisControlRunnable;
import me.eccentric_nz.tardis.database.*;
import me.eccentric_nz.tardis.database.converters.*;
import me.eccentric_nz.tardis.destroyers.TardisDestroyerInner;
import me.eccentric_nz.tardis.destroyers.TardisPresetDestroyerFactory;
import me.eccentric_nz.tardis.disguise.TardisDisguiseListener;
import me.eccentric_nz.tardis.dynmap.TardisDynmap;
import me.eccentric_nz.tardis.enumeration.Difficulty;
import me.eccentric_nz.tardis.enumeration.InventoryManager;
import me.eccentric_nz.tardis.enumeration.Language;
import me.eccentric_nz.tardis.enumeration.WorldManager;
import me.eccentric_nz.tardis.files.*;
import me.eccentric_nz.tardis.flight.TardisVortexPersister;
import me.eccentric_nz.tardis.forcefield.TardisForceField;
import me.eccentric_nz.tardis.forcefield.TardisForceFieldPersister;
import me.eccentric_nz.tardis.hads.TardisHadsPersister;
import me.eccentric_nz.tardis.handles.TardisHandlesRunnable;
import me.eccentric_nz.tardis.info.TardisInformationSystemListener;
import me.eccentric_nz.tardis.junk.TardisJunkReturnRunnable;
import me.eccentric_nz.tardis.light.RequestSteamMachine;
import me.eccentric_nz.tardis.mobfarming.TardisBeeWaker;
import me.eccentric_nz.tardis.move.TardisMonsterRunnable;
import me.eccentric_nz.tardis.move.TardisPortalPersister;
import me.eccentric_nz.tardis.move.TardisSpectaclesRunnable;
import me.eccentric_nz.tardis.placeholders.TardisPlaceholderExpansion;
import me.eccentric_nz.tardis.planets.TardisAliasResolver;
import me.eccentric_nz.tardis.planets.TardisSpace;
import me.eccentric_nz.tardis.recipes.*;
import me.eccentric_nz.tardis.rooms.TardisRoomPersister;
import me.eccentric_nz.tardis.rooms.TardisZeroRoomRunnable;
import me.eccentric_nz.tardis.siegemode.TardisSiegePersister;
import me.eccentric_nz.tardis.siegemode.TardisSiegeRunnable;
import me.eccentric_nz.tardis.travel.TardisArea;
import me.eccentric_nz.tardis.travel.TardisPluginRespect;
import me.eccentric_nz.tardis.utility.*;
import me.eccentric_nz.tardis.utility.logging.TardisBlockLogger;
import me.eccentric_nz.tardischunkgenerator.TardisHelperPlugin;
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
public class TardisPlugin extends JavaPlugin {

    public static final RequestSteamMachine MACHINE = new RequestSteamMachine();
    public static TardisPlugin plugin;
    private final TardisDatabaseConnection service = TardisDatabaseConnection.getINSTANCE();
    private final TardisArea tardisArea = new TardisArea(this);
    private final TardisBuilderInstanceKeeper buildKeeper = new TardisBuilderInstanceKeeper();
    private final TardisDestroyerInner interiorDestroyer = new TardisDestroyerInner(this);
    private final TardisFileCopier tardisCopier = new TardisFileCopier(this);
    private final TardisPresetBuilderFactory presetBuilder = new TardisPresetBuilderFactory(this);
    private final TardisPresetDestroyerFactory presetDestroyer = new TardisPresetDestroyerFactory(this);
    private final TardisTrackerInstanceKeeper trackerKeeper = new TardisTrackerInstanceKeeper();
    private final TardisChatGuiJson jsonKeeper = new TardisChatGuiJson();
    private final List<String> cleanUpWorlds = new ArrayList<>();
    private final HashMap<String, String> versions = new HashMap<>();
    private Calendar afterCal;
    private Calendar beforeCal;
    private ConsoleCommandSender console;
    private File quotesFile = null;
    private FileConfiguration advancementConfig;
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
    private HashMap<String, Integer> condensables;
    private BukkitTask standbyTask;
    private String pluginName;
    private String resourcePack;
    private TardisChameleonPreset presets;
    private TardisPerceptionFilter filter;
    private TardisPluginRespect pluginRespect;
    private TardisSeedRecipe obstructionum;
    private TardisShapedRecipe figura;
    private TardisShapelessRecipe incomposita;
    private TardisUtils utils;
    private TardisLocationGetters locationUtils;
    private TardisWorldGuardUtils worldGuardUtils;
    private boolean hasVersion = false;
    private boolean tardisSpawn = false;
    private boolean worldGuardOnServer;
    private boolean disguisesOnServer;
    private InventoryManager inventoryManager;
    private PluginManager pluginManager;
    private TardisGeneralInstanceKeeper generalKeeper;
    private TardisHelperPlugin tardisHelper = null;
    private TardisMultiverseHelper multiverseHelper = null;
    private String prefix;
    private Difficulty difficulty;
    private WorldManager worldManager;
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
    private TardisBlockLogger blockLogger;
    private TardisDynmap tardisDynmap;

    public TardisPlugin() {
        worldGuardOnServer = false;
        inventoryManager = InventoryManager.NONE;
        versions.put("dynmap", "3.0.1");
        versions.put("GriefPrevention", "16.13");
        versions.put("LibsDisguises", "10.0.14");
        versions.put("MultiWorld", "5.2");
        versions.put("Multiverse-Adventure", "2.5");
        versions.put("Multiverse-Core", "4.0");
        versions.put("Multiverse-Inventories", "3.0");
        versions.put("MultiInv", "3.3.6");
        versions.put("My_Worlds", "1.16.1");
        versions.put("PerWorldInventory", "2.3.0");
        versions.put("TARDISChunkGenerator", "4.7.0");
        versions.put("Towny", "0.95");
        versions.put("WorldBorder", "1.9.0");
        versions.put("WorldGuard", "7.0.0");
    }

    private Version getServerVersion(String string) {
        Pattern pattern = Pattern.compile("\\((.+?)\\)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(string);
        String version;
        if (matcher.find()) {
            String[] split = matcher.group(1).split(" ");
            String[] temp = split[1].split("-");
            if (temp.length > 1) {
                version = temp[0];
            } else {
                version = split[1];
            }
        } else {
            version = "1.7.10";
        }
        return new Version(version);
    }

    private boolean checkPluginVersion(String pluginName, String min) {
        if (pluginManager.isPluginEnabled(pluginName)) {
            Plugin check = pluginManager.getPlugin(pluginName);
            Version minVersion = new Version(min);
            assert check != null;
            String preSplit = check.getDescription().getVersion();
            String[] split = preSplit.split("-");
            try {
                Version version;
                if (pluginName.equals("TARDISChunkGenerator") && preSplit.startsWith("1")) {
                    version = new Version("1");
                } else if (pluginName.equals("WorldGuard") && preSplit.contains(";")) {
                    // eg 6.2.1;84bc322
                    String[] semi = split[0].split(";");
                    version = new Version(semi[0]);
                } else if (pluginName.equals("Towny") && preSplit.contains(" ")) {
                    // eg 0.93.1.0 Pre-Release 4
                    String[] space = split[0].split(" ");
                    version = new Version(space[0]);
                } else {
                    version = new Version(split[0]);
                }
                return (version.compareTo(minVersion) >= 0);
            } catch (IllegalArgumentException illegalArgumentException) {
                getServer().getLogger().log(Level.WARNING, "TARDIS failed to get the version for {0}.", pluginName);
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
        if (MACHINE.isStarted()) {
            MACHINE.shutdown();
        }
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
            new TardisRoomPersister(this).saveProgress();
            TardisPerceptionFilter.removePerceptionFilter();
            debug("Perception Filters removed");
            if (getConfig().getBoolean("preferences.walk_in_tardis")) {
                new TardisPortalPersister(this).save();
            }
            if (disguisesOnServer && getConfig().getBoolean("arch.enabled")) {
                new TardisArchPersister(this).saveAll();
            }
            if (getConfig().getBoolean("siege.enabled")) {
                new TardisSiegePersister(this).saveCubes();
            }
            if (getConfig().getBoolean("allow.hads")) {
                new TardisHadsPersister(this).save();
            }
            new TardisVortexPersister(this).save();
            if (getConfig().getInt("allow.force_field") > 0) {
                new TardisForceFieldPersister(this).save();
            }
            new TardisSeedBlockPersister(this).save();
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
        // register disguise listener
        getServer().getPluginManager().registerEvents(new TardisDisguiseListener(this), this);
        // start RequestStreamMachine
        MACHINE.start(2, 400);
        pluginManager = getServer().getPluginManager();
        pluginName = ChatColor.GOLD + "[" + getDescription().getName() + "]" + ChatColor.RESET + " ";
        plugin = this;
        oldBlockKey = new NamespacedKey(this, "customBlock");
        customBlockKey = new NamespacedKey(this, "custom_block");
        timeLordUuidKey = new NamespacedKey(this, "timelord_uuid");
        blueprintKey = new NamespacedKey(this, "blueprint");
        sonicUuidKey = new NamespacedKey(this, "sonic_uuid");
        persistentDataTypeUUID = new TardisUuidDataType();
        console = getServer().getConsoleSender();
        Version serverVersion = getServerVersion(getServer().getVersion());
        Version minVersion = new Version("1.16.2");
        // check server version
        if (serverVersion.compareTo(minVersion) >= 0) {
            if (!PaperLib.isPaper() && !PaperLib.isSpigot()) {
                console.sendMessage(pluginName + ChatColor.RED + "TARDIS no longer supports servers running CraftBukkit. Please use Spigot or Paper instead!)");
                hasVersion = false;
                pluginManager.disablePlugin(this);
                return;
            }
            // TARDISChunkGenerator needs to be enabled
            if (!loadHelper()) {
                console.sendMessage(pluginName + ChatColor.RED + "This plugin requires TARDISChunkGenerator to function, disabling...");
                hasVersion = false;
                pluginManager.disablePlugin(this);
                return;
            }
            hasVersion = true;
            for (Map.Entry<String, String> plugin : versions.entrySet()) {
                if (!checkPluginVersion(plugin.getKey(), plugin.getValue())) {
                    console.sendMessage(this.pluginName + ChatColor.RED + "This plugin requires " + plugin.getKey() + " to be v" + plugin.getValue() + " or higher, disabling...");
                    hasVersion = false;
                    pluginManager.disablePlugin(this);
                    return;
                }
            }
            PaperLib.suggestPaper(this);
            worldManager = WorldManager.getWorldManager();
            saveDefaultConfig();
            reloadConfig();
            loadCustomConfigs();
            loadLanguage();
            loadSigns();
            loadChameleonGUIs();
            new TardisConfiguration(this).checkConfig();
            prefix = getConfig().getString("storage.mysql.prefix");
            loadDatabase();
            queryFactory = new QueryFactory(this);
            int conversions = 0;
            // update database materials
            if (!getConfig().getBoolean("conversions.ars_materials")) {
                new ArsConverter(this).convertARS();
                getConfig().set("conversions.ars_materials", true);
                conversions++;
            }
            if (!getConfig().getBoolean("conversions.constructs")) {
                new ConstructsConverter(this).convertConstructs();
                getConfig().set("conversions.constructs", true);
                conversions++;
            }
            if (!getConfig().getBoolean("conversions.controls")) {
                new TardisControlsConverter(this).update();
                getConfig().set("conversions.controls", true);
                conversions++;
            }
            if (!getConfig().getBoolean("conversions.bind")) {
                new TardisBindConverter(this).update();
                getConfig().set("conversions.bind", true);
                conversions++;
            }
            if (!getConfig().getBoolean("conversions.icons")) {
                new TardisSaveIconUpdater(this).addIcons();
                getConfig().set("conversions.icons", true);
                conversions++;
            }
            if (!getConfig().getBoolean("conversions.datapacks")) {
                TardisChecker.updateDimension("gallifrey");
                TardisChecker.updateDimension("siluria");
                TardisChecker.updateDimension("skaro");
                getConfig().set("conversions.datapacks", true);
                conversions++;
            }
            if (!getConfig().getBoolean("conversions.archive_wall_data")) {
                new TardisWallConverter(this).processArchives();
                getConfig().set("conversions.archive_wall_data", true);
                conversions++;
            }
            loadMultiverse();
            loadInventoryManager();
            checkTardisHelper();
            checkDefaultWorld();
            cleanUpWorlds();
            TardisAliasResolver.createAliasMap();
            utils = new TardisUtils(this);
            locationUtils = new TardisLocationGetters(this);
            buildKeeper.setSeeds(getSeeds());
            new TardisConsoleLoader(this).addSchematics();
            loadFiles();
            disguisesOnServer = pluginManager.isPluginEnabled("LibsDisguises");
            generalKeeper = new TardisGeneralInstanceKeeper(this);
            generalKeeper.setQuotes(quotes());
            try {
                difficulty = Difficulty.valueOf(Objects.requireNonNull(getConfig().getString("preferences.difficulty")).toUpperCase(Locale.ENGLISH));
            } catch (IllegalArgumentException illegalArgumentException) {
                debug("Could not determine difficulty setting, using EASY");
                difficulty = Difficulty.EASY;
            }
            // register recipes
            obstructionum = new TardisSeedRecipe(this);
            obstructionum.addSeedRecipes();
            figura = new TardisShapedRecipe(this);
            figura.addShapedRecipes();
            incomposita = new TardisShapelessRecipe(this);
            incomposita.addShapelessRecipes();
            new TardisSmithingRecipe(this).addSmithingRecipes();
            TardisInformationSystemListener info = new TardisListenerRegistrar(this).registerListeners();
            new TardisCommandSetter(this, info).loadCommands();
            startSound();
            startReminders();
            loadWorldGuard();
            loadPluginRespect();
            startZeroHealing();
            startBeeTicks();
            startSiegeTicks();
            if (pluginManager.isPluginEnabled("TARDISChunkGenerator")) {
                TardisSpace alwaysNight = new TardisSpace(this);
                if (getConfig().getBoolean("creation.keep_night")) {
                    alwaysNight.keepNight();
                }
            }
            if (pluginManager.isPluginEnabled("dynmap")) {
                tardisDynmap = new TardisDynmap(this);
                tardisDynmap.enable();
                debug("Creating markers for Dynmap.");
            }
            if (!getConfig().getBoolean("conversions.condenser_materials") || !getConfig().getBoolean("conversions.player_prefs_materials") || !getConfig().getBoolean("conversions.block_materials")) {
                TardisMaterialIdConverter tardisMaterialIdConverter = new TardisMaterialIdConverter(this);
                tardisMaterialIdConverter.checkCondenserData();
                tardisMaterialIdConverter.checkPlayerPrefsData();
                tardisMaterialIdConverter.checkBlockData();
                new TardisFarmingConverter(this).update();
            }
            if (!getConfig().getBoolean("conversions.block_wall_signs")) {
                new TardisWallSignConverter(this).convertSignBlocks();
                getConfig().set("conversions.block_wall_signs", true);
            }
            TardisBlockLoader tardisBlockLoader = new TardisBlockLoader(this);
            tardisBlockLoader.loadGravityWells();
            tardisBlockLoader.loadProtectedBlocks();
            if (worldGuardOnServer && getConfig().getBoolean("allow.wg_flag_set")) {
                tardisBlockLoader.loadAntiBuild();
            }
            loadPerms();
            loadBooks();
            resourcePack = getServerResourcePack();
            // copy advancements to tardis datapack
            new TardisChecker(this).checkAdvancements();
            presets = new TardisChameleonPreset();
            presets.makePresets();
            if (getConfig().getBoolean("preferences.walk_in_tardis")) {
                new TardisPortalPersister(this).load();
                getServer().getScheduler().scheduleSyncRepeatingTask(this, new TardisMonsterRunnable(this), 2400, 2400);
            }
            if (getConfig().getBoolean("allow.3d_doors")) {
                getServer().getScheduler().scheduleSyncRepeatingTask(this, new TardisSpectaclesRunnable(this), 120, 100);
            }
            if (disguisesOnServer && getConfig().getBoolean("arch.enabled")) {
                new TardisArchPersister(this).checkAll();
            }
            if (getConfig().getBoolean("siege.enabled")) {
                TardisSiegePersister tardisSiegePersister = new TardisSiegePersister(this);
                tardisSiegePersister.loadSiege();
                tardisSiegePersister.loadCubes();
            }
            if (getConfig().getBoolean("allow.hads")) {
                TardisHadsPersister tardisHadsPersister = new TardisHadsPersister(this);
                tardisHadsPersister.load();
            }
            TardisTimeRotorLoader tardisTimeRotorLoader = new TardisTimeRotorLoader(this);
            tardisTimeRotorLoader.load();
            if (getConfig().getBoolean("allow.chemistry")) {
                new ChemistryBlockRecipes(this).addRecipes();
                new BleachRecipe(this).setRecipes();
                getServer().getScheduler().scheduleSyncRepeatingTask(this, new GlowStickRunnable(this), 200, 200);
                getServer().getScheduler().scheduleSyncRepeatingTask(this, new HeatBlockRunnable(this), 200, 80);
            }
            if (getConfig().getInt("allow.force_field") > 0) {
                getServer().getScheduler().scheduleSyncRepeatingTask(this, new TardisForceField(this), 20, 5);
            }
            // hook CoreProtectAPI
            blockLogger = new TardisBlockLogger(this);
            if (pluginManager.getPlugin("CoreProtect") != null) {
                debug("Logging block changes with CoreProtect.");
                blockLogger.enableLogger();
            }
            new TardisVortexPersister(this).load();
            new TardisJunkPlayerPersister(this).load();
            new TardisSeedBlockPersister(this).load();
            setDates();
            startStandBy();
            if (getConfig().getBoolean("allow.perception_filter")) {
                filter = new TardisPerceptionFilter(this);
                filter.createPerceptionFilter();
            }
            TardisCondensables tardisCondensables = new TardisCondensables(this);
            tardisCondensables.makeCondensables();
            condensables = tardisCondensables.getCondensables();
            checkDropChests();
            if (artronConfig.getBoolean("artron_furnace.particles")) {
                new TardisArtronFurnaceParticle(this).addParticles();
            }
            if (getConfig().getBoolean("junk.enabled") && getConfig().getLong("junk.return") > 0) {
                generalKeeper.setJunkTime(System.currentTimeMillis());
                long delay = getConfig().getLong("junk.return") * 20;
                getServer().getScheduler().scheduleSyncRepeatingTask(this, new TardisJunkReturnRunnable(this), delay, delay);
            }
            startRecorderTask();
            getServer().getScheduler().scheduleSyncRepeatingTask(this, new TardisControlRunnable(this), 200, 200);
            getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
                if (!TardisAdvancementFactory.checkAdvancement("tardis")) {
                    getConsole().sendMessage(getPluginName() + getLanguage().getString("ADVANCEMENT_RELOAD"));
                    getServer().reloadData();
                }
            }, 199);
            // check TARDIS build
            if (getConfig().getBoolean("preferences.notify_update")) {
                getServer().getScheduler().runTaskAsynchronously(this, new TardisUpdateChecker(this, null));
            }
            // check Spigot build
            getServer().getScheduler().runTaskAsynchronously(this, new TardisSpigotChecker(this));
            // resume any room growing
            new TardisRoomPersister(this).resume();
            if (getConfig().getInt("allow.force_field") > 0) {
                new TardisForceFieldPersister(this).load();
            }
            // hook PlaceholderAPI
            if (pluginManager.getPlugin("PlaceholderAPI") != null) {
                debug("Registering expansion with PlaceholderAPI.");
                new TardisPlaceholderExpansion(this).register();
            }
            if (!getConfig().getBoolean("conversions.restore_biomes")) {
                getServer().getScheduler().scheduleSyncDelayedTask(this, () -> new TardisBiomeConverter(this).convertBiomes(), 1200);
                getConfig().set("conversions.restore_biomes", true);
                conversions++;
            }
            if (conversions > 0) {
                saveConfig();
            }
        } else {
            console.sendMessage(pluginName + ChatColor.RED + "This plugin requires CraftBukkit/Spigot " + minVersion.get() + " or higher, disabling...");
            pluginManager.disablePlugin(this);
        }
    }

    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets up the database.
     */
    private void loadDatabase() {
        String databaseType = getConfig().getString("storage.database");
        try {
            assert databaseType != null;
            if (databaseType.equals("sqlite")) {
                String path = getDataFolder() + File.separator + "TARDIS.db";
                service.setConnection(path);
                TardisSqliteDatabase sqlite = new TardisSqliteDatabase(this);
                sqlite.createTables();
            } else {
                service.setConnection();
                TardisMySqlDatabase mysql = new TardisMySqlDatabase(this);
                mysql.createTables();
            }
        } catch (Exception exception) {
            console.sendMessage(pluginName + "Connection and Tables Error: " + exception);
        }
    }

    /**
     * Closes the database.
     */
    private void closeDatabase() {
        try {
            service.connection.close();
        } catch (SQLException sqlException) {
            console.sendMessage(pluginName + "Could not close database connection: " + sqlException);
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
            if (result) {
                langDir.setWritable(true);
                langDir.setExecutable(true);
                console.sendMessage(pluginName + "Created language directory.");
            }
        }
        // always copy English default
        TardisFileCopier.copy(getDataFolder() + File.separator + "language" + File.separator + "en.yml", getResource("en.yml"), true);
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
        new TardisLanguageUpdater(this).update();
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
            TardisFileCopier.copy(getDataFolder() + File.separator + "language" + File.separator + "signs.yml", getResource("signs.yml"), true);
            file = new File(getDataFolder() + File.separator + "language" + File.separator + "signs.yml");
        }
        // load the language
        signs = YamlConfiguration.loadConfiguration(file);
        new TardisSignsUpdater(plugin, signs).checkSignsConfig();
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
            TardisFileCopier.copy(getDataFolder() + File.separator + "language" + File.separator + "chameleon_guis.yml", getResource("chameleon_guis.yml"), true);
            file = new File(getDataFolder() + File.separator + "language" + File.separator + "chameleon_guis.yml");
        }
        // load the language
        chameleonGuis = YamlConfiguration.loadConfiguration(file);
        new TardisChameleonGuiUpdater(plugin, chameleonGuis).checkChameleonConfig();
    }

    /**
     * Loads the custom configuration files.
     */
    private void loadCustomConfigs() {
        List<String> files = Arrays.asList("achievements.yml", "artron.yml", "blocks.yml", "rooms.yml", "planets.yml", "handles.yml", "tag.yml", "recipes.yml", "kits.yml", "condensables.yml", "custom_consoles.yml");
        for (String f : files) {
            tardisCopier.copy(f);
        }
        planetsConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "planets.yml"));
        new TardisPlanetsUpdater(this, planetsConfig).checkPlanetsConfig();
        roomsConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "rooms.yml"));
        new TardisRoomsUpdater(this, roomsConfig).checkRoomsConfig();
        artronConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "artron.yml"));
        new TardisArtronUpdater(this).checkArtronConfig();
        blocksConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "blocks.yml"));
        new TardisBlocksUpdater(this, blocksConfig).checkBlocksConfig();
        recipesConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "recipes.yml"));
        new TardisRecipesUpdater(this, recipesConfig).addRecipes();
        condensablesConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "condensables.yml"));
        new TardisCondensablesUpdater(this).checkCondensables();
        customConsolesConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "custom_consoles.yml"));
        kitsConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "kits.yml"));
        advancementConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "achievements.yml"));
        tagConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "tag.yml"));
        handlesConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "handles.yml"));
    }

    /**
     * Builds the schematics used to create TARDISes and rooms. Also loads the quotes from the quotes file.
     */
    private void loadFiles() {
        tardisCopier.copyFiles();
        new TardisRoomMap(this).load();
        quotesFile = tardisCopier.copy("quotes.txt");
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
        Set<String> booknames = advancementConfig.getKeys(false);
        booknames.forEach((b) -> TardisFileCopier.copy(getDataFolder() + File.separator + "books" + File.separator + b + ".txt", getResource(b + ".txt"), false));
    }

    /**
     * Starts a repeating task that plays TARDIS sound effects to players while they are inside the TARDIS.
     */
    private void startSound() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> new TardisHumSounds().playTARDISHum(), 60, 1500);
    }

    /**
     * Starts a repeating task that schedules reminders added to a players Handles cyberhead companion.
     */
    private void startReminders() {
        if (getHandlesConfig().getBoolean("reminders.enabled")) {
            getServer().getScheduler().scheduleSyncRepeatingTask(this, new TardisHandlesRunnable(this), 120, getHandlesConfig().getLong("reminders.schedule"));
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
            standbyTask = getServer().getScheduler().runTaskTimerAsynchronously(this, new TardisStandbyMode(this), 6000, repeat);
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
            getServer().getScheduler().scheduleSyncRepeatingTask(this, new TardisSiegeRunnable(this), 1500, ticks);
        }
    }

    /**
     * Starts a repeating task that heals players 1/2 a heart per cycle when they are in the Zero room.
     */
    private void startZeroHealing() {
        if (getConfig().getBoolean("allow.zero_room")) {
            getServer().getScheduler().scheduleSyncRepeatingTask(this, new TardisZeroRoomRunnable(this), 20, getConfig().getLong("preferences.heal_speed"));
        }
    }

    /**
     * Starts a repeating task that wakes bees in the Apiary room.
     */
    private void startBeeTicks() {
        if (getConfig().getBoolean("preferences.wake_bees")) {
            getServer().getScheduler().scheduleSyncRepeatingTask(this, new TardisBeeWaker(this), 40, 500);
        }
    }

    /**
     * Checks if the WorldGuard plugin is available, and loads support if it is.
     */
    private void loadWorldGuard() {
        if (pluginManager.getPlugin("WorldGuard") != null) {
            debug("Hooking into WorldGuard!");
            worldGuardOnServer = true;
            worldGuardUtils = new TardisWorldGuardUtils(this);
        }
    }

    private void loadInventoryManager() {
        if (pluginManager.isPluginEnabled("MultiInv")) {
            inventoryManager = InventoryManager.MULTI;
        }
        if (pluginManager.isPluginEnabled("Multiverse-Inventories")) {
            inventoryManager = InventoryManager.MULTIVERSE;
        }
        if (pluginManager.isPluginEnabled("PerWorldInventory")) {
            inventoryManager = InventoryManager.PER_WORLD;
            TardisPerWorldInventoryChecker.setupPWI();
        }
        if (pluginManager.isPluginEnabled("GameModeInventories")) {
            inventoryManager = InventoryManager.GAMEMODE;
        }
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    /**
     * Checks if the Multiverse-Core plugin is available, and loads support if it is.
     */
    private void loadMultiverse() {
        if (worldManager.equals(WorldManager.MULTIVERSE)) {
            Plugin mvplugin = pluginManager.getPlugin("Multiverse-Core");
            debug("Hooking into Multiverse-Core!");
            multiverseHelper = new TardisMultiverseHelper(mvplugin);
        }
    }

    public TardisMultiverseHelper getMVHelper() {
        return multiverseHelper;
    }

    /**
     * Checks if the TARDISChunkGenerator plugin is available, and loads support if it is.
     */
    private boolean loadHelper() {
        Plugin tcg = pluginManager.getPlugin("TARDISChunkGenerator");
        if (tcg != null && tcg.isEnabled()) {
            debug("Hooking into TARDISChunkGenerator!");
            tardisHelper = (TardisHelperPlugin) getPM().getPlugin("TARDISChunkGenerator");
            return true;
        }
        return false;
    }

    public TardisHelperPlugin getTardisHelper() {
        return tardisHelper;
    }

    private void loadPluginRespect() {
        pluginRespect = new TardisPluginRespect(this);
        pluginRespect.loadFactions();
        pluginRespect.loadTowny();
        pluginRespect.loadWorldBorder();
        pluginRespect.loadGriefPrevention();
        pluginRespect.loadRedProtect();
    }

    /**
     * Loads the permissions handler for TARDIS worlds if the relevant permissions plugin is enabled. Currently supports
     * GroupManager and bPermissions (as they have per world config files).
     */
    private void loadPerms() {
        if (pluginManager.getPlugin("GroupManager") != null || pluginManager.getPlugin("bPermissions") != null || pluginManager.getPlugin("PermissionsEx") != null) {
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
        if (quotesFile != null) {
            BufferedReader bufRdr = null;
            try {
                bufRdr = new BufferedReader(new FileReader(quotesFile));
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
        Set<String> rooms = Objects.requireNonNull(getRoomsConfig().getConfigurationSection("rooms")).getKeys(false);
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

    private void checkTardisHelper() {
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
            if (pluginManager.getPlugin("TARDISChunkGenerator") == null) {
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
        getCleanUpWorlds().forEach((w) -> new TardisWorldRemover(plugin).cleanWorld(w));
    }

    /**
     * Gets the server default resource pack. Will use the Minecraft default pack if none is specified. Until
     * Minecraft/Bukkit lets us set the RP back to Default, we'll have to host it on DropBox
     *
     * @return The server specified texture pack.
     */
    private String getServerResourcePack() {
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
        assert it != null;
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
        String defWorld = getConfig().getString("creation.default_world_name");
        assert defWorld != null;
        if (getServer().getWorld(defWorld) == null) {
            console.sendMessage(pluginName + "Default world specified, but it doesn't exist! Trying to create it now...");
            new TardisSpace(this).createDefaultWorld(defWorld);
        }
    }

    /**
     * Removes unused drop chest database records from the vaults table.
     */
    private void checkDropChests() {
        getServer().getScheduler().scheduleSyncDelayedTask(this, new TardisVaultChecker(this), 2400);
    }

    /**
     * Gets whether TARDISWeepingAngels is the correct version
     *
     * @return true if TWA is the correct version
     */
    public boolean checkTWA() {
        if (getPM().isPluginEnabled("TARDISWeepingAngels")) {
            Plugin twa = getPM().getPlugin("TARDISWeepingAngels");
            assert twa != null;
            Version version = new Version(twa.getDescription().getVersion());
            return (version.compareTo(new Version("3.3.1")) >= 0);
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

    public FileConfiguration getAdvancementConfig() {
        return advancementConfig;
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

    public FileConfiguration getPlanetsConfig() {
        return planetsConfig;
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

    public FileConfiguration getCustomConsolesConfig() {
        return customConsolesConfig;
    }

    public FileConfiguration getLanguage() {
        return language;
    }

    public void setLanguage(FileConfiguration language) {
        this.language = language;
    }

    public FileConfiguration getSigns() {
        return signs;
    }

    public FileConfiguration getChameleonGuis() {
        return chameleonGuis;
    }

    public FileConfiguration getHandlesConfig() {
        return handlesConfig;
    }

    public TardisUtils getUtils() {
        return utils;
    }

    public TardisLocationGetters getLocationUtils() {
        return locationUtils;
    }

    public TardisPluginRespect getPluginRespect() {
        return pluginRespect;
    }

    public TardisPresetBuilderFactory getPresetBuilder() {
        return presetBuilder;
    }

    public TardisDestroyerInner getInteriorDestroyer() {
        return interiorDestroyer;
    }

    public TardisPresetDestroyerFactory getPresetDestroyer() {
        return presetDestroyer;
    }

    public TardisArea getTardisArea() {
        return tardisArea;
    }

    public TardisWorldGuardUtils getWorldGuardUtils() {
        return worldGuardUtils;
    }

    public TardisChameleonPreset getPresets() {
        return presets;
    }

    public TardisShapedRecipe getFigura() {
        return figura;
    }

    public TardisSeedRecipe getOobstructionum() {
        return obstructionum;
    }

    public TardisShapelessRecipe getIncomposita() {
        return incomposita;
    }

    public TardisPerceptionFilter getFilter() {
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

    public TardisGeneralInstanceKeeper getGeneralKeeper() {
        return generalKeeper;
    }

    public TardisBuilderInstanceKeeper getBuildKeeper() {
        return buildKeeper;
    }

    public TardisTrackerInstanceKeeper getTrackerKeeper() {
        return trackerKeeper;
    }

    public TardisChatGuiJson getJsonKeeper() {
        return jsonKeeper;
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

    public boolean isTardisSpawn() {
        return tardisSpawn;
    }

    public void setTardisSpawn(boolean tardisSpawn) {
        this.tardisSpawn = tardisSpawn;
    }

    public boolean isWorldGuardOnServer() {
        return worldGuardOnServer;
    }

    public boolean isDisguisesOnServer() {
        return disguisesOnServer;
    }

    public PluginManager getPM() {
        return pluginManager;
    }

    public TardisFileCopier getTardisCopier() {
        return tardisCopier;
    }

    public Tardises getTardisAPI() {
        return new Tardises();
    }

    public BukkitTask getStandbyTask() {
        return standbyTask;
    }

    public List<String> getCleanUpWorlds() {
        return cleanUpWorlds;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    private void startRecorderTask() {
        int recorder_tick_delay = 5;
        // we schedule it once, it will reschedule itself
        getServer().getScheduler().runTaskLaterAsynchronously(this, new TardisRecordingTask(this), recorder_tick_delay);
    }

    public void setRecordingTask(BukkitTask recordingTask) {
    }

    public NamespacedKey getOldBlockKey() {
        return oldBlockKey;
    }

    public NamespacedKey getCustomBlockKey() {
        return customBlockKey;
    }

    public NamespacedKey getTimeLordUuidKey() {
        return timeLordUuidKey;
    }

    public NamespacedKey getBlueprintKey() {
        return blueprintKey;
    }

    public NamespacedKey getSonicUuidKey() {
        return sonicUuidKey;
    }

    public PersistentDataType<byte[], UUID> getPersistentDataTypeUUID() {
        return persistentDataTypeUUID;
    }

    public QueryFactory getQueryFactory() {
        return queryFactory;
    }

    public boolean isUpdateFound() {
        return updateFound;
    }

    public void setUpdateFound(boolean updateFound) {
        this.updateFound = updateFound;
    }

    public int getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(int buildNumber) {
        this.buildNumber = buildNumber;
    }

    public int getUpdateNumber() {
        return updateNumber;
    }

    public void setUpdateNumber(int updateNumber) {
        this.updateNumber = updateNumber;
    }

    public TardisBlockLogger getBlockLogger() {
        return blockLogger;
    }

    public void savePlanetsConfig() {
        try {
            String planetsPath = plugin.getDataFolder() + File.separator + "planets.yml";
            planetsConfig.save(new File(planetsPath));
        } catch (IOException io) {
            plugin.debug("Could not save planets.yml, " + io.getMessage());
        }
    }
}
