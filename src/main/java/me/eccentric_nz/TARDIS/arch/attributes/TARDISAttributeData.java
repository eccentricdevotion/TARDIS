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

import java.io.Serializable;

/**
 * @author eccentric_nz
 */
public class TARDISAttributeData implements Serializable {

    private static final long serialVersionUID = 1990400415318745737L;
    private final String attribute;
    private final String attributeID;
    private final double value;
    private final TARDISAttributeOperation operation;

    public TARDISAttributeData(String attribute, String attributeID, double value, TARDISAttributeOperation operation) {
        this.attribute = attribute;
        this.attributeID = attributeID;
        this.value = value;
        this.operation = operation;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getAttributeID() {
        return attributeID;
    }

    public double getValue() {
        return value;
    }

    public TARDISAttributeOperation getOperation() {
        return operation;
    }
}
