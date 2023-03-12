package me.eccentric_nz.tardisshop.listener;

import me.eccentric_nz.tardisshop.TARDISShop;
import me.eccentric_nz.tardisshop.database.ResultSetShopItem;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class TARDISShopItemBreak implements Listener {

    private final TARDISShop plugin;

    public TARDISShopItemBreak(TARDISShop plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onShopItemBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block != null && block.getType() == plugin.getBlockMaterial()) {
            String location = block.getLocation().toString();
            ResultSetShopItem rs = new ResultSetShopItem(plugin);
            event.setCancelled(rs.itemFromBlock(location));
        }
    }
}
