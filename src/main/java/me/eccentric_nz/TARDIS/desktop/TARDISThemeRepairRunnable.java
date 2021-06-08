/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.desktop;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.tardis.TARDISBuilderInstanceKeeper;
import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.api.event.TARDISDesktopThemeEvent;
import me.eccentric_nz.tardis.builders.FractalFence;
import me.eccentric_nz.tardis.builders.TARDISInteriorPostioning;
import me.eccentric_nz.tardis.builders.TARDISTIPSData;
import me.eccentric_nz.tardis.builders.TARDISTimeRotor;
import me.eccentric_nz.tardis.custommodeldata.TARDISMushroomBlockData;
import me.eccentric_nz.tardis.database.data.Archive;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.ConsoleSize;
import me.eccentric_nz.tardis.enumeration.Schematic;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.rooms.TARDISCondenserData;
import me.eccentric_nz.tardis.schematic.ArchiveReset;
import me.eccentric_nz.tardis.schematic.ResultSetArchive;
import me.eccentric_nz.tardis.schematic.TARDISSchematicGZip;
import me.eccentric_nz.tardis.utility.TARDISBlockSetters;
import me.eccentric_nz.tardis.utility.TARDISMaterials;
import me.eccentric_nz.tardis.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.tardis.utility.TARDISStaticUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;

import java.io.File;
import java.util.*;

/**
 * There was also a safety mechanism for when tardis rooms were deleted, automatically relocating any living beings in
 * the deleted room, depositing them in the control room.
 *
 * @author eccentric_nz
 */
public class TARDISThemeRepairRunnable extends TARDISThemeRunnable {

	private final TARDISPlugin plugin;
	private final UUID uuid;
	private final TARDISUpgradeData tud;
	private final List<Block> lampBlocks = new ArrayList<>();
	private final List<Block> fractalBlocks = new ArrayList<>();
	private final HashMap<Block, BlockData> postDoorBlocks = new HashMap<>();
	private final HashMap<Block, BlockData> postRedstoneTorchBlocks = new HashMap<>();
	private final HashMap<Block, BlockData> postTorchBlocks = new HashMap<>();
	private final HashMap<Block, BlockData> postLeverBlocks = new HashMap<>();
	private final HashMap<Block, BlockData> postSignBlocks = new HashMap<>();
	private final HashMap<Block, BlockData> postRepeaterBlocks = new HashMap<>();
	private final HashMap<Block, BlockData> postPistonBaseBlocks = new HashMap<>();
	private final HashMap<Block, BlockData> postStickyPistonBaseBlocks = new HashMap<>();
	private final HashMap<Block, BlockData> postPistonExtensionBlocks = new HashMap<>();
	private final boolean clean;
	private boolean running;
	private int id;
	private int slot;
	private int level = 0;
	private int row = 0;
	private int h;
	private int w;
	private int c;
	private int startx;
	private int starty;
	private int startz;
	private int j = 2;
	private World world;
	private List<Chunk> chunks;
	private Block postBedrock;
	private JsonArray arr;
	private Material wall_type;
	private Material floor_type;
	private HashMap<String, Object> set;
	private HashMap<String, Object> where;
	private boolean own_world;
	private Location wg1;
	private Location wg2;
	private Player player;
	private Location ender = null;
	private Archive archive;

	TARDISThemeRepairRunnable(TARDISPlugin plugin, UUID uuid, TARDISUpgradeData tud, boolean clean) {
		this.plugin = plugin;
		this.uuid = uuid;
		this.tud = tud;
		this.clean = clean;
	}

	@Override

