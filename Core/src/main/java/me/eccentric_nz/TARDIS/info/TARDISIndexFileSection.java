package me.eccentric_nz.TARDIS.info;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonPoliceBoxes;
import me.eccentric_nz.TARDIS.custommodeldata.keys.GuiVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISIndexFileSection {

    private final TARDIS plugin;
    private final TISCategory category;
    private final ItemStack[] menu;

    public TARDISIndexFileSection(TARDIS plugin, TISCategory category) {
        this.plugin = plugin;
        this.category = category;
        this.menu = getItemStack();
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[54];
        int i = 0;
        for (TARDISInfoMenu tim : TARDISInfoMenu.values()) {
            if (category == TISCategory.ITEMS && tim.isItem()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.CONSOLE_BLOCKS && tim.isConsoleBlock()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.ACCESSORIES && tim.isAccessory()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.COMPONENTS && tim.isComponent()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.SONIC_COMPONENTS && tim.isSonicComponent()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.SONIC_UPGRADES && tim.isSonicUpgrade()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.CONSOLES && tim.isConsole()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.DISKS && tim.isDisk()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.ROOMS && tim.isRoom()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.PLANETS && tim.isPlanet()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.TIME_TRAVEL && tim.isTimeTravel()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.FOOD && tim.isFood()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.UPDATEABLE_BLOCKS && tim.isUpdateable()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.MONSTERS && tim.isMonster()) {
                stack[i] = makeButton(tim);
                i++;
            }
        }
        // back
        ItemStack back = new ItemStack(GUIChameleonPoliceBoxes.BACK.material(), 1);
        ItemMeta but = back.getItemMeta();
        but.setDisplayName("Back");
        but.setItemModel(GUIChameleonPoliceBoxes.BACK.key());
        back.setItemMeta(but);
        stack[45] = back;
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close_im.setItemModel(GuiVariant.CLOSE.getKey());
        close.setItemMeta(close_im);
        stack[53] = close;
        return stack;
    }

    private ItemStack makeButton(TARDISInfoMenu tim) {
        ItemStack is = new ItemStack(Material.WRITTEN_BOOK, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(TARDISStringUtils.capitalise(tim.toString()));
        is.setItemMeta(im);
        return is;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
