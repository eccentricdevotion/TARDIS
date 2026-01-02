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
package me.eccentric_nz.TARDIS.utility;

import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.util.Locale;

public class ComponentUtils {

    public static TextComponent toWhite(String plain) {
        return Component.text(plain, NamedTextColor.WHITE).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public static TextComponent toGold(String plain) {
        return Component.text(plain, NamedTextColor.GOLD).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public static String stripColour(Component component) {
        return component == null ? "" : ((TextComponent) component).content();
    }

    public static boolean endsWith(Component component, String end) {
        return stripColour(component).endsWith(end);
    }

    public static boolean startsWith(Component component, String start) {
        return stripColour(component).startsWith(start);
    }

    public static int parseInt(Component component) {
        return Integer.parseInt(stripColour(component));
    }

    public static boolean parseBoolean(Component component) {
        return Boolean.parseBoolean(stripColour(component));
    }

    public static String toEnumUppercase(Component component) {
        return stripColour(component).replace(" ", "_").replace("-", "_").replace("3", "THREE").toUpperCase(Locale.ROOT);
    }

    public static JsonElement getJson(Component component) {
        GsonComponentSerializer serializer = GsonComponentSerializer.gson();
        return serializer.serializeToTree(component);
    }

    public static Component fromJson(JsonElement element) {
        GsonComponentSerializer serializer = GsonComponentSerializer.gson();
        return serializer.deserializeFromTree(element);
    }
}
