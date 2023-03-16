/*
 * Copyright (C) 2023 eccentric_nz
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.MODULE;
import me.eccentric_nz.tardischunkgenerator.custombiome.*;
import me.eccentric_nz.tardischunkgenerator.disguise.*;
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
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.craftbukkit.v1_19_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftVillager;
import org.bukkit.entity.*;
import org.bukkit.map.MapView;
import org.bukkit.util.Vector;

public class TARDISHelper implements TARDISHelperAPI {

    public static final HashMap<String, net.minecraft.world.level.biome.Biome> biomeMap = new HashMap<>();
    public static boolean colourSkies;

    public void enable(TARDIS plugin) {
        // get server default world name
        String levelName = BiomeUtilities.getLevelName();
        // load custom biomes if they are enabled
        boolean aPlanetIsEnabled = false;
        if (plugin.getPlanetsConfig().getBoolean("planets.gallifrey.enabled")) {
            plugin.getConsole().sendMessage(MODULE.HELPER.getName() + "Adding custom biome for planet Gallifrey...");
            CustomBiome.addCustomBiome(TARDISBiomeData.BADLANDS);
            aPlanetIsEnabled = true;
        }
        if (plugin.getPlanetsConfig().getBoolean("planets.skaro.enabled")) {
            plugin.getServer().getConsoleSender().sendMessage(MODULE.HELPER.getName() + "Adding custom biome for planet Skaro...");
            CustomBiome.addCustomBiome(TARDISBiomeData.DESERT);
            aPlanetIsEnabled = true;
        }
        colourSkies = (aPlanetIsEnabled && plugin.getPlanetsConfig().getBoolean("colour_skies"));
        // should we filter the log?
        if (plugin.getConfig().getBoolean("debug")) {
            // yes we should!
            String basePath = plugin.getServer().getWorldContainer() + File.separator + "plugins" + File.separator + "TARDIS" + File.separator;
            filterLog(basePath + "filtered.log");
            plugin.getServer().getConsoleSender().sendMessage(MODULE.HELPER.getName() + "Starting filtered logging for TARDIS plugins...");
            plugin.getServer().getConsoleSender().sendMessage(MODULE.HELPER.getName() + "Log file located at 'plugins/TARDIS/filtered.log'");
        }
        // register disguise listener
        plugin.getServer().getPluginManager().registerEvents(new TARDISDisguiseListener(plugin), plugin);
    }

    @Override
    public void nameFurnaceGUI(Block block, String name) {
        if (block != null) {
            ServerLevel ws = ((CraftWorld) block.getWorld()).getHandle();
            BlockPos bp = new BlockPos(block.getX(), block.getY(), block.getZ());
            BlockEntity tile = ws.getBlockEntity(bp);
            if (tile instanceof FurnaceBlockEntity furnace) {
                furnace.setCustomName(MutableComponent.create(new LiteralContents((name))));
            }
        }
    }

    @Override
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

//    @Override
//    public void setFallFlyingTag(org.bukkit.entity.Entity e) {
//        Entity nmsEntity = ((CraftEntity) e).getHandle();
//        CompoundTag tag = new CompoundTag();
//        // writes the entity's NBT data to the `tag` object
//        nmsEntity.save(tag);
//        tag.putBoolean("FallFlying", true);
//        // sets the entity's tag to the altered `tag`
//        nmsEntity.load(tag);
//    }

    @Override
    public void setRandomSeed(String world) {
        File file = new File(Bukkit.getWorldContainer().getAbsolutePath() + File.separator + world + File.separator + "level.dat");
        if (file.exists()) {
            try {
                FileInputStream fileinputstream = new FileInputStream(file);
                CompoundTag tagCompound = NbtIo.readCompressed(fileinputstream);
                CompoundTag data = tagCompound.getCompound("Data");
                fileinputstream.close();
                long random = new Random().nextLong();
                // set RandomSeed tag
                data.putLong("RandomSeed", random);
                tagCompound.put("Data", data);
                FileOutputStream fileoutputstream = new FileOutputStream(file);
                NbtIo.writeCompressed(tagCompound, fileoutputstream);
                fileoutputstream.close();
            } catch (IOException ex) {
                Bukkit.getLogger().log(Level.SEVERE, MODULE.HELPER.getName() + ex.getMessage());
            }
        }
    }

    @Override
    public void setLevelName(String oldName, String newName) {
        File file = new File(Bukkit.getWorldContainer().getAbsolutePath() + File.separator + oldName + File.separator + "level.dat");
        if (file.exists()) {
            try {
                FileInputStream fileinputstream = new FileInputStream(file);
                CompoundTag tagCompound = NbtIo.readCompressed(fileinputstream);
                CompoundTag data = tagCompound.getCompound("Data");
                fileinputstream.close();
                // set LevelName tag
                data.putString("LevelName", newName);
                tagCompound.put("Data", data);
                FileOutputStream fileoutputstream = new FileOutputStream(file);
                NbtIo.writeCompressed(tagCompound, fileoutputstream);
                fileoutputstream.close();
                Bukkit.getLogger().log(Level.INFO, MODULE.HELPER.getName() + "Renamed level to " + newName);
                // rename the directory
                File directory = new File(Bukkit.getWorldContainer().getAbsolutePath() + File.separator + oldName);
                File folder = new File(Bukkit.getWorldContainer().getAbsolutePath() + File.separator + newName);
                if (directory.renameTo(folder)) {
                    Bukkit.getLogger().log(Level.INFO, MODULE.HELPER.getName() + "Renamed directory to " + newName);
                }
            } catch (IOException ex) {
                Bukkit.getLogger().log(Level.SEVERE, MODULE.HELPER.getName() + ex.getMessage());
            }
        }
    }

    @Override
    public void setWorldGameMode(String world, GameMode gm) {
        File file = new File(Bukkit.getWorldContainer().getAbsolutePath() + File.separator + world + File.separator + "level.dat");
        if (file.exists()) {
            try {
                FileInputStream fileinputstream = new FileInputStream(file);
                CompoundTag tagCompound = NbtIo.readCompressed(fileinputstream);
                CompoundTag data = tagCompound.getCompound("Data");
                fileinputstream.close();
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
            } catch (IOException ex) {
                Bukkit.getLogger().log(Level.SEVERE, MODULE.HELPER.getName() + ex.getMessage());
            }
        }
    }

    @Override
    public TARDISPlanetData getLevelData(String world) {
        File file = new File(Bukkit.getWorldContainer().getAbsolutePath() + File.separator + world + File.separator + "level.dat");
        if (file.exists()) {
            try {
                FileInputStream fileinputstream = new FileInputStream(file);
                CompoundTag tagCompound = NbtIo.readCompressed(fileinputstream);
                fileinputstream.close();
                CompoundTag data = tagCompound.getCompound("Data");
                // get GameType tag
                GameMode gameMode;
                int gm = data.getInt("GameType");
                gameMode = switch (gm) {
                    case 1 -> GameMode.CREATIVE;
                    case 2 -> GameMode.ADVENTURE;
                    case 3 -> GameMode.SPECTATOR;
                    default -> GameMode.SURVIVAL;
                };
                // get generatorName tag
                WorldType worldType;
                String wt = data.getString("generatorName");
                worldType = switch (wt.toLowerCase(Locale.ROOT)) {
                    case "flat" -> WorldType.FLAT;
                    case "largebiomes" -> WorldType.LARGE_BIOMES;
                    case "amplified" -> WorldType.AMPLIFIED;
                    default -> WorldType.NORMAL; // default or unknown
                };
                World.Environment environment = World.Environment.NORMAL;
                File dimDashOne = new File(Bukkit.getWorldContainer().getAbsolutePath() + File.separator + world + File.separator + "DIM-1");
                File dimOne = new File(Bukkit.getWorldContainer().getAbsolutePath() + File.separator + world + File.separator + "DIM1");
                if (dimDashOne.exists() && !dimOne.exists()) {
                    environment = World.Environment.NETHER;
                }
                if (dimOne.exists() && !dimDashOne.exists()) {
                    environment = World.Environment.THE_END;
                }
                return new TARDISPlanetData(gameMode, environment, worldType);
            } catch (IOException ex) {
                Bukkit.getLogger().log(Level.SEVERE, MODULE.HELPER.getName() + ex.getMessage());
                return new TARDISPlanetData(GameMode.SURVIVAL, World.Environment.NORMAL, WorldType.NORMAL);
            }
        }
        Bukkit.getLogger().log(Level.INFO, MODULE.HELPER.getName() + "Defaulted to GameMode.SURVIVAL, World.Environment.NORMAL, WorldType.NORMAL");
        return new TARDISPlanetData(GameMode.SURVIVAL, World.Environment.NORMAL, WorldType.NORMAL);
    }

    @Override
    public void disguise(EntityType entityType, Player player) {
        new TARDISDisguiser(entityType, player).disguiseToAll();
    }

    @Override
    public void disguise(EntityType entityType, Player player, Object[] options) {
        new TARDISDisguiser(entityType, player, options).disguiseToAll();
    }

    @Override
    public void disguise(Player player, String name) {
        new TARDISChameleonArchDisguiser(player).changeSkin(name);
    }

    @Override
    public void disguise(Player player, UUID uuid) {
        new TARDISPlayerDisguiser(player, uuid).disguiseToAll();
    }

    @Override
    public void undisguise(Player player) {
        new TARDISDisguiser(player).removeDisguise();
    }

    @Override
    public void reset(Player player) {
        new TARDISChameleonArchDisguiser(player).resetSkin();
    }

    @Override
    public int spawnEmergencyProgrammeOne(Player player, Location location) {
        return new TARDISEPSDisguiser(player, location).showToAll();
    }

    @Override
    public void removeNPC(int id, World world) {
        TARDISEPSDisguiser.removeNPC(id, world);
    }

    @Override
    public void disguiseArmourStand(ArmorStand stand, EntityType entityType, Object[] options) {
        new TARDISArmourStandDisguiser(stand, entityType, options).disguiseToAll();
    }

    @Override
    public void undisguiseArmourStand(ArmorStand stand) {
        TARDISArmourStandDisguiser.removeDisguise(stand);
    }

    @Override
    public void updateMap(World world, MapView mapView) {
        new TARDISMapUpdater(world, mapView.getCenterX(), mapView.getCenterZ()).update(mapView);
    }

    @Override
    public Location searchBiome(World world, Biome biome, Location policeBox) {
        return BiomeUtilities.searchBiome(world, biome, policeBox);
    }

    @Override
    public void addCustomBiome(String biome) {
        CustomBiomeData data;
        if (biome.equalsIgnoreCase("gallifrey")) {
            data = TARDISBiomeData.BADLANDS;
        } else {
            data = TARDISBiomeData.DESERT;
        }
        CustomBiome.addCustomBiome(data);
    }

    @Override
    public void setCustomBiome(String biome, Chunk chunk) {
        new BiomeHelper().setCustomBiome(biome, chunk);
    }

    @Override
    public void removeTileEntity(org.bukkit.block.BlockState tile) {
        net.minecraft.world.level.chunk.ChunkAccess chunk = ((CraftChunk) tile.getChunk()).getHandle();
        BlockPos position = new BlockPos(tile.getLocation().getBlockX(), tile.getLocation().getBlockY(), tile.getLocation().getBlockZ());
        chunk.removeBlockEntity(position);
        tile.getBlock().setType(Material.AIR);
    }

    @Override
    public void setPowerableBlockInteract(Block block) {
        Direction direction = Direction.NORTH;
        if (block.getBlockData() instanceof Directional directional) {
            direction = Direction.byName(directional.getFacing().toString().toLowerCase(Locale.ROOT));
        }
        BlockState data = ((CraftBlock) block).getNMS();
        net.minecraft.world.level.Level world = ((CraftWorld) block.getWorld()).getHandle();
        BlockPos position = ((CraftBlock) block).getPosition();
        data.use(world, null, null, BlockHitResult.miss(data.getOffset(world, position), direction, position));
    }

    @Override
    public void growTree(String tree, Location location) {
        try {
            TARDISTree type = TARDISTree.valueOf(tree.toUpperCase(Locale.ROOT));
            CustomTree.grow(type, location);
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().log(Level.WARNING, MODULE.HELPER.getName() + "Invalid TARDISTree type specified!");
        }
    }

    @Override
    public void growTree(Location location, Material base, Material hat, Material stem, Material decor) {
        CustomTree.grow(location, base, hat, stem, decor);
    }

    @Override
    public List<Material> getTreeMatrials() {
        return CubicMaterial.cubes;
    }

    @Override
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

    @Override
    public void setReputation(Villager villager, UUID uuid, int[] reputation) {
        net.minecraft.world.entity.npc.Villager v = ((CraftVillager) villager).getHandle();
        GossipContainer entries = v.getGossips();
        entries.add(uuid, GossipType.MAJOR_NEGATIVE, reputation[GossipType.MAJOR_NEGATIVE.ordinal()]);
        entries.add(uuid, GossipType.MINOR_NEGATIVE, reputation[GossipType.MINOR_NEGATIVE.ordinal()]);
        entries.add(uuid, GossipType.MINOR_POSITIVE, reputation[GossipType.MINOR_POSITIVE.ordinal()]);
        entries.add(uuid, GossipType.MAJOR_POSITIVE, reputation[GossipType.MAJOR_POSITIVE.ordinal()]);
        entries.add(uuid, GossipType.TRADING, reputation[GossipType.TRADING.ordinal()]);
    }

    @Override
    public int castFakeItemFrame(ItemFrame frame, Player player, Vector location) {
        return TARDISItemFrameFaker.cast(frame, player, location);
    }

    @Override
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

    @Override
    public void listBlockColours() {
        GetBlockColours.list();
    }
}