	public void run() {
		// initialise
		if (!running) {
			// get Archive if nescessary
			if (tud.getSchematic().getPermission().equals("archive")) {
				HashMap<String, Object> wherean = new HashMap<>();
				wherean.put("uuid", uuid.toString());
				wherean.put("use", 1);
				ResultSetArchive rs = new ResultSetArchive(plugin, wherean);
				if (rs.resultSet()) {
					archive = rs.getArchive();
				} else {
					// abort
					Player cp = plugin.getServer().getPlayer(uuid);
					TARDISMessage.send(cp, "ARCHIVE_NOT_FOUND");
					// cancel task
					plugin.getServer().getScheduler().cancelTask(taskID);
					return;
				}
			}
			set = new HashMap<>();
			where = new HashMap<>();
			JsonObject obj;
			if (archive == null) {
				String directory = (tud.getSchematic().isCustom()) ? "user_schematics" : "schematics";
				String path = plugin.getDataFolder() + File.separator + directory + File.separator +
							  tud.getSchematic().getPermission() + ".tschm";
				File file = new File(path);
				if (!file.exists()) {
					plugin.debug("Could not find a schematic with that name!");
					// cancel task
					plugin.getServer().getScheduler().cancelTask(taskID);
					return;
				}
				// get JSON
				obj = TARDISSchematicGZip.unzip(path);
			} else {
				obj = archive.getJson();
			}
			// get dimensions
			assert obj != null;
			JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
			h = dimensions.get("height").getAsInt();
			w = dimensions.get("width").getAsInt();
			c = dimensions.get("length").getAsInt();
			// calculate startx, starty, startz
			HashMap<String, Object> wheret = new HashMap<>();
			wheret.put("uuid", uuid.toString());
			ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 0);
			if (!rs.resultSet()) {
				// ?
				return;
			}
			TARDIS tardis = rs.getTardis();
			slot = tardis.getTIPS();
			id = tardis.getTardisId();
			Chunk chunk = TARDISStaticLocationGetters.getChunk(tardis.getChunk());
			if (tud.getPrevious().getPermission().equals("ender")) {
				// remove ender crystal
				for (Entity end : chunk.getEntities()) {
					if (end.getType().equals(EntityType.ENDER_CRYSTAL)) {
						end.remove();
					}
				}
			}
			if (tardis.getRotor() != null) {
				// remove item frame and delete UUID in db
				ItemFrame itemFrame = TARDISTimeRotor.getItemFrame(tardis.getRotor());
				itemFrame.setItem(null, false);
				itemFrame.remove();
				TARDISTimeRotor.updateRotorRecord(id, "");
			}
			chunks = getChunks(chunk, tud.getSchematic());
			if (!tardis.getCreeper().isEmpty()) {
				// remove the charged creeper
				Location creeper = TARDISStaticLocationGetters.getLocationFromDB(tardis.getCreeper());
				assert creeper != null;
				Entity ent = Objects.requireNonNull(creeper.getWorld()).spawnEntity(creeper, EntityType.EGG);
				ent.getNearbyEntities(1.5d, 1.5d, 1.5d).forEach((e) -> {
					if (e instanceof Creeper) {
						e.remove();
					}
				});
				ent.remove();
			}
			if (slot != -1) { // default world - use TIPS
				TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
				TARDISTIPSData pos = tintpos.getTIPSData(slot);
				startx = pos.getCentreX();
				startz = pos.getCentreZ();
			} else {
				int[] gsl = plugin.getLocationUtils().getStartLocation(tardis.getTardisId());
				startx = gsl[0];
				startz = gsl[2];
			}
			starty = TARDISConstants.HIGHER.contains(tardis.getSchematic().getPermission()) ? 65 : 64;
			world = TARDISStaticLocationGetters.getWorld(tardis.getChunk());
			own_world = plugin.getConfig().getBoolean("creation.create_worlds");
			wg1 = new Location(world, startx, starty, startz);
			wg2 = new Location(world, startx + (w - 1), starty + (h - 1), startz + (c - 1));
			// wall/floor block prefs
			String[] wall = tud.getWall().split(":");
			String[] floor = tud.getFloor().split(":");
			wall_type = Material.valueOf(wall[0]);
			floor_type = Material.valueOf(floor[0]);
			// get input array
			arr = obj.get("input").getAsJsonArray();
			// clear existing lamp blocks
			HashMap<String, Object> whered = new HashMap<>();
			whered.put("tardis_id", id);
			plugin.getQueryFactory().doDelete("lamps", whered);
			// clear existing precious blocks
			HashMap<String, Object> wherep = new HashMap<>();
			wherep.put("tardis_id", id);
			wherep.put("police_box", 0);
			plugin.getQueryFactory().doDelete("blocks", wherep);
			// set running
			running = true;
			player = plugin.getServer().getPlayer(uuid);
			// remove upgrade data
			plugin.getTrackerKeeper().getUpgrades().remove(uuid);
			// clear lamps table as we'll be adding the new lamp positions later
			HashMap<String, Object> wherel = new HashMap<>();
			wherel.put("tardis_id", id);
			plugin.getQueryFactory().doDelete("lamps", wherel);
			plugin.getPM().callEvent(new TARDISDesktopThemeEvent(player, tardis, tud));
		}
		if (level == (h - 1) && row == (w - 1)) {
			// we're finished
			// remove items
			chunks.forEach((chink) -> {
				// remove dropped items
				for (Entity e : chink.getEntities()) {
					if (e instanceof Item) {
						e.remove();
					}
				}
			});
			// put on the door, redstone torches, signs, and the repeaters
			postDoorBlocks.forEach(Block::setBlockData);
			postRedstoneTorchBlocks.forEach(Block::setBlockData);
			postLeverBlocks.forEach(Block::setBlockData);
			postTorchBlocks.forEach(Block::setBlockData);
			postRepeaterBlocks.forEach(Block::setBlockData);
			postStickyPistonBaseBlocks.forEach((pspb, value) -> {
				plugin.getGeneralKeeper().getDoorPistons().add(pspb);
				pspb.setBlockData(value);
			});
			postPistonBaseBlocks.forEach((ppb, value) -> {
				plugin.getGeneralKeeper().getDoorPistons().add(ppb);
				ppb.setBlockData(value);
			});
			postPistonExtensionBlocks.forEach(Block::setBlockData);
			int s = 0;
			for (Map.Entry<Block, BlockData> entry : postSignBlocks.entrySet()) {
				Block psb = entry.getKey();
				psb.setBlockData(entry.getValue());
				// always make the control centre the first oak wall sign
				if (s == 0 && psb.getType().equals(Material.OAK_WALL_SIGN)) {
					Sign cs = (Sign) psb.getState();
					cs.setLine(0, "");
					cs.setLine(1, plugin.getSigns().getStringList("control").get(0));
					cs.setLine(2, plugin.getSigns().getStringList("control").get(1));
					cs.setLine(3, "");
					cs.update();
					String controlloc = psb.getLocation().toString();
					plugin.getQueryFactory().insertSyncControl(id, 22, controlloc, 0);
					s++;
				}
			}
			lampBlocks.forEach((lamp) -> {
				BlockData l = (tud.getSchematic().hasLanterns() || (archive != null &&
																	archive.isLanterns())) ? TARDISConstants.LANTERN : TARDISConstants.LAMP;
				lamp.setBlockData(l);
			});
			lampBlocks.clear();
			for (int f = 0; f < fractalBlocks.size(); f++) {
				FractalFence.grow(fractalBlocks.get(f), f);
			}
			if (postBedrock != null) {
				postBedrock.setBlockData(TARDISConstants.GLASS);
			}
			if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
				if (slot == -1) {
					plugin.getWorldGuardUtils().addWGProtection(player, wg1, wg2);
				}
			}
			if (ender != null) {
				Entity ender_crystal = world.spawnEntity(ender, EntityType.ENDER_CRYSTAL);
				((EnderCrystal) ender_crystal).setShowingBottom(false);
			}
			// finished processing - update tardis table!
			if (set.size() > 0) {
				where.put("tardis_id", id);
				plugin.getQueryFactory().doUpdate("tardis", set, where);
			}
			if (!tud.getSchematic().getPermission().equals("archive")) {
				// reset archive use back to 0
				new ArchiveReset(plugin, uuid.toString(), 0).resetUse();
			}
			if (tud.getSchematic().getPermission().equals("coral") &&
				tud.getPrevious().getConsoleSize().equals(ConsoleSize.TALL)) {
				// clean up space above coral console
				int tidy = starty + h;
				int plus = 32 - h;
				chunks.forEach((chk) -> setAir(chk.getBlock(0, 64, 0).getX(), tidy, chk.getBlock(0, 64, 0).getZ(), chk.getWorld(), plus));
			}
			// add / remove chunks from the chunks table
			HashMap<String, Object> wherec = new HashMap<>();
			wherec.put("tardis_id", id);
			plugin.getQueryFactory().doDelete("chunks", wherec);
			List<Chunk> chunkList = TARDISStaticUtils.getChunks(world, wg1.getChunk().getX(), wg1.getChunk().getZ(), w, c);
			// update chunks list in DB
			chunkList.forEach((hunk) -> {
				HashMap<String, Object> setc = new HashMap<>();
				setc.put("tardis_id", id);
				setc.put("world", world.getName());
				setc.put("x", hunk.getX());
				setc.put("z", hunk.getZ());
				plugin.getQueryFactory().doInsert("chunks", setc);
			});
			// remove blocks from condenser table if necessary
			if (!clean && plugin.getGeneralKeeper().getRoomCondenserData().containsKey(uuid)) {
				TARDISCondenserData c_data = plugin.getGeneralKeeper().getRoomCondenserData().get(uuid);
				int amount = 0;
				for (Map.Entry<String, Integer> entry : c_data.getBlockIDCount().entrySet()) {
					HashMap<String, Object> whered = new HashMap<>();
					whered.put("tardis_id", c_data.getTardisId());
					whered.put("block_data", entry.getKey());
					plugin.getQueryFactory().alterCondenserBlockCount(entry.getValue(), whered);
					amount += entry.getValue() * plugin.getCondensables().get(entry.getKey());
				}
				plugin.getGeneralKeeper().getRoomCondenserData().remove(uuid);
				if (amount > 0) {
					HashMap<String, Object> wheret = new HashMap<>();
					wheret.put("tardis_id", id);
					plugin.getQueryFactory().alterEnergyLevel("tardis", -amount, wheret, player);
				}
			}
			// cancel the task
			plugin.getServer().getScheduler().cancelTask(taskID);
			taskID = 0;
			String message = (clean) ? "REPAIR_CLEAN" : "REPAIR_DONE";
			TARDISMessage.send(player, message);
		} else {
			JsonArray floor = arr.get(level).getAsJsonArray();
			JsonArray r = (JsonArray) floor.get(row);
			// place a row of blocks
			for (int col = 0; col < c; col++) {
				JsonObject bb = r.get(col).getAsJsonObject();
				int x = startx + row;
				int y = starty + level;
				int z = startz + col;
				BlockData data = plugin.getServer().createBlockData(bb.get("data").getAsString());
				Material type = data.getMaterial();
				if (type.equals(Material.BEDROCK)) {
					// remember bedrock location to block off the beacon light
					String bedrocloc = world.getName() + ":" + x + ":" + y + ":" + z;
					set.put("beacon", bedrocloc);
					postBedrock = world.getBlockAt(x, y, z);
				}
				if (type.equals(Material.NOTE_BLOCK)) {
					// remember the location of this Disk Storage
					String storage = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
					plugin.getQueryFactory().insertSyncControl(id, 14, storage, 0);
					// set block data to correct MUSHROOM_STEM
					data = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(51));
				}
				if (type.equals(Material.ORANGE_WOOL)) {
					if (wall_type == Material.ORANGE_WOOL) {
						data = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(46));
					} else {
						data = wall_type.createBlockData();
					}
				}
				if (type.equals(Material.BLUE_WOOL)) {
					data = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(54));
				}
				if ((type.equals(Material.WARPED_FENCE) || type.equals(Material.CRIMSON_FENCE)) &&
					tud.getSchematic().getPermission().equals("delta")) {
					fractalBlocks.add(world.getBlockAt(x, y, z));
				}
				if (type.equals(Material.WHITE_STAINED_GLASS) && tud.getSchematic().getPermission().equals("war")) {
					data = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(47));
				}
				if (type.equals(Material.WHITE_TERRACOTTA) && tud.getSchematic().getPermission().equals("war")) {
					data = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(48));
				}
				if (type.equals(Material.LIGHT_GRAY_WOOL)) {
					data = floor_type.createBlockData();
				}
				if (type.equals(Material.SPAWNER)) { // scanner button
					/*
					 * mob spawner will be converted to the correct id by
					 * setBlock(), but remember it for the scanner.
					 */
					String scanner = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
					plugin.getQueryFactory().insertSyncControl(id, 33, scanner, 0);
				}
				if (type.equals(Material.CHEST)) {
					// remember the location of the condenser chest
					String condenser = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
					plugin.getQueryFactory().insertSyncControl(id, 34, condenser, 0);
				}
				if (type.equals(Material.IRON_DOOR)) {
					Bisected bisected = (Bisected) data;
					if (bisected.getHalf().equals(Bisected.Half.BOTTOM)) { // iron door bottom
						HashMap<String, Object> setd = new HashMap<>();
						String doorloc = world.getName() + ":" + x + ":" + y + ":" + z;
						setd.put("door_location", doorloc);
						HashMap<String, Object> whered = new HashMap<>();
						whered.put("tardis_id", id);
						whered.put("door_type", 1);
						plugin.getQueryFactory().doUpdate("doors", setd, whered);
						// if create_worlds is true, set the world spawn
						if (own_world) {
							world.setSpawnLocation(x, y, (z + 1));
						}
					}
				}
				if (type.equals(Material.STONE_BUTTON)) { // random button
					// remember the location of this button
					String button = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
					plugin.getQueryFactory().insertSyncControl(id, 1, button, 0);
				}
				if (type.equals(Material.JUKEBOX)) {
					// remember the location of this Advanced Console
					String advanced = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
					plugin.getQueryFactory().insertSyncControl(id, 15, advanced, 0);
					// set block data to correct MUSHROOM_STEM
					data = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(50));
				}
				if (type.equals(Material.CAKE)) {
					/*
					 * This block will be converted to a lever by setBlock(),
					 * but remember it so we can use it as the handbrake!
					 */
					String handbrakeloc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
					plugin.getQueryFactory().insertSyncControl(id, 0, handbrakeloc, 0);
				}
				if (type.equals(Material.REDSTONE_LAMP) || type.equals(Material.SEA_LANTERN)) {
					// remember lamp blocks
					Block lamp = world.getBlockAt(x, y, z);
					lampBlocks.add(lamp);
					// remember lamp block locations for malfunction and light switch
					HashMap<String, Object> setlb = new HashMap<>();
					String lloc = world.getName() + ":" + x + ":" + y + ":" + z;
					setlb.put("tardis_id", id);
					setlb.put("location", lloc);
					plugin.getQueryFactory().doInsert("lamps", setlb);
				}
				if (type.equals(Material.COMMAND_BLOCK) || ((tud.getSchematic().getPermission().equals("bigger") ||
															 tud.getSchematic().getPermission().equals("coral") ||
															 tud.getSchematic().getPermission().equals("deluxe") ||
															 tud.getSchematic().getPermission().equals("twelfth")) &&
															type.equals(Material.BEACON))) {
					/*
					 * command block - remember it to spawn the creeper on.
					 * could also be a beacon block, as the creeper sits over
					 * the beacon in the deluxe and bigger consoles.
					 */
					String creeploc = world.getName() + ":" + (x + 0.5) + ":" + y + ":" + (z + 0.5);
					set.put("creeper", creeploc);
					if (type.equals(Material.COMMAND_BLOCK)) {
						if (tud.getSchematic().getPermission().equals("ender")) {
							data = Material.END_STONE_BRICKS.createBlockData();
						} else if (tud.getSchematic().getPermission().equals("delta")) {
							data = Material.BLACKSTONE.createBlockData();
						} else {
							data = Material.STONE_BRICKS.createBlockData();
						}
					}
				}
				if (type.equals(Material.OAK_BUTTON)) {
					/*
					 * Remember it for the Artron Energy Capacitor.
					 */
					String woodbuttonloc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
					plugin.getQueryFactory().insertSyncControl(id, 6, woodbuttonloc, 0);
				}
				if (type.equals(Material.DAYLIGHT_DETECTOR)) {
					/*
					 * remember the telepathic circuit.
					 */
					String telepathicloc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
					plugin.getQueryFactory().insertSyncControl(id, 23, telepathicloc, 0);
				}
				if (type.equals(Material.BEACON) && tud.getSchematic().getPermission().equals("ender")) {
					/*
					 * get the ender crystal location
					 */
					ender = world.getBlockAt(x, y, z).getLocation().add(0.5d, 4d, 0.5d);
				}
				// if it's an iron/gold/diamond/emerald/beacon/redstone/bedrock/conduit/netherite block put it in the blocks table
				if (TARDISBuilderInstanceKeeper.getPrecious().contains(type)) {
					HashMap<String, Object> setpb = new HashMap<>();
					String loc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
					setpb.put("tardis_id", id);
					setpb.put("location", loc);
					setpb.put("data", "minecraft:air");
					setpb.put("police_box", 0);
					plugin.getQueryFactory().doInsert("blocks", setpb);
					plugin.getGeneralKeeper().getProtectBlockMap().put(loc, id);
				}
				// if it's the door, don't set it just remember its block then do it at the end
				if (type.equals(Material.HONEYCOMB_BLOCK) && (tud.getSchematic().getPermission().equals("delta") ||
															  tud.getSchematic().getPermission().equals("rotor"))) {
					/*
					 * spawn an item frame and place the time rotor in it
					 */
					TARDISBlockSetters.setBlock(world, x, y, z, (tud.getSchematic().getPermission().equals("delta")) ? Material.POLISHED_BLACKSTONE_BRICKS : Material.STONE_BRICKS);
					TARDISTimeRotor.setItemFrame(tud.getSchematic().getPermission(), new Location(world, x,
							y + 1, z), id);
				} else if (type.equals(Material.IRON_DOOR)) { // doors
					postDoorBlocks.put(world.getBlockAt(x, y, z), data);
				} else if (type.equals(Material.LEVER)) {
					postLeverBlocks.put(world.getBlockAt(x, y, z), data);
				} else if (type.equals(Material.REDSTONE_TORCH) || type.equals(Material.REDSTONE_WALL_TORCH)) {
					postRedstoneTorchBlocks.put(world.getBlockAt(x, y, z), data);
				} else if (type.equals(Material.TORCH) || type.equals(Material.WALL_TORCH) ||
						   type.equals(Material.SOUL_TORCH) || type.equals(Material.SOUL_WALL_TORCH)) {
					postTorchBlocks.put(world.getBlockAt(x, y, z), data);
				} else if (type.equals(Material.STICKY_PISTON)) {
					postStickyPistonBaseBlocks.put(world.getBlockAt(x, y, z), data);
				} else if (type.equals(Material.PISTON)) {
					postPistonBaseBlocks.put(world.getBlockAt(x, y, z), data);
				} else if (type.equals(Material.PISTON_HEAD)) {
					postPistonExtensionBlocks.put(world.getBlockAt(x, y, z), data);
				} else if (Tag.WALL_SIGNS.isTagged(type)) {
					postSignBlocks.put(world.getBlockAt(x, y, z), data);
				} else if (TARDISMaterials.infested.contains(type)) {
					// legacy monster egg stone for controls
					TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR);
				} else if (type.equals(Material.MUSHROOM_STEM)) { // mushroom stem for repeaters
					// save repeater location
					if (j < 6) {
						String repeater = world.getName() + ":" + x + ":" + y + ":" + z;
						data = Material.REPEATER.createBlockData();
						Directional directional = (Directional) data;
						switch (j) {
							case 2 -> {
								directional.setFacing(BlockFace.WEST);
								data = directional;
								postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
								plugin.getQueryFactory().insertSyncControl(id, 3, repeater, 0);
							}
							case 3 -> {
								directional.setFacing(BlockFace.NORTH);
								data = directional;
								postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
								plugin.getQueryFactory().insertSyncControl(id, 2, repeater, 0);
							}
							case 4 -> {
								directional.setFacing(BlockFace.SOUTH);
								data = directional;
								postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
								plugin.getQueryFactory().insertSyncControl(id, 5, repeater, 0);
							}
							default -> {
								directional.setFacing(BlockFace.EAST);
								data = directional;
								postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
								plugin.getQueryFactory().insertSyncControl(id, 4, repeater, 0);
							}
						}
						j++;
					}
				} else if (type.equals(Material.SPONGE)) {
					TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR);
				} else {
					BlockState state = world.getBlockAt(x, y, z).getState();
					plugin.getTardisHelper().removeTileEntity(state);
					TARDISBlockSetters.setBlock(world, x, y, z, data);
				}
			}
			// remove items
			chunks.forEach((chink) -> {
				// remove dropped items
				for (Entity e : chink.getEntities()) {
					if (e instanceof Item) {
						e.remove();
					}
				}
			});
			if (row < w) {
				row++;
			}
			if (row == w && level < h) {
				row = 0;
				level++;
			}
		}
	}

	private void setAir(int jx, int jy, int jz, World jw, int plusy) {
		for (int yy = jy; yy < (jy + plusy); yy++) {
			for (int xx = jx; xx < (jx + 16); xx++) {
				for (int zz = jz; zz < (jz + 16); zz++) {
					Block b = jw.getBlockAt(xx, yy, zz);
					b.setBlockData(TARDISConstants.AIR);
				}
			}
		}
	}

	private List<Chunk> getChunks(Chunk c, Schematic s) {
		List<Chunk> chinks = new ArrayList<>();
		chinks.add(c);
		if (s.getConsoleSize().equals(ConsoleSize.MASSIVE)) {
			chinks.add(c.getWorld().getChunkAt(c.getX() + 2, c.getZ()));
			chinks.add(c.getWorld().getChunkAt(c.getX(), c.getZ() + 2));
			chinks.add(c.getWorld().getChunkAt(c.getX() + 2, c.getZ() + 2));
			chinks.add(c.getWorld().getChunkAt(c.getX() + 1, c.getZ() + 2));
			chinks.add(c.getWorld().getChunkAt(c.getX() + 2, c.getZ() + 1));
		}
		if (!s.getConsoleSize().equals(ConsoleSize.SMALL)) {
			chinks.add(c.getWorld().getChunkAt(c.getX() + 1, c.getZ()));
			chinks.add(c.getWorld().getChunkAt(c.getX(), c.getZ() + 1));
			chinks.add(c.getWorld().getChunkAt(c.getX() + 1, c.getZ() + 1));
		}
		return chinks;
	}
}
