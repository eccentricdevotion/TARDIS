package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.equip.MonsterEquipment;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffectType;

public class Cleaner implements Runnable {

    private final TARDIS plugin;

    public Cleaner(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (World world : plugin.getServer().getWorlds()) {
            // only in loaded chunks
            for (Chunk chunk : world.getLoadedChunks()) {
                for (Entity e : chunk.getEntities()) {
                    // only monsters
                    if (e instanceof Monster monster) {
                        // only invisible TWA monsters
                        if (monster.hasPotionEffect(PotionEffectType.INVISIBILITY) && MonsterEquipment.isMonster(monster)) {
                            // check helmet
                            EntityEquipment ee = monster.getEquipment();
                            if (ee != null && ee.getHelmet() == null) {
                                monster.remove();
                            }
                        }
                    }
                }
            }
        }
    }
}
