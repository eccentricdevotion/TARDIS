/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.VortexManipulatorVariant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
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
public class TVMGUI implements InventoryHolder {

    private final TARDIS plugin;
    private final int tachyonLevel;
    private final Inventory inventory;

    public TVMGUI(TARDIS plugin, int tachyonLevel) {
        this.plugin = plugin;
        this.tachyonLevel = tachyonLevel;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Vortex Manipulator", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Vortex Manipulator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        // display
        ItemStack dis = ItemStack.of(Material.BOWL, 1);
        ItemMeta play = dis.getItemMeta();
        play.displayName(Component.text("Display"));
        play.lore(List.of(Component.empty()));
        dis.setItemMeta(play);
        // predictive world
        ItemStack pred = ItemStack.of(Material.BOWL, 1);
        ItemMeta ict = dis.getItemMeta();
        ict.displayName(Component.text("Predictive text"));
        ict.lore(List.of(Component.empty()));
        pred.setItemMeta(ict);
        // keypad pad
        // 1
        ItemStack one = ItemStack.of(Material.BOWL, 1);
        ItemMeta none = one.getItemMeta();
        none.displayName(Component.text("1"));
        one.setItemMeta(none);
        // 2 abc
        ItemStack two = ItemStack.of(Material.BOWL, 1);
        ItemMeta abc = two.getItemMeta();
        abc.displayName(Component.text("2"));
        abc.lore(List.of(Component.text("abc")));
        two.setItemMeta(abc);
        // 3 def
        ItemStack three = ItemStack.of(Material.BOWL, 1);
        ItemMeta def = three.getItemMeta();
        def.displayName(Component.text("3"));
        def.lore(List.of(Component.text("def")));
        three.setItemMeta(def);
        // 4 ghi
        ItemStack four = ItemStack.of(Material.BOWL, 1);
        ItemMeta ghi = four.getItemMeta();
        ghi.displayName(Component.text("4"));
        ghi.lore(List.of(Component.text("ghi")));
        four.setItemMeta(ghi);
        // 5 jkl
        ItemStack five = ItemStack.of(Material.BOWL, 1);
        ItemMeta jkl = five.getItemMeta();
        jkl.displayName(Component.text("5"));
        jkl.lore(List.of(Component.text("jkl")));
        five.setItemMeta(jkl);
        // 6 mno
        ItemStack six = ItemStack.of(Material.BOWL, 1);
        ItemMeta mno = six.getItemMeta();
        mno.displayName(Component.text("6"));
        mno.lore(List.of(Component.text("mno")));
        six.setItemMeta(mno);
        // 7 pqrs
        ItemStack seven = ItemStack.of(Material.BOWL, 1);
        ItemMeta pqrs = seven.getItemMeta();
        pqrs.displayName(Component.text("7"));
        pqrs.lore(List.of(Component.text("pqrs")));
        seven.setItemMeta(pqrs);
        // 8 tuv
        ItemStack eight = ItemStack.of(Material.BOWL, 1);
        ItemMeta tuv = eight.getItemMeta();
        tuv.displayName(Component.text("8"));
        tuv.lore(List.of(Component.text("tuv")));
        eight.setItemMeta(tuv);
        // 9 wxyz
        ItemStack nine = ItemStack.of(Material.BOWL, 1);
        ItemMeta wxyz = nine.getItemMeta();
        wxyz.displayName(Component.text("9"));
        wxyz.lore(List.of(Component.text("wxyz")));
        nine.setItemMeta(wxyz);
        // 0
        ItemStack zero = ItemStack.of(Material.BOWL, 1);
        ItemMeta nada = zero.getItemMeta();
        nada.displayName(Component.text("0"));
        zero.setItemMeta(nada);
        // symbols -_*~
        ItemStack hash = ItemStack.of(Material.BOWL, 1);
        ItemMeta symbols = hash.getItemMeta();
        symbols.displayName(Component.text("#"));
        symbols.lore(List.of(Component.text("~_-")));
        hash.setItemMeta(symbols);
        // space
        ItemStack star = ItemStack.of(Material.BOWL, 1);
        ItemMeta space = star.getItemMeta();
        space.displayName(Component.text("*"));
        space.lore(List.of(Component.text("Space")));
        star.setItemMeta(space);
        // world
        ItemStack world = ItemStack.of(Material.BOWL, 1);
        ItemMeta but = world.getItemMeta();
        but.displayName(Component.text("World"));
        world.setItemMeta(but);
        // x
        ItemStack x = ItemStack.of(Material.BOWL, 1);
        ItemMeta sel = x.getItemMeta();
        sel.displayName(Component.text("X"));
        x.setItemMeta(sel);
        // y
        ItemStack y = ItemStack.of(Material.BOWL, 1);
        ItemMeta hei = y.getItemMeta();
        hei.displayName(Component.text("Y"));
        y.setItemMeta(hei);
        // z
        ItemStack z = ItemStack.of(Material.BOWL, 1);
        ItemMeta coord = z.getItemMeta();
        coord.displayName(Component.text("Z"));
        z.setItemMeta(coord);
        // tachyon level - show different levels depening on % full
        double percent = tachyonLevel / plugin.getVortexConfig().getDouble("tachyon_use.max");
        short durability = (short) (1562 - (percent * 1562));
        ItemStack tach = ItemStack.of(Material.DIAMOND_PICKAXE, 1);
        ItemMeta yon = tach.getItemMeta();
        yon.displayName(Component.text("Tachyon Level"));
        int level = (int) (percent * 100);
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
        yon.lore(List.of(Component.text(level + "%")));
        yon.addItemFlags(ItemFlag.values());
        yon.setAttributeModifiers(Multimaps.forMap(Map.of()));
        Damageable damageable = (Damageable) yon;
        damageable.setDamage(durability);
        tach.setItemMeta(damageable);
        // lifesigns
        ItemStack life = ItemStack.of(Material.BOWL, 1);
        ItemMeta signs = life.getItemMeta();
        signs.displayName(Component.text("Lifesigns"));
        life.setItemMeta(signs);
        // warp
        ItemStack warp = ItemStack.of(Material.BOWL, 1);
        ItemMeta tol = warp.getItemMeta();
        tol.displayName(Component.text("Enter Vortex / Save location / Check lifesigns"));
        warp.setItemMeta(tol);
        // beacon
        ItemStack bea = ItemStack.of(Material.BOWL, 1);
        ItemMeta con = bea.getItemMeta();
        con.displayName(Component.text("Beacon signal"));
        bea.setItemMeta(con);
        // message
        ItemStack mess = ItemStack.of(Material.BOWL, 1);
        ItemMeta age = mess.getItemMeta();
        age.displayName(Component.text("Messages"));
        mess.setItemMeta(age);
        // save
        ItemStack save = ItemStack.of(Material.BOWL, 1);
        ItemMeta curr = save.getItemMeta();
        curr.displayName(Component.text("Save current location"));
        save.setItemMeta(curr);
        // load
        ItemStack load = ItemStack.of(Material.BOWL, 1);
        ItemMeta disk = load.getItemMeta();
        disk.displayName(Component.text("Load saved location"));
        load.setItemMeta(disk);
        // close
        ItemStack close = ItemStack.of(Material.BOWL, 1);
        ItemMeta win = close.getItemMeta();
        win.displayName(Component.text("Close"));
        close.setItemMeta(win);
        // next
        ItemStack next = ItemStack.of(Material.BOWL, 1);
        ItemMeta cha = next.getItemMeta();
        cha.displayName(Component.text("Next character"));
        next.setItemMeta(cha);
        // back
        ItemStack prev = ItemStack.of(Material.BOWL, 1);
        ItemMeta let = prev.getItemMeta();
        let.displayName(Component.text("Previous character"));
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
}
