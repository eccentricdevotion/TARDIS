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

import com.google.common.collect.Maps;

import java.util.concurrent.ConcurrentMap;

/**
 * @author Kristian
 */
public class TARDISAttributeType {

    private static final ConcurrentMap<String, TARDISAttributeType> LOOKUP = Maps.newConcurrentMap();
    public static final TARDISAttributeType GENERIC_ARMOR = new TARDISAttributeType("generic.armor").register();
    public static final TARDISAttributeType GENERIC_ARMOR_TOUGHNESS = new TARDISAttributeType("generic.armorToughness").register();
    public static final TARDISAttributeType GENERIC_ATTACK_DAMAGE = new TARDISAttributeType("generic.attackDamage").register();
    public static final TARDISAttributeType GENERIC_ATTACK_SPEED = new TARDISAttributeType("generic.attackSpeed").register();
    public static final TARDISAttributeType GENERIC_FOLLOW_RANGE = new TARDISAttributeType("generic.followRange").register();
    public static final TARDISAttributeType GENERIC_KNOCKBACK_RESISTANCE = new TARDISAttributeType("generic.knockbackResistance").register();
    public static final TARDISAttributeType GENERIC_LUCK = new TARDISAttributeType("generic.luck").register();
    public static final TARDISAttributeType GENERIC_MAX_HEALTH = new TARDISAttributeType("generic.maxHealth").register();
    public static final TARDISAttributeType GENERIC_MOVEMENT_SPEED = new TARDISAttributeType("generic.movementSpeed").register();

    private final String minecraftId;

    /**
     * Construct a new attribute type.
     * <p>
     * Remember to {@link #register()} the type.
     *
     * @param minecraftId - the ID of the type.
     */
    public TARDISAttributeType(String minecraftId) {
        this.minecraftId = minecraftId;
    }

    /**
     * Retrieve the associated minecraft ID.
     *
     * @return The associated ID.
     */
    public String getMinecraftId() {
        return minecraftId;
    }

    /**
     * Register the type in the central registry.
     *
     * @return The registered type.
     */
    // Constructors should have no side-effects!
    public TARDISAttributeType register() {
        TARDISAttributeType old = LOOKUP.putIfAbsent(minecraftId, this);
        return old != null ? old : this;
    }

    /**
     * Retrieve the attribute type associated with a given ID.
     *
     * @param minecraftId The ID to search for.
     * @return The attribute type, or NULL if not found.
     */
    public static TARDISAttributeType fromId(String minecraftId) {
        return LOOKUP.get(minecraftId);
    }

    /**
     * Retrieve every registered attribute type.
     *
     * @return Every type.
     */
    public static Iterable<TARDISAttributeType> values() {
        return LOOKUP.values();
    }
}
