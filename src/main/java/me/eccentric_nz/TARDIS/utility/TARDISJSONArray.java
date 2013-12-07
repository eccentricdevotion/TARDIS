/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.ArrayList;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONException;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.JSON.JSONTokener;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJSONArray extends JSONArray {

    private final ArrayList myArrayList;

    public TARDISJSONArray() {
        this.myArrayList = new ArrayList();
    }

    public TARDISJSONArray(JSONTokener x) throws JSONException {
        this();
        if (x.nextClean() != '[') {
            throw x.syntaxError("A JSONArray text must start with '['");
        }
        if (x.nextClean() != ']') {
            x.back();
            for (;;) {
                if (x.nextClean() == ',') {
                    x.back();
                    this.myArrayList.add(JSONObject.NULL);
                } else {
                    x.back();
                    this.myArrayList.add(x.nextValue());
                }
                switch (x.nextClean()) {
                    case ',':
                        if (x.nextClean() == ']') {
                            return;
                        }
                        x.back();
                        break;
                    case ']':
                        return;
                    default:
                        throw x.syntaxError("Expected a ',' or ']'");
                }
            }
        }
    }

    public TARDISJSONArray(String source) throws JSONException {
        this(new JSONTokener(source));
    }

    /**
     * Get the byte value associated with an index.
     *
     * @param index The index must be between 0 and length() - 1.
     * @return The value.
     * @throws JSONException If the key is not found or if the value cannot be
     * converted to a number.
     */
    public byte getByte(int index) throws JSONException {
        Object object = this.get(index);
        try {
            return object instanceof Number ? ((Number) object).byteValue()
                    : Byte.parseByte((String) object);
        } catch (NumberFormatException e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.");
        }
    }

    @Override
    public TARDISJSONArray getJSONArray(int index) throws JSONException {
        return (TARDISJSONArray) super.getJSONArray(index);
    }
}
