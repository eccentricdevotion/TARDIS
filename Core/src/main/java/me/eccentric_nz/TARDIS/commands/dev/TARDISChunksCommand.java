package me.eccentric_nz.TARDIS.commands.dev;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.desktop.TARDISChunkUtils;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TARDISChunksCommand {

    private final TARDIS plugin;

    public TARDISChunksCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean list(CommandSender sender) {
        if (sender instanceof Player player) {
            // get TARDIS player is in
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
            if (rs.resultSet()) {
                int id = rs.getTardis_id();
                // get TARDIS schematic
                HashMap<String, Object> wheret = new HashMap<>();
                wheret.put("tardis_id", id);
                ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false, 2);
                if (rst.resultSet()) {
                    Tardis tardis = rst.getTardis();
                    String[] tc = tardis.getChunk().split(":");
                    int cx = TARDISNumberParsers.parseInt(tc[1]);
                    int cz = TARDISNumberParsers.parseInt(tc[2]);
                    World world = player.getLocation().getWorld();
                    Chunk chunk = world.getChunkAt(cx, cz);
                    Schematic schematic = tardis.getSchematic();
                    for (Chunk c : TARDISChunkUtils.getConsoleChunks(chunk, schematic)) {
                        plugin.debug(c);
                    }
                    plugin.debug("-----");
                    JsonObject obj = TARDISSchematicGZip.getObject(plugin, "consoles", schematic.getPermission(), schematic.isCustom());
                    if (obj != null) {
                        // get dimensions
                        JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
                        int w = dimensions.get("width").getAsInt();
                        int d = dimensions.get("length").getAsInt() - 1;
                        Location location = getLocation(schematic, tardis, world);
                        for (Chunk c : TARDISChunkUtils.getConsoleChunks(world, location.getChunk().getX(), location.getChunk().getZ(), w, d)) {
                            plugin.debug(c);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private Location getLocation(Schematic schematic, Tardis tardis, World world) {
        int starty;
        if (schematic.getPermission().equals("mechanical")) {
            starty = 62;
        } else if (TARDISConstants.HIGHER.contains(schematic.getPermission())) {
            starty = 65;
        } else {
            starty = 64;
        }
        TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
        TARDISTIPSData pos = tintpos.getTIPSData(tardis.getTIPS());
        int startx = pos.getCentreX();
        int startz = pos.getCentreZ();
        return new Location(world, startx, starty, startz);
    }
}
