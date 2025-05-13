/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.VortexManipulatorVariant;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;
import java.util.Map;

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
        play.setLore(List.of(""));
        dis.setItemMeta(play);
        // predictive world
        ItemStack pred = new ItemStack(Material.BOWL, 1);
        ItemMeta ict = dis.getItemMeta();
        ict.setDisplayName("Predictive text");
        ict.setLore(List.of(""));
        pred.setItemMeta(ict);
        // keypad pad
        // 1
        ItemStack one = new ItemStack(Material.BOWL, 1);
        ItemMeta none = one.getItemMeta();
        none.setDisplayName("1");
        one.setItemMeta(none);
        // 2 abc
        ItemStack two = new ItemStack(Material.BOWL, 1);
        ItemMeta abc = two.getItemMeta();
        abc.setDisplayName("2");
        abc.setLore(List.of("abc"));
        two.setItemMeta(abc);
        // 3 def
        ItemStack three = new ItemStack(Material.BOWL, 1);
        ItemMeta def = three.getItemMeta();
        def.setDisplayName("3");
        def.setLore(List.of("def"));
        three.setItemMeta(def);
        // 4 ghi
        ItemStack four = new ItemStack(Material.BOWL, 1);
        ItemMeta ghi = four.getItemMeta();
        ghi.setDisplayName("4");
        ghi.setLore(List.of("ghi"));
        four.setItemMeta(ghi);
        // 5 jkl
        ItemStack five = new ItemStack(Material.BOWL, 1);
        ItemMeta jkl = five.getItemMeta();
        jkl.setDisplayName("5");
        jkl.setLore(List.of("jkl"));
        five.setItemMeta(jkl);
        // 6 mno
        ItemStack six = new ItemStack(Material.BOWL, 1);
        ItemMeta mno = six.getItemMeta();
        mno.setDisplayName("6");
        mno.setLore(List.of("mno"));
        six.setItemMeta(mno);
        // 7 pqrs
        ItemStack seven = new ItemStack(Material.BOWL, 1);
        ItemMeta pqrs = seven.getItemMeta();
        pqrs.setDisplayName("7");
        pqrs.setLore(List.of("pqrs"));
        seven.setItemMeta(pqrs);
        // 8 tuv
        ItemStack eight = new ItemStack(Material.BOWL, 1);
        ItemMeta tuv = eight.getItemMeta();
        tuv.setDisplayName("8");
        tuv.setLore(List.of("tuv"));
        eight.setItemMeta(tuv);
        // 9 wxyz
        ItemStack nine = new ItemStack(Material.BOWL, 1);
        ItemMeta wxyz = nine.getItemMeta();
        wxyz.setDisplayName("9");
        wxyz.setLore(List.of("wxyz"));
        nine.setItemMeta(wxyz);
        // 0
        ItemStack zero = new ItemStack(Material.BOWL, 1);
        ItemMeta nada = zero.getItemMeta();
        nada.setDisplayName("0");
        zero.setItemMeta(nada);
        // symbols -_*~
        ItemStack hash = new ItemStack(Material.BOWL, 1);
        ItemMeta symbols = hash.getItemMeta();
        symbols.setDisplayName("#");
        symbols.setLore(List.of("~_-"));
        hash.setItemMeta(symbols);
        // space
        ItemStack star = new ItemStack(Material.BOWL, 1);
        ItemMeta space = star.getItemMeta();
        space.setDisplayName("*");
        space.setLore(List.of("Space"));
        star.setItemMeta(space);
        // world
        ItemStack world = new ItemStack(Material.BOWL, 1);
        ItemMeta but = world.getItemMeta();
        but.setDisplayName("World");
        world.setItemMeta(but);
        // x
        ItemStack x = new ItemStack(Material.BOWL, 1);
        ItemMeta sel = x.getItemMeta();
        sel.setDisplayName("X");
        x.setItemMeta(sel);
        // y
        ItemStack y = new ItemStack(Material.BOWL, 1);
        ItemMeta hei = y.getItemMeta();
        hei.setDisplayName("Y");
        y.setItemMeta(hei);
        // z
        ItemStack z = new ItemStack(Material.BOWL, 1);
        ItemMeta coord = z.getItemMeta();
        coord.setDisplayName("Z");
        z.setItemMeta(coord);
        // tachyon level - show different levels depening on % full
        double percent = tachyonLevel / plugin.getVortexConfig().getDouble("tachyon_use.max");
        short durability = (short) (1562 - (percent * 1562));
        ItemStack tach = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        ItemMeta yon = tach.getItemMeta();
        yon.setDisplayName("Tachyon Level");
        int level = (int) (percent * 100);
        List<String> lore = List.of(level + "%");
        CustomModelDataComponent component = yon.getCustomModelDataComponent();
        if (level == 0) {
            component.setFloats(VortexManipulatorVariant.PERCENT_ZERO.getFloats());
        } else if (level < 11) {
            component.setFloats(VortexManipulatorVariant.PERCENT_TEN.getFloats());
        } else if (level < 21) {
            component.setFloats(VortexManipulatorVariant.PERCENT_TWENTY.getFloats());
        } else if (level < 31) {
            component.setFloats(VortexManipulatorVariant.PERCENT_THIRTY.getFloats());
        } else if (level < 41) {
            component.setFloats(VortexManipulatorVariant.PERCENT_FORTY.getFloats());
        } else if (level < 51) {
            component.setFloats(VortexManipulatorVariant.PERCENT_FIFTY.getFloats());
        } else if (level < 61) {
            component.setFloats(VortexManipulatorVariant.PERCENT_SIXTY.getFloats());
        } else if (level < 71) {
            component.setFloats(VortexManipulatorVariant.PERCENT_SEVENTY.getFloats());
        } else if (level < 81) {
            component.setFloats(VortexManipulatorVariant.PERCENT_EIGHTY.getFloats());
        } else if (level < 91) {
            component.setFloats(VortexManipulatorVariant.PERCENT_NINETY.getFloats());
        } else {
            component.setFloats(VortexManipulatorVariant.PERCENT_HUNDRED.getFloats());
        }
        yon.setCustomModelDataComponent(component);
        yon.setLore(lore);
        yon.addItemFlags(ItemFlag.values());
        yon.setAttributeModifiers(Multimaps.forMap(Map.of()));
        Damageable damageable = (Damageable) yon;
        damageable.setDamage(durability);
        tach.setItemMeta(damageable);
        // lifesigns
        ItemStack life = new ItemStack(Material.BOWL, 1);
        ItemMeta signs = life.getItemMeta();
        signs.setDisplayName("Lifesigns");
        life.setItemMeta(signs);
        // warp
        ItemStack warp = new ItemStack(Material.BOWL, 1);
        ItemMeta tol = warp.getItemMeta();
        tol.setDisplayName("Enter Vortex / Save location / Check lifesigns");
        warp.setItemMeta(tol);
        // beacon
        ItemStack bea = new ItemStack(Material.BOWL, 1);
        ItemMeta con = bea.getItemMeta();
        con.setDisplayName("Beacon signal");
        bea.setItemMeta(con);
        // message
        ItemStack mess = new ItemStack(Material.BOWL, 1);
        ItemMeta age = mess.getItemMeta();
        age.setDisplayName("Messages");
        mess.setItemMeta(age);
        // save
        ItemStack save = new ItemStack(Material.BOWL, 1);
        ItemMeta curr = save.getItemMeta();
        curr.setDisplayName("Save current location");
        save.setItemMeta(curr);
        // load
        ItemStack load = new ItemStack(Material.BOWL, 1);
        ItemMeta disk = load.getItemMeta();
        disk.setDisplayName("Load saved location");
        load.setItemMeta(disk);
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta win = close.getItemMeta();
        win.setDisplayName("Close");
        close.setItemMeta(win);
        // next
        ItemStack next = new ItemStack(Material.BOWL, 1);
        ItemMeta cha = next.getItemMeta();
        cha.setDisplayName("Next character");
        next.setItemMeta(cha);
        // back
        ItemStack prev = new ItemStack(Material.BOWL, 1);
        ItemMeta let = prev.getItemMeta();
        let.setDisplayName("Previous character");
        prev.setItemMeta(let);

        return new ItemStack[]{
                null, null, null, null, dis, null, pred, null, null,
                tach, null, world, one, two, three, null, save, null,
                life, null, x, four, five, six, null, load, null,
                null, null, y, seven, eight, nine, null, mess, null,
                null, null, z, star, zero, hash, null, bea, null,
                close, null, null, prev, null, next, null, null, warp
        };
    }

    public ItemStack[] getGUI() {
        return gui;
    }
}
