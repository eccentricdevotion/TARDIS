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

import java.util.UUID;

/**
 * @author Kristian
 */
// Makes it easier to construct an attribute
public class TARDISAttributeBuilder {

    public double amount;
    public TARDISAttributeOperation operation = TARDISAttributeOperation.ADD_NUMBER;
    public TARDISAttributeType type;
    public String name;
    public UUID uuid;

    public TARDISAttributeBuilder() {
        // Don't make this accessible
    }

    public TARDISAttributeBuilder amount(double amount) {
        this.amount = amount;
        return this;
    }

    public TARDISAttributeBuilder operation(TARDISAttributeOperation operation) {
        this.operation = operation;
        return this;
    }

    public TARDISAttributeBuilder type(TARDISAttributeType type) {
        this.type = type;
        return this;
    }

    public TARDISAttributeBuilder name(String name) {
        this.name = name;
        return this;
    }

    public TARDISAttributeBuilder uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public TARDISAttribute build() {
        return new TARDISAttribute(this);
    }
}
