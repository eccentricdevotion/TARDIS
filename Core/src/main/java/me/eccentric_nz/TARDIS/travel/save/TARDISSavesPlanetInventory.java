package me.eccentric_nz.TARDIS.travel.save;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUISaves;
import me.eccentric_nz.TARDIS.database.data.Planet;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlanets;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TARDISSavesPlanetInventory {

    private final TARDIS plugin;
    private final ItemStack[] planets;
    private final int id;

    public TARDISSavesPlanetInventory(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        this.planets = getWorlds();
    }

    private ItemStack[] getWorlds() {
        ItemStack[] stack = new ItemStack[54];
        // home stack
        ItemStack his = new ItemStack(GUISaves.HOME.material(), 1);
        ItemMeta him = his.getItemMeta();
        List<String> hlore = new ArrayList<>();
        HashMap<String, Object> wherehl = new HashMap<>();
        wherehl.put("tardis_id", id);
        ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, wherehl);
        if (rsh.resultSet()) {
            him.setDisplayName("Home");
            hlore.add(rsh.getWorld().getName());
            hlore.add("" + rsh.getX());
            hlore.add("" + rsh.getY());
            hlore.add("" + rsh.getZ());
            hlore.add(rsh.getDirection().toString());
            hlore.add((rsh.isSubmarine()) ? "true" : "false");
            if (!rsh.getPreset().isEmpty()) {
                hlore.add(rsh.getPreset());
            }
        } else {
            hlore.add("Not found!");
        }
        him.setLore(hlore);
        him.setCustomModelData(GUISaves.HOME.customModelData());
        his.setItemMeta(him);
        stack[GUISaves.HOME.slot()] = his;
        // unique planets from saved destinations
        ResultSetPlanets rsd = new ResultSetPlanets(plugin, id);
        if (rsd.resultSet()) {
            int i = 2;
            for (Planet planet : rsd.getData()) {
                ItemStack is = new ItemStack(planet.getMaterial(), 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(planet.getName());
                is.setItemMeta(im);
                stack[i] = is;
                i += 2;
            }
        }
        return stack;
    }

    public ItemStack[] getPlanets() {
        return planets;
    }
}
