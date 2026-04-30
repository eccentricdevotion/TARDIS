package me.eccentric_nz.TARDIS.lazarus;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIGeneticManipulator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LazarusItems {

    public void addItems(TARDIS plugin, ItemStack[] stacks, int current) {
        if (stacks.length < 54) {
            return;
        }
        String previousLore = "";
        String nextLore = "";
        switch (current) {
            case 1 -> { // passive
                previousLore = "Monsters";
                nextLore = "Neutral Mobs";
            }
            case 2 -> { // neutral
                previousLore = "Passive Mobs";
                nextLore = "Hostile Mobs";
            }
            case 3 -> { // hostile
                previousLore = "Neutral Mobs";
                nextLore = "Hostile Adjacent Mobs";
            }
            case 4 -> { // adjacent
                previousLore = "Hostile Mobs";
                nextLore = "Doctors";
            }
            case 5 -> { // doctors
                previousLore = "Hostile Adjacent Mobs";
                nextLore = "Companions";
            }
            case 6 -> { // companions
                previousLore = "Doctors";
                nextLore = "Characters";
            }
            case 7 -> { // characters
                previousLore = "Companions";
                nextLore = "Monsters";
            }
            case 8 -> { // monsters
                previousLore = "Characters";
                nextLore = "Passive Mobs";
            }
        }
        // previous
        ItemStack previous = ItemStack.of(GUIGeneticManipulator.BUTTON_PREVIOUS.material(), 1);
        previous.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Previous page"));
        previous.lore(List.of(Component.text(previousLore)));
        stacks[GUIGeneticManipulator.BUTTON_PREVIOUS.slot()] = previous;
        // back to Lazarus choices button
        ItemStack back = ItemStack.of(GUIGeneticManipulator.BUTTON_BACK.material(), 1);
        back.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Back"));
        stacks[GUIGeneticManipulator.BUTTON_BACK.slot()] = back;
        // next
        ItemStack next = ItemStack.of(GUIGeneticManipulator.BUTTON_NEXT.material(), 1);
        next.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Next page"));
        next.lore(List.of(Component.text(nextLore)));
        stacks[GUIGeneticManipulator.BUTTON_NEXT.slot()] = next;
        // add options
        if (current < 5) {
            // adult
            ItemStack adult = ItemStack.of(GUIGeneticManipulator.BUTTON_AGE.material(), 1);
            adult.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_AGE", "Age")));
            adult.lore(List.of(Component.text("ADULT")));
            stacks[GUIGeneticManipulator.BUTTON_AGE.slot()] = adult;
            // type
            ItemStack typ = ItemStack.of(GUIGeneticManipulator.BUTTON_TYPE.material(), 1);
            typ.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_TYPE", "Type/Colour")));
            typ.lore(List.of(Component.text("WHITE")));
            stacks[GUIGeneticManipulator.BUTTON_TYPE.slot()] = typ;
            // tamed
            ItemStack tamed = ItemStack.of(GUIGeneticManipulator.BUTTON_OPTS.material(), 1);
            tamed.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_OPTS", "Disguise Options")));
            List<Component> opts = new ArrayList<>();
            for (String o : plugin.getLanguage().getString("BUTTON_OPTS_LIST", "Tamed/Flying/Blazing/Powered/Beaming/Aggressive/Chest carrying/Decorated")
                    .split("/")) {
                opts.add(Component.text(o).decorate(TextDecoration.ITALIC));
            }
            opts.add(Component.text("FALSE", NamedTextColor.RED));
            tamed.setData(DataComponentTypes.LORE, ItemLore.lore(opts));
            stacks[GUIGeneticManipulator.BUTTON_OPTS.slot()] = tamed;
        }
        // add buttons
        ItemStack remove = ItemStack.of(GUIGeneticManipulator.BUTTON_RESTORE.material(), 1);
        remove.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_RESTORE", "Restore my original genetic material")));
        stacks[GUIGeneticManipulator.BUTTON_RESTORE.slot()] = remove;
        // set
        ItemStack set = ItemStack.of(GUIGeneticManipulator.BUTTON_DNA.material(), 1);
        set.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_DNA", "Modify my genetic material")));
        stacks[GUIGeneticManipulator.BUTTON_DNA.slot()] = set;
        // cancel
        ItemStack cancel = ItemStack.of(GUIGeneticManipulator.BUTTON_CANCEL.material(), 1);
        cancel.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_CANCEL", "Cancel")));
        stacks[GUIGeneticManipulator.BUTTON_CANCEL.slot()] = cancel;
    }
}
