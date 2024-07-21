package me.eccentric_nz.TARDIS.rooms.eye;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIArtronStorage;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetArtronStorage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TARDISEyeStorage {

    private final TARDIS plugin;

    public TARDISEyeStorage(TARDIS plugin) {
        this.plugin = plugin;
    }

    public ItemStack[] getGUI(int id) {
        ItemStack[] stacks = new ItemStack[9];
        // info
        ItemStack info = new ItemStack(GUIArtronStorage.INFO.material(), 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.setDisplayName("Info");
        info_im.setLore(List.of("Increase the Artron storage", "capacity by placing", "up to 5 Artron Capacitors", "in the slots to the right."));
        info_im.setCustomModelData(GUIArtronStorage.INFO.customModelData());
        info.setItemMeta(info_im);
        stacks[GUIArtronStorage.INFO.slot()] = info;
        // right arrow
        ItemStack r_arrow = new ItemStack(GUIArtronStorage.ARROW_RIGHT.material(), 1);
        ItemMeta r_arrow_im = r_arrow.getItemMeta();
        r_arrow_im.setDisplayName(ChatColor.RESET + "");
        r_arrow_im.setCustomModelData(GUIArtronStorage.ARROW_RIGHT.customModelData());
        r_arrow.setItemMeta(r_arrow_im);
        stacks[GUIArtronStorage.ARROW_RIGHT.slot()] = r_arrow;
        ResultSetArtronStorage rs = new ResultSetArtronStorage(plugin);
        if (rs.fromID(id)) {
            int count = rs.getCapacitorCount();
            // capacitors
            for (int i = 2; i < 2 + count; i++) {
                ItemStack is = new ItemStack(Material.BUCKET, 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName("Artron Capacitor");
                im.setCustomModelData(10000003);
                is.setItemMeta(im);
                stacks[i] = is;
            }
        }
        // left arrow
        ItemStack l_arrow = new ItemStack(GUIArtronStorage.ARROW_LEFT.material(), 1);
        ItemMeta l_arrow_im = l_arrow.getItemMeta();
        l_arrow_im.setDisplayName(ChatColor.RESET + "");
        l_arrow_im.setCustomModelData(GUIArtronStorage.ARROW_LEFT.customModelData());
        l_arrow.setItemMeta(l_arrow_im);
        stacks[GUIArtronStorage.ARROW_LEFT.slot()] = l_arrow;
        // close
        ItemStack close = new ItemStack(GUIArtronStorage.CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        can.setCustomModelData(GUIArtronStorage.CLOSE.customModelData());
        close.setItemMeta(can);
        stacks[GUIArtronStorage.CLOSE.slot()] = close;
        return stacks;
    }
}
