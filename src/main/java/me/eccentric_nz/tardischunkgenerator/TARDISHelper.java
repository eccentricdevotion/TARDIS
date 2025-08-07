/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (location your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardischunkgenerator;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.lazarus.disguise.TARDISArmourStandDisguiser;
import me.eccentric_nz.TARDIS.lazarus.disguise.TARDISDisguiseListener;
import me.eccentric_nz.TARDIS.lazarus.disguise.TARDISDisguiser;
import me.eccentric_nz.TARDIS.lazarus.disguise.TARDISPlayerDisguiser;
import me.eccentric_nz.TARDIS.skins.TARDISChameleonArchDisguiser;
import me.eccentric_nz.tardischunkgenerator.custombiome.CubicMaterial;
import me.eccentric_nz.tardischunkgenerator.custombiome.CustomBiome;
import me.eccentric_nz.tardischunkgenerator.custombiome.CustomBiomeData;
import me.eccentric_nz.tardischunkgenerator.custombiome.TARDISBiomeData;
import me.eccentric_nz.tardischunkgenerator.helpers.GetBlockColours;
import me.eccentric_nz.tardischunkgenerator.helpers.TARDISItemFrameFaker;
import me.eccentric_nz.tardischunkgenerator.helpers.TARDISMapUpdater;
import me.eccentric_nz.tardischunkgenerator.helpers.TARDISPlanetData;
import me.eccentric_nz.tardischunkgenerator.logging.TARDISLogFilter;
import me.eccentric_nz.tardischunkgenerator.worldgen.feature.CustomTree;
import me.eccentric_nz.tardischunkgenerator.worldgen.feature.TARDISTree;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftVillager;
import org.bukkit.entity.*;
import org.bukkit.map.MapView;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class TARDISHelper {

    public static final HashMap<String, net.minecraft.world.level.biome.Biome> biomeMap = new HashMap<>();
    public static boolean colourSkies;
    private final TARDIS plugin;

    public TARDISHelper(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        // should we filter the log?
        if (plugin.getConfig().getBoolean("debug")) {
            // yes we should!
            String basePath = plugin.getServer().getWorldContainer() + File.separator + "plugins" + File.separator + "TARDIS" + File.separator;
            filterLog(basePath + "filtered.log");
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER, "Starting filtered logging for TARDIS...");
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER, "Log file located at 'plugins/TARDIS/filtered.log'");
        }
        // load custom biomes if they are enabled
        boolean aPlanetIsEnabled = false;
        if (plugin.getPlanetsConfig().getBoolean("planets.gallifrey.enabled")) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER, "Adding custom biome for planet Gallifrey...");
            CustomBiome.addCustomBiome(TARDISBiomeData.BADLANDS);
            aPlanetIsEnabled = true;
        }
        if (plugin.getPlanetsConfig().getBoolean("planets.skaro.enabled")) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER, "Adding custom biome for planet Skaro...");
            CustomBiome.addCustomBiome(TARDISBiomeData.DESERT);
            aPlanetIsEnabled = true;
        }
        colourSkies = (aPlanetIsEnabled && plugin.getPlanetsConfig().getBoolean("colour_skies"));
        // register disguise listener
        plugin.getServer().getPluginManager().registerEvents(new TARDISDisguiseListener(plugin), plugin);
    }

    public void nameFurnaceGUI(Block block, String name) {
        if (block != null) {
            ServerLevel ws = ((CraftWorld) block.getWorld()).getHandle();
            BlockPos bp = new BlockPos(block.getX(), block.getY(), block.getZ());
            BlockEntity tile = ws.getBlockEntity(bp);
            if (tile instanceof FurnaceBlockEntity furnace) {
                furnace.name = Component.literal(name);
            }
        }
    }

    public boolean isArtronFurnace(Block block) {
        if (block != null) {
            ServerLevel ws = ((CraftWorld) block.getWorld()).getHandle();
            BlockPos bp = new BlockPos(block.getX(), block.getY(), block.getZ());
            BlockEntity tile = ws.getBlockEntity(bp);
            if (tile instanceof FurnaceBlockEntity furnace && furnace.getCustomName() != null) {
                return furnace.getCustomName().getString().equals("TARDIS Artron Furnace");
            }
        }
        return false;
    }

    public void setLevelName(String oldName, String newName) {
        File file = new File(Bukkit.getWorldContainer().getAbsolutePath() + File.separator + oldName + File.separator + "level.dat");
        if (file.exists()) {
            try {
                CompoundTag tagCompound;
                CompoundTag data;
                try (FileInputStream fileinputstream = new FileInputStream(file)) {
                    tagCompound = NbtIo.readCompressed(fileinputstream, NbtAccounter.unlimitedHeap());
                    if (tagCompound.getCompound("Data").isPresent()) {
                        data = tagCompound.getCompound("Data").get();
                        // set LevelName tag
                        data.putString("LevelName", newName);
                        tagCompound.put("Data", data);
                        FileOutputStream fileoutputstream = new FileOutputStream(file);
                        NbtIo.writeCompressed(tagCompound, fileoutputstream);
                        fileoutputstream.close();
                        plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER, "Renamed level to " + newName);
                        // rename the directory
                        File directory = new File(Bukkit.getWorldContainer().getAbsolutePath() + File.separator + oldName);
                        File folder = new File(Bukkit.getWorldContainer().getAbsolutePath() + File.separator + newName);
                        if (directory.renameTo(folder)) {
                            plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER, "Renamed directory to " + newName);
                        }
                    }
                }
            } catch (IOException ex) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER_SEVERE, ex.getMessage());
            }
        }
    }

    public void setWorldGameMode(String world, GameMode gm) {
        File file = new File(Bukkit.getWorldContainer().getAbsolutePath() + File.separator + world + File.separator + "level.dat");
        if (file.exists()) {
            try {
                CompoundTag tagCompound;
                CompoundTag data;
                try (FileInputStream fileinputstream = new FileInputStream(file)) {
                    tagCompound = NbtIo.readCompressed(fileinputstream, NbtAccounter.unlimitedHeap());
                    if (tagCompound.getCompound("Data").isPresent()) {
                        data = tagCompound.getCompound("Data").get();
                        int mode = switch (gm) {
                            case CREATIVE -> 1;
                            case ADVENTURE -> 2;
                            case SPECTATOR -> 3;
                            default -> 0; // SURVIVAL
                        };
                        // set GameType tag
                        data.putInt("GameType", mode);
                        tagCompound.put("Data", data);
                        FileOutputStream fileoutputstream = new FileOutputStream(file);
                        NbtIo.writeCompressed(tagCompound, fileoutputstream);
                        fileoutputstream.close();
                    }
                }
            } catch (IOException ex) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER_SEVERE, ex.getMessage());
            }
        }
    }

    public TARDISPlanetData getLevelData(String world) {
        File file = new File(Bukkit.getWorldContainer().getAbsolutePath() + File.separator + world + File.separator + "level.dat");
        if (file.exists()) {
            try {
                FileInputStream fileinputstream = new FileInputStream(file);
                CompoundTag tagCompound = NbtIo.readCompressed(fileinputstream, NbtAccounter.unlimitedHeap());
                fileinputstream.close();
                GameMode gameMode = GameMode.SURVIVAL;
                WorldType worldType = WorldType.NORMAL;
                World.Environment environment = World.Environment.NORMAL;
                Difficulty difficulty = Difficulty.NORMAL;
                if (tagCompound.getCompound("Data").isPresent()) {
                    CompoundTag data = tagCompound.getCompound("Data").get();
                    // get GameType tag
                    if (data.getInt("GameType").isPresent()) {
                        int gm = data.getInt("GameType").get();
                        gameMode = switch (gm) {
                            case 1 -> GameMode.CREATIVE;
                            case 2 -> GameMode.ADVENTURE;
                            case 3 -> GameMode.SPECTATOR;
                            default -> GameMode.SURVIVAL;
                        };
                    }
                    // get generatorName tag
                    if (data.getString("generatorName").isPresent()) {
                        String wt = data.getString("generatorName").get();
                        worldType = switch (wt.toLowerCase(Locale.ROOT)) {
                            case "flat" -> WorldType.FLAT;
                            case "largebiomes" -> WorldType.LARGE_BIOMES;
                            case "amplified" -> WorldType.AMPLIFIED;
                            default -> WorldType.NORMAL; // default or unknown
                        };
                    }
                    File dimDashOne = new File(Bukkit.getWorldContainer().getAbsolutePath() + File.separator + world + File.separator + "DIM-1");
                    File dimOne = new File(Bukkit.getWorldContainer().getAbsolutePath() + File.separator + world + File.separator + "DIM1");
                    if (dimDashOne.exists() && !dimOne.exists()) {
                        environment = World.Environment.NETHER;
                    }
                    if (dimOne.exists() && !dimDashOne.exists()) {
                        environment = World.Environment.THE_END;
                    }
                    // 0 is Peaceful, 1 is Easy, 2 is Normal, and 3 is Hard
                    if (data.getInt("Difficulty").isPresent()) {
                        int diff = data.getInt("Difficulty").get();
                        switch (diff) {
                            case 0 -> difficulty = Difficulty.PEACEFUL;
                            case 1 -> difficulty = Difficulty.EASY;
                            case 3 -> difficulty = Difficulty.HARD;
                            default -> {
                            }
                        }
                    }
                }
                return new TARDISPlanetData(gameMode, environment, worldType, difficulty);
            } catch (IOException ex) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER_SEVERE, ex.getMessage());
                return new TARDISPlanetData(GameMode.SURVIVAL, World.Environment.NORMAL, WorldType.NORMAL, Difficulty.NORMAL);
            }
        }
        plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER, "Defaulted to GameMode.SURVIVAL, World.Environment.NORMAL, WorldType.NORMAL");
        return new TARDISPlanetData(GameMode.SURVIVAL, World.Environment.NORMAL, WorldType.NORMAL, Difficulty.NORMAL);
    }

    public void disguise(EntityType entityType, Player player) {
        new TARDISDisguiser(entityType, player).disguiseToAll();
    }

    public void disguise(EntityType entityType, Player player, Object[] options) {
        new TARDISDisguiser(entityType, player, options).disguiseToAll();
    }

    public void disguise(Player player, String name) {
        new TARDISChameleonArchDisguiser(plugin, player).changeSkin(name);
    }

    public void disguise(Player player, UUID uuid) {
        new TARDISPlayerDisguiser(player, uuid);
    }

    public void undisguise(Player player) {
        new TARDISDisguiser(player).removeDisguise();
    }

    public void disguiseArmourStand(ArmorStand stand, EntityType entityType, Object[] options) {
        new TARDISArmourStandDisguiser(stand, entityType, options).disguiseToAll();
    }

    public void undisguiseArmourStand(ArmorStand stand) {
        TARDISArmourStandDisguiser.removeDisguise(stand);
    }

    public void updateMap(World world, MapView mapView) {
        new TARDISMapUpdater(world).update(mapView);
    }

    public void addCustomBiome(String biome) {
        CustomBiomeData data;
        if (biome.equalsIgnoreCase("gallifrey")) {
            data = TARDISBiomeData.BADLANDS;
        } else if (biome.equalsIgnoreCase("skaro")) {
            data = TARDISBiomeData.DESERT;
        } else {
            data = TARDISBiomeData.EYE;
        }
        CustomBiome.addCustomBiome(data);
    }

    public void removeTileEntity(org.bukkit.block.BlockState tile) {
        BlockPos position = new BlockPos(tile.getLocation().getBlockX(), tile.getLocation().getBlockY(), tile.getLocation().getBlockZ());
        ServerLevel level = ((CraftWorld) tile.getLocation().getWorld()).getHandle();
        ChunkAccess chunk = level.getChunk(position);
        chunk.removeBlockEntity(position);
        tile.getBlock().setType(Material.AIR);
    }

    public void setPowerableBlockInteract(Block block) {
        Direction direction = Direction.NORTH;
        if (block.getBlockData() instanceof Directional directional) {
            direction = Direction.byName(directional.getFacing().toString().toLowerCase(Locale.ROOT));
        }
        BlockState data = ((CraftBlock) block).getNMS();
        net.minecraft.world.level.Level world = ((CraftWorld) block.getWorld()).getHandle();
        BlockPos position = ((CraftBlock) block).getPosition();
        data.useWithoutItem(world, null, BlockHitResult.miss(data.getOffset(position), direction, position));
    }

    public void growTree(String tree, Location location) {
        try {
            TARDISTree type = TARDISTree.valueOf(tree.toUpperCase(Locale.ROOT));
            CustomTree.grow(type, location);
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER_WARNING, "Invalid TARDISTree type specified!");
        }
    }

    public void growTree(Location location, Material base, Material hat, Material stem, Material decor) {
        CustomTree.grow(location, base, hat, stem, decor);
    }

    public List<Material> getTreeMaterials() {
        return CubicMaterial.cubes;
    }

    public int[] getReputation(Villager villager, UUID uuid) {
        net.minecraft.world.entity.npc.Villager v = ((CraftVillager) villager).getHandle();
        GossipContainer entries = v.getGossips();
        int[] reputation = new int[5];
        reputation[GossipType.MAJOR_NEGATIVE.ordinal()] = entries.getReputation(uuid, gossipType -> gossipType == GossipType.MAJOR_NEGATIVE);
        reputation[GossipType.MINOR_NEGATIVE.ordinal()] = entries.getReputation(uuid, gossipType -> gossipType == GossipType.MINOR_NEGATIVE);
        reputation[GossipType.MINOR_POSITIVE.ordinal()] = entries.getReputation(uuid, gossipType -> gossipType == GossipType.MINOR_POSITIVE);
        reputation[GossipType.MAJOR_POSITIVE.ordinal()] = entries.getReputation(uuid, gossipType -> gossipType == GossipType.MAJOR_POSITIVE);
        reputation[GossipType.TRADING.ordinal()] = entries.getReputation(uuid, gossipType -> gossipType == GossipType.TRADING);
        return reputation;
    }

    public void setReputation(Villager villager, UUID uuid, int[] reputation) {
        net.minecraft.world.entity.npc.Villager v = ((CraftVillager) villager).getHandle();
        GossipContainer entries = v.getGossips();
        entries.add(uuid, GossipType.MAJOR_NEGATIVE, reputation[GossipType.MAJOR_NEGATIVE.ordinal()]);
        entries.add(uuid, GossipType.MINOR_NEGATIVE, reputation[GossipType.MINOR_NEGATIVE.ordinal()]);
        entries.add(uuid, GossipType.MINOR_POSITIVE, reputation[GossipType.MINOR_POSITIVE.ordinal()]);
        entries.add(uuid, GossipType.MAJOR_POSITIVE, reputation[GossipType.MAJOR_POSITIVE.ordinal()]);
        entries.add(uuid, GossipType.TRADING, reputation[GossipType.TRADING.ordinal()]);
    }

    public int castFakeItemFrame(ItemFrame frame, Player player, Vector location) {
        return TARDISItemFrameFaker.cast(frame, player, location);
    }

    public void removeFakeItemFrame(int id, Player player) {
        TARDISItemFrameFaker.remove(id, player);
    }

    /**
     * Start filtering logs for TARDIS related information
     *
     * @param path the file path for the filtered log file
     */
    public void filterLog(String path) {
        ((Logger) LogManager.getRootLogger()).addFilter(new TARDISLogFilter(path));
    }

    public void listBlockColours() {
        GetBlockColours.list();
    }
}
