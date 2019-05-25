package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.artron.TARDISArtronLevels;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class TARDISCustardCreamDispenser {

    private final TARDIS plugin;
    private final Player player;
    private final Block block;
    private final int id;

    public TARDISCustardCreamDispenser(TARDIS plugin, Player player, Block block, int id) {
        this.plugin = plugin;
        this.player = player;
        this.block = block;
        this.id = id;
    }

    public void dispense() {
        // block must be a dispenser
        if (!block.getType().equals(Material.DISPENSER)) {
            TARDISMessage.send(player, "DISPENSER_TYPE");
            return;
        }
        // check artron energy level
        TARDISArtronLevels tardisArtronLevels = new TARDISArtronLevels(plugin);
        if (tardisArtronLevels.checkLevel(id, plugin.getArtronConfig().getInt("custard_cream"), player)) {
            ItemStack cookie = new ItemStack(Material.COOKIE, 1);
            ItemMeta im = cookie.getItemMeta();
            im.setDisplayName("Custard Cream");
            cookie.setItemMeta(im);
            // put cookie on top of the block
            Location location = block.getLocation().add(0.5, 1.0, 0.5);
            Item cream = location.getWorld().dropItem(location, cookie);
            cream.setVelocity(new Vector(0, 0, 0));
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> cream.teleport(location), 1L);
            // take their artron energy
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            new QueryFactory(plugin).alterEnergyLevel("tardis", plugin.getArtronConfig().getInt("custard_cream") * -1, where, player);
        }
    }
}
