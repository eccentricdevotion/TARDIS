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
package me.eccentric_nz.TARDIS.monitor;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.awt.*;
import java.io.IOException;

public class ColorTypeAdapter extends TypeAdapter<Color> {

    @Override
    public void write(JsonWriter jsonWriter, Color color) throws IOException {
        if (color == null) {
            jsonWriter.nullValue();
            return;
        }
        String rgba = color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "," + color.getAlpha();
        jsonWriter.value(rgba);
    }

    @Override
    public Color read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        String rgba = jsonReader.nextString();
        String[] parts = rgba.split(",");
        int r = ensureRange(Integer.parseInt(parts[0]));
        int g = ensureRange(Integer.parseInt(parts[1]));
        int b = ensureRange(Integer.parseInt(parts[2]));
        int a = ensureRange(Integer.parseInt(parts[3]));
        return new Color(r, g, b, a);
    }

    int ensureRange(int value) {
        return Math.min(Math.max(value, 0), 255);
    }
}
