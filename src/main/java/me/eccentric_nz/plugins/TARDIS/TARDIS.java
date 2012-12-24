package me.eccentric_nz.plugins.TARDIS;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.PersistenceException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TARDIS extends JavaPlugin {

    public ImprovedOfflinePlayer_api iopHandler;
//    TARDISDatabase service = TARDISDatabase.getInstance();
    public PluginDescriptionFile pdfFile;
//    public FileConfiguration config = null;
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
    public String MY_PLUGIN_NAME;
    TARDISUtils utils = new TARDISUtils(this);
    TARDISBuilder builder = new TARDISBuilder(this);
    TARDISDestroyer destroyer = new TARDISDestroyer(this);
    TARDISArea ta = new TARDISArea(this);
    TARDISWorldGuardChecker wgchk;
    TARDISBlockPlaceListener tardisBlockPlaceListener = new TARDISBlockPlaceListener(this);
    TARDISBlockBreakListener tardisBlockBreakListener = new TARDISBlockBreakListener(this);
    TARDISPlayerListener tardisPlayerListener = new TARDISPlayerListener(this);
    TARDISBlockProtectListener tardisProtectListener = new TARDISBlockProtectListener(this);
    TARDISBlockDamageListener tardisDamageListener = new TARDISBlockDamageListener(this);
    TARDISExplosionListener tardisExplosionListener = new TARDISExplosionListener(this);
    TARDISWitherDragonListener tardisWDBlocker = new TARDISWitherDragonListener(this);
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
    public ConsoleCommandSender console;

    @Override
    public void onEnable() {
        loadNMSClasses();
        saveDefaultConfig();

        plugin = this;
        console = getServer().getConsoleSender();
        pdfFile = getDescription();
        MY_PLUGIN_NAME = ChatColor.GRAY + "[" + pdfFile.getName() + "]" + ChatColor.RESET;

        setupDatabase();
        registerEvents();
        loadFiles();
        loadCommands();
        TARDISConfiguration tc = new TARDISConfiguration(this);
        tc.checkConfig();
        loadMetrics();

        Constants.TARDIS_KEY = getConfig().getString("key");

        if (plugin.getServer().getPluginManager().getPlugin("Spout") != null && getConfig().getBoolean("sfx")) {
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
        saveConfig();
    }

    private void loadNMSClasses() {
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
    }

    private void setupDatabase() {
        try {
            getDatabase().find(eBeanTardis.class).findRowCount();
        } catch (PersistenceException ex) {
            System.out.println("Installing database for " + getDescription().getName() + " due to first time usage");
            installDDL();
        }
    }

    private void registerEvents() {
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

    public void loadFiles() {
        try {
            budgetschematiccsv = createFile(Constants.SCHEMATIC_BUDGET + ".csv");
            biggerschematiccsv = createFile(Constants.SCHEMATIC_BIGGER + ".csv");
            deluxeschematiccsv = createFile(Constants.SCHEMATIC_DELUXE + ".csv");
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
            budgetschematic = Schematic.schematic(budgetschematiccsv, budgetdimensions[0], budgetdimensions[1], budgetdimensions[2]);
            biggerschematic = Schematic.schematic(biggerschematiccsv, biggerdimensions[0], biggerdimensions[1], biggerdimensions[2]);
            deluxeschematic = Schematic.schematic(deluxeschematiccsv, deluxedimensions[0], deluxedimensions[1], deluxedimensions[2]);
            TARDISSchematicReader reader = new TARDISSchematicReader(plugin);
            reader.main(budnstr, Constants.SCHEMATIC.BUDGET);
            reader.main(bignstr, Constants.SCHEMATIC.BIGGER);
            reader.main(delnstr, Constants.SCHEMATIC.DELUXE);

            quotesfile = new File(getDataFolder(), Constants.QUOTES_FILE_NAME);
            if (!quotesfile.exists()) {
                copy(getResource(Constants.QUOTES_FILE_NAME), quotesfile);
            }
            quote = quotes();
            quotelen = quote.size();
        } catch (Exception e) {
            console.sendMessage(MY_PLUGIN_NAME + " failed to retrieve files from directory. Using defaults.");
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

    public File createFile(String filename) {
        File file = new File(getDataFolder(), filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException io) {
                console.sendMessage(MY_PLUGIN_NAME + "Error creating file! " + io.getMessage());
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
                console.sendMessage(MY_PLUGIN_NAME + " could not save the defaults to file.");
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                    }
                }
            }
        } catch (FileNotFoundException e) {
            console.sendMessage(MY_PLUGIN_NAME + " File not found.");
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
                console.sendMessage(MY_PLUGIN_NAME + " Could not read quotes file");
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
            console.sendMessage(MY_PLUGIN_NAME + " " + o);
        }
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(eBeanTardis.class);
        return list;
    }
}
