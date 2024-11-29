package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.ARS.TARDISARS;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.BlueprintConsole;
import me.eccentric_nz.TARDIS.blueprints.BlueprintRoom;
import me.eccentric_nz.TARDIS.custommodeldata.keys.DiskVariant;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GallifreyBlueprintTrade {

    private final TARDIS plugin;
    private final int uses;

    public GallifreyBlueprintTrade(TARDIS plugin) {
        this.plugin = plugin;
        // minecraft limits max uses to 16 - see https://minecraft.wiki/w/Trading#Java_Edition_offers
        this.uses = Math.min(this.plugin.getPlanetsConfig().getInt("planets.gallifrey.villager_blueprints.uses"), 16);
    }

    public void setTrades(Merchant villager) {
        List<MerchantRecipe> recipes = getRandomRecipes();
        villager.setRecipes(recipes);
    }

    public MerchantRecipe getRoom() {
        // room blueprint index 0 and 1 are not rooms, and the last index is the zero room which can't be grown with ARS
        BlueprintRoom bpr = BlueprintRoom.values()[TARDISConstants.RANDOM.nextInt(2, BlueprintRoom.values().length - 1)];
        // get the blueprint item stack
        ItemStack ris = buildResult(bpr.getPermission(), bpr.toString());
        // single use?
        MerchantRecipe roomRecipe = new MerchantRecipe(ris, uses);
        // get the room material for the ingredient from the blueprint
        Material roomMaterial = Material.valueOf(TARDISARS.valueOf(bpr.toString()).getMaterial());
        // determine the stack size of the ingredient
        int roomAmount = plugin.getRoomsConfig().getInt("rooms." + bpr.toString() + ".cost") / 20;
        roomRecipe.addIngredient(new ItemStack(roomMaterial, roomAmount));
        return roomRecipe;
    }

    public MerchantRecipe getConsole() {
        // don't include the custom console
        BlueprintConsole bpc = BlueprintConsole.values()[TARDISConstants.RANDOM.nextInt(BlueprintConsole.values().length - 1)];
        // get the blueprint item stack
        ItemStack cis = buildResult(bpc.getPermission(), bpc.toString());
        // single use?
        MerchantRecipe consoleRecipe = new MerchantRecipe(cis, uses);
        // get the console material for the ingredient from the blueprint
        String perm = bpc.getPermission().split("\\.")[1];
        Material consoleMaterial = Consoles.getBY_PERMS().get(perm).getSeedMaterial();
        // determine the stack size of the ingredient
        int consoleAmount = plugin.getArtronConfig().getInt("upgrades." + perm) / 250;
        // add the ingredient
        consoleRecipe.addIngredient(new ItemStack(consoleMaterial, consoleAmount));
        return consoleRecipe;
    }

    private List<MerchantRecipe> getRandomRecipes() {
        MerchantRecipe roomRecipe = getRoom();
        MerchantRecipe consoleRecipe = getConsole();
        return List.of(roomRecipe, consoleRecipe);
    }

    private ItemStack buildResult(String perm, String name) {
        ItemStack is = new ItemStack(Material.MUSIC_DISC_MELLOHI, 1);
        ItemMeta im = is.getItemMeta();
        im.setItemModel(DiskVariant.BLUEPRINT_DISK.getKey());
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        pdc.set(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID(), UUID.randomUUID());
        pdc.set(plugin.getBlueprintKey(), PersistentDataType.STRING, perm);
        im.setDisplayName("TARDIS Blueprint Disk");
        List<String> lore = Arrays.asList(TARDISStringUtils.capitalise(name), "Valid only for", "the trading player");
        im.setLore(lore);
        im.addItemFlags(ItemFlag.values());
        is.setItemMeta(im);
        return is;
    }
}
