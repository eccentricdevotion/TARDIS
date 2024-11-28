/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Bowl;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TVMGUI {

    private final TARDIS plugin;
    private final int tachyonLevel;
    private final ItemStack[] gui;

    public TVMGUI(TARDIS plugin, int tachyonLevel) {
        this.plugin = plugin;
        this.tachyonLevel = tachyonLevel;
        gui = getItemStack();
    }

    /**
     * Constructs an inventory for the Vortex Manipulator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        // display
        ItemStack dis = new ItemStack(Material.BOWL, 1);
        ItemMeta play = dis.getItemMeta();
        play.setDisplayName("Display");
        play.setLore(Arrays.asList(""));
        play.setItemModel(Bowl.VM_DISPLAY.getKey());
        dis.setItemMeta(play);
        // predictive world
        ItemStack pred = new ItemStack(Material.BOWL, 1);
        ItemMeta ict = dis.getItemMeta();
        ict.setDisplayName("Predictive Text");
        ict.setLore(Arrays.asList(""));
        ict.setItemModel(Bowl.PREDICTIVE.getKey());
        pred.setItemMeta(ict);
        // keypad pad
        // 1
        ItemStack one = new ItemStack(Material.BOWL, 1);
        ItemMeta none = one.getItemMeta();
        none.setDisplayName("1");
        none.setItemModel(Bowl.VM_ONE.getKey());
        one.setItemMeta(none);
        // 2 abc
        ItemStack two = new ItemStack(Material.BOWL, 1);
        ItemMeta abc = two.getItemMeta();
        abc.setDisplayName("2");
        abc.setLore(Arrays.asList("abc"));
        abc.setItemModel(Bowl.VM_TWO.getKey());
        two.setItemMeta(abc);
        // 3 def
        ItemStack three = new ItemStack(Material.BOWL, 1);
        ItemMeta def = three.getItemMeta();
        def.setDisplayName("3");
        def.setLore(Arrays.asList("def"));
        def.setItemModel(Bowl.VM_THREE.getKey());
        three.setItemMeta(def);
        // 4 ghi
        ItemStack four = new ItemStack(Material.BOWL, 1);
        ItemMeta ghi = four.getItemMeta();
        ghi.setDisplayName("4");
        ghi.setLore(Arrays.asList("ghi"));
        ghi.setItemModel(Bowl.VM_FOUR.getKey());
        four.setItemMeta(ghi);
        // 5 jkl
        ItemStack five = new ItemStack(Material.BOWL, 1);
        ItemMeta jkl = five.getItemMeta();
        jkl.setDisplayName("5");
        jkl.setLore(Arrays.asList("jkl"));
        jkl.setItemModel(Bowl.VM_FIVE.getKey());
        five.setItemMeta(jkl);
        // 6 mno
        ItemStack six = new ItemStack(Material.BOWL, 1);
        ItemMeta mno = six.getItemMeta();
        mno.setDisplayName("6");
        mno.setLore(Arrays.asList("mno"));
        mno.setItemModel(Bowl.VM_SIX.getKey());
        six.setItemMeta(mno);
        // 7 pqrs
        ItemStack seven = new ItemStack(Material.BOWL, 1);
        ItemMeta pqrs = seven.getItemMeta();
        pqrs.setDisplayName("7");
        pqrs.setLore(Arrays.asList("pqrs"));
        pqrs.setItemModel(Bowl.VM_SEVEN.getKey());
        seven.setItemMeta(pqrs);
        // 8 tuv
        ItemStack eight = new ItemStack(Material.BOWL, 1);
        ItemMeta tuv = eight.getItemMeta();
        tuv.setDisplayName("8");
        tuv.setLore(Arrays.asList("tuv"));
        tuv.setItemModel(Bowl.VM_EIGHT.getKey());
        eight.setItemMeta(tuv);
        // 9 wxyz
        ItemStack nine = new ItemStack(Material.BOWL, 1);
        ItemMeta wxyz = nine.getItemMeta();
        wxyz.setDisplayName("9");
        wxyz.setLore(Arrays.asList("wxyz"));
        wxyz.setItemModel(Bowl.VM_NINE.getKey());
        nine.setItemMeta(wxyz);
        // 0
        ItemStack zero = new ItemStack(Material.BOWL, 1);
        ItemMeta nada = zero.getItemMeta();
        nada.setDisplayName("0");
        nada.setItemModel(Bowl.VM_ZERO.getKey());
        zero.setItemMeta(nada);
        // symbols -_*~
        ItemStack hash = new ItemStack(Material.BOWL, 1);
        ItemMeta symbols = hash.getItemMeta();
        symbols.setDisplayName("#");
        symbols.setLore(Arrays.asList("~_-"));
        symbols.setItemModel(Bowl.VM_HASH.getKey());
        hash.setItemMeta(symbols);
        // space
        ItemStack star = new ItemStack(Material.BOWL, 1);
        ItemMeta space = star.getItemMeta();
        space.setDisplayName("*");
        space.setLore(Arrays.asList("Space"));
        space.setItemModel(Bowl.VM_STAR.getKey());
        star.setItemMeta(space);
        // world
        ItemStack world = new ItemStack(Material.BOWL, 1);
        ItemMeta but = world.getItemMeta();
        but.setDisplayName("World");
        but.setItemModel(Bowl.VM_WORLD.getKey());
        world.setItemMeta(but);
        // x
        ItemStack x = new ItemStack(Material.BOWL, 1);
        ItemMeta sel = x.getItemMeta();
        sel.setDisplayName("X");
        sel.setItemModel(Bowl.VM_X.getKey());
        x.setItemMeta(sel);
        // y
        ItemStack y = new ItemStack(Material.BOWL, 1);
        ItemMeta hei = y.getItemMeta();
        hei.setDisplayName("Y");
        hei.setItemModel(Bowl.VM_Y.getKey());
        y.setItemMeta(hei);
        // z
        ItemStack z = new ItemStack(Material.BOWL, 1);
        ItemMeta coord = z.getItemMeta();
        coord.setDisplayName("Z");
        coord.setItemModel(Bowl.VM_Z.getKey());
        z.setItemMeta(coord);
        // tachyon level - show different levels depening on % full
        double percent = tachyonLevel / plugin.getVortexConfig().getDouble("tachyon_use.max");
        short durability = (short) (1562 - (percent * 1562));
        ItemStack tach = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        ItemMeta yon = tach.getItemMeta();
        yon.setDisplayName("Tachyon Level");
        int level = (int) (percent * 100);
        List<String> lore = Arrays.asList(level + "%");
        NamespacedKey tachyon = Bowl.PERCENT_HUNDRED.getKey();
        if (level == 0) {
            tachyon = Bowl.PERCENT_ZERO.getKey();
        } else if (level < 11) {
            tachyon = Bowl.PERCENT_TEN.getKey();
        } else if (level < 21) {
            tachyon = Bowl.PERCENT_TWENTY.getKey();
        } else if (level < 31) {
            tachyon = Bowl.PERCENT_THIRTY.getKey();
        } else if (level < 41) {
            tachyon = Bowl.PERCENT_FORTY.getKey();
        } else if (level < 51) {
            tachyon = Bowl.PERCENT_FIFTY.getKey();
        } else if (level < 61) {
            tachyon = Bowl.PERCENT_SIXTY.getKey();
        } else if (level < 71) {
            tachyon = Bowl.PERCENT_SEVENTY.getKey();
        } else if (level < 81) {
            tachyon = Bowl.PERCENT_EIGHTY.getKey();
        } else if (level < 91) {
            tachyon = Bowl.PERCENT_NINETY.getKey();
        }
        yon.setItemModel(tachyon);
        yon.setLore(lore);
        tach.setItemMeta(yon);
        // deprecated, but if resource pack not installed gives a visial indication of tachyon levels
        tach.setDurability(durability);
        // lifesigns
        ItemStack life = new ItemStack(Material.BOWL, 1);
        ItemMeta signs = life.getItemMeta();
        signs.setDisplayName("Lifesigns");
        signs.setItemModel(Bowl.VM_LIFE.getKey());
        life.setItemMeta(signs);
        // warp
        ItemStack warp = new ItemStack(Material.BOWL, 1);
        ItemMeta tol = warp.getItemMeta();
        tol.setDisplayName("Enter Vortex / Save location / Check lifesigns");
        tol.setItemModel(Bowl.CHECK.getKey());
        warp.setItemMeta(tol);
        // beacon
        ItemStack bea = new ItemStack(Material.BOWL, 1);
        ItemMeta con = bea.getItemMeta();
        con.setDisplayName("Beacon signal");
        con.setItemModel(Bowl.VM_BEACON.getKey());
        bea.setItemMeta(con);
        // message
        ItemStack mess = new ItemStack(Material.BOWL, 1);
        ItemMeta age = mess.getItemMeta();
        age.setDisplayName("Messages");
        age.setItemModel(Bowl.VM_MESSAGE.getKey());
        mess.setItemMeta(age);
        // save
        ItemStack save = new ItemStack(Material.BOWL, 1);
        ItemMeta curr = save.getItemMeta();
        curr.setDisplayName("Save current location");
        curr.setItemModel(Bowl.SAVE.getKey());
        save.setItemMeta(curr);
        // load
        ItemStack load = new ItemStack(Material.BOWL, 1);
        ItemMeta disk = load.getItemMeta();
        disk.setDisplayName("Load saved location");
        disk.setItemModel(Bowl.VM_LOAD.getKey());
        load.setItemMeta(disk);
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta win = close.getItemMeta();
        win.setDisplayName("Close");
        win.setItemModel(Bowl.CLOSE.getKey());
        close.setItemMeta(win);
        // next
        ItemStack next = new ItemStack(Material.BOWL, 1);
        ItemMeta cha = next.getItemMeta();
        cha.setDisplayName("Next character");
        cha.setItemModel(Bowl.VM_NEXT.getKey());
        next.setItemMeta(cha);
        // back
        ItemStack prev = new ItemStack(Material.BOWL, 1);
        ItemMeta let = prev.getItemMeta();
        let.setDisplayName("Previous character");
        let.setItemModel(Bowl.VM_PREV.getKey());
        prev.setItemMeta(let);

        ItemStack[] is = {
                null, null, null, null, dis, null, pred, null, null,
                tach, null, world, one, two, three, null, save, null,
                life, null, x, four, five, six, null, load, null,
                null, null, y, seven, eight, nine, null, mess, null,
                null, null, z, star, zero, hash, null, bea, null,
                close, null, null, prev, null, next, null, null, warp
        };
        return is;
    }

    public ItemStack[] getGUI() {
        return gui;
    }
}
