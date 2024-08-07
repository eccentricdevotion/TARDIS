package me.eccentric_nz.TARDIS.skins.tv;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUITelevision;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TVGUI {

    public void addDefaults(ItemStack[] stack) {
        // download
        ItemStack down = new ItemStack(GUITelevision.DOWNLOAD.material(), 1);
        ItemMeta load = down.getItemMeta();
        load.setDisplayName("Toggle skin download");
        load.setCustomModelData(GUITelevision.DOWNLOAD.customModelData());
        down.setItemMeta(load);
        stack[GUITelevision.DOWNLOAD.slot()] = down;
        // remove
        ItemStack remove = new ItemStack(GUITelevision.REMOVE.material(), 1);
        ItemMeta rim = remove.getItemMeta();
        rim.setDisplayName("Remove skin");
        rim.setCustomModelData(GUITelevision.REMOVE.customModelData());
        remove.setItemMeta(rim);
        stack[GUITelevision.REMOVE.slot()] = remove;
        // back
        ItemStack back = new ItemStack(GUITelevision.BACK.material(), 1);
        ItemMeta but = back.getItemMeta();
        but.setDisplayName("Back");
        but.setCustomModelData(GUITelevision.BACK.customModelData());
        back.setItemMeta(but);
        stack[GUITelevision.BACK.slot()] = back;
        // close
        ItemStack close = new ItemStack(GUITelevision.CLOSE.material(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(TARDIS.plugin.getLanguage().getString("BUTTON_CLOSE"));
        close_im.setCustomModelData(GUITelevision.CLOSE.customModelData());
        close.setItemMeta(close_im);
        stack[GUITelevision.CLOSE.slot()] = close;
    }
}
