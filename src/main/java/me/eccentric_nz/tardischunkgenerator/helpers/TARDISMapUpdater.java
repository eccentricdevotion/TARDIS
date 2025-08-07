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
package me.eccentric_nz.tardischunkgenerator.helpers;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.map.CraftMapView;
import org.bukkit.map.MapView;

import java.lang.reflect.Field;
import java.util.UUID;

public final class TARDISMapUpdater extends Player {

    public static final UUID ID = UUID.randomUUID();
    public static final String NAME = "_____MapUpdater_____";

    public TARDISMapUpdater(World world) {
        super(((CraftWorld) world).getHandle(), new GameProfile(ID, NAME));
    }

    @Override
    public GameType gameMode() {
        return GameType.DEFAULT_MODE;
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
