/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.tardis.commands.admin;

import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.custommodeldata.TARDISSeedModel;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.Consoles;
import me.eccentric_nz.tardis.enumeration.RecipeCategory;
import me.eccentric_nz.tardis.enumeration.RecipeItem;
import me.eccentric_nz.tardis.messaging.TARDISGiveLister;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author eccentric_nz
 */
public class TARDISGiveCommand implements CommandExecutor {

	private final TARDIS plugin;
	private final int full;
	private final HashMap<String, String> items = new HashMap<>();

	public TARDISGiveCommand(TARDIS plugin) {
		this.plugin = plugin;
		full = this.plugin.getArtronConfig().getInt("full_charge");
		items.put("artron", "");
		items.put("blueprint", "");
		items.put("kit", "");
		items.put("mushroom", "");
		items.put("recipes", "");
		items.put("seed", "");
		items.put("tachyon", "");
		for (RecipeItem recipeItem : RecipeItem.values()) {
			if (recipeItem.getCategory() != RecipeCategory.SONIC_UPGRADES && recipeItem.getCategory() != RecipeCategory.UNUSED && recipeItem.getCategory() != RecipeCategory.UNCRAFTABLE) {
				items.put(recipeItem.toTabCompletionString(), recipeItem.toRecipeString());
			}
		}
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
		// If the player typed /tardisgive then do the following...
		if (cmd.getName().equalsIgnoreCase("tardisgive")) {
			if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
				if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
					new TARDISGiveLister(plugin, sender).list();
					return true;
				}
				if (args.length < 3) {
					TARDISMessage.send(sender, "TOO_FEW_ARGS");
					TARDISMessage.message(sender, "/tardisgive [player] [item] [amount]");
					return true;
				}
				String item = args[1].toLowerCase(Locale.ENGLISH);
				if (!items.containsKey(item)) {
					new TARDISGiveLister(plugin, sender).list();
					return true;
				}
				if (item.equals("kit")) {
					Player p = plugin.getServer().getPlayer(args[0]);
					if (p == null) { // player must be online
						TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
						return true;
					}
					if (!plugin.getKitsConfig().contains("kits." + args[2])) {
						TARDISMessage.send(sender, "ARG_KIT");
						return true;
					}
					plugin.getKitsConfig().getStringList("kits." + args[2]).forEach((k) -> giveItem(k, p));
					TARDISMessage.send(p, "GIVE_KIT", sender.getName(), args[2]);
					return true;
				}
				if (item.equals("blueprint")) {
					String blueprint = args[2].toUpperCase(Locale.ENGLISH);
					if (TARDISGiveTabComplete.getBlueprints().contains(blueprint)) {
						return giveBlueprint(sender, args, blueprint);
					} else {
						TARDISMessage.send(sender, "ARG_BLUEPRINT");
						return true;
					}
				}
				if (item.equals("recipes")) {
					if (args[2].equalsIgnoreCase("all")) {
						return grantRecipes(sender, args);
					} else {
						grantRecipe(sender, args);
					}
				}
				if (item.equals("seed")) {
					String seed = args[2].toUpperCase(Locale.ENGLISH);
					if (Consoles.getBY_NAMES().containsKey(seed) && !seed.equals("SMALL") && !seed.equals("MEDIUM") && !seed.equals("TALL") && !seed.equals("ARCHIVE")) {
						if (args.length > 3 && args[3].equalsIgnoreCase("knowledge")) {
							Player sp = plugin.getServer().getPlayer(args[0]);
							if (sp == null) { // player must be online
								TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
								return true;
							}
							return giveKnowledgeBook(sender, seed.toLowerCase() + "_seed", sp);
						} else {
							return giveSeed(sender, args);
						}
					} else {
						TARDISMessage.send(sender, "ARG_SEED");
						return true;
					}
				}
				if (item.equals("tachyon")) {
					return giveTachyon(sender, args[0], args[2]);
				}
				int amount;
				switch (args[2]) {
					case "full":
						amount = full;
						break;
					case "empty":
						amount = 0;
						break;
					case "knowledge":
						amount = 1;
						break;
					default:
						try {
							amount = Integer.parseInt(args[2]);
						} catch (NumberFormatException nfe) {
							TARDISMessage.send(sender, "ARG_GIVE");
							return true;
						}
						break;
				}
				if (item.equals("mushroom")) {
					if (args.length < 4) {
						TARDISMessage.send(sender, "TOO_FEW_ARGS");
						TARDISMessage.message(sender, "/tardisgive [player] mushroom [amount] [type]");
						return true;
					}
					Player p = plugin.getServer().getPlayer(args[0]);
					if (p == null) { // player must be online
						TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
						return true;
					}
					ItemStack mushroom = new TARDISMushroomCommand(plugin).getStack(args);
					p.getInventory().addItem(mushroom);
					p.updateInventory();
					TARDISMessage.send(p, "GIVE_ITEM", sender.getName(), amount + " " + args[3]);
					return true;
				}
				if (item.equals("artron")) {
					if (plugin.getServer().getOfflinePlayer(args[0]) == null) {
						TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
						return true;
					}
					return giveArtron(sender, args[0], amount);
				} else {
					Player player = null;
					if (args[0].equals("@s") && sender instanceof Player) {
						player = (Player) sender;
					} else if (args[0].equals("@p")) {
						List<Entity> near = Bukkit.selectEntities(sender, "@p");
						if (near.size() > 0 && near.get(0) instanceof Player) {
							player = (Player) near.get(0);
							if (player == null) {
								TARDISMessage.send(sender, "COULD_NOT_NEARBY_PLAYER");
								return true;
							}
						}
					} else {
						player = plugin.getServer().getPlayer(args[0]);
					}
					if (player == null) { // player must be online
						TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
						return true;
					}
					if (args[1].equalsIgnoreCase("cell") && args.length == 4 && args[3].equalsIgnoreCase("full")) {
						return giveFullCell(sender, amount, player);
					} else if (args[2].equals("knowledge")) {
						if (item.equalsIgnoreCase("all")) {
							return giveAllKnowledge(sender, player);
						} else {
							return giveKnowledgeBook(sender, item, player);
						}
					} else if (!args[2].endsWith("_seed")) {
						return giveItem(sender, item, amount, player);
					}
				}
			} else {
				TARDISMessage.send(sender, "NO_PERMS");
				return true;
			}
		}
		return false;
	}

	private boolean giveItem(CommandSender sender, String item, int amount, Player player) {
		if (amount > 64) {
			TARDISMessage.send(sender, "ARG_MAX");
			return true;
		}
		String item_to_give = items.get(item);
		ItemStack result;
		if (item.equals("save-storage-disk") || item.equals("preset-storage-disk") || item.equals("biome-storage-disk") || item.equals("player-storage-disk") || item.equals("bowl-of-custard") || item.equals("jelly-baby") || item.equals("schematic-wand")) {
			ShapelessRecipe recipe = plugin.getIncomposita().getShapelessRecipes().get(item_to_give);
			result = recipe.getResult();
		} else {
			ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get(item_to_give);
			result = recipe.getResult();
		}
		if (item.equals("invisible")) {
			// set the second line of lore
			ItemMeta im = result.getItemMeta();
			List<String> lore = im.getLore();
			String uses = (plugin.getConfig().getString("circuits.uses.invisibility").equals("0") || !plugin.getConfig().getBoolean("circuits.damage")) ? ChatColor.YELLOW + "unlimited" : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses.invisibility");
			lore.set(1, uses);
			im.setLore(lore);
			result.setItemMeta(im);
		}
		if (item.equals("blank") || item.equals("save-disk") || item.equals("preset-disk") || item.equals("biome-disk") || item.equals("player-disk") || item.equals("blaster") || item.equals("control")) {
			ItemMeta im = result.getItemMeta();
			im.addItemFlags(ItemFlag.values());
			result.setItemMeta(im);
		}
		if (item.equals("key") || item.equals("control")) {
			ItemMeta im = result.getItemMeta();
			im.getPersistentDataContainer().set(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID(), player.getUniqueId());
			List<String> lore = im.getLore();
			if (lore == null) {
				lore = new ArrayList<>();
			}
			String format = ChatColor.AQUA + "" + ChatColor.ITALIC;
			String what = item.equals("key") ? "key" : "disk";
			lore.add(format + "This " + what + " belongs to");
			lore.add(format + player.getName());
			im.setLore(lore);
			result.setItemMeta(im);
		}
		result.setAmount(amount);
		player.getInventory().addItem(result);
		player.updateInventory();
		TARDISMessage.send(player, "GIVE_ITEM", sender.getName(), amount + " " + item_to_give);
		return true;
	}

	private void giveItem(String item, Player player) {
		ItemStack result;
		if (plugin.getIncomposita().getShapelessRecipes().containsKey(item)) {
			ShapelessRecipe recipe = plugin.getIncomposita().getShapelessRecipes().get(item);
			result = recipe.getResult();
		} else {
			ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get(item);
			result = recipe.getResult();
			if (result.hasItemMeta()) {
				ItemMeta im = result.getItemMeta();
				if (im.hasDisplayName() && (im.getDisplayName().contains("Key") || im.getDisplayName().contains("Authorised Control Disk"))) {
					im.getPersistentDataContainer().set(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID(), player.getUniqueId());
					if (im.hasLore()) {
						List<String> lore = im.getLore();
						String format = ChatColor.AQUA + "" + ChatColor.ITALIC;
						String what = im.getDisplayName().contains("Key") ? "key" : "disk";
						lore.add(format + "This " + what + " belongs to");
						lore.add(format + player.getName());
						im.setLore(lore);
					}
					result.setItemMeta(im);
				}
			}
		}
		result.setAmount(1);
		player.getInventory().addItem(result);
		player.updateInventory();
	}

	private boolean giveArtron(CommandSender sender, String player, int amount) {
		// Look up this player's UUID
		UUID uuid = plugin.getServer().getOfflinePlayer(player).getUniqueId();
		if (uuid != null) {
			HashMap<String, Object> where = new HashMap<>();
			where.put("uuid", uuid.toString());
			ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
			if (rs.resultSet()) {
				Tardis tardis = rs.getTardis();
				int id = tardis.getTardis_id();
				int level = tardis.getArtron_level();
				int set_level;
				if (amount == 0) {
					set_level = 0;
				} else {
					// always fill to full and no more
					if (level >= full && amount > 0) {
						TARDISMessage.send(sender, "GIVE_FULL", player);
						return true;
					}
					if ((full - level) < amount) {
						set_level = full;
					} else {
						set_level = level + amount;
					}
				}
				HashMap<String, Object> set = new HashMap<>();
				set.put("artron_level", set_level);
				HashMap<String, Object> wheret = new HashMap<>();
				wheret.put("tardis_id", id);
				plugin.getQueryFactory().doUpdate("tardis", set, wheret);
				sender.sendMessage(plugin.getPluginName() + player + "'s Artron Energy Level was set to " + set_level);
			}
		} else {
			TARDISMessage.send(sender, "UUID_NOT_FOUND", player);
		}
		return true;
	}

	private boolean giveBlueprint(CommandSender sender, String[] args, String blueprint) {
		Player player = null;
		if (args[0].equals("@s") && sender instanceof Player) {
			player = (Player) sender;
		} else if (args[0].equals("@p")) {
			List<Entity> near = Bukkit.selectEntities(sender, "@p");
			if (near.size() > 0 && near.get(0) instanceof Player) {
				player = (Player) near.get(0);
				if (player == null) {
					TARDISMessage.send(sender, "COULD_NOT_NEARBY_PLAYER");
					return true;
				}
			}
		} else {
			player = plugin.getServer().getPlayer(args[0]);
			if (player == null) {
				TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
				return true;
			}
		}
		if (player != null) {
			ItemStack bp = plugin.getTardisAPI().getTARDISBlueprintItem(blueprint, player);
			player.getInventory().addItem(bp);
			player.updateInventory();
			TARDISMessage.send(player, "GIVE_ITEM", sender.getName(), "a tardis Blueprint Disk");
		}
		return true;
	}

	private boolean giveSeed(CommandSender sender, String[] args) {
		Player player = null;
		if (args[0].equals("@s") && sender instanceof Player) {
			player = (Player) sender;
		} else if (args[0].equals("@p")) {
			List<Entity> near = Bukkit.selectEntities(sender, "@p");
			if (near.size() > 0 && near.get(0) instanceof Player) {
				player = (Player) near.get(0);
				if (player == null) {
					TARDISMessage.send(sender, "COULD_NOT_NEARBY_PLAYER");
					return true;
				}
			}
		} else {
			player = plugin.getServer().getPlayer(args[0]);
			if (player == null) {
				TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
				return true;
			}
		}
		if (player != null) {
			String type = args[2].toUpperCase(Locale.ENGLISH);
			String wall = "ORANGE_WOOL";
			String floor = "LIGHT_GRAY_WOOL";
			if (args.length > 4) {
				try {
					wall = Material.valueOf(args[3].toUpperCase()).toString();
					floor = Material.valueOf(args[4].toUpperCase()).toString();
				} catch (IllegalArgumentException e) {
					TARDISMessage.send(sender, "SEED_MAT_NOT_VALID");
					return true;
				}
			}
			ItemStack is;
			if (Consoles.getBY_NAMES().containsKey(type)) {
				int model = TARDISSeedModel.modelByString(type);
				if (Consoles.getBY_NAMES().get(type).isCustom()) {
					is = new ItemStack(Material.MUSHROOM_STEM, 1);
				} else if (type.equalsIgnoreCase("DELTA") || type.equalsIgnoreCase("ROTOR") || type.equalsIgnoreCase("COPPER")) {
					is = new ItemStack(Material.MUSHROOM_STEM, 1);
				} else {
					is = new ItemStack(Material.RED_MUSHROOM_BLOCK, 1);
				}
				ItemMeta im = is.getItemMeta();
				im.setCustomModelData(10000000 + model);
				im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, model);
				// set display name
				im.setDisplayName(ChatColor.GOLD + "tardis Seed Block");
				List<String> lore = new ArrayList<>();
				lore.add(type);
				lore.add("Walls: " + wall);
				lore.add("Floors: " + floor);
				lore.add("Chameleon: FACTORY");
				im.setLore(lore);
				is.setItemMeta(im);
				player.getInventory().addItem(is);
				player.updateInventory();
				TARDISMessage.send(player, "GIVE_ITEM", sender.getName(), "a " + type + " seed block");
			}
		}
		return true;
	}

	private boolean giveTachyon(CommandSender sender, String player, String amount) {
		if (!plugin.getPM().isPluginEnabled("TARDISVortexManipulator")) {
			TARDISMessage.send(sender, "RECIPE_VORTEX");
			return true;
		}
		if (plugin.getServer().getOfflinePlayer(player) == null) {
			TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
			return true;
		}
		// Look up this player's UUID
		UUID uuid = plugin.getServer().getOfflinePlayer(player).getUniqueId();
		if (uuid != null) {
			plugin.getServer().dispatchCommand(sender, "vmg " + uuid + " " + amount);
		} else {
			TARDISMessage.send(sender, "UUID_NOT_FOUND", player);
		}
		return true;
	}

	private boolean giveFullCell(CommandSender sender, int amount, Player player) {
		if (amount > 64) {
			TARDISMessage.send(sender, "ARG_MAX");
			return true;
		}
		ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get("Artron Storage Cell");
		ItemStack result = recipe.getResult();
		result.setAmount(amount);
		// add lore and enchantment
		ItemMeta im = result.getItemMeta();
		List<String> lore = im.getLore();
		int max = plugin.getArtronConfig().getInt("full_charge");
		lore.set(1, "" + max);
		im.setLore(lore);
		im.addEnchant(Enchantment.DURABILITY, 1, true);
		im.addItemFlags(ItemFlag.values());
		result.setItemMeta(im);
		player.getInventory().addItem(result);
		player.updateInventory();
		TARDISMessage.send(player, "GIVE_ITEM", sender.getName(), amount + " Full Artron Storage Cell");
		return true;
	}

	private boolean giveAllKnowledge(CommandSender sender, Player player) {
		ItemStack book = new ItemStack(Material.KNOWLEDGE_BOOK, 1);
		KnowledgeBookMeta kbm = (KnowledgeBookMeta) book.getItemMeta();
		for (Map.Entry<String, String> map : items.entrySet()) {
			if (!map.getValue().isEmpty()) {
				switch (map.getKey()) {
					case "bow-tie" -> {
						List<String> colours = Arrays.asList("white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "grey", "light_grey", "cyan", "purple", "blue", "brown", "green", "red", "black");
						colours.forEach((bt) -> {
							NamespacedKey nsk = new NamespacedKey(plugin, bt + "_bow_tie");
							kbm.addRecipe(nsk);
						});
					}
					case "jelly-baby" -> {
						List<String> flavours = Arrays.asList("vanilla", "orange", "watermelon", "bubblegum", "lemon", "lime", "strawberry", "earl_grey", "vodka", "island_punch", "grape", "blueberry", "cappuccino", "apple", "raspberry", "licorice");
						flavours.forEach((jelly) -> {
							NamespacedKey nsk = new NamespacedKey(plugin, jelly + "_jelly_baby");
							kbm.addRecipe(nsk);
						});
					}
					default -> {
						NamespacedKey nsk = new NamespacedKey(plugin, map.getValue().replace(" ", "_").toLowerCase(Locale.ENGLISH));
						kbm.addRecipe(nsk);
					}
				}
			}
		}
		book.setItemMeta(kbm);
		player.getInventory().addItem(book);
		player.updateInventory();
		TARDISMessage.send(player, "GIVE_KNOWLEDGE", sender.getName(), "all tardis recipes");
		return true;
	}

	private boolean giveKnowledgeBook(CommandSender sender, String item, Player player) {
		String item_to_give = (item.endsWith("_seed")) ? item : items.get(item);
		ItemStack book = new ItemStack(Material.KNOWLEDGE_BOOK, 1);
		KnowledgeBookMeta kbm = (KnowledgeBookMeta) book.getItemMeta();
		String message = item_to_give;
		switch (item) {
			case "bow-tie" -> {
				List<String> colours = Arrays.asList("white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "grey", "light_grey", "cyan", "purple", "blue", "brown", "green", "red", "black");
				colours.forEach((bt) -> {
					NamespacedKey nsk = new NamespacedKey(plugin, bt + "_bow_tie");
					kbm.addRecipe(nsk);
				});
				message = "Bow Ties";
			}
			case "jelly-baby" -> {
				List<String> flavours = Arrays.asList("vanilla", "orange", "watermelon", "bubblegum", "lemon", "lime", "strawberry", "earl_grey", "vodka", "island_punch", "grape", "blueberry", "cappuccino", "apple", "raspberry", "licorice");
				flavours.forEach((jelly) -> {
					NamespacedKey nsk = new NamespacedKey(plugin, jelly + "_jelly_baby");
					kbm.addRecipe(nsk);
				});
				message = "Jelly Babies";
			}
			default -> {
				NamespacedKey nsk = new NamespacedKey(plugin, item_to_give.replace(" ", "_").toLowerCase(Locale.ENGLISH));
				kbm.addRecipe(nsk);
			}
		}
		book.setItemMeta(kbm);
		player.getInventory().addItem(book);
		player.updateInventory();
		TARDISMessage.send(player, "GIVE_KNOWLEDGE", sender.getName(), message);
		return true;
	}

	private boolean grantRecipes(CommandSender sender, String[] args) {
		Player player = plugin.getServer().getPlayer(args[0]);
		if (player == null) { // player must be online
			TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
			return true;
		}
		Set<NamespacedKey> keys = new HashSet<>();
		for (Map.Entry<String, String> map : items.entrySet()) {
			if (!map.getValue().isEmpty()) {
				switch (map.getKey()) {
					case "bow-tie" -> {
						List<String> colours = Arrays.asList("white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "grey", "light_grey", "cyan", "purple", "blue", "brown", "green", "red", "black");
						colours.forEach((bt) -> {
							NamespacedKey nsk = new NamespacedKey(plugin, bt + "_bow_tie");
							keys.add(nsk);
						});
					}
					case "jelly-baby" -> {
						List<String> flavours = Arrays.asList("vanilla", "orange", "watermelon", "bubblegum", "lemon", "lime", "strawberry", "earl_grey", "vodka", "island_punch", "grape", "blueberry", "cappuccino", "apple", "raspberry", "licorice");
						flavours.forEach((jelly) -> {
							NamespacedKey nsk = new NamespacedKey(plugin, jelly + "_jelly_baby");
							keys.add(nsk);
						});
					}
					default -> {
						NamespacedKey nsk = new NamespacedKey(plugin, map.getValue().replace(" ", "_").toLowerCase(Locale.ENGLISH));
						keys.add(nsk);
					}
				}
			}
		}
		player.discoverRecipes(keys);
		return true;
	}

	private void grantRecipe(CommandSender sender, String[] args) {
		Player player = plugin.getServer().getPlayer(args[0]);
		if (player == null) { // player must be online
			TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
			return;
		}
		String item = args[2].toLowerCase(Locale.ROOT);
		if (!items.containsKey(item)) {
			new TARDISGiveLister(plugin, sender).list();
			return;
		}
		Set<NamespacedKey> keys = new HashSet<>();
		switch (item) {
			case "bow-tie" -> {
				List<String> colours = Arrays.asList("white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "grey", "light_grey", "cyan", "purple", "blue", "brown", "green", "red", "black");
				colours.forEach((bt) -> {
					NamespacedKey nsk = new NamespacedKey(plugin, bt + "_bow_tie");
					keys.add(nsk);
				});
			}
			case "jelly-baby" -> {
				List<String> flavours = Arrays.asList("vanilla", "orange", "watermelon", "bubblegum", "lemon", "lime", "strawberry", "earl_grey", "vodka", "island_punch", "grape", "blueberry", "cappuccino", "apple", "raspberry", "licorice");
				flavours.forEach((jelly) -> {
					NamespacedKey nsk = new NamespacedKey(plugin, jelly + "_jelly_baby");
					keys.add(nsk);
				});
			}
			default -> {
				NamespacedKey nsk = new NamespacedKey(plugin, items.get(item).replace(" ", "_").toLowerCase(Locale.ENGLISH));
				keys.add(nsk);
			}
		}
		player.discoverRecipes(keys);
	}
}
