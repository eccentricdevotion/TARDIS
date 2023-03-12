package me.eccentric_nz.TARDIS.monitor;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.awt.Color;
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
