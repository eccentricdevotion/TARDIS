package me.eccentric_nz.TARDIS.lazarus;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIGeneticManipulator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
                previousLore = "TARDIS Monsters";
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
                nextLore = "TARDIS Monsters";
            }
            case 8 -> { // monsters
                previousLore = "Characters";
                nextLore = "Passive Mobs";
            }
        }
        // previous
        ItemStack previous = ItemStack.of(GUIGeneticManipulator.BUTTON_PREVIOUS.material(), 1);
        ItemMeta previousItemMeta = previous.getItemMeta();
        previousItemMeta.displayName(Component.text("Previous page"));
        previousItemMeta.lore(List.of(Component.text(previousLore)));
        previous.setItemMeta(previousItemMeta);
        stacks[GUIGeneticManipulator.BUTTON_PREVIOUS.slot()] = previous;
        // back to Lazarus choices button
        ItemStack back = ItemStack.of(GUIGeneticManipulator.BUTTON_BACK.material(), 1);
        ItemMeta backItemMeta = back.getItemMeta();
        backItemMeta.displayName(Component.text("Back"));
        back.setItemMeta(backItemMeta);
        stacks[GUIGeneticManipulator.BUTTON_BACK.slot()] = back;
        // next
        ItemStack next = ItemStack.of(GUIGeneticManipulator.BUTTON_NEXT.material(), 1);
        ItemMeta nextItemMeta = next.getItemMeta();
        nextItemMeta.displayName(Component.text("Next page"));
        nextItemMeta.lore(List.of(Component.text(nextLore)));
        next.setItemMeta(nextItemMeta);
        stacks[GUIGeneticManipulator.BUTTON_NEXT.slot()] = next;
        // add options
        if (current < 5) {
            // adult
            ItemStack adult = ItemStack.of(GUIGeneticManipulator.BUTTON_AGE.material(), 1);
            ItemMeta baby = adult.getItemMeta();
            baby.displayName(Component.text(plugin.getLanguage().getString("BUTTON_AGE", "Age")));
            baby.lore(List.of(Component.text("ADULT")));
            adult.setItemMeta(baby);
            stacks[GUIGeneticManipulator.BUTTON_AGE.slot()] = adult;
            // type
            ItemStack typ = ItemStack.of(GUIGeneticManipulator.BUTTON_TYPE.material(), 1);
            ItemMeta col = typ.getItemMeta();
            col.displayName(Component.text(plugin.getLanguage().getString("BUTTON_TYPE", "Type/Colour")));
            col.lore(List.of(Component.text("WHITE")));
            typ.setItemMeta(col);
            stacks[GUIGeneticManipulator.BUTTON_TYPE.slot()] = typ;
            // tamed
            ItemStack tamed = ItemStack.of(GUIGeneticManipulator.BUTTON_OPTS.material(), 1);
            ItemMeta tf = tamed.getItemMeta();
            tf.displayName(Component.text(plugin.getLanguage().getString("BUTTON_OPTS", "Disguise Options")));
            List<Component> opts = new ArrayList<>();
            for (String o : plugin.getLanguage().getString("BUTTON_OPTS_LIST").split("/")) {
                opts.add(Component.text(o).decorate(TextDecoration.ITALIC));
            }
            opts.add(Component.text("FALSE", NamedTextColor.RED));
            tf.lore(opts);
            tamed.setItemMeta(tf);
            stacks[GUIGeneticManipulator.BUTTON_OPTS.slot()] = tamed;
        }
        // add buttons
        ItemStack rem = ItemStack.of(GUIGeneticManipulator.BUTTON_RESTORE.material(), 1);
        ItemMeta ove = rem.getItemMeta();
        ove.displayName(Component.text(plugin.getLanguage().getString("BUTTON_RESTORE", "Restore my original genetic material")));
        rem.setItemMeta(ove);
        stacks[GUIGeneticManipulator.BUTTON_RESTORE.slot()] = rem;
        // set
        ItemStack s = ItemStack.of(GUIGeneticManipulator.BUTTON_DNA.material(), 1);
        ItemMeta sim = s.getItemMeta();
        sim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_DNA", "Modify my genetic material")));
        s.setItemMeta(sim);
        stacks[GUIGeneticManipulator.BUTTON_DNA.slot()] = s;
        // cancel
        ItemStack can = ItemStack.of(GUIGeneticManipulator.BUTTON_CANCEL.material(), 1);
        ItemMeta cel = can.getItemMeta();
        cel.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CANCEL", "Cancel")));
        can.setItemMeta(cel);
        stacks[GUIGeneticManipulator.BUTTON_CANCEL.slot()] = can;
    }
}
