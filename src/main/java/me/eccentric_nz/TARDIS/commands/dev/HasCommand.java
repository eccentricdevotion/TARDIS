package me.eccentric_nz.TARDIS.commands.dev;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardischemistry.lab.Lab;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HasCommand {

    public void check(Player player) {
        ItemStack bleach = ItemStack.of(Material.WHITE_DYE);
        bleach.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Vanilla Bleach"));
        ItemStack model = bleach.clone();
        model.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Bleach Model"));
        model.setData(DataComponentTypes.ITEM_MODEL, Lab.Bleach.getModel());
        model.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("Lore")).build());
        player.getInventory().addItem(bleach, model);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
            int i = 1;
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.hasData(DataComponentTypes.LORE)) {
                    player.sendMessage(Component.text(i + " has lore component"));
                }
                i++;
            }
        }, 10);
    }
}
