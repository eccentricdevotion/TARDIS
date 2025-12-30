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
package me.eccentric_nz.TARDIS.commands.config;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ConfigDialog {

    private final TARDIS plugin;

    public ConfigDialog(TARDIS plugin) {
        this.plugin = plugin;
    }

    public Dialog create() {
        List<DialogBody> body = (List.of(DialogBody.plainMessage(Component.text("Choose a config section below:"), 150)));
        DialogBase dialogData = DialogBase.create(Component.text("TARDIS Configuration"), null, true, true, DialogBase.DialogAfterAction.CLOSE, body, List.of());
        List<ActionButton> actions = new ArrayList<>();
        for (String key : plugin.getConfig().getKeys(false)) {
            if (key.equals("debug") || key.equals("conversions")) {
                continue;
            }
            DialogAction action = DialogAction.customClick((response, audience) -> {
                        Player player = audience instanceof Player ? (Player) audience : null;
                        new ConfigSectionProcessor(TARDIS.plugin, player).showDialog(key);
                    },
                    ClickCallback.Options.builder().build()
            );
            String comment = plugin.getConfig().getComments(key).getFirst();
            ActionButton button = ActionButton.create(Component.text(TARDISStringUtils.capitalise(key)), Component.text(TARDISStringUtils.uppercaseFirst(comment)), 150, action);
            actions.add(button);
        }
        ActionButton doneButton = ActionButton.create(Component.text("Done"), null, 150, null);
        return Dialog.create(builder -> builder.empty()
                .base(dialogData)
                .type(DialogType.multiAction(actions, doneButton, 2))
        );
    }
}
