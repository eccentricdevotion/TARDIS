/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.VortexManipulatorVariant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
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
        play.displayName(Component.text("Display"));
        play.lore(List.of(Component.text("")));
        play.setItemModel(VortexManipulatorVariant.DISPLAY.getKey());
        dis.setItemMeta(play);
        // predictive world
        ItemStack pred = new ItemStack(Material.BOWL, 1);
        ItemMeta ict = dis.getItemMeta();
        ict.displayName(Component.text("Predictive Text"));
        ict.lore(List.of(Component.text("")));
        ict.setItemModel(VortexManipulatorVariant.PREDICTIVE.getKey());
        pred.setItemMeta(ict);
        // keypad pad
        // 1
        ItemStack one = new ItemStack(Material.BOWL, 1);
        ItemMeta none = one.getItemMeta();
        none.displayName(Component.text("1"));
        none.setItemModel(VortexManipulatorVariant.ONE.getKey());
        one.setItemMeta(none);
        // 2 abc
        ItemStack two = new ItemStack(Material.BOWL, 1);
        ItemMeta abc = two.getItemMeta();
        abc.displayName(Component.text("2"));
        abc.lore(List.of(Component.text("abc")));
        abc.setItemModel(VortexManipulatorVariant.TWO.getKey());
        two.setItemMeta(abc);
        // 3 def
        ItemStack three = new ItemStack(Material.BOWL, 1);
        ItemMeta def = three.getItemMeta();
        def.displayName(Component.text("3"));
        def.lore(List.of(Component.text("def")));
        def.setItemModel(VortexManipulatorVariant.THREE.getKey());
        three.setItemMeta(def);
        // 4 ghi
        ItemStack four = new ItemStack(Material.BOWL, 1);
        ItemMeta ghi = four.getItemMeta();
        ghi.displayName(Component.text("4"));
        ghi.lore(List.of(Component.text("ghi")));
        ghi.setItemModel(VortexManipulatorVariant.FOUR.getKey());
        four.setItemMeta(ghi);
        // 5 jkl
        ItemStack five = new ItemStack(Material.BOWL, 1);
        ItemMeta jkl = five.getItemMeta();
        jkl.displayName(Component.text("5"));
        jkl.lore(List.of(Component.text("jkl")));
        jkl.setItemModel(VortexManipulatorVariant.FIVE.getKey());
        five.setItemMeta(jkl);
        // 6 mno
        ItemStack six = new ItemStack(Material.BOWL, 1);
        ItemMeta mno = six.getItemMeta();
        mno.displayName(Component.text("6"));
        mno.lore(List.of(Component.text("mno")));
        mno.setItemModel(VortexManipulatorVariant.SIX.getKey());
        six.setItemMeta(mno);
        // 7 pqrs
        ItemStack seven = new ItemStack(Material.BOWL, 1);
        ItemMeta pqrs = seven.getItemMeta();
        pqrs.displayName(Component.text("7"));
        pqrs.lore(List.of(Component.text("pqrs")));
        pqrs.setItemModel(VortexManipulatorVariant.SEVEN.getKey());
        seven.setItemMeta(pqrs);
        // 8 tuv
        ItemStack eight = new ItemStack(Material.BOWL, 1);
        ItemMeta tuv = eight.getItemMeta();
        tuv.displayName(Component.text("8"));
        tuv.lore(List.of(Component.text("tuv")));
        tuv.setItemModel(VortexManipulatorVariant.EIGHT.getKey());
        eight.setItemMeta(tuv);
        // 9 wxyz
        ItemStack nine = new ItemStack(Material.BOWL, 1);
        ItemMeta wxyz = nine.getItemMeta();
        wxyz.displayName(Component.text("9"));
        wxyz.lore(List.of(Component.text("wxyz")));
        wxyz.setItemModel(VortexManipulatorVariant.NINE.getKey());
        nine.setItemMeta(wxyz);
        // 0
        ItemStack zero = new ItemStack(Material.BOWL, 1);
        ItemMeta nada = zero.getItemMeta();
        nada.displayName(Component.text("0"));
        nada.setItemModel(VortexManipulatorVariant.ZERO.getKey());
        zero.setItemMeta(nada);
        // symbols -_*~
        ItemStack hash = new ItemStack(Material.BOWL, 1);
        ItemMeta symbols = hash.getItemMeta();
        symbols.displayName(Component.text("#"));
        symbols.lore(List.of(Component.text("~_-")));
        symbols.setItemModel(VortexManipulatorVariant.HASH.getKey());
        hash.setItemMeta(symbols);
        // space
        ItemStack star = new ItemStack(Material.BOWL, 1);
        ItemMeta space = star.getItemMeta();
        space.displayName(Component.text("*"));
        space.lore(List.of(Component.text("Space")));
        space.setItemModel(VortexManipulatorVariant.STAR.getKey());
        star.setItemMeta(space);
        // world
        ItemStack world = new ItemStack(Material.BOWL, 1);
        ItemMeta but = world.getItemMeta();
        but.displayName(Component.text("World"));
        but.setItemModel(VortexManipulatorVariant.WORLD.getKey());
        world.setItemMeta(but);
        // x
        ItemStack x = new ItemStack(Material.BOWL, 1);
        ItemMeta sel = x.getItemMeta();
        sel.displayName(Component.text("X"));
        sel.setItemModel(VortexManipulatorVariant.X.getKey());
        x.setItemMeta(sel);
        // y
        ItemStack y = new ItemStack(Material.BOWL, 1);
        ItemMeta hei = y.getItemMeta();
        hei.displayName(Component.text("Y"));
        hei.setItemModel(VortexManipulatorVariant.Y.getKey());
        y.setItemMeta(hei);
        // z
        ItemStack z = new ItemStack(Material.BOWL, 1);
        ItemMeta coord = z.getItemMeta();
        coord.displayName(Component.text("Z"));
        coord.setItemModel(VortexManipulatorVariant.Z.getKey());
        z.setItemMeta(coord);
        // tachyon level - show different levels depening on % full
        double percent = tachyonLevel / plugin.getVortexConfig().getDouble("tachyon_use.max");
        short durability = (short) (1562 - (percent * 1562));
        ItemStack tach = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        ItemMeta yon = tach.getItemMeta();
        yon.displayName(Component.text("Tachyon Level"));
        int level = (int) (percent * 100);
        NamespacedKey tachyon = VortexManipulatorVariant.PERCENT_HUNDRED.getKey();
        if (level == 0) {
            tachyon = VortexManipulatorVariant.PERCENT_ZERO.getKey();
        } else if (level < 11) {
            tachyon = VortexManipulatorVariant.PERCENT_TEN.getKey();
        } else if (level < 21) {
            tachyon = VortexManipulatorVariant.PERCENT_TWENTY.getKey();
        } else if (level < 31) {
            tachyon = VortexManipulatorVariant.PERCENT_THIRTY.getKey();
        } else if (level < 41) {
            tachyon = VortexManipulatorVariant.PERCENT_FORTY.getKey();
        } else if (level < 51) {
            tachyon = VortexManipulatorVariant.PERCENT_FIFTY.getKey();
        } else if (level < 61) {
            tachyon = VortexManipulatorVariant.PERCENT_SIXTY.getKey();
        } else if (level < 71) {
            tachyon = VortexManipulatorVariant.PERCENT_SEVENTY.getKey();
        } else if (level < 81) {
            tachyon = VortexManipulatorVariant.PERCENT_EIGHTY.getKey();
        } else if (level < 91) {
            tachyon = VortexManipulatorVariant.PERCENT_NINETY.getKey();
        }
        yon.setItemModel(tachyon);
        yon.lore(List.of(Component.text(level + "%")));
        tach.setItemMeta(yon);
        // deprecated, but if resource pack not installed gives a visial indication of tachyon levels
        tach.setDurability(durability);
        // lifesigns
        ItemStack life = new ItemStack(Material.BOWL, 1);
        ItemMeta signs = life.getItemMeta();
        signs.displayName(Component.text("Lifesigns"));
        signs.setItemModel(VortexManipulatorVariant.LIFE.getKey());
        life.setItemMeta(signs);
        // warp
        ItemStack warp = new ItemStack(Material.BOWL, 1);
        ItemMeta tol = warp.getItemMeta();
        tol.displayName(Component.text("Enter Vortex / Save location / Check lifesigns"));
        tol.setItemModel(GuiVariant.CHECK.getKey());
        warp.setItemMeta(tol);
        // beacon
        ItemStack bea = new ItemStack(Material.BOWL, 1);
        ItemMeta con = bea.getItemMeta();
        con.displayName(Component.text("Beacon signal"));
        con.setItemModel(VortexManipulatorVariant.BEACON.getKey());
        bea.setItemMeta(con);
        // message
        ItemStack mess = new ItemStack(Material.BOWL, 1);
        ItemMeta age = mess.getItemMeta();
        age.displayName(Component.text("Messages"));
        age.setItemModel(VortexManipulatorVariant.MESSAGE.getKey());
        mess.setItemMeta(age);
        // save
        ItemStack save = new ItemStack(Material.BOWL, 1);
        ItemMeta curr = save.getItemMeta();
        curr.displayName(Component.text("Save current location"));
        curr.setItemModel(GuiVariant.SAVE.getKey());
        save.setItemMeta(curr);
        // load
        ItemStack load = new ItemStack(Material.BOWL, 1);
        ItemMeta disk = load.getItemMeta();
        disk.displayName(Component.text("Load saved location"));
        disk.setItemModel(VortexManipulatorVariant.LOAD.getKey());
        load.setItemMeta(disk);
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta win = close.getItemMeta();
        win.displayName(Component.text("Close"));
        win.setItemModel(GuiVariant.CLOSE.getKey());
        close.setItemMeta(win);
        // next
        ItemStack next = new ItemStack(Material.BOWL, 1);
        ItemMeta cha = next.getItemMeta();
        cha.displayName(Component.text("Next character"));
        cha.setItemModel(VortexManipulatorVariant.NEXT.getKey());
        next.setItemMeta(cha);
        // back
        ItemStack prev = new ItemStack(Material.BOWL, 1);
        ItemMeta let = prev.getItemMeta();
        let.displayName(Component.text("Previous character"));
        let.setItemModel(VortexManipulatorVariant.PREV.getKey());
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
