/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.schematic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 *
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

    public static JSONObject unzip(String instr) {
        InputStreamReader isr = null;
        StringWriter sw = null;
        String s = "";
        try {
            GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(instr));
            isr = new InputStreamReader(gzis, "UTF-8");
            sw = new StringWriter();
            char[] buffer = new char[1024 * 16];
            int len;
            while ((len = isr.read(buffer)) > 0) {
                sw.write(buffer, 0, len);
            }
            s = sw.toString();
        } catch (IOException ex) {
            TARDIS.plugin.debug("Could not read GZip schematic file!" + ex.getMessage());
        } finally {
            try {
                if (sw != null) {
                    sw.close();
                }
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException ex) {
                TARDIS.plugin.debug("Could not close GZip schematic file!" + ex.getMessage());
            }
        }
        return new JSONObject(s);
    }
}
