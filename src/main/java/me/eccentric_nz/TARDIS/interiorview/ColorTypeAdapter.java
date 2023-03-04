package me.eccentric_nz.TARDIS.interiorview;

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
        String rgba = color.getRed()+","+color.getGreen()+","+color.getBlue()+","+color.getAlpha();
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
        float r = Float.parseFloat(parts[0]);
        float g = Float.parseFloat(parts[1]);
        float b = Float.parseFloat(parts[2]);
        float a = Float.parseFloat(parts[3]);
        return new Color(r, g, b, a);
    }
}
