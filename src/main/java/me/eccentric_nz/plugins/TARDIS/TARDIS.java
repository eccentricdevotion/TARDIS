package me.eccentric_nz.plugins.TARDIS;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TARDIS extends JavaPlugin implements Listener {

    TARDISdatabase service = TARDISdatabase.getInstance();
    public PluginDescriptionFile pdfFile;
    public FileConfiguration config = null;
    public File schematicfile = null;
    public File budgetNschematicfile = null;
    public File budgetWschematicfile = null;
    public File budgetSschematicfile = null;
    public File budgetEschematicfile = null;
    public File biggerNschematicfile = null;
    public File biggerWschematicfile = null;
    public File biggerSschematicfile = null;
    public File biggerEschematicfile = null;
    public File deluxeNschematicfile = null;
    public File deluxeWschematicfile = null;
    public File deluxeSschematicfile = null;
    public File deluxeEschematicfile = null;
    public File budgetNschematiccsv = null;
    public File budgetWschematiccsv = null;
    public File budgetSschematiccsv = null;
    public File budgetEschematiccsv = null;
    public File biggerNschematiccsv = null;
    public File biggerWschematiccsv = null;
    public File biggerSschematiccsv = null;
    public File biggerEschematiccsv = null;
    public File deluxeNschematiccsv = null;
    public File deluxeWschematiccsv = null;
    public File deluxeSschematiccsv = null;
    public File deluxeEschematiccsv = null;
    public File myconfigfile = null;
    public File timelordsfile = null;
    public File quotesfile = null;
    private TARDISexecutor tardisExecutor;
    public String[][][] budgetschematic_NORTH;
    public String[][][] budgetschematic_WEST;
    public String[][][] budgetschematic_SOUTH;
    public String[][][] budgetschematic_EAST;
    public String[][][] biggerschematic_NORTH;
    public String[][][] biggerschematic_WEST;
    public String[][][] biggerschematic_SOUTH;
    public String[][][] biggerschematic_EAST;
    public String[][][] deluxeschematic_NORTH;
    public String[][][] deluxeschematic_WEST;
    public String[][][] deluxeschematic_SOUTH;
    public String[][][] deluxeschematic_EAST;
    public short[] budgetdimensions = new short[3];
    public short[] biggerdimensions = new short[3];
    public short[] deluxedimensions = new short[3];
    protected static TARDIS plugin;
    TARDISBlockPlaceListener tardisBlockPlaceListener = new TARDISBlockPlaceListener(this);
    TARDISBlockBreakListener tardisBlockBreakListener = new TARDISBlockBreakListener(this);
    TARDISPlayerListener tardisPlayerListener = new TARDISPlayerListener(this);
    TARDISBlockProtectListener tardisProtectListener = new TARDISBlockProtectListener(this);
    TARDISBlockDamageListener tardisDamageListener = new TARDISBlockDamageListener(this);
    PluginManager pm = Bukkit.getServer().getPluginManager();
    public HashMap<String, String> trackPlayers = new HashMap<String, String>();
    private static ArrayList<String> quotes = new ArrayList<String>();
    public ArrayList<String> quote;
    public int quotelen;

    @Override
    public void onEnable() {
        plugin = this;
        pm.registerEvents(tardisBlockPlaceListener, this);
        pm.registerEvents(tardisBlockBreakListener, this);
        pm.registerEvents(tardisPlayerListener, this);
        pm.registerEvents(tardisProtectListener, this);
        pm.registerEvents(tardisDamageListener, this);

        pdfFile = getDescription();
        Constants.MY_PLUGIN_NAME = "[" + pdfFile.getName() + "]";

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
        String budnstr = getDataFolder() + File.separator + Constants.SCHEMATIC_NORTH_BUDGET;
        budgetNschematicfile = new File(budnstr);
        if (!budgetNschematicfile.exists()) {
            copy(getResource(Constants.SCHEMATIC_NORTH_BUDGET), budgetNschematicfile);
        }
        String budwstr = getDataFolder() + File.separator + Constants.SCHEMATIC_WEST_BUDGET;
        budgetWschematicfile = new File(budwstr);
        if (!budgetWschematicfile.exists()) {
            copy(getResource(Constants.SCHEMATIC_WEST_BUDGET), budgetWschematicfile);
        }
        String budsstr = getDataFolder() + File.separator + Constants.SCHEMATIC_SOUTH_BUDGET;
        budgetSschematicfile = new File(budsstr);
        if (!budgetSschematicfile.exists()) {
            copy(getResource(Constants.SCHEMATIC_SOUTH_BUDGET), budgetSschematicfile);
        }
        String budestr = getDataFolder() + File.separator + Constants.SCHEMATIC_EAST_BUDGET;
        budgetEschematicfile = new File(budestr);
        if (!budgetEschematicfile.exists()) {
            copy(getResource(Constants.SCHEMATIC_EAST_BUDGET), budgetEschematicfile);
        }
        String bignstr = getDataFolder() + File.separator + Constants.SCHEMATIC_NORTH_BIGGER;
        biggerNschematicfile = new File(bignstr);
        if (!biggerNschematicfile.exists()) {
            copy(getResource(Constants.SCHEMATIC_NORTH_BIGGER), biggerNschematicfile);
        }
        String bigwstr = getDataFolder() + File.separator + Constants.SCHEMATIC_WEST_BIGGER;
        biggerWschematicfile = new File(bigwstr);
        if (!biggerWschematicfile.exists()) {
            copy(getResource(Constants.SCHEMATIC_WEST_BIGGER), biggerWschematicfile);
        }
        String bigsstr = getDataFolder() + File.separator + Constants.SCHEMATIC_SOUTH_BIGGER;
        biggerSschematicfile = new File(bigsstr);
        if (!biggerSschematicfile.exists()) {
            copy(getResource(Constants.SCHEMATIC_SOUTH_BIGGER), biggerSschematicfile);
        }
        String bigestr = getDataFolder() + File.separator + Constants.SCHEMATIC_EAST_BIGGER;
        biggerEschematicfile = new File(bigestr);
        if (!biggerEschematicfile.exists()) {
            copy(getResource(Constants.SCHEMATIC_EAST_BIGGER), biggerEschematicfile);
        }
        String delnstr = getDataFolder() + File.separator + Constants.SCHEMATIC_NORTH_DELUXE;
        deluxeNschematicfile = new File(delnstr);
        if (!deluxeNschematicfile.exists()) {
            copy(getResource(Constants.SCHEMATIC_NORTH_DELUXE), deluxeNschematicfile);
        }
        String delwstr = getDataFolder() + File.separator + Constants.SCHEMATIC_WEST_DELUXE;
        deluxeWschematicfile = new File(delwstr);
        if (!deluxeWschematicfile.exists()) {
            copy(getResource(Constants.SCHEMATIC_WEST_DELUXE), deluxeWschematicfile);
        }
        String delsstr = getDataFolder() + File.separator + Constants.SCHEMATIC_SOUTH_DELUXE;
        deluxeSschematicfile = new File(delsstr);
        if (!deluxeSschematicfile.exists()) {
            copy(getResource(Constants.SCHEMATIC_SOUTH_DELUXE), deluxeSschematicfile);
        }
        String delestr = getDataFolder() + File.separator + Constants.SCHEMATIC_EAST_DELUXE;
        deluxeEschematicfile = new File(delestr);
        if (!deluxeEschematicfile.exists()) {
            copy(getResource(Constants.SCHEMATIC_EAST_DELUXE), deluxeEschematicfile);
        }
        reader.main(budnstr, Constants.SCHEMATIC.BUDGET);
        reader.main(budwstr, Constants.SCHEMATIC.BUDGET);
        reader.main(budsstr, Constants.SCHEMATIC.BUDGET);
        reader.main(budestr, Constants.SCHEMATIC.BUDGET);
        reader.main(bignstr, Constants.SCHEMATIC.BIGGER);
        reader.main(bigwstr, Constants.SCHEMATIC.BIGGER);
        reader.main(bigsstr, Constants.SCHEMATIC.BIGGER);
        reader.main(bigestr, Constants.SCHEMATIC.BIGGER);
        reader.main(delnstr, Constants.SCHEMATIC.DELUXE);
        reader.main(delwstr, Constants.SCHEMATIC.DELUXE);
        reader.main(delsstr, Constants.SCHEMATIC.DELUXE);
        reader.main(delestr, Constants.SCHEMATIC.DELUXE);

        if (config == null) {
            loadConfig();
        }
        Constants.TARDIS_KEY = config.getString("key");

        tardisExecutor = new TARDISexecutor(this);
        getCommand("TARDIS").setExecutor(tardisExecutor);

        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
        quote = quotes();
        quotelen = quote.size();

        if (plugin.getServer().getPluginManager().getPlugin("Spout") != null && config.getBoolean("sfx") == Boolean.valueOf("true")) {
            this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    TARDISSounds.randomTARDISSound();
                }
            }, 60L, 1200L);
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
            schematicfile = new File(getDataFolder(), Constants.SCHEMATIC_FILE_NAME);
            if (!schematicfile.exists()) {
                copy(getResource(Constants.SCHEMATIC_FILE_NAME), schematicfile);
            }
            budgetNschematiccsv = new File(getDataFolder(), Constants.SCHEMATIC_NORTH_BUDGET + ".csv");
            budgetWschematiccsv = new File(getDataFolder(), Constants.SCHEMATIC_WEST_BUDGET + ".csv");
            budgetSschematiccsv = new File(getDataFolder(), Constants.SCHEMATIC_SOUTH_BUDGET + ".csv");
            budgetEschematiccsv = new File(getDataFolder(), Constants.SCHEMATIC_EAST_BUDGET + ".csv");
            biggerNschematiccsv = new File(getDataFolder(), Constants.SCHEMATIC_NORTH_BIGGER + ".csv");
            biggerWschematiccsv = new File(getDataFolder(), Constants.SCHEMATIC_WEST_BIGGER + ".csv");
            biggerSschematiccsv = new File(getDataFolder(), Constants.SCHEMATIC_SOUTH_BIGGER + ".csv");
            biggerEschematiccsv = new File(getDataFolder(), Constants.SCHEMATIC_EAST_BIGGER + ".csv");
            deluxeNschematiccsv = new File(getDataFolder(), Constants.SCHEMATIC_NORTH_DELUXE + ".csv");
            deluxeWschematiccsv = new File(getDataFolder(), Constants.SCHEMATIC_WEST_DELUXE + ".csv");
            deluxeSschematiccsv = new File(getDataFolder(), Constants.SCHEMATIC_SOUTH_DELUXE + ".csv");
            deluxeEschematiccsv = new File(getDataFolder(), Constants.SCHEMATIC_EAST_DELUXE + ".csv");
            budgetschematic_NORTH = Schematic.schematic(budgetNschematiccsv, budgetdimensions[0], budgetdimensions[1], budgetdimensions[2]);
            budgetschematic_WEST = Schematic.schematic(budgetWschematiccsv, budgetdimensions[0], budgetdimensions[1], budgetdimensions[2]);
            budgetschematic_SOUTH = Schematic.schematic(budgetSschematiccsv, budgetdimensions[0], budgetdimensions[1], budgetdimensions[2]);
            budgetschematic_EAST = Schematic.schematic(budgetEschematiccsv, budgetdimensions[0], budgetdimensions[1], budgetdimensions[2]);
            biggerschematic_NORTH = Schematic.schematic(biggerNschematiccsv, biggerdimensions[0], biggerdimensions[1], biggerdimensions[2]);
            biggerschematic_WEST = Schematic.schematic(biggerWschematiccsv, biggerdimensions[0], biggerdimensions[1], biggerdimensions[2]);
            biggerschematic_SOUTH = Schematic.schematic(biggerSschematiccsv, biggerdimensions[0], biggerdimensions[1], biggerdimensions[2]);
            biggerschematic_EAST = Schematic.schematic(biggerEschematiccsv, biggerdimensions[0], biggerdimensions[1], biggerdimensions[2]);
            deluxeschematic_NORTH = Schematic.schematic(deluxeNschematiccsv, deluxedimensions[0], deluxedimensions[1], deluxedimensions[2]);
            deluxeschematic_WEST = Schematic.schematic(deluxeWschematiccsv, deluxedimensions[0], deluxedimensions[1], deluxedimensions[2]);
            deluxeschematic_SOUTH = Schematic.schematic(deluxeSschematiccsv, deluxedimensions[0], deluxedimensions[1], deluxedimensions[2]);
            deluxeschematic_EAST = Schematic.schematic(deluxeEschematiccsv, deluxedimensions[0], deluxedimensions[1], deluxedimensions[2]);

            myconfigfile = new File(getDataFolder(), Constants.CONFIG_FILE_NAME);
            if (!myconfigfile.exists()) {
                // load the default values into file
                copy(getResource(Constants.CONFIG_FILE_NAME), myconfigfile);
            }
            quotesfile = new File(getDataFolder(), Constants.QUOTES_FILE_NAME);
            if (!quotesfile.exists()) {
                copy(getResource(Constants.QUOTES_FILE_NAME), quotesfile);
            }

            timelordsfile = new File(getDataFolder(), Constants.TIMELORDS_FILE_NAME);
        } catch (Exception e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " failed to retrieve files from directory. Using defaults.");
        }
        config = YamlConfiguration.loadConfiguration(myconfigfile);

        // add worlds
        List<World> worlds = this.getServer().getWorlds();
        for (World w : worlds) {
            String worldname = "worlds." + w.getName();
            if (w.getEnvironment() == Environment.NORMAL && !config.contains(worldname)) {
                config.set(worldname, true);
                System.out.println(Constants.MY_PLUGIN_NAME + " Added '" + w.getName() + "' to config. To exclude this world run: " + ChatColor.GREEN + "tardis admin exclude " + w.getName());
            }
        }

        return config;
    }

    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file, false);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " could not save the config file.");
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
            try {
                BufferedReader bufRdr = new BufferedReader(new FileReader(quotesfile));
                String line;
                //read each line of text file
                while ((line = bufRdr.readLine()) != null) {
                    quotes.add(line);
                }
            } catch (IOException io) {
                System.err.println(Constants.MY_PLUGIN_NAME + " Could not read quotes file");
            }
        }
        return quotes;
    }
}