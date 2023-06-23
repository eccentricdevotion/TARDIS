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
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.data.Area;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.data.Transmat;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.handles.wiki.WikiLink;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class AdventureComponents {

    private static final String[] quotes = new String[]{"It's bigger on the inside", "It's smaller on the outside", "May contain bugs!"};

    public static TextComponent getModule(TardisModule module) {
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

    public static TextComponent getJenkinsUpdateReady(int current, int latest) {
        return Component.text("There is a new TARDIS build! You are using ", NamedTextColor.RED)
                .append(Component.text("#" + current, NamedTextColor.GOLD))
                .append(Component.text(", the latest build is ", NamedTextColor.RED))
                .append(Component.text("#" + latest, NamedTextColor.GOLD))
                .append(Component.text("!", NamedTextColor.RED));
    }

    public static TextComponent getUpdateCommand() {
        return Component.text("Visit ", NamedTextColor.GOLD)
                .append(Component.text("http://tardisjenkins.duckdns.org:8080/job/TARDIS/")
                        .clickEvent(ClickEvent.openUrl("http://tardisjenkins.duckdns.org:8080/job/TARDIS/"))
                )
                .append(Component.text(" or run the "))
                .append(Component.text("'/tardisadmin update_plugins'")
                        .color(NamedTextColor.AQUA)
                        .clickEvent(ClickEvent.suggestCommand("/tardisadmin update_plugins"))
                )
                .append(Component.text(" command"));
    }

    public static TextComponent getBuildsBehind(int behind) {
        // TODO check this
        return getModule(TardisModule.TARDIS)
                .color(NamedTextColor.WHITE)
                .append(Component.text("You are "))
                .append(Component.text(behind))
                .append(Component.text(" builds behind! Type "))
                .append(Component.text("/tadmin update_plugins", NamedTextColor.AQUA)
                        .clickEvent(ClickEvent.suggestCommand("/tadmin update_plugins"))
                )
                .append(Component.text("to update!"));
    }

    public static TextComponent getRequestComehereAccept(String key, String command) {
        TextComponent textComponent = Component.text(key, NamedTextColor.AQUA)
                .hoverEvent(HoverEvent.showText(Component.text("Click me!")))
                .clickEvent(ClickEvent.runCommand(command));
        return textComponent;
    }

    public static TextComponent getAbandoned(int i, String owner, String location, int id) {
        TextComponent textComponent = Component.text(i + ". Abandoned by: " + owner + ", " + location)
                .append(Component.text(" < Enter > ")
                        .color(NamedTextColor.GREEN)
                        .hoverEvent(HoverEvent.showText(Component.text("Click to enter this TARDIS")))
                        .clickEvent(ClickEvent.runCommand("/tardisadmin enter " + id))
                );
        return textComponent;
    }

    public static TextComponent getTransmat(Transmat t) {
        TextComponent textComponent = Component.text(t.getName(), NamedTextColor.GREEN)
                .append(Component.text(String.format(" X: %.2f, Y: %.2f, Z: %.2f, Yaw %.2f", t.getX(), t.getY(), t.getZ(), t.getYaw()), NamedTextColor.WHITE))
                .append(Component.text(" <Transmat> ", NamedTextColor.AQUA)
                        .hoverEvent(HoverEvent.showText(Component.text("Transmat to this location")))
                        .clickEvent(ClickEvent.runCommand("/tardis transmat tp " + t.getName()))
                );
        return textComponent;
    }

    public static TextComponent getTARDISForList(Tardis t, String world, int x, int y, int z) {
        TextComponent textComponent = Component.text(String.format("%s %s", t.getTardis_id(), t.getOwner()), NamedTextColor.GREEN)
                .hoverEvent(HoverEvent.showText(Component.text(String.format("%s %s, %s, %s", world, x, y, z))))
                .clickEvent(ClickEvent.runCommand("/tardisadmin enter " + t.getTardis_id()));
        return textComponent;
    }

    public static TextComponent getExterminate(TARDIS plugin) {
        TextComponent textComponent = Component.text(plugin.getLanguage().getString("EXTERMINATE_CONFIRM"), NamedTextColor.AQUA)
                .hoverEvent(HoverEvent.showText(Component.text("Click me!")))
                .clickEvent(ClickEvent.runCommand("/tardis exterminate 6z@3=V!Q7*/O_OB^"));
        return textComponent;
    }

    public static TextComponent getRescue(TARDIS plugin) {
        TextComponent textComponent = Component.text(plugin.getLanguage().getString("REQUEST_COMEHERE_ACCEPT"), NamedTextColor.AQUA)
                .hoverEvent(HoverEvent.showText(Component.text("Click me!")))
                .clickEvent(ClickEvent.runCommand("tardis rescue accept"));
        return textComponent;
    }

    public static TextComponent getSuggestCommand(String item, String hover, String colour) {
        TextComponent textComponent = Component.text(item, TextColor.fromHexString(colour))
                .hoverEvent(HoverEvent.showText(Component.text(hover)))
                .clickEvent(ClickEvent.suggestCommand("/tardisgive [player] " + item + " "));
        return textComponent;
    }

    public static TextComponent getRunCommand(String item, String hover, String colour) {
        TextComponent textComponent = Component.text(item, TextColor.fromHexString(colour))
                .hoverEvent(HoverEvent.showText(Component.text(hover)))
                .clickEvent(ClickEvent.runCommand("/tardisrecipe " + item + " "));
        return textComponent;
    }

    public static TextComponent getShowMore(String command) {
        TextComponent textComponent = Component.text("Show more...")
                .hoverEvent(HoverEvent.showText(Component.text("Click me!")))
                .clickEvent(ClickEvent.runCommand("/" + command + " list_more"));
        return textComponent;
    }

    public static TextComponent getRecharger(String recharger, String world, String x, String y, String z, boolean hasPerm) {
        TextComponent textComponent;
        if (hasPerm) {
            textComponent = Component.text(recharger, NamedTextColor.GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text(String.format("%s %s, %s, %s", world, x, y, z))))
                    .clickEvent(ClickEvent.runCommand(String.format("/tardisteleport %s %s %s not_for_players", world, x, z)));
        } else {
            textComponent = Component.text(recharger, NamedTextColor.GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text(String.format("%s %s, %s, %s", world, x, y, z))));
        }
        return textComponent;
    }

    public static TextComponent getHome(TARDIS plugin, String world, int x, int y, int z) {
        TextComponent textComponent = Component.text(plugin.getLanguage().getString("HOME"), NamedTextColor.GREEN)
                .hoverEvent(HoverEvent.showText(Component.text(String.format("%s %s, %s, %s", world, x, y, z))))
                .clickEvent(ClickEvent.runCommand("/tardistravel home"));
        return textComponent;
    }

    public static TextComponent getSave(HashMap<String, String> map, String world) {
        TextComponent textComponent = Component.text(map.get("dest_name"), NamedTextColor.GREEN)
                .hoverEvent(HoverEvent.showText(Component.text(String.format("%s %s, %s, %s", world, map.get("x"), map.get("y"), map.get("z")))))
                .clickEvent(ClickEvent.runCommand("/tardistravel save " + map.get("dest_name")));
        return textComponent;
    }

    public static TextComponent getArea(Area a, int n, boolean hasPerm) {
        TextComponent textComponent;
        if (hasPerm) {
            textComponent = Component.text(n + ". [" + a.getAreaName() + "] in world: " + a.getWorld())
                    .hoverEvent(HoverEvent.showText(Component.text("Click to /tardistravel here")))
                    .clickEvent(ClickEvent.runCommand(String.format("/tardistravel area %s", a.getAreaName())));
        } else {
            textComponent = Component.text(n + ". [" + a.getAreaName() + "] in world: " + a.getWorld());
        }
        return textComponent;
    }

    public static TextComponent getRoom(String room, boolean hasPerm) {
        NamedTextColor colour = (hasPerm) ? NamedTextColor.GREEN : NamedTextColor.RED;
        TextComponent textComponent = Component.text("    " + room, colour)
                .hoverEvent(HoverEvent.showText(Component.text("Click me!")))
                .clickEvent(ClickEvent.suggestCommand("/tardis room " + room));
        return textComponent;
    }

    public static TextComponent getRoomGallery() {
        TextComponent textComponent = Component.text("https://eccentricdevotion.github.io/TARDIS/room-gallery", NamedTextColor.AQUA)
                .hoverEvent(HoverEvent.showText(Component.text("Click me!")))
                .clickEvent(ClickEvent.openUrl("https://eccentricdevotion.github.io/TARDIS/room-gallery"));
        return textComponent;
    }

    public static TextComponent getEyebrows() {
        TextComponent textComponent = getModule(TardisModule.TARDIS)
                .append(Component.text("Look at these eyebrows. These are attack eyebrows! They could take off bottle caps!")
                        .hoverEvent(HoverEvent.showText(Component.text("Click me!")))
                        .clickEvent(ClickEvent.runCommand("/tardis egg")));
        return textComponent;
    }

    public static TextComponent getSign() {
        TextComponent textComponent = Component.text("Click the link to view the TARDIS wiki: ")
                .append(Component.text("https://eccentricdevotion.github.io/TARDIS/site-map.html")
                        .color(NamedTextColor.GREEN)
                        .hoverEvent(HoverEvent.showText(Component.text("Click me!")))
                        .clickEvent(ClickEvent.openUrl("https://eccentricdevotion.github.io/TARDIS/site-map.html"))
                );
        return textComponent;
    }

    public static TextComponent getUpdate(String first, String value, String split) {
        TextComponent textComponent = Component.text(first, NamedTextColor.GOLD)
                .append(Component.text(value, NamedTextColor.WHITE))
                .append(Component.text(split, NamedTextColor.GOLD))
                .hoverEvent(HoverEvent.showText(Component.text("Click me!")))
                .clickEvent(ClickEvent.runCommand("/tardisinfo " + value));
        return textComponent;
    }

    public static TextComponent getHADS(TARDIS plugin) {
        TextComponent textComponent = getModule(TardisModule.TARDIS)
                .append(Component.text("H", NamedTextColor.RED))
                .append(Component.text("ostile ", NamedTextColor.WHITE))
                .append(Component.text("A", NamedTextColor.RED))
                .append(Component.text("ction ", NamedTextColor.WHITE))
                .append(Component.text("D", NamedTextColor.RED))
                .append(Component.text("isplacement ", NamedTextColor.WHITE))
                .append(Component.text("S", NamedTextColor.RED))
                .append(Component.text("ystem " + plugin.getLanguage().getString("HADS_ENGAGED"), NamedTextColor.WHITE));
        return textComponent;
    }

    public static TextComponent getColouredCommand(String which, String command, TARDIS plugin) {
        String[] split = plugin.getLanguage().getString(which).split("%s");
        TextComponent textComponent = getModule(TardisModule.TARDIS)
                .append(Component.text(split[0], NamedTextColor.WHITE))
                .append(Component.text(command, NamedTextColor.GREEN)
                        .hoverEvent(HoverEvent.showText(Component.text("Click me!")))
                        .clickEvent(ClickEvent.suggestCommand(command))
                )
                .append(Component.text(split[1], NamedTextColor.WHITE));
        return textComponent;
    }

    public static TextComponent getInsertColour(String local, String which, TARDIS plugin) {
        String[] split = plugin.getLanguage().getString(local).split("%s");
        TextComponent textComponent = getModule(TardisModule.TARDIS)
                .append(Component.text(split[0], NamedTextColor.WHITE))
                .append(Component.text(which, NamedTextColor.AQUA))
                .append(Component.text(split[1], NamedTextColor.WHITE));
        return textComponent;
    }

    public static TextComponent getWithColours(String first, String colour, String last, String hue) {
        TextComponent textComponent = Component.text(first, TextColor.fromHexString(colour))
                .append(Component.text(last, TextColor.fromHexString(hue)));
        return textComponent;
    }

    public static TextComponent getWithColours(TardisModule module, String first, String colour, String last, String hue) {
        TextComponent textComponent = getModule(module)
                .append(Component.text(first, TextColor.fromHexString(colour)))
                .append(Component.text(last, TextColor.fromHexString(hue)));
        return textComponent;
    }

    public static TextComponent getCommand(String root, String command) {
        TextComponent textComponent = Component.text(root, NamedTextColor.GOLD)
                .append(Component.text(" commands - use ", NamedTextColor.WHITE))
                .append(Component.text(command + " ", NamedTextColor.AQUA)
                        .hoverEvent(HoverEvent.showText(Component.text("Click me!")))
                        .clickEvent(ClickEvent.suggestCommand(command))
                )
                .append(Component.text("for more info", NamedTextColor.WHITE));
        return textComponent;
    }

    public static TextComponent getWikiLink(WikiLink wikiLink) {
        TextComponent textComponent = Component.text(wikiLink.getTitle(), NamedTextColor.BLUE)
                .hoverEvent(HoverEvent.showText(Component.text("Open link")))
                .clickEvent(ClickEvent.openUrl(wikiLink.getURL()));
        return textComponent;
    }

    public static Component getStartupBanner() {
        Component tardisTop = Component.text()
                .append(Component.text("▀█▀ ▄▀▄ █▀▄ █▀▄ █ ▄▀▀", NamedTextColor.GOLD))
                .build();
        Component tardisBottom = Component.text()
                .append(Component.text(" █  █▀█ █▀▄ █▄▀ █ ▄▄▀", NamedTextColor.GOLD))
                .build();
        String quote = quotes[TARDISConstants.RANDOM.nextInt(3)];
        Component tardisQuote = Component.text(quote, NamedTextColor.AQUA);

        // "  ‗‗≡‗‗"
        // "  |‡|‡|    "
        // "  |☒|•|    "
        // "  | | |    "
        // "  ═════"

        return Component.join(JoinConfiguration.newlines(),
                Component.newline()
                        .append(Component.text("  ‗‗", NamedTextColor.BLUE))
                        .append(Component.text("≡", NamedTextColor.WHITE))
                        .append(Component.text("‗‗", NamedTextColor.BLUE)),
                Component.text()
                        .append(Component.text("  |", NamedTextColor.BLUE))
                        .append(Component.text("‡", NamedTextColor.WHITE))
                        .append(Component.text("|", NamedTextColor.BLUE))
                        .append(Component.text("‡", NamedTextColor.WHITE))
                        .append(Component.text("|    ", NamedTextColor.BLUE))
                        .append(tardisTop)
                        .build(),
                Component.text()
                        .append(Component.text("  |", NamedTextColor.BLUE))
                        .append(Component.text("☒", NamedTextColor.DARK_GRAY))
                        .append(Component.text("|", NamedTextColor.BLUE))
                        .append(Component.text("•", NamedTextColor.WHITE))
                        .append(Component.text("|    ", NamedTextColor.BLUE))
                        .append(tardisBottom)
                        .build(),
                Component.text()
                        .append(Component.text("  | | |    ", NamedTextColor.BLUE))
                        .append(tardisQuote)
                        .build(),
                Component.text()
                        .append(Component.text("  ═════", NamedTextColor.BLUE))
                        .build(),
                Component.empty()
        );
    }
}

