/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.custommodels.keys.VortexManipulatorVariant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

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
        ItemStack display = ItemStack.of(Material.BOWL, 1);
        display.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Display"));
        display.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.empty()));
        // predictive world
        ItemStack pred = ItemStack.of(Material.BOWL, 1);
        pred.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Predictive text"));
        pred.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.empty()));
        // keypad pad
        // 1
        ItemStack one = ItemStack.of(Material.BOWL, 1);
        one.setData(DataComponentTypes.CUSTOM_NAME, Component.text("1"));
        // 2 abc
        ItemStack two = ItemStack.of(Material.BOWL, 1);
        two.setData(DataComponentTypes.CUSTOM_NAME, Component.text("2"));
        two.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("abc")).build());
        // 3 def
        ItemStack three = ItemStack.of(Material.BOWL, 1);
        three.setData(DataComponentTypes.CUSTOM_NAME, Component.text("3"));
        three.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("def")).build());
        // 4 ghi
        ItemStack four = ItemStack.of(Material.BOWL, 1);
        four.setData(DataComponentTypes.CUSTOM_NAME, Component.text("4"));
        four.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("ghi")).build());
        // 5 jkl
        ItemStack five = ItemStack.of(Material.BOWL, 1);
        five.setData(DataComponentTypes.CUSTOM_NAME, Component.text("5"));
        five.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("jkl")).build());
        // 6 mno
        ItemStack six = ItemStack.of(Material.BOWL, 1);
        six.setData(DataComponentTypes.CUSTOM_NAME, Component.text("6"));
        six.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("mno")).build());
        // 7 pqrs
        ItemStack seven = ItemStack.of(Material.BOWL, 1);
        seven.setData(DataComponentTypes.CUSTOM_NAME, Component.text("7"));
        seven.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("pqrs")).build());
        // 8 tuv
        ItemStack eight = ItemStack.of(Material.BOWL, 1);
        eight.setData(DataComponentTypes.CUSTOM_NAME, Component.text("8"));
        eight.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("tuv")).build());
        // 9 wxyz
        ItemStack nine = ItemStack.of(Material.BOWL, 1);
        nine.setData(DataComponentTypes.CUSTOM_NAME, Component.text("9"));
        nine.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("wxyz")).build());
        // 0
        ItemStack zero = ItemStack.of(Material.BOWL, 1);
        zero.setData(DataComponentTypes.CUSTOM_NAME, Component.text("0"));
        // symbols -_*~
        ItemStack hash = ItemStack.of(Material.BOWL, 1);
        hash.setData(DataComponentTypes.CUSTOM_NAME, Component.text("#"));
        hash.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("~_-")).build());
        // space
        ItemStack star = ItemStack.of(Material.BOWL, 1);
        star.setData(DataComponentTypes.CUSTOM_NAME, Component.text("*"));
        star.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("Space")).build());
        // world
        ItemStack world = ItemStack.of(Material.BOWL, 1);
        world.setData(DataComponentTypes.CUSTOM_NAME, Component.text("World"));
        // x
        ItemStack x = ItemStack.of(Material.BOWL, 1);
        x.setData(DataComponentTypes.CUSTOM_NAME, Component.text("X"));
        // y
        ItemStack y = ItemStack.of(Material.BOWL, 1);
        y.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Y"));
        // z
        ItemStack z = ItemStack.of(Material.BOWL, 1);
        z.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Z"));
        // tachyon level - show different levels depening on % full
        double percent = tachyonLevel / plugin.getVortexConfig().getDouble("tachyon_use.max");
        int durability = (int) (1562 - (percent * 1562));
        ItemStack tach = ItemStack.of(Material.DIAMOND_PICKAXE, 1);
        tach.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Tachyon Level"));
        int level = (int) (percent * 100);
        CustomModelData.Builder component = CustomModelData.customModelData();
        if (level == 0) {
            component.addFloats(VortexManipulatorVariant.PERCENT_ZERO.getFloats());
        } else if (level < 11) {
            component.addFloats(VortexManipulatorVariant.PERCENT_TEN.getFloats());
        } else if (level < 21) {
            component.addFloats(VortexManipulatorVariant.PERCENT_TWENTY.getFloats());
        } else if (level < 31) {
            component.addFloats(VortexManipulatorVariant.PERCENT_THIRTY.getFloats());
        } else if (level < 41) {
            component.addFloats(VortexManipulatorVariant.PERCENT_FORTY.getFloats());
        } else if (level < 51) {
            component.addFloats(VortexManipulatorVariant.PERCENT_FIFTY.getFloats());
        } else if (level < 61) {
            component.addFloats(VortexManipulatorVariant.PERCENT_SIXTY.getFloats());
        } else if (level < 71) {
            component.addFloats(VortexManipulatorVariant.PERCENT_SEVENTY.getFloats());
        } else if (level < 81) {
            component.addFloats(VortexManipulatorVariant.PERCENT_EIGHTY.getFloats());
        } else if (level < 91) {
            component.addFloats(VortexManipulatorVariant.PERCENT_NINETY.getFloats());
        } else {
            component.addFloats(VortexManipulatorVariant.PERCENT_HUNDRED.getFloats());
        }
        tach.setData(DataComponentTypes.CUSTOM_MODEL_DATA, component.build());
        tach.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(level + "%")));
        tach.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                .addHiddenComponents(TARDISConstants.HIDE)
                .build());
        tach.setData(DataComponentTypes.DAMAGE, durability);
        // lifesigns
        ItemStack life = ItemStack.of(Material.BOWL, 1);
        life.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Lifesigns"));
        // warp
        ItemStack warp = ItemStack.of(Material.BOWL, 1);
        warp.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Enter Vortex / Save location / Check lifesigns"));
        // beacon
        ItemStack bea = ItemStack.of(Material.BOWL, 1);
        bea.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Beacon signal"));
        // message
        ItemStack mess = ItemStack.of(Material.BOWL, 1);
        mess.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Messages"));
        // save
        ItemStack save = ItemStack.of(Material.BOWL, 1);
        save.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Save current location"));
        // load
        ItemStack load = ItemStack.of(Material.BOWL, 1);
        load.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Load saved location"));
        // close
        ItemStack close = GUIItemFactory.close();
        // next
        ItemStack next = ItemStack.of(Material.BOWL, 1);
        next.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Next character"));
        // back
        ItemStack prev = ItemStack.of(Material.BOWL, 1);
        prev.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Previous character"));

        return new ItemStack[]{
                null, null, null, null, display, null, pred, null, null,
                tach, null, world, one, two, three, null, save, null,
                life, null, x, four, five, six, null, load, null,
                null, null, y, seven, eight, nine, null, mess, null,
                null, null, z, star, zero, hash, null, bea, null,
                close, null, null, prev, null, next, null, null, warp
        };
    }
}
