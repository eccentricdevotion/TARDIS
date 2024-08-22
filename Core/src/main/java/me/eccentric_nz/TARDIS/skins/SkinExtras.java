package me.eccentric_nz.TARDIS.skins;

import com.mojang.datafixers.util.Pair;
import org.bukkit.Material;

import java.util.List;

public class SkinExtras {

    public static final Pair<Material, Integer> ACE = new Pair<>(Material.LEATHER, 6);
    public static final Pair<Material, Integer> ANGEL_OF_LIBERTY = new Pair<>(Material.LEATHER, 15); // + 5 torch
    public static final Pair<Material, Integer> BANNAKAFFALATTA = new Pair<>(Material.NETHER_WART, 5);
    public static final Pair<Material, Integer> BRIGADIER_LETHBRIDGE_STEWART = new Pair<>(Material.LEATHER, 5);
    public static final Pair<Material, Integer> CYBERMAN = new Pair<>(Material.IRON_INGOT, 6); // + 7 weapon
    public static final Pair<Material, Integer> CYBERSHADE = new Pair<>(Material.LEATHER, 13);
    public static final Pair<Material, Integer> DALEK_SEC = new Pair<>(Material.MANGROVE_PROPAGULE, 5);
    public static final Pair<Material, Integer> EMPTY_CHILD = new Pair<>(Material.SUGAR, 5);
    public static final Pair<Material, Integer> HATH = new Pair<>(Material.PUFFERFISH, 6);
    public static final Pair<Material, Integer> ICE_WARRIOR = new Pair<>(Material.SNOWBALL, 6);
    public static final Pair<Material, Integer> IMPOSSIBLE_ASTRONAUT = new Pair<>(Material.ORANGE_STAINED_GLASS_PANE, 5);
    public static final Pair<Material, Integer> JENNY_FLINT = new Pair<>(Material.LEATHER, 17); // katana
    public static final Pair<Material, Integer> JO_GRANT = new Pair<>(Material.LEATHER, 7);
    public static final Pair<Material, Integer> JUDOON = new Pair<>(Material.YELLOW_DYE, 5);
    public static final Pair<Material, Integer> MARTHA_JONES = new Pair<>(Material.LEATHER, 8);
    public static final Pair<Material, Integer> MIRE = new Pair<>(Material.NETHERITE_SCRAP, 6); // + 7, 8 arms
    public static final Pair<Material, Integer> OMEGA = new Pair<>(Material.LEATHER, 14);
    public static final Pair<Material, Integer> OOD = new Pair<>(Material.ROTTEN_FLESH, 5);
    public static final Pair<Material, Integer> RACNOSS = new Pair<>(Material.LEATHER, 16);
    public static final Pair<Material, Integer> SCARECROW = new Pair<>(Material.WHEAT, 5);
    public static final Pair<Material, Integer> SEA_DEVIL = new Pair<>(Material.KELP, 5);
    public static final Pair<Material, Integer> SILENCE = new Pair<>(Material.END_STONE, 9);
    public static final Pair<Material, Integer> SILURIAN = new Pair<>(Material.FEATHER, 5);
    public static final Pair<Material, Integer> SLITHEEN = new Pair<>(Material.TURTLE_EGG, 6); // + 7, 8 left, right claws
    public static final Pair<Material, Integer> SONTARAN = new Pair<>(Material.POTATO, 6);
    public static final Pair<Material, Integer> STRAX = new Pair<>(Material.POTATO, 7);
    public static final Pair<Material, Integer> SUTEKH = new Pair<>(Material.LEATHER, 11);
    public static final Pair<Material, Integer> SYCORAX = new Pair<>(Material.LEATHER, 10);
    public static final Pair<Material, Integer> TEGAN = new Pair<>(Material.LEATHER, 9);
    public static final Pair<Material, Integer> THE_BEAST = new Pair<>(Material.LEATHER, 12);
    public static final Pair<Material, Integer> VAMPIRE_OF_VENICE = new Pair<>(Material.COD, 5);
    public static final Pair<Material, Integer> WEEPING_ANGEL = new Pair<>(Material.BRICK, 6);
    public static final Pair<Material, Integer> ZYGON = new Pair<>(Material.PAINTING, 5);

    public static List<Material> MATERIALS = List.of(Material.BRICK, Material.COD, Material.END_STONE, Material.FEATHER, Material.IRON_INGOT, Material.KELP, Material.LEATHER, Material.MANGROVE_PROPAGULE, Material.NETHERITE_SCRAP, Material.NETHER_WART, Material.ORANGE_STAINED_GLASS_PANE, Material.PAINTING, Material.POTATO, Material.PUFFERFISH, Material.ROTTEN_FLESH, Material.SNOWBALL, Material.SUGAR, Material.TORCH, Material.TURTLE_EGG, Material.WHEAT, Material.YELLOW_DYE);

