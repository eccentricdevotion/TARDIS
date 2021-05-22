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
package me.eccentric_nz.tardis.messaging;

import me.eccentric_nz.tardis.TARDIS;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * The TARDISChatPaginator takes a raw string of arbitrary length and breaks it down into an array of strings
 * appropriate for displaying on the Minecraft player console.
 */
public class TARDISChatPaginator {

	static int GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH = TARDIS.plugin.getConfig().getInt("preferences.chat_width"); // Will never wrap, even with the largest characters

	/**
	 * Breaks a raw string up into a series of lines. Words are wrapped using spaces as decimeters and the newline
	 * character is respected.
	 *
	 * @param rawString The raw string to break.
	 * @return An array of word-wrapped lines.
	 */
	static String[] wordWrap(String rawString) {
		// A null string is a single line
		if (rawString == null) {
			return new String[]{""};
		}
		// A string shorter than the lineWidth is a single line
		if (rawString.length() <= GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH && !rawString.contains("\n")) {
			return new String[]{rawString};
		}
		char[] rawChars = (rawString + ' ').toCharArray(); // add a trailing space to trigger pagination
		StringBuilder word = new StringBuilder();
		StringBuilder line = new StringBuilder();
		List<String> lines = new LinkedList<>();
		int lineColorChars = 0;
		for (int i = 0; i < rawChars.length; i++) {
			char c = rawChars[i];
			// skip chat color modifiers
			if (c == ChatColor.COLOR_CHAR) {
				word.append(ChatColor.getByChar(rawChars[i + 1]));
				lineColorChars += 2;
				i++; // Eat the next character as we have already processed it
				continue;
			}
			if (c == ' ' || c == '\n') {
				if (line.length() == 0 && word.length() - lineColorChars > GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH) { // special case: extremely long word begins a line
					lines.addAll(Arrays.asList(word.toString().split("(?<=\\G.{" + GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH + "})")));
				} else if (line.length() > 0 && line.length() + 1 + word.length() - lineColorChars > GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH) { // Line too long...break the line
					for (String partialWord : word.toString().split("(?<=\\G.{" + GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH + "})")) {
						lines.add(line.toString());
						line = new StringBuilder(partialWord);
					}
					lineColorChars = 0;
				} else {
					if (line.length() > 0) {
						line.append(' ');
					}
					line.append(word);
				}
				word = new StringBuilder();

				if (c == '\n') { // Newline forces the line to flush
					lines.add(line.toString());
					line = new StringBuilder();
				}
			} else {
				word.append(c);
			}
		}
		if (line.length() > 0) { // Only add the last line if there is anything to add
			lines.add(line.toString());
		}
		// Iterate over the wrapped lines, applying the last color from one line to the beginning of the next
		if (lines.get(0).length() == 0 || lines.get(0).charAt(0) != ChatColor.COLOR_CHAR) {
			lines.set(0, ChatColor.WHITE + lines.get(0));
		}
		for (int i = 1; i < lines.size(); i++) {
			String pLine = lines.get(i - 1);
			String subLine = lines.get(i);

			char color = pLine.charAt(pLine.lastIndexOf(ChatColor.COLOR_CHAR) + 1);
			if (subLine.length() == 0 || subLine.charAt(0) != ChatColor.COLOR_CHAR) {
				lines.set(i, ChatColor.getByChar(color) + subLine);
			}
		}
		return lines.toArray(new String[0]);
	}

	public static void setGuaranteedNoWrapChatPageWidth(int guaranteedNoWrapChatPageWidth) {
		GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH = guaranteedNoWrapChatPageWidth;
	}
}
