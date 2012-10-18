package me.eccentric_nz.plugins.TARDIS;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;
import java.io.File;
import java.io.IOException;

public class TARDISWEPaste {

    private TARDIS plugin;

    public TARDISWEPaste(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void pasteSchematic(File f, Constants.COMPASS d, String world, int x, int y, int z) {
        LocalWorld w = BukkitUtil.getLocalWorld(plugin.getServer().getWorld(world));
        int angle;
        switch (d) {
            case NORTH:
                angle = 270;
                break;
            case WEST:
                angle = 180;
                break;
            case SOUTH:
                angle = 90;
                break;
            default:
                angle = 0;
                break;
        }
        String formatName = "mcedit";
        if (!f.exists()) {
            plugin.getServer().broadcastMessage("Schematic not exists");
            return;
        }
        SchematicFormat format = SchematicFormat.getFormat(formatName);
        if (format == null) {
            plugin.getServer().broadcastMessage("Unknown schematic format: " + formatName);
            return;
        }
        try {
            CuboidClipboard clip = format.load(f);
            clip.rotate2D(angle);
            clip.paste(new EditSession(w, 500000), new Vector(x, y, z), false);
        } catch (DataException e) {
            plugin.getServer().broadcastMessage("Load error: " + e.getMessage());
        } catch (IOException e) {
            plugin.getServer().broadcastMessage("Schematic could not read or it does not exist: " + e.getMessage());
        } catch (MaxChangedBlocksException e) {
            plugin.getServer().broadcastMessage("Max blocks changed: " + e.getMessage());
        }
    }
}
