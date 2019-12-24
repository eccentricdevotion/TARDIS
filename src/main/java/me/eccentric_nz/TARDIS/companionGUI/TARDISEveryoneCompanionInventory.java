package me.eccentric_nz.TARDIS.companionGUI;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUICompanion;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class TARDISEveryoneCompanionInventory {

    private final TARDIS plugin;
    private final ItemStack[] skulls;

    public TARDISEveryoneCompanionInventory(TARDIS plugin) {
        this.plugin = plugin;
        skulls = getItemStack();
    }

    private ItemStack[] getItemStack() {
        ItemStack[] heads = new ItemStack[54];
        int i = 0;
        for (Player c : plugin.getServer().getOnlinePlayers()) {
            if (i < 45) {
                ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta skull = (SkullMeta) head.getItemMeta();
                skull.setOwningPlayer(c);
                skull.setDisplayName(c.getName());
                ArrayList<String> lore = new ArrayList<>();
                lore.add(c.getUniqueId().toString());
                skull.setLore(lore);
                head.setItemMeta(skull);
                heads[i] = head;
                i++;
            }
        }
        // add buttons
        ItemStack info = new ItemStack(Material.BOOK, 1);
        ItemMeta ii = info.getItemMeta();
        ii.setDisplayName("Info");
        ArrayList<String> info_lore = new ArrayList<>();
        info_lore.add("To REMOVE a companion");
        info_lore.add("select a player head");
        info_lore.add("then click the Remove");
        info_lore.add("button (bucket).");
        info_lore.add("To ADD a companion");
        info_lore.add("click the Add button");
        info_lore.add("(nether star).");
        ii.setLore(info_lore);
        info.setItemMeta(ii);
        heads[45] = info;
        ItemStack add = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta aa = add.getItemMeta();
        aa.setDisplayName("Add");
        add.setItemMeta(aa);
        heads[48] = add;
        ItemStack del = new ItemStack(Material.BUCKET, 1);
        ItemMeta dd = add.getItemMeta();
        dd.setDisplayName("Remove");
        del.setItemMeta(dd);
        heads[51] = del;
        // Cancel / close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        can.setCustomModelData(GUICompanion.BUTTON_CLOSE.getCustomModelData());
        close.setItemMeta(can);
        heads[53] = close;

        return heads;
    }

    public ItemStack[] getSkulls() {
        return skulls;
    }
}
