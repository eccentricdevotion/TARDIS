/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISGeneticManipulatorDisguiseEvent;
import me.eccentric_nz.TARDIS.api.event.TARDISGeneticManipulatorUndisguiseEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.lazarus.TARDISLazarusDisguise;
import me.eccentric_nz.TARDIS.lazarus.TARDISLazarusLibs;
import me.eccentric_nz.TARDIS.lazarus.TARDISLazarusRunnable;
import me.eccentric_nz.TARDIS.lazarus.disguise.*;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.Locale;
import java.util.UUID;

public class FloodgateGeneticManipulatorForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final Block block;
    private final String path = "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/spawn_eggs/%s.png";

    public FloodgateGeneticManipulatorForm(TARDIS plugin, UUID uuid, Block block) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.block = block;
    }

    public void send() {
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("Genetic Manipulator");
        builder.button("Restore original");
        for (Material m : FloodgateSpawnEgg.disguises) {
            builder.button(m.toString().replace("_SPAWN_EGG", ""), FormImage.Type.URL, String.format(path, m.toString().toLowerCase(Locale.ROOT)));
        }
        builder.button("Master's Reverse Polarity");
        builder.validResultHandler(this::handleResponse);
        builder.closedOrInvalidResultHandler(response -> handleClose());
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = Bukkit.getPlayer(uuid);
        plugin.getTrackerKeeper().getGeneticManipulation().add(uuid);
        // animate the manipulator walls
        TARDISLazarusRunnable runnable = new TARDISLazarusRunnable(plugin, block);
        int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 6L, 6L);
        runnable.setTaskID(taskId);
        TARDISSounds.playTARDISSound(player.getLocation(), "lazarus_machine");
        // open the door
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            openDoor(block);
            untrack(uuid);
        }, 100L);
        String label = response.clickedButton().text();
        switch (label) {
            case "Master's Reverse Polarity" -> {
                if (TARDISPermission.hasPermission(player, "tardis.themaster")) {
                    plugin.getTrackerKeeper().setImmortalityGate(player.getName());
                    if (plugin.isDisguisesOnServer()) {
                        TARDISLazarusLibs.runImmortalityGate(player);
                    } else {
                        TARDISLazarusDisguise.runImmortalityGate(player);
                    }
                    plugin.getMessenger().broadcast(TardisModule.TARDIS, "The Master (aka " + player.getName() + ") has cloned his genetic template to all players. Behold the Master Race!");
                    plugin.getPM().callEvent(new TARDISGeneticManipulatorDisguiseEvent(player, player.getName()));
                    // schedule a delayed task to remove the disguise
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        plugin.getServer().getOnlinePlayers().forEach((p) -> {
                            if (plugin.isDisguisesOnServer()) {
                                TARDISLazarusLibs.removeDisguise(p);
                            } else {
                                TARDISLazarusDisguise.removeDisguise(p);
                            }
                        });
                        plugin.getMessenger().broadcast(TardisModule.TARDIS, "Lord Rassilon has reset the Master Race back to human form.");
                        plugin.getTrackerKeeper().setImmortalityGate("");
                        plugin.getPM().callEvent(new TARDISGeneticManipulatorUndisguiseEvent(player));
                    }, 3600L);
                }
            }
            case "Restore original" -> {
                // undisguise the player
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    if (plugin.isDisguisesOnServer()) {
                        TARDISLazarusLibs.removeDisguise(player);
                    } else {
                        TARDISLazarusDisguise.removeDisguise(player);
                    }
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "GENETICS_RESTORED");
                    plugin.getPM().callEvent(new TARDISGeneticManipulatorUndisguiseEvent(player));
                }, 80L);
            }
            default -> {
                EntityType dt = EntityType.valueOf(label);
                Object[] options = null;
                switch (dt) {
                    case AXOLOTL -> {
                        if (plugin.isDisguisesOnServer()) {
                            new TARDISLazarusLibs(player, label, Axolotl.Variant.LUCY, false, false).createDisguise();
                        } else {
                            options = new Object[]{Axolotl.Variant.WILD, AGE.ADULT};
                        }
                    }
                    case FROG -> {
                        if (plugin.isDisguisesOnServer()) {
                            new TARDISLazarusLibs(player, label, Frog.Variant.TEMPERATE, false, false).createDisguise();
                        } else {
                            options = new Object[]{Frog.Variant.TEMPERATE, AGE.ADULT};
                        }
                    }
                    case CAT -> {
                        if (plugin.isDisguisesOnServer()) {
                            new TARDISLazarusLibs(player, label, Cat.Type.JELLIE, false, false).createDisguise();
                        } else {
                            options = new Object[]{Cat.Type.JELLIE, false, AGE.ADULT};
                        }
                    }
                    case PANDA -> {
                        if (plugin.isDisguisesOnServer()) {
                            new TARDISLazarusLibs(player, label, GENE.NORMAL, false, false).createDisguise();
                        } else {
                            options = new Object[]{GENE.NORMAL, AGE.ADULT};
                        }
                    }
                    case DONKEY, MULE, PIG, OCELOT -> {
                        if (plugin.isDisguisesOnServer()) {
                            new TARDISLazarusLibs(player, label, null, false, false).createDisguise();
                        } else {
                            options = new Object[]{false, AGE.ADULT};
                        }
                    }
                    case PILLAGER, BAT, CREEPER, ENDERMAN, BLAZE -> {
                        if (plugin.isDisguisesOnServer()) {
                            new TARDISLazarusLibs(player, label, null, false, false).createDisguise();
                        } else {
                            options = new Object[]{false};
                        }
                    }
                    case SHEEP, WOLF -> {
                        if (plugin.isDisguisesOnServer()) {
                            new TARDISLazarusLibs(player, label, DyeColor.LIME, false, false).createDisguise();
                        } else {
                            options = new Object[]{DyeColor.LIME, false, AGE.ADULT};
                        }
                    }
                    case HORSE -> {
                        if (plugin.isDisguisesOnServer()) {
                            new TARDISLazarusLibs(player, label, Horse.Color.DARK_BROWN, false, false).createDisguise();
                        } else {
                            options = new Object[]{Horse.Color.DARK_BROWN, AGE.ADULT};
                        }
                    }
                    case LLAMA -> {
                        if (plugin.isDisguisesOnServer()) {
                            new TARDISLazarusLibs(player, label, Llama.Color.BROWN, true, false).createDisguise();
                        } else {
                            options = new Object[]{Llama.Color.BROWN, true, AGE.ADULT};
                        }
                    }
                    case PARROT -> {
                        if (plugin.isDisguisesOnServer()) {
                            new TARDISLazarusLibs(player, label, Parrot.Variant.GREEN, false, false).createDisguise();
                        } else {
                            options = new Object[]{Parrot.Variant.GREEN, AGE.ADULT};
                        }
                    }
                    case RABBIT -> {
                        if (plugin.isDisguisesOnServer()) {
                            new TARDISLazarusLibs(player, label, Rabbit.Type.SALT_AND_PEPPER, false, false).createDisguise();
                        } else {
                            options = new Object[]{Rabbit.Type.SALT_AND_PEPPER, false, AGE.ADULT};
                        }
                    }
                    case VILLAGER, ZOMBIE_VILLAGER -> {
                        if (plugin.isDisguisesOnServer()) {
                            new TARDISLazarusLibs(player, label, Villager.Profession.FARMER, false, false).createDisguise();
                        } else {
                            options = new Object[]{PROFESSION.FARMER, AGE.ADULT};
                        }
                    }
                    case SLIME, MAGMA_CUBE, PUFFERFISH -> {
                        if (plugin.isDisguisesOnServer()) {
                            new TARDISLazarusLibs(player, label, 1, false, false).createDisguise();
                        } else {
                            options = new Object[]{1};
                        }
                    }
                    case COW, TURTLE, ZOMBIE, BEE -> {
                        if (plugin.isDisguisesOnServer()) {
                            new TARDISLazarusLibs(player, label, null, false, false).createDisguise();
                        } else {
                            options = new Object[]{AGE.ADULT};
                        }
                    }
                    case SNOW_GOLEM -> {
                        if (plugin.isDisguisesOnServer()) {
                            new TARDISLazarusLibs(player, label, true, false, false).createDisguise();
                        } else {
                            options = new Object[]{true};
                        }
                    }
                    case TROPICAL_FISH -> {
                        if (plugin.isDisguisesOnServer()) {
                            new TARDISLazarusLibs(player, label, TropicalFish.Pattern.values()[0], false, false).createDisguise();
                        } else {
                            options = new Object[]{TropicalFish.Pattern.values()[0]};
                        }
                    }
                    case MOOSHROOM -> {
                        if (plugin.isDisguisesOnServer()) {
                            new TARDISLazarusLibs(player, label, MushroomCow.Variant.RED, false, false).createDisguise();
                        } else {
                            options = new Object[]{MUSHROOM_COW.getFromMushroomCowType(MushroomCow.Variant.RED), AGE.ADULT};
                        }
                    }
                    case FOX -> {
                        if (plugin.isDisguisesOnServer()) {
                            new TARDISLazarusLibs(player, label, Fox.Type.RED, false, false).createDisguise();
                        } else {
                            options = new Object[]{FOX.getFromFoxType(Fox.Type.RED), AGE.ADULT};
                        }
                    }
                    default -> {
                        if (plugin.isDisguisesOnServer()) {
                            new TARDISLazarusLibs(player, label, null, false, false).createDisguise();
                        }
                    }
                }
                if (!plugin.isDisguisesOnServer()) {
                    new TARDISLazarusDisguise(plugin, player, dt, options).createDisguise();
                }
            }
        }
    }

    private void handleClose() {
        openDoor(block);
        untrack(uuid);
    }

    private void untrack(UUID uuid) {
        // stop tracking player
        plugin.getTrackerKeeper().getLazarus().remove(uuid);
        plugin.getTrackerKeeper().getGeneticManipulation().remove(uuid);
        plugin.getTrackerKeeper().getGeneticallyModified().remove(uuid);
    }

    private void openDoor(Block block) {
        block.getRelative(BlockFace.SOUTH).setType(Material.AIR);
        block.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).setType(Material.AIR);
    }
}
