/*
 * Copyright (c) 2018 Shadow1013GL, Pyr0Byt3, pendo324
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package me.eccentric_nz.TARDIS.sonic;

/*
 * Borrowed from the SimpleSort plugin. http://dev.bukkit.org/bukkit-plugins/simplesort/
 *
 * @author Shadow1013GL
 * @author Pyr0Byt3
 * @author pendo324
 */

import org.bukkit.inventory.ItemStack;

import java.util.Comparator;

class TARDISItemComparator implements Comparator<ItemStack> {

    // TODO check this still works
    @Override
    public int compare(ItemStack item1, ItemStack item2) {
        if (item1 == null && item2 != null) {
            return 1;
        } else if (item1 != null && item2 == null) {
            return -1;
        } else if (item1 == null && item2 == null) {
            return 0;
        } else if (item1 != null && item2 != null) {
            if (item1.getType().toString().compareTo(item2.getType().toString()) > 0) {
                return 1;
            } else if (item1.getType().toString().compareTo(item2.getType().toString()) < 0) {
                return -1;
            } else if (item1.getType().toString().compareTo(item2.getType().toString()) == 0) {
                if (item1.getDurability() > item2.getDurability()) {
                    return 1;
                } else if (item1.getDurability() < item2.getDurability()) {
                    return -1;
                } else if (item1.getDurability() == item2.getDurability()) {
                    return 0;
                }
                if (item1.getAmount() > item2.getAmount()) {
                    return 1;
                } else if (item1.getAmount() < item2.getAmount()) {
                    return -1;
                }
            }
        }
        return 0;
    }
}
