package me.eccentric_nz.TARDIS.planets;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.view.MerchantView;

public interface BlueprintMerchantView {

    MerchantView getView(Villager villager, Player player);
}