    /*
    public static boolean isSkinHeadPiece(Material material, int cmd) {
        switch (material) {
            case BRICK, IRON_INGOT, NETHERITE_SCRAP, PUFFERFISH, SNOWBALL, TURTLE_EGG -> {
                return cmd == 6;
            }
            case COD, FEATHER, KELP, MANGROVE_PROPAGULE, NETHER_WART, ORANGE_STAINED_GLASS_PANE, PAINTING, ROTTEN_FLESH,
                 SUGAR, WHEAT, YELLOW_DYE -> {
                return cmd == 5;
            }
            case END_STONE -> {
                return cmd == 9;
            }
            case LEATHER -> {
                return cmd > 4 && cmd < 17;
            }
            case POTATO -> {
                return cmd == 7;
            }
            default -> {
                return false;
            }
        }
    }

    public static Skin getByMaterialAndData(Material material, int cmd) {
        switch (cmd) {
            case 5 -> {
                switch (material) {
                    case COD -> {
                        return MonsterSkins.VAMPIRE_OF_VENICE;
                    }
                    case FEATHER -> {
                        return MonsterSkins.SILURIAN;
                    }
                    case KELP -> {
                        return MonsterSkins.SEA_DEVIL;
                    }
                    case MANGROVE_PROPAGULE -> {
                        return CharacterSkins.DALEK_SEC;
                    }
                    case NETHER_WART -> {
                        return CharacterSkins.BANNAKAFFALATTA;
                    }
                    case ORANGE_STAINED_GLASS_PANE -> {
                        return CharacterSkins.IMPOSSIBLE_ASTRONAUT;
                    }
                    case PAINTING -> {
                        return MonsterSkins.ZYGON;
                    }
                    case ROTTEN_FLESH -> {
                        return CharacterSkins.OOD;
                    }
                    case SUGAR -> {
                        return MonsterSkins.EMPTY_CHILD;
                    }
                    case WHEAT -> {
                        return MonsterSkins.SCARECROW;
                    }
                    case YELLOW_DYE -> {
                        return CharacterSkins.JUDOON;
                    }
                    case LEATHER -> {
                        return CharacterSkins.BRIGADIER_LETHBRIDGE_STEWART;
                    }
                }
            }
            case 6 -> {
                switch (material) {
                    case BRICK -> {
                        return MonsterSkins.WEEPING_ANGEL;
                    }
                    case IRON_INGOT -> {
                        return MonsterSkins.CYBERMAN;
                    }
                    case NETHERITE_SCRAP -> {
                        return MonsterSkins.MIRE;
                    }
                    case PUFFERFISH -> {
                        return CharacterSkins.HATH;
                    }
                    case SNOWBALL -> {
                        return MonsterSkins.ICE_WARRIOR;
                    }
                    case TURTLE_EGG -> {
                        return MonsterSkins.SLITHEEN;
                    }
                    case LEATHER -> {
                        return CompanionSkins.ACE;
                    }
                }
            }
            case 7 -> {
                switch (material) {
                    case POTATO -> {
                        return CharacterSkins.STRAX;
                    }
                    case LEATHER -> {
                        return CompanionSkins.JO_GRANT;
                    }
                }
            }
            case 8 -> {
                return (material == Material.LEATHER) ? CompanionSkins.MARTHA_JONES : null;
            }
            case 9 -> {
                switch (material) {
                    case END_STONE -> {
                        return MonsterSkins.SILENCE;
                    }
                    case LEATHER -> {
                        return CompanionSkins.TEGAN_JOVANKA;
                    }
                }
            }
            case 10 -> {
                return (material == Material.LEATHER) ? MonsterSkins.SYCORAX : null;
            }
            case 11 -> {
                return (material == Material.LEATHER) ? MonsterSkins.SUTEKH : null;
            }
            case 12 -> {
                return (material == Material.LEATHER) ? MonsterSkins.THE_BEAST : null;
            }
            case 13 -> {
                return (material == Material.LEATHER) ? MonsterSkins.CYBERSHADE : null;
            }
            case 15 -> {
                return (material == Material.LEATHER) ? MonsterSkins.ANGEL_OF_LIBERTY : null;
            }
            case 16 -> {
                return (material == Material.LEATHER) ? MonsterSkins.RACNOSS : null;
            }
        }
        return null;
    }
     */
}

