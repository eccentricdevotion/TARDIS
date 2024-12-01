package me.eccentric_nz.TARDIS.travel.save;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.GUISaves;
import me.eccentric_nz.TARDIS.database.data.Planet;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDeathLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlanets;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TARDISSavesPlanetInventory {

    private final TARDIS plugin;
    private final ItemStack[] planets;
    private final int id;
    private final Player player;

    public TARDISSavesPlanetInventory(TARDIS plugin, int id, Player player) {
        this.plugin = plugin;
        this.id = id;
        this.player = player;
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
        him.setItemModel(GUISaves.HOME.key());
        his.setItemMeta(him);
        stack[GUISaves.HOME.slot()] = his;
        if (TARDISPermission.hasPermission(player, "tardis.save.death")) {
            // home stack
            ItemStack death = new ItemStack(GUISaves.DEATH.material(), 1);
            ItemMeta dim = death.getItemMeta();
            dim.setDisplayName("Death location");
            List<String> dlore = new ArrayList<>();
            ResultSetDeathLocation rsd = new ResultSetDeathLocation(plugin, player.getUniqueId().toString());
            if (rsd.resultSet()) {
                dlore.add(rsd.getWorld().getName());
                dlore.add("" + rsd.getX());
                dlore.add("" + rsd.getY());
                dlore.add("" + rsd.getZ());
                dlore.add(rsd.getDirection().toString());
                dlore.add((rsd.isSubmarine()) ? "true" : "false");
            } else {
                dlore.add("Not found!");
            }
            dim.setLore(dlore);
            dim.setItemModel(GUISaves.DEATH.key());
            death.setItemMeta(dim);
            stack[GUISaves.DEATH.slot()] = death;
        }
        // unique planets from saved destinations
        ResultSetPlanets rsd = new ResultSetPlanets(plugin, id);
        if (rsd.resultSet()) {
            int i = 9;
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
