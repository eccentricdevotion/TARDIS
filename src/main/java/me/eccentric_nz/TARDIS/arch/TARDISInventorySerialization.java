/*
 * Copyright (C) 2013 drtshock
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
package me.eccentric_nz.TARDIS.arch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONException;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;

/**
 * Fancy JSON serialization mostly by evilmidget38.
 *
 * @author eccentric_nz
 *
 */
public class TARDISInventorySerialization {

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<String> keys = object.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            map.put(key, fromJson(object.get(key)));
        }
        return map;
    }

    private static Object fromJson(Object json) throws JSONException {
        if (json == JSONObject.NULL) {
            return null;
        } else if (json instanceof JSONObject) {
            return toMap((JSONObject) json);
        } else if (json instanceof JSONArray) {
            return toList((JSONArray) json);
        } else {
            return json;
        }
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            list.add(fromJson(array.get(i)));
        }
        return list;
    }

    public static String toString(ItemStack[] inv) {
        List<String> result = new ArrayList<String>();
        List<ConfigurationSerializable> items = new ArrayList<ConfigurationSerializable>();
        items.addAll(Arrays.asList(inv));
        for (ConfigurationSerializable cs : items) {
            if (cs == null) {
                result.add("null");
            } else {
                result.add(new JSONObject(serialize(cs)).toString());
            }
        }
        JSONArray json_array = new JSONArray(result);
        return json_array.toString();
    }

    public static ItemStack[] toItemStacks(String s) {
        JSONArray json = new JSONArray(s);
        List<ItemStack> contents = new ArrayList<ItemStack>();
        for (int i = 0; i < json.length(); i++) {
            String piece = json.getString(i);
            if (piece.equalsIgnoreCase("null")) {
                contents.add(null);
            } else {
                try {
                    ItemStack item = (ItemStack) deserialize(toMap(new JSONObject(piece)));
                    contents.add(item);
                } catch (JSONException e) {
                    System.err.println("There was a JSON error: " + e.getMessage());
                }
            }
        }
        ItemStack[] items = new ItemStack[contents.size()];
        for (int x = 0; x < contents.size(); x++) {
            items[x] = contents.get(x);
        }
        return items;
    }

    public static Map<String, Object> serialize(ConfigurationSerializable cs) {
        Map<String, Object> serialized = recreateMap(cs.serialize());
        for (Entry<String, Object> entry : serialized.entrySet()) {
            if (entry.getValue() instanceof ConfigurationSerializable) {
                entry.setValue(serialize((ConfigurationSerializable) entry.getValue()));
            }
        }
        serialized.put(ConfigurationSerialization.SERIALIZED_TYPE_KEY, ConfigurationSerialization.getAlias(cs.getClass()));
        return serialized;
    }

    public static Map<String, Object> recreateMap(Map<String, Object> original) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (Entry<String, Object> entry : original.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static ConfigurationSerializable deserialize(Map<String, Object> map) {
        for (Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map && ((Map) entry.getValue()).containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
                entry.setValue(deserialize((Map) entry.getValue()));
            }
        }
        return ConfigurationSerialization.deserializeObject(map);
    }
}
