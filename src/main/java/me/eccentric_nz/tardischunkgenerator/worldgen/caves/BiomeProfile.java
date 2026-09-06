package me.eccentric_nz.tardischunkgenerator.worldgen.caves;

import me.eccentric_nz.tardischunkgenerator.worldgen.feature.TARDISTree;
import org.bukkit.Material;

public record BiomeProfile(Material wall,
                           Material floor,
                           Material ceiling,
                           Material accent,
                           Material featureOne,
                           Material featureTwo,
                           Material light,
                           TARDISTree tree) {
}
