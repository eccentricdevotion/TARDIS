package me.eccentric_nz.TARDIS.paper;

import me.eccentric_nz.tardisregeneration.ComponentSetter;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.Consumables;
import org.bukkit.craftbukkit.v1_21_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class ComponentSetterPaper implements ComponentSetter {
    @Override
    public ItemStack setConsumable(ItemStack is) {
        net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(is);
        stack.applyComponents(DataComponentPatch.builder().set(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK).build());
        return CraftItemStack.asBukkitCopy(stack);
    }
}
