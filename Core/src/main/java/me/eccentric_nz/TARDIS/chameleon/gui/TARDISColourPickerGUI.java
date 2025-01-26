package me.eccentric_nz.TARDIS.chameleon.gui;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleon;
import me.eccentric_nz.TARDIS.custommodels.keys.ArrowVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.ColouredVariant;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;

public class TARDISColourPickerGUI {

    private final TARDIS plugin;
    private final ItemStack[] gui;

    public TARDISColourPickerGUI(TARDIS plugin) {
        this.plugin = plugin;
        gui = getItemStack();
    }

    private ItemStack[] getItemStack() {
        // display
        ItemStack dis = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
        LeatherArmorMeta play = (LeatherArmorMeta) dis.getItemMeta();
        play.setDisplayName("Colour");
        play.setLore(List.of("Red: 255", "Green: 255", "Blue: 255"));
        play.setItemModel(ColouredVariant.TINTED_CAMERA.getKey());
        play.setColor(Color.fromRGB(255, 255, 255)); // white
        dis.setItemMeta(play);
        // red
        ItemStack red = new ItemStack(Material.RED_WOOL, 1);
        ItemMeta rrr = red.getItemMeta();
        rrr.setDisplayName("Red");
        red.setItemMeta(rrr);
        // green
        ItemStack green = new ItemStack(Material.LIME_WOOL, 1);
        ItemMeta ggg = green.getItemMeta();
        ggg.setDisplayName("Green");
        green.setItemMeta(ggg);
        // blue
        ItemStack blue = new ItemStack(Material.LIGHT_BLUE_WOOL, 1);
        ItemMeta bbb = blue.getItemMeta();
        bbb.setDisplayName("Blue");
        blue.setItemMeta(bbb);
        // red tint
        ItemStack redtint = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
        LeatherArmorMeta rrrtint = (LeatherArmorMeta) redtint.getItemMeta();
        rrrtint.setColor(Color.fromRGB(255, 0, 0)); // red
        rrrtint.setItemModel(ColouredVariant.TINTED_CAMERA.getKey());
        rrrtint.setDisplayName("Red");
        redtint.setItemMeta(rrrtint);
        // green tint
        ItemStack greentint = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
        LeatherArmorMeta gggtint = (LeatherArmorMeta) greentint.getItemMeta();
        gggtint.setColor(Color.fromRGB(0, 255, 0)); // green
        gggtint.setItemModel(ColouredVariant.TINTED_CAMERA.getKey());
        gggtint.setDisplayName("Green");
        greentint.setItemMeta(gggtint);
        // blue tint
        ItemStack bluetint = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
        LeatherArmorMeta bbbtint = (LeatherArmorMeta) bluetint.getItemMeta();
        bbbtint.setColor(Color.fromRGB(0, 0, 255)); // blue
        bbbtint.setItemModel(ColouredVariant.TINTED_CAMERA.getKey());
        bbbtint.setDisplayName("Blue");
        bluetint.setItemMeta(bbbtint);
        // less
        ItemStack less = new ItemStack(Material.ARROW, 1);
        ItemMeta lll = less.getItemMeta();
        bbbtint.setItemModel(ArrowVariant.LESS.getKey());
        lll.setDisplayName("Less");
        less.setItemMeta(lll);
        // more
        ItemStack more = new ItemStack(Material.ARROW, 1);
        ItemMeta mmm = more.getItemMeta();
        bbbtint.setItemModel(ArrowVariant.MORE.getKey());
        mmm.setDisplayName("More");
        more.setItemMeta(mmm);
        // select
        ItemStack select = new ItemStack(Material.BOWL, 1);
        ItemMeta sss = select.getItemMeta();
        sss.setDisplayName("Select colour");
        select.setItemMeta(sss);
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta win = close.getItemMeta();
        win.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        win.setItemModel(GUIChameleon.BUTTON_CLOSE.key());
        close.setItemMeta(win);
        return new ItemStack[]{null, null, null, null, dis, null, null, null, null, null, null, null, null, null, null, null, null, null, red, null, less, null, redtint, null, more, null, null, green, null, less, null, greentint, null, more, null, select, blue, null, less, null, bluetint, null, more, null, null, null, null, null, null, null, null, null, null, close};
    }

    public ItemStack[] getGUI() {
        return gui;
    }
}
