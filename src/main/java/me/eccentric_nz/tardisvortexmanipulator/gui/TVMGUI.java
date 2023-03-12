/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TVMGUI {

    private final TARDISVortexManipulator plugin;
    private final int tachyonLevel;
    private final ItemStack[] gui;

    public TVMGUI(TARDISVortexManipulator plugin, int tachyonLevel) {
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
        play.setCustomModelData(108);
        dis.setItemMeta(play);
        // keypad pad
        // 1
        ItemStack one = new ItemStack(Material.BOWL, 1);
        ItemMeta none = one.getItemMeta();
        none.setDisplayName("1");
        none.setCustomModelData(118);
        one.setItemMeta(none);
        // 2 abc
        ItemStack two = new ItemStack(Material.BOWL, 1);
        ItemMeta abc = two.getItemMeta();
        abc.setDisplayName("2");
        abc.setLore(Arrays.asList("abc"));
        abc.setCustomModelData(126);
        two.setItemMeta(abc);
        // 3 def
        ItemStack three = new ItemStack(Material.BOWL, 1);
        ItemMeta def = three.getItemMeta();
        def.setDisplayName("3");
        def.setLore(Arrays.asList("def"));
        def.setCustomModelData(125);
        three.setItemMeta(def);
        // 4 ghi
        ItemStack four = new ItemStack(Material.BOWL, 1);
        ItemMeta ghi = four.getItemMeta();
        ghi.setDisplayName("4");
        ghi.setLore(Arrays.asList("ghi"));
        ghi.setCustomModelData(111);
        four.setItemMeta(ghi);
        // 5 jkl
        ItemStack five = new ItemStack(Material.BOWL, 1);
        ItemMeta jkl = five.getItemMeta();
        jkl.setDisplayName("5");
        jkl.setLore(Arrays.asList("jkl"));
        jkl.setCustomModelData(110);
        five.setItemMeta(jkl);
        // 6 mno
        ItemStack six = new ItemStack(Material.BOWL, 1);
        ItemMeta mno = six.getItemMeta();
        mno.setDisplayName("6");
        mno.setLore(Arrays.asList("mno"));
        mno.setCustomModelData(123);
        six.setItemMeta(mno);
        // 7 pqrs
        ItemStack seven = new ItemStack(Material.BOWL, 1);
        ItemMeta pqrs = seven.getItemMeta();
        pqrs.setDisplayName("7");
        pqrs.setLore(Arrays.asList("pqrs"));
        pqrs.setCustomModelData(122);
        seven.setItemMeta(pqrs);
        // 8 tuv
        ItemStack eight = new ItemStack(Material.BOWL, 1);
        ItemMeta tuv = eight.getItemMeta();
        tuv.setDisplayName("8");
        tuv.setLore(Arrays.asList("tuv"));
        tuv.setCustomModelData(109);
        eight.setItemMeta(tuv);
        // 9 wxyz
        ItemStack nine = new ItemStack(Material.BOWL, 1);
        ItemMeta wxyz = nine.getItemMeta();
        wxyz.setDisplayName("9");
        wxyz.setLore(Arrays.asList("wxyz"));
        wxyz.setCustomModelData(117);
        nine.setItemMeta(wxyz);
        // 0
        ItemStack zero = new ItemStack(Material.BOWL, 1);
        ItemMeta nada = zero.getItemMeta();
        nada.setDisplayName("0");
        nada.setCustomModelData(132);
        zero.setItemMeta(nada);
        // symbols -_*~
        ItemStack hash = new ItemStack(Material.BOWL, 1);
        ItemMeta symbols = hash.getItemMeta();
        symbols.setDisplayName("#");
        symbols.setLore(Arrays.asList("~_-"));
        symbols.setCustomModelData(112);
        hash.setItemMeta(symbols);
        // space
        ItemStack star = new ItemStack(Material.BOWL, 1);
        ItemMeta space = star.getItemMeta();
        space.setDisplayName("*");
        space.setLore(Arrays.asList("Space"));
        space.setCustomModelData(124);
        star.setItemMeta(space);
        // world
        ItemStack world = new ItemStack(Material.BOWL, 1);
        ItemMeta but = world.getItemMeta();
        but.setDisplayName("World");
        but.setCustomModelData(128);
        world.setItemMeta(but);
        // x
        ItemStack x = new ItemStack(Material.BOWL, 1);
        ItemMeta sel = x.getItemMeta();
        sel.setDisplayName("X");
        sel.setCustomModelData(129);
        x.setItemMeta(sel);
        // y
        ItemStack y = new ItemStack(Material.BOWL, 1);
        ItemMeta hei = y.getItemMeta();
        hei.setDisplayName("Y");
        hei.setCustomModelData(130);
        y.setItemMeta(hei);
        // z
        ItemStack z = new ItemStack(Material.BOWL, 1);
        ItemMeta coord = z.getItemMeta();
        coord.setDisplayName("Z");
        coord.setCustomModelData(131);
        z.setItemMeta(coord);
        // tachyon level - TODO show different levels depening on % full
        double percent = tachyonLevel / plugin.getConfig().getDouble("tachyon_use.max");
        short durability = (short) (1562 - (percent * 1562));
        ItemStack tach = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        ItemMeta yon = tach.getItemMeta();
        yon.setDisplayName("Tachyon Level");
        int level = (int) (percent * 100);
        List<String> lore = Arrays.asList(level + "%");
        int cmd = 105;
        if (level == 0) {
            cmd = 95;
        } else if (level < 11) {
            cmd = 96;
        } else if (level < 21) {
            cmd = 97;
        } else if (level < 31) {
            cmd = 98;
        } else if (level < 41) {
            cmd = 99;
        } else if (level < 51) {
            cmd = 100;
        } else if (level < 61) {
            cmd = 101;
        } else if (level < 71) {
            cmd = 102;
        } else if (level < 81) {
            cmd = 103;
        } else if (level < 91) {
            cmd = 104;
        }
        yon.setCustomModelData(cmd);
        yon.setLore(lore);
        tach.setItemMeta(yon);
        tach.setDurability(durability);
        // lifesigns
        ItemStack life = new ItemStack(Material.BOWL, 1);
        ItemMeta signs = life.getItemMeta();
        signs.setDisplayName("Lifesigns");
        signs.setCustomModelData(113);
        life.setItemMeta(signs);
        // warp
        ItemStack warp = new ItemStack(Material.BOWL, 1);
        ItemMeta tol = warp.getItemMeta();
        tol.setDisplayName("Enter Vortex / Save location / Check lifesigns");
        tol.setCustomModelData(127);
        warp.setItemMeta(tol);
        // beacon
        ItemStack bea = new ItemStack(Material.BOWL, 1);
        ItemMeta con = bea.getItemMeta();
        con.setDisplayName("Beacon signal");
        con.setCustomModelData(106);
        bea.setItemMeta(con);
        // message
        ItemStack mess = new ItemStack(Material.BOWL, 1);
        ItemMeta age = mess.getItemMeta();
        age.setDisplayName("Messages");
        age.setCustomModelData(115);
        mess.setItemMeta(age);
        // save
        ItemStack save = new ItemStack(Material.BOWL, 1);
        ItemMeta curr = save.getItemMeta();
        curr.setDisplayName("Save current location");
        curr.setCustomModelData(74);
        save.setItemMeta(curr);
        // load
        ItemStack load = new ItemStack(Material.BOWL, 1);
        ItemMeta disk = load.getItemMeta();
        disk.setDisplayName("Load saved location");
        disk.setCustomModelData(114);
        load.setItemMeta(disk);
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta win = close.getItemMeta();
        win.setDisplayName("Close");
        win.setCustomModelData(1);
        close.setItemMeta(win);
        // next
        ItemStack next = new ItemStack(Material.BOWL, 1);
        ItemMeta cha = next.getItemMeta();
        cha.setDisplayName("Next character");
        cha.setCustomModelData(116);
        next.setItemMeta(cha);
        // back
        ItemStack prev = new ItemStack(Material.BOWL, 1);
        ItemMeta let = prev.getItemMeta();
        let.setDisplayName("Previous character");
        let.setCustomModelData(120);
        prev.setItemMeta(let);

        ItemStack[] is = {
                null, null, null, null, dis, null, null, null, null,
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
