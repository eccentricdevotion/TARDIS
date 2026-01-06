package me.eccentric_nz.TARDIS.chameleon.utils;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Material;

import java.util.EnumMap;
import java.util.List;

public record CustomPreset(EnumMap<COMPASS, ChameleonColumn> blueprint,
                           EnumMap<COMPASS, ChameleonColumn> stained,
                           EnumMap<COMPASS, ChameleonColumn> glass,
                           List<String> lines,
                           Material icon) {
}
