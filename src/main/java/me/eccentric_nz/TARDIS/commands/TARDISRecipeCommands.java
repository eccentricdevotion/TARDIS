/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.messaging.TARDISRecipeLister;
import me.eccentric_nz.TARDIS.recipes.TARDISRecipeCategoryInventory;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

/**
 * A Time Control Unit is a golden sphere about the size of a Cricket ball. It is stored in the Secondary Control Room.
 * All TARDISes have one of these devices, which can be used to remotely control a TARDIS by broadcasting Stattenheim
 * signals that travel along the time contours in the Space/Time Vortex.
 *
 * @author eccentric_nz
 */
public class TARDISRecipeCommands implements CommandExecutor {

    private final TARDIS plugin;
    private final HashMap<String, String> recipeItems = new HashMap<>();
    private final HashMap<String, Material> t = new HashMap<>();
    private final Set<String> CHEM_SUBS = new HashSet<>();

    public TARDISRecipeCommands(TARDIS plugin) {
        this.plugin = plugin;
        recipeItems.put("seed", "");
        recipeItems.put("tardis", "");
        for (RecipeItem recipeItem : RecipeItem.values()) {
            if (recipeItem.getCategory() != RecipeCategory.UNCRAFTABLE && recipeItem.getCategory() != RecipeCategory.UNUSED) {
                recipeItems.put(recipeItem.toTabCompletionString(), recipeItem.toRecipeString());
            }
        }
        // custom time rotors
        for (String r : TARDIS.plugin.getCustomRotorsConfig().getKeys(false)) {
            recipeItems.put("time-rotor-" + r.toLowerCase(), "Time Rotor " + TARDISStringUtils.capitalise(r));
        }
        // custom doors
        for (String r : TARDIS.plugin.getCustomDoorsConfig().getKeys(false)) {
            recipeItems.put("door-" + r.toLowerCase(), "Door " + TARDISStringUtils.capitalise(r));
        }
        // remove recipes form modules that are not enabled
        if (!plugin.getConfig().getBoolean("modules.vortex_manipulator")) {
            recipeItems.remove("vortex-manipulator");
        }
        if (!plugin.getConfig().getBoolean("modules.sonic_blaster")) {
            recipeItems.remove("sonic-blaster");
            recipeItems.remove("blaster-battery");
            recipeItems.remove("landing-pad");
        }
        if (!plugin.getConfig().getBoolean("modules.weeping_angels")) {
            recipeItems.remove("judoon-ammunition");
            recipeItems.remove("k9");
        }
        // DELUXE, ELEVENTH, TWELFTH, ARS & REDSTONE schematics designed by Lord_Rahl and killeratnight at mcnovus.net
        t.put("ANCIENT", Material.SCULK); // ancient
        t.put("ARS", Material.QUARTZ_BLOCK); // ARS
        t.put("BIGGER", Material.GOLD_BLOCK); // bigger
        t.put("BUDGET", Material.IRON_BLOCK); // budget
        t.put("CAVE", Material.DRIPSTONE_BLOCK); // dripstone cave
        t.put("COPPER", Material.WARPED_PLANKS); // copper schematic designed by vistaero
        t.put("CORAL", Material.NETHER_WART_BLOCK); // coral schematic designed by vistaero
        t.put("CURSED", Material.BLACK_CONCRETE); // designed by airomis (player at thatsnotacreeper.com)
        t.put("DELTA", Material.CRYING_OBSIDIAN); // delta
        t.put("DELUXE", Material.DIAMOND_BLOCK); // deluxe
        t.put("DIVISION", Material.PINK_GLAZED_TERRACOTTA); // division
        t.put("ELEVENTH", Material.EMERALD_BLOCK); // eleventh
        t.put("ENDER", Material.PURPUR_BLOCK); // ender schematic designed by ToppanaFIN (player at thatsnotacreeper.com)
        t.put("FACTORY", Material.YELLOW_CONCRETE_POWDER); // factory schematic designed by Razihel
        t.put("FIFTEENTH", Material.OCHRE_FROGLIGHT); // designed by airomis (player at thatsnotacreeper.com)
        t.put("FUGITIVE", Material.POLISHED_DEEPSLATE); // fugitive - based on TARDIS designed by DT10 - https://www.youtube.com/watch?v=aykwXVemSs8
        t.put("HOSPITAL", Material.WHITE_CONCRETE); // hospital
        t.put("MASTER", Material.NETHER_BRICKS); // master schematic designed by ShadowAssociate
        t.put("MECHANICAL", Material.POLISHED_ANDESITE); // mechanical schematic adapted from design by Plastic Straw
        t.put("ORIGINAL", Material.PACKED_MUD); // original
        t.put("PLANK", Material.BOOKSHELF); // plank
        t.put("PYRAMID", Material.SANDSTONE_STAIRS); // pyramid schematic designed by airomis (player at thatsnotacreeper.com)
        t.put("REDSTONE", Material.REDSTONE_BLOCK); // redstone
        t.put("ROTOR", Material.HONEYCOMB_BLOCK); // rotor
        t.put("STEAMPUNK", Material.COAL_BLOCK); // steampunk
        t.put("THIRTEENTH", Material.ORANGE_CONCRETE); // thirteenth designed by Razihel
        t.put("TOM", Material.LAPIS_BLOCK); // tom baker
        t.put("TWELFTH", Material.PRISMARINE); // twelfth
        t.put("WAR", Material.WHITE_TERRACOTTA); // war doctor
        t.put("WEATHERED", Material.WEATHERED_COPPER); // weathered
        t.put("LEGACY_BIGGER", Material.ORANGE_GLAZED_TERRACOTTA);
        t.put("LEGACY_DELUXE", Material.LIME_GLAZED_TERRACOTTA);
        t.put("LEGACY_ELEVENTH", Material.CYAN_GLAZED_TERRACOTTA);
        t.put("LEGACY_REDSTONE", Material.RED_GLAZED_TERRACOTTA);
        // custom seeds
        plugin.getCustomConsolesConfig().getKeys(false).forEach((console) -> {
            if (plugin.getCustomConsolesConfig().getBoolean(console + ".enabled")) {
                Material cmat = Material.valueOf(plugin.getCustomConsolesConfig().getString(console + ".seed"));
                t.put(console.toUpperCase(Locale.ENGLISH), cmat);
            }
        });
        for (RecipeItem recipeItem : RecipeItem.values()) {
            if (recipeItem.getCategory() == RecipeCategory.CHEMISTRY) {
                CHEM_SUBS.add(recipeItem.toTabCompletionString());
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisrecipe")) {
            if (!TARDISPermission.hasPermission(sender, "tardis.help")) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "NO_PERMS");
                return true;
            }
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                if (args.length == 0) {
                    new TARDISRecipeLister(plugin, sender).list();
                } else {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_PLAYER");
                }
                return true;
            }
            if (args.length == 0) {
                // open recipe GUI
                ItemStack[] emenu = new TARDISRecipeCategoryInventory().getMenu();
                Inventory categories = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Recipe Categories");
                categories.setContents(emenu);
                player.openInventory(categories);
                return true;
            }
            String which = args[0].toLowerCase();
            if (CHEM_SUBS.contains(which)) {
                // use `/tchemistry formula command`
                String command = TARDISStringUtils.chemistryCase(which);
                plugin.getMessenger().sendColouredCommand(player, "USE_FORMULA", "/tchemistry formula " + command, plugin);
                return true;
            }
            if (!recipeItems.containsKey(which)) {
                if (args[0].equalsIgnoreCase("list_more")) {
                    new TARDISRecipeLister(plugin, sender).listMore();
                } else {
                    new TARDISRecipeLister(plugin, sender).list();
                }
                return true;
            }
            if ((args[0].equalsIgnoreCase("seed") || args[0].equalsIgnoreCase("tardis")) && args.length < 2) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                return true;
            }
            if ((args[0].equalsIgnoreCase("seed") || args[0].equalsIgnoreCase("tardis")) && args.length == 2) {
                if (!t.containsKey(args[1].toUpperCase(Locale.ENGLISH))) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_NOT_VALID");
                    return true;
                }
                showTARDISRecipe(player, args[1].toUpperCase(Locale.ENGLISH));
                return true;
            }
            switch (which) {
                case "bowl-of-custard", "jelly-baby", "biome-storage-disk", "player-storage-disk", "preset-storage-disk", "save-storage-disk", "schematic-wand", "admin-upgrade", "bio-scanner-upgrade", "redstone-upgrade", "diamond-upgrade", "emerald-upgrade", "painter-upgrade", "ignite-upgrade", "pickup-arrows-upgrade", "knockback-upgrade", "brush-upgrade", "judoon-ammunition" -> {
                    showShapelessRecipe(player, recipeItems.get(which));
                    return true;
                }
                default -> {
                    showShapedRecipe(player, recipeItems.get(which));
                    return true;
                }
            }
        }
        return false;
    }

    // Maps still seem to use numeric values
    private void showShapedRecipe(Player player, String str) {
        ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get(str);
        player.discoverRecipe(recipe.getKey());
        player.closeInventory();
        plugin.getTrackerKeeper().getRecipeViewers().add(player.getUniqueId());
        Inventory inv = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + str + " recipe");
        String[] recipeShape = recipe.getShape();
        Map<Character, ItemStack> ingredientMap = recipe.getIngredientMap();
        int glowstoneCount = 0;
        for (int j = 0; j < recipeShape.length; j++) {
            for (int k = 0; k < recipeShape[j].length(); k++) {
                ItemStack item = ingredientMap.get(recipeShape[j].toCharArray()[k]);
                if (item == null) {
                    continue;
                }
                ItemMeta im = item.getItemMeta();
                if (item.getType().equals(Material.GLOWSTONE_DUST)) {
                    String dn = getDisplayName(str, glowstoneCount);
                    im.setDisplayName(dn);
                    im.setCustomModelData(RecipeItem.getByName(dn).getCustomModelData());
                    glowstoneCount++;
                }
                if (str.endsWith("TARDIS Remote Key") && item.getType().equals(Material.GOLD_NUGGET)) {
                    im.setDisplayName("TARDIS Key");
                    im.setCustomModelData(1);
                }
                if (str.equals("Acid Battery") && item.getType().equals(Material.WATER_BUCKET)) {
                    im.setDisplayName("Acid Bucket");
                    im.setCustomModelData(1);
                }
                if (str.equals("Rift Manipulator") && item.getType().equals(Material.NETHER_BRICK)) {
                    im.setDisplayName("Acid Battery");
                    im.setCustomModelData(10000001);
                }
                if (str.equals("Rust Plague Sword") && item.getType().equals(Material.LAVA_BUCKET)) {
                    im.setDisplayName("Rust Bucket");
                    im.setCustomModelData(1);
                }
                item.setItemMeta(im);
                inv.setItem(j * 9 + k, item);
            }
        }
        ItemStack result = recipe.getResult();
        ItemMeta im = result.getItemMeta();
        im.setDisplayName(str);
        RecipeItem recipeItem = RecipeItem.getByName(str);
        if (recipeItem != RecipeItem.NOT_FOUND) {
            im.setCustomModelData(recipeItem.getCustomModelData());
        }
        if (str.equals("TARDIS Invisibility Circuit")) {
            // set the second line of lore
            List<String> lore = im.getLore();
            String uses = (plugin.getConfig().getString("circuits.uses.invisibility").equals("0") || !plugin.getConfig().getBoolean("circuits.damage")) ? ChatColor.YELLOW + "unlimited" : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses.invisibility");
            lore.set(1, uses);
            im.setLore(lore);
        }
        if (str.equals("Blank Storage Disk") || str.equals("Save Storage Disk") || str.equals("Preset Storage Disk") || str.equals("Biome Storage Disk") || str.equals("Player Storage Disk") || str.equals("Authorised Control Disk")) {
            im.addItemFlags(ItemFlag.values());
        }
        result.setAmount(1);
        result.setItemMeta(im);
        inv.setItem(17, result);
        player.openInventory(inv);
    }

    private void showShapelessRecipe(Player player, String str) {
        ShapelessRecipe recipe = plugin.getIncomposita().getShapelessRecipes().get(str);
        player.discoverRecipe(recipe.getKey());
        List<ItemStack> ingredients = recipe.getIngredientList();
        plugin.getTrackerKeeper().getRecipeViewers().add(player.getUniqueId());
        Inventory inv = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + str + " recipe");
        int glowstoneCount = 0;
        for (int i = 0; i < ingredients.size(); i++) {
            ItemMeta im = ingredients.get(i).getItemMeta();
            if (ingredients.get(i).getType().equals(Material.GLOWSTONE_DUST)) {
                String dn = getDisplayName(str, glowstoneCount);
                im.setDisplayName(dn);
                im.setCustomModelData(RecipeItem.getByName(dn).getCustomModelData());
                glowstoneCount++;
            }
            if (ingredients.get(i).getType().equals(Material.MUSIC_DISC_STRAD)) {
                im.setDisplayName("Blank Storage Disk");
                im.setCustomModelData(10000001);
                im.addItemFlags(ItemFlag.values());
            }
            if (ingredients.get(i).getType().equals(Material.BLAZE_ROD)) {
                im.setDisplayName("Sonic Screwdriver");
                im.setCustomModelData(10000010);
            }
            ingredients.get(i).setItemMeta(im);
            inv.setItem(i * 9, ingredients.get(i));
        }
        ItemStack result = recipe.getResult();
        ItemMeta im = result.getItemMeta();
        im.setDisplayName(str);
        if (str.equals("Blank Storage Disk") || str.equals("Save Storage Disk") || str.equals("Preset Storage Disk") || str.equals("Biome Storage Disk") || str.equals("Player Storage Disk") || str.equals("Authorised Control Disk")) {
            im.addItemFlags(ItemFlag.values());
        }
        RecipeItem recipeItem = RecipeItem.getByName(str);
        if (recipeItem != RecipeItem.NOT_FOUND) {
            im.setCustomModelData(recipeItem.getCustomModelData());
            if (recipeItem.getCategory().equals(RecipeCategory.SONIC_UPGRADES)) {
                im.setDisplayName("Sonic Screwdriver");
                im.setLore(Arrays.asList("Upgrades:", str));
            }
        }
        result.setAmount(1);
        result.setItemMeta(im);
        inv.setItem(17, result);
        player.openInventory(inv);
    }

    private void showTARDISRecipe(Player player, String type) {
        plugin.getTrackerKeeper().getRecipeViewers().add(player.getUniqueId());
        Inventory inv = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "TARDIS " + type + " seed recipe");
        // redstone torch
        ItemStack red = new ItemStack(Material.REDSTONE_TORCH, 1);
        // lapis block
        ItemStack lapis = new ItemStack(Material.LAPIS_BLOCK, 1);
        // interior wall
        ItemStack in_wall = new ItemStack(Material.ORANGE_WOOL, 1);
        ItemMeta in_meta = in_wall.getItemMeta();
        in_meta.setDisplayName("Interior walls");
        in_meta.setLore(List.of("Any valid Wall/Floor block"));
        in_wall.setItemMeta(in_meta);
        // interior floor
        ItemStack in_floor = new ItemStack(Material.LIGHT_GRAY_WOOL, 1);
        ItemMeta fl_meta = in_floor.getItemMeta();
        fl_meta.setDisplayName("Interior floors");
        fl_meta.setLore(List.of("Any valid Wall/Floor block"));
        in_floor.setItemMeta(fl_meta);
        // seed block
        ItemStack block = new ItemStack(t.get(type), 1);
        // tardis type
        Schematic schm = Consoles.getBY_NAMES().get(type);
        ItemStack tardis;
        int model = 10001;
        if (schm.isCustom()) {
            tardis = new ItemStack(schm.getSeedMaterial(), 1);
        } else {
            try {
                TARDISDisplayItem tdi = TARDISDisplayItem.valueOf(type);
                tardis = new ItemStack(tdi.getMaterial(), 1);
                model = tdi.getCustomModelData();
            } catch (IllegalArgumentException e) {
                tardis = new ItemStack(TARDISDisplayItem.CUSTOM.getMaterial(), 1);
            }
        }
        ItemMeta seed = tardis.getItemMeta();
        seed.setCustomModelData(model);
        seed.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, model);
        // set display name
        seed.setDisplayName(ChatColor.GOLD + "TARDIS Seed Block");
        List<String> lore = new ArrayList<>();
        lore.add(type);
        lore.add("Walls: ORANGE_WOOL");
        lore.add("Floors: LIGHT_GRAY_WOOL");
        lore.add("Chameleon: FACTORY");
        seed.setLore(lore);
        tardis.setItemMeta(seed);
        inv.setItem(0, red);
        inv.setItem(9, lapis);
        inv.setItem(11, in_wall);
        inv.setItem(17, tardis);
        inv.setItem(18, block);
        inv.setItem(20, in_floor);
        player.openInventory(inv);
    }

    private String getDisplayName(String recipe, int quartzCount) {
        switch (recipe) {
            case "TARDIS Locator" -> {
                return "TARDIS Locator Circuit"; // 1965
            }
            case "Stattenheim Remote" -> {
                return "TARDIS Stattenheim Circuit"; // 1963
            }
            case "TARDIS Chameleon Circuit", "TARDIS Remote Key" -> {
                return "TARDIS Materialisation Circuit"; // 1964
            }
            case "TARDIS Invisibility Circuit", "Perception Filter" -> {
                return "Perception Circuit"; // 1978
            }
            case "Sonic Screwdriver", "Server Admin Circuit" -> {
                return "Sonic Oscillator"; // 1967
            }
            case "Fob Watch" -> {
                return "TARDIS Chameleon Circuit"; // 1966
            }
            case "TARDIS Biome Reader", "Emerald Upgrade" -> {
                return "Emerald Environment Circuit"; // 1972
            }
            case "Rift Manipulator" -> {
                return "Rift Circuit"; // 1983
            }
            case "Admin Upgrade" -> {
                return "Server Admin Circuit";
            }
            case "Bio-scanner Upgrade" -> {
                return "Bio-scanner Circuit";
            }
            case "Redstone Upgrade" -> {
                return "Redstone Activator Circuit";
            }
            case "Diamond Upgrade" -> {
                return "Diamond Disruptor Circuit";
            }
            case "Painter Upgrade" -> {
                return "Painter Circuit";
            }
            case "Ignite Upgrade" -> {
                return "Ignite Circuit";
            }
            case "Pickup Arrows Upgrade" -> {
                return "Pickup Arrows Circuit";
            }
            case "Knockback Upgrade" -> {
                return "Knockback Circuit";
            }
            case "Brush Upgrade" -> {
                return "Brush Circuit";
            }
            case "Conversion Upgrade" -> {
                return "Conversion Circuit";
            }
            default -> {  // TARDIS Stattenheim Circuit"
                if (quartzCount == 0) {
                    return "TARDIS Locator Circuit"; // 1965
                } else {
                    return "TARDIS Materialisation Circuit"; // 1964
                }
            }
        }
    }
}
