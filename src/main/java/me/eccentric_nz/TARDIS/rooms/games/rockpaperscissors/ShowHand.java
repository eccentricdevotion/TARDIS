package me.eccentric_nz.TARDIS.rooms.games.rockpaperscissors;

import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShowHand {

    private final Material MAGMA = Material.MAGMA_BLOCK;
    private final Material ICE = Material.ICE;
    private final Material STONE = Material.STONE;
    private final List<ItemStack> choices = List.of(
        ItemStack.of(STONE),
        ItemStack.of(MAGMA),
        ItemStack.of(ICE)
    );

    public ItemStack getTARDISChoice() {
        return choices.get(TARDISConstants.RANDOM.nextInt(3));
    }

    public Sound revealResults(InventoryView view) {
        Material playerBlock = view.getItem(3).getType();
        Material agentBlock = view.getItem(5).getType();
        StoneMagmaIceState state;
        if (playerBlock == agentBlock) {
            state = StoneMagmaIceState.DRAW;
        } else if (playerBlock == STONE) {
            if (agentBlock == MAGMA) {
                state = StoneMagmaIceState.LOSE;
            } else {
                state = StoneMagmaIceState.WIN;
            }
        } else if (playerBlock == ICE) {
            if (agentBlock == STONE) {
                state = StoneMagmaIceState.LOSE;
            } else {
                state = StoneMagmaIceState.WIN;
            }
        } else {
            if (agentBlock == ICE) {
                state = StoneMagmaIceState.LOSE;
            } else {
                state = StoneMagmaIceState.WIN;
            }
        }
        setBannerSlots(state, view);
        return state.getSound();
    }

    private void setBannerSlots(StoneMagmaIceState state, InventoryView view) {
        for (int i = 18; i < 27; i++) {
            view.setItem(i, state.getBanners()[i - 18]);
        }
    }
}
