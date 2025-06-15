package me.eccentric_nz.TARDIS.paper;

import me.eccentric_nz.TARDIS.planets.BlueprintMerchantView;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.view.MerchantView;

public class PaperMerchantView implements BlueprintMerchantView {

    public MerchantView getView(Villager villager, Player player) {
        MerchantView merchant = MenuType.MERCHANT.builder()
                .merchant(villager)
                .checkReachable(true)
                .title(villager.customName())
                .build(player);
        return merchant;
    }
}
