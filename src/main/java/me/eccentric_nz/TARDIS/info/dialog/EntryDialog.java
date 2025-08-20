/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.info.dialog;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import me.eccentric_nz.TARDIS.info.TISCategory;
import me.eccentric_nz.TARDIS.info.processors.EntryProcessor;
import me.eccentric_nz.TARDIS.info.processors.SectionProcessor;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class EntryDialog {

    public Dialog create(TARDISInfoMenu tardisInfoMenu, TISCategory category) {
        TreeSet<TARDISInfoMenu> entries = getChildren(tardisInfoMenu);
        if (!entries.isEmpty()) {
            DialogBase dialogData = DialogBase.create(Component.text(tardisInfoMenu.getName()), null, true, true, DialogBase.DialogAfterAction.CLOSE, List.of(), List.of());
            List<ActionButton> actions = new ArrayList<>();
            for (TARDISInfoMenu key : entries) {
                actions.add(makeButton(key));
            }
            DialogAction action = DialogAction.staticAction(ClickEvent.callback(
                    audience -> audience.showDialog(new SectionDialog().create(category))
            ));
            ActionButton backButton = ActionButton.create(Component.text("Back"), null, 150, action);
            return Dialog.create(builder -> builder.empty()
                    .base(dialogData)
                    .type(DialogType.multiAction(actions, backButton, 1))
            );
        }
        return null;
    }

    private TreeSet<TARDISInfoMenu> getChildren(TARDISInfoMenu parent) {
        TreeSet<TARDISInfoMenu> children = new TreeSet<>();
        for (TARDISInfoMenu tardisInfoMenu : TARDISInfoMenu.values()) {
            if (!tardisInfoMenu.getParent().isEmpty() && TARDISInfoMenu.valueOf(tardisInfoMenu.getParent()).equals(parent)) {
                children.add(tardisInfoMenu);
            }
        }
        return children;
    }

    private ActionButton makeButton(TARDISInfoMenu tardisInfoMenu) {
        DialogAction action = DialogAction.customClick((response, audience) -> {
                    Player player = audience instanceof Player ? (Player) audience : null;
                    if (tardisInfoMenu.toString().endsWith("_ITEMS")) {
                        new SectionProcessor(TARDIS.plugin, player).showDialog("SKARO_ITEMS");
                    } else {
                        new EntryProcessor(TARDIS.plugin, player).showInfoOrRecipe(tardisInfoMenu.toString());
                    }
                },
                ClickCallback.Options.builder().build()
        );
        return ActionButton.create(Component.text(TARDISStringUtils.capitalise(tardisInfoMenu.toString())), null, 150, action);
    }
}
