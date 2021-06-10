/*
 * Copyright (C) 2018 michidk && xxmicloxx
 * http://dev.bukkit.org/bukkit-plugins/noteblockapi/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.noteblock;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

class NBSDecoder {

    static Song parse(InputStream inputStream) {
        HashMap<Integer, Layer> layerHashMap = new HashMap<>();
        try {
            DataInputStream dis = new DataInputStream(inputStream);
            short length = readShort(dis);
            short songHeight = readShort(dis);
            readString(dis);
            float speed = readShort(dis) / 100f;
            dis.readBoolean(); // auto-save
            dis.readByte(); // auto-save duration
            dis.readByte(); // x/4ths, time signature
            readInt(dis); // minutes spent on project
            readInt(dis); // left clicks (why?)
            readInt(dis); // right clicks (why?)
            readInt(dis); // blocks added
            readInt(dis); // blocks removed
            readString(dis); // .mid/.schematic file name
            short tick = -1;
            while (true) {
                short jumpTicks = readShort(dis); // jumps till next tick
                if (jumpTicks == 0) {
                    break;
                }
                tick += jumpTicks;
                short layer = -1;
                while (true) {
                    short jumpLayers = readShort(dis); // jumps till next layer
                    if (jumpLayers == 0) {
                        break;
                    }
                    layer += jumpLayers;
                    setNote(layer, tick, dis.readByte() /* instrument */, dis.readByte() /* note */, layerHashMap);
                }
            }
            for (int i = 0; i < songHeight; i++) {
                Layer l = layerHashMap.get(i);
                if (l != null) {
                    l.setName(readString(dis));
                    l.setVolume(dis.readByte());
                }
            }
            return new Song(speed, layerHashMap, length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void setNote(int layer, int ticks, byte instrument, byte key, HashMap<Integer, Layer> layerHashMap) {
        Layer l = layerHashMap.get(layer);
        if (l == null) {
            l = new Layer();
            layerHashMap.put(layer, l);
        }
        l.setNote(ticks, new Note(instrument, key));
    }

    private static short readShort(DataInputStream dis) throws IOException {
        int byte1 = dis.readUnsignedByte();
        int byte2 = dis.readUnsignedByte();
        return (short) (byte1 + (byte2 << 8));
    }

    private static int readInt(DataInputStream dis) throws IOException {
        int byte1 = dis.readUnsignedByte();
        int byte2 = dis.readUnsignedByte();
        int byte3 = dis.readUnsignedByte();
        int byte4 = dis.readUnsignedByte();
        return (byte1 + (byte2 << 8) + (byte3 << 16) + (byte4 << 24));
    }

    private static String readString(DataInputStream dis) throws IOException {
        int length = readInt(dis);
        StringBuilder sb = new StringBuilder(length);
        for (; length > 0; --length) {
            char c = (char) dis.readByte();
            if (c == (char) 0x0D) {
                c = ' ';
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
