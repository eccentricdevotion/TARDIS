package me.eccentric_nz.TARDIS.commands.dev;

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.mobfarming.HappyGhastUtils;
import me.eccentric_nz.TARDIS.mobfarming.TARDISHappyGhast;
import me.eccentric_nz.TARDIS.rooms.happy.HappyLocations;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class HappyCommand {

    public void leash(Player player) {
        TARDISHappyGhast happy = new TARDISHappyGhast();
        happy.setHealth(20.0d);
        happy.setHarness(ItemStack.of(Material.CYAN_HARNESS, 1));
        happy.setName("Cuthbert");
        happy.setAge(7);
        happy.setBaby(false);
        Location location = player.getLocation().getBlock().getLocation();
        for (Pair<Vector, BlockFace> p : HappyLocations.VECTORS) {
            Location possible = location.clone().add(p.getFirst());
            HappyGhastUtils.setLeashed(possible, happy, p.getSecond());
        }
    }
}