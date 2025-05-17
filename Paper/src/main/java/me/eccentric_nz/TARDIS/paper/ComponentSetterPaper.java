package me.eccentric_nz.TARDIS.paper;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.Weapon;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import io.papermc.paper.registry.keys.SoundEventKeys;
import me.eccentric_nz.tardisregeneration.ComponentSetter;
import org.bukkit.inventory.ItemStack;

public class ComponentSetterPaper implements ComponentSetter {
    @Override
    public ItemStack setConsumable(ItemStack is) {

        Consumable consumable = Consumable.consumable()
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEventKeys.ENTITY_GENERIC_DRINK)
                .consumeSeconds(1.6f)
                .hasConsumeParticles(false)
                .build();
        is.setData(DataComponentTypes.CONSUMABLE, consumable);
        return is;
    }

    @Override
    public void setWeapon(ItemStack is) {
        Weapon weapon = Weapon.weapon()
                .itemDamagePerAttack(8)
                .build();
        is.setData(DataComponentTypes.WEAPON, weapon);
    }
}
