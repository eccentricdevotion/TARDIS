package me.eccentric_nz.TARDIS.chameleon.itemframe;

import java.util.Arrays;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ColourPickerGUI {

    private final ItemStack[] gui;

    public ColourPickerGUI() {
        gui = getItemStack();
    }

    private ItemStack[] getItemStack() {
        // display
        ItemStack dis = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
        LeatherArmorMeta play = (LeatherArmorMeta) dis.getItemMeta();
        play.setDisplayName("Colour");
        play.setLore(Arrays.asList("Red: 255", "Green: 255", "Blue: 255"));
        play.setCustomModelData(4);
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
        rrrtint.setCustomModelData(4);
        rrrtint.setDisplayName("Red");
        redtint.setItemMeta(rrrtint);
        // green tint
        ItemStack greentint = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
        LeatherArmorMeta gggtint = (LeatherArmorMeta) greentint.getItemMeta();
        gggtint.setColor(Color.fromRGB(0, 255, 0)); // green
        gggtint.setCustomModelData(4);
        gggtint.setDisplayName("Green");
        greentint.setItemMeta(gggtint);
        // blue tint
        ItemStack bluetint = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
        LeatherArmorMeta bbbtint = (LeatherArmorMeta) bluetint.getItemMeta();
        bbbtint.setColor(Color.fromRGB(0, 0, 255)); // blue
        bbbtint.setCustomModelData(4);
        bbbtint.setDisplayName("Blue");
        bluetint.setItemMeta(bbbtint);
        // less
        ItemStack less = new ItemStack(Material.ARROW, 1);
        ItemMeta lll = less.getItemMeta();
        lll.setDisplayName("Less");
        less.setItemMeta(lll);
        // more
        ItemStack more = new ItemStack(Material.ARROW, 1);
        ItemMeta mmm = more.getItemMeta();
        mmm.setDisplayName("More");
        more.setItemMeta(mmm);
        // select
        ItemStack select = new ItemStack(Material.ARROW, 1);
        ItemMeta sss = select.getItemMeta();
        sss.setDisplayName("Select colour");
        select.setItemMeta(sss);
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta win = close.getItemMeta();
        win.setDisplayName("Close");
        close.setItemMeta(win);
        ItemStack[] is = {
                null, null, null, null, dis, null, null, null, null,
                null, null, null, null, null, null, null, null, null,
                red, null, less, null, redtint, null, more, null, null,
                green, null, less, null, greentint, null, more, null, select,
                blue, null, less, null, bluetint, null, more, null, null,
                null, null, null, null, null, null, null, null, close
        };
        return is;
    }

    public ItemStack[] getGUI() {
        return gui;
    }
}
