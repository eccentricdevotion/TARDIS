package me.eccentric_nz.plugins.TARDIS;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.ConsoleCommandSender;
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
    public FileConfiguration timelords = null;
    public File schematicfile = null;
    public File myconfigfile = null;
    public File timelordsfile = null;
    private TARDISexecutor tardisExecutor;
    public String[][][] schematic;
    protected static TARDIS plugin;
    TARDISBlockPlaceListener tardisBlockPlaceListener = new TARDISBlockPlaceListener(this);
    TARDISBlockBreakListener tardisBlockBreakListener = new TARDISBlockBreakListener(this);
    TARDISPlayerListener tardisPlayerListener = new TARDISPlayerListener(this);
    TARDISBlockProtectListener tardisProtectListener = new TARDISBlockProtectListener(this);
    PluginManager pm = Bukkit.getServer().getPluginManager();
    public HashMap<String, String> trackPlayers = new HashMap<String, String>();

    @Override
    public void onEnable() {
        plugin = this;
        pm.registerEvents(tardisBlockPlaceListener, this);
        pm.registerEvents(tardisBlockBreakListener, this);
        pm.registerEvents(tardisPlayerListener, this);
        pm.registerEvents(tardisProtectListener, this);

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
    }

    @Override
    public void onDisable() {
        saveCustomConfig();
    }

    public FileConfiguration loadConfig() {
        Server server = plugin.getServer();
        ConsoleCommandSender console = server.getConsoleSender();
        try {
            schematicfile = new File(getDataFolder(), Constants.SCHEMATIC_FILE_NAME);
            if (!schematicfile.exists()) {
                copy(getResource(Constants.SCHEMATIC_FILE_NAME), schematicfile);
            }
            schematic = Schematic.schematic(schematicfile);

            myconfigfile = new File(getDataFolder(), Constants.CONFIG_FILE_NAME);
            if (!myconfigfile.exists()) {
                // load the default values into file
                copy(getResource(Constants.CONFIG_FILE_NAME), myconfigfile);
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
                console.sendMessage(Constants.MY_PLUGIN_NAME + " Added '" + w.getName() + "' to config. To exclude this world run: " + ChatColor.GREEN + "tardis admin exclude " + w.getName());
            }
        }
        if (timelordsfile.exists()) {
            timelords = YamlConfiguration.loadConfiguration(timelordsfile);
        }

        return config;
    }

    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
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
}