package me.eccentric_nz.TARDIS.lazarus;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonPoliceBoxes;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.custommodeldata.GUIGeneticManipulator;
import me.eccentric_nz.TARDIS.custommodeldata.keys.*;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TARDISWeepingAngelsMonstersInventory {

    private final ItemStack[] monsters;
    private final TARDIS plugin;

    public TARDISWeepingAngelsMonstersInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.monsters = getItemStack();
    }

    /**
     * Constructs an inventory for the Genetic Manipulator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[54];
        int i = 0;
        for (Monster monster : Monster.values()) {
            ItemStack mon = new ItemStack(monster.getMaterial(), 1);
            ItemMeta ster = mon.getItemMeta();
            ster.setDisplayName(monster.toString());
            NamespacedKey model = null;
            switch (monster) {
                case CLOCKWORK_DROID -> model = DroidVariant.CLOCKWORK_DROID.getKey();
                case CYBERMAN -> model = CybermanVariant.CYBERMAN.getKey();
                case DALEK -> model = DalekVariant.DALEK.getKey();
                case DALEK_SEC -> model = DalekSecVariant.DALEK_SEC.getKey();
                case DAVROS -> model = DavrosVariant.DAVROS.getKey();
                case EMPTY_CHILD -> model = EmptyChildVariant.EMPTY_CHILD.getKey();
                case HATH -> model = HathVariant.HATH.getKey();
                case HEADLESS_MONK -> model = MonkVariant.HEADLESS_MONK.getKey();
                case ICE_WARRIOR -> model = IceWarriorVariant.ICE_WARRIOR.getKey();
                case JUDOON -> model = JudoonVariant.JUDOON.getKey();
                case K9 -> model = K9Variant.BUTTON_K9.getKey();
                case MIRE -> model = MireVariant.THE_MIRE.getKey();
                case OOD -> model = OodVariant.OOD.getKey();
                case OSSIFIED -> model = OssifiedVariant.OSSIFIED.getKey();
                case RACNOSS -> model = RacnossVariant.RACNOSS.getKey();
                case SCARECROW -> model = ScarecrowVariant.SCARECROW.getKey();
                case SEA_DEVIL -> model = SeaDevilVariant.SEA_DEVIL.getKey();
                case SILENT -> model = SilentVariant.BUTTON_SILENT.getKey();
                case SILURIAN -> model = SilurianVariant.SILURIAN.getKey();
                case SLITHEEN -> model = SlitheenVariant.SLITHEEN.getKey();
                case SONTARAN -> model = SontaranVariant.SONTARAN.getKey();
                case STRAX -> model = StraxVariant.STRAX.getKey();
                case SYCORAX -> model = SycoraxVariant.SYCORAX.getKey();
                case TOCLAFANE -> model = ToclafaneVariant.BUTTON_TOCLAFANE.getKey();
                case VASHTA_NERADA -> model = VashtaNeradaVariant.VASHTA_NERADA.getKey();
                case WEEPING_ANGEL -> model = WeepingAngelVariant.WEEPING_ANGEL.getKey();
                case ZYGON -> model = ZygonVariant.ZYGON.getKey();
            }
            ster.setItemModel(model);
            mon.setItemMeta(ster);
            stacks[i] = mon;
            i++;
        }
        // page one
        ItemStack page1 = new ItemStack(GUIChameleonPoliceBoxes.GO_TO_PAGE_1.material(), 1);
        ItemMeta one = page1.getItemMeta();
        one.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_1"));
        one.setItemModel(GUIChameleonPoliceBoxes.GO_TO_PAGE_1.key());
        page1.setItemMeta(one);
        stacks[42] = page1;
        // page two
        ItemStack page2 = new ItemStack(GUIChameleonPresets.GO_TO_PAGE_2.material(), 1);
        ItemMeta two = page2.getItemMeta();
        two.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_2"));
        two.setItemModel(GUIChameleonPresets.GO_TO_PAGE_2.key());
        page2.setItemMeta(two);
        stacks[43] = page2;
        // add skins
        ItemStack down = new ItemStack(GUIGeneticManipulator.BUTTON_SKINS.material(), 1);
        ItemMeta load = down.getItemMeta();
        load.setDisplayName("TARDIS Television");
        load.setItemModel(GUIGeneticManipulator.BUTTON_SKINS.key());
        down.setItemMeta(load);
        stacks[44] = down;
        // master
        ItemStack the = new ItemStack(GUIGeneticManipulator.BUTTON_MASTER.material(), 1);
        ItemMeta master = the.getItemMeta();
        master.setDisplayName(plugin.getLanguage().getString("BUTTON_MASTER"));
        master.setLore(List.of(plugin.getLanguage().getString("SET_OFF")));
        master.setItemModel(GUIGeneticManipulator.BUTTON_MASTER.key());
        the.setItemMeta(master);
        stacks[GUIGeneticManipulator.BUTTON_MASTER.slot()] = the;
        // add buttons
        ItemStack rem = new ItemStack(GUIGeneticManipulator.BUTTON_RESTORE.material(), 1);
        ItemMeta ove = rem.getItemMeta();
        ove.setDisplayName(plugin.getLanguage().getString("BUTTON_RESTORE"));
        ove.setItemModel(GUIGeneticManipulator.BUTTON_RESTORE.key());
        rem.setItemMeta(ove);
        stacks[GUIGeneticManipulator.BUTTON_RESTORE.slot()] = rem;
        // set
        ItemStack s = new ItemStack(GUIGeneticManipulator.BUTTON_DNA.material(), 1);
        ItemMeta sim = s.getItemMeta();
        sim.setDisplayName(plugin.getLanguage().getString("BUTTON_DNA"));
        sim.setItemModel(GUIGeneticManipulator.BUTTON_DNA.key());
        s.setItemMeta(sim);
        stacks[GUIGeneticManipulator.BUTTON_DNA.slot()] = s;
        // cancel
        ItemStack can = new ItemStack(GUIGeneticManipulator.BUTTON_CANCEL.material(), 1);
        ItemMeta cel = can.getItemMeta();
        cel.setDisplayName(plugin.getLanguage().getString("BUTTON_CANCEL"));
        cel.setItemModel(GUIGeneticManipulator.BUTTON_CANCEL.key());
        can.setItemMeta(cel);
        stacks[GUIGeneticManipulator.BUTTON_CANCEL.slot()] = can;

        return stacks;
    }

    public ItemStack[] getMonsters() {
        return monsters;
    }
}
