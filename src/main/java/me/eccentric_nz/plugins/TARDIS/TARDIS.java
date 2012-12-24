package me.eccentric_nz.plugins.TARDIS;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TARDIS extends JavaPlugin {

    public ImprovedOfflinePlayer_api iopHandler;
    TARDISDatabase service = TARDISDatabase.getInstance();
    public PluginDescriptionFile pdfFile;
    public FileConfiguration config = null;
    public File schematicfile = null;
    public File budgetschematicfile = null;
    public File biggerschematicfile = null;
    public File deluxeschematicfile = null;
    public File budgetschematiccsv = null;
    public File biggerschematiccsv = null;
    public File deluxeschematiccsv = null;
    public File myconfigfile = null;
    public File quotesfile = null;
    private TARDISCommands tardisCommand;
    private TARDISAdminCommands tardisAdminCommand;
    private TARDISPrefsCommands tardisPrefsCommand;
    private TARDISTravelCommands tardisTravelCommand;
    private TARDISAreaCommands tardisAreaCommand;
    public String[][][] budgetschematic;
    public String[][][] biggerschematic;
    public String[][][] deluxeschematic;
    public short[] budgetdimensions = new short[3];
    public short[] biggerdimensions = new short[3];
    public short[] deluxedimensions = new short[3];
    protected static TARDIS plugin;
    TARDISUtils utils = new TARDISUtils(this);
    TARDISBuilder builder = new TARDISBuilder(this);
    TARDISDestroyer destroyer = new TARDISDestroyer(this);
    TARDISArea ta = new TARDISArea(this);
    TARDISWorldGuardChecker wgchk;
    TARDISBlockPlaceListener tardisBlockPlaceListener = new TARDISBlockPlaceListener(this);
    TARDISBlockBreakListener tardisBlockBreakListener = new TARDISBlockBreakListener(this);
    TARDISPlayerListener tardisPlayerListener = new TARDISPlayerListener(this);
    TARDISBlockProtectListener tardisProtectListener = new TARDISBlockProtectListener(this);
    TARDISBlockDamageListener tardisDamageListener = new TARDISBlockDamageListener();
    TARDISExplosionListener tardisExplosionListener = new TARDISExplosionListener(this);
    TARDISWitherDragonListener tardisWDBlocker = new TARDISWitherDragonListener();
    PluginManager pm = Bukkit.getServer().getPluginManager();
    public HashMap<String, String> trackPlayers = new HashMap<String, String>();
    public HashMap<String, String> trackName = new HashMap<String, String>();
    public HashMap<String, String> trackBlock = new HashMap<String, String>();
    public HashMap<String, String> trackEnd = new HashMap<String, String>();
    public HashMap<String, String> trackPerm = new HashMap<String, String>();
    public HashMap<String, String> trackDest = new HashMap<String, String>();
    public HashMap<Integer, Integer> trackTravellers = new HashMap<Integer, Integer>();
    private static ArrayList<String> quotes = new ArrayList<String>();
    public ArrayList<String> quote;
    public int quotelen;
    public boolean worldGuardOnServer = false;

    @Override
    public void onEnable() {
        String packageName = this.getServer().getClass().getPackage().getName();
        // Get full package string of CraftServer.
        // org.bukkit.craftbukkit.versionstring (or for pre-refactor, just org.bukkit.craftbukkit
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        // Get the last element of the package
        if (version.equals("craftbukkit")) { // If the last element of the package was "craftbukkit" we are now pre-refactor
            version = "pre";
        }
        try {
            final Class<?> clazz = Class.forName("me.eccentric_nz.plugins.TARDIS.ImprovedOfflinePlayer_" + version);
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
        this.getLogger().log(Level.INFO, "Loading support for CB {0}", version);

        pdfFile = getDescription();
        Constants.MY_PLUGIN_NAME = ChatColor.GRAY + "[" + pdfFile.getName() + "]" + ChatColor.RESET;
        plugin = this;
        pm.registerEvents(tardisBlockPlaceListener, this);
        pm.registerEvents(tardisBlockBreakListener, this);
        pm.registerEvents(tardisPlayerListener, this);
        pm.registerEvents(tardisProtectListener, this);
        pm.registerEvents(tardisDamageListener, this);
        pm.registerEvents(tardisExplosionListener, this);
        pm.registerEvents(tardisWDBlocker, this);

        if (!getDataFolder().exists()) {
            if (!getDataFolder().mkdir()) {
                System.err.println(Constants.MY_PLUGIN_NAME + " could not create directory!");
                System.out.println(Constants.MY_PLUGIN_NAME + " requires you to manually make the TARDIS/ directory!");
            }
            getDataFolder().setWritable(true);
            getDataFolder().setExecutable(true);
        }

        try {
            String path = getDataFolder() + File.separator + "TARDIS.db";
            service.setConnection(path);
            service.createTables();
        } catch (Exception e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Connection and Tables Error: " + e);
        }

        TARDISSchematicReader reader = new TARDISSchematicReader(plugin);
        String budnstr = getDataFolder() + File.separator + Constants.SCHEMATIC_BUDGET;
        budgetschematicfile = new File(budnstr);
        if (!budgetschematicfile.exists()) {
            copy(getResource(Constants.SCHEMATIC_BUDGET), budgetschematicfile);
        }
        String bignstr = getDataFolder() + File.separator + Constants.SCHEMATIC_BIGGER;
        biggerschematicfile = new File(bignstr);
        if (!biggerschematicfile.exists()) {
            copy(getResource(Constants.SCHEMATIC_BIGGER), biggerschematicfile);
        }
        String delnstr = getDataFolder() + File.separator + Constants.SCHEMATIC_DELUXE;
        deluxeschematicfile = new File(delnstr);
        if (!deluxeschematicfile.exists()) {
            copy(getResource(Constants.SCHEMATIC_DELUXE), deluxeschematicfile);
        }
        reader.main(budnstr, Constants.SCHEMATIC.BUDGET);
        reader.main(bignstr, Constants.SCHEMATIC.BIGGER);
        reader.main(delnstr, Constants.SCHEMATIC.DELUXE);

        if (config == null) {
            loadConfig();
        }
        Constants.TARDIS_KEY = config.getString("key");
        if (!config.contains("the_end")) {
            config.set("the_end", false);
        }
        if (!config.contains("nether")) {
            config.set("nether", false);
        }
        if (!config.contains("debug")) {
            config.set("debug", false);
        }
        if (!config.contains("use_worldguard")) {
            config.set("use_worldguard", true);
        }
        if (!config.contains("respect_worldguard")) {
            config.set("respect_worldguard", true);
        }
        if (!config.contains("land_on_water")) {
            config.set("land_on_water", false);
        }
        if (!config.contains("timeout")) {
            config.set("timeout", 5);
        }
        if (!config.contains("timeout_height")) {
            config.set("timeout_height", 135);
        }
        if (!config.contains("name_tardis")) {
            config.set("name_tardis", false);
        }

        tardisCommand = new TARDISCommands(this);
        tardisAdminCommand = new TARDISAdminCommands(this);
        tardisPrefsCommand = new TARDISPrefsCommands();
        tardisTravelCommand = new TARDISTravelCommands(this);
        tardisAreaCommand = new TARDISAreaCommands(this);
        getCommand("tardis").setExecutor(tardisCommand);
        getCommand("tardisadmin").setExecutor(tardisAdminCommand);
        getCommand("tardisprefs").setExecutor(tardisPrefsCommand);
        getCommand("tardistravel").setExecutor(tardisTravelCommand);
        getCommand("tardisarea").setExecutor(tardisAreaCommand);

        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
        quote = quotes();
        quotelen = quote.size();

        if (plugin.getServer().getPluginManager().getPlugin("Spout") != null && config.getBoolean("sfx")) {
            this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    TARDISSounds.randomTARDISSound();
                }
            }, 60L, 1200L);
        }
        if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            worldGuardOnServer = true;
            wgchk = new TARDISWorldGuardChecker(this);
        }
    }

    @Override
    public void onDisable() {
        saveCustomConfig();
        try {
            service.connection.close();
        } catch (Exception e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Could not close database connection: " + e);
        }
    }

    public FileConfiguration loadConfig() {
        try {
            budgetschematiccsv = new File(getDataFolder(), Constants.SCHEMATIC_BUDGET + ".csv");
            biggerschematiccsv = new File(getDataFolder(), Constants.SCHEMATIC_BIGGER + ".csv");
            deluxeschematiccsv = new File(getDataFolder(), Constants.SCHEMATIC_DELUXE + ".csv");
            budgetschematic = Schematic.schematic(budgetschematiccsv, budgetdimensions[0], budgetdimensions[1], budgetdimensions[2]);
            biggerschematic = Schematic.schematic(biggerschematiccsv, biggerdimensions[0], biggerdimensions[1], biggerdimensions[2]);
            deluxeschematic = Schematic.schematic(deluxeschematiccsv, deluxedimensions[0], deluxedimensions[1], deluxedimensions[2]);

            myconfigfile = new File(getDataFolder(), Constants.CONFIG_FILE_NAME);
            if (!myconfigfile.exists()) {
                // load the default values into file
                copy(getResource(Constants.CONFIG_FILE_NAME), myconfigfile);
            }
            quotesfile = new File(getDataFolder(), Constants.QUOTES_FILE_NAME);
            if (!quotesfile.exists()) {
                copy(getResource(Constants.QUOTES_FILE_NAME), quotesfile);
            }
        } catch (Exception e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " failed to retrieve files from directory. Using defaults.");
        }
        config = YamlConfiguration.loadConfiguration(myconfigfile);

        // add worlds
        List<World> worlds = this.getServer().getWorlds();
        for (World w : worlds) {
            String worldname = "worlds." + w.getName();
            if (!config.contains(worldname)) {
                config.set(worldname, true);
                System.out.println(Constants.MY_PLUGIN_NAME + " Added '" + w.getName() + "' to config. To exclude this world run: /tardis admin exclude " + w.getName());
            }
        }
        // now remove worlds that may have been deleted
        Set<String> cWorlds = config.getConfigurationSection("worlds").getKeys(true);
        for (String cw : cWorlds) {
            if (getServer().getWorld(cw) == null) {
                config.set("worlds." + cw, null);
                System.out.println(Constants.MY_PLUGIN_NAME + " Removed '" + cw + " from config.yml");
            }
        }
        saveCustomConfig();
        return config;
    }

    private void copy(InputStream in, File file) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file, false);
            byte[] buf = new byte[1024];
            int len;
            try {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } catch (IOException io) {
                System.err.println(Constants.MY_PLUGIN_NAME + " could not save the config file.");
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " File not found.");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public void saveCustomConfig() {
        if (config == null || myconfigfile == null) {
            return;
        }
        try {
            config.save(myconfigfile);
        } catch (IOException ex) {
            System.err.println(Constants.MY_PLUGIN_NAME + "Could not save config to " + myconfigfile);
        }
    }

    public ArrayList<String> quotes() {
        // load quotes from txt file
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
                System.err.println(Constants.MY_PLUGIN_NAME + " Could not read quotes file");
            } finally {
                if (bufRdr != null) {
                    try {
                        bufRdr.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        return quotes;
    }

    public void debug(Object o) {
        if (config.getBoolean("debug") == true) {
            System.out.println(Constants.MY_PLUGIN_NAME + " " + o);
        }
    }
}
