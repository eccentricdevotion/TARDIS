/*
 * Copyright (C) 2025 eccentric_nz
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

import me.eccentric_nz.TARDIS.api.TARDII;
import me.eccentric_nz.TARDIS.artron.TARDISCondensables;
import me.eccentric_nz.TARDIS.bStats.TARDISStats;
import me.eccentric_nz.TARDIS.builders.exterior.TARDISPresetBuilderFactory;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonPreset;
import me.eccentric_nz.TARDIS.chatGUI.TARDISChatGUI;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.TARDISMySQLDatabase;
import me.eccentric_nz.TARDIS.database.TARDISSQLiteDatabase;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTIPS;
import me.eccentric_nz.TARDIS.destroyers.TARDISDestroyerInner;
import me.eccentric_nz.TARDIS.destroyers.TARDISPresetDestroyerFactory;
import me.eccentric_nz.TARDIS.doors.TARDISCustomDoorLoader;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.files.*;
import me.eccentric_nz.TARDIS.flight.vehicle.VehicleLoader;
import me.eccentric_nz.TARDIS.handles.TARDISHandlesUpdater;
import me.eccentric_nz.TARDIS.handles.wiki.HandlesWikiServerLink;
import me.eccentric_nz.TARDIS.mapping.TARDISBlueMap;
import me.eccentric_nz.TARDIS.mapping.TARDISDynmap;
import me.eccentric_nz.TARDIS.mapping.TARDISMapper;
import me.eccentric_nz.TARDIS.mapping.TARDISSquareMap;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.monitor.SnapshotLoader;
import me.eccentric_nz.TARDIS.perms.TARDISContexts;
import me.eccentric_nz.TARDIS.placeholders.TARDISPlaceholderExpansion;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.recipes.*;
import me.eccentric_nz.TARDIS.rooms.eye.EyeLoader;
import me.eccentric_nz.TARDIS.rotors.TARDISCustomRotorLoader;
import me.eccentric_nz.TARDIS.sensor.SensorTracker;
import me.eccentric_nz.TARDIS.skins.SkinChanger;
import me.eccentric_nz.TARDIS.travel.TARDISArea;
import me.eccentric_nz.TARDIS.travel.TARDISPluginRespect;
import me.eccentric_nz.TARDIS.utility.*;
import me.eccentric_nz.TARDIS.utility.logging.TARDISBlockLogger;
import me.eccentric_nz.TARDIS.utility.protection.TARDISWorldGuardUtils;
import me.eccentric_nz.tardischunkgenerator.TARDISHelper;
import me.eccentric_nz.tardischunkgenerator.worldgen.*;
import me.eccentric_nz.tardisregeneration.TARDISRegenerationUpdater;
import me.eccentric_nz.tardisshop.ShopSettings;
import me.eccentric_nz.tardissonicblaster.BlasterSettings;
import me.eccentric_nz.tardisvortexmanipulator.TVMSettings;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    private final List<String> cleanUpWorlds = new ArrayList<>();
    private final HashMap<String, String> versions = new HashMap<>();
    private final String versionRegex = "(\\d+[.])+\\d+";
    private final Pattern versionPattern = Pattern.compile(versionRegex);
    private TARDISMessage messenger;
    private TARDISChatGUI jsonKeeper;
    private SkinChanger skinChanger;
    private Calendar afterCal;
    private Calendar beforeCal;
    private ConsoleCommandSender console;
    private File quotesfile = null;
    private FileConfiguration achievementConfig;
    private FileConfiguration artronConfig;
    private FileConfiguration blocksConfig;
    private FileConfiguration condensablesConfig;
    private FileConfiguration customConsolesConfig;
    private FileConfiguration customDoorsConfig;
    private FileConfiguration customRotorsConfig;
    private FileConfiguration kitsConfig;
    private FileConfiguration language;
    private FileConfiguration signs;
    private FileConfiguration chameleonGuis;
    private FileConfiguration roomsConfig;
    private FileConfiguration tagConfig;
    private FileConfiguration planetsConfig;
    private FileConfiguration handlesConfig;
    private FileConfiguration adaptiveConfig;
    private FileConfiguration generatorConfig;
    private FileConfiguration shopConfig;
    private FileConfiguration monstersConfig;
    private FileConfiguration vortexConfig;
    private FileConfiguration itemsConfig;
    private FileConfiguration blasterConfig;
    private FileConfiguration customModelConfig;
    private FileConfiguration systemUpgradesConfig;
    private FileConfiguration regenerationConfig;
    private HashMap<String, Integer> condensables;
    private BukkitTask standbyTask;
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
    private CraftingDifficulty craftingDifficulty;
    private WorldManager worldManager;
    private NamespacedKey oldBlockKey;
    private NamespacedKey customBlockKey;
    private NamespacedKey destroyKey;
    private NamespacedKey loopKey;
    private NamespacedKey tardisIdKey;
    private NamespacedKey timeLordUuidKey;
    private NamespacedKey standUuidKey;
    private NamespacedKey interactionUuidKey;
    private NamespacedKey modelUuidKey;
    private NamespacedKey unaryKey;
    private NamespacedKey blueprintKey;
    private NamespacedKey sonicUuidKey;
    private NamespacedKey sonicChargeKey;
    private NamespacedKey microscopeKey;
    private PersistentDataType<byte[], UUID> persistentDataTypeUUID;
    private QueryFactory queryFactory;
    private TARDISBlockLogger blockLogger;
    private TARDISMapper tardisMapper;
    private ShopSettings shopSettings;
    private TVMSettings tvmSettings;
    private BlasterSettings blasterSettings;

    /**
     * Constructor
     */
    public TARDIS() {
        worldGuardOnServer = false;
        invManager = InventoryManager.NONE;
        versions.put("GriefPrevention", "16.18");
        versions.put("LibsDisguises", "11.0.6");
        versions.put("Multiverse-Core", "5.0");
        versions.put("Multiverse-Inventories", "5.0");
        versions.put("Towny", "0.101");
        versions.put("WorldGuard", "7.0.14");
    }

    public TARDISMessage getMessenger() {
        return messenger;
    }

    @Override
    public void onDisable() {
        if (hasVersion) {
            if (tardisMapper != null) {
                tardisMapper.disable();
            }
            // turn off charging sensors
            SensorTracker.resetChargingSensors();
            // force TARDISes to materialise (next restart) if interrupted
            for (int id : getTrackerKeeper().getDematerialising()) {
                if (getTrackerKeeper().getHasDestination().containsKey(id)) {
                    getTrackerKeeper().getDestinationVortex().put(id, -1);
                }
            }
            for (int id : getTrackerKeeper().getMaterialising()) {
                getTrackerKeeper().getDestinationVortex().put(id, -2);
            }
            // only try to persist data if the plugin was enabled properly
            boolean hasDatabase = TARDISDatabaseConnection.getINSTANCE().connection != null;
            if (hasDatabase) {
                TARDISPerceptionFilter.removePerceptionFilter();
                debug("Perception Filters removed");
                new TARDISPersister(this).save();
            }
            getServer().getScheduler().cancelTasks(this);
            debug("Cancelling all scheduled tasks");
            resetTime();
            debug("Resetting player time(s)");
            if (hasDatabase) {
                updateTagStats();
                debug("Updated Tag stats");
                closeDatabase();
                debug("Closing database");
            }
            debug("TARDIS disabled successfully!");
        }
    }

    @Override
    public void onEnable() {
        pm = getServer().getPluginManager();
        plugin = this;
        oldBlockKey = new NamespacedKey(this, "customBlock");
        customBlockKey = new NamespacedKey(this, "custom_block");
        destroyKey = new NamespacedKey(this, "destroy");
        loopKey = new NamespacedKey(this, "loop");
        tardisIdKey = new NamespacedKey(this, "tardis_id");
        timeLordUuidKey = new NamespacedKey(this, "timelord_uuid");
        standUuidKey = new NamespacedKey(this, "stand_uuid");
        interactionUuidKey = new NamespacedKey(this, "interaction_uuid");
        modelUuidKey = new NamespacedKey(this, "model_uuid");
        unaryKey = new NamespacedKey(this, "unary");
        blueprintKey = new NamespacedKey(this, "blueprint");
        sonicUuidKey = new NamespacedKey(this, "sonic_uuid");
        sonicChargeKey = new NamespacedKey(this, "sonic_charge");
        microscopeKey = new NamespacedKey(this, "microscope");
        persistentDataTypeUUID = new TARDISUUIDDataType();
        console = getServer().getConsoleSender();
        ModuleDescriptor.Version serverVersion = getServerVersion(getServer().getVersion());
        ModuleDescriptor.Version minVersion = ModuleDescriptor.Version.parse("1.21.10");
        // check server version
        if (serverVersion.compareTo(minVersion) >= 0) {
            // don't start if TARDISChunkGenerator is present
            if (pm.isPluginEnabled("TARDISChunkGenerator")) {
                getLogger().log(Level.SEVERE, "This plugin no longer requires TARDISChunkGenerator please remove and try again, disabling...");
                hasVersion = false;
                pm.disablePlugin(this);
                return;
            }
            messenger = new TARDISMessage();
            jsonKeeper = new TARDISChatGUI();
            skinChanger = new SkinChanger();
            // send banner
            messenger.sendStartBanner(console);
            for (Map.Entry<String, String> plg : versions.entrySet()) {
                if (!checkPluginVersion(plg.getKey(), plg.getValue())) {
                    getLogger().log(Level.SEVERE, "This plugin requires " + plg.getKey() + " to be v" + plg.getValue() + " or higher, disabling...");
                    hasVersion = false;
                    pm.disablePlugin(this);
                    return;
                }
            }
            hasVersion = true;
            worldManager = WorldManager.getWorldManager();
            saveDefaultConfig();
            reloadConfig();
            // load planets config
            tardisCopier.copy("planets.yml");
            planetsConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "planets.yml"));
            loadCustomConfigs();
            // load TARDISChunkGenerator module
            loadHelper();
            // load Multiverse
            loadMultiverse();
            // load worldguard
            loadWorldGuard();
            // add luckperms context
            loadLuckPerms();
            loadLanguage();
            loadSigns();
            loadChameleonGUIs();
            // world loading happens here
            new TARDISConfiguration(this).checkConfig();
            prefix = getConfig().getString("storage.mysql.prefix", "");
            loadDatabase();
            queryFactory = new QueryFactory(this);
            loadInventoryManager();
            new TARDISWorldConfig(this).check();
            TARDISAliasResolver.createAliasMap();
            utils = new TARDISUtils(this);
            locationUtils = new TARDISLocationGetters(this);
            buildKeeper.setSeeds(getSeeds());
            if (getConfig().getString("creation.tips_next", "HIGHEST").equalsIgnoreCase("FREE")) {
                new ResultSetTIPS(this).fillUsedSlotList();
            }
            if (getConfig().getBoolean("preferences.add_server_link")) {
                new HandlesWikiServerLink(this).addServerLink();
            }
            new TARDISConsoleLoader(this).addSchematics();
            new TARDISCustomRotorLoader(this).addRotors();
            new TARDISCustomDoorLoader(this).addDoors();
            loadFiles();
            disguisesOnServer = pm.isPluginEnabled("LibsDisguises");
            generalKeeper = new TARDISGeneralInstanceKeeper(this);
            generalKeeper.setQuotes(quotes());
            try {
                craftingDifficulty = CraftingDifficulty.valueOf(getConfig().getString("difficulty.crafting", "EASY").toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                debug("Could not determine difficulty setting, using EASY");
                craftingDifficulty = CraftingDifficulty.EASY;
            }
            // register recipes
            figura = new TARDISShapedRecipe(this);
            figura.addShapedRecipes();
            incomposita = new TARDISShapelessRecipe(this);
            incomposita.addShapelessRecipes();
            if (getConfig().getBoolean("creation.seed_block.crafting")) {
                obstructionum = new TARDISSeedRecipe(this);
                obstructionum.addSeedRecipes();
            }
            new TARDISSmithingRecipe(this).addSmithingRecipes();
            new TARDISDisplayItemRecipe(this).addDisplayItemRecipes();
            // add listeners
            new TARDISListenerRegisterer(this).registerListeners();
            // register commands
            new TARDISCommandSetter(this).loadCommands();
            loadPluginRespect();
            String mapper = getConfig().getString("mapping.provider", "dynmap");
            if (pm.isPluginEnabled(mapper) && getConfig().getBoolean("modules.mapping")) {
                getMessenger().message(console, TardisModule.TARDIS, "Loading Mapping Module");
                if (mapper.equals("dynmap")) {
                    tardisMapper = new TARDISDynmap(this);
                } else if (mapper.equals("squaremap")) {
                    tardisMapper = new TARDISSquareMap(this);
                } else {
                    tardisMapper = new TARDISBlueMap(this);
                }
                tardisMapper.enable();
            }
            // modules
            new TARDISModuleLoader(this).enable();
            // conversions
            new TARDISConversions(this).convert();
            TARDISBlockLoader bl = new TARDISBlockLoader(this);
            bl.loadGravityWells();
            bl.loadProtectedBlocks();
            if (worldGuardOnServer && getConfig().getBoolean("allow.wg_flag_set")) {
                bl.loadAntiBuild();
            }
            loadBooks();
            // copy advancements to tardis datapack
            new TARDISChecker(this).checkDataPack();
            presets = new TARDISChameleonPreset();
            presets.makePresets();
            // hook CoreProtectAPI
            blockLogger = new TARDISBlockLogger(this);
            if (pm.getPlugin("CoreProtect") != null) {
                debug("Logging block changes with CoreProtect.");
                blockLogger.enableLogger();
            }
            // load the custom map renderer for TARDIS monitor maps
            new SnapshotLoader(this).load();
            // load persistent data
            new TARDISPersister(this).load();
            setDates();
            if (getConfig().getBoolean("allow.perception_filter")) {
                filter = new TARDISPerceptionFilter(this);
                filter.createPerceptionFilter();
            }
            TARDISCondensables cond = new TARDISCondensables(this);
            cond.makeCondensables();
            condensables = cond.getCondensables();
            // start runnable tasks
            new TARDISRunnables(this).start();
            // hook PlaceholderAPI
            if (pm.getPlugin("PlaceholderAPI") != null) {
                debug("Registering expansion with PlaceholderAPI.");
                new TARDISPlaceholderExpansion(this).register();
            }
            getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
                // start bStats metrics delayed so citizens has a chance to load for the dependent plugins chart
                new TARDISStats(this).startMetrics();
            }, 200L);
        } else {
            getLogger().log(Level.SEVERE, "This plugin requires Spigot/Paper " + minVersion + " or higher, disabling...");
            pm.disablePlugin(this);
        }
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        if (id != null) {
            if (id.equalsIgnoreCase("flat")) {
                return new FlatGenerator(this);
            }
            if (id.equalsIgnoreCase("plot")) {
                return new PlotGenerator(this);
            }
            if (id.equalsIgnoreCase("water")) {
                return new WaterGenerator();
            }
            if (id.equalsIgnoreCase("gallifrey")) {
                return new GallifreyGenerator(this);
            }
            if (id.equalsIgnoreCase("siluria")) {
                return new SiluriaGenerator(this);
            }
            if (id.equalsIgnoreCase("skaro")) {
                return new SkaroGenerator(this);
            }
            if (id.equalsIgnoreCase("rooms")) {
                return new RoomGenerator(this);
            }
            return new TARDISChunkGenerator();
        }
        return new TARDISChunkGenerator();
    }

    /**
     * Gets the MySQL database prefix for TARDIS tables
     *
     * @return the prefix from the TARDIS configuration
     */
    public String getPrefix() {
        return prefix;
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
     * Gets the Multiverse plugin utility class
     *
     * @return the utility class
     */
    public TARDISMultiverseHelper getMVHelper() {
        return mvHelper;
    }

    /**
     * Gets the TARDISChunkGenerator helper utility for accessing CraftBukkit and NMS methods
     *
     * @return the helper utility
     */
    public TARDISHelper getTardisHelper() {
        return tardisHelper;
    }

    /**
     * Outputs a message to the console. Requires debug: true in config.yml
     *
     * @param o the Object to print to the console
     */
    public void debug(Object o) {
        if (getConfig().getBoolean("debug")) {
            getMessenger().message(console, TardisModule.DEBUG, "" + o);
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
     * Gets the custom consoles configuration
     *
     * @return the custom consoles configuration
     */
    public FileConfiguration getCustomDoorsConfig() {
        return customDoorsConfig;
    }

    /**
     * Gets the custom consoles configuration
     *
     * @return the custom consoles configuration
     */
    public FileConfiguration getCustomRotorsConfig() {
        return customRotorsConfig;
    }

    /**
     * Gets the flat world configuration
     *
     * @return the flat world configuration
     */
    public FileConfiguration getGeneratorConfig() {
        return generatorConfig;
    }

    /**
     * Gets the shop configuration
     *
     * @return the shop configuration
     */
    public FileConfiguration getShopConfig() {
        return shopConfig;
    }

    /**
     * Gets the monsters configuration
     *
     * @return the monsters configuration
     */
    public FileConfiguration getMonstersConfig() {
        return monstersConfig;
    }

    /**
     * Gets the vortex manipulator configuration
     *
     * @return the vortex manipulator configuration
     */
    public FileConfiguration getVortexConfig() {
        return vortexConfig;
    }

    /**
     * Gets the shop items configuration
     *
     * @return the shop items configuration
     */
    public FileConfiguration getItemsConfig() {
        return itemsConfig;
    }

    /**
     * Gets the shop items configuration
     *
     * @return the shop items configuration
     */
    public FileConfiguration getBlasterConfig() {
        return blasterConfig;
    }

    /**
     * Gets the custom exterior preset models configuration
     *
     * @return the custom models configuration
     */
    public FileConfiguration getCustomModelConfig() {
        return customModelConfig;
    }

    /**
     * Gets the system upgrades configuration
     *
     * @return the system upgrades configuration
     */
    public FileConfiguration getSystemUpgradesConfig() {
        return systemUpgradesConfig;
    }

    /**
     * Gets the regeneration configuration
     *
     * @return the system upgrades configuration
     */
    public FileConfiguration getRegenerationConfig() {
        return regenerationConfig;
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
    public TARDISChatGUI getJsonKeeper() {
        return jsonKeeper;
    }

    /**
     * Gets the TARDIS Skin Changer class
     *
     * @return the TARDIS Skin Changer class
     */
    public SkinChanger getSkinChanger() {
        return skinChanger;
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
     * Sets the TARDIS Artron Energy Standby Task
     */
    public void setStandbyTask(BukkitTask task) {
        standbyTask = task;
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
    public CraftingDifficulty getCraftingDifficulty() {
        return craftingDifficulty;
    }

    /**
     * Sets the TARDIS Difficulty level
     *
     * @param craftingDifficulty the {@link CraftingDifficulty} level to set
     */
    public void setDifficulty(CraftingDifficulty craftingDifficulty) {
        this.craftingDifficulty = craftingDifficulty;
    }

    /**
     * Gets the TARDIS World Manager
     *
     * @return the TARDIS World Manager
     */
    public WorldManager getWorldManager() {
        return worldManager;
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
     * Gets the TARDIS Destroy NamespacedKey
     *
     * @return the TARDIS Destroy NamespacedKey
     */
    public NamespacedKey getDestroyKey() {
        return destroyKey;
    }

    /**
     * Gets the TARDIS Loop NamespacedKey
     *
     * @return the TARDIS Loop NamespacedKey
     */
    public NamespacedKey getLoopKey() {
        return loopKey;
    }

    /**
     * Gets the TARDIS ID NamespacedKey
     *
     * @return the TARDIS ID NamespacedKey
     */
    public NamespacedKey getTardisIdKey() {
        return tardisIdKey;
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
     * Gets the armour stand UUID NamespacedKey
     *
     * @return the armour stand UUID NamespacedKey
     */
    public NamespacedKey getStandUuidKey() {
        return standUuidKey;
    }

    /**
     * Gets the console interaction UUID NamespacedKey
     *
     * @return the console interaction UUID NamespacedKey
     */
    public NamespacedKey getInteractionUuidKey() {
        return interactionUuidKey;
    }

    /**
     * Gets the console model UUID NamespacedKey
     *
     * @return the console model UUID NamespacedKey
     */
    public NamespacedKey getModelUuidKey() {
        return modelUuidKey;
    }

    /**
     * Gets the console interaction unary NamespacedKey
     *
     * @return the console interaction unary NamespacedKey
     */
    public NamespacedKey getUnaryKey() {
        return unaryKey;
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
     * Gets the Sonic Screwdriver Charge NamespacedKey
     *
     * @return the Sonic Screwdriver Charge NamespacedKey
     */
    public NamespacedKey getSonicChargeKey() {
        return sonicChargeKey;
    }

    /**
     * Gets the Microscope NamespacedKey
     *
     * @return the Microscope NamespacedKey
     */
    public NamespacedKey getMicroscopeKey() {
        return microscopeKey;
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

    public TARDISMapper getTardisMapper() {
        return tardisMapper;
    }

    public void setTardisMapper(TARDISMapper tardisMapper) {
        this.tardisMapper = tardisMapper;
    }

    public ShopSettings getShopSettings() {
        return shopSettings;
    }

    public void setShopSettings(ShopSettings settings) {
        this.shopSettings = settings;
    }

    public TVMSettings getTvmSettings() {
        return tvmSettings;
    }

    public void setTvmSettings(TVMSettings tvmSettings) {
        this.tvmSettings = tvmSettings;
    }

    public BlasterSettings getBlasterSettings() {
        return blasterSettings;
    }

    public void setBlasterSettings(BlasterSettings blasterSettings) {
        this.blasterSettings = blasterSettings;
    }

    private ModuleDescriptor.Version getServerVersion(String s) {
        Matcher mat = versionPattern.matcher(s);
        String v = mat.find() ? mat.group(0) : "1.13";
        return ModuleDescriptor.Version.parse(v);
    }

    private boolean checkPluginVersion(String plg, String min) {
        if (pm.isPluginEnabled(plg)) {
            Plugin check = pm.getPlugin(plg);
            ModuleDescriptor.Version minVersion = ModuleDescriptor.Version.parse(min);
            String yamlVersion = check.getDescription().getVersion();
            Matcher matcher = versionPattern.matcher(yamlVersion);
            if (matcher.find()) {
                String pluginVersion = matcher.group(0);
                try {
                    ModuleDescriptor.Version version = ModuleDescriptor.Version.parse(pluginVersion);
                    return (version.compareTo(minVersion) >= 0);
                } catch (IllegalArgumentException e) {
                    debug("Version IllegalArgumentException");
                }
            }
            getLogger().log(Level.WARNING, "TARDIS failed to get the version for {0}.", plg);
            getLogger().log(Level.WARNING, "This could cause issues with enabling the plugin.");
            getLogger().log(Level.WARNING, "Please check you have at least v{0}", min);
            getLogger().log(Level.WARNING, "The invalid version format was {0}", yamlVersion);
        }
        return true;
    }

    /**
     * Sets up the database.
     */
    private void loadDatabase() {
        String databaseType = getConfig().getString("storage.database", "sqlite");
        try {
            if (databaseType.equals("sqlite")) {
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
            getMessenger().message(console, TardisModule.TARDIS, "Connection and Tables Error: " + e);
        }
    }

    /**
     * Closes the database.
     */
    private void closeDatabase() {
        try {
            service.connection.close();
        } catch (SQLException e) {
            getLogger().log(Level.WARNING, "Could not close database connection: " + e);
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
                getMessenger().message(console, TardisModule.TARDIS, "Created language directory.");
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
        getMessenger().message(console, TardisModule.TARDIS, "Loading language: " + Language.valueOf(lang).getLang());
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
        List<String> files = List.of(
                "achievements.yml", "adaptive.yml", "artron.yml",
                "blaster.yml", "blocks.yml",
                "condensables.yml", "custom_consoles.yml", "custom_models.yml", "custom_doors.yml", "custom_time_rotors.yml",
                "generator.yml",
                "handles.yml",
                "items.yml",
                "kits.yml",
                "monsters.yml",
                "regeneration.yml", "rooms.yml",
                "shop.yml", "system_upgrades.yml",
                "tag.yml",
                "vortex_manipulator.yml"
        );
        for (String f : files) {
//            debug(f);
            tardisCopier.copy(f);
            tardisCopier.copy(f);
        }
        new TARDISPlanetsUpdater(this, planetsConfig).checkPlanetsConfig();
        roomsConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "rooms.yml"));
        new TARDISRoomsUpdater(this, roomsConfig).checkRoomsConfig();
        artronConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "artron.yml"));
        new TARDISArtronUpdater(this).checkArtronConfig();
        blocksConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "blocks.yml"));
        new TARDISBlocksUpdater(this, blocksConfig).checkBlocksConfig();
        condensablesConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "condensables.yml"));
        new TARDISCondensablesUpdater(this).checkCondensables();
        customConsolesConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "custom_consoles.yml"));
        customDoorsConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "custom_doors.yml"));
        customRotorsConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "custom_time_rotors.yml"));
        kitsConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "kits.yml"));
        new TARDISKitsUpdater(this, kitsConfig).checkKits();
        achievementConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "achievements.yml"));
        tagConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "tag.yml"));
        handlesConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "handles.yml"));
        new TARDISHandlesUpdater(this, handlesConfig).checkHandles();
        adaptiveConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "adaptive.yml"));
        new TARDISAdaptiveUpdater(this).checkBiomesConfig();
        generatorConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "generator.yml"));
        new TARDISGeneratorUpdater(this, generatorConfig).checkTrees();
        if (getConfig().getBoolean("modules.weeping_angels")) {
            monstersConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "monsters.yml"));
        }
        if (getConfig().getBoolean("modules.shop")) {
            shopConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "shop.yml"));
            itemsConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "items.yml"));
        }
        if (getConfig().getBoolean("modules.vortex_manipulator")) {
            vortexConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "vortex_manipulator.yml"));
        }
        if (getConfig().getBoolean("modules.sonic_blaster")) {
            blasterConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "blaster.yml"));
        }
        customModelConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "custom_models.yml"));
        systemUpgradesConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "system_upgrades.yml"));
        new TARDISSysUpsUpdater(this).checkSystemUpgradesConfig();
        if (getConfig().getBoolean("modules.regeneration")) {
            regenerationConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "regeneration.yml"));
            new TARDISRegenerationUpdater(this).checkConfig();
        }
    }

    /**
     * Builds the schematics used to create TARDISes and rooms. Also loads the quotes from the quotes file.
     */
    private void loadFiles() {
        tardisCopier.copyRoomTemplateFile();
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
                getMessenger().message(console, TardisModule.TARDIS, "Created books directory.");
            }
        }
        Set<String> bookNames = achievementConfig.getKeys(false);
        bookNames.forEach((b) -> TARDISFileCopier.copy(getDataFolder() + File.separator + "books" + File.separator + b + ".txt", getResource("books/" + b + ".txt"), false));
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
     * If the LuckPerms plugin is available, load a custom permissions context.
     */
    private void loadLuckPerms() {
        if (pm.getPlugin("LuckPerms") != null) {
            debug("Loading LuckPerms `is-flying-tardis` context");
            new TARDISContexts().register();
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
     * Checks if the Multiverse-Core plugin is available, and loads support if it is.
     */
    private void loadMultiverse() {
        if (worldManager.equals(WorldManager.MULTIVERSE)) {
            debug("Hooking into Multiverse-Core!");
            mvHelper = new TARDISMultiverseHelper();
        }
    }

    /**
     * Loads the TARDISChunkGenerator support module
     */
    private void loadHelper() {
        debug("Loading Helper module!");
        tardisHelper = new TARDISHelper(this);
        tardisHelper.enable();
        new EyeLoader(this).enable();
        new VehicleLoader().injectEntity();
    }

    private void loadPluginRespect() {
        pluginRespect = new TARDISPluginRespect(this);
        pluginRespect.loadTowny();
        pluginRespect.loadChunkyBorder();
        pluginRespect.loadGriefPrevention();
        pluginRespect.checkWorldGuardEntry();
    }

    /**
     * Loads the quotes from a text file.
     *
     * @return an ArrayList of quotes
     */
    private ArrayList<String> quotes() {
        ArrayList<String> quotes = new ArrayList<>();
        if (quotesfile != null) {
            try (BufferedReader bufRdr = new BufferedReader(new FileReader(quotesfile))) {
                String line;
                //read each line of text file
                while ((line = bufRdr.readLine()) != null) {
                    quotes.add(line);
                }
                if (quotes.isEmpty()) {
                    quotes.add("");
                }
            } catch (IOException io) {
                getLogger().log(Level.WARNING, "Could not read quotes file");
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
        String it = getTagConfig().getString("it", "");
        if (!it.isEmpty()) {
            HashMap<String, Object> set = new HashMap<>();
            set.put("player", getTagConfig().getString("it"));
            long time = System.currentTimeMillis() - getTagConfig().getLong("time");
            set.put("time", time);
            getQueryFactory().doSyncInsert("tag", set);
        }
    }
}
