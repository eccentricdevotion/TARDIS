/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.tardis.chatGUI;

import me.eccentric_nz.tardis.enumeration.Updateable;
import me.eccentric_nz.tardis.update.TARDISUpdateableCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISChatGUIJSON {

	// counter, updateable.getDescription(), updateable.getName()
	private final String REPLACEMENT_STRING = "{\"text\":\"%s. %s\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update %s\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}";

	private final String EGG = "{\"text\":\"[tardis] \",\"color\":\"gold\",\"extra\":[{\"text\":\"Look at these eyebrows. These are attack eyebrows! They could take off bottle caps!\",\"color\":\"white\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis egg\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}]}";

	private final String TRANSMAT_LOCATION = "{\"text\":\"%s \",\"color\":\"green\",\"extra\":[{\"text\":\"X: %.2f, Y: %.2f, Z: %.2f, Yaw %.2f\",\"color\":\"white\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis transmat tp %s\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Transmat to this location\"}}}]}";

	private final List<String> sections = Arrays.asList("{\"text\":\"1. tardis Controls\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis section controls\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}", "{\"text\":\"2. tardis User Interfaces\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis section interfaces\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}", "{\"text\":\"3. tardis Internal Spawn Locations\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis section locations\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}", "{\"text\":\"4. Others\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis section others\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}");

	private final List<String> controls = new ArrayList<>();
	private final List<String> interfaces = new ArrayList<>();
	private final List<String> locations = new ArrayList<>();
	private final List<String> others = new ArrayList<>();

	public TARDISChatGUIJSON() {
		int c = 1;
		int i = 1;
		int l = 1;
		int o = 1;
		for (Updateable updateable : Updateable.values()) {
			if (updateable.getCategory() == TARDISUpdateableCategory.CONTROLS) {
				controls.add(String.format(REPLACEMENT_STRING, c, updateable.getDescription(), updateable.getName()));
				c++;
			}
			if (updateable.getCategory() == TARDISUpdateableCategory.INTERFACES) {
				interfaces.add(String.format(REPLACEMENT_STRING, i, updateable.getDescription(), updateable.getName()));
				i++;
			}
			if (updateable.getCategory() == TARDISUpdateableCategory.LOCATIONS) {
				locations.add(String.format(REPLACEMENT_STRING, l, updateable.getDescription(), updateable.getName()));
				l++;
			}
			if (updateable.getCategory() == TARDISUpdateableCategory.OTHERS) {
				others.add(String.format(REPLACEMENT_STRING, o, updateable.getDescription(), updateable.getName()));
				o++;
			}
		}
	}

	public List<String> getSections() {
		return sections;
	}

	public List<String> getControls() {
		return controls;
	}

	List<String> getInterfaces() {
		return interfaces;
	}

	public List<String> getLocations() {
		return locations;
	}

	public List<String> getOthers() {
		return others;
	}

	public String getEgg() {
		return EGG;
	}

	public String getTransmatLocation() {
		return TRANSMAT_LOCATION;
	}
}
