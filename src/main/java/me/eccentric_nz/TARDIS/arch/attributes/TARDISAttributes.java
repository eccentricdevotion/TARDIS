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

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import me.eccentric_nz.TARDIS.arch.attributes.TARDISNbtFactory.NbtCompound;
import me.eccentric_nz.TARDIS.arch.attributes.TARDISNbtFactory.NbtList;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.UUID;

/**
 * @author Kristian
 */
public class TARDISAttributes {

    // This may be modified
    public ItemStack stack;
    private final NbtList attributes;

    public TARDISAttributes(ItemStack stack) {
        // Create a CraftItemStack (under the hood)
        this.stack = TARDISNbtFactory.getCraftItemStack(stack);

        // Load NBT
        NbtCompound nbt = TARDISNbtFactory.fromItemTag(this.stack);
        attributes = nbt.getList("AttributeModifiers", true);
    }

    /**
     * Retrieve the modified item stack.
     *
     * @return The modified item stack.
     */
    public ItemStack getStack() {
        return stack;
    }

    /**
     * Retrieve the number of attributes.
     *
     * @return Number of attributes.
     */
    public int size() {
        return attributes.size();
    }

    /**
     * Add a new attribute to the list.
     *
     * @param attribute - the new attribute.
     */
    public void add(TARDISAttribute attribute) {
        Preconditions.checkNotNull(attribute.getName(), "must specify an attribute name.");
        attributes.add(attribute.data);
    }

    /**
     * Remove the first instance of the given attribute.
     * <p>
     * The attribute will be removed using its UUID.
     *
     * @param attribute - the attribute to remove.
     * @return TRUE if the attribute was removed, FALSE otherwise.
     */
    public boolean remove(TARDISAttribute attribute) {
        UUID uuid = attribute.getUUID();

        for (Iterator<TARDISAttribute> it = values().iterator(); it.hasNext(); ) {
            if (Objects.equal(it.next().getUUID(), uuid)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public void clear() {
        attributes.clear();
    }

    /**
     * Retrieve the attribute at a given index.
     *
     * @param index - the index to look up.
     * @return The attribute at that index.
     */
    public TARDISAttribute get(int index) {
        return new TARDISAttribute((NbtCompound) attributes.get(index));
    }

    // We can't make TARDISAttributes itself iterable without splitting it up into separate classes
    public Iterable<TARDISAttribute> values() {
        return () -> Iterators.transform(attributes.iterator(), (Object element) -> new TARDISAttribute((NbtCompound) element));
    }
}
