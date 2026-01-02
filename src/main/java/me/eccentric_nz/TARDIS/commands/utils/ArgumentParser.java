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
package me.eccentric_nz.TARDIS.commands.utils;

import java.util.Collection;
import java.util.List;

public class ArgumentParser {

    /**
     * Characters considered quotation marks
     */
    private static final Collection<Character> QUOTATION_MARKS = List.of('\'', '\"');

    /**
     * Space character
     */
    private static final Character SPACE = ' ';

    /**
     * Adds and clears
     *
     * @param sb        StringBuilder to use and clear
     * @param arguments Arguments to add to
     */
    private static void addAndClear(StringBuilder sb, Arguments arguments) {
        arguments.addArgument(sb.toString().trim());
        sb.setLength(0);
    }

    /**
     * Parses the given string arguments
     *
     * @param raw Raw string
     * @return Parsed arguments
     */
    public Arguments parse(String raw) {
        char[] chars = raw.toCharArray();
        Arguments arguments = new Arguments();
        StringBuilder sb = new StringBuilder();
        boolean quote = false;
        int i = 0;
        for (char c : chars) {
            // increment i
            i++;
            // if we're not in quotes and there's a space, a word is finished
            if (!quote && c == SPACE) {
                addAndClear(sb, arguments);
                continue;
            }
            // if char is a quotation mark, then reverse booleans
            if (QUOTATION_MARKS.contains(c)) {
                quote = !quote;
                // also add if the quoted string is the last argument of the command
                if (i == chars.length) {
                    addAndClear(sb, arguments);
                }
                continue;
            }
            // append character
            sb.append(c);
            // if at last word, there's not expected to be a final space
            if (i >= chars.length) {
                addAndClear(sb, arguments);
            }
        }
        // finally, return the arguments
        return arguments;
    }

    public String join(String[] args) {
        return String.join(" ", args);
    }
}
