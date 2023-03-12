/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardischunkgenerator.worldgen.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;

/**
 * @author eccentric_nz
 */
public class TARDISSchematicReader {

    public static JsonObject unzip(InputStream stream) {
        InputStreamReader isr = null;
        StringWriter sw = null;
        String s = "";
        try {
            GZIPInputStream gzis = new GZIPInputStream(stream);
            isr = new InputStreamReader(gzis, StandardCharsets.UTF_8);
            sw = new StringWriter();
            char[] buffer = new char[1024 * 16];
            int len;
            while ((len = isr.read(buffer)) > 0) {
                sw.write(buffer, 0, len);
            }
            s = sw.toString();
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.WARNING, "Could not read GZip schematic file! " + ex.getMessage());
        } finally {
            try {
                if (sw != null) {
                    sw.close();
                }
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException ex) {
                Bukkit.getLogger().log(Level.WARNING, "Could not close GZip schematic file! " + ex.getMessage());
            }
        }
        return (s.startsWith("{")) ? new JsonParser().parse(s).getAsJsonObject() : null;
    }
}
