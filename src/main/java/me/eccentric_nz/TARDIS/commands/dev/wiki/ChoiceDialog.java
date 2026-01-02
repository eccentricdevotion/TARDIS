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
package me.eccentric_nz.TARDIS.commands.dev.wiki;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.eccentric_nz.TARDIS.handles.wiki.SearchDialog;
import me.eccentric_nz.TARDIS.info.dialog.CategoryDialog;
import me.eccentric_nz.TARDIS.travel.dialog.TerminalDialog;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import java.util.ArrayList;
import java.util.List;

public class ChoiceDialog {

    public Dialog create() {
        DialogBase dialogData = DialogBase.create(Component.text("Dialogs"), null, true, true, DialogBase.DialogAfterAction.CLOSE, List.of(), List.of());
        DialogAction terminalAction = DialogAction.staticAction(ClickEvent.callback(
                audience -> audience.showDialog(new TerminalDialog().create())
        ));
        DialogAction infoAction = DialogAction.staticAction(ClickEvent.callback(
                audience -> audience.showDialog(new CategoryDialog().create())
        ));
        DialogAction wikiAction = DialogAction.staticAction(ClickEvent.callback(
                audience -> audience.showDialog(new SearchDialog().create())
        ));
        DialogAction bitmapAction = DialogAction.staticAction(ClickEvent.callback(
                audience -> audience.showDialog(new BitmapDialog().create())
        ));
        ActionButton terminalButton = ActionButton.create(Component.text("Terminal"), null, 150, terminalAction);
        ActionButton infoButton = ActionButton.create(Component.text("TIS"), null, 150, infoAction);
        ActionButton wikiButton = ActionButton.create(Component.text("Wiki"), null, 150, wikiAction);
        ActionButton bitmapButton = ActionButton.create(Component.text("Bitmap"), null, 150, bitmapAction);
        List<ActionButton> actions = new ArrayList<>();
        actions.add(terminalButton);
        actions.add(infoButton);
        actions.add(wikiButton);
        actions.add(bitmapButton);
        return Dialog.create(builder -> builder.empty()
                .base(dialogData)
                .type(DialogType.multiAction(actions, null, 1))
        );
    }
}
