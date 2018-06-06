/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.arch.attributes;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISAttributeSerialization {

    public static String toDatabase(HashMap<Integer, List<TARDISAttributeData>> map) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // Save every element in the map
            try (BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {
                // Save every element in the map
                dataOutput.writeObject(map);
                // Serialize that array
            }
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save attributes.", e);
        }
    }

    public static HashMap<Integer, List<TARDISAttributeData>> fromDatabase(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            HashMap<Integer, List<TARDISAttributeData>> map;
            // Read the serialized map
            try (BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
                // Read the serialized map
                map = (HashMap<Integer, List<TARDISAttributeData>>) dataInput.readObject();
            }
            return map;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode attributes.", e);
        }
    }
}
