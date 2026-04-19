package me.eccentric_nz.TARDIS.blueprints.trader;

import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.custommodels.keys.Features;
import me.eccentric_nz.TARDIS.enumeration.Room;
import me.eccentric_nz.TARDIS.skins.CharacterSkins;
import me.eccentric_nz.TARDIS.skins.Skin;
import net.kyori.adventure.text.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Mannequin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class TimeLordTraderSpawner {
    private static final int DEFAULT_TICK_DELAY = 1200;
    public static final int DEFAULT_SPAWN_DELAY = 24000;
    public static final int MIN_SPAWN_CHANCE = 25;
    private static final int MAX_SPAWN_CHANCE = 75;
    private static final int SPAWN_CHANCE_INCREASE = 25;
    private static final int SPAWN_ONE_IN_X_CHANCE = 10;
    private static final int NUMBER_OF_SPAWN_ATTEMPTS = 10;
    private final TARDIS plugin;

    public TimeLordTraderSpawner(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean spawn(World world) {
        Player player = getRandomPlayer(world);
        if (player == null) {
            return true;
        } else if (TARDISConstants.RANDOM.nextInt(10) != 0) {
            return false;
        } else {
            Location loc = player.getLocation();
            BlockPos playerPos = new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            ServerLevel level = ((CraftWorld) world).getHandle();
            PoiManager poiManager = level.getPoiManager();
            Optional<BlockPos> poiPos = poiManager.find(p -> p.is(PoiTypes.MEETING), p -> true, playerPos, 48, PoiManager.Occupancy.ANY);
            BlockPos referencePos = poiPos.orElse(playerPos);
            BlockPos spawnPosition = this.findSpawnPositionNear(level, referencePos);
            if (spawnPosition != null && this.hasEnoughSpace(level, spawnPosition)) {
                if (level.getBiome(spawnPosition).is(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS)) {
                    return false;
                }
                Location biomeLocation = new Location(world, spawnPosition.getX(), spawnPosition.getY(), spawnPosition.getZ());
                Mannequin mannequin = world.spawn(biomeLocation, Mannequin.class);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    Skin skin = CharacterSkins.RASSILON;
                    mannequin.setProfile(ResolvableProfile.resolvableProfile().name("").uuid(UUID.randomUUID()).addProperty(new ProfileProperty("textures", skin.value(), skin.signature())).build());
                    mannequin.setSilent(true);
                    mannequin.setAI(false);
                    mannequin.setImmovable(true);
                    mannequin.setMainHand(MainHand.RIGHT);
                    mannequin.getEquipment().setItemInMainHand(ItemStack.of(Material.TOTEM_OF_UNDYING));
                    Material material = Material.COD;
                    NamespacedKey key = Features.VAMPIRE_OF_VENICE_FAN.getKey();
                    ItemStack head = ItemStack.of(material, 1);
                    ItemMeta im = head.getItemMeta();
                    im.displayName(Component.text(skin.name()));
                    im.setItemModel(key);
                    im.getPersistentDataContainer().set(TARDIS.plugin.getTimeLordUuidKey(), PersistentDataType.BOOLEAN, true);
                    head.setItemMeta(im);
                    mannequin.getEquipment().setHelmet(head);
                    // set trades in PDC
                    String trades = getTrades(biomeLocation);
                    mannequin.getPersistentDataContainer().set(plugin.getTimeLordUuidKey(), PersistentDataType.STRING, trades);
                }, 5L);
                return true;
            }
            return false;
        }
    }

    private BlockPos findSpawnPositionNear(final LevelReader level, final BlockPos referencePosition) {
        BlockPos spawnPosition = null;
        SpawnPlacementType wanderingTraderSpawnType = SpawnPlacements.getPlacementType(EntityType.WANDERING_TRADER);
        for (int i = 0; i < 10; i++) {
            int xPosition = referencePosition.getX() + TARDISConstants.RANDOM.nextInt(48 * 2) - 48;
            int zPosition = referencePosition.getZ() + TARDISConstants.RANDOM.nextInt(48 * 2) - 48;
            int yPosition = level.getHeight(SpawnPlacements.getHeightmapType(EntityType.WANDERING_TRADER), xPosition, zPosition);
            BlockPos spawnPos = new BlockPos(xPosition, yPosition, zPosition);
            if (wanderingTraderSpawnType.isSpawnPositionOk(level, spawnPos, EntityType.WANDERING_TRADER)) {
                spawnPosition = spawnPos;
                break;
            }
        }
        return spawnPosition;
    }

    private boolean hasEnoughSpace(final BlockGetter level, final BlockPos spawnPos) {
        for (BlockPos pos : BlockPos.betweenClosed(spawnPos, spawnPos.offset(1, 2, 1))) {
            if (!level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private Player getRandomPlayer(World world) {
        List<Player> players = world.getPlayers();
        return players.isEmpty() ? null : players.get(TARDISConstants.RANDOM.nextInt(players.size()));
    }

    public String getTrades(Location location) {
        Biome biome = location.getBlock().getBiome();
        List<Room> rooms = BiomeTrades.CHOICES.get(biome);
        Set<String> trades = new HashSet<>();
        if (rooms != null) {
            for (Room room : rooms) {
                trades.add(room.toString());
            }
        }
        while (trades.size() < 5) {
            Room r = Room.values()[TARDISConstants.RANDOM.nextInt(Room.values().length)];
            trades.add(r.toString());
        }
        return String.join(",", trades);
    }
}

