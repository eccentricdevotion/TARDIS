/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.messaging;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class AdventureMessage implements TARDISMessage {

    private TextComponent getModule(TardisModule module) {
        TextColor colour = TextColor.fromHexString(module.getHex());
        return Component
                .text()
                .color(colour)
                .append(Component.text("["))
                .append(Component.text(module.getName()))
                .append(Component.text("]"))
                .append(Component.text(" "))
                .build();
    }

    @Override
    public void sendJenkinsUpdateReady(CommandSender cs, int current, int latest) {
        cs.sendMessage(AdventureComponents.getJenkinsUpdateReady(current, latest));
    }

    @Override
    public void sendUpdateCommand(CommandSender cs) {
        cs.sendMessage(AdventureComponents.getUpdateCommand());
    }

    @Override
    public void sendBuildsBehind(CommandSender cs, int behind) {
        cs.sendMessage(AdventureComponents.getBuildsBehind(behind));
    }

    public void message(Audience audience, TardisModule module, String message) {
        if (audience != null) {
            TextComponent textComponent = getModule(module)
                    .append(Component.text(message, NamedTextColor.WHITE));
            audience.sendMessage(textComponent);
        }
    }

    @Override
    public void message(CommandSender cs, TardisModule module, String message) {
        if (cs != null) {
            TextComponent textComponent = getModule(module)
                    .append(Component.text(message, NamedTextColor.WHITE));
            cs.sendMessage(textComponent);
        }
    }

    @Override
    public void message(CommandSender cs, String message) {
        if (message.length() > TARDISChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH) {
            String[] multiline = TARDISChatPaginator.wordWrap(message);
            cs.sendMessage(multiline);
        } else {
            cs.sendMessage(message);
        }
    }

    @Override
    public void handlesMessage(Player p, String message) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> message(p, TardisModule.HANDLES, message), 2L);
    }

    @Override
    public void handlesSend(Player p, String key) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> message(p, TardisModule.HANDLES, local), 2L);
    }

    @Override
    public void handlesSend(Player p, String key, Object ... subs) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> message(p, TardisModule.HANDLES, String.format(local, subs)), 2L);
    }

    @Override
    public void send(CommandSender cs, TardisModule module, String key, boolean handbrake) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        if (handbrake) {
            message(cs, module, local + " " + TARDIS.plugin.getLanguage().getString("HANDBRAKE_RELEASE"));
        } else {
            message(cs, module, local + " " + TARDIS.plugin.getLanguage().getString("LEAVING_VORTEX"));
        }
    }

    @Override
    public void send(CommandSender cs, TardisModule module, String key, Object ... subs) {
        String local = TARDIS.plugin.getLanguage().getString(key);
        message(cs, module, String.format(local, subs));
    }

    @Override
    public void broadcast(TardisModule module, String message) {
        message(TARDIS.plugin.getServer(), module, message);
    }

    @Override
    public void sendWithColour(CommandSender cs, TardisModule module, String message, String hex) {
        TextColor colour = TextColor.fromHexString(hex);
        TextComponent textComponent = getModule(module)
                .append(Component.text(message, colour));
        cs.sendMessage(textComponent);
    }

    @Override
    public void messageWithColour(CommandSender cs, String message, String hex) {
        TextColor colour = TextColor.fromHexString(hex);
        TextComponent textComponent = Component.text(message, colour);
        cs.sendMessage(textComponent);
    }
}
