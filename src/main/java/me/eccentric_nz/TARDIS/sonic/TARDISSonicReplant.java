package me.eccentric_nz.TARDIS.sonic;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

public class TARDISSonicReplant implements Runnable {

    private final TARDIS plugin;
    private final Block block;
    private final Block under;
    private final Material type;
    private final Material air = Material.AIR;
    private final Material dirt = Material.DIRT;
    private final Material grass = Material.GRASS;
    private final Material log = Material.LOG;
    private final Material sand = Material.SAND;
    private final Material soil = Material.SOIL;
    private final Material soul = Material.SOUL_SAND;
    private final HashMap<BlockFace, Byte> c_data = new HashMap<BlockFace, Byte>();

    public TARDISSonicReplant(TARDIS plugin, Block block, Material type) {
        this.plugin = plugin;
        this.block = block;
        this.under = block.getRelative(BlockFace.DOWN);
        this.type = type;
        this.c_data.put(BlockFace.NORTH, (byte) 2);
        this.c_data.put(BlockFace.WEST, (byte) 1);
        this.c_data.put(BlockFace.SOUTH, (byte) 0);
        this.c_data.put(BlockFace.EAST, (byte) 3);
    }

    @Override
    public void run() {
        switch (type) {
            case BEETROOT_SEEDS:
                if (under.getType().equals(soil) && block.getType().equals(air)) {
                    block.setType(Material.BEETROOT_BLOCK);
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.BEETROOT));
                }
                break;
            case CARROT_ITEM:
                if (under.getType().equals(soil) && block.getType().equals(air)) {
                    block.setType(Material.CARROT);
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.CARROT_ITEM));
                }
                break;
            case INK_SACK:
                if (block.getType().equals(air)) {
                    for (BlockFace f : plugin.getGeneralKeeper().getFaces()) {
                        // only jungle logs
                        if (block.getRelative(f).getType().equals(log) && (block.getRelative(f).getData() == 3)) {
                            block.setType(Material.COCOA);
                            block.setData(c_data.get(f));
                        }
                    }
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.INK_SACK, 1, (short) 3));
                }
                break;
            case MELON_SEEDS:
                if (under.getType().equals(soil) && block.getType().equals(air)) {
                    block.setType(Material.MELON_STEM);
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.MELON_SEEDS));
                }
                break;
            case NETHER_WARTS:
                if (under.getType().equals(soul) && block.getType().equals(air)) {
                    block.setType(Material.NETHER_STALK);
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.NETHER_WARTS));
                }
                break;
            case POTATO_ITEM:
                if (under.getType().equals(soil) && block.getType().equals(air)) {
                    block.setType(Material.POTATO);
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.POTATO_ITEM));
                }
                break;
            case PUMPKIN_SEEDS:
                if (under.getType().equals(soil) && block.getType().equals(air)) {
                    block.setType(Material.PUMPKIN_STEM);
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.PUMPKIN_SEEDS));
                }
                break;
            case SUGAR_CANE:
                if ((under.getType().equals(grass) || under.getType().equals(dirt) || under.getType().equals(sand)) && block.getType().equals(air)) {
                    block.setType(Material.SUGAR_CANE_BLOCK);
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SUGAR_CANE));
                }
                break;
            case SEEDS:
                if (under.getType().equals(soil) && block.getType().equals(air)) {
                    block.setType(Material.CROPS);
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SEEDS));
                }
                break;
            default:
                break;
        }
    }
}
