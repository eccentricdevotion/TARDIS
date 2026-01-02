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
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ConfigSectionProcessor {

    private final TARDIS plugin;
    private final Player player;

    public ConfigSectionProcessor(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void showDialog(String section) {
        List<DialogInput> inputs = new ArrayList<>();
        for (String key : plugin.getConfig().getConfigurationSection(section).getKeys(false)) {
            String value = plugin.getConfig().getString(section + "." + key, "");
            if (value.startsWith("MemorySection")) {
                // get keys
                for (String deep : plugin.getConfig().getConfigurationSection(section + "." + key).getKeys(false)) {
                    String dv = plugin.getConfig().getString(section + "." + key + "." + deep, "");
                    if (dv.equals("true") || dv.equals("false")) {
                        // DialogInput.bool
                        boolean b = plugin.getConfig().getBoolean(section + "." + key + "." + deep);
                        inputs.add(DialogInput.bool(key + "9" + deep, Component.text(TARDISStringUtils.rightPad(deep, 180)), b, "true", "false"));
                    } else {
                        // DialogInput.text
                        inputs.add(DialogInput.text(key + "9" + deep, 200, Component.text(deep), true, dv, value.length() + 16, null));
                    }
                }
            } else if (value.equals("true") || value.equals("false")) {
                // DialogInput.bool
                boolean b = plugin.getConfig().getBoolean(section + "." + key);
                inputs.add(DialogInput.bool(key, Component.text(TARDISStringUtils.rightPad(key, 180)), b, "true", "false"));
            } else {
                // DialogInput.text
                inputs.add(DialogInput.text(key, 200, Component.text(key), true, value, value.length() + 16, null));
            }
        }
        DialogBase dialogData = DialogBase.create(Component.text(TARDISStringUtils.capitalise(section) + " Configuration"), null, true, true, DialogBase.DialogAfterAction.CLOSE, List.of(), inputs);
        DialogAction backAction = DialogAction.staticAction(ClickEvent.callback(
                audience -> audience.showDialog(new ConfigDialog(plugin).create())
        ));
        ActionButton backButton = ActionButton.create(Component.text("Back"), null, 150, backAction);
        DialogAction saveAction = DialogAction.customClick((response, audience) -> {
                    Player player = audience instanceof Player ? (Player) audience : null;
                    new ConfigConfirmationProcessor(TARDIS.plugin, player).show(section, response);
                },
                ClickCallback.Options.builder().build()
        );
        ActionButton saveButton = ActionButton.create(Component.text("Save"), null, 150, saveAction);
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(dialogData)
                .type(DialogType.confirmation(backButton, saveButton))
        );
        Audience.audience(player).showDialog(dialog);
    }
}
