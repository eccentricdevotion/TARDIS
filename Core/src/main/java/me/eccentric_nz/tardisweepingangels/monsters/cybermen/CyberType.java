package me.eccentric_nz.tardisweepingangels.monsters.cybermen;

import me.eccentric_nz.TARDIS.custommodels.keys.ArmourVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.CybermanVariant;
import org.bukkit.NamespacedKey;

import java.util.HashMap;

public class CyberType {

    public static HashMap<NamespacedKey, NamespacedKey> CYBER_HEADS = new HashMap<>() {
        {
            put(ArmourVariant.CYBERMAN.getKey(), CybermanVariant.CYBERMAN_HEAD.getKey());
            put(ArmourVariant.BLACK_CYBERMAN.getKey(), CybermanVariant.BLACK_CYBERMAN_HEAD.getKey());
            put(ArmourVariant.CYBERMAN_EARTHSHOCK.getKey(), CybermanVariant.CYBERMAN_EARTHSHOCK_HEAD.getKey());
            put(ArmourVariant.CYBERMAN_INVASION.getKey(), CybermanVariant.CYBERMAN_INVASION_HEAD.getKey());
            put(ArmourVariant.CYBERMAN_MOONBASE.getKey(), CybermanVariant.CYBERMAN_MOONBASE_HEAD.getKey());
            put(ArmourVariant.CYBERMAN_RISE.getKey(), CybermanVariant.CYBERMAN_RISE_HEAD.getKey());
            put(ArmourVariant.CYBERMAN_TENTH_PLANET.getKey(), CybermanVariant.CYBERMAN_TENTH_PLANET_HEAD.getKey());
            put(ArmourVariant.CYBER_LORD.getKey(), CybermanVariant.CYBER_LORD_HEAD.getKey());
            put(ArmourVariant.WOOD_CYBERMAN.getKey(), CybermanVariant.WOOD_CYBERMAN_HEAD.getKey());
            put(ArmourVariant.CYBERSHADE.getKey(), CybermanVariant.CYBERSHADE_HEAD.getKey());
        }
    };
}
