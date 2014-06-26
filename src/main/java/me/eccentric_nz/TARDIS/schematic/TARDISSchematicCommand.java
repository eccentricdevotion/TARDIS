package me.eccentric_nz.TARDIS.schematic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TARDISSchematicCommand implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISSchematicCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisschematic")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage(plugin.getPluginName() + "Command can only be used by a player!");
                return true;
            }
            UUID uuid = player.getUniqueId();
            if (args.length == 1 && args[0].equalsIgnoreCase("paste")) {
                return new TARDISSchematicPaster(plugin, player).paste();
            }
            if (args.length < 2) {
                sender.sendMessage(plugin.getPluginName() + "Too few arguments!");
                return true;
            }
            if (!args[0].equalsIgnoreCase("load") && !args[0].equalsIgnoreCase("save")) {
                sender.sendMessage(plugin.getPluginName() + "You must supply a schematic name!");
                return true;
            }
            if (args[0].equalsIgnoreCase("save")) {
                // check they have selected start and end blocks
                if (!plugin.getTrackerKeeper().getStartLocation().containsKey(uuid)) {
                    player.sendMessage(plugin.getPluginName() + "No start block selected!");
                    return true;
                }
                if (!plugin.getTrackerKeeper().getEndLocation().containsKey(uuid)) {
                    player.sendMessage(plugin.getPluginName() + "No end block selected!");
                    return true;
                }
                // get the world
                World w = plugin.getTrackerKeeper().getStartLocation().get(uuid).getWorld();
                String chk_w = plugin.getTrackerKeeper().getStartLocation().get(uuid).getWorld().getName();
                if (!w.getName().equals(chk_w)) {
                    player.sendMessage(plugin.getPluginName() + "Start and end blocks are not in the same world!");
                    return true;
                }
                // get the raw coords
                int sx = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockX();
                int sy = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockY();
                int sz = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockZ();
                int ex = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockX();
                int ey = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockY();
                int ez = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockZ();
                // get the min & max coords
                int minx = (sx < ex) ? sx : ex;
                int maxx = (sx < ex) ? ex : sx;
                int miny = (sy < ey) ? sy : ey;
                int maxy = (sy < ey) ? ey : sy;
                int minz = (sz < ez) ? sz : ez;
                int maxz = (sz < ez) ? ez : sz;
                // create a JSON objet for relative position
                JSONObject relative = new JSONObject();
                int px = player.getLocation().getBlockX() - minx;
                int py = player.getLocation().getBlockY() - miny;
                int pz = player.getLocation().getBlockZ() - minz;
                relative.put("x", px);
                relative.put("y", py);
                relative.put("z", pz);
                // create a JSON objet for dimensions
                JSONObject dimensions = new JSONObject();
                int width = (maxx - minx) + 1;
                int height = (maxy - miny) + 1;
                int length = (maxz - minz) + 1;
                dimensions.put("width", width);
                dimensions.put("height", height);
                dimensions.put("length", length);
                if (width != length) {
                    player.sendMessage(plugin.getPluginName() + "Region must be a square!");
                    return true;
                }
                if (width % 16 != 0 || length % 16 != 0) {
                    player.sendMessage(plugin.getPluginName() + "Length of sides must be a multiple of 16 blocks!");
                    return true;
                }
                // create JSON arrays for block data
                JSONArray levels = new JSONArray();
                // loop through the blocks inside this cube
                for (int l = miny; l <= maxy; l++) {
                    JSONArray rows = new JSONArray();
                    for (int r = minx; r <= maxx; r++) {
                        JSONArray columns = new JSONArray();
                        for (int c = minz; c <= maxz; c++) {
                            JSONObject obj = new JSONObject();
                            Block b = w.getBlockAt(r, l, c);
                            obj.put("type", b.getType().toString());
                            obj.put("data", b.getData());
                            columns.put(obj);
                        }
                        rows.put(columns);
                    }
                    levels.put(rows);
                }
                JSONObject schematic = new JSONObject();
                schematic.put("relative", relative);
                schematic.put("dimensions", dimensions);
                schematic.put("input", levels);
                String output = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + args[1] + ".json";
                File file = new File(output);
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file), 16 * 1024);
                    bw.write(schematic.toString());
                    bw.close();
                    TARDISSchematicGZip.zip(output, plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + args[1] + ".tschm");
                    file.delete();
                    player.sendMessage(plugin.getPluginName() + args[1] + ".tschm saved :)");
                } catch (IOException e) {
                    player.sendMessage(plugin.getPluginName() + "Could not write GZipped JSON schematic file!");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("load")) {
                String instr = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + args[1] + ".tschm";
                File file = new File(instr);
                if (!file.exists()) {
                    player.sendMessage(plugin.getPluginName() + "Could not find a schematic with that name!");
                    return true;
                }
                JSONObject sch = TARDISSchematicGZip.unzip(instr);
                plugin.getTrackerKeeper().getPastes().put(uuid, sch);
                player.sendMessage(plugin.getPluginName() + "Schematic loaded! You can now use the " + ChatColor.GREEN + "/ts paste" + ChatColor.RESET + " command");
                return true;
            }
        }
        return false;
    }
}
