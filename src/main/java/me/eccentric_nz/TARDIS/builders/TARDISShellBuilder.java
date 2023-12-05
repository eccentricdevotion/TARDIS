/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetShells;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A police box is a telephone kiosk that can be used by members of the public wishing to get help from the police.
 * Early in the First Doctor's travels, the TARDIS assumed the exterior shape of a police box during a five-month
 * stopover in 1963 London. Due a malfunction in its chameleon circuit, the TARDIS became locked into that shape.
 *
 * @author eccentric_nz
 */
public class TARDISShellBuilder {

    private final TARDIS plugin;
    private final TARDISChameleonColumn column;
    private final Location centre;
    private final ChameleonPreset preset;

    private final int cid;
    private final Material random_colour;
    private final List<TARDISInstantPreset.ProblemBlock> do_at_end = new ArrayList<>();

    public TARDISShellBuilder(TARDIS plugin, ChameleonPreset preset, TARDISChameleonColumn column, Location centre, int cid) {
        this.plugin = plugin;
        this.preset = preset;
        this.column = column;
        this.centre = centre;
        this.cid = cid;
        Material[] colours = new Material[]{Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL, Material.LIGHT_BLUE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL, Material.PINK_WOOL, Material.CYAN_WOOL, Material.PURPLE_WOOL, Material.BLUE_WOOL, Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL};
        random_colour = colours[TARDISConstants.RANDOM.nextInt(13)];
    }

    /**
     * Builds the TARDIS Shell.
     */
    public void buildPreset() {
        int plusx, minusx, x, plusz, y, minusz, z;
        // get relative locations
        x = centre.getBlockX();
        plusx = centre.getBlockX() + 1;
        minusx = centre.getBlockX() - 1;
        y = centre.getBlockY();
        z = centre.getBlockZ();
        plusz = centre.getBlockZ() + 1;
        minusz = centre.getBlockZ() - 1;
        World world = centre.getWorld();
        int signx = (minusx - 1);
        int signz = z;
        int xx, zz;
        BlockData[][] data = column.getBlockData();
        for (int i = 0; i < 10; i++) {
            BlockData[] colData = data[i];
            switch (i) {
                case 0 -> {
                    xx = minusx;
                    zz = minusz;
                }
                case 1 -> {
                    xx = x;
                    zz = minusz;
                }
                case 2 -> {
                    xx = plusx;
                    zz = minusz;
                }
                case 3 -> {
                    xx = plusx;
                    zz = z;
                }
                case 4 -> {
                    xx = plusx;
                    zz = plusz;
                }
                case 5 -> {
                    xx = x;
                    zz = plusz;
                }
                case 6 -> {
                    xx = minusx;
                    zz = plusz;
                }
                case 7 -> {
                    xx = minusx;
                    zz = z;
                }
                case 8 -> {
                    xx = x;
                    zz = z;
                }
                default -> {
                    xx = signx;
                    zz = signz;
                }
            }
            for (int yy = 0; yy < 4; yy++) {
                Material mat = colData[yy].getMaterial();
                switch (mat) {
                    case WHITE_WOOL, ORANGE_WOOL, MAGENTA_WOOL, LIGHT_BLUE_WOOL, YELLOW_WOOL, LIME_WOOL, PINK_WOOL, GRAY_WOOL, LIGHT_GRAY_WOOL, CYAN_WOOL, PURPLE_WOOL, BLUE_WOOL, BROWN_WOOL, GREEN_WOOL, RED_WOOL, BLACK_WOOL -> {
                        if (preset.equals(ChameleonPreset.PARTY) || (preset.equals(ChameleonPreset.FLOWER) && mat.equals(Material.WHITE_WOOL))) {
                            TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, random_colour);
                        }
                        TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colData[yy]);
                    }
                    case TORCH, GLOWSTONE, REDSTONE_LAMP -> { // lamps, glowstone and torches
                        if (mat.equals(Material.TORCH)) {
                            do_at_end.add(new TARDISInstantPreset.ProblemBlock(new Location(world, xx, (y + yy), zz), colData[yy]));
                        } else {
                            if (colData[yy] instanceof Lightable lit) {
                                lit.setLit(true);
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, lit);
                            } else {
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colData[yy]);
                            }
                        }
                    }
                    case NETHER_PORTAL -> {
                        TARDISBlockSetters.setBlock(world, xx, (y + yy + 1), zz, Material.OBSIDIAN);
                        TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colData[yy]);
                    }
                    case SKELETON_SKULL -> {
                        Rotatable rotatable = (Rotatable) colData[yy];
                        rotatable.setRotation(plugin.getPresetBuilder().getSkullDirection(COMPASS.EAST));
                        TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, rotatable);
                    }
                    default -> { // everything else
                        if (mat.equals(Material.LEVER) || mat.equals(Material.STONE_BUTTON) || mat.equals(Material.OAK_BUTTON)) {
                            do_at_end.add(new TARDISInstantPreset.ProblemBlock(new Location(world, xx, (y + yy), zz), colData[yy]));
                        } else {
                            TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colData[yy]);
                            if (cid != -1 && Tag.ALL_SIGNS.isTagged(mat) && xx == signx && zz == signz) {
                                // get the sign data and write the sign
                                HashMap<String, Object> where = new HashMap<>();
                                where.put("chameleon_id", cid);
                                ResultSetShells rss = new ResultSetShells(plugin, where);
                                if (rss.resultSet()) {
                                    HashMap<String, String> map = rss.getData().get(0);
                                    Sign sign = (Sign) world.getBlockAt(xx, (y + yy), zz).getState();
                                    SignSide front = sign.getSide(Side.FRONT);
                                    front.setLine(0, map.get("line1"));
                                    front.setLine(1, map.get("line2"));
                                    front.setLine(2, map.get("line3"));
                                    front.setLine(3, map.get("line4"));
                                    sign.update();
                                }
                            }
                        }
                    }
                }
            }
        }
        do_at_end.forEach((pb) -> TARDISBlockSetters.setBlock(pb.getL().getWorld(), pb.getL().getBlockX(), pb.getL().getBlockY(), pb.getL().getBlockZ(), pb.getData()));
    }
}
