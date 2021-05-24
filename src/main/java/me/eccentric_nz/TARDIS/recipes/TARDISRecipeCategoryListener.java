package me.eccentric_nz.tardis.recipes;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.enumeration.RecipeCategory;
import me.eccentric_nz.tardis.listeners.TARDISMenuListener;
import me.eccentric_nz.tardis.utility.TARDISStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISRecipeCategoryListener extends TARDISMenuListener implements Listener {

	private final TARDISPlugin plugin;

	public TARDISRecipeCategoryListener(TARDISPlugin plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true)
	public void onRecipeCategoryClick(InventoryClickEvent event) {
		InventoryView view = event.getView();
		String name = view.getTitle();
		if (name.equals(ChatColor.DARK_RED + "Recipe Categories")) {
			event.setCancelled(true);
			int slot = event.getRawSlot();
			Player player = (Player) event.getWhoClicked();
			if (slot >= 9 && slot < 27) {
				ItemStack is = view.getItem(slot);
				if (is != null) {
					ItemMeta im = is.getItemMeta();
					String cat = TARDISStringUtils.toEnumUppercase(im.getDisplayName());
					RecipeCategory category = RecipeCategory.valueOf(cat);
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
						ItemStack[] items = new TARDISRecipeInventory(plugin, category).getMenu();
						Inventory recipes = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "tardis Recipes");
						recipes.setContents(items);
						player.openInventory(recipes);
					}, 2L);
				}
			}
		}
	}
}
