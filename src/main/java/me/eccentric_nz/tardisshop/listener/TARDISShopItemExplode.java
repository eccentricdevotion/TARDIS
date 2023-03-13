package me.eccentric_nz.tardisshop.listener;

import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisshop.database.ResultSetShopItem;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

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
