package me.eccentric_nz.TARDIS.skins.tv;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUITelevision;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TVGUI {

    public void addDefaults(ItemStack[] stack) {
        // download
        ItemStack down = new ItemStack(GUITelevision.DOWNLOAD.material(), 1);
        ItemMeta load = down.getItemMeta();
        load.setDisplayName("Toggle skin download");
        load.setItemModel(GUITelevision.DOWNLOAD.key());
        down.setItemMeta(load);
        stack[GUITelevision.DOWNLOAD.slot()] = down;
        // remove
        ItemStack remove = new ItemStack(GUITelevision.REMOVE.material(), 1);
        ItemMeta rim = remove.getItemMeta();
        rim.setDisplayName("Remove skin");
        rim.setItemModel(GUITelevision.REMOVE.key());
        remove.setItemMeta(rim);
        stack[GUITelevision.REMOVE.slot()] = remove;
        // back
        ItemStack back = new ItemStack(GUITelevision.BACK.material(), 1);
        ItemMeta but = back.getItemMeta();
        but.setDisplayName("Back");
        but.setItemModel(GUITelevision.BACK.key());
        back.setItemMeta(but);
        stack[GUITelevision.BACK.slot()] = back;
        // close
        ItemStack close = new ItemStack(GUITelevision.CLOSE.material(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(TARDIS.plugin.getLanguage().getString("BUTTON_CLOSE"));
        close_im.setItemModel(GUITelevision.CLOSE.key());
        close.setItemMeta(close_im);
        stack[GUITelevision.CLOSE.slot()] = close;
    }
}
