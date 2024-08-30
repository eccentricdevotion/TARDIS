package me.eccentric_nz.TARDIS.desktop;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderPreview;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmat;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.World;

public class TARDISDesktopPreview {

    private final TARDIS plugin;
    private long delay = 5;

    public TARDISDesktopPreview(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void create() {
        // message start
        plugin.getMessenger().send(plugin.getConsole(), TardisModule.TARDIS, "PREVIEW_START");
        // get default world
        String dn = "TARDIS_TimeVortex";
        if (plugin.getConfig().getBoolean("creation.default_world")) {
            dn = plugin.getConfig().getString("creation.default_world_name");
        }
        World world = plugin.getServer().getWorld(dn);
        if (world != null) {
            for (Schematic schematic : Consoles.getBY_NAMES().values()) {
                if (schematic.getPreview() < 0 && !hasPreview(schematic)) {
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        plugin.getMessenger().send(plugin.getConsole(), TardisModule.TARDIS, "PREVIEW_SCHM", schematic.getPermission());
                        TARDISBuilderPreview builder = new TARDISBuilderPreview(plugin, schematic, world);
                        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, builder, 1L, 3L);
                        builder.setTask(task);
                        // TODO fix records for consoles with no iron doors
                    }, delay);
                    delay += 1200;
                }
            }
            // message finish
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().send(plugin.getConsole(), TardisModule.TARDIS, "PREVIEW_END"), delay);
        }
    }

    private boolean hasPreview(Schematic schematic) {
        ResultSetTransmat rst = new ResultSetTransmat(plugin, schematic.getPreview(), schematic.getPermission());
        return rst.resultSet();
    }
}
