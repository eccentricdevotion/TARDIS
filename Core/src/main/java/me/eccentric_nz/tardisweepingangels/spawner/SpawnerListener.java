package me.eccentric_nz.tardisweepingangels.spawner;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class SpawnerListener implements Listener {

    private final TARDIS plugin;

    public SpawnerListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpawn(SpawnerSpawnEvent event) {
        CreatureSpawner spawner = event.getSpawner();
        if (spawner.getPersistentDataContainer().has(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.STRING)) {
            // set the armour for this entity
            String m = spawner.getPersistentDataContainer().get(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.STRING);
            Monster monster = Monster.valueOf(m);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                new Equipper(monster, (LivingEntity) event.getEntity(), false).setHelmetAndInvisibility();
                plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(event.getEntity(), EntityType.SKELETON, Monster.SILURIAN, spawner.getLocation()));
            }, 5L);
        }
    }

    @EventHandler
    public void onSpawnerClick(PlayerInteractEvent event) {
        if (!event.getPlayer().isOp()) {
            plugin.getMessenger().send(event.getPlayer(), TardisModule.MONSTERS, "NO_PERMS");
            return;
        }
        if (event.getHand() != EquipmentSlot.HAND || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.SPAWNER) {
            return;
        }
        ItemStack is = event.getItem();
        if (is == null || !is.hasItemMeta()) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if (im == null || !im.getPersistentDataContainer().has(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.INTEGER)) {
            return;
        }
        NamespacedKey key = im.getItemModel();
        if (key == null) {
            return;
        }
        for (Monster monster : Monster.values()) {
            if (key.equals(monster.getHeadModel())) {
                setCustomSpawner(block, monster);
                break;
            }
        }
    }

    private void setCustomSpawner(Block block, Monster monster) {
        BlockState state = block.getState();
        if (state instanceof CreatureSpawner spawner) {
            spawner.getPersistentDataContainer().set(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.STRING, monster.name());
            spawner.setSpawnedType(monster.getEntityType());
            spawner.update();
        }
    }
}
