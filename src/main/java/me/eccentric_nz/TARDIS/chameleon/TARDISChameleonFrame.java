package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.enumeration.CONTROL;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class TARDISChameleonFrame {

    private final TARDIS plugin;

    public TARDISChameleonFrame(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void updateChameleonFrame(int id, PRESET preset) {
        // is there a Chameleon frame record for this TARDIS?
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("type", CONTROL.FRAME.getId());
        ResultSetControls rsc = new ResultSetControls(plugin, where, false);
        if (rsc.resultSet()) {
            // get location of Chameleon frame
            Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
            if (location != null) {
                for (Entity e : location.getChunk().getEntities()) {
                    if (e instanceof ItemFrame) {
                        if (compareLocations(e.getLocation(), location)) {
                            ItemFrame frame = (ItemFrame) e;
                            ItemStack is = new ItemStack(preset.getMaterial());
                            ItemMeta im = is.getItemMeta();
                            im.setDisplayName(preset.toString());
                            is.setItemMeta(im);
                            frame.setItem(is, true);
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean compareLocations(Location one, Location two) {
        return one.getBlockX() == two.getBlockX() && one.getBlockY() == two.getBlockY() && one.getBlockZ() == two.getBlockZ();
    }
}
