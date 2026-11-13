package me.eccentric_nz.TARDIS.desktop;

import org.bukkit.Material;
import org.bukkit.World;

public record CheckData(int x, int z, World world, Material wall, Material floor) {
}
