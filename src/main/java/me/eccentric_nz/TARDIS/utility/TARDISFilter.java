/*
 * Copyright (C) 2019 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;

import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author eccentric_nz
 */
class TARDISFilter implements Filter {

    @Override
    public boolean isLoggable(LogRecord record) {

        String message = record.getMessage();
        Pattern pattern = Pattern.compile("World border is currently (\\d+) blocks wide");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            TARDIS.plugin.debug("Match found");
            // get the number
            String num = matcher.group(1);
            TARDIS.plugin.debug("The worldborder distance was: " + num);
        }
        return true;
    }
}
