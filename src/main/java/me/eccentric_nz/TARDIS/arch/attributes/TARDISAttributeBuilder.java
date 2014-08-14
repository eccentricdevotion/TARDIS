/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.arch.attributes;

import java.util.UUID;

/**
 *
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
