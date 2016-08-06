/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.arch.attributes;

import com.google.common.base.Preconditions;
import java.util.UUID;

/**
 *
 * @author Kristian
 */
public class TARDISAttribute {

    public TARDISNbtFactory.NbtCompound data;

    public TARDISAttribute(TARDISAttributeBuilder builder) {
        data = TARDISNbtFactory.createCompound();
        setAmount(builder.amount);
        setOperation(builder.operation);
        setAttributeType(builder.type);
        setName(builder.name);
        setUUID(builder.uuid);
    }

    public TARDISAttribute(TARDISNbtFactory.NbtCompound data) {
        this.data = data;
    }

    public double getAmount() {
        return data.getDouble("Amount", 0.0);
    }

    public final void setAmount(double amount) {
        data.put("Amount", amount);
    }

    public TARDISAttributeOperation getOperation() {
        return TARDISAttributeOperation.fromId(data.getInteger("Operation", 0));
    }

    public final void setOperation(TARDISAttributeOperation operation) {
        Preconditions.checkNotNull(operation, "operation cannot be NULL.");
        data.put("Operation", operation.getId());
    }

    public TARDISAttributeType getAttributeType() {
        return TARDISAttributeType.fromId(data.getString("AttributeName", null));
    }

    public final void setAttributeType(TARDISAttributeType type) {
        Preconditions.checkNotNull(type, "type cannot be NULL.");
        data.put("AttributeName", type.getMinecraftId());
    }

    public String getName() {
        return data.getString("Name", null);
    }

    public final void setName(String name) {
        Preconditions.checkNotNull(name, "name cannot be NULL.");
        data.put("Name", name);
    }

    public UUID getUUID() {
        return new UUID(data.getLong("UUIDMost", null), data.getLong("UUIDLeast", null));
    }

    public final void setUUID(UUID id) {
        data.put("UUIDLeast", id.getLeastSignificantBits());
        data.put("UUIDMost", id.getMostSignificantBits());
    }

    /**
     * Construct a new attribute builder with a random UUID and default
     * operation of adding numbers.
     *
     * @return The attribute builder.
     */
    public static TARDISAttributeBuilder newBuilder() {
        return new TARDISAttributeBuilder().uuid(UUID.randomUUID()).operation(TARDISAttributeOperation.ADD_NUMBER);
    }

}
