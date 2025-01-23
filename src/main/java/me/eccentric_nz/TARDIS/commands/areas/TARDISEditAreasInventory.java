package me.eccentric_nz.TARDIS.commands.areas;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIMap;
import me.eccentric_nz.TARDIS.custommodels.GUIUpgrade;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreaLocations;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TARDISEditAreasInventory {

    private final TARDIS plugin;
    private final int area_id;

    public TARDISEditAreasInventory(TARDIS plugin, int area_id) {
        this.plugin = plugin;
        this.area_id = area_id;
    }

    public ItemStack[] getLocations() {
        ItemStack[] stacks = new ItemStack[54];
        ResultSetAreaLocations rs = new ResultSetAreaLocations(plugin, area_id);
        if (rs.resultSet()) {
            int i = 0;
            for (Location l : rs.getLocations()) {
                if (i < 45) {
                    ItemStack is = new ItemStack(Material.MAP);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName("Location " + (i + 1));
                    List<String> lore = new ArrayList<>();
                    lore.add(l.getWorld().getName());
                    lore.add("x: " + l.getBlockX());
                    lore.add("y: " + l.getBlockY());
                    lore.add("z: " + l.getBlockZ());
                    lore.add("id: " + area_id);
                    im.lore(lore);
                    is.setItemMeta(im);
                    stacks[i] = is;
                    i++;
                }
            }
        }
        // Info
        ItemStack info = new ItemStack(Material.BOOK, 1);
        ItemMeta ii = info.getItemMeta();
        ii.setDisplayName("Info");
        ArrayList<String> info_lore = new ArrayList<>();
        info_lore.add("To REMOVE a location");
        info_lore.add("select a location map");
        info_lore.add("then click the Remove");
        info_lore.add("button (bucket).");
        info_lore.add("To ADD the location");
        info_lore.add("where you are standing");
        info_lore.add("click the Add button");
        info_lore.add("(nether star).");
        ii.lore(info_lore);
        info.setItemMeta(ii);
        stacks[45] = info;
        // add
        ItemStack add = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta er = add.getItemMeta();
        er.setDisplayName("Add");
        er.lore(List.of("area_id: " + area_id));
        add.setItemMeta(er);
        stacks[48] = add;
        // remove
        ItemStack del = new ItemStack(Material.BUCKET, 1);
        ItemMeta dd = del.getItemMeta();
        dd.setDisplayName("Remove");
        del.setItemMeta(dd);
        stacks[50] = del;
        // close
        ItemStack close = new ItemStack(GUIMap.BUTTON_CLOSE.material(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close_im.setItemModel(GUIUpgrade.CLOSE.key());
        close.setItemMeta(close_im);
        stacks[53] = close;
        return stacks;
    }
}
