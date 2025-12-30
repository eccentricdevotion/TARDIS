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
package me.eccentric_nz.TARDIS.travel.dialog;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.entity.Player;

import java.util.List;

public class TerminalDialog {

    public Dialog create() {
        List<DialogBody> body = List.of(DialogBody.plainMessage(Component.text("Set a step distance, the x and z coordinates, a distance multiplier, and a world type."), 200));
        List<DialogInput> inputs = List.of(
                DialogInput.numberRange("x", 200, Component.text("X"), "options.generic_value", -500f, 500f, 0f, 50f),
                DialogInput.numberRange("z", 200, Component.text("Z"), "options.generic_value", -500f, 500f, 0f, 50f),
                DialogInput.numberRange("multiplier", 200, Component.text("Multiplier"), "options.generic_value", 1f, 10f, 1f, 1f),
                DialogInput.singleOption("environment", 200, List.of(
                        SingleOptionDialogInput.OptionEntry.create("CURRENT", Component.text("Current world"), true),
                        SingleOptionDialogInput.OptionEntry.create("NORMAL", Component.text("An Overworld"), false),
                        SingleOptionDialogInput.OptionEntry.create("NETHER", Component.text("A Nether world"), false),
                        SingleOptionDialogInput.OptionEntry.create("THE_END", Component.text("An End world"), false)
                ), Component.text("Environment"), true),
                DialogInput.bool("submarine", Component.text("Submarine"), false, "true", "false")
        );
        DialogAction action = DialogAction.customClick((response, audience) -> {
                    Player player = audience instanceof Player ? (Player) audience : null;
                    new TARDISTerminalDialogProcessor(TARDIS.plugin, player).process(response);
                },
                ClickCallback.Options.builder().build()
        );
        DialogBase dialogData = DialogBase.create(Component.text("Destination Terminal"), null, true, true, DialogBase.DialogAfterAction.CLOSE, body, inputs);
        ActionButton button = ActionButton.create(Component.text("Set destination"), null, 150, action);
        return Dialog.create(builder -> builder.empty()
                .base(dialogData)
                .type(DialogType.notice(button))
        );
    }
}
