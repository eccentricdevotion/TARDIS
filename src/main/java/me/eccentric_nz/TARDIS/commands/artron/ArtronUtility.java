package me.eccentric_nz.TARDIS.commands.artron;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArtronUtility {

    public static ItemStack hasCell(TARDIS plugin, Player player) {
        ItemStack is = player.getInventory().getItemInMainHand();
        if (!is.hasItemMeta()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_IN_HAND");
            return null;
        }
        if (is.getAmount() > 1) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_ONE");
            return null;
        }
        if (!is.hasData(DataComponentTypes.CUSTOM_NAME) || !ComponentUtils.endsWith(is.getData(DataComponentTypes.CUSTOM_NAME), "Artron Storage Cell")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_IN_HAND");
            return null;
        }
        return is;
    }

    public static int getLevel(ItemStack is) {
        String lore = ComponentUtils.stripColour(is.getData(DataComponentTypes.LORE).lines().get(1));
        return TARDISNumberParsers.parseInt(lore);
    }

    public static void setLevel(ItemStack is, int level, Player player, boolean main) {
        List<Component> lore = new ArrayList<>(is.getData(DataComponentTypes.LORE).lines());
        lore.set(1, Component.text(level));
        is.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
        // add glint if missing
        if (main && !is.hasData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE)) {
            is.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
        }
        if (main) {
            player.getInventory().setItemInMainHand(is);
        } else {
            // remove enchantment glint if level <= 0
            if (level <= 0) {
                is.unsetData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);
            }
            player.getInventory().setItemInOffHand(is);
        }
    }

    public static void chargeCell(TARDIS plugin, ItemStack is, Player player, int amount, String table) {
        List<Component> lore = new ArrayList<>(is.getData(DataComponentTypes.LORE).lines());
        int level = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.get(1)));
        if (level < 0) {
            level = 0;
        }
        int max = plugin.getArtronConfig().getInt("full_charge");
        int new_amount = amount + level;
        if (new_amount > max) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_NO_CHARGE", String.format("%d", (max - level)));
            return;
        }
        lore.set(1, Component.text(new_amount));
        is.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
        is.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
        is.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                .addHiddenComponents(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                .hideTooltip(true)
                .build());
        // remove the energy from the tardis/timelord
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        plugin.getQueryFactory().alterEnergyLevel(table, -amount, where, player);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_CHARGED", String.format("%d", new_amount));
    }
}
