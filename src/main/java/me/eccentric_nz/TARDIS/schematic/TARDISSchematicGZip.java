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
package me.eccentric_nz.TARDIS.schematic;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 * @author eccentric_nz
 */
public class TARDISSchematicGZip {

    public static void zip(String instr, String outstr) {
        try {
            FileInputStream fis = new FileInputStream(instr);
            FileOutputStream fos = new FileOutputStream(outstr);
            GZIPOutputStream gzos = new GZIPOutputStream(fos);
            byte[] buffer = new byte[1024 * 16];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                gzos.write(buffer, 0, len);
            }
            gzos.close();
            fos.close();
            fis.close();
        } catch (IOException e) {
            TARDIS.plugin.debug("Could not GZip schematic file!" + e.getMessage());
        }
    }

    public static JsonObject unzip(String instr) {
        InputStreamReader isr = null;
        StringWriter sw = null;
        String s = "";
        try {
            GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(instr));
            isr = new InputStreamReader(gzis, StandardCharsets.UTF_8);
            sw = new StringWriter();
            char[] buffer = new char[1024 * 16];
            int len;
            while ((len = isr.read(buffer)) > 0) {
                sw.write(buffer, 0, len);
            }
            s = sw.toString();
        } catch (IOException ex) {
            TARDIS.plugin.debug("Could not read GZip schematic file! " + ex.getMessage());
        } finally {
            try {
                if (sw != null) {
                    sw.close();
                }
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException ex) {
                TARDIS.plugin.debug("Could not close GZip schematic file! " + ex.getMessage());
            }
        }
        return (s.startsWith("{")) ? JsonParser.parseString(s).getAsJsonObject() : null;
    }

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
            TARDIS.plugin.debug("Could not read GZip schematic file! " + ex.getMessage());
        } finally {
            try {
                if (sw != null) {
                    sw.close();
                }
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException ex) {
                TARDIS.plugin.debug("Could not close GZip schematic file! " + ex.getMessage());
            }
        }
        return (s.startsWith("{")) ? new JsonParser().parse(s).getAsJsonObject() : null;
    }

    public static JsonObject getObject(TARDIS plugin, String folder, String which, boolean user) {
        JsonObject obj = null;
        String path;
        if (user) {
            // get the schematic from the plugin folder
            path = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + which + ".tschm";
            File file = new File(path);
            if (!file.exists()) {
                plugin.debug("Could not find a schematic with that name!");
                return null;
            }
            obj = TARDISSchematicGZip.unzip(path);
        } else {
            // just get the schematic from the plugin JAR
            path = folder + "/" + which + ".tschm";
            InputStream stream = plugin.getResource(path);
            if (stream != null) {
                obj = TARDISSchematicGZip.unzip(stream);
            }
        }
        return obj;
    }
}
