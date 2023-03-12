package me.eccentric_nz.tardisshop.listener;

import me.eccentric_nz.tardisshop.TARDISShop;
import me.eccentric_nz.tardisshop.database.ResultSetShopItem;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class TARDISShopItemExplode implements Listener {

    private final TARDISShop plugin;

    public TARDISShopItemExplode(TARDISShop plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onShopItemBreak(EntityExplodeEvent event) {
        if (event.getEntityType().equals(EntityType.ENDER_DRAGON)) {
            return;
        }
        List<Block> blockList = new ArrayList<>(event.blockList());
        blockList.forEach((block) -> {
            if (block != null && block.getType() == plugin.getBlockMaterial()) {
                String location = block.getLocation().toString();
                ResultSetShopItem rs = new ResultSetShopItem(plugin);
                if (rs.itemFromBlock(location)) {
                    event.blockList().remove(block);
                }
            }
        });
    }
}
