/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.schematic;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BundleContents;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FadesDialog {

    private final TARDIS plugin;

    public FadesDialog(TARDIS plugin) {
        this.plugin = plugin;
    }

    public Dialog create() {
        List<DialogBody> body = new ArrayList<>();
        body.add(DialogBody.plainMessage(Component.text("Hover over the block icons to preview each fade:")));
        body.add(DialogBody.plainMessage(Component.text("")));
        List<ActionButton> actions = new ArrayList<>();
        for (Map.Entry<Material, Material[]> gradient : plugin.getBuildKeeper().getFade().getGradients().entrySet()) {
            List<ItemStack> blocks = new ArrayList<>();
            for (Material m : gradient.getValue()) {
                blocks.add(new ItemStack(m));
            }
            Key key = gradient.getKey().key();
            ItemStack displayItem = new ItemStack(Material.LIGHT_GRAY_BUNDLE);
            displayItem.setData(DataComponentTypes.CUSTOM_NAME, Component.text(TARDISStringUtils.capitalise(key.value())));
            displayItem.setData(DataComponentTypes.BUNDLE_CONTENTS, BundleContents.bundleContents(blocks));
            displayItem.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                    .addHiddenComponents(TARDISConstants.HIDE)
                    .build());
            displayItem.setData(DataComponentTypes.ITEM_MODEL, key);
            ActionButton button = ActionButton.builder(Component.text(TARDISStringUtils.capitalise(key.value())))
                    .action(DialogAction.staticAction(ClickEvent.callback(
                            audience -> setSeedLore(audience, key.value()))
                    )).build();
            actions.add(button);
            body.add(DialogBody.item(displayItem, DialogBody.plainMessage(Component.text(TARDISStringUtils.capitalise(key.value()))), false, true, 16, 16));
        }
        actions.add(ActionButton.builder(Component.text("Cancel"))
                .action(null) // Simply closes the dialogue UI window
                .build());
        DialogBase dialogData = DialogBase.create(Component.text("Fade Interior Chooser"), null, true, true, DialogBase.DialogAfterAction.CLOSE, body, List.of());
        return Dialog.create(builder -> builder.empty()
                .base(dialogData)
                .type(DialogType.multiAction(actions, null, 2))
        );
    }

    private void setSeedLore(Audience audience, String value) {
        if (audience instanceof Player player) {
            // get the seed block in the player's main hand
            ItemStack is = player.getInventory().getItemInMainHand();
            if (is.getType() != Material.ORANGE_POPLAR_LEAVES) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "FADE_SEED");
                return;
            }
            if (!is.hasData(DataComponentTypes.CUSTOM_NAME)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "FADE_SEED");
                return;
            }
            if (!"TARDIS Seed Block".equals(ComponentUtils.stripColour(is.getData(DataComponentTypes.CUSTOM_NAME)))) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "FADE_SEED");
                return;
            }
            ItemLore lore = is.getData(DataComponentTypes.LORE);
            if (lore == null) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "FADE_SEED");
                return;
            }
            List<Component> lines = new ArrayList<>(lore.lines());
            if (lines.size() > 4) {
                lines.set(4, Component.text("Gradient: " + value.toUpperCase(Locale.ROOT)));
            } else {
                lines.add(Component.text("Gradient: " + value.toUpperCase(Locale.ROOT)));
            }
            is.setData(DataComponentTypes.LORE, ItemLore.lore().addLines(lines));
            plugin.getMessenger().send(player, TardisModule.TARDIS, "FADE_SET");
        }
    }
}
