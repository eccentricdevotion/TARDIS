package me.eccentric_nz.TARDIS;

import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.travel.TARDISArea;
import me.eccentric_nz.TARDIS.thirdparty.ImprovedOfflinePlayer_api;
import me.eccentric_nz.TARDIS.thirdparty.MetricsLite;
import me.eccentric_nz.TARDIS.utility.TARDISUtils;
import me.eccentric_nz.TARDIS.utility.TARDISWorldGuardChecker;
import me.eccentric_nz.TARDIS.files.TARDISConfiguration;
import me.eccentric_nz.TARDIS.files.TARDISSchematic;
import me.eccentric_nz.TARDIS.files.TARDISSchematicReader;
import me.eccentric_nz.TARDIS.commands.TARDISPrefsCommands;
import me.eccentric_nz.TARDIS.commands.TARDISCommands;
import me.eccentric_nz.TARDIS.commands.TARDISAreaCommands;
import me.eccentric_nz.TARDIS.commands.TARDISTravelCommands;
import me.eccentric_nz.TARDIS.commands.TARDISAdminCommands;
import me.eccentric_nz.TARDIS.destroyers.TARDISDestroyer;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderInner;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderOuter;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockProtectListener;
import me.eccentric_nz.TARDIS.listeners.TARDISExplosionListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockPlaceListener;
import me.eccentric_nz.TARDIS.listeners.TARDISWitherDragonListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockBreakListener;
import me.eccentric_nz.TARDIS.listeners.TARDISBlockDamageListener;
import me.eccentric_nz.TARDIS.listeners.TARDISPlayerListener;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TARDIS extends JavaPlugin {

    public ImprovedOfflinePlayer_api iopHandler;
    TARDISDatabase service = TARDISDatabase.getInstance();
    public PluginDescriptionFile pdfFile;
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
    public static TARDIS plugin;
    public TARDISUtils utils = new TARDISUtils(this);
    public TARDISBuilderInner inner = new TARDISBuilderInner(this);
    public TARDISBuilderOuter outer = new TARDISBuilderOuter(this);
    public TARDISDestroyer destroyer = new TARDISDestroyer(this);
    public TARDISArea ta = new TARDISArea(this);
    public TARDISWorldGuardChecker wgchk;
    TARDISBlockPlaceListener tardisBlockPlaceListener = new TARDISBlockPlaceListener(this);
    TARDISBlockBreakListener tardisBlockBreakListener = new TARDISBlockBreakListener(this);
    TARDISPlayerListener tardisPlayerListener = new TARDISPlayerListener(this);
    TARDISBlockProtectListener tardisProtectListener = new TARDISBlockProtectListener(this);
    TARDISBlockDamageListener tardisDamageListener = new TARDISBlockDamageListener(this);
    TARDISExplosionListener tardisExplosionListener = new TARDISExplosionListener(this);
    TARDISWitherDragonListener tardisWDBlocker = new TARDISWitherDragonListener(this);
    public PluginManager pm = Bukkit.getServer().getPluginManager();
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
    public ConsoleCommandSender console;
    public String pluginName;
    public String TARDIS_KEY;

    @Override
    public void onEnable() {
        pdfFile = getDescription();
        pluginName = ChatColor.GOLD + "[" + pdfFile.getName() + "]" + ChatColor.RESET;
        plugin = this;
        console = getServer().getConsoleSender();

        loadClasses();
        saveDefaultConfig();
        TARDISConfiguration tc = new TARDISConfiguration(this);
        tc.checkConfig();
        loadDatabase();
        loadFiles();
        registerListeners();
        loadCommands();
        loadMetrics();
        startSound();
        loadWorldGuard();

        TARDIS_KEY = getConfig().getString("key");
        quote = quotes();
        quotelen = quote.size();
    }

    @Override
    public void onDisable() {
        saveConfig();
        closeDatabase();
    }

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

    private void loadDatabase() {
        try {
            String path = getDataFolder() + File.separator + "TARDIS.db";
            service.setConnection(path);
            service.createTables();
        } catch (Exception e) {
            console.sendMessage(pluginName + " Connection and Tables Error: " + e);
        }
    }

    private void closeDatabase() {
        try {
            service.connection.close();
        } catch (Exception e) {
            console.sendMessage(pluginName + " Could not close database connection: " + e);
        }
    }

    private void registerListeners() {
        pm.registerEvents(tardisBlockPlaceListener, this);
        pm.registerEvents(tardisBlockBreakListener, this);
        pm.registerEvents(tardisPlayerListener, this);
        pm.registerEvents(tardisProtectListener, this);
        pm.registerEvents(tardisDamageListener, this);
        pm.registerEvents(tardisExplosionListener, this);
        pm.registerEvents(tardisWDBlocker, this);
    }

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

    private void loadFiles() {
        try {
            // load csv files - create them if they don't exist
            budgetschematiccsv = createFile(TARDISConstants.SCHEMATIC_BUDGET + ".csv");
            biggerschematiccsv = createFile(TARDISConstants.SCHEMATIC_BIGGER + ".csv");
            deluxeschematiccsv = createFile(TARDISConstants.SCHEMATIC_DELUXE + ".csv");
            TARDISSchematicReader reader = new TARDISSchematicReader(plugin);
            // load schematic files - copy the defaults if they don't exist
            String budnstr = getDataFolder() + File.separator + TARDISConstants.SCHEMATIC_BUDGET;
            budgetschematicfile = new File(budnstr);
            if (!budgetschematicfile.exists()) {
                copy(getResource(TARDISConstants.SCHEMATIC_BUDGET), budgetschematicfile);
            }
            String bignstr = getDataFolder() + File.separator + TARDISConstants.SCHEMATIC_BIGGER;
            biggerschematicfile = new File(bignstr);
            if (!biggerschematicfile.exists()) {
                copy(getResource(TARDISConstants.SCHEMATIC_BIGGER), biggerschematicfile);
            }
            String delnstr = getDataFolder() + File.separator + TARDISConstants.SCHEMATIC_DELUXE;
            deluxeschematicfile = new File(delnstr);
            if (!deluxeschematicfile.exists()) {
                copy(getResource(TARDISConstants.SCHEMATIC_DELUXE), deluxeschematicfile);
            }
            // read the schematics
            reader.main(budnstr, TARDISConstants.SCHEMATIC.BUDGET);
            reader.main(bignstr, TARDISConstants.SCHEMATIC.BIGGER);
            reader.main(delnstr, TARDISConstants.SCHEMATIC.DELUXE);
            // load the schematic data into the csv files
            budgetschematic = TARDISSchematic.schematic(budgetschematiccsv, budgetdimensions[0], budgetdimensions[1], budgetdimensions[2]);
            biggerschematic = TARDISSchematic.schematic(biggerschematiccsv, biggerdimensions[0], biggerdimensions[1], biggerdimensions[2]);
            deluxeschematic = TARDISSchematic.schematic(deluxeschematiccsv, deluxedimensions[0], deluxedimensions[1], deluxedimensions[2]);

            quotesfile = new File(getDataFolder(), TARDISConstants.QUOTES_FILE_NAME);
            if (!quotesfile.exists()) {
                copy(getResource(TARDISConstants.QUOTES_FILE_NAME), quotesfile);
            }
        } catch (Exception e) {
            console.sendMessage(pluginName + " failed to retrieve files from directory. Using defaults.");
        }
    }

    private void loadMetrics() {
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
    }

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

    private void loadWorldGuard() {
        if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            worldGuardOnServer = true;
            wgchk = new TARDISWorldGuardChecker(this);
        }
    }

    private File createFile(String filename) {
        File file = new File(getDataFolder(), filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException io) {
                console.sendMessage(pluginName + " " + filename + " could not be created! " + io.getMessage());
            }
        }
        return file;
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
                console.sendMessage(pluginName + " could not save the config file.");
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                    }
                }
            }
        } catch (FileNotFoundException e) {
            console.sendMessage(pluginName + " File not found.");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
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
                console.sendMessage(pluginName + " Could not read quotes file");
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
        if (getConfig().getBoolean("debug") == true) {
            console.sendMessage(pluginName + " " + o);
        }
    }
}
