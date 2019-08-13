package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TARDISDisguiseCommand {

    private final TARDIS plugin;
    private int task;
    private int i;

    public TARDISDisguiseCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean disguise(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase("disguise")) {
            Player player = null;
            if (args.length == 3) {
                player = plugin.getServer().getPlayer(args[2]);
            } else if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                TARDISMessage.message(sender, "You need to specify a player!");
                return true;
            }
            if (args[1].equalsIgnoreCase("test")) {
                List<Pair> list = new ArrayList<>();
//                list.add(new Pair(EntityType.PLAYER, UUID.fromString("01aadb99-32ea-4e5b-a1df-50b46d3f82e3")));
                list.add(new Pair(EntityType.SNOWMAN, true));
//                list.add(new Pair(EntityType.LLAMA, CARPET.CYAN));
//        list.add(new Pair(EntityType.LLAMA, Llama.Color.GRAY));
//                list.add(new Pair(EntityType.VILLAGER, AGE.BABY));
//                list.add(new Pair(EntityType.DONKEY, true));
//                list.add(new Pair(EntityType.MAGMA_CUBE, 1));
//        list.add(new Pair(EntityType.SLIME, 4));
//                list.add(new Pair(EntityType.MUSHROOM_COW, MUSHROOM_COW.BROWN));
//                list.add(new Pair(EntityType.PANDA, GENE.PLAYFUL));
//        list.add(new Pair(EntityType.PANDA, GENE.BROWN));
//        list.add(new Pair(EntityType.PANDA, GENE.WEAK));
//        list.add(new Pair(EntityType.PANDA, GENE.AGGRESSIVE));
//                list.add(new Pair(EntityType.PARROT, Parrot.Variant.GREEN));
//                list.add(new Pair(EntityType.RABBIT, Rabbit.Type.BLACK));
//                list.add(new Pair(EntityType.VILLAGER, PROFESSION.FISHERMAN));
//                list.add(new Pair(EntityType.ZOMBIE_VILLAGER, PROFESSION.ARMORER));
//        list.add(new Pair(EntityType.ZOMBIE_VILLAGER, PROFESSION.CARTOGRAPHER));
//        list.add(new Pair(EntityType.ZOMBIE_VILLAGER, PROFESSION.CLERIC));
//                list.add(new Pair(EntityType.ENDERMAN, true));
//        list.add(new Pair(EntityType.ENDERMAN, false));
//                list.add(new Pair(EntityType.WOLF, DyeColor.ORANGE));
//                list.add(new Pair(EntityType.PUFFERFISH, 2));
//                list.add(new Pair(EntityType.TROPICAL_FISH, TropicalFish.Pattern.SPOTTY));
//                list.add(new Pair(EntityType.TROPICAL_FISH, TropicalFish.Pattern.DASHER));
//               list.add(new Pair(EntityType.TROPICAL_FISH, TropicalFish.Pattern.STRIPEY));
//                list.add(new Pair(EntityType.PIG, true));
//                list.add(new Pair(EntityType.CREEPER, true));
//        list.add(new Pair(EntityType.BAT, true));
                i = 0;
                Player p = player;
                Object[] options = new Object[1];
                task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
//                    plugin.debug(i + ". " + list.get(i).entityType.toString() + ", " + list.get(i).object.toString());
                    options[0] = list.get(i).object;
                    plugin.getTardisHelper().disguise(list.get(i).entityType, p, options);
                    i++;
                    if (i == list.size()) {
                        plugin.getServer().getScheduler().cancelTask(task);
                    }
                }, 5L, 200L);
                return true;
            } else {
                EntityType entityType;
                try {
                    entityType = EntityType.valueOf(args[1]);
                } catch (IllegalArgumentException e) {
                    TARDISMessage.message(sender, "You need to specify a valid living entity type!");
                    return true;
                }
                plugin.getTardisHelper().disguise(entityType, player);
            }
        }
        if (args[0].equalsIgnoreCase("undisguise")) {
            Player player = null;
            if (args.length == 2) {
                player = plugin.getServer().getPlayer(args[1]);
            } else if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                TARDISMessage.message(sender, "You need to specify a player!");
                return true;
            }
            plugin.getTardisHelper().undisguise(player);
        }
        return true;
    }

    class Pair {

        EntityType entityType;
        Object object;

        Pair(EntityType entityType, Object object) {
            this.entityType = entityType;
            this.object = object;
        }
    }
}
