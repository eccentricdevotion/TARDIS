/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.tardisshop.listener;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisshop.database.ResultSetShopItem;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class TARDISShopItemExplode implements Listener {

    private final TARDIS plugin;

    public TARDISShopItemExplode(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onShopItemBreak(EntityExplodeEvent event) {
        if (event.getEntityType().equals(EntityType.ENDER_DRAGON)) {
            return;
        }
        List<Block> blockList = new ArrayList<>(event.blockList());
        blockList.forEach((block) -> {
            if (block != null && block.getType() == plugin.getShopSettings().getBlockMaterial()) {
                String location = block.getLocation().toString();
                ResultSetShopItem rs = new ResultSetShopItem(plugin);
                if (rs.itemFromBlock(location)) {
                    event.blockList().remove(block);
                }
            }
        });
    }
}
