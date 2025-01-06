package me.eccentric_nz.TARDIS.lights;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.customblocks.VariableLight;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TARDISLightConverter {

    private final TARDIS plugin;
    private final int id;

    public TARDISLightConverter(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
    }

    public void apply(TardisLight light, Material emitting, Player player, Material variable) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            // get the world
            World w = player.getWorld();
            int starty, endy;
            Schematic schm = tardis.getSchematic();
            // get JSON
            JsonObject obj = TARDISSchematicGZip.getObject(plugin, "consoles", schm.getPermission(), schm.isCustom());
            if (obj != null) {
                // get dimensions
                JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
                int h = dimensions.get("height").getAsInt();
                int width = dimensions.get("width").getAsInt();
                int d = dimensions.get("length").getAsInt() - 1;
                if (schm.getPermission().equals("mechanical") || schm.getPermission().equals("cursed")) {
                    starty = 62;
                } else if (TARDISConstants.HIGHER.contains(schm.getPermission())) {
                    starty = 65;
                } else {
                    starty = 64;
                }
                endy = starty + h;
                TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                TARDISTIPSData pos = tintpos.getTIPSData(tardis.getTIPS());
                int minx = pos.getCentreX();
                int maxx = minx + width;
                int minz = pos.getCentreZ();
                int maxz = minz + d;
                // loop through the blocks inside this cube
                for (int l = starty; l <= endy; l++) {
                    for (int r = minx; r <= maxx; r++) {
                        for (int c = minz; c <= maxz; c++) {
                            Block b = w.getBlockAt(r, l, c);
                            if (b.getType().equals(emitting)) {
                                if (light.getOn().isVariable()) {
                                    new VariableLight(variable, b.getLocation().add(0.5, 0.5, 0.5)).set(tardis.isLightsOn() ? light.getOn().getCustomModel() : light.getOff().getCustomModel(), tardis.isLightsOn() ? 15 : 0);
                                } else {
                                    TARDISDisplayItemUtils.set(light.getOn(), b, -1);
                                }
                                // search around block for redstone and remove
                                for (BlockFace face : plugin.getGeneralKeeper().getBlockFaces()) {
                                    Block block = b.getRelative(face);
                                    if (block.getType().equals(Material.LEVER) || block.getType().equals(Material.REDSTONE_BLOCK)) {
                                        block.setType(Material.AIR);
                                    }
                                }
                            }
                        }
                    }
                }
                plugin.getMessenger().message(player, TardisModule.TARDIS, "Light conversion complete");
                plugin.getTrackerKeeper().getLightChangers().remove(player.getUniqueId());
            }
        }
    }
}
