package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.LampToggler;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLamps;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class TARDISSonicLight {

    private final TARDIS plugin;

    public TARDISSonicLight(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addLamp(Block light, Player player) {
        // must be in a TARDIS
        // get TARDIS player is in
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
        if (rs.resultSet()) {
            // TARDIS_TimeVortex:500:66:504
            int x = light.getLocation().getBlockX();
            int y = light.getLocation().getBlockY();
            int z = light.getLocation().getBlockZ();
            String location = light.getWorld().getName() + ":" + x + ":" + y + ":" + z;
            // check if already added
            HashMap<String, Object> wherel = new HashMap<>();
            wherel.put("location", location);
            ResultSetLamps rsl = new ResultSetLamps(plugin, wherel, false);
            if (rsl.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "LAMP_EXISTS");
            } else {
                HashMap<String, Object> set = new HashMap<>();
                set.put("tardis_id", rs.getTardis_id());
                set.put("location", location);
                plugin.getQueryFactory().doInsert("lamps", set);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "LAMP_ADD", (x + ":" + y + ":" + z));
            }
            // add to lamps
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_IN_WORLD");
        }
    }

    public void removeLamp(Block light, Player player) {
        int x = light.getLocation().getBlockX();
        int y = light.getLocation().getBlockY();
        int z = light.getLocation().getBlockZ();
        String location = light.getWorld().getName() + ":" + x + ":" + y + ":" + z;
        // check if exists
        HashMap<String, Object> wherel = new HashMap<>();
        wherel.put("location", location);
        ResultSetLamps rsl = new ResultSetLamps(plugin, wherel, false);
        if (rsl.resultSet()) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("location", location);
            plugin.getQueryFactory().doDelete("lamps", where);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "LAMP_REMOVE");
        }
    }

    public void toggle(ItemDisplay display, Block light) {
        ItemStack lamp = display.getItemStack();
        TARDISDisplayItem tdi = TARDISDisplayItemUtils.get(display);
        // check the block is a chemistry lamp block
        if (tdi != null && tdi.isLight()) {
            TARDISDisplayItem toggled = TardisLight.getToggled(tdi);
            ItemMeta im = lamp.getItemMeta();
            ItemStack change = new ItemStack(toggled.getMaterial(), 1);
            if (toggled.isLit()) {
                // create light source
                LampToggler.setLightlevel(light, 15);
            } else {
                // delete light source - should eventually get rid of this...
                LampToggler.deleteLight(light);
                // set light level to zero
                LampToggler.setLightlevel(light, 0);
            }
            change.setItemMeta(im);
            display.setItemStack(change);
        }
    }
}
