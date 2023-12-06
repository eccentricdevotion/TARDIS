package me.eccentric_nz.tardischunkgenerator.helpers;

import com.mojang.authlib.GameProfile;

import java.lang.reflect.Field;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.map.CraftMapView;
import org.bukkit.map.MapView;

public final class TARDISMapUpdater extends Player {

    public static final UUID ID = UUID.randomUUID();
    public static final String NAME = "_____MapUpdater_____";

    public TARDISMapUpdater(World world, int x, int z) {
        super(((CraftWorld) world).getHandle(), new BlockPos(x, 64, z), 1.0f, new GameProfile(ID, NAME));
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }

    public void update(MapView mapView) {
        if (((CraftWorld) mapView.getWorld()).getHandle() != level()) {
            throw new IllegalArgumentException("world of mapView cannot be different");
        }
        try {
            Field field = CraftMapView.class.getDeclaredField("worldMap");
            field.setAccessible(true);
            MapItemSavedData worldMap = (MapItemSavedData) field.get(mapView);
            int size = 128 << worldMap.scale;
            for (int x = worldMap.mapView.getCenterX() - size / 2; x <= worldMap.mapView.getCenterX() + size / 2; x += 64) {
                for (int z = worldMap.mapView.getCenterZ() - size / 2; z <= worldMap.mapView.getCenterZ() + size / 2; z += 64) {
                    setPosRaw(x, 0.0, z);
                    ((MapItem) Items.FILLED_MAP).update(level(), this, worldMap);
                }
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
