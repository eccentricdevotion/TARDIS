/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.database.data;

import me.eccentric_nz.tardis.advanced.TardisInventorySerializer;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

/**
 * @author eccentric_nz
 */
public class Program {

    private final int programId;
    private final String uuid;
    private final String name;
    private final ItemStack[] inventory;
    private final String parsed;
    private final boolean checkedOut;

    public Program(int programId, String uuid, String name, String inventory, String parsed, boolean checkedOut) {
        this.programId = programId;
        this.uuid = uuid;
        this.name = name;
        this.inventory = createInventory(inventory);
        this.parsed = parsed;
        this.checkedOut = checkedOut;
    }

    private ItemStack[] createInventory(String inventory) {
        ItemStack[] stack;
        try {
            stack = TardisInventorySerializer.itemStacksFromString(inventory);
        } catch (IOException ex) {
            stack = new ItemStack[36];
        }
        return stack;
    }

    public int getProgramId() {
        return programId;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public String getParsed() {
        return parsed;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }
}
