/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.chatGUI;

import me.eccentric_nz.TARDIS.enumeration.Updateable;
import me.eccentric_nz.TARDIS.update.TARDISUpdateableCategory;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISChatGUISpigot implements TARDISChatGUI<TextComponent> {

    private final List<TextComponent> sections = new ArrayList<>();
    private final List<TextComponent> controls = new ArrayList<>();
    private final List<TextComponent> interfaces = new ArrayList<>();
    private final List<TextComponent> locations = new ArrayList<>();
    private final List<TextComponent> sensors = new ArrayList<>();
    private final List<TextComponent> others = new ArrayList<>();

    public TARDISChatGUISpigot() {
        int s = 1;
        int c = 1;
        int i = 1;
        int l = 1;
        int e = 1;
        int o = 1;
        for (TARDISUpdateableCategory category : TARDISUpdateableCategory.values()) {
            sections.add(buildTextComponent(s, category.getName(), category.toString().toLowerCase(), "section"));
            s++;
        }
        for (Updateable updateable : Updateable.values()) {
            // counter, updateable.getDescription(), updateable.getName()
            if (updateable.getCategory() == TARDISUpdateableCategory.CONTROLS) {
                controls.add(buildTextComponent(c, updateable.getDescription(), updateable.getName(), "update"));
                c++;
            }
            if (updateable.getCategory() == TARDISUpdateableCategory.INTERFACES) {
                interfaces.add(buildTextComponent(i, updateable.getDescription(), updateable.getName(), "update"));
                i++;
            }
            if (updateable.getCategory() == TARDISUpdateableCategory.LOCATIONS) {
                locations.add(buildTextComponent(l, updateable.getDescription(), updateable.getName(), "update"));
                l++;
            }
            if (updateable.getCategory() == TARDISUpdateableCategory.SENSORS) {
                sensors.add(buildTextComponent(e, updateable.getDescription(), updateable.getName(), "update"));
                e++;
            }
            if (updateable.getCategory() == TARDISUpdateableCategory.OTHERS) {
                others.add(buildTextComponent(o, updateable.getDescription(), updateable.getName(), "update"));
                o++;
            }
        }
    }

    private TextComponent buildTextComponent(int counter, String description, String name, String command) {
        TextComponent tc = new TextComponent(counter + ". " + description);
        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardis " + command + " " + name));
        return tc;
    }

    @Override
    public List<TextComponent> getSections() {
        return sections;
    }

    @Override
    public List<TextComponent> getControls() {
        return controls;
    }

    @Override
    public List<TextComponent> getInterfaces() {
        return interfaces;
    }

    @Override
    public List<TextComponent> getLocations() {
        return locations;
    }

    @Override
    public List<TextComponent> getSensors() {
        return sensors;
    }

    @Override
    public List<TextComponent> getOthers() {
        return others;
    }
}
