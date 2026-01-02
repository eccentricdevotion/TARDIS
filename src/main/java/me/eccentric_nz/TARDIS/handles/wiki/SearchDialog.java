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
package me.eccentric_nz.TARDIS.handles.wiki;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.entity.Player;

import java.util.List;

public class SearchDialog {

    public Dialog create() {
        List<DialogInput> inputs = List.of(
                DialogInput.text("search", 200, Component.text("I'm looking for:"), true, "", 32, null)
        );
        DialogAction action = DialogAction.customClick((response, audience) -> {
                    Player player = audience instanceof Player ? (Player) audience : null;
                    new HandlesWikiDialogProcessor(TARDIS.plugin).getLinks(response, player);
                },
                ClickCallback.Options.builder().build()
        );
        DialogBase dialogData = DialogBase.create(Component.text("Search the TARDIS Wiki"), null, true, true, DialogBase.DialogAfterAction.CLOSE, List.of(), inputs);
        ActionButton button = ActionButton.create(Component.text("Search"), null, 150, action);
        return Dialog.create(builder -> builder.empty()
                .base(dialogData)
                .type(DialogType.notice(button))
        );
    }
}
